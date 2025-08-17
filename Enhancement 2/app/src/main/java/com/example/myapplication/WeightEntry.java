package com.example.myapplication;

public class WeightEntry {
    private String date;
    private float weight;

    public WeightEntry(String date, float weight) {
        this.date = date;
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public float getWeight() {
        return weight;
    }
}
