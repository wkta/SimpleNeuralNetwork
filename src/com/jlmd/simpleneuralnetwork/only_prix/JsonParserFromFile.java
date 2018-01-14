package com.jlmd.simpleneuralnetwork.only_prix;

import com.jlmd.simpleneuralnetwork.only_prix.model.Currency;
import com.jlmd.simpleneuralnetwork.only_prix.model.Hour;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class JsonParserFromFile {


    //https://quantquote.com/historical-stock-data


    //java CNN on mnist (0-9 symbols reco)
    //    https://github.com/BigPeng/JavaCNN


    double minBTC = 1000.0f;
    double maxBTC = 25000.0f;

    public static String pathnameBTC = "data/ex_bateau_btc86h.json";

    //        todo :
    //        1) ping url avec fenetre qui se deplace dans le passé pour ajuter des lignes dans les tendances
    //        2) ouv + close + min + max + volume + diff mme + diff bollinger  par heure

    public static void main(String [] args) throws IOException, ParseException {
        JsonParserFromFile jsonParser = new JsonParserFromFile();

        JsonParserFromFile.emptyFile("data/tendancesInput.txt");
        JsonParserFromFile.emptyFile("data/tendancesOutput.txt");
        jsonParser.write1LineInTendances();

    }

    public void write1LineInTendances() throws IOException, ParseException {
        Currency btc = extractJson(pathnameBTC, "BTC");

        //met les 85 derniers close dans input.txt
        String inputValues="";
        for(int i =0; i < 85; i++){
            if(i < 84) {
                inputValues += normalise(minBTC, maxBTC, btc.hours.get(i).close) + ",";
            } else {
                inputValues += normalise(minBTC, maxBTC, btc.hours.get(i).close);
            }
        }
        writeToFile(inputValues, "data/tendancesInput.txt");

        // met 0 ou 1 dans output.txt selon diff entre drnier et avant dernier
        int sensOutput;
        if(btc.hours.get(84).close > btc.hours.get(85).close) {
            //0 baisse
            sensOutput = 0;
        } else {
            //1 hausse
            sensOutput = 1;
        }

        writeToFile(sensOutput + "", "data/tendancesOutput.txt");
    }


    public static String normalise(double minBTC, double maxBTC,double close) {
        double diff = maxBTC - minBTC;
        double res= (close - minBTC) / diff;
        res = (res * 2) - 1;
        return arrondi5Dec(res) + "";
    }

    private static double arrondi5Dec(double res) {
        DecimalFormat df = new DecimalFormat("#.#####");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(df.format(res).replaceAll(",","."));
    }

    public static void writeToFile(String msg, String path)  {
        try {
            FileWriter fw = new FileWriter(path,true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(msg+"\n");
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void emptyFile(String path) {
        try {
            FileWriter fw = new FileWriter(path,false);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("");
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
