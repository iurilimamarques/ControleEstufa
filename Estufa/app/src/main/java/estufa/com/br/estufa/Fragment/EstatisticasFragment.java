package estufa.com.br.estufa.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.otto.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import estufa.com.br.estufa.Activity.MainActivity;
import estufa.com.br.estufa.Objetos.IdEstufa;
import estufa.com.br.estufa.Objetos.Informacoes;
import estufa.com.br.estufa.Objetos.UrlWeb;
import estufa.com.br.estufa.PackageEventBus.EventBus;
import estufa.com.br.estufa.R;

public class EstatisticasFragment extends Fragment {

    private TextView temperatura;
    private TextView humidade;
    private Informacoes informacoes = Informacoes.getInstance();
    private Context context;
    private RequestQueue queue;
    private String data;
    private ImageView temperaturaTermometro;
    private ImageView umidadeTermometro;
    private View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_estatisticas, null);

        temperatura = view.findViewById(R.id.temperaturaId);
        humidade = view.findViewById(R.id.humidadeId);
        queue = Volley.newRequestQueue(context);
        temperaturaTermometro = view.findViewById(R.id.viewTermometroId);
        umidadeTermometro = view.findViewById(R.id.viewUmidadeId);

        ligacaoVolley();
        return (view);
    }

    private void ligacaoVolley() {
        UrlWeb urlDados = new UrlWeb();
        String UrlLogin = urlDados.getURL() + "AtualizaDados.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlLogin,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("resposta_servidor");
                            JSONObject JO = jsonArray.getJSONObject(0);

                            temperatura.setText(JO.getString("temperatura_estufa"));
                            humidade.setText(JO.getString("umidade_estufa"));


                            if (Integer.parseInt(JO.getString("temperatura_estufa")) >= 25) {
                                temperaturaTermometro.setImageDrawable(view.getResources().getDrawable(R.drawable.termometro_calor));
                            } else {
                                temperaturaTermometro.setImageDrawable(view.getResources().getDrawable(R.drawable.termometro_frio));
                            }

                            if (Integer.parseInt(JO.getString("umidade_estufa")) == 100) {
                                umidadeTermometro.setImageDrawable(view.getResources().getDrawable(R.drawable.umidade_maxima));
                            } else if (Integer.parseInt(JO.getString("umidade_estufa")) >= 30) {
                                umidadeTermometro.setImageDrawable(view.getResources().getDrawable(R.drawable.umidade_media));
                            } else {
                                umidadeTermometro.setImageDrawable(view.getResources().getDrawable(R.drawable.umidade_minima));
                            }
                            atualizaInfos();
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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id_estufa", Integer.toString(informacoes.getIdEstufa()));
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void atualizaInfos() {
        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (verificacaoConexao()) {
                    ligacaoVolley();
                } else if (!verificacaoConexao()) {
                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                }
            }
        }, 5000);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getBus().register(this);
        EventBus.getBus().post(new IdEstufa(informacoes.getIdEstufa()));
    }

    @Subscribe
    public void mensagemTransportada(IdEstufa idEstufa) {
        data = Integer.toString(informacoes.getIdEstufa());
        if (data != null) {
            ligacaoVolley();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getBus().unregister(this);
    }

    private boolean verificacaoConexao() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
