package com.swag.solutions.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Branimir on 4.5.2015..
 * Razred je rađen na uzoru na jedan tutorial, ali sam shvatio da ne valja.
 * MyShakeDetector je ispravna verzija.
 */
public class BadShakeDetector extends ShakeDetector {
    float prevAccelX;
    float prevAccelY;
    float prevAccelZ;
    long timeOfLastUpdate;
    boolean deviceBeingShaken;
    private static final int SHAKE_THRESHOLD = 500;
    private static final int TIME_DIFFERENCE_THRESHOLD = 100;

    public BadShakeDetector() {
        prevAccelX = Gdx.input.getAccelerometerX();
        prevAccelY = Gdx.input.getAccelerometerY();
        prevAccelZ = Gdx.input.getAccelerometerZ();
        timeOfLastUpdate = System.currentTimeMillis();
    }

    @Override
    public void act(float delta) {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - timeOfLastUpdate;
        if (timeDifference < TIME_DIFFERENCE_THRESHOLD) {
            return;
        }
        timeOfLastUpdate = currentTime;

        float currentAccelX = Gdx.input.getAccelerometerX();
        float currentAccelY = Gdx.input.getAccelerometerY();
        float currentAccelZ = Gdx.input.getAccelerometerZ();

        float positionDifference = Math.abs(
            currentAccelX + currentAccelY + currentAccelZ
            - prevAccelX - prevAccelY - prevAccelZ);
        float speed = positionDifference / timeDifference * 10000;
        //Gdx.app.log("Position difference", "" + positionDifference);
        //Gdx.app.log("Speed", "" + speed);

        deviceBeingShaken = (speed > SHAKE_THRESHOLD);

        prevAccelX = currentAccelX;
        prevAccelY = currentAccelY;
        prevAccelZ = currentAccelZ;
    }

    @Override
    public boolean deviceBeingShaken() {
        return deviceBeingShaken;
    }
}
