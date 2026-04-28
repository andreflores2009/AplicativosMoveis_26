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



   //--------------------------------VALIDAR CPF ----------------------------------//
    default boolean validaCpf(String CPF) {
        // Remove espaços e caracteres não numéricos (caso tenha sido digitado com . ou -)
        CPF = CPF.replaceAll("[^0-9]", "");

        // Verifica se o CPF tem exatamente 11 dígitos
        if (CPF.length() != 11) {
            return false;
        }

        // Verifica se o CPF não é uma sequência repetida (como 00000000000, 11111111111, etc.)
        if (CPF.matches("(\\d)\\1{10}")) {
            return false;
        }

        char dig10, dig11;
        int soma, num, peso, resto;

        try {
            // Cálculo do Primeiro Dígito Verificador (D1)
            soma = 0;
            peso = 10;
            for (int i = 0; i < 9; i++) {
                num = CPF.charAt(i) - '0';
                soma += (num * peso);
                peso--;
            }
            resto = soma % 11;
            dig10 = (resto < 2) ? '0' : (char) ((11 - resto) + '0');

            // Cálculo do Segundo Dígito Verificador (D2)
            soma = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                num = CPF.charAt(i) - '0';
                soma += (num * peso);
                peso--;
            }
            resto = soma % 11;
            dig11 = (resto < 2) ? '0' : (char) ((11 - resto) + '0');

            return (dig10 == CPF.charAt(9)) && (dig11 == CPF.charAt(10));

        } catch (Exception e) {
            return false;
        }
    }


    // Expressão regular para telefone (XX) 9XXXX-XXXX ou XX 9XXXXXXXX
    Pattern TELEFONE_PATTERN = Pattern.compile("^\\(?([1-9]{2})\\)?\\s?9[0-9]{4}-?[0-9]{4}$");

    //--------------------------------VALIDAR TELEFONE ----------------------------------//
    default boolean validaTelefone(String telefone) {
        if (telefone == null) return false;

        // Remove caracteres não numéricos
        telefone = telefone.replaceAll("[^0-9]", "");

        // O número deve ter 11 dígitos (2 do DDD + 9 do telefone)
        return telefone.length() == 11 && TELEFONE_PATTERN.matcher(telefone).matches();
    }

    //------------------------ FORMATAÇÃO DE TELEFONE ------------------------//
    /*default String formatarTelefone(String telefone) {
        telefone = telefone.replaceAll("[^0-9]", "");

        if (telefone.length() == 11) {
            return String.format("(%s) %s-%s", telefone.substring(0, 2), telefone.substring(2, 7), telefone.substring(7));
        }
        return telefone;
    }*/
}
