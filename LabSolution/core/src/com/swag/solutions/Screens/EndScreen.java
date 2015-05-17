package com.swag.solutions.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.swag.solutions.GameStage;
import com.swag.solutions.LabGame;
import com.swag.solutions.logic.Score;

/**
 * Created by Ante on 13.5.2015..
 */
public class EndScreen implements Screen {

    private final OrthographicCamera camera;
    LabGame game;
    GameStage stage;
    Batch batch;
    TextureRegion background;
    Table table;
    InputMultiplexer inputMultiplexer;
    Score score;
    Label label;
    AssetManager manager;

    private static Sound gameFinishedSound;
    private static Sound gameOverSound;

    float BUTTONPADDING;

    public EndScreen(final LabGame game, boolean gameFinished, Score score) {
        this.game = game;
        this.score = score;
        this.manager = game.assetManager;


        stage = new GameStage(game);
        this.camera = (OrthographicCamera) stage.getCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        BUTTONPADDING = 10 * camera.viewportWidth / 480f;

        gameFinishedSound = this.game.assetManager.get("sounds/game_finished.wav", Sound.class);
        gameOverSound = this.game.assetManager.get("sounds/game_over.wav", Sound.class);
        //background = this.game.assetManager.get("blackboard.png", Texture.class);

        InputProcessor backProcessor = new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK) )
                    stage.transitionCover.transitionIn(game, game.mainMenu);
                return false;
            }
        };
        inputMultiplexer = new InputMultiplexer();

        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(backProcessor);

        batch = new SpriteBatch();
        if (gameFinished) {
            gameFinishedSound.play();
        } else {
            gameOverSound.play();
        }



        TextureAtlas textureAtlas = manager.get("data/labui.pack", TextureAtlas.class);
        Skin skin2 = new Skin(textureAtlas);

        background = skin2.getRegion("background");

        Skin skin = new Skin();
        BitmapFont textFont = manager.get("scorefont.ttf", BitmapFont.class);
        Label.LabelStyle ls = new Label.LabelStyle(textFont, Color.DARK_GRAY);
        skin.add("score_font",ls);

        BitmapFont subFont = manager.get("quitfont.ttf", BitmapFont.class);
        Label.LabelStyle ss = new Label.LabelStyle(subFont, Color.DARK_GRAY);
        skin.add("number_font",ss);

        table = new Table();

        Table table1 = new Table();
        Label label = new Label("Total score:", skin, "score_font");
        label.setAlignment(Align.left);
        table1.add(label);

        Label label1 = new Label(score.totalScore+"", skin, "number_font");
        label1.setAlignment(Align.right);
        table1.add(label1).row();


        Table table2 = new Table();
        Label label2 = new Label("Levels solved:", skin, "score_font");
        label2.setAlignment(Align.left);
        table2.add(label2);

        Label label3 = new Label(score.level+"", skin, "number_font");
        label3.setAlignment(Align.right);
        table2.add(label3).row();
        /*levelScoreLabel = new Label((int)levelScore+"", skin, "level_score_font");
        levelScoreLabel.setAlignment(Align.bottomLeft);
        table.add(levelScoreLabel);*/

        //table1.setBackground(skin2.getDrawable("blue_panel"));

        table1.setBackground(skin2.getDrawable("panel_top"));
        table2.setBackground(skin2.getDrawable("panel_mid"));

        table.add(table1).row();
        table.add(table2).row();


        //Skin skin2 = new Skin(Gdx.files.internal("data/uiskin.json"));


        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = textFont;
        style.up = skin2.getDrawable("blue_button02");
        style.down = skin2.getDrawable("blue_button03");
        style.pressedOffsetY = -4;
        TextButton dbutton = new TextButton("Retry", style);
        dbutton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                stage.transitionCover.transitionIn(game, game.gameScreen);
            }
        });

        TextButton dbutton2 = new TextButton("Menu", style);
        dbutton2.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                stage.transitionCover.transitionIn(game, game.mainMenu);
            }
        });
        Table table3 = new Table();
        table3.add(dbutton).pad(0,BUTTONPADDING,0,BUTTONPADDING).width(camera.viewportWidth / 2 - BUTTONPADDING*2);
        table3.add(dbutton2).pad(0,BUTTONPADDING,0,BUTTONPADDING).width(camera.viewportWidth / 2 - BUTTONPADDING*2);
        table3.setBackground(skin2.getDrawable("panel_bot"));
        table.add(table3);
        table.pack();

        table.setPosition(camera.viewportWidth/2-table.getWidth()/2, camera.viewportHeight/2-table.getHeight()/2);



        stage.addActor(table);



        stage.addActor(stage.transitionCover);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputMultiplexer);
        Gdx.input.setCatchBackKey(true);
        stage.transitionCover.transitionOut();
        LabGame.googleServices.submitScore(Score.totalScore);
        if(Score.totalScore>=1000000){
            LabGame.googleServices.unlockAchievement("CgkIrYPb-McCEAIQBg"); //millionaire achievement
        }



    }

    @Override
    public void render(float delta) {
        batch.begin();
        batch.draw(background,0,0, camera.viewportWidth,camera.viewportHeight);
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
