package com.example.regenval;

import android.graphics.Color;
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

public class WeeklyCloudActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_cloud);

        TemplateValues templateValues = new TemplateValues();
        List<Integer> clouds = templateValues.getCloud();
        LinearLayout container = findViewById(R.id.cloudContainer);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", Locale.getDefault());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth = displayMetrics.widthPixels;
        int maxBarWidth = (int) (screenWidth * 0.8);

        for (int i = 0; i < clouds.size(); i++) {
            View cloudCard = LayoutInflater.from(this).inflate(R.layout.item_cloud_card, container, false);

            TextView tvDay = cloudCard.findViewById(R.id.tvDay);
            TextView tvPercentage = cloudCard.findViewById(R.id.tvPercentage);
            View cloudIcon = cloudCard.findViewById(R.id.cloudIcon);
            View cloudBarFill = cloudCard.findViewById(R.id.cloudBarFill);

            String dayName = dayFormat.format(calendar.getTime());
            tvDay.setText(dayName);

            int cloudCover = clouds.get(i);
            tvPercentage.setText(String.format(Locale.getDefault(), "%d%%", cloudCover));

            updateCloudIcon(cloudIcon, cloudCover);

            int barWidth = (int) (maxBarWidth * (cloudCover / 100.0));
            cloudBarFill.getLayoutParams().width = barWidth;
            cloudBarFill.requestLayout();

            GradientDrawable gradient = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{
                            Color.parseColor("#80FFFFFF"),
                            Color.parseColor("#B3FFFFFF")
                    }
            );
            gradient.setCornerRadius(6);
            cloudBarFill.setBackground(gradient);

            container.addView(cloudCard);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Handle back button click
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void updateCloudIcon(View cloudIcon, int cloudCover) {
        cloudIcon.setBackgroundResource(R.drawable.ic_cloud);
    }
}