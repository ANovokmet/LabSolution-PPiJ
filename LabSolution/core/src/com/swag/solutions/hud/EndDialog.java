package com.swag.solutions.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.swag.solutions.LabGame;
import com.swag.solutions.screens.MainMenu;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;

/**
 * Created by Ante on 11.5.2015..
 */
public class EndDialog extends Actor {



    private static final int        FRAME_COLS = 5;         // #1
    private static final int        FRAME_ROWS = 4;         // #2

    Animation expAnimation;          // #3
    Texture explosionSheet;              // #4
    TextureRegion[]                 explosionFrames;
    TextureRegion                   currentFrame;           // #7
    float stateTime;

    LabGame main_game;
    OrthographicCamera camera;

    public EndDialog(OrthographicCamera camera, final LabGame main_game){
        this.camera=camera;
        this.main_game = main_game;




        explosionSheet = new Texture(Gdx.files.internal("explosion.png")); // #9
        TextureRegion[][] tmp = TextureRegion.split(explosionSheet, 960/FRAME_COLS, 768/FRAME_ROWS);              // #10
        explosionFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                explosionFrames[index++] = tmp[i][j];
            }
        }
        expAnimation = new Animation(0.05f, explosionFrames);                // #12
        stateTime = 0f;

        setWidth(960/FRAME_COLS);
        setHeight(768/FRAME_ROWS);

        this.addListener(new ClickListener(){
            public void touchUp(InputEvent event,float x,float y,int pointer,int button){
                changeLevel();
            }
        });
    }

    private void changeLevel(){
        if(main_game.currentState == LabGame.GameState.ENDGAME)
            main_game.setScreen(new MainMenu(main_game));
    }

    @Override
    public void draw(Batch batch, float alpha){
        if(main_game.currentState == LabGame.GameState.ENDGAME) {
            stateTime += Gdx.graphics.getDeltaTime();           // #15
            currentFrame = expAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, getX(), getY());
        }
    }


    @Override
    public void act(float delta){
        if(main_game.currentState == LabGame.GameState.ENDGAME) {
            setX(camera.position.x);
            setY(camera.position.y);
        }
    }

}
