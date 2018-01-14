package com.jlmd.simpleneuralnetwork.thom_4_config_output;

import com.jlmd.simpleneuralnetwork.neuralnetwork.NeuralNetwork;
import com.jlmd.simpleneuralnetwork.neuralnetwork.callback.INeuralNetworkCallback;
import com.jlmd.simpleneuralnetwork.neuralnetwork.entity.Error;
import com.jlmd.simpleneuralnetwork.neuralnetwork.entity.Result;
import com.jlmd.simpleneuralnetwork.neuralnetwork.utils.DataUtils;

public class TendancesMainThom4ConfigsOutpu {

    public static void main(String [] args){
        System.out.println("Starting neural network sample... ");

        float[][] x = DataUtils.readInputsFromFile("data/tendancesInputThom.txt");
        int[] t = DataUtils.readOutputsFromFile("data/tendancesOutputThom.txt");

        NeuralNetwork neuralNetwork = new NeuralNetwork(x, t, new INeuralNetworkCallback() {
            // 1.2)  quand train finifff, va dans callback cad -> success ou failure

            @Override
            public void success(Result result) {
                System.out.println("Success percentage: " + result.getSuccessPercentage());

                //TODO iterer sur les 60 inputs
                //checker avec les 60 outputs


                // 2) si success alors lance un test :
                float[] valueToPredict = new float[] {0.791391472674f, 0.933812773469f, 4.70377833105e-05f, 0.0661401887481f, 0.38870176649f, 0.495157215933f, 0.486238770343f, 0.4310223935f, 0.545061687342f, 0.467320674252f, 0.399454980506f, 0.476355211385f, 0.48911394838f, 0.432530822171f, 0.625448566274f, 0.487229852008f, 0.374339474761f, 0.484622751457f, 0.477024297942f, 0.400401767277f, 0.58143304065f, 0.46073713037f};

                //doit etre 0.0f, 0.0f, 0.0f, 1.0
                System.out.println("Predicted result: " + result.predictValue(valueToPredict));

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
