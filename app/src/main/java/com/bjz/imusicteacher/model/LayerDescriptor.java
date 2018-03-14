package com.bjz.imusicteacher.model;

/**
 * Created by bjz on 3/14/2018.
 */

public class LayerDescriptor {
    private double[][][] weights;
    private LayerType layerType;
    private ActivationType activationType;

    public double[][][] getWeights() {
        return weights;
    }

    public void setWeights(double[][][] weights) {
        this.weights = weights;
    }

    public LayerType getLayerType() {
        return layerType;
    }

    public void setLayerType(LayerType layerType) {
        this.layerType = layerType;
    }

    public ActivationType getActivationType() {
        return activationType;
    }

    public void setActivationType(ActivationType activationType) {
        this.activationType = activationType;
    }
}
