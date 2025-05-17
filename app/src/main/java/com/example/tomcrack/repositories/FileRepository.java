package com.example.tomcrack.repositories;

import com.example.tomcrack.models.File;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FileRepository {

    private final CollectionReference fileCollection;

    public FileRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        fileCollection = db.collection("files");
    }

    public void createFile(File file, Consumer<Void> onSuccess, Consumer<Exception> onError) {
        fileCollection.add(file)
                .addOnSuccessListener(documentReference -> onSuccess.accept(null))
                .addOnFailureListener(onError::accept);
    }

    public void getAllFiles(OnSuccessListener<List<File>> onSuccess, OnFailureListener onFailure) {
        fileCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<File> files = new ArrayList<>();
                    queryDocumentSnapshots.getDocuments().forEach(document -> {
                        File file = document.toObject(File.class);
                        if (file != null) {
                            file.setId(document.getId());
                            files.add(file);
                        }
                    });
                    onSuccess.onSuccess(files);
                })
                .addOnFailureListener(onFailure);
    }

    public void getFileById(String id, Consumer<File> onSuccess, Consumer<Exception> onError) {
        fileCollection.document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        File file = documentSnapshot.toObject(File.class);
                        onSuccess.accept(file);
                    } else {
                        onError.accept(new Exception("File not found"));
                    }
                })
                .addOnFailureListener(onError::accept);
    }

    public void updateFile(String id, File updatedFile, Consumer<Void> onSuccess, Consumer<Exception> onError) {
        fileCollection.document(id).set(updatedFile)
                .addOnSuccessListener(aVoid -> onSuccess.accept(null))
                .addOnFailureListener(onError::accept);
    }

    public void deleteFile(String id, Consumer<Void> onSuccess, Consumer<Exception> onError) {
        fileCollection.document(id).delete()
                .addOnSuccessListener(aVoid -> onSuccess.accept(null))
                .addOnFailureListener(onError::accept);
    }

    public void searchFilesByName(String query, Consumer<List<File>> onSuccess, Consumer<Exception> onError) {
        fileCollection
                .whereGreaterThanOrEqualTo("name", query)
                .whereLessThanOrEqualTo("name", query + "\uf8ff")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<File> files = querySnapshot.toObjects(File.class);
                    onSuccess.accept(files);
                })
                .addOnFailureListener(onError::accept);
    }

    public void getTopFilesUploadedByUser(String userId, Consumer<List<File>> onSuccess, Consumer<Exception> onError) {
        fileCollection
                .whereEqualTo("uploader.id", userId)
                .orderBy("uploadDate", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<File> files = querySnapshot.toObjects(File.class);
                    onSuccess.accept(files);
                })
                .addOnFailureListener(onError::accept);
    }

    public void getFilesByCategory(String categoryName, Consumer<List<File>> onSuccess, Consumer<Exception> onError) {
        fileCollection
                .whereEqualTo("category.name", categoryName)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<File> files = new ArrayList<>();
                    for (var document : querySnapshot.getDocuments()) {
                        File file = document.toObject(File.class);
                        files.add(file);
                    }
                    onSuccess.accept(files);
                })
                .addOnFailureListener(onError::accept);
    }
}