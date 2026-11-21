import 'package:flutter_test/flutter_test.dart';
import 'package:jogger_frontend/models/activity.dart';
import 'package:jogger_frontend/models/search_user.dart';
import 'package:jogger_frontend/models/leaderboard_entry.dart';

void main() {
  group('Model Serialization Tests', () {
    test('Activity deserializes correctly with runner info', () {
      final json = {
        'id': 1,
        'distanceKm': 5.25,
        'durationSec': 1800,
        'calories': 350,
        'date': '2026-11-15',
        'route': 'Morning Run',
        'pace': 5.71,
        'runnerId': 42,
        'runnerUsername': 'runner_john',
      };

      final activity = Activity.fromJson(json);

      assertEquals(activity.id, 1);
      assertEquals(activity.distanceKm, 5.25);
      assertEquals(activity.durationSec, 1800);
      assertEquals(activity.calories, 350);
      assertEquals(activity.route, 'Morning Run');
      assertEquals(activity.runnerId, 42);
      assertEquals(activity.runnerUsername, 'runner_john');
      assertEquals(activity.durationFormatted, '30 min');
      assertEquals(activity.paceFormatted, '5.71 min/km');
    });

    test('SearchUser deserializes correctly', () {
      final json = {
        'id': 7,
        'username': 'speedy',
        'email': 'speedy@run.com',
        'role': 'USER',
        'following': true,
      };

      final user = SearchUser.fromJson(json);

      assertEquals(user.id, 7);
      assertEquals(user.username, 'speedy');
      assertEquals(user.email, 'speedy@run.com');
      assertEquals(user.isFollowing, true);
    });

    test('LeaderboardEntry deserializes correctly', () {
      final json = {
        'rank': 1,
        'runnerId': 10,
        'username': 'champion',
        'totalDistanceKm': 150.5,
        'totalRuns': 25,
      };

      final entry = LeaderboardEntry.fromJson(json);

      assertEquals(entry.rank, 1);
      assertEquals(entry.runnerId, 10);
      assertEquals(entry.username, 'champion');
      assertEquals(entry.totalDistanceKm, 150.5);
      assertEquals(entry.totalRuns, 25);
    });
  });
}

void assertEquals(dynamic actual, dynamic expected) {
  expect(actual, expected);
}
