// Importações principais do Drift
import 'package:drift/drift.dart';
import 'package:drift/native.dart';
import 'package:path_provider/path_provider.dart';
import 'package:path/path.dart' as p;
import 'dart:io';

// Parte gerada automaticamente (com build_runner) iremos gerar com o comando:
//flutter pub run build_runner build
//Ele faz isso a partir do que definimos aqui nesta classe
part 'app_database.g.dart';

// Definição da tabela de clientes
class Clientes extends Table {
  IntColumn get id => integer().autoIncrement()(); // Chave primária
  TextColumn get nome => text()();                // Nome obrigatório
  TextColumn get cpf => text()();                 // CPF obrigatório
  TextColumn get telefone => text()();            // Telefone obrigatório
}

// Classe principal do banco (DriftDatabase)
@DriftDatabase(tables: [Clientes])
class AppDatabase extends _$AppDatabase {
  // Construtor chamando a função que abre o banco
  AppDatabase() : super(_abrirConexao());

  // Versão do schema do banco
  @override
  int get schemaVersion => 1;

  // CRUD: inserir cliente
  // INSERIR: Retorna Future<int>
  // Por que int? O banco retorna o "ID" (Primary Key) da linha que acabou de ser criada.
  // Se retornar 5, significa que o novo cliente é o quinto registro do banco.
  // Usamos 'ClientesCompanion' porque, ao inserir, o ID ainda não existe (é gerado pelo banco).
  Future<int> inserirCliente(ClientesCompanion cliente) =>
      into(clientes).insert(cliente);

  // Listar todos os clientes
  // Por que uma Lista de Cliente? O 'select' busca todas as linhas da tabela.
  // O Drift converte cada linha do SQLite em um objeto da classe 'Cliente' (Data Class).
  // O '.get()' executa a busca e entrega a lista pronta.
  Future<List<Cliente>> listarClientes() =>
      select(clientes).get();

  // Atualizar cliente existente
  // Por que bool? O método '.replace' verifica se o registro existe antes de mudar.
  // Retorna 'true' se encontrou o ID e conseguiu atualizar com sucesso.
  // Retorna 'false' se o ID não foi encontrado no banco de dados.
  Future<bool> atualizarCliente(Cliente cliente) =>
      update(clientes).replace(cliente);

  // Excluir cliente por ID
  // Por que int? Diferente do atualizar, o delete retorna a "quantidade de linhas afetadas".
  // Se retornar 1, significa que 1 usuário foi deletado.
  // Se retornar 0, significa que ninguém foi excluído (ID não existia).
  // O '..where' filtra para não apagar o banco inteiro por engano!
  Future<int> excluirCliente(int id) =>
      (delete(clientes)..where((t) => t.id.equals(id))).go();
}

//Future - Significa que a operação é assíncrona (vai acontecer no futuro).
//No Flutter (e no Dart em geral), todas as operações de banco de dados, rede, disco etc. são assíncronas para não travar a interface do usuário.

// Função que cria e retorna a instância do banco
LazyDatabase _abrirConexao() {
  return LazyDatabase(() async {
    final dir = await getApplicationDocumentsDirectory(); // pasta local do app
    final path = p.join(dir.path, 'clientes.db');         // nome do arquivo do banco
    return NativeDatabase(File(path));                    // retorna o banco SQLite nativo
  });
}
