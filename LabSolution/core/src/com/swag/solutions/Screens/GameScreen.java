package com.swag.solutions.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.swag.solutions.CameraController;
import com.swag.solutions.Objects.Molecule;
import com.swag.solutions.Objects.ReactionArea;
import com.swag.solutions.World;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateBy;

/**
 * Created by Ante on 15.4.2015..
 */
public class GameScreen implements Screen {

    Stage gameStage;
    CameraController controller;
    public GameScreen(){



        final float screenWidth = Gdx.graphics.getWidth();
        final float screenHeight = Gdx.graphics.getHeight();

        gameStage = new Stage();
        //gameStage.setViewport(new StretchViewport(640, 1024));
        //gameStage.setViewport(new FitViewport(screenWidth, screenHeight));


        final OrthographicCamera camera = (OrthographicCamera) gameStage.getCamera();
        /*gameStage.addListener(new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                if(!event.isHandled()){
                    //camera.translate((camera.position.x-x)/10, (camera.position.y-y)/10);   //popraviti pomicanje kamere

                    camera.position.set((camera.position.x-x)+screenWidth/2, (camera.position.y-y)+screenHeight/2, camera.position.z);
                    camera.update();
                }
            }
        });*/


        final Molecule molekula1 = new Molecule(250,250);
        Molecule molekula2 = new Molecule(400,400);

        Array<Molecule> a = new Array<Molecule>();
        a.add(molekula1);
        a.add(molekula2);

        ReactionArea b = new ReactionArea(a, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        World world = new World(1000,1000,a,b);
        gameStage.addActor(b);
        gameStage.addActor(molekula1);
        gameStage.addActor(molekula2);
        gameStage.addActor(world);

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
