package com.swag.solutions;

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
    }

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
