class Activity {
  final int id;
  final double distanceKm;
  final int durationSec;
  final int calories;
  final DateTime date;
  final String? route;
  final double pace;

  Activity({
    required this.id,
    required this.distanceKm,
    required this.durationSec,
    required this.calories,
    required this.date,
    this.route,
    required this.pace,
  });

  factory Activity.fromJson(Map<String, dynamic> json) {
    return Activity(
      id: json['id'],

      distanceKm: (json['distanceKm'] as num).toDouble(),
      durationSec: json['durationSec'],
      calories: json['calories'],
      date: DateTime.parse(json['date']),
      route: json['route'],
      pace: (json['pace'] as num).toDouble(),
    );
  }

  String get durationFormatted {
    final minutes = durationSec ~/ 60;
    return '$minutes min';
  }
}
