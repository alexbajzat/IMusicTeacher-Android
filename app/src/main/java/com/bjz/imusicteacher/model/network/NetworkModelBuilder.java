package com.bjz.imusicteacher.model.network;

import android.util.Log;

import com.bjz.cnninference.activations.Activation;
import com.bjz.cnninference.activations.ReLUActivation;
import com.bjz.cnninference.layers.ConvComplexLayer;
import com.bjz.cnninference.layers.FlatteningTransitionLayer;
import com.bjz.cnninference.layers.HiddenSimpleLayer;
import com.bjz.cnninference.layers.MaxPoolingComplexLayer;
import com.bjz.cnninference.layers.apis.ComplexLayer;
import com.bjz.cnninference.layers.apis.SimpleLayer;
import com.bjz.cnninference.model.Model;
import com.bjz.cnninference.model.ModelBuilder;
import com.bjz.cnninference.prediction.PredictionResult;
import com.bjz.imusicteacher.exception.IncosistentModelTransportException;
import com.bjz.imusicteacher.model.descriptor.ActivationType;
import com.bjz.imusicteacher.model.descriptor.LayerDescriptor;
import com.bjz.imusicteacher.model.descriptor.LayerType;
import com.bjz.imusicteacher.model.descriptor.ModelDescriptor;
import com.bjz.imusicteacher.model.descriptor.PredictionSample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bjz on 3/15/2018.
 */

public class NetworkModelBuilder {
    private static final double ERROR = 1e-6;

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

                simpleLayers.add(new HiddenSimpleLayer(activation, descriptor.getWeights()[0][0], descriptor.getBiases()[0]));
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

        // validate if the dto transport failed or the client model operations don`t match
        validate(model, modelDescriptor.getSample());
        return new NetworkModel(model, configuration);
    }

    private void validate(Model model, PredictionSample sample) {
        double[][][] data = sample.getData();
        long start = System.currentTimeMillis();
        PredictionResult predict = model.predict(data);
        long stop = System.currentTimeMillis();
        Log.d("NetworkBuilderValidator", String.format("Time to predict: %d ms", stop - start));

        double[] actualRawResults = predict.getRawResults();
        double[] validRawResults = sample.getResult();

        for (int i = 0; i < actualRawResults.length; i++) {
            if (!equals(actualRawResults[i], validRawResults[i])) {
                throw new IncosistentModelTransportException("Model integrity/construction degradation. Raw results do not match");
            }
        }

        double[] actualProbs = predict.getProbabilites();
        double[] validProbs = sample.getProbabilities();

        for (int i = 0; i < actualRawResults.length; i++) {
            if (!equals(actualProbs[i], validProbs[i])) {
                throw new IncosistentModelTransportException("Model integrity/construction degradation. Raw results do not match");
            }
        }
    }

    /**
     *
     * @param a first double number
     * @param b second double number
     * @return true if they equal (with an epsilon precision)
     */
    public static boolean equals(double a, double b) {
        return a == b || Math.abs(a - b) < ERROR;
    }

}
