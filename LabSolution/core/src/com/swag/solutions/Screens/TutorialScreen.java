package com.swag.solutions.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.swag.solutions.GameStage;
import com.swag.solutions.LabGame;

/**
 * Created by Mate on 12.5.2015..
 */
public class TutorialScreen implements Screen{

    private LabGame game;
    private Camera camera;
    private Sprite pozadinaSprite;
    private GameStage stage;
    private SpriteBatch batch;
    private Sprite sprite;
    private Skin skin;

    public TutorialScreen(LabGame game){
        this.game = game;
    }

    private void create() {

        Gdx.input.setCatchBackKey(true);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(1, h / w);

        //ucitavanje spritesheeta
        TextureAtlas textureAtlas = game.assetManager.get("spritesheet.txt", TextureAtlas.class);
        TextureAtlas textureAtlas1 = new TextureAtlas(Gdx.files.internal("menu/spritesheet1.pack"));

        //ucitavanje pozadine
        pozadinaSprite = textureAtlas1.createSprite("menu_pozadina1");
        pozadinaSprite.setSize(1f,
                1f * h / w);
        pozadinaSprite.setOrigin(pozadinaSprite.getWidth() / 2,
                pozadinaSprite.getHeight() / 2);
        pozadinaSprite.setPosition(-pozadinaSprite.getWidth() / 2,
                -pozadinaSprite.getHeight() / 2);

        batch = new SpriteBatch();
        stage = new GameStage(game);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        sprite = textureAtlas.createSprite("tutorial1");
        sprite.setSize(1f,
                1f * sprite.getHeight() / sprite.getWidth());
        sprite.setOrigin(pozadinaSprite.getWidth() / 2,
                pozadinaSprite.getHeight() / 2);
        sprite.setPosition(-pozadinaSprite.getWidth() / 2,
                -pozadinaSprite.getHeight() / 2);

        skin = new Skin();
        skin.addRegions(textureAtlas);
        skin.addRegions(textureAtlas1);

        // font
        Skin fontSkin = new Skin();
        BitmapFont bfont = game.assetManager.get("menufont.ttf", BitmapFont.class);
        fontSkin.add("default", bfont);

        Image image = new Image(skin.getDrawable("tutorial1"));

        //table.add(image).height(image.getHeight()).width(image.getWidth());
        //table.row().
        table.add(image).width(w * 5/24).height(h * 7 / 40).fill().padBottom(h / 34);

        Label.LabelStyle labelStyle = new Label.LabelStyle(bfont, Color.BLACK);
        Label label = new Label("Drag molecules in\na container in order to\nenforce a chemical reaction", labelStyle);
        label.setAlignment(Align.center);
        table.add(label).expandX().padRight(w / 28);


        Image image1 = new Image(skin.getDrawable("tut2"));
        Label label1 = new Label("Move the screen\nin order to find\nmolecules you need", labelStyle);
        label1.setAlignment(Align.center);
        table.row();
        table.add(label1).expandX();
        table.add(image1).width(w * 12 / 48).height(h * 11 / 48);


        Image image2 = new Image(skin.getDrawable("tut3"));
        Label label2 = new Label("Shake your device\nto get more energy", labelStyle);
        label2.setAlignment(Align.center);
        table.row();
        table.add(image2).width(w * 12/48).height(h * 11 / 48);
        table.add(label2).expandX();

        Image image3 = new Image(skin.getDrawable("tut4"));
        Label label3 = new Label("Press the help button\nto get hints", labelStyle);
        label3.setAlignment(Align.center);
        table.row();
        table.add(label3).expandX().padLeft(w / 28);
        table.add(image3).width(w * 12 / 48).height(h * 11 / 48);
        table.setBackground(skin.getDrawable("tablica_pozadina"));

        table.center();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(final int keycode) {
                if (keycode == Input.Keys.BACK) {
                    //Gdx.app.log("Score", "Back");
                    TutorialScreen.this.game.setScreen(TutorialScreen.this.game.mainMenu);
                }
                return false;
            }
        });

        stage.addActor(stage.transitionCover);
    }

    @Override
    public void show() {
        create();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        pozadinaSprite.draw(batch);
        //sprite.draw(batch);
        //naslovSprite.draw(batch);
        batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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
        stage.dispose();
        skin.dispose();
    }
}
