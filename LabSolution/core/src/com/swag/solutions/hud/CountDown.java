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
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.swag.solutions.screens.GameScreen;

/**
 * Created by Ante on 15.5.2015..
 */
public class CountDown extends Actor {

    GameScreen screen;
    OrthographicCamera camera;

    Label label;
    Table table;


    Skin skin = new Skin();

    int FONT_SIZE;

    public CountDown(OrthographicCamera camera, GameScreen screen, AssetManager manager){
        this.camera = camera;
        this.screen = screen;
        setWidth(camera.viewportWidth);
        setHeight(camera.viewportHeight);

        table = new Table();
        FONT_SIZE = (int)(200*camera.viewportWidth/480f);
        table.getColor().a = 1;


        BitmapFont textFont = manager.get("countdownfont.ttf", BitmapFont.class);
        Label.LabelStyle ls = new Label.LabelStyle(textFont, Color.WHITE);
        skin.add("nas_font",ls);



        label = new Label("3", skin, "nas_font");
        label.setWrap(true);
        label.setAlignment(Align.center);
        table.add(label).width(this.getWidth()).height(this.getHeight());
        Pixmap pm = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pm.setColor(1f, 1f, 1f, (float) 0.3);
        pm.fill();
        table.background(new TextureRegionDrawable(new TextureRegion(new Texture(pm))));
        table.pack();

    }


    float timeSecond = 0;
    int seconds = 2;

    private void setText(String s){
        label.setText(s);
        table.pack();
    }

    @Override
    public void act(float delta){
        super.act(delta);
        setX(camera.position.x - camera.viewportWidth / 2);
        setY(camera.position.y-camera.viewportHeight/2);

        if(screen.gameState == GameScreen.State.COUNTDOWN){
            timeSecond+=delta;
            if(timeSecond>=0.75f){
                Gdx.app.log("timesecond",timeSecond+"");
                Gdx.app.log("second",seconds+"");

                timeSecond=0;
                if(seconds==0){
                    setText("GO");
                }else if(seconds==-1){
                    stopCountdown();
                }
                else{
                    setText(seconds+"");
                }
                seconds-=1;
            }
        }

        table.setPosition(camera.position.x - table.getWidth() / 2, camera.position.y - table.getHeight() / 2);
        table.act(delta);
    }



    @Override
    public void draw(Batch batch, float alpha){
        if(screen.gameState== GameScreen.State.COUNTDOWN){
            batch.setColor(1.0f, 1.0f, 1.0f, alpha * table.getColor().a);
            table.draw(batch, alpha);
            batch.setColor(1.0f, 1.0f, 1.0f, alpha);
        }
    }

    public void startCountdown(){
        setWidth(camera.viewportWidth);
        setHeight(camera.viewportHeight);
        timeSecond = 0;
        seconds = 2;
        setText("3");
        this.setTouchable(Touchable.enabled);
        screen.gameState = GameScreen.State.COUNTDOWN;
    }

    public void stopCountdown(){
        this.setWidth(0);
        this.setHeight(0);
        this.setTouchable(Touchable.disabled);
        screen.gameState = GameScreen.State.PLAYING;
    }

}
