package com.jlmd.simpleneuralnetwork.prix_volume;


import com.jlmd.simpleneuralnetwork.only_prix.JsonParserFromFile;
import com.jlmd.simpleneuralnetwork.prix_volume.model.HeureInterval;
import com.jlmd.simpleneuralnetwork.prix_volume.model.LigneNormalisée;
import com.jlmd.simpleneuralnetwork.prix_volume.model.Transac;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class InputWriterFromTransacFiles {

    String sico = "1rPCAP";
    int sizeInputData = 40;
    int sizeAfterBuy = 60;
    int nbLigneInTest = 50;
    static String inputTrainPath = "data/tendancesInputPrixVolumeTrain.txt";
    static String outputTrainPath = "data/tendancesOutputPrixVolumeTrain.txt";
    static String inputTestPath = "data/tendancesInputPrixVolumeTest.txt";
    static String outputTestPath = "data/tendancesOutputPrixVolumeTest.txt";

    public static void main(String [] args) {

        InputWriterFromTransacFiles inputWriterFromTransacFiles = new InputWriterFromTransacFiles();

        JsonParserFromFile.emptyFile(inputTrainPath);
        JsonParserFromFile.emptyFile(outputTrainPath);

        JsonParserFromFile.emptyFile(inputTestPath);
        JsonParserFromFile.emptyFile(outputTestPath);

        inputWriterFromTransacFiles.writeLinesInTendances();
    }

    public void writeLinesInTendances()  {

        //itere sur tous les jours, 2 fenetres par jour ->
        // 11h-> 11h30   -> prends les 30 a partir de heure debut
        // 14h-14h30

        String path = "E:/bourse/sorties/valeursMinuteThread/transac/cac/";
        List<String> listJours = getJours(path);

        List<HeureInterval> heuresInterval = getHeuresInterval();

        List<LigneNormalisée> ligneNormalisées = new ArrayList<>();

        for(String jour : listJours) {
            for(HeureInterval heureInterval : heuresInterval){
                List<Transac> transacs = getTransacs(sico , jour, path, heureInterval);
                if(transacs!= null &&  transacs.size() == sizeInputData + sizeAfterBuy){

                    double minPrix = getMinPrix(transacs);
                    double maxPrix = getMaxPrix(transacs);

                    double minVol = getMinVolume(transacs);
                    double maxVol = getMaxVolume(transacs);

                    String inputValues = "";
                    for (int i = 0; i < sizeInputData - 1; i++) {
                        if (i < sizeInputData - 2) {
                                inputValues += JsonParserFromFile.normalise(minPrix, maxPrix, transacs.get(i).prix) + ",";
                                //  +JsonParserFromFile.normalise(minVol, maxVol, transacs.get(i).volume) + ",";

                        } else {
                            inputValues += JsonParserFromFile.normalise(minVol, maxVol, transacs.get(i).prix) ; //+ "    " + jour +" " + formatToHeur(heureInterval.heureDebut);
                                // + "," + JsonParserFromFile.normalise(minVol, maxVol, transacs.get(i).volume) ;
                        }
                    }

                    LigneNormalisée ligne = new LigneNormalisée();
                    ligne.input = inputValues;

                    // met 0 ou 1 dans output.txt selon prix sur 5 minutes suivantes >  transacs.get(sizeInputData - 1 ).prix + 2%
                    int isGainMaxAtteint = 0;
                    double targetPriceForGain = transacs.get(sizeInputData - 2).prix + (transacs.get(sizeInputData - 2).prix * 0.0025);
                    for (int i = sizeInputData - 1; i < sizeInputData + sizeAfterBuy - 1; i++) {
                        if (transacs.get(i).prix  > targetPriceForGain) {
                            //gain max attends dans les 5 vals suivantes
                            isGainMaxAtteint = 1;
                            break;
                        }
                    }

                    ligne.output = isGainMaxAtteint+"";
                    ligneNormalisées.add(ligne);

                }else {
                    if(transacs == null){
                        System.out.println("null le : " + path +" " + jour  + " " + sico);
                    }
//                    else {
//                        System.out.println(" < "+sizeInputData+" le : " + path +" " + jour  + " " + sico +"     " + transacs.size());
//                    }
                }
            }
        }

        Collections.shuffle(ligneNormalisées);

        writeLignes(ligneNormalisées);

        System.out.println("fertish");
    }

    private void writeLignes(List<LigneNormalisée> ligneNormalisées) {

        int nbLigne = 0;
        for(LigneNormalisée ligneNormalisée : ligneNormalisées) {
            if(nbLigne > nbLigneInTest) {
                JsonParserFromFile.writeToFile(ligneNormalisée.input, inputTrainPath);
            } else{
                JsonParserFromFile.writeToFile(ligneNormalisée.input, inputTestPath);
            }

            if(nbLigne > nbLigneInTest) {
                JsonParserFromFile.writeToFile(ligneNormalisée.output, outputTrainPath);
            } else {
                System.out.println(ligneNormalisée.output);
                JsonParserFromFile.writeToFile(ligneNormalisée.output , outputTestPath);
            }
            nbLigne++;
        }
    }

    public static String formatToHeur(Date heure) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(heure);

    }

    private double getMaxPrix(List<Transac> transacs) {
        double max = transacs.get(0).prix;
        for(Transac transac: transacs){
            if(transac.prix > max){
                max = transac.prix;
            }
        }
        return max;
    }

    private double getMinPrix(List<Transac> transacs) {
        double min = transacs.get(0).prix;
        for(Transac transac: transacs){
            if(transac.prix < min){
                min = transac.prix;
            }
        }
        return min;
    }

    private double getMaxVolume(List<Transac> transacs) {
        double max = transacs.get(0).volume;
        for(Transac transac: transacs){
            if(transac.volume > max){
                max = transac.volume;
            }
        }
        return max;
    }

    private double getMinVolume(List<Transac> transacs) {
        double min = transacs.get(0).volume;
        for(Transac transac: transacs){
            if(transac.volume < min){
                min = transac.volume;
            }
        }
        return min;
    }

    public List<String> getJours(String fileDir) {

        File folder = new File(fileDir);
        File[] listOfFiles = folder.listFiles();
        List<String> files = new ArrayList<>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isDirectory() && listOfFiles[i].getName().indexOf("tests") == -1 && listOfFiles[i].getName().indexOf("sortie histo") == -1
                    && listOfFiles[i].getName().indexOf("pertesTest") == -1 && listOfFiles[i].getName().indexOf("sortis html") == -1) {
                files.add(listOfFiles[i].getName());
            }

        }
        return files;
    }

    public List<HeureInterval> getHeuresInterval() {
        List<HeureInterval> heuresInterval = new ArrayList<>();

        //toutes les 10 mins
        int h = 10;
        int m =0;
        while(h <= 16){
            m=0;
            while(m <= 6){
                addHour(heuresInterval, h+":"+m+"0:00");
                if(h == 16 && m ==0){
                    break;
                }
                m++;
            }
            h++;
        }

        return heuresInterval;
    }

    private void addHour(List<HeureInterval> heuresInterval, String s) {
        HeureInterval heureInterval0 = new HeureInterval();
        heureInterval0.heureDebut = heureToDate(s);
        heuresInterval.add(heureInterval0);
    }

    public static Date heureToDate(String h) {
        SimpleDateFormat parser = new SimpleDateFormat("HH:mm:ss");
        try {
            if (h == null) {
                return null;
            }
            return parser.parse(h.trim());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<Transac> getTransacs(String sico, String jour, String path, HeureInterval heureInterval) {


        List<Transac> lines = new ArrayList<>();
        String ligne;
        BufferedReader br = null;
        InputStream ips;
        String []items;

        try {
            String completePath = path  +jour + "/"+ sico + ".txt";
            ips = new FileInputStream(completePath);
            InputStreamReader ipsr = new InputStreamReader(ips);
            br = new BufferedReader(ipsr);
        } catch (FileNotFoundException e1) {
            return null;
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        try {
            if (br != null) {
                ligne = br.readLine();
                boolean start = false;
                while (ligne != null) {
                    items = ligne.split(";");
                    if (items.length == 3) {
                        String heure = items[0];
                        Date heureD = heureToDate(heure);
                        if (!start && (heureD.equals(heureInterval.heureDebut) || heureD.after(heureInterval.heureDebut))) {
                            start = true;
                        }
                        if (start && lines.size() > sizeInputData + sizeAfterBuy - 1) {
                            break;
                        }

                        if (start && lines.size() < sizeInputData + sizeAfterBuy) {
                            String val = items[1];
                            double valtemp = Double.parseDouble(val);
                            Transac transac = new Transac();
                            transac.sico = sico;
                            transac.heure = heureD;
                            transac.prix = valtemp;
                            transac.volume = Double.parseDouble(items[2]);
                            lines.add(transac);
                        }
                    }
                    ligne = br.readLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;
    }
}
