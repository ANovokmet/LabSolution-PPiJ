package com.swag.solutions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Ante on 11.5.2015..
 */
public class GameStage extends Stage {
    private LabGame game = null;


    public TransitionCover transitionCover;

    public GameStage(LabGame game) {
        super();
        this.transitionCover = new TransitionCover(this.getCamera());
        this.game = game;
    }//izbrisati razred ako se ne implementiraju metode

    @Override
    public void draw(){
        super.draw();
    }

    @Override
    public void act(float delta){
        super.act(delta);
    }

    @Override
    public boolean keyDown(int keycode) {
        /*clickSound.play();
        if ((keycode==Input.Keys.ESCAPE || keycode == Input.Keys.BACK) && game != null) {
            transitionCover.transitionIn(game, game.mainMenu);
            return true;
        } else {
            return super.keyDown(keycode);
        }*/
        return super.keyDown(keycode);
    }

    public boolean disableInput = false;

    @Override
    public Actor hit(float stageX, float stageY, boolean touchable){
        if(disableInput)
            return null;
        else
            return super.hit(stageX, stageY, touchable);
    }

}
