package com.example.tomcrack.utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tomcrack.R;
import com.example.tomcrack.models.File;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

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
        holder.categoryTextView.setText(file.getCategory().getName());
        holder.uploadDateTextView.setText(new SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                .format(file.getUploadDate()));
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView fileNameTextView, categoryTextView, uploadDateTextView;

        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.fileNameTextView);
            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            uploadDateTextView = itemView.findViewById(R.id.uploadDateTextView);
        }
    }
}