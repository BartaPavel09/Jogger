import 'package:flutter/material.dart';
import '../models/leaderboard_entry.dart';
import '../services/api_service.dart';

class LeaderboardScreen extends StatefulWidget {
  final int? currentUserId;

  const LeaderboardScreen({super.key, this.currentUserId});

  @override
  State<LeaderboardScreen> createState() => _LeaderboardScreenState();
}

class _LeaderboardScreenState extends State<LeaderboardScreen> {
  final ApiService _apiService = ApiService();
  List<LeaderboardEntry> _entries = [];
  bool _isLoading = true;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    _loadLeaderboard();
  }

  Future<void> _loadLeaderboard() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      final data = await _apiService.getLeaderboard();
      if (mounted) {
        setState(() {
          _entries = data;
          _isLoading = false;
        });
      }
    } catch (e) {
      if (mounted) {
        setState(() {
          _errorMessage = "Failed to load leaderboard";
          _isLoading = false;
        });
      }
    }
  }

  Widget _buildRankBadge(int rank) {
    if (rank == 1) {
      return Container(
        width: 36,
        height: 36,
        decoration: BoxDecoration(
          color: Colors.amber.shade400,
          shape: BoxShape.circle,
        ),
        child: const Center(
          child: Text(
            "🥇",
            style: TextStyle(fontSize: 18),
          ),
        ),
      );
    } else if (rank == 2) {
      return Container(
        width: 36,
        height: 36,
        decoration: BoxDecoration(
          color: Colors.grey.shade300,
          shape: BoxShape.circle,
        ),
        child: const Center(
          child: Text(
            "🥈",
            style: TextStyle(fontSize: 18),
          ),
        ),
      );
    } else if (rank == 3) {
      return Container(
        width: 36,
        height: 36,
        decoration: BoxDecoration(
          color: Colors.orange.shade200,
          shape: BoxShape.circle,
        ),
        child: const Center(
          child: Text(
            "🥉",
            style: TextStyle(fontSize: 18),
          ),
        ),
      );
    } else {
      return Container(
        width: 36,
        height: 36,
        decoration: BoxDecoration(
          color: Colors.grey.shade100,
          shape: BoxShape.circle,
        ),
        child: Center(
          child: Text(
            "#$rank",
            style: TextStyle(
              fontWeight: FontWeight.bold,
              color: Colors.grey.shade700,
              fontSize: 13,
            ),
          ),
        ),
      );
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
              onPressed: _loadLeaderboard,
              child: const Text("Retry"),
            ),
          ],
        ),
      );
    }

    if (_entries.isEmpty) {
      return RefreshIndicator(
        onRefresh: _loadLeaderboard,
        child: ListView(
          padding: const EdgeInsets.all(32),
          children: const [
            SizedBox(height: 80),
            Icon(Icons.leaderboard_outlined, size: 70, color: Colors.grey),
            SizedBox(height: 16),
            Text(
              "No runners ranked yet!",
              textAlign: TextAlign.center,
              style: TextStyle(
                fontSize: 20,
                fontWeight: FontWeight.bold,
                color: Colors.grey,
              ),
            ),
            SizedBox(height: 8),
            Text(
              "Start logging activities to appear on the global leaderboard.",
              textAlign: TextAlign.center,
              style: TextStyle(color: Colors.grey),
            ),
          ],
        ),
      );
    }

    return RefreshIndicator(
      onRefresh: _loadLeaderboard,
      child: ListView.builder(
        padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
        itemCount: _entries.length,
        itemBuilder: (context, index) {
          final entry = _entries[index];
          final isCurrentUser = widget.currentUserId != null &&
              entry.runnerId == widget.currentUserId;

          return Card(
            elevation: entry.rank <= 3 ? 3 : 1,
            margin: const EdgeInsets.symmetric(vertical: 6),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
              side: isCurrentUser
                  ? const BorderSide(color: Colors.blue, width: 2)
                  : BorderSide.none,
            ),
            color: isCurrentUser
                ? Colors.blue.shade50.withAlpha(120)
                : Colors.white,
            child: ListTile(
              contentPadding: const EdgeInsets.symmetric(
                horizontal: 16,
                vertical: 8,
              ),
              leading: _buildRankBadge(entry.rank),
              title: Row(
                children: [
                  Flexible(
                    child: Text(
                      entry.username,
                      overflow: TextOverflow.ellipsis,
                      style: TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 16,
                        color: isCurrentUser ? Colors.blue.shade900 : Colors.black87,
                      ),
                    ),
                  ),
                  if (isCurrentUser) ...[
                    const SizedBox(width: 8),
                    Container(
                      padding: const EdgeInsets.symmetric(
                        horizontal: 8,
                        vertical: 2,
                      ),
                      decoration: BoxDecoration(
                        color: Colors.blue,
                        borderRadius: BorderRadius.circular(10),
                      ),
                      child: const Text(
                        "You",
                        style: TextStyle(
                          color: Colors.white,
                          fontSize: 10,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                    ),
                  ],
                ],
              ),
              subtitle: Text(
                "${entry.totalRuns} ${entry.totalRuns == 1 ? 'activity' : 'activities'}",
                style: TextStyle(
                  color: Colors.grey.shade600,
                  fontSize: 13,
                ),
              ),
              trailing: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.end,
                children: [
                  Text(
                    "${entry.totalDistanceKm.toStringAsFixed(1)} km",
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                      fontSize: 17,
                      color: entry.rank == 1
                          ? Colors.amber.shade900
                          : Colors.blue.shade800,
                    ),
                  ),
                  Text(
                    "total distance",
                    style: TextStyle(
                      color: Colors.grey.shade500,
                      fontSize: 11,
                    ),
                  ),
                ],
              ),
            ),
          );
        },
      ),
    );
  }
}
