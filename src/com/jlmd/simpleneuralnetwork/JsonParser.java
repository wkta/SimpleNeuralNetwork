package com.jlmd.simpleneuralnetwork;

import com.jlmd.simpleneuralnetwork.model.Currency;
import com.jlmd.simpleneuralnetwork.model.Hour;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    static String pathnameBTC = "data/ex_bateau_btc86h.json";
    static String pathnameRIPL = "data/ex_bateau_xrp86h.json";
    static String pathnameETH = "data/ex_bateau_eth86h.json";

    public static void main(String [] args) throws IOException, ParseException {
        JsonParser jsonParser = new JsonParser();

        Currency btc = jsonParser.extractJson(pathnameBTC, "BTC");
        Currency ripple = jsonParser.extractJson(pathnameRIPL, "RIPL");
        Currency eth = jsonParser.extractJson(pathnameETH, "ETH");

//        System.out.println(btc);
//        System.out.println(ripple);
//        System.out.println(eth);

        List<Currency> targetTimestamps= jsonParser.getTripletForTimestamp(btc ,ripple , eth, 1514746800);
        System.out.println(targetTimestamps);
    }

    private  List<Currency> getTripletForTimestamp(Currency btc, Currency ripple, Currency eth, long timestamp) {
        List<Currency> filteredCurrencys = new ArrayList<>();
        filteredCurrencys.add( btc.getHourForTimestamp(timestamp));
        filteredCurrencys.add( ripple.getHourForTimestamp(timestamp));
        filteredCurrencys.add( eth.getHourForTimestamp(timestamp));
        return filteredCurrencys;
    }

    private Currency  extractJson(String pathname, String name) throws IOException, ParseException {

        List<Hour> hours= new ArrayList<Hour>();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(pathname));

        if(obj instanceof JSONObject){
            JSONObject jSONObject =  (JSONObject)obj;
            List datas = (List)jSONObject.get("Data");
            for(Object data : datas){
                JSONObject jSONObjectData = (JSONObject)data;
                Hour hour = new Hour();
                hour.time = (long) jSONObjectData.get("time");
                hour.close=  Double.parseDouble( jSONObjectData.get("close")+"");
                hour.high= Double.parseDouble(  jSONObjectData.get("high")+"");
                hour.low= Double.parseDouble(   jSONObjectData.get("low")+"");
                hour.open= Double.parseDouble(   jSONObjectData.get("open")+"");
                hour.volumefrom= Double.parseDouble(   jSONObjectData.get("volumefrom")+"");
                hour.volumeto= Double.parseDouble(   jSONObjectData.get("volumeto")+"");
                hours.add(hour);
            }
        }

        Currency curr = new Currency(name);
        curr.hours = hours;
        return curr;
    }

}
