package com.swag.solutions.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by Mate on 13.5.2015..
 */
public class Score extends Actor implements Observer{

    private int score;
    private int level;
    private BitmapFont textFont;
    private Camera camera;

    public Score(Camera camera){
        this.camera = camera;
        createFonts();
        score = 0;
        level = 0;
    }

    @Override
    public void draw(Batch batch, float alpha){
        textFont.draw(batch, Integer.toString(score), 16 + getX() - camera.viewportWidth / 2, -32 + getY() + camera.viewportHeight / 2 + 18);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        setX(camera.position.x);
        setY(camera.position.y);
    }

    @Override
    public void update(Observable o, Object arg) {
        score = (int) (100 * Math.round(Math.pow(2, level)));
        level += 1;
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
