package com.example.tomcrack.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.tomcrack.R;
import com.example.tomcrack.models.Category;
import com.example.tomcrack.models.File;
import com.example.tomcrack.repositories.CategoryRepository;
import com.example.tomcrack.repositories.FileRepository;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

public class FileUploadActivity extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;

    private EditText fileNameEditText, fileDescriptionEditText;
    private Spinner categorySpinner;
    private Uri selectedFileUri;
    private ConstraintLayout rootLayout;

    private FileRepository fileRepository;
    private CategoryRepository categoryRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Apply fade_in animation on activity entry
        overridePendingTransition(R.anim.fade_in, R.anim.fade_in);

        setContentView(R.layout.activity_file_upload);

        fileNameEditText = findViewById(R.id.fileNameEditText);
        fileDescriptionEditText = findViewById(R.id.fileDescriptionEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        Button pickFileButton = findViewById(R.id.pickFileButton);
        Button uploadButton = findViewById(R.id.uploadButton);
        rootLayout = findViewById(R.id.rootLayout);

        fileRepository = new FileRepository();
        categoryRepository = new CategoryRepository();

        loadCategories();

        pickFileButton.setOnClickListener(v -> openFilePicker());
        uploadButton.setOnClickListener(v -> uploadFile());
    }

    private void loadCategories() {
        categoryRepository.fetchCategories(categories -> {
            ArrayAdapter<Category> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);
        }, e -> Toast.makeText(this, "Failed to load categories", Toast.LENGTH_SHORT).show());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedFileUri = data.getData();
            Toast.makeText(this, "File selected: " + selectedFileUri.getLastPathSegment(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadFile() {
        String fileName = fileNameEditText.getText().toString();
        String fileDescription = fileDescriptionEditText.getText().toString();
        Category selectedCategory = (Category) categorySpinner.getSelectedItem();

        if (TextUtils.isEmpty(fileName) || TextUtils.isEmpty(fileDescription) || selectedFileUri == null || selectedCategory == null) {
            Snackbar.make(rootLayout, "Please fill all fields and select a file", Snackbar.LENGTH_SHORT).show();
            return;
        }

        File file = new File(fileName, 0, selectedFileUri.toString(), selectedCategory, new Date(), fileDescription, null);

        fileRepository.createFile(file, aVoid -> {
            Snackbar.make(rootLayout, "File uploaded successfully", Snackbar.LENGTH_SHORT).show();
        }, e -> {
            Snackbar.make(rootLayout, "Failed to upload file: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
        });
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(rootLayout, message, Snackbar.LENGTH_SHORT);

        // Apply slide_up animation to the Snackbar
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onShown(Snackbar sb) {
                Animation slideUp = AnimationUtils.loadAnimation(FileUploadActivity.this, R.anim.slide_up);
                sb.getView().startAnimation(slideUp);
            }
        });

        snackbar.show();
    }
}