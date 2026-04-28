// Importa o pacote principal do Flutter
import 'package:flutter/material.dart';

// Define a terceira tela (formulário)
class FormularioBasicoPage extends StatelessWidget {
  const FormularioBasicoPage({super.key});

  @override
  Widget build(BuildContext context) {
    // Criamos uma chave para validar o formulário
    final _formKey = GlobalKey<FormState>();

    return Scaffold( // Estrutura principal da tela
      appBar: AppBar(
        title: const Text('Formulário Básico'),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0), // Espaçamento interno
        child: Form(
          key: _formKey, // Liga o formulário à chave de validação
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start, // Alinha à esquerda
            children: [
              // Campo de texto para nome
              TextFormField(
                decoration: const InputDecoration(
                  labelText: 'Digite seu nome',
                ),
                validator: (value) {
                  // Validação simples
                  if (value == null || value.isEmpty) {
                    return 'Por favor, insira seu nome';
                  }
                  return null;
                },
              ),
              const SizedBox(height: 20), // Espaço entre o campo e o botão
              // Botão de envio
              ElevatedButton(
                onPressed: () {
                  // Se o formulário for válido, mostrar mensagem
                  if (_formKey.currentState!.validate()) {
                    ScaffoldMessenger.of(context).showSnackBar(
                      const SnackBar(content: Text('Processando dados')),
                    );
                  }
                },
                child: const Text('Enviar'),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
