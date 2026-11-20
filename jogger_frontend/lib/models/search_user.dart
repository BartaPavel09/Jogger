class SearchUser {
  final int id;
  final String username;
  final String email;
  final String role;
  bool isFollowing;

  SearchUser({
    required this.id,
    required this.username,
    required this.email,
    required this.role,
    required this.isFollowing,
  });

  factory SearchUser.fromJson(Map<String, dynamic> json) {
    return SearchUser(
      id: json['id'],
      username: json['username'] ?? '',
      email: json['email'] ?? '',
      role: json['role'] ?? 'USER',
      isFollowing: json['following'] ?? false,
    );
  }
}
