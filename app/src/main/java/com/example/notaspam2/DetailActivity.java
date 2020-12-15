package com.example.notaspam2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notaspam2.Models.NotaModel;
import com.example.notaspam2.connection.FireBaseConnection;
import com.example.notaspam2.ui.list.NotificationsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.notaspam2.ui.list.NotificationsFragment.COLLECTION_NAME;

public class DetailActivity extends AppCompatActivity {

    String strId, strTitle, strDescription;
    private EditText etTitle, etDescription;
    private Button btnUpdate;

    private NotificationsViewModel notificationsViewModel;

    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private NotaModel model;
    private ArrayList<NotaModel> NotaList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
    }

    private void init() {
        strId = getIntent().getStringExtra("id");
        strTitle = getIntent().getStringExtra("title");
        strDescription = getIntent().getStringExtra("description");

        etTitle = (EditText)findViewById(R.id.et_title_detail);
        etDescription = (EditText)findViewById(R.id.et_description_detail);
        btnUpdate = (Button)findViewById(R.id.btn_update);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update(model);
            }
        });

        if (strTitle != null){
            etTitle.setText(strTitle);
        }else {
            etTitle.setText("Error");
        }

        if (strDescription != null){
            etDescription.setText(strDescription);
        }else {
            etDescription.setText("Error");
        }

        model = new NotaModel();
        db = FireBaseConnection.ConnectionFirestore();
        collectionReference = db.collection(COLLECTION_NAME);
    }

    private void updateUi(){

        collectionReference
                .document(strId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                                model = document.toObject(NotaModel.class);
                                model.setFbId(document.getId());
                        }
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUi();
    }

    private void update(NotaModel update) {
        if (update==null){
            Toast.makeText(DetailActivity.this, "Error al actualizar", Toast.LENGTH_SHORT).show();
            return;
        }

        model.set_id(update.get_id());
        model.setTitle(etTitle.getText().toString());
        model.setDescription(etDescription.getText().toString());
        model.setFbId(update.getFbId());
        collectionReference
                .document(strId)
                .set(update).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(DetailActivity.this, "Actualizacion exitosa", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(DetailActivity.this, "Actualizacion fallida", Toast.LENGTH_SHORT).show();
                }
                updateUi();
            }
        });
    }

    protected void makeAlertDialog(String title, String message, String buttonText){

        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
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