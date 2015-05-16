package com.swag.solutions.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.swag.solutions.screens.GameScreen;

/**
 * Created by Ante on 16.5.2015..
 */
public class QuitDialog extends Actor{
    GameScreen screen;
    OrthographicCamera camera;

    Label label;
    Label sublabel;
    Table table;
    Skin skin = new Skin();

    boolean show = false;

    public QuitDialog(OrthographicCamera camera, final GameScreen screen, AssetManager manager){
        this.camera = camera;
        this.screen = screen;
        setWidth(camera.viewportWidth);
        setHeight(camera.viewportHeight);

        table = new Table();
        table.getColor().a = 1;


        BitmapFont textFont = manager.get("quitfont.ttf", BitmapFont.class);
        Label.LabelStyle ls = new Label.LabelStyle(textFont, Color.WHITE);
        skin.add("big_font",ls);

        BitmapFont subFont = manager.get("hintfont.ttf", BitmapFont.class);
        Label.LabelStyle ss = new Label.LabelStyle(subFont, Color.WHITE);
        skin.add("sub_font",ss);

        label = new Label("Quit game?", skin, "big_font");
        label.setWrap(true);
        label.setAlignment(Align.bottom);
        table.add(label).width(this.getWidth()).height(this.getHeight()/2).row();
        sublabel = new Label("(Tap to continue)", skin, "sub_font");
        sublabel.setWrap(true);
        sublabel.setAlignment(Align.top);
        table.add(sublabel).width(this.getWidth()).height(this.getHeight()/2);
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 1f, 1f, (float) 0.3);
        pm.fill();
        table.background(new TextureRegionDrawable(new TextureRegion(new Texture(pm))));
        table.pack();

        this.addListener(new DragListener() {

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                hideDialog();
            }
        });

    }

    public void showDialog(){
        show = true;
        setWidth(camera.viewportWidth);
        setHeight(camera.viewportHeight);
    }

    public void hideDialog(){
        show = false;
        setWidth(0);
        setHeight(0);
    }

    @Override
    public void act(float delta){
        if(show) {
            super.act(delta);
            setX(camera.position.x - camera.viewportWidth / 2);
            setY(camera.position.y - camera.viewportHeight / 2);

            table.setPosition(camera.position.x - table.getWidth() / 2, camera.position.y - table.getHeight() / 2);
            table.act(delta);
        }
    }



    @Override
    public void draw(Batch batch, float alpha){
        if(show){
            batch.setColor(1.0f, 1.0f, 1.0f, alpha * table.getColor().a);
            table.draw(batch, alpha);
            batch.setColor(1.0f, 1.0f, 1.0f, alpha);
        }
    }

    public boolean isVisible(){
        return show;
    }

}
