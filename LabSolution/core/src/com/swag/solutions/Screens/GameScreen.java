package com.swag.solutions.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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

    public enum State {
        NOTREADY, PLAYING, PAUSED
    }

    State gameState;


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
    Dialog dialog;

    public GameScreen(LabGame main){
        main_game=main;

        gameState = State.PLAYING;

        SCREEN_SCALING = Gdx.graphics.getWidth()/360f;

        gameStage = new GameStage(main_game);
        this.camera = (OrthographicCamera) gameStage.getCamera();
        final float screenWidth = Gdx.graphics.getWidth();
        final float screenHeight = Gdx.graphics.getHeight();
        camera.setToOrtho(false, screenWidth, screenHeight);//OVDJE JE DI SE NAMJESTI OMJER, ostali djelovi se prilagođavaju viewportwidth i height

        endDialog = new EndDialog(camera, main_game);
        gameStage.addActor(endDialog);

        professor = new Professor(camera);
        gameStage.addActor(professor);


        ShakeDetector shakeDetector = new MyShakeDetector();
        score = new Score(camera, shakeDetector);
        hintButton = new HintButton(camera, professor, score);
        EnergyContainer enContainer = new EnergyContainer(5000.f, shakeDetector, main_game);
        hudElement = new HudElement(camera, enContainer);
        LevelHandler levelHandler =
                new LevelHandler(enContainer, hudElement, hintButton, main_game);
        reactionArea = new ReactionArea(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight(), camera, enContainer, levelHandler);
        solution = new Solution(1000*SCREEN_SCALING, 1000*SCREEN_SCALING, reactionArea, gameStage);
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
        if(gameState == State.PLAYING) {

            controller.update();
            gameStage.act(delta);

            Camera camera = gameStage.getViewport().getCamera();
            camera.update();
        }

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

            if(dialogOpen)
                dialog.draw(batch,alpha);

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
        gameState = State.PAUSED;
        quitGameDialog();
    }

    @Override
    public void resume() {
        gameState = State.PLAYING;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    boolean dialogOpen = false;

    public void quitGameDialog() {
        dialogOpen = true;
        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        FileHandle fontFile = Gdx.files.internal("04B_30__.TTF");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        BitmapFont textFont = generator.generateFont(parameter);
        Label.LabelStyle ls = new Label.LabelStyle(textFont, Color.WHITE);

        skin.add("defaultt",ls);

        Label label = new Label("Zelis li uistinu izaci iz igre moj africko americki prijatelju?", skin, "defaultt");
        label.setWrap(true);

        label.setFontScale(1f);
        label.setAlignment(Align.center);


        dialog =
                new Dialog("", skin, "default") {
                    protected void result (Object shouldQuit) {
                        System.out.println("Chosen: " + shouldQuit);
                        dialogOpen = false;
                        if((Boolean)shouldQuit){
                            main_game.setScreen(new MainMenu(main_game));
                        }
                        else {
                            gameState = State.PLAYING;
                        }

                    }
                };

        dialog.padTop(50).padBottom(50);
        dialog.getContentTable().add(label).width(camera.viewportWidth*2/3).row();
        dialog.getButtonTable().padTop(50);
        TextButton dbutton = new TextButton("Yes", skin, "default");
        dialog.button(dbutton, true);

        dbutton = new TextButton("Resume", skin, "default");
        dialog.button(dbutton, false);
        dialog.key(Input.Keys.ENTER, true).key(Input.Keys.ESCAPE, false);
        dialog.invalidateHierarchy();
        dialog.invalidate();
        dialog.layout();
        dialog.setModal(true);
        dialog.pack();
        gameStage.addActor(dialog);

        dialog.setPosition(camera.position.x-dialog.getWidth()/2,camera.position.y-dialog.getHeight()/2);
        //dialog.show(dialogStage);
    }
}
