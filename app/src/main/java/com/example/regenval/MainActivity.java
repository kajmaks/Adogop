package com.example.regenval;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvTime, tvDate;
    private Handler timeHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTime = findViewById(R.id.tvTime);
        tvDate = findViewById(R.id.tvDate);

        setupTimeUpdater();
        animateClock();
    }

    private void setupTimeUpdater() {
        timeHandler = new Handler();
        final Runnable timeRunnable = new Runnable() {
            @Override
            public void run() {
                updateTime();
                timeHandler.postDelayed(this, 60000);
            }
        };

        updateTime();
        timeHandler.post(timeRunnable);
    }
    private void updateTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE | MMMM d", Locale.getDefault());

        Date now = new Date();
        tvTime.setText(timeFormat.format(now));

        String dateString = dateFormat.format(now).toUpperCase();
        tvDate.setText(dateString);
    }

    private void animateClock() {
        View clockContainer = findViewById(R.id.clockContainer);

        // Create the scale animations
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.98f, 1.02f, // Start and end for X
                0.98f, 1.02f, // Start and end for Y
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot X type and value
                Animation.RELATIVE_TO_SELF, 0.5f  // Pivot Y type and value
        );

        scaleAnimation.setDuration(2000);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(Animation.INFINITE);
        scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator());

        clockContainer.startAnimation(scaleAnimation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timeHandler.removeCallbacksAndMessages(null);
    }
}