package com.swag.solutions.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.swag.solutions.LabGame;

/**
 * Created by Ante on 16.5.2015..
 */
public class LoadingScreen implements Screen {

    LabGame game;
    AssetManager manager;

    int BIG_FONT_SIZE;
    int SMALL_FONT_SIZE;
    int ENERGY_FONT_SIZE;
    int SCORE_FONT_SIZE;
    int COUNTDOWN_FONT_SIZE;
    int HINT_FONT_SIZE;
    int MENU_FONT_SIZE;
    int QUIT_FONT_SIZE;
    int LEVEL_SCORE_FONT_SIZE;
    float percent;

    BitmapFont loadText;
    SpriteBatch batch;
    Stage stage;

    Label label;
    Table table;

    public LoadingScreen(LabGame game){
        this.game = game;
        this.manager = game.assetManager;
        this.stage = new Stage();
        //((OrthographicCamera) stage.getCamera()).setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float gameWidth = Gdx.graphics.getWidth();
        BIG_FONT_SIZE = (int)(96 * gameWidth/480f);
        SMALL_FONT_SIZE = (int)(48 * gameWidth/480f);
        ENERGY_FONT_SIZE = (int)(24 * gameWidth/480f);
        COUNTDOWN_FONT_SIZE = (int)(200 * gameWidth/480f);
        HINT_FONT_SIZE = (int)(36 * gameWidth/480f);
        SCORE_FONT_SIZE = (int)(48 * gameWidth/480f);
        QUIT_FONT_SIZE = (int)(96 * gameWidth/480f);
        LEVEL_SCORE_FONT_SIZE = (int)(24 * gameWidth/480f);
        MENU_FONT_SIZE = (int)(gameWidth/14 * Gdx.graphics.getDensity());
        batch = new SpriteBatch();

        FileHandleResolver resolver = new InternalFileHandleResolver();
        manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter size4Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size4Params.fontFileName = "orange-juice.ttf";
        size4Params.fontParameters.size = COUNTDOWN_FONT_SIZE;
        manager.load("countdownfont.ttf", BitmapFont.class, size4Params);

        manager.finishLoading();

        loadText = manager.get("countdownfont.ttf", BitmapFont.class);
        loadText.setColor(Color.WHITE);

        Label.LabelStyle ls = new Label.LabelStyle(loadText, Color.WHITE);
        Skin skin = new Skin();
        table = new Table();
        skin.add("loadfont",ls);
        label = new Label("0%", skin, "loadfont");
        label.setAlignment(Align.center);
        table.add(label).width(Gdx.graphics.getWidth()).height(Gdx.graphics.getHeight());
        table.pack();

        game.assetManager.load("spritesheet.txt", TextureAtlas.class);

        manager.load("bar_empty_bot.png", Texture.class);
        manager.load("bar_empty_mid.png", Texture.class);
        manager.load("bar_empty_top.png", Texture.class);
        manager.load("bar_fill_bot.png", Texture.class);
        manager.load("bar_fill_mid.png", Texture.class);
        manager.load("bar_fill_top.png", Texture.class);
        manager.load("barRed_verticalMid.png", Texture.class);
        manager.load("professor1x0.png", Texture.class);
        manager.load("hint_c.png",Texture.class);
        manager.load("hint_z.png",Texture.class);
        manager.load("hint_s.png",Texture.class);
        manager.load("blackboard.png", Texture.class);
        manager.load("wateranim.png", Texture.class);
        manager.load("AzureWaters.jpg", Texture.class);
        manager.load("reaction_area_top.png", Texture.class);
        manager.load("reaction_area_bot.png", Texture.class);

        JsonReader jsonReader = new JsonReader();
        FileHandle moleculesFile = Gdx.files.internal("data/all.json");
        JsonValue molecules = jsonReader.parse(moleculesFile.readString());
        for(int i=0;i<molecules.size;i++){
            JsonValue moleculeInfo = molecules.get(i);
            manager.load(moleculeInfo.get("path").asString(), Texture.class);
        }

        manager.load("data/labui.pack", TextureAtlas.class);

        manager.load("sounds/mumbling.ogg", Sound.class);
        manager.load("sounds/game_finished.wav", Sound.class);
        manager.load("sounds/game_over.wav", Sound.class);
        manager.load("sounds/molecule_pick_up.wav", Sound.class);
        manager.load("sounds/molecule_put_down.wav", Sound.class);
        manager.load("sounds/reaction_success.wav", Sound.class);
        manager.load("sounds/click.wav", Sound.class);

        manager.load("sounds/background_music.ogg", Music.class);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size1Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size1Params.fontFileName = "coolvetica.ttf";
        size1Params.fontParameters.size = BIG_FONT_SIZE;
        manager.load("bigfont.ttf", BitmapFont.class, size1Params);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size2Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size2Params.fontFileName = "coolvetica.ttf";
        size2Params.fontParameters.size = SMALL_FONT_SIZE;
        manager.load("smallfont.ttf", BitmapFont.class, size2Params);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size3Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size3Params.fontFileName = "coolvetica.ttf";
        size3Params.fontParameters.size = ENERGY_FONT_SIZE;
        manager.load("energyfont.ttf", BitmapFont.class, size3Params);



        FreetypeFontLoader.FreeTypeFontLoaderParameter size5Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size5Params.fontFileName = "orange-juice.ttf";
        size5Params.fontParameters.size = HINT_FONT_SIZE;
        manager.load("hintfont.ttf", BitmapFont.class, size5Params);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size6Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size6Params.fontFileName = "coolvetica.ttf";
        size6Params.fontParameters.size = SCORE_FONT_SIZE;
        manager.load("scorefont.ttf", BitmapFont.class, size6Params);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size9Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size9Params.fontFileName = "coolvetica.ttf";
        size9Params.fontParameters.size = LEVEL_SCORE_FONT_SIZE;
        manager.load("levelscorefont.ttf", BitmapFont.class, size9Params);


        FreetypeFontLoader.FreeTypeFontLoaderParameter size7Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size7Params.fontFileName = "sf-atarian-system.extended-bold.ttf";
        size7Params.fontParameters.size = MENU_FONT_SIZE;
        manager.load("menufont.ttf", BitmapFont.class, size7Params);

        FreetypeFontLoader.FreeTypeFontLoaderParameter size8Params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        size8Params.fontFileName = "orange-juice.ttf";
        size8Params.fontParameters.size = QUIT_FONT_SIZE;
        manager.load("quitfont.ttf", BitmapFont.class, size8Params);


    }


    @Override
    public void show() {


    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0/255.0f, 0/255.0f, 0/255.0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (game.assetManager.update()) {
                game.setScreen(game.mainMenu);
        }

        percent = manager.getProgress();//Interpolation.linear.apply(percent, manager.getProgress(), 0.1f);
        label.setText((int)(percent*100)+"%");

        batch.begin();
        table.draw(batch,1);
        batch.end();
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
