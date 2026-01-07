import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import '../models/user.dart';
import '../models/activity.dart';
import '../models/chart_data.dart';
import '../models/badge_model.dart';

class ApiService {
  static const String baseUrl = 'http://localhost:8080';

  Future<bool> login(String username, String password) async {
    final url = Uri.parse('$baseUrl/auth/login');
    final response = await http.post(
      url,
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'username': username, 'password': password}),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final token = data['token'];
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString('token', token);
      return true;
    } else {
      return false;
    }
  }

  Future<String?> register(
    String username,
    String email,
    String password,
  ) async {
    final url = Uri.parse('$baseUrl/auth/register');
    final response = await http.post(
      url,
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'username': username,
        'email': email,
        'password': password,
      }),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final token = data['token'];
      final prefs = await SharedPreferences.getInstance();
      await prefs.setString('token', token);
      return null;
    } else {
      try {
        final errorData = jsonDecode(response.body);

        if (errorData['errors'] != null) {
          Map<String, dynamic> errors = errorData['errors'];

          String cleanMessage = "";
          errors.forEach((field, message) {
            String fieldName = field[0].toUpperCase() + field.substring(1);
            cleanMessage += "$fieldName: $message\n";
          });

          return cleanMessage.trim();
        }
        return errorData['message'] ?? 'Registration failed';
      } catch (_) {
        return 'Registration failed (${response.statusCode})';
      }
    }
  }

  Future<void> logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('token');
  }

  Future<String?> _getToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('token');
  }

  Future<Map<String, String>> _getHeaders() async {
    final token = await _getToken();
    return {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer $token',
    };
  }

  Future<User?> getCurrentUser() async {
    final url = Uri.parse('$baseUrl/runners/me');
    final headers = await _getHeaders();
    try {
      final response = await http.get(url, headers: headers);
      if (response.statusCode == 200) {
        return User.fromJson(jsonDecode(response.body));
      }
    } catch (e) {
      print("Error fetching user: $e");
    }
    return null;
  }

  Future<List<Activity>> getActivities(int runnerId) async {
    final url = Uri.parse('$baseUrl/runners/$runnerId/activities');
    final headers = await _getHeaders();

    final response = await http.get(url, headers: headers);

    if (response.statusCode == 200) {
      final List<dynamic> body = jsonDecode(response.body);
      return body.map((json) => Activity.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load activities');
    }
  }

  Future<void> addActivity(
    int runnerId,
    double distance,
    int durationSec,
    DateTime date,
    String? route,
  ) async {
    final url = Uri.parse('$baseUrl/runners/$runnerId/activities');
    final headers = await _getHeaders();

    final body = {
      'distanceKm': distance,
      'durationSec': durationSec,
      'date': date.toIso8601String().split('T')[0],
      'route': route ?? "Park Run",
    };

    final response = await http.post(
      url,
      headers: headers,
      body: jsonEncode(body),
    );

    if (response.statusCode != 200) {
      throw Exception('Failed to add activity: ${response.body}');
    }
  }

  Future<List<ChartData>> getStats(int runnerId) async {
    final url = Uri.parse('$baseUrl/runners/$runnerId/charts');
    final headers = await _getHeaders();

    final response = await http.get(url, headers: headers);

    if (response.statusCode == 200) {
      final List<dynamic> body = jsonDecode(response.body);
      return body.map((json) => ChartData.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load stats');
    }
  }

  Future<bool> updateProfile(int userId, String email, double weight) async {
    final url = Uri.parse('$baseUrl/runners/$userId');
    final headers = await _getHeaders();

    final body = {'email': email, 'weight': weight};

    final response = await http.put(
      url,
      headers: headers,
      body: jsonEncode(body),
    );

    return response.statusCode == 200;
  }

  Future<List<BadgeModel>> getBadges(int userId) async {
    final url = Uri.parse('$baseUrl/runners/$userId/badges');

    final headers = await _getHeaders();
    final response = await http.get(url, headers: headers);

    if (response.statusCode == 200) {
      final List<dynamic> body = jsonDecode(response.body);
      return body.map((json) => BadgeModel.fromJson(json)).toList();
    } else {
      return [];
    }
  }

  Future<String> getReportUrl(int userId) async {
    final token = await _getToken();
    return '$baseUrl/runners/$userId/reports/progress?token=$token';
  }

  Future<bool> deleteActivity(int activityId) async {
    final url = Uri.parse('$baseUrl/activities/$activityId');
    final headers = await _getHeaders();

    final response = await http.delete(url, headers: headers);

    return response.statusCode == 204 || response.statusCode == 200;
  }

  Future<void> markBadgesAsSeen(int userId) async {
    final url = Uri.parse('$baseUrl/runners/$userId/badges/seen');
    final headers = await _getHeaders();

    await http.post(url, headers: headers);
  }

  Future<List<User>> getAllRunners() async {
    final url = Uri.parse('$baseUrl/runners');
    final headers = await _getHeaders();
    final response = await http.get(url, headers: headers);

    if (response.statusCode == 200) {
      final List<dynamic> data = jsonDecode(response.body);
      return data.map((json) => User.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load runners');
    }
  }

  Future<void> updateActivity(
    int activityId,
    double distance,
    int durationSec,
    String route,
  ) async {
    final url = Uri.parse('$baseUrl/activities/$activityId');
    final headers = await _getHeaders();

    final body = jsonEncode({
      'distanceKm': distance,
      'durationSec': durationSec,
      'route': route,
    });

    final response = await http.put(url, headers: headers, body: body);

    if (response.statusCode != 200) {
      throw Exception('Failed to update activity: ${response.body}');
    }
  }

  Future<void> deleteAccount(int userId) async {
    final url = Uri.parse('$baseUrl/runners/$userId');
    final headers = await _getHeaders();

    final response = await http.delete(url, headers: headers);

    if (response.statusCode != 204 && response.statusCode != 200) {
      throw Exception('Failed to delete account');
    }
  }

  Future<List<ChartData>> getWeeklyRuns(int runnerId) async {
    final url = Uri.parse('$baseUrl/runners/$runnerId/charts/weekly');
    final headers = await _getHeaders();

    final response = await http.get(url, headers: headers);

    if (response.statusCode == 200) {
      final List<dynamic> body = jsonDecode(response.body);
      return body.map((json) => ChartData.fromJson(json)).toList();
    } else {
      throw Exception('Failed to load weekly stats');
    }
  }
}
