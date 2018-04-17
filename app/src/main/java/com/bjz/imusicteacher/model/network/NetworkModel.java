package com.bjz.imusicteacher.model.network;


import android.graphics.Bitmap;

import com.bjz.cnninference.model.Model;
import com.bjz.cnninference.prediction.PredictionResult;
import com.bjz.imusicteacher.exception.InconpatibleConfigException;
import com.bjz.imusicteacher.model.network.prediction.Prediction;

/**
 * Created by bjz on 3/14/2018.
 */

public class NetworkModel {
    private Model model;
    private Configuration configuration;


    NetworkModel(Model model, Configuration configuration) {
        this.model = model;
        this.configuration = configuration;
    }

    public static NetworkModelBuilder builder() {
        return new NetworkModelBuilder();
    }

    public Prediction process(Bitmap target) {
        long counterStart = System.currentTimeMillis();
        Bitmap resized = Bitmap.createScaledBitmap(target, configuration.inputWidth, configuration.inputHeight, false);

        double[][][] prepared = this.prepareData(resized);
        PredictionResult predict = model.predict(prepared);

        long finalCounter = System.currentTimeMillis() - counterStart;

        return new Prediction()
                .setProbabilities(predict.getProbabilites())
                .setProcessingTime(finalCounter);
    }

    private double[][][] prepareData(Bitmap target) {
        if (!target.getConfig().equals(Bitmap.Config.ARGB_8888)) {
            throw new InconpatibleConfigException(String.format("Accepted bitmap color config: %s, actual: %s"
                    , Bitmap.Config.ARGB_8888.name()
                    , target.getConfig().name()));
        }

        double[][][] result = new double[1][target.getHeight()][target.getWidth()];
        for (int i = 0; i < target.getHeight(); i++) {
            for (int j = 0; j < target.getWidth(); j++) {
                int pixel = target.getPixel(i, j);
                int R = (pixel >> 16) & 0xff;
                int G = (pixel >> 8) & 0xff;
                int B = pixel & 0xff;
                result[0][i][j] = (R + G + B) / 3; //grayscale
            }

        }
        return result;
    }

}
