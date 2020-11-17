package com.example.notaspam2.connection;

import com.google.firebase.firestore.FirebaseFirestore;

public class FireBaseConnection {

    private static FirebaseFirestore db;

    public static FirebaseFirestore ConnectionFirestore(){
        return db = FirebaseFirestore.getInstance();
    }
}
