package com.bjz.imusicteacher.service;

import com.bjz.imusicteacher.model.PredictionModel;

import retrofit2.http.GET;

/**
 * Created by bjz on 3/14/2018.
 */

public interface PredictionModelService {
    @GET(value = "/network/model")
    PredictionModel getModel();
}
