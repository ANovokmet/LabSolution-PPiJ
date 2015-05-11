package com.swag.solutions.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.swag.solutions.CameraController;
import com.swag.solutions.GameStage;
import com.swag.solutions.LabGame;
import com.swag.solutions.Objects.EndDialog;
import com.swag.solutions.Objects.EnergyContainer;
import com.swag.solutions.Objects.HudElement;
import com.swag.solutions.Objects.Molecule;
import com.swag.solutions.Objects.ReactionArea;
import com.swag.solutions.World;
import com.swag.solutions.input.BadShakeDetector;
import com.swag.solutions.input.MyShakeDetector;
import com.swag.solutions.input.ShakeDetector;

import java.util.ArrayList;
import java.util.HashMap;

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
    World world;
    EnergyContainer enContainer;
    ShakeDetector shakeDetector;

    HashMap<Integer,Integer> targetReaction;
    private final Sound reactionSuccessSound;

    EndDialog endDialog;

    public GameScreen(LabGame main){

        main_game=main;

        main_game.currentState = LabGame.GameState.PLAYING;

        final float screenWidth = Gdx.graphics.getWidth();
        final float screenHeight = Gdx.graphics.getHeight();

        gameStage = new GameStage();
        this.camera = (OrthographicCamera) gameStage.getCamera();

        camera.setToOrtho(false, screenWidth,screenHeight);//OVDJE JE DI SE NAMJESTI OMJER, ostali djelovi se prilagođavaju viewportwidth i height

        shakeDetector = new MyShakeDetector();
        gameStage.addActor(shakeDetector);

        enContainer =
                new EnergyContainer(1000.f, shakeDetector);
        gameStage.addActor(enContainer);

        endDialog = new EndDialog(camera, main_game);
        gameStage.addActor(endDialog);

        reactionArea = new ReactionArea(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);

        world = new World(1000,1000,reactionArea);

        world.generateMolecules("");
        gameStage.addActor(reactionArea);

        hudElement = new HudElement(camera, enContainer);
        gameStage.addActor(hudElement);

        gameStage.addActor(world);
        for (Molecule m: world.getFreeMolecules()){
            gameStage.addActor(m);
        }

        controller = new CameraController(camera, world);
        //molekula.addAction(parallel(moveTo(200,0,5),rotateBy(90,5)));
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(gameStage);
        multiplexer.addProcessor(new GestureDetector(20, 0.5f, 2, 0.15f, controller));
        Gdx.input.setInputProcessor(multiplexer);

        //Gdx.input.setInputProcessor(gameStage);

        loadJson();

        reactionSuccessSound = Gdx.audio.newSound(
                               Gdx.files.internal("sounds/reaction_success.wav"));
    }

    private void loadJson(){
        FileHandle file = Gdx.files.internal("data/levels.json");
        JsonValue levels = new JsonReader().parse(file.readString());
        JsonValue prvi = levels.get(0);

        JsonValue reactants = prvi.get("reactants");
        int len = levels.size;
        targetReaction = new HashMap<Integer,Integer>();

        for (int i = 0; i < len+1; i++)
        {
            JsonValue reactant = reactants.get(i);
            targetReaction.put(reactant.get("id").asInt(),reactant.get("quantity").asInt());
        }

        float neededEnergy = prvi.get("energy_needed").asFloat();
        enContainer.setNeededEnergy(neededEnergy);
        //hudElement.setTargetEnergy(targetEnergy);

        JsonValue result =  prvi.get("result");
        int idResult = result.get("id").asInt();

        FileHandle file2 = Gdx.files.internal("data/all.json");
        JsonValue molecules = new JsonReader().parse(file2.readString());
        JsonValue molecule = molecules.get(idResult);
        hudElement.setMoleculeTitle(molecule.get("formula").asString());
    }

    public boolean isReactionFulfilled(){
        HashMap<Integer,Integer> h = new HashMap<Integer,Integer>();
        Array<Molecule> molecules = reactionArea.getReactionMolecules();


        for(Molecule m : molecules){
            int id = m.getId();
            if(h.containsKey(id)){
                h.put(id,h.get(id)+1);
            }
            else{
                h.put(id,1);
            }

        }
        /*Gdx.app.log("ReactionArea",  ""); //test ispis hashmapi
        for(Integer key : h.keySet()){
            Gdx.app.log("m", key+":"+h.get(key));
        }
        Gdx.app.log("Target", "");
        for(Integer key : targetReaction.keySet()){
            Gdx.app.log("m", key+":"+targetReaction.get(key));
        }*/
        return h.equals(targetReaction);
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

        //if(enContainer.getCurrentEnergy() >= targetEnergy) {
        if (enContainer.enoughEnergyForReaction()) {
            if (isReactionFulfilled()) {
                reactionSuccessSound.play();
                main_game.currentState = LabGame.GameState.ENDGAME;
                //main_game.setScreen(new MainMenu(main_game));
            }
        }
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

            reactionArea.draw(batch,1);
            hudElement.draw(batch,1); //zatočene molekule se crtaju u ovoj metodi

            world.draw(batch,1);
            for(Molecule m : world.getFreeMolecules()){
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
