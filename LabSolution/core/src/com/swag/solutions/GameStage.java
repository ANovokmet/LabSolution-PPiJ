package com.swag.solutions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.swag.solutions.screens.MainMenu;

/**
 * Created by Ante on 11.5.2015..
 */
public class GameStage extends Stage {
    private LabGame game = null;
    private final static Sound clickSound =
            Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));

    public TransitionCover transitionCover;

    public GameStage(LabGame game) {
        super();

        this.game = game;
    }//izbrisati razred ako se ne implementiraju metode

    public void initTransition(){
        this.transitionCover = new TransitionCover(this.getCamera());
        this.addActor(transitionCover);
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
        clickSound.play();
        if (keycode == Input.Keys.BACK && game != null) {
            transitionCover.transitionIn(game, game.mainMenu);
            return true;
        } else {
            return super.keyDown(keycode);
        }
    }

}
