package com.jlmd.simpleneuralnetwork.example;

import com.jlmd.simpleneuralnetwork.neuralnetwork.NeuralNetwork;
import com.jlmd.simpleneuralnetwork.neuralnetwork.callback.INeuralNetworkCallback;
import com.jlmd.simpleneuralnetwork.neuralnetwork.entity.Error;
import com.jlmd.simpleneuralnetwork.neuralnetwork.entity.Result;
import com.jlmd.simpleneuralnetwork.neuralnetwork.utils.DataUtils;

public class TendancesMain {

    public static void main(String [] args){
        System.out.println("Starting neural network sample... ");

        float[][] x = DataUtils.readInputsFromFile("data/tendancesInput.txt");
        int[] t = DataUtils.readOutputsFromFile("data/tendancesOutput.txt");

        NeuralNetwork neuralNetwork = new NeuralNetwork(x, t, new INeuralNetworkCallback() {
            // 1.2)  quand train finiff, va dans callback cad -> success ou failure

            @Override
            public void success(Result result) {
                System.out.println("Success percentage: " + result.getSuccessPercentage());

                // 2) si success alors lance un test :
                float[] valueToPredict = new float[] {-0.34167f,-0.32833f,-0.32083f,-0.32f,-0.31252f,-0.30579f,-0.29692f,-0.32583f,-0.31178f,-0.34833f,-0.36573f,-0.42917f,-0.41667f,-0.35845f,-0.38112f,-0.38474f,-0.37833f,-0.34416f,-0.34427f,-0.3432f,-0.33173f,-0.34729f,-0.36083f,-0.35084f,-0.35008f,-0.35751f,-0.39271f,-0.38958f,-0.37958f,-0.42167f,-0.415f,-0.42496f,-0.41042f,-0.4075f,-0.39818f,-0.38492f,-0.40583f,-0.39521f,-0.385f,-0.37817f,-0.38433f,-0.38793f,-0.38818f,-0.39439f,-0.38464f,-0.39733f,-0.4021f,-0.40083f,-0.38617f,-0.37078f,-0.38243f,-0.37501f,-0.36061f,-0.33142f,-0.33865f,-0.33492f,-0.33144f,-0.34421f,-0.34152f,-0.33867f,-0.31947f,-0.31542f,-0.32409f,-0.32088f,-0.31832f,-0.31258f,-0.31247f,-0.32217f,-0.32467f,-0.3226f,-0.31973f,-0.31667f,-0.31675f,-0.30488f,-0.30833f,-0.31668f,-0.30485f,-0.30999f,-0.31967f,-0.32592f,-0.325f,-0.31968f,-0.31667f,-0.31971f,-0.31891f};
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
