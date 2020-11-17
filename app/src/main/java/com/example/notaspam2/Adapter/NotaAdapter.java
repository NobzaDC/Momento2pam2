package com.example.notaspam2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.notaspam2.Models.NotaModel;
import com.example.notaspam2.R;

import java.util.ArrayList;

public class NotaAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NotaModel> modelArrayList;

    public NotaAdapter(Context context, ArrayList<NotaModel> modelArrayList) {
        this.context = context;
        this.modelArrayList = modelArrayList;
    }

    @Override
    public int getCount() {
        return modelArrayList.size();
    }

    @Override
    public NotaModel getItem(int position) {
        return modelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            LayoutInflater layoutInflater = LayoutInflater.from(this.context);
            convertView = layoutInflater.inflate(R.layout.list_nota_model, parent, true);
        }

        TextView title = convertView.findViewById(R.id.txt_titulo_list_view),
                description = convertView.findViewById(R.id.txt_descripcion_list_view);

        title.setText(getItem(position).getTitle());
        description.setText(getItem(position).getDescription());
        return convertView;
    }
}
