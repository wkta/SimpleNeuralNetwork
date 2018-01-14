package com.jlmd.simpleneuralnetwork.prix_volume;

import com.jlmd.simpleneuralnetwork.neuralnetwork.NeuralNetwork;
import com.jlmd.simpleneuralnetwork.neuralnetwork.callback.INeuralNetworkCallback;
import com.jlmd.simpleneuralnetwork.neuralnetwork.entity.Error;
import com.jlmd.simpleneuralnetwork.neuralnetwork.entity.Result;
import com.jlmd.simpleneuralnetwork.neuralnetwork.utils.DataUtils;

import java.util.List;

public class TendancesMainPrixVolume {

    static String inputTrainPath = "data/tendancesInputPrixVolumeTrain.txt";
    static String outputTrainPath = "data/tendancesOutputPrixVolumeTrain.txt";

    static String inputTestPath = "data/tendancesInputPrixVolumeTest.txt";
    static String outputTestPath = "data/tendancesOutputPrixVolumeTest.txt";

    public static void main(String [] args){
        System.out.println("Starting neural network sample... ");

        float[][] x = DataUtils.readInputsFromFile(inputTrainPath);
        int[] t = DataUtils.readOutputsFromFile(outputTrainPath);

        List<float[]> xTest = DataUtils.readInputsListFromFile(inputTestPath);
        int[] tTest = DataUtils.readOutputsFromFile(outputTestPath);

        NeuralNetwork neuralNetwork = new NeuralNetwork(x, t, new INeuralNetworkCallback() {
            // 1.2)  quand train finif, va dans callback cad -> success ou failure

            @Override
            public void success(Result result) {
                System.out.println("Success percentage: " + result.getSuccessPercentage());
                int i = 0;
                int nbJuste = 0;
                for(float[] vals : xTest){

                    // 2) si success alors lance un test :
                    // float[] valueToPredict = new float[] {0.83333f,-0.23871f,1.0f,-0.22581f,0.83333f,-0.84516f,0.16667f,-0.81935f,0.33333f,-0.27742f,0.5f,1.0f,0.33333f,-0.87097f,0.33333f,0.23871f,0.5f,-0.47097f,0.5f,-0.36774f,0.16667f,0.25161f,0.16667f,-0.9871f,0.5f,-0.2129f,0.5f,0.04516f,-0.33333f,-0.62581f,-0.66667f,-0.94839f,-0.66667f,-0.72903f,-0.83333f,0.75484f,-0.83333f,0.04516f,-0.83333f,-0.14839f,-0.83333f,-0.71613f,-0.83333f,-1.0f,-0.83333f,-0.76774f,-0.66667f,-0.62581f,-0.5f,0.26452f,-0.83333f,0.22581f,-1.0f,-0.26452f,-1.0f,0.74194f,0.24465f,-0.22581f};

                    //doit etre 0
                    System.out.println("Predicted result: " + result.predictValue(vals) +"  =? " + tTest[i]);
                    if(result.predictValue(vals) == tTest[i]){
                        nbJuste++;
                    }
                    i++;
                }

                System.out.println("res global : " + nbJuste +"/" + xTest.size() + " " + ((double)nbJuste / (double)xTest.size()));
            }

            @Override
            public void failure(Error error) {
                System.out.println("Error: " + error.getDescription());
            }
        });



        // 1.1) train
        neuralNetwork.startLearning();

        //TODO
        // save

    }


    // TODO
    // load neural network
    // System.out.println("Predicted result: " + result.predictValue(valueToPredict));
    // save
}
