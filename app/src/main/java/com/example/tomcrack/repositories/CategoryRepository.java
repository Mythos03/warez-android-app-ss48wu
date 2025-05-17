package com.example.tomcrack.repositories;

import com.example.tomcrack.models.Category;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CategoryRepository {

    private final CollectionReference categoryCollection;
    private final List<Category> cachedCategories;

    public CategoryRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        categoryCollection = db.collection("categories");
        cachedCategories = new ArrayList<>();
    }

    public void createCategory(Category category, Consumer<Void> onSuccess, Consumer<Exception> onError) {
        categoryCollection.add(category)
                .addOnSuccessListener(documentReference -> onSuccess.accept(null))
                .addOnFailureListener(onError::accept);
    }

    public void fetchCategories(Consumer<List<Category>> onSuccess, Consumer<Exception> onError) {
        categoryCollection.get()
                .addOnSuccessListener(querySnapshot -> {
                    cachedCategories.clear();
                    for (var document : querySnapshot.getDocuments()) {
                        Category category = document.toObject(Category.class);
                        cachedCategories.add(category);
                    }
                    onSuccess.accept(new ArrayList<>(cachedCategories));
                })
                .addOnFailureListener(onError::accept);
    }

    public List<Category> getCachedCategories() {
        return new ArrayList<>(cachedCategories);
    }
}