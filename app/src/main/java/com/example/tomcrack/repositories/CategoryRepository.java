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

    // Fetch all categories from Firestore and cache them
    public void fetchCategories(Consumer<List<Category>> onSuccess, Consumer<Exception> onError) {
        categoryCollection.get()
                .addOnSuccessListener(querySnapshot -> {
                    cachedCategories.clear();
                    for (var document : querySnapshot.getDocuments()) {
                        Category category = document.toObject(Category.class);
                        cachedCategories.add(category);
                    }
                    onSuccess.accept(cachedCategories);
                })
                .addOnFailureListener(onError::accept);
    }

    // Get locally cached categories
    public List<Category> getCachedCategories() {
        return new ArrayList<>(cachedCategories);
    }
}