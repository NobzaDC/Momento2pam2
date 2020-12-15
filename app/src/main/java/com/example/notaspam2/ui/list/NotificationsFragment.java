package com.example.notaspam2.ui.list;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.notaspam2.Adapter.NotaAdapter;
import com.example.notaspam2.DetailActivity;
import com.example.notaspam2.Models.NotaModel;
import com.example.notaspam2.R;
import com.example.notaspam2.connection.FireBaseConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;

    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    private NotaModel model;
    private ArrayList<NotaModel> NotaList;
    private NotaAdapter adapter;

    private Query query;

    public static String COLLECTION_NAME = "notas";
    ListView ListViewNotas;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        init(root);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getNotas();
    }

    private void init(View root) {
        ListViewNotas = root.findViewById(R.id.ListViewNotas);
        model = new NotaModel();
        db = FireBaseConnection.ConnectionFirestore();
        collectionReference = db.collection(COLLECTION_NAME);

        ListViewNotas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToDetail(String.valueOf(NotaList.get(position).getFbId()), String.valueOf(NotaList.get(position).getTitle()), String.valueOf(NotaList.get(position).getDescription()));
            }
        });
    }

    private void goToDetail(String id, String title, String description) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("description", description);
        startActivity(intent);
    }


    private void getNotas(){
        if (collectionReference!=null){
            collectionReference.get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult() != null){
                                    NotaList = new ArrayList<>();
                                    for (QueryDocumentSnapshot document: task.getResult()){
                                        model = document.toObject(NotaModel.class);
                                        model.setFbId(document.getId());
                                        NotaList.add(model);
                                    }

                                    if (NotaList.size() > 0){
                                        paintNotas(NotaList);
                                    }else {
                                        makeAlertDialog("Error", "Notas no encontradas", "OK");
                                    }

                                }else{
                                    makeAlertDialog("Error", "Notas no encontradas", "OK");
                                }
                            }else{
                                makeAlertDialog("Error", task.getException().getMessage(), "OK");
                            }
                        }
                    });
        }else {
            makeToast("Error de conexión", Toast.LENGTH_SHORT);
        }
    }

    private void paintNotas(ArrayList<NotaModel> notaList) {
        if (notaList.size()>=1){
            adapter = new NotaAdapter(getContext(), R.layout.list_nota_model, notaList);
            ListViewNotas.setAdapter(adapter);
        }
    }


    protected void makeToast(String message, int duration){
        Toast.makeText(getContext(), message, duration).show();
    }

    protected void makeAlertDialog(String title, String message, String buttonText){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title).setMessage(message);
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}