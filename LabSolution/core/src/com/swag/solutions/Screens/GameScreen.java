package com.swag.solutions.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.input.GestureDetector;
import com.swag.solutions.CameraController;
import com.swag.solutions.GameStage;
import com.swag.solutions.LabGame;
import com.swag.solutions.hud.EndDialog;
import com.swag.solutions.logic.EnergyContainer;
import com.swag.solutions.hud.HudElement;
import com.swag.solutions.logic.Molecule;
import com.swag.solutions.hud.Professor;
import com.swag.solutions.logic.ReactionArea;
import com.swag.solutions.logic.Solution;
import com.swag.solutions.input.MyShakeDetector;
import com.swag.solutions.input.ShakeDetector;
import com.swag.solutions.logic.LevelHandler;

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

    EndDialog endDialog;

    public GameScreen(LabGame main){
        main_game=main;
        main_game.currentState = LabGame.GameState.PLAYING;

        gameStage = new GameStage();
        this.camera = (OrthographicCamera) gameStage.getCamera();
        final float screenWidth = Gdx.graphics.getWidth();
        final float screenHeight = Gdx.graphics.getHeight();
        camera.setToOrtho(false, screenWidth, screenHeight);//OVDJE JE DI SE NAMJESTI OMJER, ostali djelovi se prilagođavaju viewportwidth i height

        endDialog = new EndDialog(camera, main_game);
        gameStage.addActor(endDialog);

        professor = new Professor(camera);
        gameStage.addActor(professor);
        professor.tellHint("yole");
        professor.tellHint("yole");

        ShakeDetector shakeDetector = new MyShakeDetector();
        EnergyContainer enContainer = new EnergyContainer(5000.f, shakeDetector);
        hudElement = new HudElement(camera, enContainer);
        LevelHandler levelHandler =
                new LevelHandler(enContainer, hudElement);
        reactionArea = new ReactionArea(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(), camera, enContainer, levelHandler);
        solution = new Solution(1000, 1000, reactionArea, gameStage);
        levelHandler.setSolution(solution);
        levelHandler.loadLevel();

        gameStage.addActor(shakeDetector);
        gameStage.addActor(enContainer);
        gameStage.addActor(reactionArea);
        gameStage.addActor(hudElement);
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
    }

    @Override
    public void show() {

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
        if (batch != null) {
            batch.setProjectionMatrix(camera.combined);
            batch.begin();

            solution.draw(batch,1);

            reactionArea.draw(batch,1);
            hudElement.draw(batch,1); //zatočene molekule se crtaju u ovoj metodi
            professor.draw(batch, 1);
            endDialog.draw(batch,1);

            for(Molecule m : solution.getFreeMolecules()){
                m.draw(batch,1);
            }
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
