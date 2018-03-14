package com.bjz.imusicteacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bjz.cnninference.model.Model;
import com.bjz.cnninference.model.ModelBuilder;
import com.bjz.imusicteacher.model.PredictionModel;
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.56.1:3000")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        PredictionModelService service = retrofit.create(PredictionModelService.class);

        service.getModel().enqueue(new Callback<PredictionModel>() {
            @Override
            public void onResponse(Call<PredictionModel> call, Response<PredictionModel> response) {
                PredictionModel body = response.body();
            }

            @Override
            public void onFailure(Call<PredictionModel> call, Throwable t) {
                throw new RuntimeException("CALL FAILURE" + t.getMessage());
            }
        });

    }
}
