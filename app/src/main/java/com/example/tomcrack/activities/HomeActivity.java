package com.example.tomcrack.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tomcrack.R;
import com.example.tomcrack.models.File;
import com.example.tomcrack.receivers.CleanupReceiver;
import com.example.tomcrack.repositories.FileRepository;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button fileListButton = findViewById(R.id.fileListButton);
        Button fileUploadButton = findViewById(R.id.fileUploadButton);
        Button commentListButton = findViewById(R.id.commentListButton);

        fileListButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, FileListActivity.class);
            startActivity(intent);
        });

        fileUploadButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, FileUploadActivity.class);
            startActivity(intent);
        });

        commentListButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CommentListActivity.class);
            startActivity(intent);
        });

        Button fileCreationButton = findViewById(R.id.fileCreationButton);
        fileCreationButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, FileCreationActivity.class);
            startActivity(intent);
        });

        scheduleDailyCleanup();
    }

    private void scheduleDailyCleanup() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, CleanupReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // Set the alarm to trigger at midnight
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
            );
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Redirect to MainActivity if no user is logged in
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Show a welcome message
            Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadTopFiles() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FileRepository fileRepository = new FileRepository();

        fileRepository.getTopFilesUploadedByUser(userId, files -> {
            // Handle the list of top files (e.g., display in a RecyclerView or log them)
            for (File file : files) {
                System.out.println("Top File: " + file.getName());
            }
        }, error -> {
            // Handle error
            Toast.makeText(this, "Failed to load top files: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}