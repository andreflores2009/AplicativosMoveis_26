// Importa o pacote do Flutter para construir a interface (botões, listas, cores)
import 'package:flutter/material.dart';

// Importa a classe do banco de dados para que possamos usar o método 'listarClientes'
import '../database/app_database.dart';

// Esta é uma StatelessWidget porque ela apenas exibe dados que vêm do banco, 
// não gerencia variáveis internas que mudam a cada segundo.
class ListarPage extends StatelessWidget {
  // Cria uma instância do banco de dados Drift para acessar o SQLite
  final db = AppDatabase();

  // O método build é o "desenhista" da tela
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // Barra superior com o título da página
      appBar: AppBar(title: Text('Lista de Clientes')),

      // O FutureBuilder é um widget especial que "espera" uma resposta do banco de dados.
      // Como o banco de dados é lento comparado ao processador, precisamos dele para não travar o app.
      body: FutureBuilder<List<Cliente>>(
        // 'future' é a tarefa que ele deve esperar terminar (neste caso, buscar a lista)
        future: db.listarClientes(), 

        // O 'builder' é chamado várias vezes: quando começa a carregar e quando termina.
        builder: (context, snapshot) {
          
          // 1º ESTADO: Verificação de Erro (Boa prática para mostrar aos alunos)
          if (snapshot.hasError) {
            return Center(child: Text('Erro ao carregar dados!'));
          }

          // 2º ESTADO: Carregando
          // Se snapshot não tem dados (hasData) ainda, significa que o banco está trabalhando.
          if (!snapshot.hasData) {
            return Center(child: CircularProgressIndicator()); // Mostra o círculo girando
          }

          // 3º ESTADO: Dados Carregados
          // 'snapshot.data!' -> Usamos o '!' porque o 'if' acima já garantiu que o dado chegou.
          // O '!' diz ao Dart: "Pode confiar, a lista não está nula agora".
          final clientes = snapshot.data!; 

          // Verificação extra: E se o banco estiver vazio? (Importante para o aluno não ver uma tela branca)
          if (clientes.isEmpty) {
            return Center(child: Text('Nenhum cliente cadastrado.'));
          }

          // 4º ESTADO: Construção da Lista Visual
          // O ListView.builder é eficiente porque só desenha o que cabe na tela do celular.
          return ListView.builder(
            itemCount: clientes.length, // Define quantos itens a lista terá

            // Esta função roda para cada item da lista (index 0, index 1, index 2...)
            itemBuilder: (context, index) {
              final cliente = clientes[index]; // Pega o cliente específico daquela posição

              // O ListTile é um componente pronto do Flutter com título, subtítulo e ícone
              return ListTile(
                // Ícone decorativo à esquerda (leading)
                leading: CircleAvatar(
                  child: Text(cliente.nome[0].toUpperCase()), // Pega a primeira letra do nome
                ),
                title: Text(cliente.nome), // Nome em destaque
                subtitle: Text(
                  'CPF: ${cliente.cpf} \nTel: ${cliente.telefone}', // \n quebra a linha
                ),
                // Deixa o item da lista um pouco mais alto para caber o telefone
                isThreeLine: true, 
              );
            },
          );
        },
      ),
    );
  }
}
