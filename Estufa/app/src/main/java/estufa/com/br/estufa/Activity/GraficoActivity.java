package estufa.com.br.estufa.Activity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import estufa.com.br.estufa.Objetos.SPreferences;
import estufa.com.br.estufa.Objetos.UrlWeb;
import estufa.com.br.estufa.R;

public class GraficoActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SPreferences sPreferences = SPreferences.getInstance();
    private JSONObject jsonObject;
    private JSONArray jsonArray;
    private int pontosTemperatura[];
    private int pontosRetaX[];
    private int pontosUmidade[];
    private int id_estufa;
    private int id_usuario;
    private int mes;
    private int ano;
    private boolean validacao;
    private LineChart grafico;
    private List<Entry> entradaGrafico = new ArrayList<Entry>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        sharedPreferences = getSharedPreferences(sPreferences.getARQUIVO_GRAFICO_PREFERENCIA(), MODE_PRIVATE);
        id_estufa = sharedPreferences.getInt("id_estufa",0);
        id_usuario = sharedPreferences.getInt("id_usuario", 0);
        mes = sharedPreferences.getInt("mes", 0);
        validacao = sharedPreferences.getBoolean("validacao",false);
        ano = sharedPreferences.getInt("ano", 0);

        grafico = findViewById(R.id.graficoId);

        buscaDatasVolley();
    }


    private void buscaDatasVolley() {
        if (validacao) {
            RequestQueue queue = Volley.newRequestQueue(GraficoActivity.this);
            UrlWeb urlDados = new UrlWeb();
            String url = urlDados.getURL() + "HistoricoTemperaturaDiario.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                jsonObject = new JSONObject(response);
                                jsonArray = jsonObject.getJSONArray("resposta_servidor");
                                int count = 0;
                                pontosRetaX = new int[jsonArray.length()];
                                pontosTemperatura = new int[jsonArray.length()];

                                while (count < jsonArray.length()) {
                                    JSONObject JO = jsonArray.getJSONObject(count);
                                    int dia = Integer.parseInt(JO.getString("dia"));
                                    int mediaTemperatura = Integer.parseInt(JO.getString("media_temperatura"));

                                    pontosRetaX[count] = dia;
                                    pontosTemperatura[count] = mediaTemperatura;

                                    entradaGrafico.add(new Entry(pontosRetaX[count],pontosTemperatura[count]));

                                    count++;
                                }

                                LineDataSet dataSet = new LineDataSet(entradaGrafico, "Temperatura");
                                dataSet.setColor(Color.rgb(13, 138, 206));
                                dataSet.setValueTextColor(Color.BLACK);

                                LineData lineData = new LineData(dataSet);
                                grafico.setData(lineData);
                                grafico.invalidate();
                                grafico.setBackgroundColor(Color.argb(68, 174, 223, 250));
                                grafico.setVisibleXRangeMaximum(31);
                                grafico.setScaleEnabled(false);
                                grafico.zoom(3.5f,0,0,0);
                                grafico.getAxisLeft().setEnabled(false);
                                grafico.setDrawBorders(true);
                                grafico.setBorderColor(Color.rgb(13, 138, 206));
                                grafico.setNoDataText("Nenhuma informação disponivel.");

                                XAxis xAxis = grafico.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.TOP);
                                xAxis.setGranularity(1f);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("id_estufa", String.valueOf(id_estufa));
                    params.put("idusuario", String.valueOf(id_usuario));
                    params.put("mes", String.valueOf(mes));
                    params.put("ano", String.valueOf(ano));

                    return params;
                }
            };

            queue.add(stringRequest);
        }else if (!validacao){

            RequestQueue queue = Volley.newRequestQueue(GraficoActivity.this);
            UrlWeb urlDados = new UrlWeb();
            String url = urlDados.getURL() + "HistoricoUmidadeDiario.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                jsonObject = new JSONObject(response);
                                jsonArray = jsonObject.getJSONArray("resposta_servidor");
                                int count = 0;
                                pontosRetaX = new int[jsonArray.length()];
                                pontosUmidade = new int[jsonArray.length()];

                                while (count < jsonArray.length()) {
                                    JSONObject JO = jsonArray.getJSONObject(count);
                                    int dia = Integer.parseInt(JO.getString("dia"));
                                    int mediaUmidade = Integer.parseInt(JO.getString("media_umidade"));

                                    pontosRetaX[count] = dia;
                                    pontosUmidade[count] = mediaUmidade;

                                    entradaGrafico.add(new Entry(pontosRetaX[count],pontosUmidade[count]));

                                    count++;
                                }

                                LineDataSet dataSet = new LineDataSet(entradaGrafico, "Umidade");
                                dataSet.setColor(Color.rgb(13, 138, 206));
                                dataSet.setValueTextColor(Color.BLACK);

                                LineData lineData = new LineData(dataSet);
                                grafico.setData(lineData);
                                grafico.invalidate();
                                grafico.setBackgroundColor(Color.argb(68, 174, 223, 250));
                                grafico.setVisibleXRangeMaximum(31);
                                grafico.setScaleEnabled(false);
                                grafico.zoom(3.5f,0,0,0);
                                grafico.getAxisLeft().setEnabled(false);
                                grafico.setDrawBorders(true);
                                grafico.setBorderColor(Color.rgb(13, 138, 206));
                                grafico.setNoDataText("Nenhuma informação disponivel.");

                                XAxis xAxis = grafico.getXAxis();
                                xAxis.setPosition(XAxis.XAxisPosition.TOP);
                                xAxis.setGranularity(1f);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("id_estufa", String.valueOf(id_estufa));
                    params.put("idusuario", String.valueOf(id_usuario));
                    params.put("mes", String.valueOf(mes));
                    params.put("ano", String.valueOf(ano));

                    return params;
                }
            };

            queue.add(stringRequest);
        }
    }
}
