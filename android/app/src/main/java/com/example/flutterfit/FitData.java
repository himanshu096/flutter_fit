package com.example.flutterfit;

import com.google.android.gms.fitness.data.Value;

public class FitData {
    int stepCount;
    Value distance;
    double weight;
    Value heartRate;
    long startTime;
    long endTime;

    public int getStepCount() {
        return stepCount;
    }

    public void setStepCount(int stepCount) {
        this.stepCount = stepCount;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Value getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Value heartRate) {
        this.heartRate = heartRate;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    public Value getDistance() {
        return distance;
    }

    public void setDistance(Value distance) {
        this.distance = distance;
    }
}
