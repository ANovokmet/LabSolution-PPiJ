package com.swag.solutions;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.swag.solutions.screens.GameScreen;
import com.swag.solutions.screens.LoadingScreen;
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
    public GameScreen gameScreen;
    public static AbstractGoogleServices googleServices;

    public AssetManager assetManager = new AssetManager();

    public LabGame(AbstractGoogleServices googleServices)
    {
        super();
        LabGame.googleServices = googleServices;
    }

    @Override
    public void create() {
        mainMenu = new MainMenu(this);
        gameScreen = new GameScreen(this);
        setScreen(new LoadingScreen(this));
    }
}
