package estufa.com.br.estufa.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.squareup.otto.Subscribe;
import java.util.Date;
import estufa.com.br.estufa.Activity.GraficoActivity;
import estufa.com.br.estufa.Objetos.IdEstufa;
import estufa.com.br.estufa.Objetos.Informacoes;
import estufa.com.br.estufa.Objetos.SPreferences;
import estufa.com.br.estufa.Objetos.SUsuario;
import estufa.com.br.estufa.PackageEventBus.EventBus;
import estufa.com.br.estufa.R;

public class GraficoFragment extends Fragment {

    private TextView viewTempo;
    private TextView viewAno;
    private Context context;
    private int posicaoMes = 1;
    private int ano = 0;
    private Button botaoVoltarAno;
    private Button botaoProximoAno;
    private Button botaoVoltar;
    private Button botaoProximo;
    private SUsuario sUsuario = SUsuario.getInstance();
    private Informacoes informacoes = Informacoes.getInstance();
    private SPreferences sPreferences = SPreferences.getInstance();
    private Date data;
    private String idestufa;
    private Button botaoUmidadeId;
    private Button botaoTemperaturaId;
    private SharedPreferences.Editor editor;

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
        View view = inflater.inflate(R.layout.fragment_grafico, null);

        viewTempo = view.findViewById(R.id.viewMesesId);
        viewAno = view.findViewById(R.id.viewAnoId);
        botaoProximo = view.findViewById(R.id.btnProximoId);
        botaoVoltar = view.findViewById(R.id.btnAnteriorId);
        botaoTemperaturaId = view.findViewById(R.id.btnTemperaturaId);
        botaoUmidadeId = view.findViewById(R.id.btnUmidadeId);
        botaoProximoAno = view.findViewById(R.id.btnAnoProximoId);
        botaoVoltarAno = view.findViewById(R.id.btnAnoAnteriorId);
        editor = context.getSharedPreferences(sPreferences.getARQUIVO_GRAFICO_PREFERENCIA(), context.MODE_PRIVATE).edit();

        data = new Date();
        posicaoMes = data.getMonth();
        ano = (data.getYear() + 2000) - 100;

        final String[] mArray = getResources().getStringArray(R.array.meses);
        viewTempo.setText(mArray[posicaoMes]);
        viewAno.setText(Integer.toString(ano));

        botaoProximo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posicaoMes++;
                if (posicaoMes > 11) {
                    posicaoMes = 0;
                    viewTempo.setText(mArray[posicaoMes]);
                } else {
                    viewTempo.setText(mArray[posicaoMes]);
                }
            }
        });

        botaoVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                posicaoMes--;
                if (posicaoMes < 0) {
                    posicaoMes = 11;
                    viewTempo.setText(mArray[posicaoMes]);
                } else {
                    viewTempo.setText(mArray[posicaoMes]);
                }
            }
        });

        botaoProximoAno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ano++;
                viewAno.setText(Integer.toString(ano));
            }
        });

        botaoVoltarAno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ano--;
                viewAno.setText(Integer.toString(ano));
            }
        });

        botaoTemperaturaId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GraficoActivity.class);
                startActivity(intent);

                editor.putInt("id_estufa",informacoes.getIdEstufa());
                editor.putInt("id_usuario", sUsuario.getIdUsuario());
                editor.putInt("mes",posicaoMes + 1);
                editor.putBoolean("validacao", true);
                editor.putInt("ano", ano);
                editor.commit();
            }
        });

        botaoUmidadeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GraficoActivity.class);
                startActivity(intent);

                editor.putInt("id_estufa",informacoes.getIdEstufa());
                editor.putInt("id_usuario", sUsuario.getIdUsuario());
                editor.putInt("mes",posicaoMes + 1);
                editor.putBoolean("validacao", false);
                editor.putInt("ano", ano);
                editor.commit();
            }
        });

        return (view);
    }

    @Subscribe
    public void mensagemTransportada(IdEstufa idEstufa) {
        idestufa = Integer.toString(informacoes.getIdEstufa());
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getBus().unregister(this);
    }
}


