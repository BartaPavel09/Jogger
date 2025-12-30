class User {
  final int id;
  final String username;
  final String email;
  final String role;
  final DateTime dateJoined;
  final double? weight;

  User({
    required this.id,
    required this.username,
    required this.email,
    required this.role,
    required this.dateJoined,
    this.weight,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['id'],
      username: json['username'],
      email: json['email'],
      role: json['role'] ?? 'USER',

      dateJoined: json['dateJoined'] != null
          ? DateTime.tryParse(json['dateJoined'].toString()) ?? DateTime.now()
          : DateTime.now(),
      weight: json['weight'] != null
          ? (json['weight'] as num).toDouble()
          : null,
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'username': username,
      'email': email,
      'role': role,
      'dateJoined': dateJoined.toIso8601String(),
      'weight': weight,
    };
  }
}
