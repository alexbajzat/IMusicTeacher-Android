package com.bjz.imusicteacher.model.descriptor;

import com.bjz.imusicteacher.model.descriptor.LayerDescriptor;

import java.util.List;

/**
 * Created by bjz on 3/14/2018.
 */

public class ModelDescriptor {
    private List<LayerDescriptor> layers;
    private PredictionSample sample;

    public List<LayerDescriptor> getLayers() {
        return layers;
    }

    public void setLayers(List<LayerDescriptor> layers) {
        this.layers = layers;
    }

    public PredictionSample getSample() {
        return sample;
    }

    public void setSample(PredictionSample sample) {
        this.sample = sample;
    }
}
