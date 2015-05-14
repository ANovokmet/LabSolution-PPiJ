package com.swag.solutions.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
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
    private Skin buttonSkin;
    private Stage stage;
    private BitmapFont font;
    private SpriteBatch batch;
    private Texture texture;
    private Texture slikaNaslov;
    private TextureRegion naslovRegija;
    private TextureRegion region;
    private Camera camera;
    private Sprite pozadinaSprite;
    private Sprite naslovSprite;
    private final static Music backgroundMusic = Gdx.audio.newMusic(
            Gdx.files.internal("sounds/background_music.ogg"));


    TransitionCover transitionActor;
    public MainMenu(LabGame game) {
        create();
        this.game = game;
    }

    public Stage getStage(){
        return stage;
    }

    public void create(){
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(1, h / w);

        //ucitavanje spritesheeta
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("spritesheet.txt"));

        //ucitavanje pozadine
        pozadinaSprite = textureAtlas.createSprite("menu_pozadina");
        /*texture = new Texture(Gdx.files.internal("menu_pozadina.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        TextureRegion region =
                new TextureRegion(texture, 0, 0, 420, 800);
        pozadinaSprite = new Sprite(region);*/
        pozadinaSprite.setSize(1f,
                1f * h / w);
        pozadinaSprite.setOrigin(pozadinaSprite.getWidth() / 2,
                pozadinaSprite.getHeight() / 2);
        pozadinaSprite.setPosition(-pozadinaSprite.getWidth() / 2,
                -pozadinaSprite.getHeight() / 2);


        //ucitavanje naslova
        naslovSprite = textureAtlas.createSprite("naslov");
        naslovSprite.setRotation(10);
        naslovSprite.setSize(1.2f,
                1f * naslovSprite.getHeight() / naslovSprite.getWidth());
        naslovSprite.setOrigin(pozadinaSprite.getWidth() / 2,
                pozadinaSprite.getHeight() / 2);
        naslovSprite.setPosition(-naslovSprite.getWidth() / 2 - 0.07f,
                -naslovSprite.getHeight() / 2 + 0.31f);

        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);



        //proba123
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        //




        //test slike za gumbe
        buttonSkin = new Skin();
        buttonSkin.addRegions(textureAtlas);


        // font za glavni menu
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("sf-atarian-system.extended-bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size =  Math.round(w/14 * Gdx.graphics.getDensity());
        BitmapFont bfont = generator.generateFont(parameter);
        buttonSkin.add("default",bfont);

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        TextButtonStyle textButtonStyleObrnuto = new TextButtonStyle();
        /*textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);*/
        textButtonStyle.up = buttonSkin.getDrawable("gumb_menu");
        textButtonStyle.down = buttonSkin.getDrawable("gumb_menu");
        textButtonStyle.checked = buttonSkin.getDrawable("gumb_menu");
        textButtonStyle.over = buttonSkin.getDrawable("gumb_menu_oznaceno");

        textButtonStyleObrnuto.up = buttonSkin.getDrawable("gumb_menu_obrnuto");
        textButtonStyleObrnuto.down = buttonSkin.getDrawable("gumb_menu_obrnuto");
        textButtonStyleObrnuto.checked = buttonSkin.getDrawable("gumb_menu_obrnuto");
        textButtonStyleObrnuto.over = buttonSkin.getDrawable("gumb_menu_obrnuto_oznaceno");

        textButtonStyle.font = buttonSkin.getFont("default");
        textButtonStyleObrnuto.font = buttonSkin.getFont("default");
        textButtonStyle.fontColor = Color.BLACK;
        textButtonStyleObrnuto.fontColor = Color.BLACK;
        //textButtonStyle.font.setScale(1); //ne ba� sretno rje�enje

        buttonSkin.add("default", textButtonStyle);

        final Sound clickSound =
                Gdx.audio.newSound(Gdx.files.internal("sounds/click.wav"));

        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final TextButton startGameButton=new TextButton("PLAY",textButtonStyle);
        //startGameButton.setPosition(200, 200);
        table.add(startGameButton).height(h / 12).width(w * 6 / 7).padBottom(h/18);
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
               transitionActor.transitionIn(game,new GameScreen(game));
           }
       });


        final TextButton optionsButton=new TextButton("TUTORIAL",textButtonStyleObrnuto);
        table.row();
        table.add(optionsButton).height(h/12).width(w*6/7).padBottom(h/18);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                optionsButton.setText("Starting tutorial");
                clickSound.play();
                game.setScreen(new com.swag.solutions.Screens.TutorialScreen(game));
            }
        });

        final TextButton highScoresButton=new TextButton("HIGH SCORES",textButtonStyle);
        highScoresButton.setHeight(150);
        highScoresButton.setWidth(400);
        table.row();
        table.add(highScoresButton).height(h / 12).width(w * 6 / 7).padBottom(h / 8);
        table.align(Align.bottom);
        highScoresButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                highScoresButton.setText("U izradi");
                clickSound.play();
            }
        });

        transitionActor = new TransitionCover(camera);
        stage.addActor(transitionActor);

        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.25f);
        backgroundMusic.play();

    }
    @Override
    public void show() {

    }



    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        pozadinaSprite.draw(batch);
        naslovSprite.draw(batch);

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
