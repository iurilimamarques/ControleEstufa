package estufa.com.br.estufa.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import estufa.com.br.estufa.R;

public class ListaAdapter extends BaseAdapter {

    private Context context;
    private List<String> listEstufa;
    private List<String> listData;
    private List<String> listStatus;
    private List<String> listHora;
    private LayoutInflater inflater;

    public ListaAdapter(Context context, List<String> listEstufa, List<String> listData, List<String> listStatus,List<String> listHora) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listEstufa = listEstufa;
        this.listStatus = listStatus;
        this.listData = listData;
        this.listHora = listHora;
    }

    @Override
    public int getCount() {
        return listEstufa.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        return listEstufa.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ListaHolder listaHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.layout_listview, viewGroup, false);
            listaHolder = new ListaHolder();
            listaHolder.tx_idestufa = (TextView) view.findViewById(R.id.tvEstufaId);
            listaHolder.tx_data_alteracao = (TextView) view.findViewById(R.id.tvDataId);
            listaHolder.tx_hora_alteracao = view.findViewById(R.id.tvHoraId);
            listaHolder.tx_status = view.findViewById(R.id.tvStatusId);
            view.setTag(listaHolder);
        } else {
            listaHolder = (ListaHolder) view.getTag();
        }

        if (listStatus.get(i).equals("A")) {
            listaHolder.tx_status.setText("Em andamento.");
            listaHolder.tx_status.setTextColor(Color.rgb(59, 171, 33));
        } else if (listStatus.get(i).equals("I")){
            listaHolder.tx_status.setText("Encerrado.");
            listaHolder.tx_status.setTextColor(Color.RED);
        }
        listaHolder.tx_hora_alteracao.setText(listHora.get(i));
        listaHolder.tx_data_alteracao.setText(listData.get(i));
        listaHolder.tx_idestufa.setText(listEstufa.get(i));

        return view;
    }

    static class ListaHolder {
        TextView tx_data_alteracao,tx_hora_alteracao, tx_idestufa, tx_status;
    }
}
