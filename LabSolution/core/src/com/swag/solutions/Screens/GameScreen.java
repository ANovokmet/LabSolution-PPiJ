package com.swag.solutions.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.swag.solutions.CameraController;
import com.swag.solutions.GameStage;
import com.swag.solutions.LabGame;
import com.swag.solutions.hud.CountDown;
import com.swag.solutions.hud.HintButton;
import com.swag.solutions.hud.QuitDialog;
import com.swag.solutions.logic.EnergyContainer;
import com.swag.solutions.hud.HudElement;
import com.swag.solutions.logic.Molecule;
import com.swag.solutions.hud.Professor;
import com.swag.solutions.logic.ReactionArea;
import com.swag.solutions.logic.Score;
import com.swag.solutions.logic.Solution;
import com.swag.solutions.input.MyShakeDetector;
import com.swag.solutions.input.ShakeDetector;
import com.swag.solutions.logic.LevelHandler;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;

/**
 * Created by Ante on 15.4.2015..
 */
public class GameScreen implements Screen {

    public enum State {
        COUNTDOWN, PLAYING, PAUSED, GAMEOVER
    }

    public State gameState;


    LabGame main_game;  //referenca na igru zbog mijenjanja screenova
    GameStage gameStage;
    CameraController controller;
    OrthographicCamera camera;
    HudElement hudElement;
    ReactionArea reactionArea;
    Solution solution;
    Professor professor;
    Score score;
    HintButton hintButton;
    QuitDialog quitDialog;
    CountDown countDown;

    float SCREEN_SCALING;


    public GameScreen(LabGame main){
        main_game=main;
    }

    public void create(){

        gameState = State.PLAYING;

        SCREEN_SCALING = Gdx.graphics.getWidth()/360f;

        gameStage = new GameStage(main_game);
        this.camera = (OrthographicCamera) gameStage.getCamera();
        final float screenWidth = Gdx.graphics.getWidth();
        final float screenHeight = Gdx.graphics.getHeight();
        camera.setToOrtho(false, screenWidth, screenHeight);//OVDJE JE DI SE NAMJESTI OMJER, ostali djelovi se prilagođavaju viewportwidth i height


        professor = new Professor(camera, main_game.assetManager);
        gameStage.addActor(professor);


        ShakeDetector shakeDetector = new MyShakeDetector();
        score = new Score(camera, shakeDetector, main_game.assetManager);
        hintButton = new HintButton(camera, professor, score, main_game.assetManager);
        EnergyContainer enContainer = new EnergyContainer(5000.f, shakeDetector, this);
        hudElement = new HudElement(camera, enContainer, main_game.assetManager);
        LevelHandler levelHandler =
                new LevelHandler(enContainer, hudElement, hintButton, this);
        reactionArea = new ReactionArea(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(), camera, enContainer, levelHandler, main_game.assetManager);
        solution = new Solution(1000*SCREEN_SCALING, 1000*SCREEN_SCALING, reactionArea, gameStage, main_game.assetManager);
        levelHandler.setSolution(solution);
        levelHandler.loadLevel();
        levelHandler.addObserver(score);

        gameStage.addActor(score);
        gameStage.addActor(shakeDetector);
        gameStage.addActor(enContainer);
        gameStage.addActor(reactionArea);
        gameStage.addActor(hudElement);
        gameStage.addActor(hintButton);
        gameStage.addActor(solution);
        for (Molecule m: solution.getFreeMolecules()){
            gameStage.addActor(m);
        }

        quitDialog = new QuitDialog(camera, this, main_game.assetManager);
        gameStage.addActor(quitDialog);



        controller = new CameraController(camera, solution);
        //molekula.addAction(parallel(moveTo(200,0,5),rotateBy(90,5)));
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameStage);
        multiplexer.addProcessor(new GestureDetector(20, 0.5f, 2, 0.15f, controller));

        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {

                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) )
                    if(quitDialog.isVisible())
                        gameOver(false);
                    else if(gameState != State.COUNTDOWN)
                        quitDialog.showDialog();
                return false;
            }
        };
        multiplexer.addProcessor(backProcessor);

        Gdx.input.setInputProcessor(multiplexer);

        countDown = new CountDown(camera, this, main_game.assetManager);
    }

    @Override
    public void show() {
        create();
        Gdx.input.setCatchBackKey(true);
        gameStage.transitionCover.transitionOut();

        countDown.startCountdown();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(10/255.0f, 15/255.0f, 230/255.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.app.log("GameScreen FPS", (1/delta) + "");
        //renderer.render(actors)
        if(gameState == State.PLAYING || gameState == State.GAMEOVER) {//igra tece i aktori se updateaju
            if(gameStage.disableInput){//ukljucivanje inputa
                gameStage.disableInput = false;
                controller.disableCamera(false);
            }
            controller.update();
            gameStage.act(delta);
        }

        if(gameState == State.COUNTDOWN){//updatea se samo countdown
            if(!gameStage.disableInput){//iskljucivanje inputa
                gameStage.disableInput = true;
                controller.disableCamera(true);
            }
            countDown.act(delta);
        }
        gameStage.transitionCover.act(delta);//transitioncover u svakom slucaju se mora
        drawStage();
    }

    public void gameOver(boolean gameFinished){
        if(gameState!=State.GAMEOVER){
            gameState = State.GAMEOVER;
            gameStage.transitionCover.transitionIn(main_game,new EndScreen(main_game,gameFinished));
        }
    }

    public void drawStage(){//omogućava redoslijed crtanja
        Camera camera = gameStage.getViewport().getCamera();
        camera.update();

        if (!gameStage.getRoot().isVisible()) return;

        Batch batch = gameStage.getBatch();
        float alpha = gameStage.getBatch().getColor().a;
        if (batch != null) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            solution.draw(batch,alpha);

            reactionArea.draw(batch,alpha);

            for(Molecule m : solution.getFreeMolecules()){
                if(!m.isMoved())
                    m.draw(batch,1);
            }

            hudElement.draw(batch,alpha); //zatočene molekule se crtaju u ovoj metodi
            score.draw(batch,alpha);
            hintButton.draw(batch,alpha);
            professor.draw(batch,alpha);

            for(Molecule m : solution.getFreeMolecules()){
                if(m.isMoved())
                    m.draw(batch,1);
            }

            countDown.draw(batch,alpha);

            if(quitDialog.isVisible()){
                quitDialog.draw(batch,alpha);
            }

            gameStage.transitionCover.draw(batch, alpha);
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        gameState = State.PAUSED;
    }

    @Override
    public void resume() {
        countDown.startCountdown();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
