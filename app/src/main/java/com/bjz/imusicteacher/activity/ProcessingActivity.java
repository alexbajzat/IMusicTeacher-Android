package com.bjz.imusicteacher.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bjz.imusicteacher.R;
import com.bjz.imusicteacher.model.descriptor.ModelDescriptor;
import com.bjz.imusicteacher.model.network.Configuration;
import com.bjz.imusicteacher.model.network.NetworkModel;
import com.bjz.imusicteacher.model.network.prediction.Prediction;
import com.bjz.imusicteacher.service.PredictionModelService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class ProcessingActivity extends AppCompatActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    @BindView(R.id.previewTextureView)
    TextureView previewTextureView;

    @BindView(R.id.server_loading_layer_view)
    View serverLoadingView;
    @BindView(R.id.server_loading_text)
    TextView serverLoadingText;
    @BindView(R.id.prediction_debug_layout)
    LinearLayout debugView;
    @BindView(R.id.debug_processing_time)
    TextView debugProcessingTimeView;
    private CameraDevice mCameraDevice;
    private String mCameraId;
    private Size imageDimension;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CameraCaptureSession mCameraCaptureSession;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private NetworkModel model;
    private CameraDevice.StateCallback mCameraStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraDevice.close();

        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int i) {
            mCameraDevice.close();
            mCameraDevice = null;
        }
    };
    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
            initCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }
    };
    private CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            processImage();
        }
    };
    private HashMap<Integer, String> notesMap;

    private HashMap<Integer, Pair<TextView, String>> notesDebugDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);
        ButterKnife.bind(this);

        //TODO bullshit way to initialize, must change
        notesMap = new HashMap<>();
        notesMap.put(0, "C");
        notesMap.put(1, "D");
        notesMap.put(2, "E");
        notesMap.put(3, "F");
        notesMap.put(4, "G");
        notesMap.put(5, "A");
        notesMap.put(6, "B");
        notesMap.put(7, "J");
        notesMap.put(8, "K");
        notesMap.put(9, "L");

        for (Map.Entry<Integer, String> entry : notesMap.entrySet()) {
            TextView textView = new TextView(this);
            textView.setText(String.format("%s : %d", entry.getValue(), 0));
            textView.setId(entry.getKey());
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(20);
            textView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            debugView.addView(textView);
        }

        initializeModel();
        initPreviewTextureView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startBackgroundThread();
        if (previewTextureView.isAvailable()) {
            initCamera();
        } else {
            previewTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    protected void onPause() {
        stopBackgroundThread();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "You can`t use camera without permission", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    private void initPreviewTextureView() {
        previewTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
    }

    private void initCamera() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        assert cameraManager != null;

        try {
            mCameraId = cameraManager.getCameraIdList()[0];
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(mCameraId);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            cameraManager.openCamera(mCameraId, mCameraStateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void startCameraPreview() {
        try {
            SurfaceTexture texture = previewTextureView.getSurfaceTexture();
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            Surface surface = new Surface(texture);
            mPreviewRequestBuilder.addTarget(surface);
            mCameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    if (mCameraDevice == null || !previewTextureView.isAvailable() || imageDimension == null) {
                        Toast.makeText(getApplicationContext(), "Preview not available. Configuration failed", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mCameraCaptureSession = cameraCaptureSession;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                    Toast.makeText(getApplicationContext(), "Configuration change", Toast.LENGTH_SHORT).show();

                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    private void updatePreview() {
        if (mCameraDevice == null) {
            Toast.makeText(this, "Camera error", Toast.LENGTH_LONG).show();
        }
        // props for auto focus
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
        try {
            mCameraCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            Toast.makeText(getApplicationContext(), "Cannot preview", Toast.LENGTH_SHORT).show();
        }
    }

    private void processImage() {
        Bitmap original = previewTextureView.getBitmap();
        if (model != null) {
            Prediction result = model.process(original);
            updateDebugView(result);
        }
        System.out.println();
    }

    private void updateDebugView(Prediction result) {
        final double[] probabilities = result.getProbabilities();
        final double processingTime = result.getProcessingTime();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < probabilities.length; i++) {
                    TextView view = findViewById(i);
                    view.setText(String.format("%s : %f", notesMap.get(i), probabilities[i]));
                }
                debugProcessingTimeView.setText(String.format("Process time: %f", processingTime));
            }
        });
    }

    private void showLoadingPreview() {
        previewTextureView.setVisibility(View.VISIBLE);
        serverLoadingText.setVisibility(View.VISIBLE);
    }

    private void stopLoadingPreview() {
        serverLoadingView.setVisibility(View.INVISIBLE);
        serverLoadingText.setVisibility(View.INVISIBLE);
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();

        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //todo this is just a mock, remove
    private void initializeModel() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.143:3000")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        PredictionModelService service = retrofit.create(PredictionModelService.class);

        showLoadingPreview();
        service.getModel().enqueue(new Callback<ModelDescriptor>() {
            @Override
            public void onResponse(Call<ModelDescriptor> call, Response<ModelDescriptor> response) {
                ModelDescriptor body = response.body();
                model = NetworkModel.builder()
                        .build(body, new Configuration(64, 64, true));
                Log.d("Success", "Call made it");
                stopLoadingPreview();
            }

            @Override
            public void onFailure(Call<ModelDescriptor> call, Throwable t) {
                Log.d("Error", "Error");
                Toast.makeText(getApplicationContext(), "Cannot fetch data from server", Toast.LENGTH_LONG).show();
            }
        });
    }
}
