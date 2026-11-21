import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../services/api_service.dart';
import '../models/activity.dart';

class FeedScreen extends StatefulWidget {
  final VoidCallback? onDiscoverTap;

  const FeedScreen({super.key, this.onDiscoverTap});

  @override
  State<FeedScreen> createState() => _FeedScreenState();
}

class _FeedScreenState extends State<FeedScreen> {
  final ApiService _apiService = ApiService();
  List<Activity> _feedActivities = [];
  bool _isLoading = true;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    _loadFeed();
  }

  Future<void> _loadFeed() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      final feed = await _apiService.getFriendsFeed();
      if (mounted) {
        setState(() {
          _feedActivities = feed;
          _isLoading = false;
        });
      }
    } catch (e) {
      if (mounted) {
        setState(() {
          _errorMessage = "Failed to load feed";
          _isLoading = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_isLoading) {
      return const Center(child: CircularProgressIndicator());
    }

    if (_errorMessage != null) {
      return Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.error_outline, size: 48, color: Colors.grey),
            const SizedBox(height: 12),
            Text(_errorMessage!, style: const TextStyle(fontSize: 16, color: Colors.grey)),
            const SizedBox(height: 12),
            ElevatedButton(
              onPressed: _loadFeed,
              child: const Text("Retry"),
            ),
          ],
        ),
      );
    }

    if (_feedActivities.isEmpty) {
      return RefreshIndicator(
        onRefresh: _loadFeed,
        child: ListView(
          padding: const EdgeInsets.all(32),
          children: [
            const SizedBox(height: 60),
            const Icon(Icons.people_outline, size: 70, color: Colors.grey),
            const SizedBox(height: 16),
            const Text(
              "No friend activities yet!",
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
                color: Colors.grey,
              ),
            ),
            const SizedBox(height: 8),
            const Text(
              "Follow other runners to see their runs appear in your live feed.",
              textAlign: TextAlign.center,
              style: TextStyle(color: Colors.grey),
            ),
            const SizedBox(height: 24),
            if (widget.onDiscoverTap != null)
              Center(
                child: ElevatedButton.icon(
                  onPressed: widget.onDiscoverTap,
                  icon: const Icon(Icons.person_search),
                  label: const Text("Discover Runners"),
                ),
              ),
          ],
        ),
      );
    }

    return RefreshIndicator(
      onRefresh: _loadFeed,
      child: ListView.builder(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        itemCount: _feedActivities.length,
        itemBuilder: (context, index) {
          final activity = _feedActivities[index];
          final formattedDate = DateFormat('MMM dd, yyyy').format(activity.date);
          final runnerName = activity.runnerUsername ?? "Friend";

          return Card(
            elevation: 2,
            margin: const EdgeInsets.symmetric(vertical: 8),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
            ),
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    children: [
                      CircleAvatar(
                        backgroundColor: Colors.blue.shade100,
                        child: Text(
                          runnerName.isNotEmpty ? runnerName[0].toUpperCase() : "R",
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            color: Colors.blue.shade800,
                          ),
                        ),
                      ),
                      const SizedBox(width: 12),
                      Expanded(
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              runnerName,
                              style: const TextStyle(
                                fontWeight: FontWeight.bold,
                                fontSize: 16,
                              ),
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
                      ),
                      Container(
                        padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 4),
                        decoration: BoxDecoration(
                          color: Colors.blue.shade50,
                          borderRadius: BorderRadius.circular(12),
                        ),
                        child: Text(
                          "${activity.distanceKm} km",
                          style: TextStyle(
                            fontWeight: FontWeight.bold,
                            color: Colors.blue.shade700,
                          ),
                        ),
                      ),
                    ],
                  ),
                  const Divider(height: 24),
                  Text(
                    activity.route != null && activity.route!.isNotEmpty
                        ? activity.route!
                        : "Completed a run",
                    style: const TextStyle(
                      fontSize: 15,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                  const SizedBox(height: 12),
                  Row(
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    children: [
                      _buildMetric(
                        icon: Icons.timer_outlined,
                        label: "Duration",
                        value: activity.durationFormatted,
                      ),
                      _buildMetric(
                        icon: Icons.speed,
                        label: "Pace",
                        value: activity.paceFormatted,
                      ),
                      _buildMetric(
                        icon: Icons.local_fire_department_outlined,
                        label: "Calories",
                        value: "${activity.calories} kcal",
                        iconColor: Colors.orange,
                      ),
                    ],
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }

  Widget _buildMetric({
    required IconData icon,
    required String label,
    required String value,
    Color iconColor = Colors.blue,
  }) {
    return Column(
      children: [
        Row(
          mainAxisSize: MainAxisSize.min,
          children: [
            Icon(icon, size: 16, color: iconColor),
            const SizedBox(width: 4),
            Text(
              label,
              style: TextStyle(fontSize: 12, color: Colors.grey.shade600),
            ),
          ],
        ),
        const SizedBox(height: 4),
        Text(
          value,
          style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
        ),
      ],
    );
  }
}
