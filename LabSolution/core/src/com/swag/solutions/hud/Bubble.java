package com.swag.solutions.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Ante on 6.5.2015..
 */
public class Bubble extends Actor {
    Label label;
    Skin skin = new Skin();
    Table table = new Table();

    public boolean hinting = false;

    private float BUBBLE_WIDTH = 256;
    private float BUBBLE_HEIGHT = 128;
    private float BUBBLE_X = 50;
    private float BUBBLE_Y = 120;
    private int FONT_SIZE;

    private OrthographicCamera camera;
    public Bubble (OrthographicCamera camera, AssetManager manager){
        this.camera = camera;
        setWidth(BUBBLE_WIDTH);
        setHeight(BUBBLE_HEIGHT);

        BUBBLE_WIDTH=camera.viewportWidth*3/4;
        FONT_SIZE = (int)(36*camera.viewportWidth/480f);
        table.getColor().a = 0;


        BitmapFont textFont = manager.get("hintfont.ttf", BitmapFont.class);
        Label.LabelStyle ls = new Label.LabelStyle(textFont, Color.BLACK);
        skin.add("nas_font",ls);

        label = new Label("", skin, "nas_font");
        label.setWrap(true);
        label.setAlignment(Align.center);
        table.add(label).width(BUBBLE_WIDTH);


        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 1f, 1f, (float) 0.5);
        pm.fill();
        table.background(new TextureRegionDrawable(new TextureRegion(new Texture(pm))));
        table.pack();


    }

    public void setHintText(String s){
        label.setText(s);
        table.setWidth(BUBBLE_WIDTH);
        table.pack();
    }

    public void act(float delta) {
        super.act(delta);
        setX(camera.position.x);
        setY(camera.position.y);

        table.setPosition(getX()-BUBBLE_WIDTH/2, getY()+BUBBLE_Y);

        table.act(delta);
        if(table.getColor().a == 0)
            hinting = false;
    }

    public void draw(Batch batch, float alpha) {
        //Gdx.app.error("X",camera.position.x+"");
        //Gdx.app.error("Y",camera.position.y+"");


        batch.setColor(1.0f, 1.0f, 1.0f, alpha * table.getColor().a);
        table.draw(batch, alpha);
        batch.setColor(1.0f, 1.0f, 1.0f, alpha);

    }

    public boolean isHinting(){
        return hinting;
    }

    public void addAction(Action action){
        table.addAction(action);
    }
}
