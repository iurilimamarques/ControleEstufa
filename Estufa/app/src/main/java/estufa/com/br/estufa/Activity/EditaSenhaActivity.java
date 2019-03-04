package estufa.com.br.estufa.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import estufa.com.br.estufa.Objetos.SPreferences;
import estufa.com.br.estufa.Objetos.SUsuario;
import estufa.com.br.estufa.Objetos.UrlWeb;
import estufa.com.br.estufa.R;


public class EditaSenhaActivity extends AppCompatActivity {

    private EditText senhaAtual;
    private TextView viewErro;
    private Button salvarSenha;
    private TextView senhaNova;
    private TextView erroSenha;
    private EditText confirmaSenha;
    private Handler setDelay = new Handler();
    private SUsuario dados = SUsuario.getInstance();
    private SPreferences sPreferences = SPreferences.getInstance();
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private Button botaoVoltar;

    public TextWatcher verificaEditSenhaAtual = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (senhaAtual.getText().toString().equals(dados.getSenhaUsuario().toString())) {
                salvarSenha.setEnabled(true);
                viewErro.setText("");

            } else {
                viewErro.setText("Senha incorreta!");
                salvarSenha.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edita_senha);

        senhaAtual = findViewById(R.id.editSAtualId);
        confirmaSenha = findViewById(R.id.editConfirmaSId);
        viewErro = findViewById(R.id.viewErroSId);
        salvarSenha = findViewById(R.id.btnSalvarId);
        senhaNova = findViewById(R.id.editSNovaId);
        erroSenha = findViewById(R.id.viewErroId);
        botaoVoltar = findViewById(R.id.btnVoltarId);

        senhaAtual.addTextChangedListener(verificaEditSenhaAtual);

        salvarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!confirmaSenha.getText().toString().equals(senhaNova.getText().toString())) {
                    erroSenha.setText("As senhas devem coincidir!");

                    Runnable startDelay = new Runnable() {
                        @Override
                        public void run() {
                            erroSenha.setText("");
                        }
                    };
                    setDelay.postDelayed(startDelay, 4000);

                } else {
                    iniciaVolley();
                }
            }
        });

        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditaSenhaActivity.this,ContaActivity.class);
                startActivity(intent);
            }
        });

    }

    private void iniciaVolley() {

        UrlWeb urlDados = new UrlWeb();
        String url = urlDados.getURL() + "MudaSenha.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("resposta_servidor");
                            JSONObject JO = jsonArray.getJSONObject(0);

                            exibeResposta(JO.getString("mensagem"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("senha_nova", senhaNova.getText().toString());
                params.put("id_usuario", Integer.toString(dados.getIdUsuario()));

                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void exibeResposta(String resposta) {
        if (resposta.equals("atualizado")) {
            SharedPreferences.Editor editor = getSharedPreferences(sPreferences.getARQUIVO_LOGIN_PREFERENCIA(), MODE_PRIVATE).edit();
            editor.putString("senha", senhaNova.getText().toString());
            editor.commit();
            Intent intent = new Intent(EditaSenhaActivity.this, ContaActivity.class);
            startActivity(intent);

        } else if (resposta.equals("erro")) {
            erroSenha.setText("Não foi possivel mudar a senha.");
        } else if (resposta.equals("vazio")) {
            erroSenha.setText("Preencha todos os campos.");
        }
    }
}
