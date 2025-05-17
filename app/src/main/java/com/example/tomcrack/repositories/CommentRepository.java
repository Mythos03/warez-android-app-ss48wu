package com.example.tomcrack.repositories;

import com.example.tomcrack.models.Comment;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CommentRepository {

    private final CollectionReference commentCollection;

    public CommentRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        commentCollection = db.collection("comments");
    }

    public void addComment(Comment comment, Consumer<Void> onSuccess, Consumer<Exception> onError) {
        commentCollection.add(comment)
                .addOnSuccessListener(documentReference -> onSuccess.accept(null))
                .addOnFailureListener(onError::accept);
    }

    public void getCommentsForFile(String fileId, Consumer<List<Comment>> onSuccess, Consumer<Exception> onError) {
        commentCollection.whereEqualTo("file.id", fileId)
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<Comment> comments = new ArrayList<>();
                    for (var document : querySnapshot.getDocuments()) {
                        Comment comment = document.toObject(Comment.class);
                        comments.add(comment);
                    }
                    onSuccess.accept(comments);
                })
                .addOnFailureListener(onError::accept);
    }

    // Delete a comment by ID
    public void deleteComment(String commentId, Consumer<Void> onSuccess, Consumer<Exception> onError) {
        commentCollection.document(commentId).delete()
                .addOnSuccessListener(aVoid -> onSuccess.accept(null))
                .addOnFailureListener(onError::accept);
    }
}