package estufa.com.br.estufa.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import estufa.com.br.estufa.Fragment.EstatisticasFragment;
import estufa.com.br.estufa.Fragment.GraficoFragment;
import estufa.com.br.estufa.Fragment.HistoricoFragment;
import estufa.com.br.estufa.Fragment.ProgramarFragment;
import estufa.com.br.estufa.Objetos.IdData;
import estufa.com.br.estufa.Objetos.IdEstufa;
import estufa.com.br.estufa.Objetos.Informacoes;
import estufa.com.br.estufa.Objetos.SUsuario;
import estufa.com.br.estufa.Objetos.UrlWeb;
import estufa.com.br.estufa.PackageEventBus.EventBus;
import estufa.com.br.estufa.R;

public class TelaInicialActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private boolean bloqueio = false;
    private Spinner spinner;
    private int numeroEstufa;
    private List<String> lstSource = new ArrayList<>();
    private Spinner spinnerData;
    private ArrayAdapter<String> myAdapter;
    private int id;
    private TextView tvNomeId;
    private TextView tvEmailId;
    private SUsuario sUsuario = SUsuario.getInstance();
    private RequestQueue queue;

    private AdapterView.OnItemSelectedListener item = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Informacoes informacoes = Informacoes.getInstance();
            informacoes.setIdEstufa(i + 1);
            EventBus.getBus().post(new IdEstufa(informacoes.getIdEstufa()));
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Informacoes informacoes = Informacoes.getInstance();
            informacoes.setIdEstufa(1);
            EventBus.getBus().post(new IdEstufa(informacoes.getIdEstufa()));
        }
    };

    private AdapterView.OnItemSelectedListener item2 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            Informacoes informacoes = Informacoes.getInstance();
            informacoes.setIdData(i);
            EventBus.getBus().post(new IdData(informacoes.getIdData()));
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            Informacoes informacoes = Informacoes.getInstance();
            informacoes.setIdData(0);
            EventBus.getBus().post(new IdData(informacoes.getIdData()));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        drawerLayout = findViewById(R.id.drawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        bottomNavigationView = findViewById(R.id.bottom_navId);
        spinner = findViewById(R.id.spinnerId);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        spinnerData = findViewById(R.id.spinnerDataId);

        spinnerData.setVisibility(View.INVISIBLE);
        spinner.setOnItemSelectedListener(item);
        spinnerData.setOnItemSelectedListener(item2);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(TelaInicialActivity.this, drawerLayout, toolbar, R.string.abrir, R.string.fechar);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.menu_navegacaoId);
        navigationView.setNavigationItemSelectedListener(TelaInicialActivity.this);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF0D8ACE")));

        EstatisticasFragment estatisticas_fragment = new EstatisticasFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_navId, estatisticas_fragment).commit();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        View header = navigationView.getHeaderView(0);
        tvNomeId = header.findViewById(R.id.tvNomeId);
        tvEmailId = header.findViewById(R.id.tvEmailId);
        tvNomeId.setText(sUsuario.getNomeUsuario());
        tvEmailId.setText(sUsuario.getEmailUsuario());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                id = item.getItemId();
                if (id == R.id.bottom_estatiId) {
                    EstatisticasFragment estatisticas_fragment = new EstatisticasFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_navId, estatisticas_fragment).commit();
                    spinnerData.setVisibility(View.INVISIBLE);
                    bloqueio = false;
                    return true;
                }

                if (id == R.id.bottom_historicoId) {
                    HistoricoFragment historicoFragment = new HistoricoFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_navId, historicoFragment).commit();
                    myAdapter = new ArrayAdapter<String>(TelaInicialActivity.this,
                            R.layout.layout_spinner_item, getResources().getStringArray(R.array.tempo));

                    myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerData.setAdapter(myAdapter);
                    spinnerData.setVisibility(View.VISIBLE);
                    bloqueio = false;
                    return true;
                }

                if (id == R.id.bottom_prograId) {

                    if (bloqueio == false) {
                        ProgramarFragment programarFragment = new ProgramarFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_navId, programarFragment).commit();
                        spinnerData.setVisibility(View.INVISIBLE);
                        bloqueio = true;
                        return true;
                    }
                }

                if (id == R.id.bottom_grafico_diarioId) {
                    GraficoFragment graficoFragment = new GraficoFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_navId, graficoFragment).commit();
                    spinnerData.setVisibility(View.INVISIBLE);

                    bloqueio = false;
                    return true;
                }
                return false;
            }

        });

        buscaEstufaVolley();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Itens navegaçao lateral
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        AlertDialog.Builder builder = new AlertDialog.Builder(TelaInicialActivity.this);

        if (id == R.id.controleId) {
            Intent intent = new Intent(TelaInicialActivity.this, TelaInicialActivity.class);
            startActivity(intent);
        } else if (id == R.id.logoutId) {
            builder.setTitle("Sair");
            builder.setMessage("Deseja realmente sair?");

            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });

            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    queue.cancelAll("tagCancelar");
                    Intent intent = new Intent(TelaInicialActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

            builder.create();
            builder.show();

        } else if (id == R.id.configuraId) {
            Intent intent = new Intent(TelaInicialActivity.this, ContaActivity.class);
            Bundle dados = new Bundle();

            dados.putString("id", dados.toString());
            intent.putExtras(dados);
            startActivity(intent);
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        TelaInicialActivity.this.moveTaskToBack(true);
    }

    private void buscaEstufaVolley() {
        queue = Volley.newRequestQueue(this);
        final JSONObject[] jsonObject = new JSONObject[1];
        final JSONArray[] jsonArray = new JSONArray[1];

        UrlWeb urlDados = new UrlWeb();
        String Url = urlDados.getURL() + "BuscaEstufa.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonObject[0] = new JSONObject(response);
                            jsonArray[0] = jsonObject[0].getJSONArray("resposta_servidor");
                            JSONObject JO = jsonArray[0].getJSONObject(0);

                            numeroEstufa = JO.getInt("num_estufas");
                            int x = 0;
                            while (x < numeroEstufa) {
                                x++;
                                lstSource.add("Estufa " + x);
                            }

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TelaInicialActivity.this, R.layout.layout_spinner_item, lstSource);
                            ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                            spinner.setAdapter(spinnerArrayAdapter);

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
                });
        stringRequest.setTag("tagCancelar");
        queue.add(stringRequest);
    }
}


