package com.bjz.imusicteacher.model.network.prediction;

/**
 * Created by bjz on 3/21/2018.
 */

public class Prediction {
    double[] probabilities;
    double processingTime;

    public double[] getProbabilities() {
        return probabilities;
    }

    public Prediction setProbabilities(double[] probabilities) {
        this.probabilities = probabilities;
        return this;
    }

    public double getProcessingTime() {
        return processingTime;
    }

    public Prediction setProcessingTime(double processingTime) {
        this.processingTime = processingTime;
        return this;
    }
}
