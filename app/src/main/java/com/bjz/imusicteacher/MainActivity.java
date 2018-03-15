package com.bjz.imusicteacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.56.1:3000")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        PredictionModelService service = retrofit.create(PredictionModelService.class);

        service.getModel().enqueue(new Callback<ModelDescriptor>() {
            @Override
            public void onResponse(Call<ModelDescriptor> call, Response<ModelDescriptor> response) {
                ModelDescriptor body = response.body();
                NetworkModel model = NetworkModel.builder()
                        .build(body);
            }

            @Override
            public void onFailure(Call<ModelDescriptor> call, Throwable t) {
                throw new RuntimeException("CALL FAILURE" + t.getMessage());
            }
        });

    }
}
