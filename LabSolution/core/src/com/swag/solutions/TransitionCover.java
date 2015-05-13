package com.swag.solutions;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sizeTo;

/**
 * Created by Ante on 13.5.2015..
 */
public class TransitionCover extends Actor {

    private Texture texture = new Texture(Gdx.files.internal("black-pixel.png"),true);

    Action nextScreen;
    Camera camera;
    LabGame game;
    Screen screen;

    public TransitionCover(Camera camera){

        this.setX(0);//-camera.viewportHeight/2);
        this.setY(0);//-camera.viewportWidth/2);
        this.setWidth(0);
        this.setHeight(Gdx.graphics.getHeight());

        this.camera = camera;
        nextScreen = new Action() {
            @Override
            public boolean act(float delta){
                game.setScreen(screen);
                return true;
            }
        };

    }

    @Override
    public void draw(Batch batch, float alpha){

        batch.draw(texture, getX(), getY(), this.getOriginX(), this.getOriginY(), this.getWidth(),
                this.getHeight(), this.getScaleX(), this.getScaleY(), 0, 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
    }

    @Override
    public void act(float delta){
        super.act(delta);
        setX(camera.position.x-camera.viewportWidth/2);
        setY(camera.position.y-camera.viewportHeight/2);

    }

    public void transitionIn(LabGame g, Screen s){
        //g.setScreen(s);
        this.game = g;
        this.screen = s;
        this.addAction(sequence(sizeTo(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0.5f), nextScreen));
    }

    public void transitionOut(){
        //g.setScreen(s);
        this.setHeight(Gdx.graphics.getHeight());
        this.setWidth(Gdx.graphics.getWidth());
        this.addAction(sequence(sizeTo(0, Gdx.graphics.getHeight(), 0.5f)));
    }
}
