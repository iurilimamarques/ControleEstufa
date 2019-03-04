package estufa.com.br.estufa.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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

public class MainActivity extends Activity {

    private Button cadastrarBotao;
    private Button loginBotao;
    private EditText editLogin;
    private EditText editSenha;
    private TextView textView;
    private CheckBox checkBox;
    private SUsuario dados = SUsuario.getInstance();
    private SPreferences sPreferences = SPreferences.getInstance();
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkBox = findViewById(R.id.checkBoxId);
        textView = findViewById(R.id.tViewId);
        cadastrarBotao = findViewById(R.id.btnCadastrarId);
        loginBotao = findViewById(R.id.btnLogarId);
        editLogin = findViewById(R.id.edtLoginId);
        editSenha = findViewById(R.id.edtCadastrarId);

        SharedPreferences sharedPreferences = getSharedPreferences(sPreferences.getARQUIVO_LOGIN_PREFERENCIA(), MODE_PRIVATE);
        String login = sharedPreferences.getString("login", null);
        String senha = sharedPreferences.getString("senha", null);
        Boolean checkBoxStatus = sharedPreferences.getBoolean("checkBoxStatus", false);

        if (checkBoxStatus != false) {
            if (login != null) {
                editLogin.setText(login);
                editSenha.setText(senha);
                checkBox.setChecked(checkBoxStatus);
            }
        } else {
            editLogin.setText("");
            editSenha.setText("");
        }

        loginBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificacaoConexao()) {
                    SharedPreferences.Editor editor = getSharedPreferences(sPreferences.getARQUIVO_LOGIN_PREFERENCIA(), MODE_PRIVATE).edit();

                    if (checkBox.isChecked()) {

                        if (editLogin.getText().toString().equals("")) {
                            textView.setText("Preencha todos os campos.");
                        } else {
                            editor.putBoolean("checkBoxStatus", checkBox.isChecked());
                            editor.putString("login", editLogin.getText().toString());
                            editor.putString("senha", editSenha.getText().toString());
                            editor.commit();
                        }
                    } else {
                        editor.putBoolean("checkBoxStatus", checkBox.isChecked());
                        editor.commit();
                    }
                    ligacaoVolley();
                } else if (!verificacaoConexao()) {
                    Toast.makeText(MainActivity.this, "Nenhuma conexão detectada!", Toast.LENGTH_LONG).show();
                }
            }
        });

        cadastrarBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this, CadastrarActivity.class);
                startActivity(it);
            }
        });
    }

    private void ligacaoVolley() {
        UrlWeb urlDados = new UrlWeb();
        String UrlLogin = urlDados.getURL() + "LoginUsuario.php";

        LayoutInflater inflater = LayoutInflater.from(this);
        View subView = inflater.inflate(R.layout.layout_progress_bar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Verificando Login...");
        builder.setCancelable(false);
        builder.setView(subView);
        alertDialog = builder.create();
        alertDialog.show();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            String erro;
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("resposta_servidor");
                            JSONObject JO = jsonArray.getJSONObject(0);

                            alertDialog.dismiss();
                            if (JO.names().get(0).equals("erro")) {
                                erro = JO.getString("erro");
                            } else {
                                erro = "";
                            }
                            if (!erro.equals("info_vazias") && !erro.equals("erro_login") && (!erro.equals("inexistente"))) {

                                String id_usuario, nome_usuario;

                                id_usuario = JO.getString("id_usuario");
                                nome_usuario = JO.getString("nome");

                                dados.setIdUsuario(Integer.parseInt(id_usuario));
                                dados.setNomeUsuario(nome_usuario);
                                dados.setEmailUsuario(editLogin.getText().toString());
                                dados.setSenhaUsuario(editSenha.getText().toString());

                                Intent intent = new Intent(MainActivity.this, TelaInicialActivity.class);
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
                        alertDialog.dismiss();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Email_usuario", editLogin.getText().toString());
                params.put("Senha_usuario", editSenha.getText().toString());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void erroLogin(String erro) {
        Handler setDelay = new Handler();

        if (erro.equals("erro_login")) {
            textView.setText("Email ou senha incorretos.");
        }
        if (erro.equals("info_vazias")) {
            textView.setText("Preencha todos os campos.");
        }
        if (erro.equals("inexistente")) {
            textView.setText("Usuario inexistente.");
        }
        Runnable startDelay = new Runnable() {
            @Override
            public void run() {
                textView.setText("");
            }
        };
        setDelay.postDelayed(startDelay, 4000);
    }

    @Override
    public void onBackPressed() {
        MainActivity.this.moveTaskToBack(true);
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


