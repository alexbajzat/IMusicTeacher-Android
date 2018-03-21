package com.bjz.imusicteacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bjz.imusicteacher.activity.ProcessingActivity;
import com.bjz.imusicteacher.model.descriptor.ModelDescriptor;
import com.bjz.imusicteacher.model.network.NetworkModel;
import com.bjz.imusicteacher.model.network.NetworkModelBuilder;
import com.bjz.imusicteacher.service.PredictionModelService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, ProcessingActivity.class);
        startActivity(intent);



    }
}
