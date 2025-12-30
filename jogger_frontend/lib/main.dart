import 'package:flutter/material.dart';
import 'screens/login_screen.dart';

void main() {
  runApp(const JoggerApp());
}

class JoggerApp extends StatelessWidget {
  const JoggerApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Jogger',
      theme: ThemeData(primarySwatch: Colors.blue, useMaterial3: true),
      home: const LoginScreen(),
    );
  }
}
