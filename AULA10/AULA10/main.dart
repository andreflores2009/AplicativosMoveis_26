import 'package:flutter/material.dart';
import 'exemplos/exemplo01_stateless.dart';
import 'exemplos/exemplo02_stateful.dart';

void main() {
  runApp(const MyApp());
}

// Widget principal do app
class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      initialRoute: '/', // Rota inicial
      routes: {
        '/': (context) => const PrimeiraTela(), // Primeira tela
        '/segunda': (context) => const SegundaTela(), // Segunda tela
      },
    );
  }
}
