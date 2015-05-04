package com.swag.solutions.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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

    static float BAR_X = 16;
    static float BAR_Y = 16;
    static float BAR_WIDTH = 32;
    static float BAR_HEIGHT = 512;
    float percentFilled = 0;
    float maxEnergy = 200;
    float currentEnergy = 0;
    float targetPercentFilled = 0.7f;

    private OrthographicCamera camera;
    Table formulaTable;

    public HudElement (OrthographicCamera camera){
        setWidth(100);
        setHeight(100);
        this.camera= camera;
        createFonts();
        formulaTable = renderString(moleculeFormula);
    }


    static BitmapFont titleFont;
    static BitmapFont textFont;
    String moleculeFormula = "H2O2Ar2";//CILJNA MOLEKULA

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
    }

    public void act(float delta){
        super.act(delta);
        setX(camera.position.x);
        setY(camera.position.y);
        //percentFilled+=0.0005;//test za "animaciju"
    }

    public void draw(Batch batch, float alpha){
        batch.draw(emptybot, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                18, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                emptybot.getWidth(), emptybot.getHeight(), false, false);
        batch.draw(emptymid, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+emptybot.getHeight(), this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                BAR_HEIGHT, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                emptymid.getWidth(), emptymid.getHeight(), false, false);
        batch.draw(emptytop, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+BAR_HEIGHT+emptybot.getHeight(), this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                18, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                emptytop.getWidth(), emptytop.getHeight(), false, false);


        batch.draw(filledbot, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                18, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                filledbot.getWidth(), filledbot.getHeight(), false, false);
        batch.draw(filledmid, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+filledbot.getHeight(), this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                BAR_HEIGHT*percentFilled, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                filledmid.getWidth(), filledmid.getHeight(), false, false);
        batch.draw(filledtop, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+BAR_HEIGHT*percentFilled+filledbot.getHeight(), this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                18, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                filledtop.getWidth(), filledtop.getHeight(), false, false);

        batch.draw(target, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+BAR_HEIGHT*targetPercentFilled+18, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                9, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                target.getWidth(), target.getHeight(), false, false);

        textFont.draw(batch, (int)(BAR_HEIGHT*percentFilled)+"", BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+BAR_HEIGHT*percentFilled+18);
        //titleFont.draw(batch, moleculeFormula, getX()-80, getY()+camera.viewportWidth/2+40);
        textFont.draw(batch, "KJ/MOL", 16+getX()-camera.viewportWidth/2, 16+getY()-camera.viewportHeight/2+18);

        //potrebno za subscript brojeva
        formulaTable.setPosition(getX(), getY()+camera.viewportWidth/2);
        formulaTable.draw(batch,alpha);
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

    public void setPercentFilled(float percent) {
        percentFilled = percent/5;
        if (percentFilled < 0) {
            percentFilled = 0;
        }
        if (percentFilled > 70) {
            percentFilled = 70;
        }
    }
}
