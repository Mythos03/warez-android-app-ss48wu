package com.example.tomcrack.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomcrack.R;
import com.example.tomcrack.models.Category;
import com.example.tomcrack.models.File;
import com.example.tomcrack.repositories.CategoryRepository;
import com.example.tomcrack.repositories.FileRepository;
import com.example.tomcrack.utils.FileAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FileListActivity extends AppCompatActivity {

    private FileAdapter fileAdapter;
    private List<File> fileList;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fileList = new ArrayList<>();
        fileAdapter = new FileAdapter(fileList);
        recyclerView.setAdapter(fileAdapter);

        firestore = FirebaseFirestore.getInstance();

        Button filterButton = findViewById(R.id.filterButton);
        filterButton.setOnClickListener(v -> showCategoryFilterDialog());
    }

    private void showCategoryFilterDialog() {
        CategoryRepository categoryRepository = new CategoryRepository();
        categoryRepository.fetchCategories(categories -> {
            String[] categoryNames = categories.stream().map(Category::getName).toArray(String[]::new);

            new AlertDialog.Builder(this)
                    .setTitle("Select Category")
                    .setItems(categoryNames, (dialog, which) -> {
                        String selectedCategory = categoryNames[which];
                        filterFilesByCategory(selectedCategory);
                    })
                    .show();
        }, error -> Toast.makeText(this, "Failed to load categories", Toast.LENGTH_SHORT).show());
    }

    private void filterFilesByCategory(String categoryName) {
        FileRepository fileRepository = new FileRepository();
        fileRepository.getFilesByCategory(categoryName, files -> {
            fileList.clear();
            fileList.addAll(files);
            fileAdapter.notifyDataSetChanged();
        }, error -> Toast.makeText(this, "Failed to filter files: " + error.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onResume() {
        super.onResume();

        FileRepository fileRepository = new FileRepository();

        fileRepository.getAllFiles(files -> {
            fileList.clear();
            fileList.addAll(files);
            fileAdapter.notifyDataSetChanged();
        }, error -> {
            Toast.makeText(this, "Failed to fetch files: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        });
    }

    private void searchFiles(String query) {
        FileRepository fileRepository = new FileRepository();

        fileRepository.searchFilesByName(query, files -> {
            fileList.clear();
            fileList.addAll(files);
            fileAdapter.notifyDataSetChanged();
        }, error -> {
            Toast.makeText(this, "Search failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}