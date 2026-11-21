import 'package:flutter/material.dart';
import '../models/search_user.dart';
import '../services/api_service.dart';

class DiscoverScreen extends StatefulWidget {
  final VoidCallback? onFollowChanged;

  const DiscoverScreen({super.key, this.onFollowChanged});

  @override
  State<DiscoverScreen> createState() => _DiscoverScreenState();
}

class _DiscoverScreenState extends State<DiscoverScreen> {
  final ApiService _apiService = ApiService();
  final TextEditingController _searchController = TextEditingController();
  List<SearchUser> _runners = [];
  bool _isLoading = true;
  final Set<int> _pendingIds = {};

  @override
  void initState() {
    super.initState();
    _loadRunners();
  }

  @override
  void dispose() {
    _searchController.dispose();
    super.dispose();
  }

  Future<void> _loadRunners([String? query]) async {
    setState(() => _isLoading = true);
    try {
      final results = await _apiService.searchRunners(query);
      if (mounted) {
        setState(() {
          _runners = results;
          _isLoading = false;
        });
      }
    } catch (e) {
      if (mounted) {
        setState(() => _isLoading = false);
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text("Failed to search runners")),
        );
      }
    }
  }

  Future<void> _toggleFollow(SearchUser user) async {
    final originalState = user.isFollowing;
    setState(() {
      _pendingIds.add(user.id);
      user.isFollowing = !originalState;
    });

    bool success;
    try {
      if (originalState) {
        success = await _apiService.unfollowUser(user.id);
      } else {
        success = await _apiService.followUser(user.id);
      }
    } catch (_) {
      success = false;
    }

    if (mounted) {
      setState(() {
        _pendingIds.remove(user.id);
        if (!success) {
          user.isFollowing = originalState;
          ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(
              content: Text(
                originalState
                    ? "Failed to unfollow ${user.username}"
                    : "Failed to follow ${user.username}",
              ),
            ),
          );
        } else {
          widget.onFollowChanged?.call();
        }
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.fromLTRB(16, 12, 16, 8),
          child: TextField(
            controller: _searchController,
            onChanged: (val) => _loadRunners(val),
            decoration: InputDecoration(
              hintText: "Search runners by username...",
              prefixIcon: const Icon(Icons.search),
              suffixIcon: _searchController.text.isNotEmpty
                  ? IconButton(
                      icon: const Icon(Icons.clear),
                      onPressed: () {
                        _searchController.clear();
                        _loadRunners();
                      },
                    )
                  : null,
              contentPadding: const EdgeInsets.symmetric(horizontal: 16),
              border: OutlineInputBorder(
                borderRadius: BorderRadius.circular(12),
              ),
              filled: true,
              fillColor: Colors.grey.shade100,
            ),
          ),
        ),
        Expanded(
          child: _isLoading
              ? const Center(child: CircularProgressIndicator())
              : _runners.isEmpty
                  ? RefreshIndicator(
                      onRefresh: () => _loadRunners(_searchController.text),
                      child: ListView(
                        children: const [
                          SizedBox(height: 80),
                          Icon(Icons.search_off, size: 60, color: Colors.grey),
                          SizedBox(height: 12),
                          Center(
                            child: Text(
                              "No runners found",
                              style: TextStyle(
                                fontSize: 18,
                                fontWeight: FontWeight.bold,
                                color: Colors.grey,
                              ),
                            ),
                          ),
                        ],
                      ),
                    )
                  : RefreshIndicator(
                      onRefresh: () => _loadRunners(_searchController.text),
                      child: ListView.builder(
                        padding: const EdgeInsets.symmetric(
                          horizontal: 16,
                          vertical: 8,
                        ),
                        itemCount: _runners.length,
                        itemBuilder: (context, index) {
                          final user = _runners[index];
                          final isPending = _pendingIds.contains(user.id);

                          return Card(
                            elevation: 1,
                            margin: const EdgeInsets.symmetric(vertical: 6),
                            shape: RoundedRectangleBorder(
                              borderRadius: BorderRadius.circular(12),
                            ),
                            child: ListTile(
                              contentPadding: const EdgeInsets.symmetric(
                                horizontal: 16,
                                vertical: 6,
                              ),
                              leading: CircleAvatar(
                                backgroundColor: Colors.blue.shade100,
                                child: Text(
                                  user.username.isNotEmpty
                                      ? user.username[0].toUpperCase()
                                      : "U",
                                  style: TextStyle(
                                    fontWeight: FontWeight.bold,
                                    color: Colors.blue.shade800,
                                  ),
                                ),
                              ),
                              title: Text(
                                user.username,
                                style: const TextStyle(
                                  fontWeight: FontWeight.bold,
                                  fontSize: 16,
                                ),
                              ),
                              subtitle: Text(
                                user.email,
                                style: TextStyle(
                                  color: Colors.grey.shade600,
                                  fontSize: 13,
                                ),
                              ),
                              trailing: SizedBox(
                                width: 110,
                                child: isPending
                                    ? const Center(
                                        child: SizedBox(
                                          width: 20,
                                          height: 20,
                                          child: CircularProgressIndicator(
                                            strokeWidth: 2,
                                          ),
                                        ),
                                      )
                                    : user.isFollowing
                                        ? OutlinedButton(
                                            style: OutlinedButton.styleFrom(
                                              foregroundColor: Colors.grey.shade700,
                                              side: BorderSide(
                                                color: Colors.grey.shade400,
                                              ),
                                              padding: const EdgeInsets.symmetric(
                                                horizontal: 8,
                                              ),
                                            ),
                                            onPressed: () => _toggleFollow(user),
                                            child: const Text(
                                              "Unfollow",
                                              style: TextStyle(fontSize: 12),
                                            ),
                                          )
                                        : ElevatedButton(
                                            style: ElevatedButton.styleFrom(
                                              backgroundColor: Colors.blue,
                                              foregroundColor: Colors.white,
                                              padding: const EdgeInsets.symmetric(
                                                horizontal: 8,
                                              ),
                                            ),
                                            onPressed: () => _toggleFollow(user),
                                            child: const Text(
                                              "Follow",
                                              style: TextStyle(
                                                fontSize: 12,
                                                fontWeight: FontWeight.bold,
                                              ),
                                            ),
                                          ),
                              ),
                            ),
                          );
                        },
                      ),
                    ),
        ),
      ],
    );
  }
}
