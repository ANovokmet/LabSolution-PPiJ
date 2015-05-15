package com.swag.solutions.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.swag.solutions.logic.EnergyContainer;

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

    private OrthographicCamera camera;
    Table formulaTable;
    EnergyContainer energyContainer;

    float BAR_HEIGHT_PERCENTAGE = 0.75f;

    float FORMULA_POSITION_Y_PERCENTAGE = 0.75f;
    float HEIGHT_OF_EDGE = 18;

    static BitmapFont titleFont;
    static BitmapFont textFont;
    static BitmapFont energyFont;
    String moleculeFormula = "";//CILJNA MOLEKULA

    private int BIG_FONT_SIZE;
    private int SMALL_FONT_SIZE;
    private int ENERGY_FONT_SIZE;

    public HudElement(OrthographicCamera camera, EnergyContainer enContainer) {
        setWidth(100);
        setHeight(100);//vlastiti width i height se ne koriste

        this.camera= camera;

        this.BAR_HEIGHT = camera.viewportHeight*BAR_HEIGHT_PERCENTAGE;
        this.BAR_Y = (camera.viewportHeight-BAR_HEIGHT)/2-HEIGHT_OF_EDGE;

        BIG_FONT_SIZE = (int)(96 * camera.viewportWidth/480f);
        SMALL_FONT_SIZE = (int)(48 * camera.viewportWidth/480f);

        ENERGY_FONT_SIZE = (int)(24 * camera.viewportWidth/480f);


        createFonts();
        //formulaTable = renderString(moleculeFormula);
        energyContainer = enContainer;
    }

    private void createFonts() {
        FileHandle fontFile = Gdx.files.internal("coolvetica.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = SMALL_FONT_SIZE;
        textFont = generator.generateFont(parameter);
        parameter.size = BIG_FONT_SIZE;
        titleFont = generator.generateFont(parameter);

        /*fontFile = Gdx.files.internal("04B_30__.TTF");
        generator = new FreeTypeFontGenerator(fontFile);
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();*/

        parameter.size = ENERGY_FONT_SIZE;
        energyFont = generator.generateFont(parameter);
        generator.dispose();

        energyFont.setColor(255f/255f, 255f/255f, 255f/255f, 1f);
    }

    public void setMoleculeTitle(String formula){
        moleculeFormula = formula;
        formulaTable = renderString(moleculeFormula);
    }

    public void act(float delta){
        super.act(delta);
        setX(camera.position.x);
        setY(camera.position.y);
        //formulaTable.act(delta);
    }


    public void draw(Batch batch, float alpha){
        float percentFilled = energyContainer.percentFilled();
        float neededPercentFilled = energyContainer.neededEnergyPercentage();

        // prazna epruveta od energije
        batch.draw(emptybot, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                HEIGHT_OF_EDGE, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                emptybot.getWidth(), emptybot.getHeight(), false, false);
        batch.draw(emptymid, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+HEIGHT_OF_EDGE, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                BAR_HEIGHT, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                emptymid.getWidth(), emptymid.getHeight(), false, false);
        batch.draw(emptytop, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+BAR_HEIGHT+HEIGHT_OF_EDGE, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                HEIGHT_OF_EDGE, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                emptytop.getWidth(), emptytop.getHeight(), false, false);

        // puni dio epruvete od energije
        batch.draw(filledbot, BAR_X + getX() - camera.viewportWidth / 2, BAR_Y + getY() - camera.viewportHeight / 2, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                HEIGHT_OF_EDGE, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                filledbot.getWidth(), filledbot.getHeight(), false, false);
        batch.draw(filledmid, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+HEIGHT_OF_EDGE, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                BAR_HEIGHT* percentFilled, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                filledmid.getWidth(), filledmid.getHeight(), false, false);
        batch.draw(filledtop, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+BAR_HEIGHT* percentFilled +HEIGHT_OF_EDGE, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                HEIGHT_OF_EDGE, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                filledtop.getWidth(), filledtop.getHeight(), false, false);

        // oznaka koja pokazuje količinu energije potrebne za zadanu reakciju
        batch.draw(target, BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+BAR_HEIGHT*neededPercentFilled+HEIGHT_OF_EDGE, this.getOriginX(), this.getOriginY(), BAR_WIDTH,
                9, this.getScaleX(), this.getScaleY(), 0, 0, 0,
                target.getWidth(), target.getHeight(), false, false);

        // količina trenutne energije (broj)
        energyFont.draw(batch, (int)(energyContainer.getCurrentEnergy())+"", BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+BAR_HEIGHT* percentFilled +SMALL_FONT_SIZE);
        //titleFont.draw(batch, moleculeFormula, getX()-80, getY()+camera.viewportWidth/2+40);

        // KJ/MOL na dnu
        energyFont.draw(batch, "KJ/MOL", BAR_X + getX() - camera.viewportWidth / 2, BAR_Y + getY() - camera.viewportHeight / 2 - SMALL_FONT_SIZE);

        // zadana reakcija
        formulaTable.setPosition(getX(), getY() + camera.viewportHeight / 2 * FORMULA_POSITION_Y_PERCENTAGE);
        formulaTable.draw(batch,alpha);
    }

    // 0: sredina ekrana, +-1 vrh i dno

    static Skin skin = new Skin();
    private static final float PADDING = 50;
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
