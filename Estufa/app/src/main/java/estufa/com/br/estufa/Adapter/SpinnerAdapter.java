package estufa.com.br.estufa.Adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

public class SpinnerAdapter {

    private List<String> lstData;
    private Context context;
    private LayoutInflater inflater;

    /*
    public SpinnerAdapter(List<String> lstData, Context context) {
        this.lstData = lstData;
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return lstData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = convertView;
          if (convertView == null)
              view = inflater.inflate(R.layout.spinner_item,null);

        TextView textView = view.findViewById(R.id.textView12);
        textView.setText(lstData.get(position));

        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view = super.getDropDownView(position, convertView, parent);
        ConstraintLayout constraintLayout = (ConstraintLayout) view;
        TextView textView = (TextView) constraintLayout.findViewById(R.id.textView12);
        textView.setTextColor(Color.BLACK);
        textView.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));

        return view;
    }
    */
}
