package com.example.tomcrack.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;

public class CleanupReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        // Calculate the date 30 days ago
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -30);
        Date cutoffDate = calendar.getTime();

        // Query Firestore for files older than 30 days and delete them
        firestore.collection("files")
                .whereLessThan("uploadDate", cutoffDate)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    querySnapshot.getDocuments().forEach(document -> {
                        firestore.collection("files").document(document.getId()).delete();
                    });
                })
                .addOnFailureListener(e -> {
                    // Log or handle the error
                });
    }
}