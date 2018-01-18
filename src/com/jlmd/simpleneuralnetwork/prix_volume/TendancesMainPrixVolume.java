package com.jlmd.simpleneuralnetwork.prix_volume;

import com.jlmd.simpleneuralnetwork.neuralnetwork.NeuralNetwork;
import com.jlmd.simpleneuralnetwork.neuralnetwork.callback.INeuralNetworkCallback;
import com.jlmd.simpleneuralnetwork.neuralnetwork.entity.Error;
import com.jlmd.simpleneuralnetwork.neuralnetwork.entity.Result;
import com.jlmd.simpleneuralnetwork.neuralnetwork.utils.DataUtils;

import java.io.*;
import java.util.Date;
import java.util.List;

public class TendancesMainPrixVolume {

    static String inputTrainPath = "data/tendancesInputPrixVolumeTrain.txt";
    static String outputTrainPath = "data/tendancesOutputPrixVolumeTrain.txt";

    static String inputTestPath = "data/tendancesInputPrixVolumeTest.txt";
    static String outputTestPath = "data/tendancesOutputPrixVolumeTest.txt";

    List<float[]> xTest = DataUtils.readInputsListFromFile(inputTestPath);
    int[] tTest = DataUtils.readOutputsFromFile(outputTestPath);

    public static void main(String [] args){
        TendancesMainPrixVolume tendancesMainPrixVolume = new TendancesMainPrixVolume();

        tendancesMainPrixVolume.train();

//        tendancesMainPrixVolume.loadNetworkAndPredict();
    }


    private void loadNetworkAndPredict() {
        try {
            Result result = deserialise();

            System.out.println("Success percentage: " + result.getSuccessPercentage() + " train finished à " + new Date());
            int i = 0;
            int nbJuste = 0;
            for (float[] vals : xTest) {

                // 2) si success alors lance un test :
                // float[] valueToPredict = new float[] {0.83333f,-0.23871f,1.0f,-0.22581f,0.83333f,-0.84516f,0.16667f,-0.81935f,0.33333f,-0.27742f,0.5f,1.0f,0.33333f,-0.87097f,0.33333f,0.23871f,0.5f,-0.47097f,0.5f,-0.36774f,0.16667f,0.25161f,0.16667f,-0.9871f,0.5f,-0.2129f,0.5f,0.04516f,-0.33333f,-0.62581f,-0.66667f,-0.94839f,-0.66667f,-0.72903f,-0.83333f,0.75484f,-0.83333f,0.04516f,-0.83333f,-0.14839f,-0.83333f,-0.71613f,-0.83333f,-1.0f,-0.83333f,-0.76774f,-0.66667f,-0.62581f,-0.5f,0.26452f,-0.83333f,0.22581f,-1.0f,-0.26452f,-1.0f,0.74194f,0.24465f,-0.22581f};

                //doit etre 0
                System.out.println("Predicted result: " + result.predictValue(vals) + "  =? " + tTest[i]);
                if (result.predictValue(vals) == tTest[i]) {
                    nbJuste++;
                }
                i++;
            }

            System.out.println("res global : " + nbJuste + "/" + xTest.size() + " " + ((double) nbJuste / (double) xTest.size()));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    private void train() {
        System.out.println("Starting neural network sample... demarre à " + new Date());

        float[][] x = DataUtils.readInputsFromFile(inputTrainPath);
        int[] t = DataUtils.readOutputsFromFile(outputTrainPath);

        //serialisation

        INeuralNetworkCallback neuralNetworkCallback = new INeuralNetworkCallback() {
            // 1.2)  quand train finif, va dans callback cad -> success ou failure

            //res global : 41/51 0.80

            @Override
            public void success(Result result) {


                System.out.println("Success percentage: " + result.getSuccessPercentage() + " train finished à " + new Date());
                int i = 0;
                int nbJuste = 0;
                for (float[] vals : xTest) {

                    // 2) si success alors lance un test :
                    // float[] valueToPredict = new float[] {0.83333f,-0.23871f,1.0f,-0.22581f,0.83333f,-0.84516f,0.16667f,-0.81935f,0.33333f,-0.27742f,0.5f,1.0f,0.33333f,-0.87097f,0.33333f,0.23871f,0.5f,-0.47097f,0.5f,-0.36774f,0.16667f,0.25161f,0.16667f,-0.9871f,0.5f,-0.2129f,0.5f,0.04516f,-0.33333f,-0.62581f,-0.66667f,-0.94839f,-0.66667f,-0.72903f,-0.83333f,0.75484f,-0.83333f,0.04516f,-0.83333f,-0.14839f,-0.83333f,-0.71613f,-0.83333f,-1.0f,-0.83333f,-0.76774f,-0.66667f,-0.62581f,-0.5f,0.26452f,-0.83333f,0.22581f,-1.0f,-0.26452f,-1.0f,0.74194f,0.24465f,-0.22581f};

                    //doit etre 0
                    System.out.println("Predicted result: " + result.predictValue(vals) + "  =? " + tTest[i]);
                    if (result.predictValue(vals) == tTest[i]) {
                        nbJuste++;
                    }
                    i++;
                }

                System.out.println("res global : " + nbJuste + "/" + xTest.size() + " " + ((double) nbJuste / (double) xTest.size()));



                try {
                    //serialise
                    serialise(result);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failure(Error error) {
                System.out.println("Error: " + error.getDescription());
            }
        };


        NeuralNetwork neuralNetwork = new NeuralNetwork(x, t, neuralNetworkCallback);

        // 1.1) train
        neuralNetwork.startLearning();

    }

    private void serialise(Result result) throws IOException {
        File fichier =  new File("network.ser") ;
        ObjectOutputStream oos =  new ObjectOutputStream(new FileOutputStream(fichier)) ;

        // sérialization de l'objet
        oos.writeObject(result) ;
    }

    private Result deserialise() throws IOException, ClassNotFoundException {
        File fichier =  new File("network.ser") ;

        // ouverture d'un flux sur un fichier
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(fichier));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // désérialization de l'objet
        Result m = (Result)ois.readObject() ;
        return m;
    }

}
