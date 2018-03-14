package com.bjz.imusicteacher.model.descriptor;

/**
 * Created by bjz on 3/14/2018.
 */

public class LayerDescriptor {
    private double[][][] weights;
    private LayerType type;
    private ActivationType activation;

    public double[][][] getWeights() {
        return weights;
    }

    public void setWeights(double[][][] weights) {
        this.weights = weights;
    }

    public LayerType getType() {
        return type;
    }

    public void setType(LayerType type) {
        this.type = type;
    }

    public ActivationType getActivation() {
        return activation;
    }

    public void setActivation(ActivationType activation) {
        this.activation = activation;
    }
}
