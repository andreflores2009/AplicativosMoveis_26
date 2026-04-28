package com.example.exemplocrud;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListarAlunosActivity extends AppCompatActivity {

    private ListView listView;


    //private AlunoDao dao;
    private AlunoDaoRoom alunoDaoRoom;

    private List<Aluno> alunos;
    private List<Aluno> alunosFiltrados = new ArrayList<>();
    // =new ArrayList<>() Para não iniciar como null
    //os outros são setados por valores no método onCreate

    private EditText editPesquisar;
    //private Button btnPesquisar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Chama o método onCreate() da classe pai (AppCompatActivity),
        // que configura aspectos essenciais da Activity, como a criação da janela,
        // a restauração do estado salvo e outras inicializações necessárias do ciclo de vida.

        setContentView(R.layout.activity_listar_alunos);
        // Define qual arquivo de layout XML será usado para esta Activity,
        // ou seja, infla o layout 'activity_listar_alunos.xml' e o torna a interface exibida na tela.

        //vincular variaveis com os campos do layout
        listView = findViewById(R.id.lista_alunos); //lista_alunos é o id do listview
        //dao = new AlunoDao(this);
        alunoDaoRoom = AppDatabase.getInstance(this).alunoDaoRoom();

        alunos = alunoDaoRoom.obterTodos(); //todos alunos
        alunosFiltrados.addAll(alunos); //só os alunos que foram consultados

        //setando os componentes do filtro de pesquisa
        editPesquisar = findViewById(R.id.editPesquisar);
        //btnPesquisar = findViewById(R.id.btnPesquisar);

        //ArrayAdapter já vem pronto no android para colocar essa lista de alunos na listview
        // Cria um ArrayAdapter para transformar a lista de objetos 'Aluno' em views
        // que serão exibidas na ListView. O layout 'simple_list_item_1' define a aparência
        // de cada item da lista.
        ArrayAdapter<Aluno> adaptador = new ArrayAdapter<Aluno>(this, android.R.layout.simple_list_item_1, alunos); //usa o ToString para exibir os dados
        //colocar na listView o adaptador
        //listView.setAdapter(adaptador); //comentado para iniciar sem nomes na lista

        //Deve ficar dentro do onCreate() do ListarAlunosActivity
        //registrar o menu de contexto (excluir e atualizar) na listview através de método onCreateContextMenu
        registerForContextMenu(listView);

    }


    //método para botão voltar qdo clicado
    public void voltarParaCadastro(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    //pesquisarAluno
    public void pesquisarAluno(View view) {
        String textoPesquisa = editPesquisar.getText().toString().trim();
        //filtrarAlunos(textoPesquisa);
        filtrarAlunos_versao01(textoPesquisa);
    }


    private void filtrarAlunos_versao01(String nome) {
        // Se 'nome' for vazio, o LIKE '%%' trará todos os registros automaticamente.
        alunosFiltrados.clear();
        alunosFiltrados.addAll(alunoDaoRoom.pesquisarPorNome(nome));

        // Atualiza a lista na tela
        ArrayAdapter<Aluno> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alunosFiltrados);
        listView.setAdapter(adaptador);
    }

    //FiltrarAlunos
    private void filtrarAlunos(String nome) {
        Log.d("DEBUG", "Filtrando alunos para: " + nome); // Verifica se o método está sendo chamado corretamente
        Log.d("DEBUG", "Total de alunos antes da filtragem: " + alunos.size()); // Mostra quantos alunos existem

        alunosFiltrados.clear();

        if (nome.isEmpty()) {
            alunosFiltrados.addAll(alunos); // Se a pesquisa estiver vazia, mostrar todos
        } else {
            for (Aluno aluno : alunos) {
                if (aluno.getNome().toLowerCase().contains(nome.toLowerCase())) {
                    alunosFiltrados.add(aluno); // Adiciona os alunos que contêm o nome digitado
                }
            }
        }

        Log.d("DEBUG", "Total de alunos após filtragem: " + alunosFiltrados.size()); // Verifica se a lista está realmente sendo filtrada
        // Atualiza a lista na tela
        ArrayAdapter<Aluno> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alunosFiltrados);
        listView.setAdapter(adaptador);
    }





    //METODO MENU_CONTEXTO PARA INFLAR O MENU QUANDO ITEM PRESSIONADO
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        // Chama o método da superclasse (neste caso, o método onCreateContextMenu da classe pai).
        // Isso é importante para garantir que qualquer comportamento padrão do método na superclasse
        // (por exemplo, qualquer configuração padrão de menu que a superclasse realiza) seja executado antes
        // de você adicionar suas próprias ações ao menu.
        super.onCreateContextMenu(menu, v, menuInfo);

        // Cria um objeto MenuInflater, que é responsável por inflar (converter um arquivo XML de menu em um objeto Menu)
        // o menu de contexto a partir de um arquivo XML de menu que você criou anteriormente.
        MenuInflater i = getMenuInflater();

        // O método inflate do MenuInflater é usado para inflar o menu de contexto.
        // Aqui, você está especificando o recurso XML (R.menu.menu_contexto) que define as opções de menu
        // que aparecerão quando um item da lista for pressionado.
        i.inflate(R.menu.menu_contexto, menu); //Aqui coloca o nome do menu que havia sido configurado 'menu_contexto'
    }

    //-------------MÉTODO PARA EXCLUIR UM ALUNO SELECIONADO NO MENU DE CONTEXTO--------------------------------------
    public void excluir(MenuItem item){
        // Obtém informações do item selecionado no menu de contexto.
        // O objeto `menuInfo` contém a posição do item na lista.
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        // Obtém o aluno que será excluído a partir da lista filtrada.
        final Aluno alunoExcluir = alunosFiltrados.get(menuInfo.position);

        // Exibe um alerta de confirmação antes de excluir o aluno
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Atenção") // Título do alerta
                .setMessage("Realmente deseja excluir o aluno?") // Mensagem de confirmação
                .setNegativeButton("NÃO",null) // Caso o usuário clique em "NÃO", fecha o alerta sem fazer nada.
                .setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Remove o aluno da lista filtrada
                        alunosFiltrados.remove(alunoExcluir);
                        // Remove o aluno da lista principal
                        alunos.remove(alunoExcluir);

                        // Exclui o aluno do banco de dados
                        alunoDaoRoom.excluir(alunoExcluir);  //alterado
                        // Atualiza a ListView para refletir a exclusão
                        listView.invalidateViews();
                    }
                } ).create(); // Cria a caixa de diálogo
        dialog.show(); // Exibe o alerta na tela
    }


    //------------------------- MÉTODO PARA ATUALIZAR UM ALUNO SELECIONADO NO MENU DE CONTEXTO --------------------------//
    public void atualizar(MenuItem item) {
        // Obtém informações do item selecionado no menu de contexto.
        // O objeto `menuInfo` contém a posição do item na lista.
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        // Obtém o aluno que será atualizado a partir da lista filtrada.
        final Aluno alunoAtualizar = alunosFiltrados.get(menuInfo.position);

        // Cria uma Intent para abrir a tela de cadastro (MainActivity).
        // Isso permite reutilizar a mesma tela para edição.
        Intent it = new Intent(this, MainActivity.class);

        // Adiciona o objeto `alunoAtualizar` à Intent, para que os dados sejam
        // carregados na tela de cadastro e possam ser editados.
        it.putExtra("aluno", alunoAtualizar);

        // Inicia a Activity de cadastro (MainActivity) com os dados do aluno selecionado.
        startActivity(it);
    }

    //Recarregar a lista de alunos a ser exibida após as modificações do método 'atualizar'
    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega todos os alunos do banco de dados
        alunos = alunoDaoRoom.obterTodos();  //alterado
        Log.d("DEBUG", "Total de alunos carregados no onResume: " + alunos.size());

        // Limpa a lista filtrada e adiciona os novos alunos
        alunosFiltrados.clear();
        alunosFiltrados.addAll(alunos);

        // Atualiza o adapter da ListView para refletir os novos dados
        ArrayAdapter<Aluno> adaptador = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, alunosFiltrados);
        //listView.setAdapter(adaptador); //comentado para iniciar sem nomes na lista
    }



}