// Importa o Flutter e o material design
import 'package:flutter/material.dart';
// Importa as telas principais
import 'screens/cadastro_page.dart'; // Tela de cadastro
import 'screens/listar_page.dart';   // Tela de listagem
// Função principal que executa o aplicativo
void main() {
  runApp(const MyApp()); // Executa o widget principal
}
// Widget principal que configura rotas, tema e tela inicial
class MyApp extends StatelessWidget {
  const MyApp({super.key}); // Construtor com chave opcional
  
@override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Cadastro de Clientes',           // Título do app
      debugShowCheckedModeBanner: false,       // Remove a faixa de debug
      theme: ThemeData(primarySwatch: Colors.blue), // Tema azul
      initialRoute: '/',                       // Define a rota inicial
      routes: {
        '/': (context) => CadastroPage(),      // Rota para cadastro
        '/listar': (context) => ListarPage(),  // Rota para listagem
      },
    );
  }
}
