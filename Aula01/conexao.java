package com.example.exemplocrud;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Classe Conexao
 * Estende SQLiteOpenHelper, que é o framework do Android para gestão de ciclo de vida
 * de bancos de dados SQLite (Criação, Abertura e Atualização de Versão).
 */
public class Conexao extends SQLiteOpenHelper {

    // Nome do arquivo de banco de dados que será armazenado na pasta privada do app.
    private static final String name = "banco.db";

    // Versão do banco. Se você alterar a estrutura das tabelas, deve incrementar este número
    // para que o sistema saiba que precisa rodar o método onUpgrade.
    private static final int version = 1;

    /**
     * Construtor: Passa para a superclasse (SQLiteOpenHelper) as configurações do banco.
     * @param context Necessário para localizar o caminho do sistema de arquivos.
     */
    public Conexao(Context context){
        super(context, name, null, version);
    }

    /**
     * onCreate: Executado AUTOMATICAMENTE apenas quando o app é aberto pela primeira vez
     * e o arquivo "banco.db" ainda não existe no dispositivo.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // execSQL: Executa um comando SQL puro de DDL (Data Definition Language).
        // Aqui definimos a estrutura da tabela "aluno".
        db.execSQL("create table aluno(" +
                "id integer primary key autoincrement, " + // ID automático e único
                "nome varchar(50), " +                    // Limite de 50 caracteres para o nome
                "cpf varchar(50), " +                     // Armazenamos CPF como String/Varchar
                "telefone varchar(50))");                 // Armazenamos telefone como String/Varchar
    }

    /**
     * onUpgrade: Executado quando a versão do banco (variável version) é incrementada.
     * Útil para migrações de banco em apps que já estão na Play Store, para não apagar
     * os dados do usuário ao atualizar o sistema.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Exemplo: Se o senhor mudar a versão para 2, poderia colocar aqui um:
        // db.execSQL("alter table aluno add column email varchar(50)");
    }
}
