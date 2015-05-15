package com.swag.solutions.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swag.solutions.GameStage;
import com.swag.solutions.LabGame;
import com.swag.solutions.TransitionCover;

/**
 * Created by Ante on 13.5.2015..
 */
public class EndScreen implements Screen {

    private final OrthographicCamera camera;
    LabGame game;
    GameStage stage;
    Batch batch;
    Texture background = new Texture(Gdx.files.internal("background2.png"),true);

    private Sprite pozadinaSprite;
    private final static Sound gameFinishedSound = Gdx.audio.newSound(
            Gdx.files.internal("sounds/game_finished.wav"));
    private final static Sound gameOverSound = Gdx.audio.newSound(
            Gdx.files.internal("sounds/game_over.wav"));

    public EndScreen(LabGame game, boolean gameFinished) {
        this.game = game;
        stage = new GameStage(game);
        this.camera = (OrthographicCamera) stage.getCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
        if (gameFinished) {
            gameFinishedSound.play();
        } else {
            gameOverSound.play();
        }
        stage.initTransition();
    }

    @Override
    public void show() {
        stage.transitionCover.transitionOut();
    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background, 0, 0);
        batch.end();
        stage.act(delta);
        stage.draw();
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
