class BadgeModel {
  final String title;
  final String description;
  final String badgeType;
  final DateTime dateEarned;
  final bool isSeen;

  BadgeModel({
    required this.title,
    required this.description,
    required this.badgeType,
    required this.dateEarned,
    required this.isSeen,
  });

  factory BadgeModel.fromJson(Map<String, dynamic> json) {
    return BadgeModel(
      title: json['title'],
      description: json['description'],
      badgeType: json['badgeType'],
      dateEarned: DateTime.parse(json['dateEarned']),
      isSeen: json['seen'] ?? true,
    );
  }
}
