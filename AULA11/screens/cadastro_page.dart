// Importa os componentes principais do Flutter para construir a interface da tela
import 'package:flutter/material.dart';

// Importa o banco de dados e a tabela de clientes definidos com Drift
import '../database/app_database.dart';

// Importa a biblioteca Drift com alias 'drift' para evitar conflitos com widgets como Column
import 'package:drift/drift.dart' as drift;

// Define um widget com estado que representa a tela de cadastro
class CadastroPage extends StatefulWidget {
  // Cria o estado associado a essa tela
  @override
  _CadastroPageState createState() => _CadastroPageState();
}

// Classe de estado associada à tela de cadastro
class _CadastroPageState extends State<CadastroPage> {
  // Chave global para controle e validação do formulário
  final _formKey = GlobalKey<FormState>();

  // Controladores de texto para capturar os dados digitados pelo usuário
  final _nomeController = TextEditingController();     // Controla o campo Nome
  final _cpfController = TextEditingController();      // Controla o campo CPF
  final _telefoneController = TextEditingController(); // Controla o campo Telefone

  // Instância do banco de dados que será usada para salvar os dados
  final db = AppDatabase();

  // Método responsável por construir a interface da tela
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      // Permite que a tela se ajuste quando o teclado estiver visível
      resizeToAvoidBottomInset: true,

      // Barra superior da tela com título
      appBar: AppBar(
        title: Text('Gerenciamento de Clientes'), // Título atualizado para o sistema completo
      ),

      // Corpo principal da tela com rolagem para evitar problemas com teclado
      body: SingleChildScrollView(
        // Define espaçamento interno da tela
        padding: const EdgeInsets.all(16.0),

        // Formulário que agrupa os campos de entrada
        child: Form(
          key: _formKey, // Liga a chave do formulário para validação

          // Coluna vertical com os widgets de entrada e botões
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.stretch, // Altera o alinhamento para esticar os botões na largura total

            children: [
              // Título para a seção de entrada de dados
              const Text('Novo Cadastro', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)), // Título da seção
              const SizedBox(height: 10), // Espaçamento

              // Campo de entrada de texto para o nome do cliente
              TextFormField(
                controller: _nomeController, // Liga ao controlador do nome
                decoration: InputDecoration(labelText: 'Nome', border: OutlineInputBorder()), // Define rótulo e borda
                validator: (value) {
                  // Função de validação: verifica se o campo está vazio
                  if (value == null || value.isEmpty) {
                    return 'Informe o nome'; // Mensagem de erro
                  }
                  return null; // Tudo certo
                },
              ),
              const SizedBox(height: 10), // Espaço entre campos

              // Campo de entrada para CPF
              TextFormField(
                controller: _cpfController, // Liga ao controlador do CPF
                decoration: InputDecoration(labelText: 'CPF', border: OutlineInputBorder()), // Rótulo do campo e borda
                keyboardType: TextInputType.number, // Define o teclado como numérico para facilitar o preenchimento
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Informe o CPF';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 10), // Espaço entre campos

              // Campo de entrada para telefone
              TextFormField(
                controller: _telefoneController, // Liga ao controlador do telefone
                decoration: InputDecoration(labelText: 'Telefone', border: OutlineInputBorder()), // Rótulo e borda
                keyboardType: TextInputType.phone, // Define o teclado para padrão de telefone
                validator: (value) {
                  if (value == null || value.isEmpty) {
                    return 'Informe o telefone';
                  }
                  return null;
                },
              ),

              // Espaçamento vertical entre os campos e o botão
              const SizedBox(height: 20), 

              // Botão de salvar cliente no banco
              ElevatedButton.icon(
                icon: const Icon(Icons.save), // Adicionado ícone de salvar
                style: ElevatedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 15), // Preenchimento vertical
                ),
                // Ação executada ao pressionar o botão
                onPressed: () async {
                  if (_formKey.currentState!.validate()) {
                    final cliente = ClientesCompanion(
                      nome: drift.Value(_nomeController.text),         // Nome digitado
                      cpf: drift.Value(_cpfController.text),           // CPF digitado
                      telefone: drift.Value(_telefoneController.text), // Telefone digitado
                    );

                    await db.inserirCliente(cliente); // Insere no banco

                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text('Cliente salvo com sucesso!')),
                    );

                    _nomeController.clear(); // Limpa campo
                    _cpfController.clear();  // Limpa campo
                    _telefoneController.clear(); // Limpa campo
                  }
                },
                label: Text('SALVAR CLIENTE'), // Texto do botão
              ),

              const SizedBox(height: 20), // Espaçamento para o menu de gerenciamento
              const Divider(), // Linha horizontal para separar as seções
              const SizedBox(height: 10), // Espaçamento
              const Text('Menu de Gerenciamento', style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold)), // Título do menu
              const SizedBox(height: 15), // Espaçamento

              // Botão para navegar até a tela de listagem de clientes
              OutlinedButton.icon(
                icon: const Icon(Icons.list), // Ícone de lista
                style: OutlinedButton.styleFrom(padding: const EdgeInsets.symmetric(vertical: 15)), // Estilo do botão
                onPressed: () {
                  Navigator.pushNamed(context, '/listar'); // Navega para rota /listar
                },
                label: Text('VER LISTA DE CLIENTES'), // Texto do botão
              ),

              const SizedBox(height: 10), // Espaço entre botões

              // Botão para navegar até a tela de ATUALIZAÇÃO (EDITAR)
              OutlinedButton.icon(
                icon: const Icon(Icons.edit, color: Colors.orange), // Ícone de editar em laranja
                style: OutlinedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 15), // Espaçamento interno
                  foregroundColor: Colors.orange, // Cor do texto e ícone
                  side: const BorderSide(color: Colors.orange), // Cor da borda
                ),
                onPressed: () {
                  Navigator.pushNamed(context, '/atualizar'); // Navega para rota /atualizar definida no main.dart
                },
                label: const Text('EDITAR / ATUALIZAR CLIENTE'), // Texto do botão
              ),

              const SizedBox(height: 10), // Espaço entre botões

              // Botão para navegar até a tela de EXCLUSÃO (DELETAR)
              OutlinedButton.icon(
                icon: const Icon(Icons.delete_forever, color: Colors.red), // Ícone de lixeira em vermelho
                style: OutlinedButton.styleFrom(
                  padding: const EdgeInsets.symmetric(vertical: 15), // Espaçamento interno
                  foregroundColor: Colors.red, // Cor do texto e ícone
                  side: const BorderSide(color: Colors.red), // Cor da borda
                ),
                onPressed: () {
                  Navigator.pushNamed(context, '/excluir'); // Navega para rota /excluir definida no main.dart
                },
                label: const Text('EXCLUIR CLIENTE'), // Texto do botão
              ),
            ],
          ),
        ),
      ),
    );
  }
}
