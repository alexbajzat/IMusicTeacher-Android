package com.bjz.imusicteacher.prediction;

import com.bjz.imusicteacher.model.PredictionModel;
import com.bjz.imusicteacher.service.PredictionModelService;

import org.junit.Test;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by bjz on 3/14/2018.
 */

public class ModelPrediction {
    @Test
    public void testDataFetch() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:3000")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        PredictionModelService service = retrofit.create(PredictionModelService.class);

        service.getModel();
    }
}
