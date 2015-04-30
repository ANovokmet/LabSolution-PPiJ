package com.swag.solutions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.swag.solutions.Objects.Molecule;
import com.swag.solutions.Objects.ReactionArea;

/**
 * Created by Goran on 19.4.2015..
 */
public class World extends Actor {

    public float left_x, right_x, top_y, bottom_y, size_x, size_y;
    private Array<Molecule> free_molecules;
    private ReactionArea area;
    Texture texture = new Texture("badlogic.jpg");

    public World(float sizex, float sizey, ReactionArea podrucje){
        float offsetx = Gdx.graphics.getWidth();
        float offsety = Gdx.graphics.getHeight();
        size_x=sizex+offsetx;
        size_y=sizey+offsety;
        left_x=-sizex/2;
        right_x=sizex/2+offsetx;
        top_y=sizey/2+offsety;
        bottom_y=-sizey/2;
        free_molecules= new Array<Molecule>();
        area=podrucje;
    }

    @Override
    public void act(float delta) {
        Rectangle area_bounds = area.getBounds();
        for(Molecule molecule : free_molecules){
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

    public void generateMolecules(String param){   //string param moze biti neki json, logika za loadanje u game screenu
        Molecule molekula1 = new Molecule(250,250, this);
        Molecule molekula2 = new Molecule(400,400, this);
        free_molecules.add(molekula1);
        free_molecules.add(molekula2);
    }

    /**
     *
     * @return molekule koje lete naokolo
     */
    public Array<Molecule> getFreeMolecules(){
        return free_molecules;
    }

    /**
     * Dodaje u popis molekula reakcije i mice iz slobodnih molekula
     * @param a molekula koja je u kutiji
     */
    public void addMoleculeToReaction(Molecule a){
        if(!area.getReactionMolecules().contains(a,true))
            area.addMoleculeToReaction(a);
        free_molecules.removeValue(a,true);
    }

    /**
     * Dodaje u popis slobodnih molekula i mice iz liste molekula reakcije
     * @param a molekula koja je izvadjena iz kutije
     */
    public void removeMoleculeFromReaction(Molecule a){
        area.removeMoleculeFromReaction(a);
        if(!free_molecules.contains(a,true))
            free_molecules.add(a);
    }

    /**
     * Azuriranje polozaja svih staticnih objekata na ekranu, trenutacno kutije i molekula u kutiji
     * @param xmov
     * @param ymov
     */
    public void updateReactionArea(float xmov, float ymov){
        area.setPosition(area.getX()-xmov, area.getY()+ymov);
        for(Molecule molecule : area.getReactionMolecules()){
            Gdx.app.error("world",""+area.getReactionMolecules());
            molecule.setPosition(molecule.getX()-xmov, molecule.getY()+ymov);
        }
    }

}
