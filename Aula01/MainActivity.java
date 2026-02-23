package com.example.exemplocrud;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * MainActivity: A Controller da nossa aplicação.
 * Responsável por gerenciar a interface do usuário (UI) e a interação com o usuário.
 */
public class MainActivity extends AppCompatActivity {

    // Componentes de UI (User Interface) - Campos de entrada de texto
    private EditText nome;
    private EditText cpf;
    private EditText telefone;

    // Objeto de persistência (Data Access Object)
    private AlunoDao dao;

    /**
     * onCreate: Primeiro método do Ciclo de Vida (Lifecycle) a ser executado.
     * Aqui é onde a interface é "inflada" e os objetos são instanciados.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Define qual arquivo XML será o layout desta tela
        setContentView(R.layout.activity_main);

        // Vinculando os componentes do XML com as variáveis Java (ID deve ser igual ao do XML)
        nome = findViewById(R.id.editNome);
        cpf = findViewById(R.id.editCPF);
        telefone = findViewById(R.id.editTelefone);

        // Instanciando o DAO. Passamos 'this' (a própria Activity) como Contexto.
        // O Contexto é necessário para o SQLite saber em que pasta do sistema salvar o arquivo.
        dao = new AlunoDao(this);
    }

    /**
     * Método salvar: Acionado pelo evento onClick do botão no XML.
     * @param view Referência ao componente visual que disparou o evento.
     */
    public void salvar(View view){
        // 1. Criamos um objeto de modelo (POJO)
        Aluno a = new Aluno();
        
        // 2. Coletamos os dados digitados nos EditText e convertemos para String
        a.setNome(nome.getText().toString());
        a.setCpf(cpf.getText().toString());
        a.setTelefone(telefone.getText().toString());
        
        // 3. Chamamos o método de persistência do DAO
        // O retorno 'long' indica o ID gerado pelo banco para este novo registro.
        long id = dao.inserir(a); 
        
        // 4. Feedback ao usuário via Toast (mensagem rápida na tela)
        Toast.makeText(this, "Aluno inserido com id: " + id, Toast.LENGTH_SHORT).show();
    }
}
