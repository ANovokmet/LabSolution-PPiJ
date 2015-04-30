package com.swag.solutions.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
    Array<Molecule> closed_molecules;

    OrthographicCamera camera;

    static float REAREA_X = 50;
    static float REAREA_Y = 40;
    static float REAREA_WIDTH = 200;
    static float REAREA_HEIGHT = 100;

    public ReactionArea(float screenWidth, float screenHeight, OrthographicCamera camera){
        this.camera=camera;
        Vector2 botLeftCorner = new Vector2(camera.position.x-camera.viewportWidth/2, camera.position.y-camera.viewportHeight/2);
        setX(botLeftCorner.x+REAREA_X);
        setY(botLeftCorner.y+REAREA_Y);
        setWidth(screenWidth-100);
        setHeight(REAREA_HEIGHT);
        bounds=new Rectangle((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
        closed_molecules = new Array<Molecule>();
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

        setPosition(camera.position.x-camera.viewportWidth/2+REAREA_X, camera.position.y-camera.viewportHeight/2+REAREA_Y);
        bounds.set(getX(), getY(), getWidth(), getHeight());
    }
    public void addMoleculeToReaction(Molecule a){
        closed_molecules.add(a);
    }
    public void removeMoleculeFromReaction(Molecule a){
        closed_molecules.removeValue(a,true);
    }
    public Array<Molecule> getReactionMolecules(){
        return closed_molecules;
    }
}
