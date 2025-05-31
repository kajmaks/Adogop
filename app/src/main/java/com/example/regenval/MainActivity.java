package com.example.regenval;

import android.content.Intent;
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

import com.example.regenval.Classes.TemplateValues;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvTime, tvDate, tvTemperature;
    private TextView tvHumidityValue, tvCloudsValue, tvRainValue;
    private Handler timeHandler;
    private final TemplateValues templateValues = new TemplateValues();

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
        tvTemperature = findViewById(R.id.tvTemperature);
        tvHumidityValue = findViewById(R.id.tvHumidityValue);
        tvCloudsValue = findViewById(R.id.tvCloudsValue);
        tvRainValue = findViewById(R.id.tvRainValue);

        setupTimeUpdater();
        animateClock();
        updateWeatherUI();


        tvTemperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WeeklyTemperatureActivity.class));
            }
        });


        tvHumidityValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WeeklyHumidityActivity.class));
            }
        });

        tvCloudsValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WeeklyCloudActivity.class));
            }
        });

        tvRainValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WeeklyRainActivity.class));
            }
        });
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

    private void updateWeatherUI() {
        tvTemperature.setText(String.format(Locale.getDefault(), "%.0f°", templateValues.curTemp()));
        tvCloudsValue.setText(String.format(Locale.getDefault(), "%d%%", templateValues.curCloud()));
        tvRainValue.setText(String.format(Locale.getDefault(), "%.0fmm", templateValues.curRain()));

        int humidity = calculateHumidityApproximation(
                templateValues.curTemp(),
                templateValues.curCloud(),
                templateValues.curRain()
        );
        tvHumidityValue.setText(String.format(Locale.getDefault(), "%d%%", humidity));
    }

    private int calculateHumidityApproximation(float temp, int cloudCover, float precipitation) {
        int baseHumidity = 40;
        int cloudContribution = (int) (cloudCover * 0.3);
        int rainContribution = precipitation > 0 ? 20 : 0;
        return Math.min(baseHumidity + cloudContribution + rainContribution, 100);
    }

    private void updateTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE | MMMM d", Locale.getDefault());
        Date now = new Date();
        tvTime.setText(timeFormat.format(now));
        tvDate.setText(dateFormat.format(now).toUpperCase());
    }

    private void animateClock() {
        View clockContainer = findViewById(R.id.clockContainer);
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.98f, 1.02f,
                0.98f, 1.02f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
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
        if (timeHandler != null) timeHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeHandler != null) timeHandler.removeCallbacksAndMessages(null);
    }
}