package com.swag.solutions.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.swag.solutions.LabGame;
import com.swag.solutions.TransitionCover;


/**
 * Created by Mate on 6.5.2015..
 */
public class MainMenu implements com.badlogic.gdx.Screen {

    private LabGame game;
    private Skin skin;
    private Stage stage;
    private SpriteBatch batch;
    private Texture foreground = new Texture("chemistrySet.png");
    private TextureRegion title;
    private TextureRegion background;
    private OrthographicCamera camera;

    float TITLE_Y;
    float TITLE_H;
    float PROPORTION;
    float FORE_PROP;

    private static Music backgroundMusic = Gdx.audio.newMusic(
            Gdx.files.internal("sounds/background_music.ogg"));
    private static Sound clickSound =
            Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));

    TransitionCover transitionActor;
    public MainMenu(LabGame game) {
        this.game = game;
    }

    public Stage getStage(){
        return stage;
    }

    public void create(){
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        this.camera = (OrthographicCamera) stage.getCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        batch = new SpriteBatch();

        //ucitavanje spritesheeta
        TextureAtlas textureAtlas = game.assetManager.get("data/labui.pack", TextureAtlas.class);

        backgroundMusic = game.assetManager.get("sounds/background_music.ogg", Music.class);
        clickSound = game.assetManager.get("sounds/click.wav", Sound.class);

        //proba123
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        //

        //test slike za gumbe
        skin = new Skin(textureAtlas);


        background = skin.getRegion("background");
        foreground = game.assetManager.get("chemistrySet.png", Texture.class);

        title = skin.getRegion("cover_alpha");
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        BitmapFont textFont = game.assetManager.get("scorefont.ttf", BitmapFont.class);
        style.font = textFont;
        style.up = skin.getDrawable("blue_button02");
        style.down = skin.getDrawable("blue_button03");
        style.pressedOffsetY = -4;


        PROPORTION = ((float)title.getRegionWidth())/camera.viewportWidth;
        TITLE_H = ((float)title.getRegionHeight())/PROPORTION;
        TITLE_Y = camera.viewportHeight-TITLE_H;
        FORE_PROP = camera.viewportWidth/480f;


        final TextButton startGameButton=new TextButton("PLAY",style);
        table.add(startGameButton).height(h / 12).width(w * 5 / 7).padBottom(h/18);

       startGameButton.addListener(new ChangeListener() {
           public void changed(ChangeEvent event, Actor actor) {
               startGameButton.setText("Starting new game");
               clickSound.play();
               transitionActor.transitionIn(game, game.gameScreen);
           }
       });


        final TextButton optionsButton=new TextButton("TUTORIAL",style);
        table.row();
        table.add(optionsButton).height(h/12).width(w*5/7).padBottom(h / 18);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                optionsButton.setText("Starting tutorial");
                clickSound.play();
                game.setScreen(game.tutorialScreen);
            }
        });

        final TextButton highScoresButton=new TextButton("HIGH SCORES",style);
        table.row();
        table.add(highScoresButton).height(h / 12).width(w * 5 / 7).padBottom(h/18);
        highScoresButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LabGame.googleServices.showScores();
                clickSound.play();
            }
        });

        final TextButton achievementsButton =new TextButton("ACHIEVEMENTS",style);
        table.row();
        table.add(achievementsButton).height(h / 12).width(w * 5 / 7).padBottom(h / 16);
        table.align(Align.bottom);
        achievementsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                LabGame.googleServices.showAchievements();
                clickSound.play();
            }
        });

        transitionActor = new TransitionCover(camera);
        stage.addActor(transitionActor);

        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.25f);
        backgroundMusic.play();

        transitionActor.transitionOut();

    }
    @Override
    public void show() {
        create();
        Gdx.input.setCatchBackKey(false);
    }




    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        batch.draw(foreground, 0, 0, foreground.getWidth()*FORE_PROP, foreground.getHeight()*FORE_PROP);


        batch.draw(title, 0, TITLE_Y, camera.viewportWidth, TITLE_H);


        batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height){
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
        stage.dispose();
        skin.dispose();
    }
}
