package com.example.hw_imryashur;
/*
    Student - Imry Ashur
*/
public class TopTen implements Comparable<TopTen>{
    private String name = "";
    private double lat = 0.0;
    private double lon = 0.0;
    private int numOfSteps = 100;
    private int position = 0;

    public TopTen() {}

    public TopTen(String name, double lat, double lon, int numOfSteps) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.numOfSteps = numOfSteps;
    }


    @Override
    public String toString() {
        return position + ".) " + name + ": " + " Number Of Moves - " + numOfSteps;
    }

    @Override
    public int compareTo(TopTen another) {
        if (this.getNumOfSteps() < another.getNumOfSteps()) return -1;
        else return 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public int getNumOfSteps() {
        return numOfSteps;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}


