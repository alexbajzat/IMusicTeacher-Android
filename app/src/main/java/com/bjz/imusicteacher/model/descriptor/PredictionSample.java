package com.bjz.imusicteacher.model.descriptor;

/**
 * Created by bjz on 3/24/2018.
 */

public class PredictionSample {
    private double[][][] data;
    private double[] result;
    private double[] probabilities;

    public double[][][] getData() {
        return data;
    }

    public void setData(double[][][] data) {
        this.data = data;
    }

    public double[] getResult() {
        return result;
    }

    public void setResult(double[] result) {
        this.result = result;
    }

    public double[] getProbabilities() {
        return probabilities;
    }

    public void setProbabilities(double[] probabilities) {
        this.probabilities = probabilities;
    }
}
