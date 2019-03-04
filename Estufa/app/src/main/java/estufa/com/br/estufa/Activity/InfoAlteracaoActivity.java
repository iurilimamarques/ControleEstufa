package estufa.com.br.estufa.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import estufa.com.br.estufa.Objetos.SInformacoesHisorico;
import estufa.com.br.estufa.R;

public class InfoAlteracaoActivity extends AppCompatActivity {

    private SInformacoesHisorico sHistorico = SInformacoesHisorico.getInstance();
    private TextView tvEstufa;
    private TextView tvDataIrrigacao;
    private TextView tvHoraIrrigacao;
    private TextView tvDuracao;
    private TextView tvStatus;
    private TextView tvComentario;
    private Button botaoSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_alteracao);

        int posicao = sHistorico.getPosicao();
        String comentario = sHistorico.getComentario().get(posicao);
        String status = sHistorico.getStatus().get(posicao);

        tvEstufa = findViewById(R.id.tvEstufaId);
        tvDataIrrigacao = findViewById(R.id.tvDataIrrigacaoId);
        tvHoraIrrigacao = findViewById(R.id.tvHoraIrrigacaoId);
        tvDuracao = findViewById(R.id.tvDuracaoIrrigacaoId);
        tvStatus = findViewById(R.id.tvStatusIrrigacaoId);
        tvComentario = findViewById(R.id.tvComentarioId);
        botaoSair = findViewById(R.id.btnSairId);

        tvEstufa.setText(sHistorico.getIdestufa().get(posicao));
        tvDataIrrigacao.setText(sHistorico.getData_alteracao().get(posicao));
        tvHoraIrrigacao.setText(sHistorico.getHora_alteracao().get(posicao));
        tvDuracao.setText(sHistorico.getDuracao().get(posicao));
        tvStatus.setText(sHistorico.getStatus().get(posicao));

        if (status.equals("A")) {
            tvStatus.setTextColor(Color.rgb(59, 171, 33));
            tvStatus.setText("Em andamento.");
        } else {
            tvStatus.setTextColor(Color.RED);
            tvStatus.setText("Encerrado.");
        }

        if (comentario.equals("")) {
            tvComentario.setText("Não há observações.");
        } else {
            tvComentario.setText(comentario);
        }

        botaoSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoAlteracaoActivity.this, TelaInicialActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("id", R.id.bottom_historicoId);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
