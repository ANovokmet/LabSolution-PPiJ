package com.swag.solutions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.swag.solutions.Objects.Molecule;
import com.swag.solutions.Objects.ReactionArea;

/**
 * Created by Goran on 19.4.2015..
 */
public class World extends Actor {

    public float left_x, right_x, top_y, bottom_y, size_x, size_y;
    private Array<Molecule> molecules;
    private ReactionArea area;
    Texture texture = new Texture("badlogic.jpg");

    public World(float sizex, float sizey, Array<Molecule> molekule, ReactionArea podrucje){
        float offsetx = Gdx.graphics.getWidth();
        float offsety = Gdx.graphics.getHeight();
        size_x=sizex+offsetx;
        size_y=sizey+offsety;
        left_x=-sizex/2;
        right_x=sizex/2+offsetx;
        top_y=sizey/2+offsety;
        bottom_y=-sizey/2;
        molecules=molekule;
        area=podrucje;
    }

    @Override
    public void act(float delta) {
        for(Molecule molecule : molecules){
            Gdx.app.error("world",left_x+" "+right_x+" "+top_y+" "+bottom_y+" "+molecule.getX()+" "+molecule.getX());
            if(molecule.getX()<left_x || molecule.getX()>right_x)
                molecule.movement.x*=-1;
            if(molecule.getY()<bottom_y || molecule.getY()>top_y)
                molecule.movement.y*=-1;
        }
    }

    @Override
    public void draw(Batch batch, float alpha){
        batch.draw(texture, left_x, bottom_y , 10, size_y);
        batch.draw(texture, right_x-10, bottom_y , 10, size_y);
        batch.draw(texture, left_x, top_y-10 , size_x, 10);
        batch.draw(texture, left_x, bottom_y , size_x, 10);
    }

    public void updateReactionArea(float xmov, float ymov){
        area.setPosition(area.getX()-xmov, area.getY()+ymov);
        for(Molecule molecule : molecules){
            if(!molecule.van_kutije){
                molecule.setPosition(molecule.getX()-xmov, molecule.getY()+ymov);
            }
        }
    }
}
