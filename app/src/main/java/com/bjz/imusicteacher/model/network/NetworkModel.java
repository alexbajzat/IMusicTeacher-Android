package com.bjz.imusicteacher.model.network;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.res.TypedArrayUtils;

import com.bjz.cnninference.model.Model;
import com.bjz.imusicteacher.model.network.prediction.Prediction;
import com.bjz.imusicteacher.utils.ProcessingUtils;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

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
        Bitmap grayscale = ProcessingUtils.getGrayscale(target);
        Bitmap resized = Bitmap.createScaledBitmap(grayscale, configuration.inputWidth, configuration.inputHeight, false);

        int bytes = resized.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        resized.copyPixelsToBuffer(buffer);

//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(configuration.inputWidth * configuration.inputHeight);
//        resized.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        double[][][] preparedData = new double[1][][];
        preparedData[0] = ProcessingUtils.reshapeAndConvertByteArray(buffer.array(), configuration.inputHeight, configuration.inputWidth);
        model.predict(preparedData);
        return new Prediction();
    }

}
