package com.example.exemplocrud;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.regex.Pattern;
import java.util.List;

@Dao
public interface AlunoDaoRoom {

    // Inserir um aluno e retornar o ID gerado automaticamente
    @Insert
    long inserir(Aluno aluno);

    // Atualizar os dados de um aluno existente
    @Update
    void atualizar(Aluno aluno);

    // Excluir um aluno do banco de dados
    @Delete
    void excluir(Aluno aluno);

    // Obter todos os alunos cadastrados
    @Query("SELECT * FROM aluno")
    List<Aluno> obterTodos();

    // Verificar se o CPF já existe no banco de dados
    @Query("SELECT COUNT(*) FROM aluno WHERE cpf = :cpf")
    int cpfExistente(String cpf);

    // O Room vai substituir o :nome no meio dos símbolos de %
    @Query("SELECT * FROM aluno WHERE nome LIKE '%' || :nome || '%'")
    List<Aluno> pesquisarPorNome(String nome);

}
