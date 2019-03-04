package estufa.com.br.estufa.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import estufa.com.br.estufa.Activity.InfoAlteracaoActivity;
import estufa.com.br.estufa.Adapter.ListaAdapter;
import estufa.com.br.estufa.Objetos.IdData;
import estufa.com.br.estufa.Objetos.IdEstufa;
import estufa.com.br.estufa.Objetos.Informacoes;
import estufa.com.br.estufa.Objetos.SInformacoesHisorico;
import estufa.com.br.estufa.Objetos.SUsuario;
import estufa.com.br.estufa.Objetos.UrlWeb;
import estufa.com.br.estufa.PackageEventBus.EventBus;
import estufa.com.br.estufa.R;

public class HistoricoFragment extends Fragment {

    private ListView listView;
    private View view;
    private Context context;
    private JSONArray jsonArray;
    private JSONObject jsonObject;
    private SUsuario sUsuario = SUsuario.getInstance();
    private SInformacoesHisorico sHistorico = SInformacoesHisorico.getInstance();
    private int posicaoIdEstufa;
    private int valorData;
    private Informacoes informacoes = Informacoes.getInstance();
    private List<String> listDataAlt;
    private List<String> listEstufa;
    private List<String> listHoraAlt;
    private List<String> listTempoFunc;
    private List<String> listComentario;
    private List<String> listStatus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_historico, null);
        listView = (ListView) view.findViewById(R.id.listaId);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sHistorico.setPosicao(i);
                Intent intent = new Intent(context, InfoAlteracaoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void ligacaoVolley() {
        UrlWeb urlDados = new UrlWeb();
        String UrlHistorico = urlDados.getURL() + "HistoricoModificacoes.php";
        RequestQueue queue = Volley.newRequestQueue(context);
        listDataAlt = new ArrayList<String>();
        listEstufa = new ArrayList<String>();
        listStatus = new ArrayList<String>();
        listHoraAlt = new ArrayList<String>();
        listTempoFunc = new ArrayList<String>();
        listComentario = new ArrayList<String>();

        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.layout_progress_bar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Carregando...");
        builder.setCancelable(false);
        builder.setView(subView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlHistorico,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("resposta_servidor");
                            alertDialog.dismiss();
                            int count = 0;

                            while (count < jsonArray.length()) {
                                JSONObject JO = jsonArray.getJSONObject(count);

                                if (jsonArray.length() > listEstufa.size()) {
                                    listDataAlt.add(JO.getString("data"));
                                    listEstufa.add(JO.getString("idestufa"));
                                    listComentario.add(JO.getString("comentario"));
                                    listStatus.add(JO.getString("status"));
                                    listTempoFunc.add(JO.getString("funcionamento"));
                                    listHoraAlt.add(JO.getString("horario"));
                                } else {
                                    break;
                                }
                                count++;
                            }
                            sHistorico.setComentario(listComentario);
                            sHistorico.setData_alteracao(listDataAlt);
                            sHistorico.setDuracao(listTempoFunc);
                            sHistorico.setHora_alteracao(listHoraAlt);
                            sHistorico.setIdestufa(listEstufa);
                            sHistorico.setStatus(listStatus);

                            listView.setAdapter(new ListaAdapter(context, listEstufa, listDataAlt, listStatus,listHoraAlt));

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
                params.put("id_usuario", Integer.toString(sUsuario.getIdUsuario()));
                params.put("id_estufa", Integer.toString(posicaoIdEstufa));
                params.put("tempo", Integer.toString(valorData));
                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Subscribe
    public void mensagemTransportada(IdEstufa idEstufa) {
        posicaoIdEstufa = informacoes.getIdEstufa();
        if (posicaoIdEstufa != 0) {
            ligacaoVolley();
        }
    }

    @Subscribe
    public void mensagemTransportadaIdData(IdData idData) {
        valorData = informacoes.getValor();
        if (valorData != 0) {
            ligacaoVolley();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getBus().register(this);
        EventBus.getBus().post(new IdEstufa(informacoes.getIdEstufa()));
        EventBus.getBus().post(new IdData(informacoes.getValor()));
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getBus().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
