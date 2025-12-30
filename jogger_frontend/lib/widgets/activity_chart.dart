import 'package:fl_chart/fl_chart.dart';
import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../models/activity.dart';

class ActivityChart extends StatelessWidget {
  final List<Activity> activities;

  const ActivityChart({super.key, required this.activities});

  @override
  Widget build(BuildContext context) {
    final sortedAll = List<Activity>.from(activities);
    sortedAll.sort((a, b) => a.date.compareTo(b.date));

    final data = sortedAll.length > 10
        ? sortedAll.sublist(sortedAll.length - 10)
        : sortedAll;

    if (data.isEmpty) return const SizedBox.shrink();

    return Column(
      children: [
        const Text(
          "Pace (min/km) - Last 10 Runs",
          style: TextStyle(
            fontSize: 14,
            fontWeight: FontWeight.bold,
            color: Colors.grey,
          ),
        ),
        const SizedBox(height: 10),
        AspectRatio(
          aspectRatio: 1.70,
          child: Container(
            margin: const EdgeInsets.symmetric(horizontal: 4),
            decoration: const BoxDecoration(
              borderRadius: BorderRadius.all(Radius.circular(18)),
              color: Color(0xFF232C33),
            ),
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: LineChart(_mainData(data)),
            ),
          ),
        ),
      ],
    );
  }

  LineChartData _mainData(List<Activity> data) {
    return LineChartData(
      gridData: FlGridData(
        show: true,
        drawVerticalLine: true,
        getDrawingHorizontalLine: (_) =>
            const FlLine(color: Colors.white10, strokeWidth: 1),
        getDrawingVerticalLine: (_) =>
            const FlLine(color: Colors.white10, strokeWidth: 1),
      ),
      titlesData: FlTitlesData(
        show: true,
        rightTitles: const AxisTitles(
          sideTitles: SideTitles(showTitles: false),
        ),
        topTitles: const AxisTitles(sideTitles: SideTitles(showTitles: false)),
        bottomTitles: AxisTitles(
          sideTitles: SideTitles(
            showTitles: true,
            reservedSize: 30,
            interval: 1,
            getTitlesWidget: (value, meta) {
              final index = value.toInt();
              if (index >= 0 && index < data.length) {
                return Padding(
                  padding: const EdgeInsets.only(top: 8.0),
                  child: Text(
                    DateFormat('MM/dd').format(data[index].date),
                    style: const TextStyle(
                      color: Color(0xFF68737D),
                      fontSize: 10,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                );
              }
              return const Text('');
            },
          ),
        ),
        leftTitles: AxisTitles(
          sideTitles: SideTitles(
            showTitles: true,
            reservedSize: 40,
            getTitlesWidget: (value, meta) {
              if (value == meta.min || value == meta.max) {
                return const SizedBox.shrink();
              }
              return Text(
                value.toStringAsFixed(1),
                style: const TextStyle(color: Color(0xFF67727D), fontSize: 12),
              );
            },
          ),
        ),
      ),
      borderData: FlBorderData(
        show: true,
        border: Border.all(color: const Color(0xFF37434D)),
      ),
      minX: 0,
      maxX: (data.length - 1).toDouble(),
      minY: 0,
      maxY: (data.map((e) => e.pace).reduce((a, b) => a > b ? a : b) * 1.2),
      lineBarsData: [
        LineChartBarData(
          spots: data
              .asMap()
              .entries
              .map((e) => FlSpot(e.key.toDouble(), e.value.pace))
              .toList(),
          isCurved: true,
          color: const Color(0xFF23b6e6),
          barWidth: 4,
          isStrokeCapRound: true,
          dotData: const FlDotData(show: true),
          belowBarData: BarAreaData(
            show: true,
            color: const Color(0xFF23b6e6).withOpacity(0.3),
          ),
        ),
      ],
    );
  }
}
