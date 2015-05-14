package com.swag.solutions.logic;

import com.badlogic.gdx.Gdx;
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

    private int totalScore;
    private int level;
    private float levelScore;
    private BitmapFont textFont;
    private Camera camera;

    private final ShakeDetector shakeDetector;
    private static final float TIME_DECREASE_RATE = 10f;
    private static final float SHAKE_DECREASE_RATE = 20f;

    public Score(Camera camera, ShakeDetector shakeDetector){
        this.camera = camera;
        this.shakeDetector = shakeDetector;
        createFonts();
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
        levelScore = 1000 * Math.round(Math.pow(2, level));
    }

    private void createFonts() {
        FileHandle fontFile = Gdx.files.internal("04B_30__.TTF");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        textFont = generator.generateFont(parameter);
        generator.dispose();

        textFont.setColor(1f, 0f, 0f, 1f);
    }
}
