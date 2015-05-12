package com.swag.solutions.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

/**
 * Created by Ante on 15.4.2015..
 */
public class ReactionArea extends Actor {

    Texture texture_top = new Texture("reaction_area_top.png");
    Texture texture_bot = new Texture("reaction_area_bot.png");
    Rectangle bounds;
    Array<Molecule> closed_molecules;
    HashMap<Integer,Integer> targetReaction;

    OrthographicCamera camera;

    static float REAREA_X = 50;
    static float REAREA_Y = 40;
    static float REAREA_WIDTH_PERCENTAGE = 0.85f;
    static float REAREA_HEIGHT_PERCENTAGE = 0.15f;

    float BOUND_WIDTH_PERCENTAGE = 0.7f;
    float BOUND_HEIGHT_PERCENTAGE = 0.7f;

    public ReactionArea(float screenWidth, float screenHeight, OrthographicCamera camera){
        this.camera=camera;
        Vector2 botLeftCorner = new Vector2(camera.position.x-camera.viewportWidth/2, camera.position.y-camera.viewportHeight/2);

        setWidth(screenWidth*REAREA_WIDTH_PERCENTAGE);
        setHeight(screenHeight*REAREA_HEIGHT_PERCENTAGE);

        REAREA_X = (camera.viewportWidth-this.getWidth())/2f;

        setX(botLeftCorner.x+REAREA_X);
        setY(botLeftCorner.y+REAREA_Y);

        bounds=new Rectangle((int)getX()+(int)getWidth()*(1-BOUND_WIDTH_PERCENTAGE)/2, (int)getY()+(int)getHeight()*(1-BOUND_HEIGHT_PERCENTAGE)/2, (int)getWidth()*BOUND_WIDTH_PERCENTAGE, (int)getHeight()*BOUND_HEIGHT_PERCENTAGE);
        closed_molecules = new Array<Molecule>();
    }

    public Rectangle getBounds() {
        return bounds;
    }


    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(texture_bot, this.getX(), getY(), this.getOriginX(), this.getOriginY(), this.getWidth(),
                this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0,
                texture_bot.getWidth(), texture_bot.getHeight(), false, false);

        for(Molecule m : closed_molecules){
            m.draw(batch,alpha);
        }

        batch.draw(texture_top, this.getX(), getY(), this.getOriginX(), this.getOriginY(), this.getWidth(),
                this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0,
                texture_top.getWidth(), texture_top.getHeight(), false, false);

    }

    @Override
    public void act(float delta){
        super.act(delta);

        setPosition(camera.position.x-camera.viewportWidth/2+REAREA_X, camera.position.y-camera.viewportHeight/2+REAREA_Y);
        bounds.setPosition((int)getX()+(int)getWidth()*(1-BOUND_WIDTH_PERCENTAGE)/2, (int)getY()+(int)getHeight()*(1-BOUND_HEIGHT_PERCENTAGE)/2);

        for(Molecule m : closed_molecules){
            m.act(delta);
        }
    }

    public void setReaction(JsonValue reactants){

        int len = reactants.size;
        targetReaction = new HashMap<Integer,Integer>();

        for (int i = 0; i < len; i++)
        {
            JsonValue reactant = reactants.get(i);
            targetReaction.put(reactant.get("id").asInt(),reactant.get("quantity").asInt());
        }
    }

    public boolean isReactionFulfilled(){
        HashMap<Integer,Integer> h = new HashMap<Integer,Integer>();

        for(Molecule m : closed_molecules){
            int id = m.getId();
            if(h.containsKey(id)){
                h.put(id,h.get(id)+1);
            }
            else{
                h.put(id,1);
            }

        }
        Gdx.app.log("ReactionArea",  ""); //test ispis hashmapi
        for(Integer key : h.keySet()){
            Gdx.app.log("m", key+":"+h.get(key));
        }
        Gdx.app.log("Target", "");
        for(Integer key : targetReaction.keySet()){
            Gdx.app.log("m", key+":"+targetReaction.get(key));
        }
        return h.equals(targetReaction);
    }

    public void doReaction(){
        for(Molecule m: closed_molecules){
            m.remove();
        }

        closed_molecules.removeRange(0,closed_molecules.size-1);
    }


    public void addMoleculeToReaction(Molecule a){ closed_molecules.add(a); }
    public void removeMoleculeFromReaction(Molecule a){
        closed_molecules.removeValue(a,true);
    }
    public Array<Molecule> getReactionMolecules(){
        return closed_molecules;
    }

}
