package com.example.tomcrack.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomcrack.R;
import com.example.tomcrack.models.File;
import com.example.tomcrack.repositories.FileRepository;

import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    private final List<File> fileList;

    public FileAdapter(List<File> fileList) {
        this.fileList = fileList;
    }

    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file_card, parent, false);
        return new FileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        File file = fileList.get(position);

        holder.fileNameTextView.setText(file.getName());
        holder.fileDescriptionTextView.setText(file.getDescription());

        if (file.getCategory() != null) {
            holder.categoryTextView.setText(file.getCategory().getName());
        } else {
            holder.categoryTextView.setText("No Category");
        }

        holder.deleteButton.setOnClickListener(v -> {
            if (file.getId() == null || file.getId().isEmpty()) {
                Toast.makeText(holder.itemView.getContext(), "Invalid file ID", Toast.LENGTH_SHORT).show();
                return;
            }

            FileRepository fileRepository = new FileRepository();
            fileRepository.deleteFile(file.getId(), aVoid -> {
                try {
                    if (position >= 0 && position < fileList.size()) {
                        fileList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(holder.itemView.getContext(), "File deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(holder.itemView.getContext(), "Error updating list: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }, error -> {
                Toast.makeText(holder.itemView.getContext(), "Failed to delete file: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            });
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView, fileDescriptionTextView, categoryTextView, uploadDateTextView;
        Button deleteButton;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
            fileDescriptionTextView = itemView.findViewById(R.id.fileDescriptionTextView);
            categoryTextView = itemView.findViewById(R.id.fileCategoryTextView);
            uploadDateTextView = itemView.findViewById(R.id.uploadDateTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}