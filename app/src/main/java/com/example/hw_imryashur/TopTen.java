package com.example.hw_imryashur;

public class TopTen implements Comparable<TopTen>{
    private String name = "";
    private double lat = 0.0;
    private double lon = 0.0;
    private long time = 0;
    private int numOfSteps = 100;

    public TopTen() {}

    public TopTen(String name, double lat, double lon, long time, int numOfSteps) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.time = time;
        this.numOfSteps = numOfSteps;
    }


    @Override
    public String toString() {
        return name + ": " + " Number Of Moves - " + numOfSteps;
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

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getNumOfSteps() {
        return numOfSteps;
    }

    public void setNumOfSteps(int numOfSteps) {
        this.numOfSteps = numOfSteps;
    }


    @Override
    public int compareTo(TopTen another) {
        if (this.getNumOfSteps() < another.getNumOfSteps()) return -1;
        else return 1;
    }
}
