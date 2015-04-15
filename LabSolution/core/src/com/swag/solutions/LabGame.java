package com.swag.solutions;

import com.badlogic.gdx.Game;
import com.swag.solutions.Screens.GameScreen;

/**
 * Created by Ante on 15.4.2015..
 */
public class LabGame extends Game {
    @Override
    public void create() {
        setScreen(new GameScreen());
    }
}
