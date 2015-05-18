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
    //private Skin buttonSkin;
    private Stage stage;
    private BitmapFont font;
    private SpriteBatch batch;
    private Texture foreground = new Texture("chemistrySet.png");
    private Texture slikaNaslov;
    private TextureRegion title;
    private TextureRegion background;
    private OrthographicCamera camera;
    //private Sprite pozadinaSprite;
    //private Sprite naslovSprite;

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



        //ucitavanje pozadine



        /*pozadinaSprite = textureAtlas.createSprite("menu_pozadina1");

        pozadinaSprite.setSize(1f,
                1f * h / w);
        pozadinaSprite.setOrigin(pozadinaSprite.getWidth() / 2,
                pozadinaSprite.getHeight() / 2);
        pozadinaSprite.setPosition(-pozadinaSprite.getWidth() / 2,
                -pozadinaSprite.getHeight() / 2);*/

        /*pozadinaSprite.getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        //ucitavanje naslova
        naslovSprite = textureAtlas1.createSprite("cover");
        naslovSprite.setSize(1f,
                1f * naslovSprite.getHeight() / naslovSprite.getWidth());
        naslovSprite.setOrigin(pozadinaSprite.getWidth() / 2,
                pozadinaSprite.getHeight() / 2);
        naslovSprite.setPosition(-naslovSprite.getWidth() / 2,
                -naslovSprite.getHeight() / 2 + 0.41f);*/




        //proba123
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        //

        //test slike za gumbe
        skin = new Skin(textureAtlas);
        //buttonSkin.addRegions(textureAtlas2);


        // font za glavni menu
        /*BitmapFont bfont = game.assetManager.get("menufont.ttf", BitmapFont.class);
        buttonSkin.add("default",bfont);

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = buttonSkin.getDrawable("blue_button00");
        textButtonStyle.down = buttonSkin.getDrawable("blue_button00");
        textButtonStyle.checked = buttonSkin.getDrawable("blue_button00");
        textButtonStyle.over = buttonSkin.getDrawable("blue_button01");

        textButtonStyle.font = buttonSkin.getFont("default");
        textButtonStyle.fontColor = Color.BLACK;

        buttonSkin.add("default", textButtonStyle);*/

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


        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final TextButton startGameButton=new TextButton("PLAY",style);
        //startGameButton.setPosition(200, 200);
        table.add(startGameButton).height(h / 12).width(w * 5 / 7).padBottom(h/18);
        //stage.addActor(startGameButton);

        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
       startGameButton.addListener(new ChangeListener() {
           public void changed(ChangeEvent event, Actor actor) {
               //System.out.println("Clicked! Is checked: " + button.isChecked());
               startGameButton.setText("Starting new game");
               clickSound.play();
               //game.setScreen(new GameScreen(game));
               //game.gameScreen.create();
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
        highScoresButton.addListener(new ChangeListener() {
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
        //Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
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
