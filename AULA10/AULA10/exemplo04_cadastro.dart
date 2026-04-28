import 'exemplo05_listar.dart'; // ← importa a listaClientes e o modelo Cliente
// Importa o pacote principal do Flutter
import 'package:flutter/material.dart';

// Define o widget da Tela de Cadastro, como Stateful pois vamos manipular dados
class CadastroClientePage extends StatefulWidget {
  const CadastroClientePage({super.key}); // Construtor padrão

  @override
  State<CadastroClientePage> createState() => _CadastroClientePageState(); // Cria o objeto de estado
}

// Define a classe de estado associada à tela
class _CadastroClientePageState extends State<CadastroClientePage> {
  // Cria uma GlobalKey para controlar o formulário (validar e salvar)
  final _formKey = GlobalKey<FormState>();

  // Variáveis para armazenar os dados digitados
  String nome = '';
  String cpf = '';
  String telefone = '';

  @override
  Widget build(BuildContext context) {
    // Constrói a interface visual da tela
    return Scaffold(
      appBar: AppBar(
        title: const Text('Cadastro de Cliente'), // Título da AppBar
      ),
      // SingleChildScrollView permite que a tela role quando o teclado sobe
      body: SingleChildScrollView( 
        child: Padding(
          padding: const EdgeInsets.all(16.0), // Espaçamento interno ao redor do formulário
          child: Form(
            key: _formKey, // Liga o formulário à chave de controle
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.stretch, // Alinha e estica os widgets na largura total
              children: [
                // Campo de entrada para Nome
                TextFormField(
                  decoration: const InputDecoration(
                    labelText: 'Nome', // Rótulo acima do campo
                    border: OutlineInputBorder(), // Adiciona borda ao campo
                  ),
                  validator: (value) { // Função de validação do campo
                    if (value == null || value.isEmpty) {
                      return 'Por favor, informe o nome'; // Se vazio, mostra erro
                    }
                    return null; // Se ok, não mostra erro
                  },
                  onSaved: (value) { // Função para salvar o valor digitado
                    nome = value!; // value não é nulo = value!
                  },
                ),
                const SizedBox(height: 16), // Espaço entre campos

                // Campo de entrada para CPF
                TextFormField(
                  decoration: const InputDecoration(
                    labelText: 'CPF',
                    border: OutlineInputBorder(),
                  ),
                  keyboardType: TextInputType.number, // Abre o teclado numérico
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'Por favor, informe o CPF';
                    }
                    return null;
                  },
                  onSaved: (value) {
                    cpf = value!;
                  },
                ),
                const SizedBox(height: 16),

                // Campo de entrada para Telefone
                TextFormField(
                  decoration: const InputDecoration(
                    labelText: 'Telefone',
                    border: OutlineInputBorder(),
                  ),
                  keyboardType: TextInputType.phone, // Abre o teclado de telefone
                  validator: (value) {
                    if (value == null || value.isEmpty) {
                      return 'Por favor, informe o telefone';
                    }
                    return null;
                  },
                  onSaved: (value) {
                    telefone = value!;
                  },
                ),
                const SizedBox(height: 32), // Espaço maior antes dos botões

                // Botão para Salvar os dados
                ElevatedButton(
                  onPressed: () {
                    // Quando pressionado, primeiro valida os campos
                    if (_formKey.currentState!.validate()) {
                      _formKey.currentState!.save(); // Salva os valores nos campos

                      // Adiciona o cliente na lista global importada do exemplo05
                      listaClientes.add(
                        Cliente(nome: nome, cpf: cpf, telefone: telefone),
                      );

                      // Exibe uma mensagem de sucesso
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('Cliente $nome salvo com sucesso!')),
                      );

                      // Opcional: Limpa o formulário após salvar
                      _formKey.currentState!.reset();
                    }
                  },
                  child: const Text('Salvar'),
                ),
                const SizedBox(height: 16),

                // Botão para ir para a tela de Listar Clientes
                ElevatedButton(
                  onPressed: () {
                    Navigator.pushNamed(context, '/listar'); // Navega para a tela de listagem
                  },
                  child: const Text('Ir para Listar Clientes'),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
