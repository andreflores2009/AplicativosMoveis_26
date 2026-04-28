// Importa o pacote principal do Flutter (obrigatório para criar apps)
import 'package:flutter/material.dart';

// Define a classe da Primeira Tela, que é um widget sem estado (StatelessWidget)
class PrimeiraTela extends StatelessWidget {
  const PrimeiraTela({super.key}); // Construtor da PrimeiraTela, recebendo uma Key opcional

  @override
  Widget build(BuildContext context) {
    // Método build: monta a interface gráfica da tela
    // Scaffold cria a estrutura básica da tela (AppBar + Body)
    return Scaffold( 
      // Cria a barra superior (AppBar)
      appBar: AppBar( 
        title: const Text('Primeira Tela'), // Texto mostrado na AppBar
      ),
      // Centraliza todo o conteúdo no meio da tela
      body: Center( 
        // Organiza vários widgets em coluna (um abaixo do outro)
        child: Column( 
          mainAxisAlignment: MainAxisAlignment.center, // Alinha os widgets verticalmente ao centro
          // Lista de widgets filhos
          children: [ 
            const Text( 
              'Olá, Flutter!', 
              style: TextStyle( 
                fontSize: 32, 
                fontWeight: FontWeight.bold, // Fonte em negrito
                color: Colors.blue, // Cor azul para o texto
              ),
            ),
            const SizedBox(height: 20), // Espaço em branco de 20 pixels entre o texto e o botão
            // Botão elevado (estilo Material Design)
            ElevatedButton( 
              // Função chamada quando o botão for pressionado
              onPressed: () { 
                Navigator.pushNamed(context, '/segunda'); // Navega para a rota '/segunda'
              },
              child: const Text('Ir para a Segunda Tela'), // Texto que aparece dentro do botão
            ),
          ],
        ),
      ),
    );
  }
}
