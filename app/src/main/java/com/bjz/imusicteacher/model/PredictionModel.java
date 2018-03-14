package com.bjz.imusicteacher.model;

import java.util.List;

/**
 * Created by bjz on 3/14/2018.
 */

public class PredictionModel {
    private List<LayerDescriptor> layers;

    public List<LayerDescriptor> getLayers() {
        return layers;
    }

    public void setLayers(List<LayerDescriptor> layers) {
        this.layers = layers;
    }
}
