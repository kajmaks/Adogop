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

public class WeeklyRainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_rain);

        TemplateValues templateValues = new TemplateValues();
        List<Float> rains = templateValues.getRain();
        LinearLayout container = findViewById(R.id.rainContainer);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int maxBarWidth = (int) (screenWidth * 0.8);

        for (int i = 0; i < rains.size(); i++) {
            View rainItem = LayoutInflater.from(this).inflate(R.layout.item_day_rain, container, false);

            TextView tvDay = rainItem.findViewById(R.id.tvDay);
            TextView tvRain = rainItem.findViewById(R.id.tvRain);
            View rainBarFill = rainItem.findViewById(R.id.rainBarFill);

            String dayName = dayFormat.format(calendar.getTime());
            tvDay.setText(dayName);

            float rainAmount = rains.get(i);
            tvRain.setText(String.format(Locale.getDefault(), "%.1f mm", rainAmount));

            int barWidth = (int) (maxBarWidth * (rainAmount / 20f)); // Max 20mm for scaling
            rainBarFill.getLayoutParams().width = barWidth;
            rainBarFill.requestLayout();

            GradientDrawable gradient = createRainGradient(rainAmount);
            rainBarFill.setBackground(gradient);

            container.addView(rainItem);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private GradientDrawable createRainGradient(float rainAmount) {
        int startColor, endColor;

        if (rainAmount < 2.5) {
            startColor = ContextCompat.getColor(this, R.color.rain_light);
            endColor = ContextCompat.getColor(this, R.color.rain_medium_light);
        } else if (rainAmount < 7.5) {
            startColor = ContextCompat.getColor(this, R.color.rain_medium_light);
            endColor = ContextCompat.getColor(this, R.color.rain_medium);
        } else {
            startColor = ContextCompat.getColor(this, R.color.rain_medium);
            endColor = ContextCompat.getColor(this, R.color.rain_heavy);
        }

        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{startColor, endColor}
        );
        gradient.setCornerRadius(12);
        return gradient;
    }
}