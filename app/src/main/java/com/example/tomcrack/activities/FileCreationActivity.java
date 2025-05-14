package com.example.tomcrack.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tomcrack.R;
import com.example.tomcrack.models.Category;
import com.example.tomcrack.models.File;
import com.example.tomcrack.repositories.CategoryRepository;
import com.example.tomcrack.repositories.FileRepository;

import java.util.Date;

public class FileCreationActivity extends AppCompatActivity {

    private EditText fileNameEditText, fileDescriptionEditText;
    private Spinner categorySpinner;
    private FileRepository fileRepository;
    private CategoryRepository categoryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_creation);

        fileNameEditText = findViewById(R.id.fileNameEditText);
        fileDescriptionEditText = findViewById(R.id.fileDescriptionEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        Button saveFileButton = findViewById(R.id.saveFileButton);

        fileRepository = new FileRepository();
        categoryRepository = new CategoryRepository();

        loadCategories();

        saveFileButton.setOnClickListener(v -> saveFile());
    }

    private void loadCategories() {
        categoryRepository.fetchCategories(categories -> {
            ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);
        }, e -> Toast.makeText(this, "Failed to load categories", Toast.LENGTH_SHORT).show());
    }

    private void saveFile() {
        String fileName = fileNameEditText.getText().toString();
        String fileDescription = fileDescriptionEditText.getText().toString();
        Category selectedCategory = (Category) categorySpinner.getSelectedItem();

        if (TextUtils.isEmpty(fileName) || TextUtils.isEmpty(fileDescription) || selectedCategory == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(fileName, 0, null, selectedCategory, new Date(), fileDescription, null);

        fileRepository.createFile(file, aVoid -> {
            Toast.makeText(this, "File created successfully", Toast.LENGTH_SHORT).show();
            finish();
        }, e -> {
            Toast.makeText(this, "Failed to create file: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}