package com.example.notaspam2.ui.add;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.notaspam2.Adapter.NotaAdapter;
import com.example.notaspam2.Models.NotaModel;
import com.example.notaspam2.R;
import com.example.notaspam2.connection.FireBaseConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    private FirebaseFirestore db;
    private CollectionReference collectionReference;

    private NotaModel model;
    private ArrayList<NotaModel> NotaList;
    private NotaAdapter adapter;

    private Query query;

    private final String COLLECTION_NAME = "notas";

    EditText title, description;
    Button sendData;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        init(root);

        sendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strTitle, strDescription;
                strTitle = title.getText().toString();
                strDescription = description.getText().toString();

                if (strTitle.isEmpty() || strDescription.isEmpty()){
                    makeToast("Debe llenar todos los campos para continuar", Toast.LENGTH_SHORT);
                }else{
                    model = new NotaModel();
                    model.setTitle(strTitle);
                    model.setDescription(strDescription);
                    
                    save(model);
                }
            }
        });

        return root;
    }

    private void save(NotaModel model) {
        if (collectionReference != null){
            collectionReference.add(model)
                    .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()){
                                if(task.getResult() != null){
                                    makeToast("Nota guardada correctamente", Toast.LENGTH_SHORT);
                                    clear();
                                }else {
                                    makeToast("No se pudo guardar la nota", Toast.LENGTH_SHORT);
                                }
                            }else {
                                makeAlertDialog("ERROR", task.getException().getMessage(), "OK");
                                Log.e("BONAIRE", task.getException().toString());
                            }
                        }
                    });
        }else{
            makeAlertDialog("ERROR", "Error de conexión", "OK");
        }
    }

    private void clear(){
        title.setText("");
        description.setText("");

        title.requestFocus();
    }

    private void init(View v) {

        title = v.findViewById(R.id.txt_set_titulo);
        description = v.findViewById(R.id.editTextTextMultiLine);
        sendData = v.findViewById(R.id.btn_guardar);
        model = new NotaModel();
        db = FireBaseConnection.ConnectionFirestore();
        collectionReference = db.collection(COLLECTION_NAME);
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