package com.swag.solutions.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.swag.solutions.input.ShakeDetector;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Mate on 13.5.2015..
 * Pokraden dobar dio od Branimira :)
 * zivio oblikovni obrazac Promatrac
 */
public class Score extends Actor implements Observer{

    public static int totalScore;
    private int level;
    private float levelScore;
    private BitmapFont textFont;
    private Camera camera;

    private final ShakeDetector shakeDetector;
    private static final float TIME_DECREASE_RATE = 10f;
    private static final float SHAKE_DECREASE_RATE = 20f;


    AssetManager manager;

    public Score(Camera camera, ShakeDetector shakeDetector, AssetManager manager){
        this.camera = camera;
        this.shakeDetector = shakeDetector;
        this.manager = manager;

        setX(camera.position.x);
        setY(camera.position.y);

        textFont = manager.get("scorefont.ttf", BitmapFont.class);
        textFont.setColor(1f, 1f, 1f, 1f);

        totalScore = 0;
        levelScore = 1000;
        level = 0;

    }

    @Override
    public void draw(Batch batch, float alpha){
        textFont.draw(batch, Integer.toString(totalScore), 16 + getX() - camera.viewportWidth / 2, -32 + getY() + camera.viewportHeight / 2 + 18);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setX(camera.position.x);
        setY(camera.position.y);

        levelScore -= delta * TIME_DECREASE_RATE;
        if (shakeDetector.deviceBeingShaken()) {
            levelScore -= delta * SHAKE_DECREASE_RATE;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        totalScore += levelScore;
        if(totalScore < 0){
            totalScore = 0;
        }
        level += 1;
        levelScore = 1000 * Math.round(Math.pow(1.6, level));
        if (levelScore < 0f) {
            levelScore = 0f;
        }
    }

    public void reduceHintPoints() {
        levelScore -= 200 * Math.round(Math.pow(1.6, level));
        if (levelScore < 0f) {
            levelScore = 0f;
        }
    }


}
