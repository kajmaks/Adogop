package com.example.regenval;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.regenval.Classes.TemplateValues;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class WeeklyTemperatureActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_temperature);

        TemplateValues templateValues = new TemplateValues();
        List<Float> temperatures = templateValues.getTemp();
        LinearLayout container = findViewById(R.id.temperatureContainer);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());

        for (int i = 0; i < temperatures.size(); i++) {
            View dayView = LayoutInflater.from(this).inflate(R.layout.item_day_temp, container, false);

            TextView tvDay = dayView.findViewById(R.id.tvDay);
            TextView tvTemp = dayView.findViewById(R.id.tvTemp);

            String dayName = dayFormat.format(calendar.getTime());
            tvDay.setText(dayName);

            float temp = temperatures.get(i);
            tvTemp.setText(String.format(Locale.getDefault(), "%.0f°", temp));

            container.addView(dayView);

            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}