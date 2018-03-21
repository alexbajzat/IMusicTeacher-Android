package com.bjz.imusicteacher.model.network;

/**
 * Created by bjz on 3/21/2018.
 */

public class Configuration {
    int inputHeight;
    int inputWidth;
    boolean hasChannelDimension;

    public Configuration(int inputHeight, int inputWidth, boolean hasChannelDimension) {
        this.inputHeight = inputHeight;
        this.inputWidth = inputWidth;
        this.hasChannelDimension = hasChannelDimension;
    }
}
