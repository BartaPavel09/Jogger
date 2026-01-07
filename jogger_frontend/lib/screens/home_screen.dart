import 'package:flutter/material.dart';
import '../services/api_service.dart';
import '../models/user.dart';
import '../models/activity.dart';
import 'add_activity_screen.dart';
import 'profile_screen.dart';
import '../widgets/activity_chart.dart';
import '../widgets/calories_chart.dart';
import '../widgets/weekly_chart.dart';
import '../models/chart_data.dart';
import 'package:intl/intl.dart';

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

        activities.sort((a, b) => b.date.compareTo(a.date));

        if (mounted) {
          setState(() {
            _currentUser = user;
            _activities = activities;
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

  Future<void> _navigateToAdd() async {
    final result = await Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => const AddActivityScreen()),
    );
    if (result == true) _loadData();
  }

  void _navigateToProfile() {
    if (_currentUser != null) {
      Navigator.push(
        context,
        MaterialPageRoute(builder: (_) => ProfileScreen(user: _currentUser!)),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Jogger"),
        elevation: 0,
        actions: [
          IconButton(
            icon: const Icon(Icons.add_circle_outline, size: 28),
            tooltip: 'Add Activity',
            onPressed: _navigateToAdd,
          ),
          const SizedBox(width: 8),

          GestureDetector(
            onTap: _navigateToProfile,
            child: CircleAvatar(
              radius: 18,
              backgroundColor: Colors.white,
              child: Text(
                _currentUser?.username.substring(0, 1).toUpperCase() ?? "U",
                style: const TextStyle(
                  fontWeight: FontWeight.bold,
                  color: Colors.blue,
                ),
              ),
            ),
          ),
          const SizedBox(width: 16),
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
                    const Text(
                      "Your Progress",
                      style: TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                        color: Colors.grey,
                      ),
                    ),
                    const SizedBox(height: 10),
                    AspectRatio(
                      aspectRatio: 1.5,
                      child: PageView(
                        children: [
                          ActivityChart(activities: _activities),
                          CaloriesChart(activities: _activities),
                          WeeklyChart(weeklyData: _weeklyData),
                        ],
                      ),
                    ),
                    const SizedBox(height: 25),
                  ],

                  const Text(
                    "Recent Runs",
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 10),

                  if (_activities.isEmpty)
                    const Center(
                      child: Padding(
                        padding: EdgeInsets.all(40),
                        child: Column(
                          children: [
                            Icon(
                              Icons.directions_run,
                              size: 60,
                              color: Colors.grey,
                            ),
                            SizedBox(height: 10),
                            Text(
                              "No runs yet!",
                              style: TextStyle(
                                fontSize: 18,
                                fontWeight: FontWeight.bold,
                                color: Colors.grey,
                              ),
                            ),
                            Text(
                              "Tap the + icon above to start.",
                              style: TextStyle(color: Colors.grey),
                            ),
                          ],
                        ),
                      ),
                    )
                  else
                    ..._activities.map((activity) {
                      String formattedDate = DateFormat(
                        'MMM dd, yyyy',
                      ).format(activity.date);
                      return Card(
                        elevation: 2,
                        margin: const EdgeInsets.symmetric(vertical: 8),
                        child: ListTile(
                          leading: CircleAvatar(
                            backgroundColor: Colors.blue.shade50,
                            child: Icon(
                              Icons.directions_run,
                              color: Colors.blue,
                            ),
                          ),
                          title: Text(
                            activity.route ?? "Run",
                            style: const TextStyle(fontWeight: FontWeight.bold),
                          ),
                          subtitle: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                "${activity.distanceKm} km • ${activity.durationSec ~/ 60} min • ${activity.calories} kcal",
                              ),
                              Text(
                                formattedDate,
                                style: TextStyle(
                                  color: Colors.grey.shade600,
                                  fontSize: 12,
                                ),
                              ),
                            ],
                          ),
                          trailing: Row(
                            mainAxisSize: MainAxisSize.min,
                            children: [
                              IconButton(
                                icon: const Icon(
                                  Icons.edit,
                                  size: 20,
                                  color: Colors.grey,
                                ),
                                onPressed: () => _editActivity(activity),
                              ),
                              IconButton(
                                icon: const Icon(
                                  Icons.delete,
                                  size: 20,
                                  color: Colors.redAccent,
                                ),
                                onPressed: () => _deleteActivity(activity.id),
                              ),
                            ],
                          ),
                        ),
                      );
                    }),

                  const SizedBox(height: 40),
                ],
              ),
            ),
    );
  }
}
