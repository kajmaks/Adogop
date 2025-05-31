package com.example.regenval;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.regenval.Classes.TemplateValues;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeeklyHumidityActivity extends AppCompatActivity {

    private int maxBarWidth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_humidity);

        // Calculate maximum bar width (80% of screen width)
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        maxBarWidth = (int) (screenWidth * 0.8);

        TemplateValues templateValues = new TemplateValues();
        List<Float> temperatures = templateValues.getTemp();
        List<Integer> clouds = templateValues.getCloud();
        List<Float> rains = templateValues.getRain();

        LinearLayout container = findViewById(R.id.humidityContainer);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        for (int i = 0; i < temperatures.size(); i++) {
            View dayView = LayoutInflater.from(this).inflate(R.layout.item_day_humidity, container, false);

            TextView tvDay = dayView.findViewById(R.id.tvDay);
            TextView tvHumidity = dayView.findViewById(R.id.tvHumidity);
            TextView tvHumidityValue = dayView.findViewById(R.id.tvHumidityValue);
            View humidityBarFill = dayView.findViewById(R.id.humidityBarFill);

            String dayName = dayFormat.format(calendar.getTime());
            tvDay.setText(dayName);

            int humidity = calculateHumidityApproximation(
                    temperatures.get(i),
                    clouds.get(i),
                    rains.get(i)
            );

            tvHumidity.setText(String.format(Locale.getDefault(), "%d%%", humidity));
            tvHumidityValue.setText(getHumidityDescription(humidity));

            int barWidth = (int) (maxBarWidth * (humidity / 100.0));
            humidityBarFill.getLayoutParams().width = barWidth;
            humidityBarFill.requestLayout();

            GradientDrawable gradient = createHumidityGradient(humidity);
            humidityBarFill.setBackground(gradient);

            container.addView(dayView);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private int calculateHumidityApproximation(float temp, int cloudCover, float precipitation) {
        int baseHumidity = 40;
        int cloudContribution = (int) (cloudCover * 0.3);
        int rainContribution = precipitation > 0 ? 20 : 0;
        return Math.min(baseHumidity + cloudContribution + rainContribution, 100);
    }

    private String getHumidityDescription(int humidity) {
        if (humidity < 30) return "Very Dry";
        if (humidity < 50) return "Dry";
        if (humidity < 70) return "Comfortable";
        if (humidity < 85) return "Humid";
        return "Very Humid";
    }

    private GradientDrawable createHumidityGradient(int humidity) {
        int startColor, endColor;

        if (humidity < 25) {
            startColor = ContextCompat.getColor(this, R.color.aqua);
            endColor = ContextCompat.getColor(this, R.color.light_blue);
        } else if (humidity < 50) {
            startColor = ContextCompat.getColor(this, R.color.light_blue);
            endColor = ContextCompat.getColor(this, R.color.medium_blue);
        } else if (humidity < 75) {
            startColor = ContextCompat.getColor(this, R.color.medium_blue);
            endColor = ContextCompat.getColor(this, R.color.dark_blue);
        } else {
            startColor = ContextCompat.getColor(this, R.color.dark_blue);
            endColor = ContextCompat.getColor(this, R.color.navy);
        }

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{startColor, endColor}
        );
        gradient.setCornerRadius(12);
        return gradient;
    }
}