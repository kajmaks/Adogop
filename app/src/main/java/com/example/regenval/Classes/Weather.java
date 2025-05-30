import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

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

    public float curTemperature(int hour) {
        return temperature.get(hour);
    }

    public int curCloudcover(int hour) {
        return cloudcover.get(hour);
    }

    public float curPrecipitation(int hour) {
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