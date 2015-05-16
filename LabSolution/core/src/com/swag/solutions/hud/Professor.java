package com.swag.solutions.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.LinkedList;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;

/**
 * Created by Ante on 6.5.2015..
 */
public class Professor extends Actor {
    Texture image;
    private float PROFESSOR_X;
    private float PROFESSOR_Y;
    private float PROFESSOR_WIDTH = 256;
    private float PROFESSOR_HEIGHT = 512;
    private float SCREEN_SCALING;


    LinkedList<String> hintQueue = new LinkedList<String>();

    private Bubble bubble;
    private final Sound tellHintSound;

    private float HINT_DURATION = 2f;

    private OrthographicCamera camera;
    public Professor (OrthographicCamera camera, AssetManager manager){
        SCREEN_SCALING = Gdx.graphics.getWidth()/480f; //malo ipak veci ;)

        this.image = manager.get("professor1x0.png", Texture.class);
        this.camera = camera;
        setWidth(PROFESSOR_WIDTH*SCREEN_SCALING);
        setHeight(PROFESSOR_HEIGHT*SCREEN_SCALING);
        bubble = new Bubble(camera, manager);

        PROFESSOR_X = camera.viewportWidth/2;
        PROFESSOR_Y = camera.viewportHeight/2-this.getHeight();


        setX(camera.position.x+PROFESSOR_X);
        setY(camera.position.y+PROFESSOR_Y);

        tellHintSound = manager.get("sounds/mumbling.ogg", Sound.class);

        image.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

    }

    public void tellHint(String s){
        if(!bubble.isHinting()){
            tellHintSound.play();
            bubble.hinting = true;
            this.addAction(sequence(Actions.rotateTo(30f, 2f), delay(HINT_DURATION), Actions.rotateTo(0f, 0.5f)));
            bubble.setHintText(s);
            bubble.addAction(sequence(fadeIn(2f), delay(HINT_DURATION), fadeOut(0.5f)));
        }
        else{
            hintQueue.add(s);
        }
    }

    @Override
    public void act(float delta){
        super.act(delta);
        setX(camera.position.x+PROFESSOR_X);
        setY(camera.position.y+PROFESSOR_Y);

        bubble.act(delta);

        if(!hintQueue.isEmpty() && !bubble.isHinting()){
            tellHint(hintQueue.removeFirst());
        }
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(image, getX(), getY(), this.getOriginX(), this.getOriginY(), getWidth(),
                getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0,
                image.getWidth(), image.getHeight(), false, false);


        bubble.draw(batch, alpha);

    }
}
