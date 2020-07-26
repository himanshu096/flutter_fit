package com.example.flutterfit;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {

    private static int AUTH_REQUEST_CODE = 249;
    private FitnessOptions mFitnessOptions;

    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);

        String CHANNEL = "com.example.flutterfit/fitdata";
        new MethodChannel(flutterEngine.getDartExecutor(), CHANNEL).setMethodCallHandler((methodCall, result) -> {
            if (methodCall.method.equals("getFitData")) {
                initDataRead(result);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    void initDataRead(MethodChannel.Result result) {
        mFitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.AGGREGATE_WEIGHT_SUMMARY, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
                .build();

        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), mFitnessOptions)) {
            GoogleSignIn.requestPermissions(
                    this, // your activity instance
                    AUTH_REQUEST_CODE,
                    GoogleSignIn.getLastSignedInAccount(this),
                    mFitnessOptions);
        } else {
            accessGoogleFit().addOnSuccessListener(dataReadResponse -> {
                List<FitData> fitDataList = new ArrayList<FitData>();
                for (Bucket bucket : dataReadResponse.getBuckets()) {
                    FitData fitData = new FitData();
                    fitData.setStartTime(bucket.getStartTime(TimeUnit.MILLISECONDS));
                    fitData.setEndTime(bucket.getEndTime(TimeUnit.MILLISECONDS));

                    for (DataSet sets : bucket.getDataSets()) {
                        showDataSet(sets, fitData);
                    }
                    fitDataList.add(fitData);
                }
                Gson gson = new Gson();
                String data = gson.toJson(fitDataList);
                result.success(data);
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AUTH_REQUEST_CODE) {
                accessGoogleFit();
            }
        }
    }

    private Task<DataReadResponse> accessGoogleFit() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                .aggregate(DataType.TYPE_WEIGHT, DataType.AGGREGATE_WEIGHT_SUMMARY)
                .aggregate(DataType.TYPE_HEART_RATE_BPM, DataType.AGGREGATE_HEART_RATE_SUMMARY)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.DAYS)
                .build();

        GoogleSignInAccount account = GoogleSignIn
                .getAccountForExtension(this, mFitnessOptions);

        return Fitness.getHistoryClient(this, account).readData(readRequest);
    }

    private void showDataSet(DataSet dataSet, FitData fitData) {
        for (DataPoint dp : dataSet.getDataPoints()) {

            switch (dp.getDataType().getName()) {
                case "com.google.step_count.delta":
                    for (Field field : dp.getDataType().getFields()) {
                        fitData.setStepCount(dp.getValue(field).asInt());
                    }
                    return;
                case "com.google.distance.delta":
                    for (Field field : dp.getDataType().getFields()) {
                        fitData.setDistance(dp.getValue(field));
                    }
                    return;

                case "com.google.heart_rate.summary":
                    for (Field field : dp.getDataType().getFields()) {
                        Log.e("History", "\tField: " + field.getName() +
                                " Value: " + dp.getValue(field));
                        Log.d("codeo", field.getName());
                        if (field.getName().startsWith("average")) {
                            fitData.setHeartRate(dp.getValue(field));
                        }
                    }
                    return;
            }
        }
    }


}
