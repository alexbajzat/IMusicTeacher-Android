package com.bjz.imusicteacher.model.network.prediction;

/**
 * Created by bjz on 3/21/2018.
 */

public class Prediction {
    double[] probabilities;
    long processingTime;

    public double[] getProbabilities() {
        return probabilities;
    }

    public Prediction setProbabilities(double[] probabilities) {
        this.probabilities = probabilities;
        return this;
    }

    public long getProcessingTime() {
        return processingTime;
    }

    public Prediction setProcessingTime(long processingTime) {
        this.processingTime = processingTime;
        return this;
    }
}
