package com.jlmd.simpleneuralnetwork.model;

public class Hour {

    public long time;
    public double close;
    public double high;
    public double low;
    public double open;
    public double volumefrom;
    public double volumeto;



    public Hour(){

    }

    @Override
    public String toString() {
        return close+"\n";
    }
}
