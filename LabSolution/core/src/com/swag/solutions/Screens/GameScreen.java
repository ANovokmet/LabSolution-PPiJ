package com.swag.solutions.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.input.GestureDetector;
import com.swag.solutions.CameraController;
import com.swag.solutions.GameStage;
import com.swag.solutions.LabGame;
import com.swag.solutions.TransitionCover;
import com.swag.solutions.hud.EndDialog;
import com.swag.solutions.hud.HintButton;
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
    EndDialog endDialog;

    float SCREEN_SCALING;

    TransitionCover transitionActor;

    public GameScreen(LabGame main){
        main_game=main;
        main_game.currentState = LabGame.GameState.PLAYING;

        SCREEN_SCALING = Gdx.graphics.getWidth()/360f;

        gameStage = new GameStage();
        this.camera = (OrthographicCamera) gameStage.getCamera();
        final float screenWidth = Gdx.graphics.getWidth();
        final float screenHeight = Gdx.graphics.getHeight();
        camera.setToOrtho(false, screenWidth, screenHeight);//OVDJE JE DI SE NAMJESTI OMJER, ostali djelovi se prilagođavaju viewportwidth i height

        endDialog = new EndDialog(camera, main_game);
        gameStage.addActor(endDialog);

        professor = new Professor(camera);
        gameStage.addActor(professor);
        hintButton = new HintButton(camera,professor);

        ShakeDetector shakeDetector = new MyShakeDetector();
        EnergyContainer enContainer = new EnergyContainer(5000.f, shakeDetector);
        hudElement = new HudElement(camera, enContainer);
        LevelHandler levelHandler =
                new LevelHandler(enContainer, hudElement, hintButton, main_game);
        reactionArea = new ReactionArea(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(), camera, enContainer, levelHandler);
        solution = new Solution(1000*SCREEN_SCALING, 1000*SCREEN_SCALING, reactionArea, gameStage);
        levelHandler.setSolution(solution);
        levelHandler.loadLevel();



        //score
        score = new Score(camera, shakeDetector);
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

        controller = new CameraController(camera, solution);
        //molekula.addAction(parallel(moveTo(200,0,5),rotateBy(90,5)));
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameStage);
        multiplexer.addProcessor(new GestureDetector(20, 0.5f, 2, 0.15f, controller));
        Gdx.input.setInputProcessor(multiplexer);

        transitionActor = new TransitionCover(camera);
        gameStage.addActor(transitionActor);
    }

    @Override
    public void show() {
        gameStage.getRoot().getColor().a = 0;
        gameStage.getRoot().addAction(fadeIn(0.5f));
        transitionActor.transitionOut();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(10/255.0f, 15/255.0f, 230/255.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Gdx.app.log("GameScreen FPS", (1/delta) + "");
        //renderer.render(actors)

        controller.update();
        gameStage.act(delta);

        drawStage();
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
            hudElement.draw(batch,alpha); //zatočene molekule se crtaju u ovoj metodi
            score.draw(batch,alpha);
            hintButton.draw(batch,alpha);
            professor.draw(batch,alpha);
            endDialog.draw(batch,alpha);

            for(Molecule m : solution.getFreeMolecules()){
                m.draw(batch,1);
            }

            transitionActor.draw(batch,alpha);
            batch.end();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
