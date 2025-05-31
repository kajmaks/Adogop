package com.example.regenval.Classes;

import java.util.Arrays;
import java.util.List;

public class TemplateValues {

    public TemplateValues(){}
    private float[] temperature = {22.3f, 19.5f, 16.6f, 23.2f, 18.4f, 21.3f, 32.3f};
    private int[] cloud = {0, 46, 99, 23, 5, 23, 43};
    private float[] rain = {11.3f, 0.0f, 0.0f, 3.4f, 0.0f, 2.3f, 0.0f};

    public float curTemp() {
        return temperature[0];
    }

    public int curCloud() {
        return cloud[0];
    }

    public float curRain() {
        return rain[0];
    }

    public List<Float> getTemp() {
        return toFloatList(temperature);
    }

    public List<Integer> getCloud() {
        return toIntList(cloud);
    }

    public List<Float> getRain() {
        return toFloatList(rain);
    }

    private List<Float> toFloatList(float[] arr) {
        Float[] boxed = new Float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            boxed[i] = arr[i];
        }
        return Arrays.asList(boxed);
    }

    private List<Integer> toIntList(int[] arr) {
        Integer[] boxed = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            boxed[i] = arr[i];
        }
        return Arrays.asList(boxed);
    }

    public String getSunResults(){
        if(curCloud()<20){
            return "Sunny";
        }
        else if(curCloud()<50){
            return "kida sunny";
        }
        else if(curCloud()<100){
            return "Not sunny";
        }
        else return getProjectStatusResult();
    }

    public String getRainResults(){
        if(curRain()<1.0){
            return "no rain";
        }
        else if(curRain()<5.0){
            return "your not from sugar";
        }
        else if(curRain()<15.0){
            return "kida bad";
        }
        else if(curRain()<30.0){
            return "bad";
        }
        else{
            return "Poseidon, have mercy on us";
        }
    }

    public String getTempResults(){
        if(curTemp()< -30.0){
            return "freezing";
        }
        else if(curTemp()< -15.0){
            return "very cold";
        }
        else if(curTemp()< 0.0){
            return "cold";
        }
        else if(curTemp()< 15.0){
            return "ok";
        }
        else if(curTemp() < 30.0){
            return "hot";
        }
        else if(curTemp() < 100.0){
            return "very hot";
        }
        else{
            return "like hotdog in microwave";
        }
    }

    public String getProjectStatusResult(){
        return "We are fucked";
    }
}