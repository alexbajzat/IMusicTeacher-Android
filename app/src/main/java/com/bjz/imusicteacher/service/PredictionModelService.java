package com.bjz.imusicteacher.service;

import com.bjz.imusicteacher.model.descriptor.ModelDescriptor;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by bjz on 3/14/2018.
 */

public interface PredictionModelService {
    @GET(value = "/model")
    Call<ModelDescriptor> getModel();
}
