package com.swag.solutions.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Ante on 15.4.2015..
 */
public class ReactionArea extends Actor {

    Texture texture = new Texture("badlogic.jpg");
    Rectangle bounds;
    Array<Molecule> molecules;

    public ReactionArea(float x, float y, Array<Molecule> molecules){
        setX(x);
        setY(y);
        setWidth(500);
        setHeight(100);
        bounds=new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        this.molecules = molecules;
    }

    public Rectangle getBounds() {
        return bounds;
    }


    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(texture,this.getX(),getY(),this.getOriginX(),this.getOriginY(),this.getWidth(),
                this.getHeight(),this.getScaleX(), this.getScaleY(),this.getRotation(),0,0,
                texture.getWidth(),texture.getHeight(),false,false);
    }

    @Override
    public void act(float delta){
        super.act(delta);
        bounds.set(getX(), getY(), getWidth(), getHeight());

        for(Molecule molecule : molecules){
            if(Intersector.overlaps(molecule.getBounds(), bounds)){
                Gdx.app.log("REEE", (1/delta) + "");
                molecule.signalCollision(true);
            }
            else{
                molecule.signalCollision(false);
            }
        }
    }
}
