package com.swag.solutions.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.math.Interpolation.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.JsonValue;
import com.swag.solutions.World;

/**
 * Created by Ante on 15.4.2015..
 */
public class Molecule extends Actor {
    Texture texture;
    World world;
    Circle bounds;
    Rectangle reaction_area;
    float rotacija=0;  //posebni param za rotaciju zbog načina izračuna odmaka translacije
    float brzina_rotacije=0;

    boolean van_kutije=true;  //bitno za update pananja i random pomicanja
    boolean korisnik_mice=false;
    boolean dira_se = false;

    public Vector2 movement;

    static float TOUCH_SCALE = 2f;
    static float TOUCH_SCALETIME = 0.1f;

    public JsonValue params;
    private int id;

    public Molecule(float x, float y, World world, JsonValue params){
        setX(x);
        setY(y);
        this.params = params;
        setWidth(params.get("width").asInt());
        setHeight(params.get("height").asInt());
        id = (params.get("id").asInt());
        texture = new Texture(params.get("path").asString());
        this.world = world;
        setOrigin(getWidth()/2,getHeight()/2);
        brzina_rotacije=MathUtils.random(-15,15);
        movement = new Vector2(MathUtils.random((float)-2,2), MathUtils.random((float)-2,2));

        this.addListener(new DragListener() {
            public void drag(InputEvent event, float x, float y, int pointer) {
                korisnik_mice=true;
                Molecule.this.moveBy(x -  Molecule.this.getWidth() / 2, y -  Molecule.this.getHeight() / 2);
            }

            public void dragStop(InputEvent event, float x, float y, int pointer){
                korisnik_mice=false;
            }
        });
        final Molecule p = this;

        final Sound moleculePickUpSound =
                Gdx.audio.newSound(
                        Gdx.files.internal("sounds/molecule_pick_up.wav"));
        final Sound moleculePutDownSound =
                Gdx.audio.newSound(
                        Gdx.files.internal("sounds/molecule_put_down.wav"));

        this.addListener(new ClickListener(){
            public boolean touchDown(InputEvent event,float x,float y,int pointer,int button){
                dira_se = true;
                Gdx.app.error("touchDown", "");
                p.addAction(scaleTo(TOUCH_SCALE, TOUCH_SCALE, TOUCH_SCALETIME));
                moleculePutDownSound.play();
                return true;
            }

            public void touchUp(InputEvent event,float x,float y,int pointer,int button){
                dira_se = false;
                p.addAction(scaleTo(1, 1, TOUCH_SCALETIME));
                Gdx.app.error("touchUp", "");
                moleculePickUpSound.play();
            }
        });
        bounds = new Circle(getX()+getWidth()/2, getY()+getHeight()/2, getWidth()/2);
    }

    public int getId(){
        return id;
    }

    @Override
    public void draw(Batch batch, float alpha){

        batch.draw(texture, getX(), getY(), this.getOriginX(), this.getOriginY(), this.getWidth(),
                this.getHeight(), this.getScaleX(), this.getScaleY(), rotacija, 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
    }

    @Override
    public void act(float delta){
        super.act(delta);
        bounds.set(getX()+getWidth()/2, getY()+getHeight()/2, getWidth()/2);
        if(korisnik_mice) {
            if (Intersector.overlaps(bounds, reaction_area)) {
                van_kutije = false;
                izracunajRelPolozaj();
                world.addMoleculeToReaction(this);
            } else {
                van_kutije = true;
                world.removeMoleculeFromReaction(this);
            }
        }

        //odbijanje
        if (this.getX() < world.left_x || this.getX() > world.right_x)
            this.movement.x *= -1;
        if (this.getY() < world.bottom_y || this.getY() > world.top_y)
            this.movement.y *= -1;

        if (van_kutije && !dira_se) {
            setX(getX() + movement.x);
            setY(getY() + movement.y);
        }
        else if(!dira_se){//unutar kutije
            setX(reaction_area.x+relPolozaj.x);
            setY(reaction_area.y+relPolozaj.y);
        }

        rotacija+=brzina_rotacije*delta;
        //setRotation(getRotation()+10*delta);
        //Gdx.app.error("brojevi2",""+getOriginX()+" "+getOriginY()+" "+getX()+" "+getY()+" "+getRotation());
    }

    Vector2 relPolozaj = new Vector2();
    private void izracunajRelPolozaj(){
        relPolozaj.set(getX()-reaction_area.x, getY()-reaction_area.y);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable){

        return super.hit( x,  y,  touchable);
    }

    public void setCenter(int x, int y){
        super.setPosition(x-getWidth()/2, y-getHeight()/2);
    }


    public void setReactionAreaBounds(Rectangle bounds){
        reaction_area = bounds;
    }

    @Override
    public String toString(){
        return ""+getX()+" "+getY();
    }
}
