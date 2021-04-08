package com.example.nationinfoapp;

import android.graphics.Bitmap;

public class Country {
    private String countryName;
    private int population;
    private String area;
    private String countryCode;


    public String getCountryName() {
        return countryName;
    }

    public Country(String countryName, int population, String area, String countryCode) {
        this.countryName = countryName;
        this.population = population;
        this.area = area;
        this.countryCode = countryCode;
    }

    public Country() {
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Override
    public String toString() {
        return String.format("Name: %s - Population: %d - Area: %s", this.countryName, this.population, this.area);
    }
}
