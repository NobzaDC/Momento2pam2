package com.example.notaspam2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.notaspam2.Models.NotaModel;
import com.example.notaspam2.R;

import java.util.ArrayList;
import java.util.List;

public class NotaAdapter extends ArrayAdapter<NotaModel> {

    private ArrayList<NotaModel> model;
    private Context context;
    private int resourceLayout;

    public NotaAdapter(@NonNull Context context, int resource, @NonNull ArrayList<NotaModel> objects) {
        super(context, resource, objects);
        this.model = objects;
        this.context = context;
        this.resourceLayout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null){
                view = LayoutInflater.from(context).inflate(R.layout.list_nota_model, null);
            }

            NotaModel notaModel = model.get(position);

            TextView txt_titulo_list_view = view.findViewById(R.id.txt_titulo_list_view);
            TextView description = view.findViewById(R.id.txt_descripcion_list_view);
            txt_titulo_list_view.setText(notaModel.getTitle());
            description.setText(notaModel.getDescription());

            return view;
    }
}
