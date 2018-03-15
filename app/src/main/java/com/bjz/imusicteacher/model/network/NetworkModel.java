package com.bjz.imusicteacher.model.network;

import com.bjz.cnninference.model.Model;

/**
 * Created by bjz on 3/14/2018.
 */

public class NetworkModel {
    private Model model;

    NetworkModel(Model model) {
        this.model = model;
    }

    public static NetworkModelBuilder builder() {
        return new NetworkModelBuilder();
    }
}
