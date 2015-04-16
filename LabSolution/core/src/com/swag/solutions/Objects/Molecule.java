package com.swag.solutions.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;

/**
 * Created by Ante on 15.4.2015..
 */
public class Molecule extends Actor {
    Texture textureoff = new Texture("badlogic.jpg");
    Texture textureon = new Texture("goodlogic.jpg");
    Texture texture;
    boolean collided = false;
    final Molecule mol;
    Circle bounds;
    float rotacija=0;  //posebni param za rotaciju zbog načina izračuna odmaka translacije
    float brzina_rotacije=0;

    public Molecule(float x, float y){
        setX(x);
        setY(y);
        setWidth(100);
        setHeight(100);
        mol = this;
        texture = textureoff;

        setOrigin(getWidth()/2,getHeight()/2);
        brzina_rotacije=MathUtils.random(-15,15);

        this.addListener(new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                mol.moveBy(x - mol.getWidth() / 2, y - mol.getHeight() / 2);
            }
        });
        bounds = new Circle(getX()+getWidth()/2, getY()+getHeight()/2, getWidth()/2);
    }

    @Override
    public void draw(Batch batch, float alpha){
        if(collided){
            texture = textureon;
        }else {
            texture = textureoff;
        }
        batch.draw(texture, getX(), getY(), this.getOriginX(), this.getOriginY(), this.getWidth(),
                this.getHeight(), this.getScaleX(), this.getScaleY(), rotacija, 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
    }

    @Override
    public void act(float delta){
        super.act(delta);
        bounds.set(getX()+getWidth()/2, getY()+getHeight()/2, getWidth()/2);


        rotacija+=brzina_rotacije*delta;
        //setRotation(getRotation()+10*delta);
        //Gdx.app.error("brojevi2",""+getOriginX()+" "+getOriginY()+" "+getX()+" "+getY()+" "+getRotation());
    }

    public Circle getBounds() {
        return bounds;
    }

    @Override
    public Actor hit(float x, float y, boolean touchable){

        return super.hit( x,  y,  touchable);
    }

    public boolean isInside(int coordX, int coordY){
        if(this.getX() < coordX && (this.getWidth()+this.getX()) > coordX && this.getY() < coordY && (this.getHeight()+this.getY())> coordY)
            return true;
        return false;
    }

    public void setCenter(int x, int y){
        super.setPosition(x-getWidth()/2, y-getHeight()/2);
    }

    public void signalCollision(boolean b) {
        this.collided = b;
    }
}
