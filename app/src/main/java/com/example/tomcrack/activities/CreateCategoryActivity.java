package com.example.tomcrack.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tomcrack.R;
import com.example.tomcrack.models.Category;
import com.example.tomcrack.repositories.CategoryRepository;

public class CreateCategoryActivity extends AppCompatActivity {

    private EditText categoryNameEditText;
    private CategoryRepository categoryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        categoryNameEditText = findViewById(R.id.categoryNameEditText);
        Button saveCategoryButton = findViewById(R.id.saveCategoryButton);

        categoryRepository = new CategoryRepository();

        saveCategoryButton.setOnClickListener(v -> saveCategory());
    }

    private void saveCategory() {
        String categoryName = categoryNameEditText.getText().toString().trim();

        if (TextUtils.isEmpty(categoryName)) {
            Toast.makeText(this, "Category name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Category category = new Category(categoryName);

        categoryRepository.createCategory(category, aVoid -> {
            Toast.makeText(this, "Category created successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CreateCategoryActivity.this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }, e -> {
            Toast.makeText(this, "Failed to create category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}