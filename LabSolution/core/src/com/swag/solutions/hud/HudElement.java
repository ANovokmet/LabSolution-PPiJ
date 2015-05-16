package com.swag.solutions.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
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

    Texture emptybot;//18x9
    Texture emptymid;//18x18
    Texture emptytop;
    Texture filledbot;
    Texture filledmid;
    Texture filledtop;
    Texture target;

    float BAR_X = 16;
    float BAR_Y = 16;
    float BAR_WIDTH;
    float BAR_HEIGHT;

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

    private int ENERGY_LABEL_OFFSET;

    AssetManager manager;

    public HudElement(OrthographicCamera camera, EnergyContainer enContainer, AssetManager manager) {
        setWidth(0);
        setHeight(0);
        this.manager = manager;

        emptybot = manager.get("bar_empty_bot.png", Texture.class);
        emptymid = manager.get("bar_empty_mid.png", Texture.class);
        emptytop = manager.get("bar_empty_top.png", Texture.class);
        filledbot = manager.get("bar_fill_bot.png", Texture.class);
        filledmid = manager.get("bar_fill_mid.png", Texture.class);
        filledtop = manager.get("bar_fill_top.png", Texture.class);
        target = manager.get("barRed_verticalMid.png", Texture.class);
        emptytop.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        emptybot.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        this.camera= camera;

        setX(camera.position.x);
        setY(camera.position.y);

        this.BAR_WIDTH = 36*camera.viewportWidth/480f;
        this.HEIGHT_OF_EDGE = BAR_WIDTH/2;

        this.BAR_HEIGHT = camera.viewportHeight*BAR_HEIGHT_PERCENTAGE;
        this.BAR_Y = (camera.viewportHeight-BAR_HEIGHT)/2-HEIGHT_OF_EDGE;

        ENERGY_LABEL_OFFSET = (int)(48 * camera.viewportWidth/480f);
        createFonts();
        energyContainer = enContainer;
    }

    private void createFonts() {
        textFont = manager.get("smallfont.ttf", BitmapFont.class);
        titleFont = manager.get("bigfont.ttf", BitmapFont.class);
        energyFont = manager.get("energyfont.ttf", BitmapFont.class);
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
        energyFont.draw(batch, (int)(energyContainer.getCurrentEnergy())+"", BAR_X+getX()-camera.viewportWidth/2, BAR_Y+getY()-camera.viewportHeight/2+BAR_HEIGHT* percentFilled +ENERGY_LABEL_OFFSET);
        //titleFont.draw(batch, moleculeFormula, getX()-80, getY()+camera.viewportWidth/2+40);

        // KJ/MOL na dnu
        energyFont.draw(batch, "KJ/MOL", BAR_X + getX() - camera.viewportWidth / 2, BAR_Y + getY() - camera.viewportHeight / 2 - ENERGY_LABEL_OFFSET);

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
