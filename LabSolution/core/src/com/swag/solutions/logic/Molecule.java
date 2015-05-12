package com.swag.solutions.logic;

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

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Created by Ante on 15.4.2015..
 */
public class Molecule extends Actor {
    Texture texture;
    Solution solution;
    Circle bounds;
    Rectangle reactionAreaBounds;

    float rotacija = 0;  //posebni param za rotaciju zbog načina izračuna odmaka translacije
    float brzina_rotacije = 0;

    boolean van_kutije = true;  //bitno za update pananja i random pomicanja
    boolean korisnik_mice = false;
    boolean dira_se = false;

    public Vector2 movement;

    static final float TOUCH_SCALE = 2f;
    static final float TOUCH_SCALETIME = 0.1f;

    public JsonValue params;

    private final static Sound moleculePickUpSound = Gdx.audio.newSound(
            Gdx.files.internal("sounds/molecule_pick_up.wav"));
    private final static Sound moleculePutDownSound = Gdx.audio.newSound(
            Gdx.files.internal("sounds/molecule_put_down.wav"));

    public Molecule(float x, float y, Solution solution, JsonValue params){
        setX(x);
        setY(y);
        this.params = params;
        setWidth(params.get("width").asInt());
        setHeight(params.get("height").asInt());
        texture = new Texture(params.get("path").asString());
        this.solution = solution;
        setOrigin(getWidth()/2,getHeight()/2);
        brzina_rotacije=MathUtils.random(-15,15);
        movement = new Vector2(MathUtils.random((float)-2,2), MathUtils.random((float)-2,2));

        final Molecule p = this;

        this.addListener(new DragListener() {

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
                dropMolecule();
            }

            public void touchDragged(InputEvent event, float x, float y, int pointer){
                Molecule.this.moveBy(x - Molecule.this.getWidth() / 2, y - Molecule.this.getHeight() / 2);
            }

        });

        bounds = new Circle(getX()+getWidth()/2, getY()+getHeight()/2, getWidth()/2);
    }

    private void dropMolecule(){
        if (Intersector.overlaps(bounds, reactionAreaBounds)) {
            van_kutije = false;
            izracunajRelPolozaj();
            solution.addMoleculeToReaction(this);
        } else {
            van_kutije = true;
            solution.removeMoleculeFromReaction(this);
        }
    }

    public int getId(){
        return params.get("id").asInt();
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
        /*if(korisnik_mice) {
            if (Intersector.overlaps(bounds, reactionAreaBounds)) {
                van_kutije = false;
                izracunajRelPolozaj();
                solution.addMoleculeToReaction(this);
            } else {
                van_kutije = true;
                solution.removeMoleculeFromReaction(this);
            }
        }*/

        //odbijanje
        if (this.getX() < solution.left_x || this.getX() > solution.right_x)
            this.movement.x *= -1;
        if (this.getY() < solution.bottom_y || this.getY() > solution.top_y)
            this.movement.y *= -1;

        if (van_kutije && !dira_se) {
            setX(getX() + movement.x);
            setY(getY() + movement.y);
        }
        else if(!dira_se){//unutar kutije
            setX(reactionAreaBounds.x+relPolozaj.x);
            setY(reactionAreaBounds.y+relPolozaj.y);
        }

        rotacija+=brzina_rotacije*delta;
        //setRotation(getRotation()+10*delta);
        //Gdx.app.error("brojevi2",""+getOriginX()+" "+getOriginY()+" "+getX()+" "+getY()+" "+getRotation());
    }

    Vector2 relPolozaj = new Vector2();
    private void izracunajRelPolozaj(){
        relPolozaj.set(getX()- reactionAreaBounds.x, getY()- reactionAreaBounds.y);
    }

    @Override
    public Actor hit(float x, float y, boolean touchable){

        return super.hit( x,  y,  touchable);
    }

    public void setCenter(int x, int y){
        super.setPosition(x-getWidth()/2, y-getHeight()/2);
    }

    public void setReactionAreaBounds(Rectangle bounds){
        reactionAreaBounds = bounds;
    }

    @Override
    public String toString(){
        return ""+getX()+" "+getY();
    }

    public boolean isMoving(){
        return dira_se;
    }
}
