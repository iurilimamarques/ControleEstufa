package estufa.com.br.estufa.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.ankushgrover.hourglass.Hourglass;
import com.squareup.otto.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import estufa.com.br.estufa.Objetos.IdEstufa;
import estufa.com.br.estufa.Objetos.Informacoes;
import estufa.com.br.estufa.Objetos.SUsuario;
import estufa.com.br.estufa.Objetos.UrlWeb;
import estufa.com.br.estufa.PackageEventBus.EventBus;
import estufa.com.br.estufa.R;

public class ProgramarFragment extends Fragment {

    private EditText comentario;
    private View view;
    private TextView tvHora;
    private TextView tvMin;
    private TextView tvSec;
    private TextView viewAviso;
    private EditText editMin;
    private EditText editHora;
    private EditText editSec;
    private Button btnSalvar;
    private String tempo;
    private int horas, minutos, segundos;
    private Handler setDelay = new Handler();
    private Context context;
    private long totalSeg = 0;
    private SUsuario sUsuario = SUsuario.getInstance();
    private Informacoes informacoes = Informacoes.getInstance();
    private int posicao;
    private Hourglass hourglass;
    private Button botaoCancelar;
    private String status = "";
    private String tempoPadraoRestante = "";
    private boolean ativacao2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getBus().register(this);
        EventBus.getBus().post(new IdEstufa(informacoes.getIdEstufa()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_programar, container, false);

        comentario = view.findViewById(R.id.comentarioId);
        editHora = view.findViewById(R.id.editHoraId);
        editMin = view.findViewById(R.id.editMinId);
        editSec = view.findViewById(R.id.editSecId);
        btnSalvar = view.findViewById(R.id.btnSalvarId);
        viewAviso = view.findViewById(R.id.viewAvisoId);
        tvHora = view.findViewById(R.id.tvHoraId);
        tvMin = view.findViewById(R.id.tvMinId);
        tvSec = view.findViewById(R.id.tvSecId);
        botaoCancelar = view.findViewById(R.id.btnCancelarId);

        botaoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Irrigação");
                    builder.setMessage("Deseja cancelar irrigação?");

                    builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    });

                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            ligacaoVolley(3);
                        }
                    });
                    builder.create();
                    builder.show();
            }
        });

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    if ((editSec.getText().toString().equals("") && editMin.getText().toString().equals("") && editHora.getText().toString().equals(""))) {

                        viewAviso.setText("Prencha o tempo de funcionamento!");

                        Runnable startDelay = new Runnable() {
                            @Override
                            public void run() {
                                viewAviso.setText("");
                            }
                        };
                        setDelay.postDelayed(startDelay, 4000);

                    } else {
                        builder.setTitle("Irrigação");
                        builder.setMessage("Deseja iniciar irrigação?");

                        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                if (editSec.getText().toString().equals("")) {
                                    editSec.setText("00");
                                }
                                if (editMin.getText().toString().equals("")) {
                                    editMin.setText("00");
                                }
                                if (editHora.getText().toString().equals("")) {
                                    editHora.setText("00");
                                }
                                if (!editHora.getText().toString().equals("")
                                        && !editMin.getText().toString().equals("")
                                        && !editSec.getText().toString().equals("")) {

                                    tempo = editHora.getText().toString() + ":" + editMin.getText().toString() + ":" + editSec.getText().toString();

                                    totalSeg = Integer.parseInt(editSec.getText().toString()) * 1000;
                                    totalSeg = totalSeg + Integer.parseInt(editMin.getText().toString()) * 60000;
                                    totalSeg = totalSeg + Integer.parseInt(editHora.getText().toString()) * 3600000;

                                    ligacaoVolley(1);
                                }
                            }
                        });
                        builder.create();
                        builder.show();
                    }

                }
        });

        ligacaoVolley(2);
        return (view);
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

    private void ligacaoVolley(final int ativacao) {
        RequestQueue queue = Volley.newRequestQueue(context);
        final JSONObject[] jsonObject = new JSONObject[1];
        final JSONArray[] jsonArray = new JSONArray[1];

        if (ativacao == 1) {
            UrlWeb urlDados = new UrlWeb();
            String UrlDados = urlDados.getURL() + "IniciaIrrigacao.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlDados,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                String erro;
                                jsonObject[0] = new JSONObject(response);
                                jsonArray[0] = jsonObject[0].getJSONArray("resposta_servidor");
                                JSONObject JO = jsonArray[0].getJSONObject(0);

                                mensagemIrrigacao(JO.getString("mensagem"));

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

                    params.put("comentario", comentario.getText().toString());
                    params.put("tempo_funcionamento", tempo.toString());
                    params.put("id_usuario", Integer.toString(sUsuario.getIdUsuario()));
                    params.put("id_estufa", Integer.toString(posicao));
                    return params;
                }
            };
            queue.add(stringRequest);

        } else if (ativacao == 2) {
            UrlWeb urlDados = new UrlWeb();
            String UrlDados = urlDados.getURL() +
                    "SelecionaTempoAlt.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlDados,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                String erro;
                                jsonObject[0] = new JSONObject(response);
                                jsonArray[0] = jsonObject[0].getJSONArray("resposta_servidor");
                                JSONObject JO = jsonArray[0].getJSONObject(0);

                                totalSeg = Integer.parseInt(JO.getString("tempo_milli"));
                                status = JO.getString("status");

                               if (status.equals("A") && totalSeg > 0){
                                   /*if (hourglass.isRunning()) {
                                       hourglass.stopTimer();
                                   }else{
                                   */
                                       startTimer();
                                       ativacao2 = true;
                                       //hourglass = null;
                                       desabilitaEdicao();
                                }else if (status.equals("inativo")){
                                    totalSeg = 0;
                                    ativacao2 = false;
                                    habilitaEdicao();
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
                    params.put("idestufa", Integer.toString(posicao));
                    params.put("idusuario", Integer.toString(sUsuario.getIdUsuario()));
                    return params;
                }
            };
            queue.add(stringRequest);

        }else if (ativacao == 3){
            UrlWeb urlDados = new UrlWeb();
            String UrlDados = urlDados.getURL() +
                    "CancelaIrrigacao.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, UrlDados,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                String erro;
                                jsonObject[0] = new JSONObject(response);
                                jsonArray[0] = jsonObject[0].getJSONArray("resposta_servidor");
                                JSONObject JO = jsonArray[0].getJSONObject(0);

                                String resposta = JO.getString("mensagem");

                                if (resposta.equals("cancelado")){
                                    habilitaEdicao();
                                    if (hourglass != null) {
                                        hourglass.setTime(0);
                                        hourglass.stopTimer();
                                    }
                                    totalSeg = 0;
                                    ativacao2 = false;
                                }else if (resposta.equals("erro")){
                                    viewAviso.setText("Não foi possivel cancelar!");
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
                    params.put("idestufa", Integer.toString(posicao));
                    params.put("idusuario", Integer.toString(sUsuario.getIdUsuario()));
                    return params;
                }
            };
            queue.add(stringRequest);

        }
    }

    private void mensagemIrrigacao(String mensagem) {
        if (mensagem.equals("iniciado")) {

            Runnable startDelay = new Runnable() {
                @Override
                public void run() {
                    viewAviso.setText("");
                }

            };
            setDelay.postDelayed(startDelay, 3000);
            startTimer();

            btnSalvar.setEnabled(false);
            ativacao2 = true;
            botaoCancelar.setEnabled(true);
        }else if (mensagem.equals("erro")) {
            viewAviso.setText("Não foi possivel iniciar!");
            Runnable startDelay = new Runnable() {
                @Override
                public void run() {
                    viewAviso.setText("");
                }
            };
            setDelay.postDelayed(startDelay, 4000);
        }
    }


    private void startTimer() {
        hourglass = new Hourglass(totalSeg, 1000) {
            @Override
            public void onTimerTick(long timeRemaining) {

                horas = (int) (timeRemaining / 3600000);
                int restoHora = (int) (timeRemaining % 3600000);
                minutos = (int) (restoHora / 60000);
                int restominuto = (int) (restoHora % 60000);
                segundos = (int) (restominuto / 1000);

                if (ativacao2) {
                    editHora.setText(Integer.toString(horas));
                    editMin.setText(Integer.toString(minutos));
                    editSec.setText(Integer.toString(segundos));
                    desabilitaEdicao();
                }
                if (horas == 0) {
                    horas = 00;
                }
                if (minutos == 0) {
                    minutos = 00;
                }
                if (segundos == 0) {
                    minutos = 00;
                }
                tempoPadraoRestante = horas + ":" + minutos + ":" + segundos;

                totalSeg = timeRemaining;
            }

            @Override
            public void onTimerFinish() {
                habilitaEdicao();
            }
        };
        hourglass.startTimer();
    }

    @Subscribe
    public void mensagemTransportada(IdEstufa idEstufa) {
        posicao = informacoes.getIdEstufa();
        ligacaoVolley(2);

        if (totalSeg > 0) {
            hourglass.stopTimer();
            hourglass.setTime(0);
            habilitaEdicao();
            totalSeg = 0;
        }

    }

    private void habilitaEdicao() {
        editHora.setEnabled(true);
        editMin.setEnabled(true);
        editSec.setEnabled(true);
        comentario.setEnabled(true);

        editSec.setTextColor(Color.WHITE);
        editMin.setTextColor(Color.WHITE);
        editHora.setTextColor(Color.WHITE);
        editSec.setText("");
        editMin.setText("");
        editHora.setText("");

        horas = 0;
        minutos = 0;
        segundos = 0;

        tvHora.setTextColor(Color.WHITE);
        tvMin.setTextColor(Color.WHITE);
        tvSec.setTextColor(Color.WHITE);

        botaoCancelar.setEnabled(false);
        btnSalvar.setEnabled(true);
        btnSalvar.setText("Iniciar irrigação");
        comentario.setText("");
    }

    private void desabilitaEdicao() {
        editHora.setEnabled(false);
        editMin.setEnabled(false);
        editSec.setEnabled(false);
        comentario.setEnabled(false);

        editSec.setTextColor(Color.GRAY);
        editMin.setTextColor(Color.GRAY);
        editHora.setTextColor(Color.GRAY);

        tvHora.setTextColor(Color.GRAY);
        tvMin.setTextColor(Color.GRAY);
        tvSec.setTextColor(Color.GRAY);

        btnSalvar.setEnabled(false);
        botaoCancelar.setEnabled(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getBus().unregister(this);
    }
}
