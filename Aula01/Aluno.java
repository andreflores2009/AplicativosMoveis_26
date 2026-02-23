// Define o pacote onde a classe está localizada dentro da estrutura do projeto
package com.example.exemplocrud;

// Importa a interface Serializable. No Android, isso é essencial para que
// o objeto possa ser passado entre telas (Activities) via Intents.
import java.io.Serializable;

/**
 * Classe Model Aluno
 * Representa a entidade "Aluno" no sistema e no banco de dados SQLite.
 * Implementa Serializable para permitir o transporte dos dados do objeto.
 */
public class Aluno implements Serializable {

    // Atributos privados: Aplicando o conceito de ENCAPSULAMENTO.
    // Os dados não podem ser acessados diretamente, protegendo a integridade da classe.
    private Integer id;       // Chave primária no banco de dados (autoincremento)
    private String nome;     // Nome completo do aluno
    private String cpf;      // Documento de identificação (String para preservar zeros à esquerda)
    private String telefone; // Contato do aluno

    // --- MÉTODOS GETTERS E SETTERS ---
    // Servem para ler (get) e gravar (set) os valores nos atributos privados.

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    // DICA PARA AULA:
    // É uma boa prática sobrescrever o método toString() aqui se você
    // for exibir o objeto diretamente em uma ListView ou Spinner.
    @Override
    public String toString() {
        return nome; // Faz com que o componente mostre apenas o nome do aluno na lista
    }
}
