// Importa o Flutter e o material design
import 'package:flutter/material.dart';

// Importa as telas principais
import 'screens/cadastro_page.dart';  // Tela de cadastro
import 'screens/listar_page.dart';    // Tela de listagem
import 'screens/excluir_page.dart';   // Tela de exclusão (Adicionado para Aula 15)
import 'screens/atualizar_page.dart'; // Tela de atualização (Adicionado para Aula 15)

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
      title: 'Cadastro de Clientes',           // Título do app
      debugShowCheckedModeBanner: false,       // Remove a faixa de debug
      theme: ThemeData(
        primarySwatch: Colors.blue,            // Tema azul padrão
        useMaterial3: false,                   // Mantém o estilo visual clássico do Material
      ), 
      initialRoute: '/',                       // Define a rota inicial como a tela de cadastro
      
      // O mapa de rotas permite navegar entre telas usando nomes (ex: '/excluir')
      routes: {
        '/': (context) => CadastroPage(),        // Rota para a página de cadastro (Home)
        '/listar': (context) => ListarPage(),    // Rota para visualizar a lista de clientes
       
        
      },
    );
  }
}
