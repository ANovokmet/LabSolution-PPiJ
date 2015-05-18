package com.swag.solutions.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.TimeUtils;
import com.swag.solutions.LabGame;
import com.swag.solutions.hud.HintButton;
import com.swag.solutions.hud.HudElement;
import com.swag.solutions.screens.GameScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by Branimir on 12.5.2015..
 * Učitava levele i proslijeđuje bitne informacije ostalim objektima ili
 * objekti te informacije uzimaju od njega.
 */
public class LevelHandler extends Observable{
    private GameScreen gameScreen;

    private int currentLevel;
    private JsonValue levels;
    private Map<Integer, Integer> neededReactants;
    private Map<Integer, Integer> resultMolecules;
    private JsonValue molecules;

    private EnergyContainer energyContainer;
    private HudElement hudElement;
    private Solution solution;
    private HintButton hintButton;

    private TimeUtils time;
    private long moment_start, moment_end;

    public LevelHandler(EnergyContainer enContainer, HudElement hudElement, HintButton hintButton, GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.currentLevel = 0;
        this.energyContainer = enContainer;
        this.hudElement = hudElement;
        this.solution = null;
        this.hintButton = hintButton;

        time = new TimeUtils();

        JsonReader jsonReader = new JsonReader();
        FileHandle levelsFile = Gdx.files.internal("data/levels.json");
        levels = jsonReader.parse(levelsFile.readString());

        FileHandle moleculesFile = Gdx.files.internal("data/all.json");
        molecules = jsonReader.parse(moleculesFile.readString());

        neededReactants = new HashMap<Integer, Integer>();
        resultMolecules = new HashMap<Integer, Integer>();
    }

    public void setSolution(Solution solution) {
        this.solution = solution;
        solution.generateMolecules(molecules);
    }

    public void loadLevel() {
        JsonValue level = levels.get(currentLevel);
        JsonValue reactants = level.get("reactants");
        neededReactants.clear();
        for (int i = 0; i < reactants.size; ++i) {
            JsonValue reactant = reactants.get(i);
            neededReactants.put(
                    reactant.get("id").asInt(),
                    reactant.get("quantity").asInt());
        }

        JsonValue result;
        JsonValue results = level.get("results");
        resultMolecules.clear();
        for(int i=0;i<results.size;i++){
            result = results.get(i);
            resultMolecules.put(result.get("id").asInt(), result.get("quantity").asInt());
        }
        result = results.get(0);

        energyContainer.setNeededEnergy(level.get("energy_needed").asFloat());

        JsonValue molecule = molecules.get(result.get("id").asInt());
        hudElement.setMoleculeTitle(molecule.get("formula").asString());

        hintButton.loadHints(level.get("hints"), level.get("hint_free"));

        solution.ensureReactionSatisfiability(neededReactants, molecules);

        moment_start = time.millis();
    }

    public Map<Integer, Integer> getNeededReactants() {
        return neededReactants;
    }

    public void nextLevel() {
        energyContainer.useNeededEnergy();
        energyContainer.increaseEnergyBy(
                levels.get(currentLevel).get("energy_released").asInt());

        solution.addResultMolecules(resultMolecules, molecules);
        if(currentLevel==0)
            LabGame.googleServices.unlockAchievement("CgkIrYPb-McCEAIQAw");

        moment_end = time.millis();

        if(currentLevel>=5){
            if((moment_end-moment_start)<30*1000){
                LabGame.googleServices.unlockAchievement("CgkIrYPb-McCEAIQBQ");
            }
            else if((moment_end-moment_start)<60*1000){
                LabGame.googleServices.unlockAchievement("CgkIrYPb-McCEAIQBA");
            }
        }

        ++currentLevel;
        super.setChanged();
        super.notifyObservers();
        if (currentLevel >= levels.size) {
            gameScreen.gameOver(true);
        }
        else {
            loadLevel();
        }
    }
}