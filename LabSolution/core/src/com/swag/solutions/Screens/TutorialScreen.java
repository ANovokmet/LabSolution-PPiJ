package com.swag.solutions.screens;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.swag.solutions.LabGame;

/**
 * Created by Mate on 12.5.2015..
 */
public class TutorialScreen implements Screen{

    private LabGame game;
    private Camera camera;
    private Sprite pozadinaSprite;
    private Stage stage;
    private SpriteBatch batch;
    private Sprite sprite;
    private Skin skin;

    public TutorialScreen(LabGame game){
        create();
        this.game = game;
    }

    private void create() {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera(1, h / w);

        //ucitavanje spritesheeta
        TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("spritesheet.txt"));

        //ucitavanje pozadine
        pozadinaSprite = textureAtlas.createSprite("menu_pozadina");
        pozadinaSprite.setSize(1f,
                1f * h / w);
        pozadinaSprite.setOrigin(pozadinaSprite.getWidth() / 2,
                pozadinaSprite.getHeight() / 2);
        pozadinaSprite.setPosition(-pozadinaSprite.getWidth() / 2,
                -pozadinaSprite.getHeight() / 2);

        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        sprite = textureAtlas.createSprite("tutorial1");
        sprite.setSize(1f,
                1f * sprite.getHeight() / sprite.getWidth());
        sprite.setOrigin(pozadinaSprite.getWidth() / 2,
                pozadinaSprite.getHeight() / 2);
        sprite.setPosition(-pozadinaSprite.getWidth() / 2,
                -pozadinaSprite.getHeight() / 2);

        skin = new Skin();
        skin.addRegions(textureAtlas);

        // font
        Skin fontSkin = new Skin();
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("sf-atarian-system.extended-bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size =  Math.round(32 * Gdx.graphics.getDensity());
        BitmapFont bfont = generator.generateFont(parameter);
        fontSkin.add("default", bfont);

        Image image = new Image(skin.getDrawable("tutorial1"));

        //table.add(image).height(image.getHeight()).width(image.getWidth());
        //table.row().
        table.add(image).width(image.getWidth()*2/3).height(image.getHeight()*2/3).fill().padBottom(20);

        Label.LabelStyle labelStyle = new Label.LabelStyle(bfont, Color.BLACK);
        Label label = new Label("Povuci molekule u posudu\n kako bi obavile reakciju.", labelStyle);
        //label.setAlignment(Align.center);
        table.add(label).expandX();


        Image image1 = new Image(skin.getDrawable("tutorial2"));
        Label label1 = new Label("Ako ne mozes pronaci \nzeljenu molekulu,\npomici ekran u potrazi \nza njom.", labelStyle);
        label.setAlignment(Align.center);
        table.row();
        table.add(label1).width(image.getWidth() * 2 / 3).expandX();
        table.add(image1).width(150).height(200).spaceLeft(50);


        Image image2 = new Image(skin.getDrawable("tutorial3"));
        Label label2 = new Label("Pazi da ne ostanes\nbez energije.", labelStyle);
        table.row();
        table.add(image2).width(150).height(200);
        table.add(label2).expandX();
        table.setBackground(skin.getDrawable("pozadina_tablice"));

        table.center();
    }

    @Override
    public void show() {

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
