package com.swag.solutions.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.JsonValue;
import com.swag.solutions.logic.Score;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by Ante on 13.5.2015..
 */
public class HintButton extends Actor {
    Texture button_red = new Texture(Gdx.files.internal("hint_c.png"),true);
    Texture button_green = new Texture(Gdx.files.internal("hint_z.png"),true);
    Texture button_gray = new Texture(Gdx.files.internal("hint_s.png"),true);
    private float BUTTON_X;
    private float BUTTON_Y;
    private float BUTTON_WIDTH = 72;
    private float BUTTON_HEIGHT = 72;

    Professor professor;
    Score score;

    private float SCREEN_SCALING;

    private LinkedList<String> hints = new LinkedList<String>();
    private String freeHint;
    //private final Sound tellHintSound;


    private OrthographicCamera camera;
    public HintButton(OrthographicCamera camera, final Professor professor, Score score){
        this.camera = camera;
        this.professor = professor;
        this.score = score;
        SCREEN_SCALING = Gdx.graphics.getWidth()/360f;

        setWidth(BUTTON_WIDTH*SCREEN_SCALING);
        setHeight(BUTTON_HEIGHT*SCREEN_SCALING);



        button_red.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        button_green.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        button_gray.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        BUTTON_X = camera.viewportWidth/2-this.getWidth();
        BUTTON_Y = camera.viewportHeight/2-this.getWidth();

        setX(camera.position.x+BUTTON_X);
        setY(camera.position.y+BUTTON_Y);

        //tellHintSound = Gdx.audio.newSound(
                //Gdx.files.internal("sounds/mumbling.ogg"));

        this.addListener(new DragListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                tellHint();
            }
        });

    }

    public void tellHint(){
        if(freeHint!=null){
            professor.tellHint(freeHint);// ovo bi moglo izvuc u posebnu metodu da se izvana moze naplatit hint
            freeHint = null;
        }
        else if(!hints.isEmpty()){
            score.reduceHintPoints();
            professor.tellHint(hints.removeFirst());
        }
    }

    public void loadHints(JsonValue hints, JsonValue freeHint){
        this.freeHint = freeHint.asString();

        this.hints.clear();
        for(int i=0; i<hints.size;i++){
            this.hints.add(hints.get(i).asString());
        }

    }


    @Override
    public void act(float delta){
        super.act(delta);
        setX(camera.position.x+BUTTON_X);
        setY(camera.position.y+BUTTON_Y);
        
    }

    @Override
    public void draw(Batch batch, float alpha){
        if(freeHint!=null) {
            batch.draw(button_green, getX(), getY(), this.getOriginX(), this.getOriginY(), getWidth(),
                    getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0,
                    button_green.getWidth(), button_green.getHeight(), false, false);
        }
        else if(!hints.isEmpty()){
            batch.draw(button_red, getX(), getY(), this.getOriginX(), this.getOriginY(), getWidth(),
                    getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0,
                    button_red.getWidth(), button_red.getHeight(), false, false);
        }
        else {
            batch.draw(button_gray, getX(), getY(), this.getOriginX(), this.getOriginY(), getWidth(),
                    getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0,
                    button_gray.getWidth(), button_gray.getHeight(), false, false);
        }

    }
    
    
    
}


