// Importa o pacote principal do Flutter
import 'package:flutter/material.dart';

// Define o widget da segunda tela, que é um StatefulWidget (porque o texto vai mudar)
class SegundaTela extends StatefulWidget {
  const SegundaTela({super.key}); // Construtor da SegundaTela, recebe uma Key opcional

  @override
  State<SegundaTela> createState() => _SegundaTelaState(); // Cria o objeto de estado associado
}

// Classe que guarda o estado da SegundaTela
class _SegundaTelaState extends State<SegundaTela> {
  // Variável que guarda o texto atual mostrado na tela
  String mensagem = 'Você está na Segunda Tela!';

  // Função que altera a mensagem ao clicar no botão
  void alterarMensagem() {
    setState(() {
      // Quando setState é chamado, o Flutter redesenha a tela
      mensagem = 'Mensagem Alterada!'; // Novo valor da variável mensagem
    });
  }

  @override
  Widget build(BuildContext context) {
    // Método build que monta a interface visual
    return Scaffold( // Scaffold cria a estrutura básica visual da tela
      appBar: AppBar( // Barra superior da tela
        title: const Text('Segunda Tela'), // Texto exibido na AppBar
      ),
      body: Center( // Centraliza todo o conteúdo no centro da tela
        child: Column( // Organiza os elementos verticalmente
          mainAxisAlignment: MainAxisAlignment.center, // Alinha os filhos no centro verticalmente
          children: [ // Lista de widgets filhos
            // Exibe a variável mensagem na tela
            Text(
              mensagem,
              style: const TextStyle( // Estiliza o texto exibido
                fontSize: 28, // Tamanho da fonte
                fontWeight: FontWeight.bold, // Fonte em negrito
                color: Colors.deepPurple, // Cor roxa profunda
              ),
            ),
            // Espaçamento entre o texto e o botão
            const SizedBox(height: 20),
            // Botão para alterar a mensagem
            ElevatedButton(
              onPressed: alterarMensagem, // Quando clicar, chama alterarMensagem
              child: const Text('Alterar Mensagem'), // Texto do botão
            ),
            // Espaçamento entre os dois botões
            const SizedBox(height: 20),
            // Botão para voltar para a primeira tela
            ElevatedButton(
              onPressed: () {
                Navigator.pop(context); // Voltar para a tela anterior na pilha
              },
              child: const Text('Voltar para a Primeira Tela'), // Texto do botão de voltar
            ),
          ],
        ),
      ),
    );
  }
}
