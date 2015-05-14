package com.swag.solutions;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.swag.solutions.Screens.MainMenu;

/**
 * Created by Ante on 15.4.2015..
 */
public class LabGame extends Game {

    public enum GameState {
        MENU, PLAYING, ENDGAME
    }
    public GameState currentState;//ne koristi se, izbrisati

    @Override
    public void create() {

        setScreen(new MainMenu(this));
    }
}
