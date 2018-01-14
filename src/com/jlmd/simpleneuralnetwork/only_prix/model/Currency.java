package com.jlmd.simpleneuralnetwork.only_prix.model;

import java.util.ArrayList;
import java.util.List;

public class Currency {

    String name;
    public List<Hour> hours;

    public Currency(String name) {
        super();
        this.name = name;
    }

    public Currency getHourForTimestamp(long timestamp){
        for(Hour hour : hours){
            if(hour.time == timestamp){
                Currency currency = new Currency(name);
                currency.hours = new ArrayList<>();
                currency.hours.add(hour);
                return currency;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return name+" " +hours+ "\n";
    }
}
