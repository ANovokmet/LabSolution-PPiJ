package com.swag.solutions;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.swag.solutions.screens.CreditScreen;
import com.swag.solutions.screens.GameScreen;
import com.swag.solutions.screens.LoadingScreen;
import com.swag.solutions.screens.MainMenu;
import com.swag.solutions.screens.TutorialScreen;

/**
 * Created by Ante on 15.4.2015..
 */
public class LabGame extends Game {

    public MainMenu mainMenu;
    public GameScreen gameScreen;
    public TutorialScreen tutorialScreen;
    public CreditScreen creditScreen;

    public static AbstractGoogleServices googleServices;

    public AssetManager assetManager = new AssetManager();

    public LabGame(AbstractGoogleServices googleServices)
    {
        super();
        LabGame.googleServices = googleServices;
    }

    @Override
    public void create() {
        tutorialScreen = new TutorialScreen(this);
        mainMenu = new MainMenu(this);
        gameScreen = new GameScreen(this);
        creditScreen = new CreditScreen(this);

        setScreen(new LoadingScreen(this));
    }
}
