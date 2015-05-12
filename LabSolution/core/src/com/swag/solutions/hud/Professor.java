package com.swag.solutions.hud;

import com.badlogic.gdx.Gdx;
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
    Texture image = new Texture("professor1x0.png");
    private float PROFESSOR_X = 0;
    private float PROFESSOR_Y = 0;
    private float PROFESSOR_WIDTH = 256;
    private float PROFESSOR_HEIGHT = 512;

    LinkedList<String> hintQueue = new LinkedList<String>();

    private Bubble bubble;
    private final Sound tellHintSound;

    private float HINT_DURATION = 2f;

    private OrthographicCamera camera;
    public Professor (OrthographicCamera camera){
        this.camera = camera;
        setWidth(PROFESSOR_WIDTH);
        setHeight(PROFESSOR_HEIGHT);
        bubble = new Bubble(camera);

        PROFESSOR_X = camera.viewportWidth;
        PROFESSOR_Y = camera.viewportHeight-getHeight();

        tellHintSound = Gdx.audio.newSound(
                Gdx.files.internal("sounds/mumbling.ogg"));

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
        setX(camera.position.x);
        setY(camera.position.y);
        //percentFilled+=0.0005;//test za "animaciju"
        bubble.act(delta);

        if(!hintQueue.isEmpty() && !bubble.isHinting()){
            tellHint(hintQueue.removeFirst());
        }
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(image, PROFESSOR_X+getX()-camera.viewportWidth/2, PROFESSOR_Y+getY()-camera.viewportHeight/2, this.getOriginX(), this.getOriginY(), getWidth(),
                getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0,
                image.getWidth(), image.getHeight(), false, false);


        bubble.draw(batch, alpha);

    }
}
