package com.bjz.imusicteacher.model.descriptor;

/**
 * Created by bjz on 3/14/2018.
 */

public class LayerDescriptor {
    private double[][][] weights;
    private double[][] biases;
    private LayerType type;
    private ActivationType activation;
    private ConvolutionParams convParams;

    public class ConvolutionParams {
        private int stride;

        public int getStride() {
            return stride;
        }

        public void setStride(int stride) {
            this.stride = stride;
        }
    }

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

    public ConvolutionParams getConvParams() {
        return convParams;
    }

    public void setConvParams(ConvolutionParams convParams) {
        this.convParams = convParams;
    }

    public double[][] getBiases() {
        return biases;
    }

    public void setBiases(double[][] biases) {
        this.biases = biases;
    }
}
