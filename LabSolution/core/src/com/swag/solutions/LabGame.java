package com.swag.solutions;

import com.badlogic.gdx.Game;
import com.swag.solutions.Screens.MainMenu;

/**
 * Created by Ante on 15.4.2015..
 */
public class LabGame extends Game {

    public enum GameState {
        MENU, PLAYING, ENDGAME
    }
    public GameState currentState;


    @Override
    public void create() {
        setScreen(new MainMenu(this));
    }
}
