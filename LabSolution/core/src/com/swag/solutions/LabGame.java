package com.swag.solutions;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.swag.solutions.screens.MainMenu;

/**
 * Created by Ante on 15.4.2015..
 */
public class LabGame extends Game {

    public enum GameState {
        MENU, PLAYING, ENDGAME
    }
    public GameState currentState;//ne koristi se, izbrisati
    public MainMenu mainMenu;
    @Override
    public void create() {
        mainMenu = new MainMenu(this);
        setScreen(mainMenu);
    }
}
