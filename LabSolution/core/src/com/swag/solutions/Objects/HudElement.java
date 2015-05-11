package com.swag.solutions.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
/**
 * Created by Ante on 29.4.2015..
 */
public class HudElement extends Actor {

    Texture emptybot = new Texture("barBack_verticalBottom.png");//18x9
    Texture emptymid = new Texture("barBack_verticalMid.png");//18x18
    Texture emptytop = new Texture("barBack_verticalTop.png");
    Texture filledbot = new Texture("barGreen_verticalBottom.png");
    Texture filledmid = new Texture("barGreen_verticalMid.png");
    Texture filledtop = new Texture("barGreen_verticalTop.png");
    Texture target = new Texture("barRed_verticalMid.png");

    float BAR_X = 16;
    float BAR_Y = 16;
    float BAR_WIDTH = 32;
    float BAR_HEIGHT = 512;

    float percentFilled = 0;

    float maxEnergy = 800;
    float currentEnergy = 0;

    float targetPercentFilled = 0.7f;

    private Professor professor;
    private OrthographicCamera camera;
    Table formulaTable;
    EnergyContainer energyContainer;

    float BAR_HEIGHT_PERCENTAGE = 0.75f;

    float FORMULA_POSITION_Y_PERCENTAGE = 0.75f;
    float HEIGHT_OF_EDGE = 18;

    static BitmapFont titleFont;
    static BitmapFont textFont;
    String moleculeFormula = "";//CILJNA MOLEKULA

    public HudElement(OrthographicCamera camera, EnergyContainer enContainer) {
        setWidth(100);
        setHeight(100);//vlastiti width i height se ne koriste

        this.camera= camera;

        this.BAR_HEIGHT = camera.viewportHeight*BAR_HEIGHT_PERCENTAGE;
        this.BAR_Y = (camera.viewportHeight-BAR_HEIGHT)/2-HEIGHT_OF_EDGE;

        createFonts();
        //formulaTable = renderString(moleculeFormula);
        energyContainer = enContainer;
        professor = new Professor(camera);

    }

    private void createFonts() {
        FileHandle fontFile = Gdx.files.internal("04B_30__.TTF");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        textFont = generator.generateFont(parameter);
        parameter.size = 48;
        titleFont = generator.generateFont(parameter);
        generator.dispose();

        textFont.setColor(1f, 0f, 0f, 1f);
    }

    public void setMoleculeTitle(String formula){
        moleculeFormula = formula;
        formulaTable = renderString(moleculeFormula);
    }

    public void act(float delta){
        super.act(delta);
        setX(camera.position.x);
        setY(camera.position.y);
        //percentFilled+=0.0005;//test za "animaciju"
        percentFilled = energyContainer.percentFilled();
        professor.act(delta);
        //formulaTable.act(delta);
    }

    public void tellHint(String hint){
        professor.tellHint(hint);
    }




    public void draw(Batch batch, float alpha){
        batch.draw(emptybot, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                HEIGHT_OF_EDGE, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                emptybot.getWidth(), emptybot.getHeight(), false, false);
        batch.draw(emptymid, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+HEIGHT_OF_EDGE, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                BAR_HEIGHT, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                emptymid.getWidth(), emptymid.getHeight(), false, false);
        batch.draw(emptytop, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+BAR_HEIGHT+HEIGHT_OF_EDGE, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                HEIGHT_OF_EDGE, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                emptytop.getWidth(), emptytop.getHeight(), false, false);


        batch.draw(filledbot, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                HEIGHT_OF_EDGE, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                filledbot.getWidth(), filledbot.getHeight(), false, false);
        batch.draw(filledmid, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+HEIGHT_OF_EDGE, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                BAR_HEIGHT*percentFilled, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                filledmid.getWidth(), filledmid.getHeight(), false, false);
        batch.draw(filledtop, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+BAR_HEIGHT*percentFilled+HEIGHT_OF_EDGE, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                HEIGHT_OF_EDGE, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                filledtop.getWidth(), filledtop.getHeight(), false, false);

        batch.draw(target, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+BAR_HEIGHT*targetPercentFilled+HEIGHT_OF_EDGE, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                9, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                target.getWidth(), target.getHeight(), false, false);

        textFont.draw(batch, (int)(energyContainer.currentEnergy)+"", BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+BAR_HEIGHT*percentFilled+18);
        //titleFont.draw(batch, moleculeFormula, getX()-80, getY()+camera.viewportWidth/2+40);
        textFont.draw(batch, "KJ/MOL", 16+getX()-camera.viewportWidth/2, 16+getY()-camera.viewportHeight/2+18);
        //potrebno za subscript brojeva
        formulaTable.setPosition(getX(), getY()+camera.viewportHeight/2*FORMULA_POSITION_Y_PERCENTAGE);
        formulaTable.draw(batch,alpha);

        professor.draw(batch, alpha);

    }

    // 0: sredina ekrana, +-1 vrh i dno

    public void setTargetEnergy(float energy){
        targetPercentFilled = energy/energyContainer.maxEnergy;
    }

    public float getEnergy(){
        return (energyContainer.currentEnergy);
    }

    static Skin skin = new Skin();
    private static final float PADDING = 30;
    public static Table renderString(String string){

        skin.add("normal",titleFont);
        skin.add("subscript",textFont);
        Table table = new Table(skin);
        for (int i = 0; i < string.length(); i++){
            char c = string.charAt(i);
            if(c >= '0' && c <= '9'){
                table.add(c+"","subscript", Color.WHITE).padTop(PADDING);
            }
            else{
                table.add(c+"","normal", Color.WHITE);
            }
            table.add();

        }
        return table;
    }
}
