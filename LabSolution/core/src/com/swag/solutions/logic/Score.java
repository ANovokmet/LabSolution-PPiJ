package com.swag.solutions.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
    private float MIN_SCORE;

    Table table;
    Label levelScoreLabel;
    Label totalScoreLabel;

    float PADDING_LEFT;

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
        MIN_SCORE = 0.1f * levelScore;
        level = 0;


        PADDING_LEFT = 16*camera.viewportWidth/480f;

        Skin skin = new Skin();
        BitmapFont textFont = manager.get("scorefont.ttf", BitmapFont.class);
        Label.LabelStyle ls = new Label.LabelStyle(textFont, Color.WHITE);
        skin.add("total_score_font",ls);

        BitmapFont subFont = manager.get("levelscorefont.ttf", BitmapFont.class);
        Label.LabelStyle ss = new Label.LabelStyle(subFont, Color.YELLOW);
        skin.add("level_score_font",ss);

        table = new Table();
        totalScoreLabel = new Label("= "+totalScore+" ", skin, "total_score_font");
        totalScoreLabel.setAlignment(Align.bottomLeft);
        table.add(totalScoreLabel);
        levelScoreLabel = new Label("+"+(int)levelScore+"", skin, "level_score_font");
        levelScoreLabel.setAlignment(Align.bottomLeft);
        table.add(levelScoreLabel);

        table.pack();
        table.setPosition(getX() -camera.viewportWidth/2+PADDING_LEFT, getY() + camera.viewportHeight / 2 - table.getHeight());
    }

    @Override
    public void draw(Batch batch, float alpha){
        /*
        textFont.draw(batch, Integer.toString(totalScore), 16 + getX() - camera.viewportWidth / 2, -32 + getY() + camera.viewportHeight / 2 + 18);
        textFont.draw(batch, Integer.toString((int)levelScore), getX() - camera.viewportWidth / 2, -32 + getY() + camera.viewportHeight / 2 + 18);*/


        table.draw(batch,alpha);
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

        if (levelScore < MIN_SCORE) {
            levelScore = MIN_SCORE;
        }

        updateText();

    }

    private void updateText(){
        levelScoreLabel.setText("+"+(int)levelScore+"");
        totalScoreLabel.setText("= "+totalScore+" ");
        table.pack();
        table.setPosition(getX() -camera.viewportWidth/2+PADDING_LEFT, getY() + camera.viewportHeight / 2 - table.getHeight());
    }

    @Override
    public void update(Observable o, Object arg) {
        totalScore += levelScore;
        if(totalScore < 0){
            totalScore = 0;
        }
        level += 1;
        levelScore = 1000 * Math.round(Math.pow(1.6, level));
        MIN_SCORE = 0.1f * levelScore;

        if (levelScore < MIN_SCORE) {
            levelScore = MIN_SCORE;
        }
    }

    public void reduceHintPoints() {
        levelScore -= 200 * Math.round(Math.pow(1.6, level));
        if (levelScore < MIN_SCORE) {
            levelScore = MIN_SCORE;
        }
    }

    public boolean canPayHint(){
        return levelScore > MIN_SCORE + 200 * Math.round(Math.pow(1.6, level));
    }
}
