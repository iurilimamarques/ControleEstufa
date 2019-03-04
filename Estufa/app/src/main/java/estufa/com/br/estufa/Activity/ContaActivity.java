package estufa.com.br.estufa.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
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

public class ContaActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private EditText editNome;
    private EditText editEmail;
    private Button mudarSenha;
    private Button mudarNome;
    private Button deletaConta;
    private Switch prefeLogin;
    private AlertDialog.Builder builder;
    private SUsuario sUsuario = SUsuario.getInstance();
    private SPreferences sPreferences = SPreferences.getInstance();
    private boolean validacao;
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private RequestQueue queue;
    private TextView aviso;
    private Toolbar toolbar;
    private Handler setDelay = new Handler();
    private TextView tvNomeId;
    private TextView tvEmailId;
    private TextWatcher verificaEditNome = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String nome = editNome.getText().toString().trim();

            mudarNome.setEnabled(!nome.equals(sUsuario.getNomeUsuario().toString()));
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);

        builder = new AlertDialog.Builder(ContaActivity.this);

        aviso = findViewById(R.id.viewAvisoId);
        editNome = findViewById(R.id.editNomeId);
        editEmail = findViewById(R.id.editEmailId);
        mudarSenha = findViewById(R.id.btnMSenhaId);
        mudarNome = findViewById(R.id.btnSalvarId);
        prefeLogin = findViewById(R.id.swLoginId);
        deletaConta = findViewById(R.id.btnDContaId);
        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer);

        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(sPreferences.getARQUIVO_LOGIN_PREFERENCIA(), MODE_PRIVATE);
        Boolean checkBoxStatus = sharedPreferences.getBoolean("checkBoxStatus", false);
        if (checkBoxStatus != false) {
            prefeLogin.setChecked(checkBoxStatus);
        } else {
            prefeLogin.setChecked(false);
        }

        deletaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setTitle("Deleta");
                builder.setMessage("Deseja realmente deletar sua conta?");

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        validacao = true;
                        iniciaVolley(validacao);
                    }
                });
                builder.create();
                builder.show();

            }
        });

        prefeLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(sPreferences.getARQUIVO_LOGIN_PREFERENCIA(), MODE_PRIVATE).edit();
                editor.putBoolean("checkBoxStatus", prefeLogin.isChecked());
                editor.commit();
            }
        });

        editEmail.setFocusable(false);


        actionBarDrawerToggle = new ActionBarDrawerToggle(ContaActivity.this, drawerLayout, toolbar, R.string.abrir, R.string.fechar);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle.syncState();

        editNome.addTextChangedListener(verificaEditNome);

        NavigationView navigationView = findViewById(R.id.menu_navegacaooId);
        navigationView.setNavigationItemSelectedListener(ContaActivity.this);

        View header = navigationView.getHeaderView(0);
        tvNomeId = header.findViewById(R.id.tvNomeId);
        tvEmailId = header.findViewById(R.id.tvEmailId);
        tvNomeId.setText(sUsuario.getNomeUsuario());
        tvEmailId.setText(sUsuario.getEmailUsuario());

        mudarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validacao = false;
                iniciaVolley(validacao);
            }
        });

        editNome.setText(sUsuario.getNomeUsuario().toString());
        editEmail.setText(sUsuario.getEmailUsuario().toString());

        mudarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContaActivity.this, EditaSenhaActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.controleId) {
            Intent intent = new Intent(ContaActivity.this, TelaInicialActivity.class);
            startActivity(intent);
        }
        if (id == R.id.logoutId) {
            builder.setTitle("Sair");
            builder.setMessage("Deseja realmente sair?");

            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent intent = new Intent(ContaActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            builder.create();
            builder.show();


        }
        if (id == R.id.configuraId) {
            Intent intent = new Intent(ContaActivity.this, ContaActivity.class);
            startActivity(intent);
        }
        return false;
    }


    private void iniciaVolley(boolean validaBool) {
        queue = Volley.newRequestQueue(this);
        if (validaBool) {
            UrlWeb urlDados = new UrlWeb();
            String UrlDados = urlDados.getURL() + "DeletarConta.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlDados,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                jsonObject = new JSONObject(response);
                                jsonArray = jsonObject.getJSONArray("resposta_servidor");
                                JSONObject JO = jsonArray.getJSONObject(0);

                                mensagemService(JO.getString("mensagem"));

                                sUsuario.setSenhaUsuario(null);
                                sUsuario.setEmailUsuario(null);
                                sUsuario.setIdUsuario(0);
                                sUsuario.setNomeUsuario(null);

                                SharedPreferences.Editor editor = getSharedPreferences(sPreferences.getARQUIVO_LOGIN_PREFERENCIA(), MODE_PRIVATE).edit();
                                editor.putString("login", null);
                                editor.putString("senha", null);

                                editor.commit();

                                Intent intent = new Intent(ContaActivity.this, MainActivity.class);
                                startActivity(intent);

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
                    params.put("Id_usuario", Integer.toString(sUsuario.getIdUsuario()));
                    return params;
                }
            };
            queue.add(stringRequest);

        } else {
            UrlWeb urlDados = new UrlWeb();
            String UrlDados = urlDados.getURL() + "MudaNome.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlDados,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                jsonObject = new JSONObject(response);
                                jsonArray = jsonObject.getJSONArray("resposta_servidor");
                                JSONObject JO = jsonArray.getJSONObject(0);

                                mensagemService(JO.getString("mensagem"));
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
                    params.put("id_usuario", Integer.toString(sUsuario.getIdUsuario()));
                    params.put("nome_usuario", editNome.getText().toString());
                    return params;
                }
            };
            queue.add(stringRequest);

        }
    }

    private void mensagemService(String mensagem) {

        if (mensagem.equals("atualizado")) {
            sUsuario.setNomeUsuario(editNome.getText().toString());
            aviso.setText("Atualizado com sucesso");
            tvNomeId.setText(sUsuario.getNomeUsuario());
            tvEmailId.setText(sUsuario.getEmailUsuario());
        } else if (mensagem.equals("erro")) {
            aviso.setText("Não foi possivel atualizar o nome!");
            aviso.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        if (mensagem.equals("erroEx")) {
            aviso.setText("Não foi possível deletar a conta!");
            aviso.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        Runnable startDelay = new Runnable() {
            @Override
            public void run() {
                aviso.setText("");
            }
        };
        setDelay.postDelayed(startDelay, 4000);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ContaActivity.this, TelaInicialActivity.class);
        startActivity(intent);
    }
}
