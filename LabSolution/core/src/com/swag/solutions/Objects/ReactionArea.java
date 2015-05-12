package com.swag.solutions.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ante on 15.4.2015..
 */
public class ReactionArea extends Actor {

    Texture texture_top = new Texture("reaction_area_top.png");
    Texture texture_bot = new Texture("reaction_area_bot.png");

    static float REAREA_X = 50;
    static float REAREA_Y = 40;
    static float REAREA_WIDTH_PERCENTAGE = 0.85f;
    static float REAREA_HEIGHT_PERCENTAGE = 0.15f;

    float BOUND_WIDTH_PERCENTAGE = 0.7f;
    float BOUND_HEIGHT_PERCENTAGE = 0.7f;

    Rectangle bounds;
    OrthographicCamera camera;

    Array<Molecule> closedMolecules;
    private Map<Integer, Integer> neededReactants;

    public ReactionArea(float screenWidth, float screenHeight,
                        OrthographicCamera camera){
        this.camera=camera;
        Vector2 botLeftCorner = new Vector2(
                camera.position.x - camera.viewportWidth/2,
                camera.position.y - camera.viewportHeight/2);

        setWidth(screenWidth * REAREA_WIDTH_PERCENTAGE);
        setHeight(screenHeight * REAREA_HEIGHT_PERCENTAGE);

        REAREA_X = (camera.viewportWidth - this.getWidth()) / 2f;

        setX(botLeftCorner.x + REAREA_X);
        setY(botLeftCorner.y + REAREA_Y);

        bounds = new Rectangle(
                (int)getX() + (int)getWidth()*(1-BOUND_WIDTH_PERCENTAGE)/2,
                (int)getY() + (int)getHeight()*(1-BOUND_HEIGHT_PERCENTAGE)/2,
                (int)getWidth()*BOUND_WIDTH_PERCENTAGE,
                (int)getHeight()*BOUND_HEIGHT_PERCENTAGE);

        closedMolecules = new Array<Molecule>();
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void addMoleculeToReaction(Molecule a){
        closedMolecules.add(a);
    }

    public void removeMoleculeFromReaction(Molecule a){
        closedMolecules.removeValue(a, true);
    }

    public Array<Molecule> getReactionMolecules(){
        return closedMolecules;
    }

    public void setNeededReactants(Map<Integer, Integer> neededReactants) {
        this.neededReactants = neededReactants;
    }

    public Map<Integer, Integer> getCurrentReactants() {
        Map<Integer,Integer> currReactants = new HashMap<Integer,Integer>();
        for (Molecule molecule : closedMolecules) {
            int molecId = molecule.getId();
            if (currReactants.containsKey(molecId)) {
                currReactants.put(molecId, currReactants.get(molecId) + 1);
            } else {
                currReactants.put(molecId, 1);
            }
        }
        return currReactants;
    }

    public boolean isReactionFulfilled(){
        Map<Integer,Integer> currentReactants = getCurrentReactants();

        Gdx.app.log("ReactionArea",  ""); //test ispis hashmapi
        for(Integer key : currentReactants.keySet()){
            Gdx.app.log("m", key+":"+currentReactants.get(key));
        }
        Gdx.app.log("Target", "");
        for(Integer key : neededReactants.keySet()){
            Gdx.app.log("m", key+":"+ neededReactants.get(key));
        }

        return currentReactants.equals(neededReactants);
    }

    public void doReaction(){
        for(Molecule molecule : closedMolecules){
            molecule.remove();
        }
        closedMolecules.removeRange(0, closedMolecules.size-1);
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(texture_bot, this.getX(), getY(),
                this.getOriginX(), this.getOriginY(),
                this.getWidth(), this.getHeight(),
                this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0,
                texture_bot.getWidth(), texture_bot.getHeight(), false, false);

        batch.draw(texture_top, this.getX(), getY(),
                this.getOriginX(), this.getOriginY(),
                this.getWidth(), this.getHeight(),
                this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0,
                texture_top.getWidth(), texture_top.getHeight(), false, false);

        for(Molecule m : closedMolecules){
            m.draw(batch, alpha);
        }
    }

    @Override
    public void act(float delta){
        super.act(delta);

        setPosition(camera.position.x - camera.viewportWidth / 2 + REAREA_X,
                camera.position.y - camera.viewportHeight / 2 + REAREA_Y);

        bounds.setPosition(
                (int) getX() + (int) getWidth() * (1 - BOUND_WIDTH_PERCENTAGE) / 2,
                (int) getY() + (int) getHeight() * (1 - BOUND_HEIGHT_PERCENTAGE) / 2);

        for(Molecule m : closedMolecules){
            m.act(delta);
        }
    }
}
