package com.swag.solutions.input;

import com.badlogic.gdx.Gdx;

/**
 * Created by Branimir on 6.5.2015..
 */
public class MyShakeDetector extends ShakeDetector {
    long timeOfLastUpdate;
    boolean deviceBeingShaken;
    int shakeInertia;
    private static final int INERTIA_START_VALUE = 3;
    private static final int SHAKE_THRESHOLD = 20;
    private static final int TIME_DIFFERENCE_THRESHOLD = 100;

    public MyShakeDetector() {
        timeOfLastUpdate = System.currentTimeMillis();
        deviceBeingShaken = false;
        shakeInertia = 0;
    }

    @Override
    public void act(float delta) {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - timeOfLastUpdate;
        if (timeDifference < TIME_DIFFERENCE_THRESHOLD) {
            return;
        }
        timeOfLastUpdate = currentTime;

        float totalAccel = Math.abs(Gdx.input.getAccelerometerX())
                + Math.abs(Gdx.input.getAccelerometerY())
                + Math.abs(Gdx.input.getAccelerometerZ());

        deviceBeingShaken = (totalAccel > SHAKE_THRESHOLD);
        shakeInertia =
                deviceBeingShaken ? INERTIA_START_VALUE : shakeInertia - 1;
        if (shakeInertia > 0) {
            deviceBeingShaken = true;
        }
    }

    @Override
    public boolean deviceBeingShaken() {
        return deviceBeingShaken;
    }
}
