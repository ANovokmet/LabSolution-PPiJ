package com.swag.solutions.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
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

    public Molecule(float x, float y){
        setX(x);
        setY(y);
        setWidth(100);
        setHeight(100);
        mol = this;
        texture = textureoff;

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

        batch.draw(texture, this.getX(), getY(), this.getOriginX(), this.getOriginY(), this.getWidth(),
                this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
    }

    @Override
    public void act(float delta){
        super.act(delta);
        bounds.set(getX()+getWidth()/2, getY()+getHeight()/2, getWidth()/2);
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
