package com.swag.solutions.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.swag.solutions.CameraController;
import com.swag.solutions.Objects.EnergyContainer;
import com.swag.solutions.Objects.HudElement;
import com.swag.solutions.Objects.Molecule;
import com.swag.solutions.Objects.ReactionArea;
import com.swag.solutions.World;
import com.swag.solutions.input.BadShakeDetector;
import com.swag.solutions.input.MyShakeDetector;
import com.swag.solutions.input.ShakeDetector;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;

/**
 * Created by Ante on 15.4.2015..
 */
public class GameScreen implements Screen {

    Game main_game;  //referenca na igru zbog mijenjanja screenova
    Stage gameStage;
    //HudElement hud; //zašto je ovdje i dodan u gameStage u isto vrijeme?
    CameraController controller;
    OrthographicCamera camera;

    public GameScreen(Game main){

        main_game=main;

        final float screenWidth = Gdx.graphics.getWidth();
        final float screenHeight = Gdx.graphics.getHeight();

        gameStage = new Stage();
        this.camera = (OrthographicCamera) gameStage.getCamera();

        ShakeDetector shakeDetector = new MyShakeDetector();
        gameStage.addActor(shakeDetector);

        EnergyContainer enContainer =
                new EnergyContainer(0.8f, 1.f, 0.05f, shakeDetector);
        gameStage.addActor(enContainer);

        HudElement hud = new HudElement(camera, enContainer);
        gameStage.addActor(hud);

        ReactionArea rxnArea = new ReactionArea(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        World world = new World(1000,1000,rxnArea);

        world.generateMolecules("");
        gameStage.addActor(rxnArea);

        gameStage.addActor(world);
        for(Molecule a : world.getFreeMolecules()){
            gameStage.addActor(a);
        }

        controller = new CameraController(camera, world);
        //molekula.addAction(parallel(moveTo(200,0,5),rotateBy(90,5)));
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameStage);
        multiplexer.addProcessor(new GestureDetector(20, 0.5f, 2, 0.15f, controller));
        Gdx.input.setInputProcessor(multiplexer);

        //Gdx.input.setInputProcessor(gameStage);

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
        //hud.act(delta); //zašto je ovo potrebno?
        //hud.setPercentFilled(shakeDetector.deviceBeingShaken() ? 20.f : 0.f);

        gameStage.draw();
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
