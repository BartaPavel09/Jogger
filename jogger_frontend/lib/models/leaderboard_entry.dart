class LeaderboardEntry {
  final int rank;
  final int runnerId;
  final String username;
  final double totalDistanceKm;
  final int totalRuns;

  LeaderboardEntry({
    required this.rank,
    required this.runnerId,
    required this.username,
    required this.totalDistanceKm,
    required this.totalRuns,
  });

  factory LeaderboardEntry.fromJson(Map<String, dynamic> json) {
    return LeaderboardEntry(
      rank: json['rank'] ?? 0,
      runnerId: json['runnerId'] ?? 0,
      username: json['username'] ?? 'Runner',
      totalDistanceKm: (json['totalDistanceKm'] as num?)?.toDouble() ?? 0.0,
      totalRuns: (json['totalRuns'] as num?)?.toInt() ?? 0,
    );
  }
}
