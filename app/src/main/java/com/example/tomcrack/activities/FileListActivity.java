package com.example.tomcrack.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomcrack.R;
import com.example.tomcrack.models.File;
import com.example.tomcrack.repositories.FileRepository;
import com.example.tomcrack.utils.FileAdapter;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

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
    }

    @Override
    protected void onResume() {
        super.onResume();

        String categoryId = "exampleCategoryId"; // Replace with the actual category ID
        FileRepository fileRepository = new FileRepository();

        fileRepository.getRecentFilesByCategory(categoryId, files -> {
            fileList.clear();
            fileList.addAll(files);
            fileAdapter.notifyDataSetChanged();
        }, error -> {
            // Handle error
            Toast.makeText(this, "Failed to fetch files: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void searchFiles(String query) {
        FileRepository fileRepository = new FileRepository();

        fileRepository.searchFilesByName(query, files -> {
            fileList.clear();
            fileList.addAll(files);
            fileAdapter.notifyDataSetChanged();
        }, error -> {
            // Handle error
            Toast.makeText(this, "Search failed: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}