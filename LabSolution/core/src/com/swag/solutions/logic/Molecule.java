package com.swag.solutions.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.JsonValue;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;

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
    static float SCREEN_SCALING;

    public JsonValue params;

    private static Sound moleculePickUpSound;
    private static Sound moleculePutDownSound;

    public Molecule(float x, float y, Solution solution, JsonValue params, AssetManager manager){
        SCREEN_SCALING = Gdx.graphics.getWidth()/360f;
        setX(x);
        setY(y);

        if(moleculePickUpSound==null);
            moleculePickUpSound = manager.get("sounds/molecule_pick_up.wav", Sound.class);
        if(moleculePutDownSound==null);
            moleculePutDownSound = manager.get("sounds/molecule_put_down.wav", Sound.class);

        this.params = params;
        setWidth(params.get("width").asInt()*SCREEN_SCALING);
        setHeight(params.get("height").asInt()*SCREEN_SCALING);
        texture = manager.get(params.get("path").asString(), Texture.class);
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
                moleculePutDownSound.play(0.4f);
                return true;
            }

            public void touchUp(InputEvent event,float x,float y,int pointer,int button){
                dira_se = false;
                p.addAction(scaleTo(1, 1, TOUCH_SCALETIME));
                Gdx.app.error("touchUp", "");
                moleculePickUpSound.play(0.4f);
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
        if (this.getX() < solution.left_x || this.getX()+this.getWidth() > solution.right_x)
            this.movement.x *= -1;
        if (this.getY() < solution.bottom_y || this.getY()+getHeight() > solution.top_y)
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

    public boolean isMoved(){
        return dira_se;
    }
}
