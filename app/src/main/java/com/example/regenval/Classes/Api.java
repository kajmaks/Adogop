package com.example.regenval.Classes;

import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Api {
    private static final String TAG = "WeatherAPI";

    public Api() {}

    private static final String API_URL =
            "https://api.open-meteo.com/v1/forecast" +
                    "?latitude=52.4064&longitude=16.9252" +
                    "&hourly=temperature_2m,cloudcover,precipitation" +
                    "&timezone=Europe%2FWarsaw";

    public Weather fetchWeatherData() throws Exception {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(API_URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000); // 10 seconds
            connection.setReadTimeout(10000); // 10 seconds

            int status = connection.getResponseCode();
            Log.d(TAG, "HTTP Status: " + status);

            if (status != 200) {
                // Read error stream if available
                String errorResponse = readStream(connection.getErrorStream());
                Log.e(TAG, "API Error Response: " + errorResponse);
                throw new RuntimeException("API Error: " + status + " - " + errorResponse);
            }

            String response = readStream(connection.getInputStream());
            Log.d(TAG, "API Response: " + response.substring(0, Math.min(response.length(), 200)));

            return parseWeatherResponse(response);
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private String readStream(java.io.InputStream stream) throws Exception {
        if (stream == null) return "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        return response.toString();
    }

    private Weather parseWeatherResponse(String jsonResponse) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(jsonResponse);
        JsonNode hourlyNode = rootNode.path("hourly");

        Weather weather = new Weather();

        // Parse temperature data
        List<Float> temperatures = new ArrayList<>();
        for (JsonNode node : hourlyNode.path("temperature_2m")) {
            temperatures.add((float) node.asDouble());
        }

        // Parse cloud cover data
        List<Integer> cloudcover = new ArrayList<>();
        for (JsonNode node : hourlyNode.path("cloudcover")) {
            cloudcover.add(node.asInt());
        }

        // Parse precipitation data
        List<Float> precipitation = new ArrayList<>();
        for (JsonNode node : hourlyNode.path("precipitation")) {
            precipitation.add((float) node.asDouble());
        }

        // Set data using reflection since fields are private
        setPrivateField(weather, "temperature", temperatures);
        setPrivateField(weather, "cloudcover", cloudcover);
        setPrivateField(weather, "precipitation", precipitation);

        return weather;
    }

    private void setPrivateField(Weather weather, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = Weather.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(weather, value);
    }
}