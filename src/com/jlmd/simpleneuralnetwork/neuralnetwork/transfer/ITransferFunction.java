package com.jlmd.simpleneuralnetwork.neuralnetwork.transfer;

import java.io.Serializable;

/**
 * Interface for transfer function. The function of this is limit the values of generated output
 * @author jlmd
 */
public interface ITransferFunction extends Serializable{
    /**
     * Calculate the transfer value limited by a function
     * @param value Transfer value
     * @return obtained value
     */
    float transfer(float value);
}
