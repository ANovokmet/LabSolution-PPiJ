package com.swag.solutions.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.swag.solutions.GameStage;

import java.util.Map;

/**
 * Created by Goran on 19.4.2015..
 */
public class Solution extends Actor {

    public float left_x, right_x, top_y, bottom_y, size_x, size_y;

    private final int NUM_FREE_MOLECULES = 30;

    private Array<Molecule> freeMolecules;
    private ReactionArea reactionArea;
    private Stage stage;
    Texture texture = new Texture("badlogic.jpg");

    public Solution(float sizex, float sizey, ReactionArea podrucje, GameStage gameStage){
        float offsetx = Gdx.graphics.getWidth();
        float offsety = Gdx.graphics.getHeight();
        size_x=sizex+offsetx;
        size_y=sizey+offsety;
        left_x=-sizex/2;
        right_x=sizex/2+offsetx;
        top_y=sizey/2+offsety;
        bottom_y=-sizey/2;
        freeMolecules = new Array<Molecule>();
        reactionArea =podrucje;
        stage = gameStage;
    }

    @Override
    public void act(float delta) {
        Rectangle area_bounds = reactionArea.getBounds();
        for(Molecule molecule : freeMolecules){
            molecule.setReactionAreaBounds(area_bounds);  //podesavanje granica da bi molekule mogle provjeriti da li su u reakciji
        }
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(texture, left_x, bottom_y , 10, size_y);
        batch.draw(texture, right_x-10, bottom_y , 10, size_y);
        batch.draw(texture, left_x, top_y-10 , size_x, 10);
        batch.draw(texture, left_x, bottom_y , size_x, 10);
    }

    public void generateMolecules(JsonValue molecules){
        for (int i = 0; i < NUM_FREE_MOLECULES; ++i) {
            JsonValue moleculeInfo =
                    molecules.get(MathUtils.random(0, molecules.size - 1));
            Molecule molecule = new Molecule(
                    MathUtils.random(left_x + 100, right_x - 100),
                    MathUtils.random(bottom_y + 100, top_y - 100),
                    this, moleculeInfo);
            freeMolecules.add(molecule);
        }
    }

    /**
     *
     * @return molekule koje lete naokolo
     */
    public Array<Molecule> getFreeMolecules(){
        return freeMolecules;
    }

    /**
     * Dodaje u popis molekula reakcije i mice iz slobodnih molekula
     * @param a molekula koja je u kutiji
     */
    public void addMoleculeToReaction(Molecule a){
        if(!reactionArea.getReactionMolecules().contains(a, true))
            reactionArea.addMoleculeToReaction(a);
        freeMolecules.removeValue(a, true);
    }

    /**
     * Dodaje u popis slobodnih molekula i mice iz liste molekula reakcije
     * @param a molekula koja je izvadjena iz kutije
     */
    public void removeMoleculeFromReaction(Molecule a){
        reactionArea.removeMoleculeFromReaction(a);
        if(!freeMolecules.contains(a,true))
            freeMolecules.add(a);
    }

    public void updateFreeMolecules(Map<Integer, Integer> resultMolecules,
                                    JsonValue molecules) {
        for (Map.Entry<Integer, Integer> entry : resultMolecules.entrySet()) {
            JsonValue moleculeInfo = molecules.get(entry.getKey());
            Molecule molecule = new Molecule(
                    reactionArea.getReactionMolecules().first().getX(),
                    reactionArea.getReactionMolecules().first().getY(),
                    this, moleculeInfo);
            freeMolecules.add(molecule);
            stage.addActor(molecule);
        }
    }

    /**
     * Azuriranje polozaja svih staticnih objekata na ekranu, trenutacno kutije i molekula u kutiji
     * @param xmov
     * @param ymov
     */
    /*public void updateReactionArea(float xmov, float ymov){
        reactionArea.setPosition(reactionArea.getX() - xmov, reactionArea.getY() + ymov);
        for(Molecule molecule : reactionArea.getReactionMolecules()){
            Gdx.app.error("world",""+ reactionArea.getReactionMolecules());
            molecule.setPosition(molecule.getX()-xmov, molecule.getY()+ymov);
        }
    }*/

}
