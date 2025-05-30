import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

private static final String API_URL =
        "https://api.open-meteo.com/v1/forecast" +
                "?latitude=52.4064&longitude=16.9252" +
                "&hourly=temperature_2m,cloudcover,precipitation" +
                "&timezone=Europe%2FWarsaw";

public static WeatherResponse fetchWeatherData() throws Exception {
    URL url = new URL(API_URL);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");

    int status = connection.getResponseCode();
    if (status != 200) {
        throw new RuntimeException("Failed to fetch weather data: HTTP " + status);
    }

    BufferedReader in = new BufferedReader(
            new InputStreamReader(connection.getInputStream())
    );
    StringBuilder response = new StringBuilder();
    String line;

    while ((line = in.readLine()) != null) {
        response.append(line);
    }

    in.close();
    connection.disconnect();

    ObjectMapper mapper = new ObjectMapper();
    return mapper.readValue(response.toString(), Weather.class);
}