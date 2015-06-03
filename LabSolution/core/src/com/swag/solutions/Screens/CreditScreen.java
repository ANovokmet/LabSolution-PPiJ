package com.swag.solutions.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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
public class CreditScreen implements Screen{

    private LabGame game;
    private OrthographicCamera camera;
    private GameStage stage;
    private SpriteBatch batch;
    private Sprite sprite;
    private Skin skin;
    static Sound clickSound;

    private TextureRegion background;

    float VERTICAL_PADDING;
    float HORIZONTAL_PADDING;

    public CreditScreen(LabGame game){
        this.game = game;
    }

    private void create() {
        clickSound = game.assetManager.get("sounds/click.wav", Sound.class);
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
        BitmapFont bfont = game.assetManager.get("scorefont.ttf", BitmapFont.class);
        fontSkin.add("default", bfont);



        Table table1 = new Table();


        Label.LabelStyle labelStyle = new Label.LabelStyle(bfont, Color.DARK_GRAY);
        Label label = new Label("Goran Rumin\nAnte Novokmet\nBranimir Klaric\nMate Pipunic\nMarija Spendic", labelStyle);
        label.setAlignment(Align.bottom);

        table1.add(label).expandX().padRight(w / 28);
        //table1.row();


        Table table2 = new Table();
        Label label1 = new Label("PPiJ 2015 @FER", labelStyle);
        label1.setAlignment(Align.center);

        table2.add(label1).expandX();
        //table2.row();



        BitmapFont sfont = game.assetManager.get("menufont.ttf", BitmapFont.class);

        Label.LabelStyle labelStyle2 = new Label.LabelStyle(sfont, Color.DARK_GRAY);


        Table table3 = new Table();
        Label label2 = new Label("Orange Juice font from brittneymurphydesign", labelStyle2);
        label2.setAlignment(Align.center);

        table3.add(label2).expandX();
        //table3.row();

        table1.setBackground(skin.getDrawable("panel_top"));
        table2.setBackground(skin.getDrawable("panel_mid"));
        table3.setBackground(skin.getDrawable("panel_bot"));

        table.add(table1).prefWidth(camera.viewportWidth).prefHeight(h * 11 / 48).row();
        table.add(table2).prefWidth(camera.viewportWidth).prefHeight(h * 11 / 48).row();
        table.add(table3).prefWidth(camera.viewportWidth).prefHeight(h * 11 / 48);
        table.center();

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyUp(final int keycode) {
                if ((keycode == Input.Keys.ESCAPE) || (keycode == Input.Keys.BACK)) {
                    clickSound.play();
                    stage.transitionCover.transitionIn(game, game.mainMenu);
                }
                return false;
            }
        });

        stage.addActor(stage.transitionCover);
    }

    @Override
    public void show() {
        create();
        stage.transitionCover.transitionOut();
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
