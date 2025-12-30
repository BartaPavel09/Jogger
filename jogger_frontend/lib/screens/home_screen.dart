import 'package:flutter/material.dart';
import '../services/api_service.dart';
import '../models/user.dart';
import '../models/activity.dart';
import 'add_activity_screen.dart';
import 'login_screen.dart';
import 'profile_screen.dart';
import '../widgets/activity_chart.dart';
import '../widgets/calories_chart.dart';
import '../widgets/weekly_chart.dart';
import '../models/chart_data.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  final ApiService _apiService = ApiService();
  User? _currentUser;
  List<Activity> _activities = [];
  List<ChartData> _weeklyData = [];
  bool _isLoading = true;

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    setState(() => _isLoading = true);
    try {
      final user = await _apiService.getCurrentUser();
      if (user != null) {
        final activities = await _apiService.getActivities(user.id);
        final weekly = await _apiService.getWeeklyRuns(user.id);

        if (mounted) {
          setState(() {
            _currentUser = user;
            _activities = activities.reversed.toList();
            _weeklyData = weekly;
            _isLoading = false;
          });
        }
        _checkNewBadges(user.id);
      }
    } catch (e) {
      setState(() => _isLoading = false);
      print("Error loading data: $e");
    }
  }

  Future<void> _checkNewBadges(int userId) async {
    try {
      final badges = await _apiService.getBadges(userId);

      final newBadges = badges.where((b) => !b.isSeen).toList();

      if (newBadges.isNotEmpty && mounted) {
        for (var badge in newBadges) {
          await showDialog(
            context: context,
            builder: (_) => AlertDialog(
              title: const Text("🎉 Congratulations! 🎉"),
              content: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  const Icon(Icons.emoji_events, size: 50, color: Colors.amber),
                  const SizedBox(height: 10),
                  Text(
                    badge.title,
                    style: const TextStyle(fontWeight: FontWeight.bold),
                  ),
                  Text(badge.description),
                ],
              ),
              actions: [
                TextButton(
                  onPressed: () => Navigator.pop(context),
                  child: const Text("Awesome!"),
                ),
              ],
            ),
          );
        }

        await _apiService.markBadgesAsSeen(userId);
      }
    } catch (e) {
      print("Error checking badges: $e");
    }
  }

  Future<void> _deleteActivity(int id) async {
    final confirm = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Delete Activity?"),
        content: const Text("This action cannot be undone."),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx, false),
            child: const Text("Cancel"),
          ),
          TextButton(
            onPressed: () => Navigator.pop(ctx, true),
            child: const Text("Delete", style: TextStyle(color: Colors.red)),
          ),
        ],
      ),
    );

    if (confirm == true) {
      await _apiService.deleteActivity(id);
      _loadData();
    }
  }

  Future<void> _editActivity(Activity activity) async {
    final distanceController = TextEditingController(
      text: activity.distanceKm.toString(),
    );
    final durationController = TextEditingController(
      text: (activity.durationSec / 60).toStringAsFixed(0),
    );
    final routeController = TextEditingController(text: activity.route ?? "");

    await showDialog(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text("Edit Activity"),
        content: SingleChildScrollView(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: distanceController,
                decoration: const InputDecoration(labelText: "Distance (km)"),
                keyboardType: TextInputType.number,
              ),
              const SizedBox(height: 10),
              TextField(
                controller: durationController,
                decoration: const InputDecoration(labelText: "Duration (min)"),
                keyboardType: TextInputType.number,
              ),
              const SizedBox(height: 10),
              TextField(
                controller: routeController,
                decoration: const InputDecoration(labelText: "Route Name"),
              ),
            ],
          ),
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(ctx),
            child: const Text("Cancel"),
          ),
          ElevatedButton(
            onPressed: () async {
              try {
                final dist = double.parse(distanceController.text);
                final durMin = int.parse(durationController.text);
                final route = routeController.text;

                await _apiService.updateActivity(
                  activity.id,
                  dist,
                  durMin * 60,
                  route,
                );

                if (mounted) {
                  Navigator.pop(ctx);
                  _loadData();
                  ScaffoldMessenger.of(context).showSnackBar(
                    const SnackBar(content: Text("Activity updated!")),
                  );
                }
              } catch (e) {
                ScaffoldMessenger.of(
                  context,
                ).showSnackBar(const SnackBar(content: Text("Invalid input")));
              }
            },
            child: const Text("Save"),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Welcome, ${_currentUser?.username ?? 'Runner'}"),
        backgroundColor: Color(0xFF1976D2),
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: () async {
              await _apiService.logout();
              if (mounted) {
                Navigator.pushReplacement(
                  context,
                  MaterialPageRoute(builder: (_) => const LoginScreen()),
                );
              }
            },
          ),
        ],
      ),
      body: _isLoading
          ? const Center(child: CircularProgressIndicator())
          : RefreshIndicator(
              onRefresh: _loadData,
              child: ListView(
                padding: const EdgeInsets.all(16),
                children: [
                  if (_activities.isNotEmpty) ...[
                    const Padding(
                      padding: EdgeInsets.symmetric(horizontal: 8.0),
                      child: Text(
                        "Performance Charts (Swipe)",
                        style: TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                    const SizedBox(height: 10),

                    AspectRatio(
                      aspectRatio: 1.1,
                      child: PageView(
                        children: [
                          ActivityChart(activities: _activities),
                          CaloriesChart(activities: _activities),
                          WeeklyChart(weeklyData: _weeklyData),
                        ],
                      ),
                    ),
                    const SizedBox(height: 20),
                  ],

                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      const Text(
                        "Recent Activities",
                        style: TextStyle(
                          fontSize: 18,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      ElevatedButton.icon(
                        icon: const Icon(Icons.person),
                        label: const Text("Profile"),
                        onPressed: () {
                          if (_currentUser != null) {
                            Navigator.push(
                              context,
                              MaterialPageRoute(
                                builder: (_) =>
                                    ProfileScreen(user: _currentUser!),
                              ),
                            );
                          }
                        },
                      ),
                    ],
                  ),
                  const SizedBox(height: 10),

                  if (_activities.isEmpty)
                    const Center(
                      child: Padding(
                        padding: EdgeInsets.all(20),
                        child: Text("No activities yet. Go for a run!"),
                      ),
                    )
                  else
                    ..._activities.map(
                      (activity) => Card(
                        margin: const EdgeInsets.symmetric(vertical: 8),
                        child: ListTile(
                          leading: CircleAvatar(
                            backgroundColor: Colors.blueAccent,
                            child: Text(
                              activity.distanceKm.toStringAsFixed(1),
                              style: const TextStyle(
                                color: Colors.white,
                                fontSize: 12,
                              ),
                            ),
                          ),
                          title: Text(activity.route ?? "Run"),
                          subtitle: Text(
                            "${activity.date.day}/${activity.date.month} • ${activity.durationSec ~/ 60} min • ${activity.calories} kcal",
                          ),
                          trailing: Row(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              IconButton(
                                icon: const Icon(
                                  Icons.edit,
                                  color: Colors.blue,
                                ),
                                onPressed: () => _editActivity(activity),
                              ),

                              IconButton(
                                icon: const Icon(
                                  Icons.delete,
                                  color: Colors.red,
                                ),
                                onPressed: () => _deleteActivity(activity.id),
                              ),
                            ],
                          ),
                        ),
                      ),
                    ),
                ],
              ),
            ),
      floatingActionButton: FloatingActionButton(
        onPressed: () async {
          final result = await Navigator.push(
            context,
            MaterialPageRoute(builder: (_) => const AddActivityScreen()),
          );
          if (result == true) _loadData();
        },
        child: const Icon(Icons.add),
      ),
    );
  }
}
