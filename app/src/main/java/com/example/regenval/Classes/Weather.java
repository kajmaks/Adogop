package com.example.regenval.Classes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather {

    @JsonProperty("temperature_2m")
    private List<Float> temperature;

    @JsonProperty("cloudcover")
    private List<Integer> cloudcover;

    @JsonProperty("precipitation")
    private List<Float> precipitation;

    public List<Float> getTemperature() { return temperature; }
    public List<Integer> getCloudcover() { return cloudcover; }
    public List<Float> getPrecipitation() { return precipitation; }



    public float curTemperature() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH", Locale.getDefault());
        int hour = Integer.parseInt(timeFormat.toString());
        System.out.println(temperature.get(hour));
        return temperature.get(hour);
    }

    public int curCloudcover() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH", Locale.getDefault());
        int hour = Integer.parseInt(timeFormat.toString());
        return cloudcover.get(hour);
    }

    public float curPrecipitation() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH", Locale.getDefault());
        int hour = Integer.parseInt(timeFormat.toString());
        return precipitation.get(hour);
    }

    public List<Float> avgWeekTemp() {
        List<Float> avg = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            float sum = 0f;
            for (int j = 0; j < 24; j++) {
                sum += temperature.get(i * 24 + j);
            }
            avg.add(sum / 24);
        }
        return avg;
    }

    public List<Float> avgWeekCover() {
        List<Float> avg = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            float sum = 0f;
            for (int j = 0; j < 24; j++) {
                sum += cloudcover.get(i * 24 + j);
            }
            avg.add(sum / 24);
        }
        return avg;
    }

    public List<Float> avgWeekPrecipitation() {
        List<Float> avg = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            float sum = 0f;
            for (int j = 0; j < 24; j++) {
                sum += precipitation.get(i * 24 + j);
            }
            avg.add(sum / 24);
        }
        return avg;
    }
}