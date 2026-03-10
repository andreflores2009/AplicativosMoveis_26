package com.example.exemplocrud;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //campos do EditText
    private EditText nome;
    private EditText cpf;
    private EditText telefone;

    private EditText endereco;

    private EditText curso;

    private AlunoDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Chama o método onCreate() da classe pai (AppCompatActivity),
        // que configura aspectos essenciais da Activity, como a criação da janela,
        // a restauração do estado salvo e outras inicializações necessárias do ciclo de vida.

        setContentView(R.layout.activity_main);
        // Define qual arquivo de layout XML será usado para esta Activity,
        // ou seja, infla o layout 'activity_listar_alunos.xml' e o torna a interface exibida na tela.

        //Vinculando os campos do layout com as variáveis do Java
        nome = findViewById(R.id.editNome);
        cpf = findViewById(R.id.editCPF);
        telefone = findViewById(R.id.editTelefone);
        endereco = findViewById(R.id.editEndereco);
        curso = findViewById(R.id.editCurso);

        dao = new AlunoDao(this);

    }

    //----------------------------//método para botão salvar qdo clicado---------------------------------------------------------//
    public void salvar(View view){

        String nomeDigitado = nome.getText().toString().trim();
        String cpfDigitado = cpf.getText().toString().trim();
        String telefoneDigitado = telefone.getText().toString().trim();
        String enderecoDigitado = endereco.getText().toString().trim();
        String cursoDigitado = curso.getText().toString().trim();

        // Verifica se os campos estão vazios
        if (nomeDigitado.isEmpty() || cpfDigitado.isEmpty() || telefoneDigitado.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação do CPF (verifica se o formato e os dígitos são válidos)
        System.out.println("CPF antes da validação: " + cpfDigitado);
        if (!dao.validaCpf(cpfDigitado)) {
            Toast.makeText(this, "CPF inválido. Digite novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica se o CPF já existe no banco de dados
        if (dao.cpfExistente(cpfDigitado)) {
            Toast.makeText(this, "CPF duplicado. Insira um CPF diferente.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação do Telefone
        if (!dao.validaTelefone(telefoneDigitado)) {
            Toast.makeText(this, "Telefone inválido! Use o formato correto: (XX) 9XXXX-XXXX", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar objeto Aluno
        Aluno aluno = new Aluno();
        aluno.setNome(nomeDigitado);
        aluno.setCpf(cpfDigitado);
        aluno.setTelefone(telefoneDigitado);
        aluno.setCurso(cursoDigitado);
        aluno.setEndereco(enderecoDigitado);

        // Inserir aluno no banco de dados
        long id = dao.inserir(aluno);

        if (id != -1) {
            Toast.makeText(this, "Aluno inserido com id: " + id, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao inserir aluno. Tente novamente.", Toast.LENGTH_SHORT).show();
        }

    }



    //------------------------------------------------------------------------------------------//
    //método para botão irParaListar qdo clicado
    public void irParaListar(View view) {
        Intent intent = new Intent(this, ListarAlunosActivity.class);
        startActivity(intent);
    }


}
