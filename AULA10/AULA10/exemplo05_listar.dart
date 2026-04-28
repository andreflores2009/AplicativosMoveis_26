import 'package:flutter/material.dart';

// Modelo simples de Cliente
class Cliente {
  final String nome;
  final String cpf;
  final String telefone;

  Cliente({required this.nome, required this.cpf, required this.telefone});
}

// Lista de clientes cadastrados
List<Cliente> listaClientes = [];

class ListarClientesPage extends StatelessWidget {
  const ListarClientesPage({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Listar Clientes'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Builder( 
          builder: (context) {
            if (listaClientes.isEmpty) {
              // Se não tiver clientes cadastrados
              return const Center(
                child: Text('Nenhum cliente cadastrado'),
              );
            } else {
              // Se tiver clientes cadastrados
              return ListView.builder(
                itemCount: listaClientes.length,
                itemBuilder: (context, index) {
                  final cliente = listaClientes[index];
                  return Card(
                    child: ListTile(
                      title: Text(cliente.nome),
                      subtitle: Text('CPF: ${cliente.cpf}\nTelefone: ${cliente.telefone}'),
                    ),
                  );
                },
              );
            }
          },
        ),
      ),
    );
  }
}
