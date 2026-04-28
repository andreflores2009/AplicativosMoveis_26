package com.example.exemplocrud;

// Importações necessárias para funcionamento da Activity e operações de rede
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class BuscarCepActivity extends AppCompatActivity {

    // Declaração dos elementos da interface do usuário
    private EditText editCEP, editLogradouro, editBairro, editCidade, editEstado, editNumero, editComplemento;
    private Button btnBuscarCep, btnConfirmar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_cep); // Define o layout da Activity

        // Vincula os elementos do layout às variáveis declaradas acima
        editCEP = findViewById(R.id.editCEP);
        editLogradouro = findViewById(R.id.editLogradouro);
        editBairro = findViewById(R.id.editBairro);
        editCidade = findViewById(R.id.editCidade);
        editEstado = findViewById(R.id.editEstado);
        editNumero = findViewById(R.id.editNumero);
        editComplemento = findViewById(R.id.editComplemento);
        btnBuscarCep = findViewById(R.id.btnBuscarCep);
        btnConfirmar = findViewById(R.id.btnConfirmarCep);

        // Define ações dos botões: um para buscar o CEP e outro para confirmar o endereço
        //configurados diretamente no 'onClick do botão'
        //btnBuscarCep.setOnClickListener(v -> buscarCep());
        //btnConfirmar.setOnClickListener(v -> confirmarEndereco());
    }

    /**
     * Método chamado ao clicar no botão "Buscar CEP".
     * Faz a validação do CEP e inicia a busca se estiver correto.
     */
    public void buscarCep(View view) {
        String cep = editCEP.getText().toString().trim(); // Obtém o texto digitado e remove espaços em branco
        Log.d("TESTE_CEP", "Botão clicado! CEP digitado: " + cep); // <--- PRINT 1

        // Verifica se o CEP foi preenchido corretamente (8 dígitos numéricos)
        if (cep.isEmpty() || cep.length() != 8) {
            Toast.makeText(this, "Digite um CEP válido!", Toast.LENGTH_SHORT).show();
            return; // Sai do método caso o CEP seja inválido
        }

        // Se o CEP for válido, inicia a busca na API via AsyncTask
        new BuscarCepTask().execute(cep);
    }

    /**
     * Classe interna que realiza a requisição HTTP para buscar os dados do CEP na API ViaCEP.
     */
    private class BuscarCepTask extends AsyncTask<String, Void, String> {
        /*Params → O tipo do parâmetro que passamos para executar a tarefa (nesse caso, String, pois passamos o CEP).
        Progress → O tipo de dado usado para progresso da tarefa (nós colocamos Void porque não estamos mostrando progresso).
        Result → O tipo do valor retornado pela tarefa (String, pois recebemos o JSON da API).*/
        /**
         * Executa a requisição HTTP em segundo plano para evitar travamentos na interface.
         */
        @Override
        protected String doInBackground(String... params) {
            String cep = params[0]; // Obtém o CEP passado como parâmetro
            String urlString = "https://viacep.com.br/ws/" + cep + "/json/"; // Monta a URL da API

            try {
                URL url = new URL(urlString); // Cria um objeto URL com o endereço da API
                HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // Abre a conexão HTTP
                connection.setRequestMethod("GET"); // Define o método HTTP como GET

                // Lê a resposta da API
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream())); //onde recebe
                StringBuilder result = new StringBuilder(); //variavel para armazenar os dados recebidos
                String line;

                // Lê cada linha da resposta e adiciona ao StringBuilder
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close(); // Fecha o leitor de entrada

                Log.d("TESTE_CEP", "JSON recebido da API: " + result.toString()); // <--- PRINT 2
                return result.toString(); // Retorna o JSON recebido da API
            } catch (Exception e) {
                e.printStackTrace(); // Mostra o erro no log caso ocorra falha na conexão
                return null; // Retorna null se houver erro
            }
        }

        /**
         * Executado após a finalização da requisição HTTP.
         * Processa os dados e exibe na interface.
         */
        @Override
        protected void onPostExecute(String result) {
            // Se não houver resultado, exibe uma mensagem de erro

            Log.d("TESTE_CEP", "Chegou no onPostExecute. Result é nulo? " + (result == null)); // <--- PRINT 3

            if (result == null) {
                Toast.makeText(BuscarCepActivity.this, "Erro ao buscar o CEP", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONObject jsonObject = new JSONObject(result); // Converte a string JSON em um objeto JSON

                // Se a API retornar um erro (CEP não encontrado), exibe um aviso
                if (jsonObject.has("erro")) {
                    Toast.makeText(BuscarCepActivity.this, "CEP não encontrado!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Preenche os campos com os dados retornados da API
                editLogradouro.setText(jsonObject.getString("logradouro"));
                editBairro.setText(jsonObject.getString("bairro"));
                editCidade.setText(jsonObject.getString("localidade"));
                editEstado.setText(jsonObject.getString("uf"));

            } catch (Exception e) {
                e.printStackTrace(); // Mostra o erro no log caso ocorra falha no processamento do JSON
                Toast.makeText(BuscarCepActivity.this, "Erro ao processar dados do CEP", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Método chamado ao clicar no botão "Salvar Endereço".
     * Junta os dados preenchidos e retorna para a Activity principal.
     */
    public void confirmarEndereco(View view) {
        // Obtém os valores dos campos
        String logradouro = editLogradouro.getText().toString();
        String numero = editNumero.getText().toString();
        String complemento = editComplemento.getText().toString();
        String bairro = editBairro.getText().toString();
        String cidade = editCidade.getText().toString();
        String estado = editEstado.getText().toString();

        // Verifica se o complemento foi preenchido e adiciona a vírgula corretamente
        String enderecoCompleto;
        if (!complemento.isEmpty()) {
            enderecoCompleto = logradouro + ", " + numero + ", " + complemento + " - " + bairro + ", " + cidade + " - " + estado;
        } else {
            enderecoCompleto = logradouro + ", " + numero + " - " + bairro + ", " + cidade + " - " + estado;
        }

        // Envia o endereço de volta para a MainActivity
        Intent resultado = new Intent();
        resultado.putExtra("enderecoCompleto", enderecoCompleto);
        setResult(RESULT_OK, resultado);
        finish(); // Fecha a tela de busca de CEP
    }

}
