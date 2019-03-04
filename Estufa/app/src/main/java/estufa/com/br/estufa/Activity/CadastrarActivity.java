package estufa.com.br.estufa.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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
import estufa.com.br.estufa.Objetos.UrlWeb;
import estufa.com.br.estufa.R;

public class CadastrarActivity extends Activity {

    private EditText nome;
    private EditText email;
    private EditText senha;
    private EditText confirmaSenha;
    private Button cadastrar;
    private Button voltar;
    private TextView textView;
    private TextView viewErroSenha;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private Handler setDelay = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        nome = findViewById(R.id.edtNomeId);
        email = findViewById(R.id.edtEmailId);
        senha = findViewById(R.id.edtSenhaId);
        confirmaSenha = findViewById(R.id.edtConfirmaSenhaId);
        textView = findViewById(R.id.ViewErroId);
        viewErroSenha = findViewById(R.id.ViewErroSenhaId);
        cadastrar = findViewById(R.id.botaoCadastrarId);
        voltar = findViewById(R.id.btnVoltarId);

        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if (verificacaoConexao()) {
                    if (nome.getText().toString().equals("") || email.getText().toString().equals("") || senha.getText().toString().equals("") || confirmaSenha.getText().toString().equals("")) {
                        textView.setText("Preencha os campos corretamente!");

                        Runnable startDelay = new Runnable() {
                            @Override
                            public void run() {
                                textView.setText("");
                            }
                        };
                        setDelay.postDelayed(startDelay, 4000);

                    }else if (!confirmaSenha.getText().toString().equals(senha.getText().toString())){
                        viewErroSenha.setText("As senhas devem coincidir!");

                        Runnable startDelay = new Runnable() {
                            @Override
                            public void run() {
                                viewErroSenha.setText("");
                            }
                        };
                        setDelay.postDelayed(startDelay, 4000);
                    }else {
                        ligacaVolley();
                    }
                } else if (!verificacaoConexao()) {
                    Toast.makeText(CadastrarActivity.this, "Nenhuma conexão detectada!", Toast.LENGTH_LONG).show();
                }
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(CadastrarActivity.this, MainActivity.class);
                startActivity(it);
            }
        });
    }

    private void ligacaVolley() {
        UrlWeb urlDados = new UrlWeb();
        String UrlCadastrar = urlDados.getURL() + "InserirUsuario.php";
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlCadastrar,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String erro;
                            jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("resposta_servidor");
                            JSONObject JO = jsonArray.getJSONObject(0);

                            if (JO.names().get(0).equals("erro")) {
                                erro = JO.getString("erro");
                            } else {
                                erro = "";
                            }

                            if (!erro.equals("existente") && !erro.equals("vazio")) {

                                Intent intent = new Intent(CadastrarActivity.this, MainActivity.class);
                                startActivity(intent);

                            } else {
                                String erroE = JO.getString("erro");
                                erroLogin(erroE);
                            }

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
                params.put("Nome_usuario", nome.getText().toString());
                params.put("Email_usuario", email.getText().toString());
                params.put("Senha_usuario", senha.getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void erroLogin(String erro) {
        if (erro.equals("existente")) {
            textView.setText("Email ja existente.");
        }
        if (erro.equals("vazio")) {
            textView.setText("Preencha todos os campos.");
        }

        Runnable startDelay = new Runnable() {
            @Override
            public void run() {
                textView.setText("");
            }
        };
        setDelay.postDelayed(startDelay, 4000);
    }

    private boolean verificacaoConexao() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        boolean ativador;
        if (networkInfo != null && networkInfo.isConnected()) {
            ativador = true;
        } else {
            ativador = false;
        }
        return ativador;
    }
}

