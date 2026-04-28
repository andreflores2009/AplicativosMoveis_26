package com.example.exemplocrud;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.io.Serializable;

@Entity(tableName = "aluno")  // Define esta classe como uma tabela no banco de dados
public class Aluno implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "nome")
    private String nome;

    @ColumnInfo(name = "cpf")
    private String cpf;

    @ColumnInfo(name = "telefone")
    private String telefone;


    // NOVO CAMPO: ENDEREÇO
    @ColumnInfo(name = "endereco")
    private String endereco;

    //CAMERA
    @ColumnInfo(name = "fotoBytes") // Mantém compatível com o Room
    private byte[] fotoBytes;


    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }


    public byte[] getFotoBytes() {
        return fotoBytes;
    }

    public void setFotoBytes(byte[] fotoBytes) {
        this.fotoBytes = fotoBytes;
    }

    public Integer getId() {  return id;   }

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

    //facilitar a visualização de dados e passar o aluno como string
    //sobreescrever o método ToString
    //Quando o aluno for convertido para String irá mostrar somente o nome dele
    @Override
    public String toString(){

        return "Nome: " + nome + " CPF: " + cpf + " " + telefone;
    }
}
