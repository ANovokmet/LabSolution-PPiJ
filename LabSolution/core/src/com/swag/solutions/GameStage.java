package com.swag.solutions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.swag.solutions.Screens.MainMenu;

/**
 * Created by Ante on 11.5.2015..
 */
public class GameStage extends Stage {
    private LabGame game = null;
    private final static Sound clickSound =
            Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));

    public GameStage(LabGame game) {
        super();
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
        clickSound.play();
        if (keycode == Input.Keys.BACK && game != null) {
            game.setScreen(new MainMenu(game));
            return true;
        } else {
            return super.keyDown(keycode);
        }
    }

}
