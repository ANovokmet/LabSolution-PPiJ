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

import java.util.ArrayList;
import java.util.LinkedList;

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
    private float BUTTON_WIDTH = 128;
    private float BUTTON_HEIGHT = 128;

    Professor professor;


    private LinkedList<String> hints = new LinkedList<String>();
    private String freeHint;
    //private final Sound tellHintSound;


    private OrthographicCamera camera;
    public HintButton (OrthographicCamera camera, final Professor professor){
        this.camera = camera;
        this.professor = professor;
        setWidth(BUTTON_WIDTH);
        setHeight(BUTTON_HEIGHT);

        BUTTON_X = camera.viewportWidth/2-BUTTON_WIDTH;
        BUTTON_Y = camera.viewportHeight/2-BUTTON_HEIGHT;

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
        if(!hints.isEmpty()){
            professor.tellHint(hints.removeFirst());
        }
    }

    public void loadHints(JsonValue hints, JsonValue freeHint){
        this.freeHint = freeHint.asString();

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
        batch.draw(button_green, getX(), getY(), this.getOriginX(), this.getOriginY(), getWidth(),
                getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0,
                button_green.getWidth(), button_green.getHeight(), false, false);

    }
    
    
    
}


