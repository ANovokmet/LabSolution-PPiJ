package com.swag.solutions.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private OrthographicCamera camera;
    private Sprite pozadinaSprite;
    private GameStage stage;
    private SpriteBatch batch;
    private Sprite sprite;
    private Skin skin;

    private TextureRegion background;

    float VERTICAL_PADDING;
    float HORIZONTAL_PADDING;

    public TutorialScreen(LabGame game){
        this.game = game;
    }

    private void create() {
        Gdx.input.setCatchBackKey(true);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        stage = new GameStage(game);
        this.camera = (OrthographicCamera) stage.getCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        //ucitavanje spritesheeta
        TextureAtlas imageAtlas = game.assetManager.get("menu/spritesheet1.pack", TextureAtlas.class);
        TextureAtlas textureAtlas = game.assetManager.get("data/labui.pack", TextureAtlas.class);

        VERTICAL_PADDING = 0*h/480f;
        HORIZONTAL_PADDING = 20*w/480f;


        batch = new SpriteBatch();
        stage = new GameStage(game);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

        sprite = imageAtlas.createSprite("tutorial1");
        sprite.setSize(1f,
                1f * sprite.getHeight() / sprite.getWidth());
        sprite.setOrigin(w / 2,
                h / 2);
        sprite.setPosition(-w / 2,
                -h / 2);

        skin = new Skin();
        skin.addRegions(textureAtlas);
        skin.addRegions(imageAtlas);



        background = skin.getRegion("background");

        // font
        Skin fontSkin = new Skin();
        BitmapFont bfont = game.assetManager.get("menufont.ttf", BitmapFont.class);
        fontSkin.add("default", bfont);



        Image image = new Image(skin.getDrawable("tutorial1"));
        Table table1 = new Table();


        Label.LabelStyle labelStyle = new Label.LabelStyle(bfont, Color.DARK_GRAY);
        Label label = new Label("Drag molecules in\na container in order to\nenforce a chemical reaction", labelStyle);
        label.setAlignment(Align.center);

        table1.add(image).width(w * 5/24).height(h * 7 / 40).padBottom(h / 34).pad(VERTICAL_PADDING,HORIZONTAL_PADDING,VERTICAL_PADDING,0);
        table1.add(label).expandX().padRight(w / 28);
        //table1.row();


        Table table2 = new Table();
        Image image1 = new Image(skin.getDrawable("tut2"));
        Label label1 = new Label("Move the screen\nin order to find\nmolecules you need", labelStyle);
        label1.setAlignment(Align.center);

        table2.add(label1).expandX();
        table2.add(image1).width(w * 12 / 48).height(h * 11 / 48).pad(VERTICAL_PADDING,0,VERTICAL_PADDING,HORIZONTAL_PADDING);
        //table2.row();


        Table table3 = new Table();
        Image image2 = new Image(skin.getDrawable("tut3"));
        Label label2 = new Label("Shake your device\nto get more energy", labelStyle);
        label2.setAlignment(Align.center);

        table3.add(image2).width(w * 12/48).height(h * 11 / 48).pad(VERTICAL_PADDING,HORIZONTAL_PADDING,VERTICAL_PADDING,0);
        table3.add(label2).expandX();
        //table3.row();


        Table table4 = new Table();
        Image image3 = new Image(skin.getDrawable("tut4"));
        Label label3 = new Label("Press the help button\nto get hints", labelStyle);
        label3.setAlignment(Align.center);

        table4.add(label3).expandX();//.padLeft(w / 28);
        table4.add(image3).width(w * 12 / 48).height(h * 11 / 48).pad(VERTICAL_PADDING,0,VERTICAL_PADDING,HORIZONTAL_PADDING);

        table1.setBackground(skin.getDrawable("panel_top"));
        table2.setBackground(skin.getDrawable("panel_mid"));
        table3.setBackground(skin.getDrawable("panel_mid"));
        table4.setBackground(skin.getDrawable("panel_bot"));

        table.add(table1).prefWidth(camera.viewportWidth).prefHeight(h * 11 / 48).row();
        table.add(table2).prefWidth(camera.viewportWidth).prefHeight(h * 11 / 48).row();
        table.add(table3).prefWidth(camera.viewportWidth).prefHeight(h * 11 / 48).row();
        table.add(table4).prefWidth(camera.viewportWidth).prefHeight(h * 11 / 48);//.row();
        table.center();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(final int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK)) {
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

        batch.draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
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
