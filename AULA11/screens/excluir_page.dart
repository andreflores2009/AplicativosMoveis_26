// Importa os componentes de interface do Flutter para construir a tela
import 'package:flutter/material.dart';
// Importa o arquivo do banco de dados para acessar a função de excluir
import '../database/app_database.dart';

// Usamos StatefulWidget porque a tela precisa "reagir" e mudar quando um item for deletado
class ExcluirPage extends StatefulWidget {
  @override
  _ExcluirPageState createState() => _ExcluirPageState();
}

class _ExcluirPageState extends State<ExcluirPage> {
  // Cria a instância do banco de dados Drift
  final db = AppDatabase();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Excluir Clientes'), // Título da barra superior
      ),
      
      // O FutureBuilder é o "garçom": ele busca os dados e nos entrega quando estiverem prontos
      body: FutureBuilder<List<Cliente>>(
        // Define qual tarefa o FutureBuilder deve esperar (a listagem do banco)
        future: db.listarClientes(), 
        
        builder: (context, snapshot) {
          // Enquanto o banco de dados está procurando os arquivos no celular:
          if (!snapshot.hasData) {
            // Mostra aquela bolinha girando no centro da tela
            return Center(child: CircularProgressIndicator()); 
          }

          // 'snapshot.data!' -> O '!' é a nossa garantia de que a lista chegou e não está nula.
          // Sem o '!', o Dart não deixaria a gente avançar por segurança.
          final clientes = snapshot.data!;

          // Se a lista chegou, mas está vazia (zero clientes no banco):
          if (clientes.isEmpty) {
            return Center(child: Text('Não há clientes para excluir.'));
          }

          // Constrói a lista rolável com os clientes encontrados
          return ListView.builder(
            itemCount: clientes.length, // Quantidade total de clientes no banco
            itemBuilder: (context, index) {
              final cliente = clientes[index]; // Pega os dados do cliente atual (da linha X)

              return ListTile(
                // Mostra o nome do cliente
                title: Text(cliente.nome), 
                // Mostra o CPF logo abaixo do nome
                subtitle: Text('CPF: ${cliente.cpf}'), 
                
                // 'trailing' coloca um elemento no final da linha (lado direito)
                trailing: IconButton(
                  icon: Icon(Icons.delete, color: Colors.red), // Ícone de lixeira vermelho
                  onPressed: () async {
                    // 1. Chama a função de excluir lá no AppDatabase usando o ID do cliente
                    await db.excluirCliente(cliente.id);
                    
                    // 2. Exibe o SnackBar (balão de mensagem) confirmando a exclusão
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(
                        content: Text('Cliente ${cliente.nome} removido com sucesso!'),
                        backgroundColor: Colors.redAccent, // Fundo vermelho para combinar com a ação
                      ),
                    );

                    // 3. O PONTO CHAVE: setState(() {});
                    // Este comando avisa ao Flutter: "Ei, os dados mudaram! Desenhe a tela de novo".
                    // O FutureBuilder vai rodar novamente e buscar a lista atualizada sem o cliente deletado.
                    setState(() {}); 
                  },
                ),
              );
            },
          );
        },
      ),
    );
  }
}
