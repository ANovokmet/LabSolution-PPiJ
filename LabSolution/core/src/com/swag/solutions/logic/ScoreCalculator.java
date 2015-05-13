package com.swag.solutions.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.swag.solutions.input.ShakeDetector;

/**
 * Created by Branimir on 13.5.2015..
 * Igrač počinje sa nekim početnim scorom koji se smanjuje sa prolaskom vremena
 * i trešnjom mobitela. Dakle manje vrijeme, manje trešnje -> veći score.
 */
public class ScoreCalculator extends Actor {
    private final ShakeDetector shakeDetector;
    private float score = 1000f;

    private static final float TIME_DECREASE_RATE = 10f;
    private static final float SHAKE_DECREASE_RATE = 20f;

    public ScoreCalculator(ShakeDetector shakeDetector) {
        this.shakeDetector = shakeDetector;
    }

    @Override
    public void act(float delta) {
        score -= delta * TIME_DECREASE_RATE;
        if (shakeDetector.deviceBeingShaken()) {
            score -= delta * SHAKE_DECREASE_RATE;
        }
        if (score < 0f) {
            score = 0f;
        }
        Gdx.app.error("Score", "" + score);
    }
}
