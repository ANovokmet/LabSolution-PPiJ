package com.swag.solutions.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.Font;

import static com.badlogic.gdx.scenes.scene2d.ui.Table.*;


/**
 * Created by Mate on 6.5.2015..
 */
public class MainMenu implements com.badlogic.gdx.Screen {

    private Game game;
    private Skin skin;
    private Stage stage;
    private BitmapFont font;
    private SpriteBatch batch;
    private Texture texture;
    private Texture slikaNaslov;
    private TextureRegion naslovRegija;
    private TextureRegion region;
    private Camera camera;
    private Sprite sprite;
    private Sprite naslovSprite;

    public MainMenu(Game game) {
        create();
        this.game = game;
    }

    public void create(){

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(1, h / w);

        //ucitavanje spritesheeta
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("spritesheet.txt"));

        //ucitavanje pozadine
        sprite = textureAtlas.createSprite("menu_pozadina");
        sprite.setSize(1f,
                1f * sprite.getHeight() / sprite.getWidth());
        sprite.setOrigin(sprite.getWidth() / 2,
                sprite.getHeight() / 2);
        sprite.setPosition(-sprite.getWidth() / 2,
                -sprite.getHeight() / 2);


        //ucitavanje naslova
        naslovSprite = textureAtlas.createSprite("naslov");
        naslovSprite.setRotation(10);
        naslovSprite.setSize(1.1f,
                1f * naslovSprite.getHeight() / naslovSprite.getWidth());
        naslovSprite.setOrigin(sprite.getWidth() / 2,
                sprite.getHeight() / 2);
        System.out.println(naslovSprite.getWidth());
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

        // A skin can be loaded via JSON or defined programmatically, either is fine. Using a skin is optional but strongly
        // recommended solely for the convenience of getting a texture, region, etc as a drawable, tinted drawable, etc.
        skin = new Skin();
        // Generate a 1x1 white texture and store it in the skin named "white".
        Pixmap pixmap = new Pixmap(300, 100, Format.RGBA8888);
        pixmap.setColor(106, 149, 105, (float) 0.8);
        pixmap.fill();

        skin.add("white", new Texture(pixmap));

        // Font za glavni menu
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("sf-atarian-system.extended-bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size =  Math.round(28 * Gdx.graphics.getDensity());
        BitmapFont bfont = generator.generateFont(parameter);
        skin.add("default",bfont);

        // Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
        TextButtonStyle textButtonStyle = new TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);

        textButtonStyle.font = skin.getFont("default");
        //textButtonStyle.font.setScale(1); //ne baš sretno rješenje

        skin.add("default", textButtonStyle);

        // Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
        final TextButton startGameButton=new TextButton("PLAY",textButtonStyle);
        //startGameButton.setPosition(200, 200);
        table.add(startGameButton).padBottom(10);
        //stage.addActor(startGameButton);

        // Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
        // Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
        // ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
        // revert the checked state.
       startGameButton.addListener(new ChangeListener() {
           public void changed(ChangeEvent event, Actor actor) {
               //System.out.println("Clicked! Is checked: " + button.isChecked());
               startGameButton.setText("Starting new game");
               game.setScreen(new GameScreen(game));

           }
       });


        final TextButton optionsButton=new TextButton("OPTIONS",textButtonStyle);
        table.row();
        table.add(optionsButton).padBottom(10);
        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                optionsButton.setText("U izradi");
            }
        });

        final TextButton highScoresButton=new TextButton("HIGH SCORES",textButtonStyle);
        table.row();
        table.add(highScoresButton);
        table.align(Align.bottom).padBottom(40);
        highScoresButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                highScoresButton.setText("U izradi");
            }
        });
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
        sprite.draw(batch);
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
