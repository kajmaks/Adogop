package com.example.regenval;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.regenval.Classes.Api;
import com.example.regenval.Classes.Weather;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvTime, tvDate, tvTemperature;
    private TextView tvHumidityValue, tvCloudsValue, tvRainValue;
    private Handler timeHandler, weatherHandler;
    private final Api api = new Api();

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

        // Initialize views
        tvTime = findViewById(R.id.tvTime);
        tvDate = findViewById(R.id.tvDate);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvHumidityValue = findViewById(R.id.tvHumidityValue);
        tvCloudsValue = findViewById(R.id.tvCloudsValue);
        tvRainValue = findViewById(R.id.tvRainValue);

        setupTimeUpdater();
        animateClock();
        setupWeatherUpdates();
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

    private void setupWeatherUpdates() {
        weatherHandler = new Handler();
        fetchWeather(); // Initial fetch
        weatherHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchWeather();
                weatherHandler.postDelayed(this, 30 * 60 * 1000); // Update every 30 mins
            }
        }, 30 * 60 * 1000);
    }

    private void fetchWeather() {
        new Thread(() -> {
            try {
                Log.d("WeatherFetch", "Starting weather fetch");
                Weather weather = api.fetchWeatherData();

                // Add detailed log for received data
                if (weather != null) {
                    Log.d("WeatherFetch", "Weather object received");
                    if (weather.getTemperature() != null) {
                        Log.d("WeatherData", "Temperature data size: " + weather.getTemperature().size());
                    } else {
                        Log.w("WeatherData", "Temperature data is null");
                    }
                } else {
                    Log.w("WeatherFetch", "Weather object is null");
                }

                runOnUiThread(() -> {
                    updateWeatherUI(weather);
                    Log.d("WeatherUI", "Weather UI updated");
                });
            } catch (Exception e) {
                Log.e("WeatherError", "Failed to fetch weather", e);
                runOnUiThread(() -> {
                    tvTemperature.setText("--°");
                    tvHumidityValue.setText("--%");
                    tvCloudsValue.setText("--%");
                    tvRainValue.setText("--%");

                    // Add toast for better error visibility
                    Toast.makeText(MainActivity.this,
                            "Weather update failed: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                });
            }
        }).start();
    }

    private void updateWeatherUI(Weather weather) {
        // Update current weather
        tvTemperature.setText(String.format(Locale.getDefault(), "%.0f°", weather.curTemperature()));
        tvCloudsValue.setText(String.format(Locale.getDefault(), "%d%%", weather.curCloudcover()));
        tvRainValue.setText(String.format(Locale.getDefault(), "%.0fmm", weather.curPrecipitation()));

        // Calculate humidity approximation
        float temp = weather.curTemperature();
        int cloudCover = weather.curCloudcover();
        float precipitation = weather.curPrecipitation();
        int humidity = calculateHumidityApproximation(temp, cloudCover, precipitation);
        tvHumidityValue.setText(String.format(Locale.getDefault(), "%d%%", humidity));

        // Update weekly averages if needed
        List<Float> weeklyTemps = weather.avgWeekTemp();
        // ... use weekly averages as needed ...
    }
    private int calculateHumidityApproximation(float temp, float cloudCover, float precipitation) {
        // Simple heuristic to approximate humidity:
        // Base humidity (30-50%) + cloud cover contribution + precipitation contribution
        int baseHumidity = 40;
        int cloudContribution = (int) (cloudCover * 0.3); // 0-30% contribution
        int rainContribution = precipitation > 0 ? 20 : 0;

        int humidity = baseHumidity + cloudContribution + rainContribution;
        return Math.min(humidity, 100); // Cap at 100%
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
        if (weatherHandler != null) weatherHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeHandler != null) timeHandler.removeCallbacksAndMessages(null);
        if (weatherHandler != null) weatherHandler.removeCallbacksAndMessages(null);
    }
}