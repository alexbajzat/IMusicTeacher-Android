package com.bjz.imusicteacher.model.network;

import com.bjz.cnninference.activations.Activation;
import com.bjz.cnninference.activations.ReLUActivation;
import com.bjz.cnninference.layers.ConvComplexLayer;
import com.bjz.cnninference.layers.FlatteningTransitionLayer;
import com.bjz.cnninference.layers.HiddenSimpleLayer;
import com.bjz.cnninference.layers.MaxPoolingComplexLayer;
import com.bjz.cnninference.layers.api.ComplexLayer;
import com.bjz.cnninference.layers.api.SimpleLayer;
import com.bjz.cnninference.model.Model;
import com.bjz.cnninference.model.ModelBuilder;
import com.bjz.imusicteacher.model.descriptor.ActivationType;
import com.bjz.imusicteacher.model.descriptor.LayerDescriptor;
import com.bjz.imusicteacher.model.descriptor.LayerType;
import com.bjz.imusicteacher.model.descriptor.ModelDescriptor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjz on 3/15/2018.
 */

public class NetworkModelBuilder {
    NetworkModelBuilder() {
    }

    public NetworkModel build(ModelDescriptor modelDescriptor, Configuration configuration) {
        List<ComplexLayer> complexLayers = new ArrayList<>();
        List<SimpleLayer> simpleLayers = new ArrayList<>();
        FlatteningTransitionLayer flattenLayer = null;

        for (LayerDescriptor descriptor : modelDescriptor.getLayers()) {

            if (descriptor.getType().equals(LayerType.CONV)) {
                Activation activation = null;

                if (descriptor.getActivation().equals(ActivationType.RELU)) {
                    activation = new ReLUActivation();
                }

                complexLayers.add(new ConvComplexLayer(descriptor.getWeights(), descriptor.getConvParams().getStride(), true, activation));
            } else if (descriptor.getType().equals(LayerType.FLAT)) {
                flattenLayer = new FlatteningTransitionLayer();
            } else if (descriptor.getType().equals(LayerType.POOLING)) {
                complexLayers.add(new MaxPoolingComplexLayer());
            } else if (descriptor.getType().equals(LayerType.HIDDEN)) {
                Activation activation = null;

                if (descriptor.getActivation().equals(ActivationType.RELU)) {
                    activation = new ReLUActivation();
                }

                simpleLayers.add(new HiddenSimpleLayer(activation, descriptor.getWeights()[0], descriptor.getBiases()[0]));
            }
        }
        ModelBuilder modelBuilder = new ModelBuilder();
        for (ComplexLayer complexLayer : complexLayers) {
            modelBuilder.addComplexLayer(complexLayer);
        }
        modelBuilder.setTransitionLayer(flattenLayer);

        for (SimpleLayer simpleLayer : simpleLayers) {
            modelBuilder.addSimpleLayer(simpleLayer);
        }
        Model model = modelBuilder.build();
        return new NetworkModel(model, configuration);
    }

}
