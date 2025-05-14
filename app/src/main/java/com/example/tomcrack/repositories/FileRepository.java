package com.example.tomcrack.repositories;

import com.example.tomcrack.models.File;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.function.Consumer;

public class FileRepository {

    private final CollectionReference fileCollection;

    public FileRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        fileCollection = db.collection("files"); // Replace "files" with your Firestore collection name
    }

    // Create a new file
    public void createFile(File file, Consumer<Void> onSuccess, Consumer<Exception> onError) {
        fileCollection.add(file)
                .addOnSuccessListener(documentReference -> onSuccess.accept(null))
                .addOnFailureListener(onError::accept);
    }

    // Get a file by ID
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

    // Update a file
    public void updateFile(String id, File updatedFile, Consumer<Void> onSuccess, Consumer<Exception> onError) {
        fileCollection.document(id).set(updatedFile)
                .addOnSuccessListener(aVoid -> onSuccess.accept(null))
                .addOnFailureListener(onError::accept);
    }

    // Delete a file
    public void deleteFile(String id, Consumer<Void> onSuccess, Consumer<Exception> onError) {
        fileCollection.document(id).delete()
                .addOnSuccessListener(aVoid -> onSuccess.accept(null))
                .addOnFailureListener(onError::accept);
    }

    public void getRecentFilesByCategory(String categoryId, Consumer<List<File>> onSuccess, Consumer<Exception> onError) {
        fileCollection
                .whereEqualTo("category.id", categoryId)
                .orderBy("uploadDate", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<File> files = querySnapshot.toObjects(File.class);
                    onSuccess.accept(files);
                })
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
}