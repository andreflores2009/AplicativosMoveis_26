package com.example.exemplocrud;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {

    //campos do EditText
    private EditText nome;
    private EditText cpf;
    private EditText telefone;

    //private AlunoDao dao;
    private AlunoDaoRoom alunoDaoRoom;

    private Aluno aluno = null; //usado para verificar se eta recebendo algo por intenção do método atualizar

    /*------------------CONFIGURACOES PARA CAMERA--------------------*/
    private ImageView imageView;  // Adicione a ImageView para exibir a foto tirada

    // Código de identificação para a solicitação de PERMISSÃO da câmera.
    // É usado no método 'tirarFoto' para pedir autorização e
    // verificado no 'onRequestPermissionsResult' para saber se o usuário aceitou.
    private static final int CAMERA_PERMISSION_CODE = 100;

    // Código de identificação para a CAPTURA DA IMAGEM (a foto em si).
    // É usado no método 'startCamera' ao iniciar a intenção da câmera e
    // verificado no 'onActivityResult' para garantir que os dados recebidos são da foto.
    private static final int REQUEST_IMAGE_CAPTURE = 200;


    //ENDERECO
    private EditText editEnderecoCompleto;


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

        //dao = new AlunoDao(this);
        alunoDaoRoom = AppDatabase.getInstance(this).alunoDaoRoom();


        // CAMERA  - Vincula a ImageView no layout para mostrar a foto
        imageView = findViewById(R.id.imageView);
        // Vincula o botão do layout com a variável 'btnTakePhoto'.
        // Este botão será utilizado para disparar o evento de captura de foto quando clicado.
        Button btnTakePhoto = findViewById(R.id.btnTakePhoto);

        //ENDERECO
        editEnderecoCompleto = findViewById(R.id.editEnderecoCompleto);



        //----------------- Código que recebe os dados do método Atualizar() se ele estiver enviando algo (ListarAlunosActivity)---------------------------------
        Intent it = getIntent(); //pega intenção
        if(it.hasExtra("aluno")){
            aluno = (Aluno) it.getSerializableExtra("aluno");
            nome.setText(aluno.getNome().toString());
            cpf.setText(aluno.getCpf());
            telefone.setText(aluno.getTelefone());

            // Carregar a foto no ImageView no momento que carregar os dados para atualizar
            byte[] fotoBytes = aluno.getFotoBytes();
            if (fotoBytes != null && fotoBytes.length > 0) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(fotoBytes, 0, fotoBytes.length);
                imageView.setImageBitmap(bitmap);
            }

            // ENDERECO:
            editEnderecoCompleto.setText(aluno.getEndereco());

            // Desativa edição do CPF para evitar duplicação quando ele vem do 'atualizar'
            //cpf.setEnabled(false);
        }

    } //FECHA ONCREATE()


    public void abrirBuscarCep(View view) {
        Intent intent = new Intent(this, BuscarCepActivity.class);
        startActivityForResult(intent, 1); // Inicia a tela de buscar CEP e espera um resultado
    }


    //----------------------------//método para botão salvar qdo clicado---------------------------------------------------------//
    public void salvar(View view){

        String nomeDigitado = nome.getText().toString().trim();
        String cpfDigitado = cpf.getText().toString().trim();
        String telefoneDigitado = telefone.getText().toString().trim();

        //Endereco
        String enderecoDigitado = editEnderecoCompleto.getText().toString().trim();


        // Verifica se os campos estão vazios
        if (nomeDigitado.isEmpty() || cpfDigitado.isEmpty() || telefoneDigitado.isEmpty() || enderecoDigitado.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validação do CPF (verifica se o formato e os dígitos são válidos)
        System.out.println("CPF antes da validação: " + cpfDigitado);
        if (!Validador.validaCpf(cpfDigitado)) {
            Toast.makeText(this, "CPF inválido. Digite novamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Se for cadastrar novo aluno ou Se for atualizar os dados ignora o CPF se for igual do próprio aluno
        //Se o aluno atualizar um cpf diferente dai sim será verificado
        if(aluno == null || !cpfDigitado.equals(aluno.getCpf())){
            //  verifica se o CPF já existe no banco
            if (alunoDaoRoom.cpfExistente(cpfDigitado) >0 ) {  //aqui mudou a implementação por causa do retorno
                Toast.makeText(this, "CPF duplicado. Insira um CPF diferente.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Validação do Telefone
        if (!Validador.validaTelefone(telefoneDigitado)) {
            Toast.makeText(this, "Telefone inválido! Use o formato correto: (XX) 9XXXX-XXXX", Toast.LENGTH_SHORT).show();
            return;
        }

        //CADASTRAR ---------------------------------------------------------------------------------
        // aluno ==null cadastrar, aluno!=null esta recebendo do ListarAlunos
        if (aluno==null || aluno.getId() == null) {
            // Se aluno não clicar em tirar foto, aluno ainda não existe
            if (aluno == null) {
                aluno = new Aluno();
            }
            aluno.setNome(nomeDigitado);
            aluno.setCpf(cpfDigitado);
            aluno.setTelefone(telefoneDigitado);
            //Foto ja foi preparada no onActivityResults

            aluno.setEndereco(enderecoDigitado);

            // Inserir aluno no banco de dados
            long id = alunoDaoRoom.inserir(aluno); // O método da Room retorna o ID
            if (id != -1) {
                Toast.makeText(this, "Aluno inserido com id: " + id, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao inserir aluno. Tente novamente.", Toast.LENGTH_SHORT).show();
            }
        }
        //ATUALIZAR --------------------------------------------------------------------------
        else{
            // Atualização de um aluno existente se (aluno!=null), significa que esta recebendo dados pelo serial do ListarAlunos
            aluno.setNome(nomeDigitado);
            aluno.setCpf(cpfDigitado);
            aluno.setTelefone(telefoneDigitado);
            //Foto ja foi preparada no onActivityResults

            //endereco
            aluno.setEndereco(enderecoDigitado);

            alunoDaoRoom.atualizar(aluno);
            Toast.makeText(this, "Aluno atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        }

        // Limpe os campos após cadastro ou atualização
        nome.setText("");
        cpf.setText("");
        telefone.setText("");
        editEnderecoCompleto.setText("");
        aluno = null; // Importante resetar a variável global
        imageView.setImageBitmap(null); // Limpa a foto
    }


    //------------------------------------------------------------------------------------------//
    //método para botão irParaListar qdo clicado
    public void irParaListar(View view) {
        Intent intent = new Intent(this, ListarAlunosActivity.class);
        startActivity(intent);
    }


//--------------------MÉTODOS DA CAMERA --------------------------------------------------------------------
    /**
     * Método chamado pelo clique do botão "Tirar Foto" (android:onClick no XML).
     * Sua principal função é verificar se o usuário já permitiu o uso da câmera.
     */
    public void tirarFoto(View view) {
        // Verifica se a permissão de CAMERA já foi concedida anteriormente
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Se NÃO tem permissão, abre a janelinha do sistema pedindo a autorização.
            // O código CAMERA_PERMISSION_CODE (100) serve para identificarmos esta resposta depois.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        } else {
            // Se JÁ tem permissão, chama o método para abrir a câmera de fato.
            startCamera();
        }
    }


    /** ---------------------------------------------------------------------------------------
     * Método chamado automaticamente após o usuário responder à solicitação de permissão.
     * @param requestCode O código (100) que enviamos lá no tirarFoto.
     * @param permissions O array de permissões solicitadas.
     * @param grantResults O resultado (concedido ou negado) para cada permissão.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Verifica se a resposta que chegou é referente ao nosso pedido de câmera (código 100)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            // Verifica se o array de resultados não está vazio e se o usuário clicou em "Permitir"
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("CAMERA_DEBUG", "Usuário permitiu, abrindo câmera...");
                startCamera();
            } else {
                // Se o usuário negou, avisamos que ele não conseguirá tirar fotos.
                Toast.makeText(this, "A permissão é necessária para usar a câmera.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /** ---------------------------------------------------------------------------------------
     * Método auxiliar que cria a "Intenção" de abrir o aplicativo de câmera do dispositivo.
     */
    private void startCamera() {
        try {
            // Cria uma Intent (intenção) para capturar uma imagem.
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Inicia a atividade da câmera esperando um resultado (a foto).
            // O código REQUEST_IMAGE_CAPTURE (200) serve para identificarmos esta foto quando ela voltar.
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (Exception e) {
            Log.e("CAMERA_DEBUG", "Erro ao abrir a câmera: " + e.getMessage());
            Toast.makeText(this, "Erro ao abrir a câmera no seu dispositivo.", Toast.LENGTH_SHORT).show();
        }
    }


    /** ---------------------------------------------------------------------------------------
     * Método chamado quando uma atividade que iniciamos (como a câmera) termina e nos devolve dados.
     * Verifica o endereço também
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verifica se o resultado foi positivo (usuário não cancelou)
        if (resultCode == RESULT_OK && data != null) {

            // CASO 1: Retorno da busca de CEP (Seu código 1)
            if (requestCode == 1) {
                String enderecoCompleto = data.getStringExtra("enderecoCompleto");
                editEnderecoCompleto.setText(enderecoCompleto);
            }

            // CASO 2: Retorno da Câmera (Seu código REQUEST_IMAGE_CAPTURE = 200)
            else if (requestCode == REQUEST_IMAGE_CAPTURE) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                // Exibe a foto na tela
                imageView.setImageBitmap(imageBitmap);

                // Prepara os bytes para o banco (BLOB)
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                if (aluno == null) {
                    aluno = new Aluno();
                }
                aluno.setFotoBytes(byteArray);
            }
        }
    }



}  //encerra a Classe