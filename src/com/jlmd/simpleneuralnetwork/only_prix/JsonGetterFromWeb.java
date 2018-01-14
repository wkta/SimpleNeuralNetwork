package com.jlmd.simpleneuralnetwork.only_prix;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class JsonGetterFromWeb {

    //https://min-api.cryptocompare.com/data/histohour?aggregate=1&e=KRAKEN&extraParams=CryptoCompare&fsym=XRP&limit=96
    // &tryConversion=false&tsym=EUR&toTs=1514847600

    // plus vieux au plus recent  -> derniere ligne est la plus recente


    public static void main(String [] args) throws IOException, ParseException {
        JsonParserFromFile jsonParser = new JsonParserFromFile();
        JsonGetterFromWeb jsonGetter = new JsonGetterFromWeb();

        jsonParser.emptyFile("data/tendancesInput.txt");
        jsonParser.emptyFile("data/tendancesOutput.txt");

        int  nbSlides= 25;//limite max -> 25 fenetres
        for(int stepInPast = 1 ; stepInPast < nbSlides; stepInPast++){


            //TODO -> apartir de mes datas

            jsonGetter.writeJsonToFile(stepInPast);
            jsonParser.write1LineInTendances();
        }
    }

    private  void writeJsonToFile(int stepInPast) {
        int windowSize = 86;
        String url = "https://min-api.cryptocompare.com/data/histohour?aggregate=1&e=KRAKEN&extraParams=CryptoCompare&fsym=BTC&limit="+
                (windowSize * stepInPast)  +"&tryConversion=false&tsym=EUR";

        System.out.println(url + "  " + stepInPast +"/100"  ); // memes lignes à partir de 24 ??

        HttpGet httpGet = new HttpGet(url);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            CloseableHttpResponse response = client.execute(httpGet);
            String result = EntityUtils.toString(response.getEntity());
            EntityUtils.consume(response.getEntity());
            httpGet.releaseConnection();
            response.close();
            client.close();

            // parse Json
            String jsonBody = result.toString();

            JsonParserFromFile.emptyFile(JsonParserFromFile.pathnameBTC);
            JsonParserFromFile.writeToFile(jsonBody, JsonParserFromFile.pathnameBTC);
        } catch (Exception e ){
            e.printStackTrace();
        }

    }
}
