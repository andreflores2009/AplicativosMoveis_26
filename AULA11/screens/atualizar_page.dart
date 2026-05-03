// Importa os pacotes fundamentais do Flutter para a interface [cite: 5]
import 'package:flutter/material.dart';
// Importa o arquivo onde definimos a estrutura do banco e nossos métodos CRUD [cite: 5]
import '../database/app_database.dart';

class AtualizarPage extends StatefulWidget {
  @override
  _AtualizarPageState createState() => _AtualizarPageState();
}

class _AtualizarPageState extends State<AtualizarPage> {
  // Instancia a conexão com o banco de dados Drift [cite: 5]
  final db = AppDatabase();
  
  // O TextEditingController serve para "ler" o que o usuário digita e também para "escrever"
  // texto dentro do campo programaticamente (usamos para carregar o nome atual do cliente).
  final _nomeController = TextEditingController();
  
  // Variável que guarda o objeto do cliente que clicamos na lista.
  // O símbolo '?' indica que ela pode ser nula (vazia), o que é verdade até o usuário clicar em alguém.
  Cliente? _clienteSelecionado;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Atualizar Cliente')),
      // Usamos Column para colocar a lista em cima e o formulário de edição embaixo
      body: Column(
        children: [
          // Expanded: Faz a lista ocupar todo o espaço que sobrar na tela.
          Expanded(
            child: FutureBuilder<List<Cliente>>(
              // Busca a lista atualizada no SQLite através do método listarClientes [cite: 5]
              future: db.listarClientes(), 
              builder: (context, snapshot) {
                // Enquanto o banco "pensa", mostramos a animação de carregamento
                if (!snapshot.hasData) return Center(child: CircularProgressIndicator());
                
                // 'snapshot.data!' -> O '!' garante que os dados chegaram para ler o tamanho da lista
                final listaDeClientes = snapshot.data!;

                // ListView.builder: Constrói a lista sob demanda para economizar memória
                return ListView.builder(
                  itemCount: listaDeClientes.length,
                  itemBuilder: (context, index) {
                    // Pega o cliente específico da posição 'index'
                    final c = listaDeClientes[index];
                    return ListTile(
                      title: Text(c.nome), // Mostra o nome na lista
                      subtitle: Text('CPF: ${c.cpf}'), // Mostra o CPF abaixo do nome
                      trailing: Icon(Icons.edit, color: Colors.blue), // Ícone de lápis à direita
                      onTap: () {
                        // O 'setState' avisa ao Flutter para redesenhar a tela com os novos dados
                        setState(() {
                          _clienteSelecionado = c; // Guarda o cliente clicado na variável
                          _nomeController.text = c.nome; // Carrega o nome dele no campo de texto (TextField)
                        });
                      },
                    );
                  },
                );
              },
            ),
          ),
          
          // LÓGICA CONDICIONAL: O bloco (Container) abaixo só aparece se houver um cliente selecionado
          if (_clienteSelecionado != null)
            Container(
              padding: EdgeInsets.all(20),
              // Decoração visual: fundo azul claro e borda no topo para separar da lista
              decoration: BoxDecoration(
                color: Colors.blue[50], 
                border: Border(top: BorderSide(color: Colors.blue, width: 2)),
              ),
              child: Column(
                children: [
                  // Exibe o nome do cliente que está sendo editado no momento
                  Text('Editando Dados de: ${_clienteSelecionado!.nome}', 
                       style: TextStyle(fontWeight: FontWeight.bold, color: Colors.blue[900])),
                  
                  const SizedBox(height: 10), // Espaçador entre os elementos

                  TextField(
                    controller: _nomeController, // O texto digitado pelo usuário fica guardado aqui
                    decoration: InputDecoration(
                      labelText: 'Novo Nome',
                      border: OutlineInputBorder(), // Borda ao redor do campo de texto
                      fillColor: Colors.white,
                      filled: true,
                    ),
                  ),
                  
                  const SizedBox(height: 15),
                  
                  // Botão para salvar as alterações no banco de dados
                  ElevatedButton.icon(
                    icon: Icon(Icons.check),
                    label: Text('CONFIRMAR ALTERAÇÃO'),
                    // minimumSize com infinity faz o botão ocupar toda a largura da tela
                    style: ElevatedButton.styleFrom(minimumSize: Size(double.infinity, 50)),
                    onPressed: () async {
                      // Criamos um novo objeto 'Cliente' com o nome atualizado do TextField
                      // IMPORTANTE: Mantemos o ID original para o SQLite saber qual linha alterar 
                      final clienteAtualizado = Cliente(
                        id: _clienteSelecionado!.id, 
                        nome: _nomeController.text,  // Pega o novo valor digitado
                        cpf: _clienteSelecionado!.cpf, // Mantém o CPF antigo
                        telefone: _clienteSelecionado!.telefone, // Mantém o telefone antigo
                      );

                      // Envia o objeto atualizado para o banco (método replace) 
                      await db.atualizarCliente(clienteAtualizado);

                      // Após salvar, resetamos a tela para esconder o formulário e limpar o campo
                      setState(() {
                        _clienteSelecionado = null; 
                        _nomeController.clear();
                      });

                      // Feedback visual para confirmar a operação ao usuário
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(
                          content: Text('Cliente atualizado com sucesso!'),
                          backgroundColor: Colors.green,
                        ),
                      );
                    },
                  )
                ],
              ),
            ),
        ],
      ),
    );
  }
}
