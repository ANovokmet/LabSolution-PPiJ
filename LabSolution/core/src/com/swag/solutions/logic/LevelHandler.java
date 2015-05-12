package com.swag.solutions.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.swag.solutions.hud.HudElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Branimir on 12.5.2015..
 * Učitava levele i proslijeđuje bitne informacije ostalim objektima ili
 * objekti te informacije uzimaju od njega.
 */
public class LevelHandler {
    private int currentLevel;
    private JsonValue levels;
    private Map<Integer, Integer> neededReactants;
    private Map<Integer, Integer> resultMolecule;
    private JsonValue molecules;

    private EnergyContainer energyContainer;
    private HudElement hudElement;

    public LevelHandler(EnergyContainer enContainer, HudElement hudElement) {
        this.currentLevel = 0;
        this.energyContainer = enContainer;
        this.hudElement = hudElement;

        JsonReader jsonReader = new JsonReader();
        FileHandle levelsFile = Gdx.files.internal("data/levels.json");
        levels = jsonReader.parse(levelsFile.readString());

        FileHandle moleculesFile = Gdx.files.internal("data/all.json");
        molecules = jsonReader.parse(moleculesFile.readString());

        neededReactants = new HashMap<Integer, Integer>();
        resultMolecule = new HashMap<Integer, Integer>();

        loadLevel();
    }

   public void loadLevel() {
       JsonValue level = levels.get(currentLevel);
       JsonValue reactants = level.get("reactants");
       neededReactants.clear();
       //for (int i = 0; i < (levels.size + 1); i++) {
       for (int i = 0; i < reactants.size; ++i) {
           JsonValue reactant = reactants.get(i);
           neededReactants.put(
                   reactant.get("id").asInt(),
                   reactant.get("quantity").asInt());
       }

       JsonValue result = level.get("result");
       resultMolecule.clear();
       resultMolecule.put(
               result.get("id").asInt(), result.get("quantity").asInt());

       energyContainer.setNeededEnergy(level.get("energy_needed").asFloat());

       JsonValue molecule = molecules.get(result.get("id").asInt());
       hudElement.setMoleculeTitle(molecule.get("formula").asString());
    }

    public JsonValue getMolecules() {
        return molecules;
    }

    public Map<Integer, Integer> getNeededReactants() {
        return neededReactants;
    }

    public void nextLevel() {
        energyContainer.useNeededEnergy();
        energyContainer.increaseEnergyBy(
                levels.get(currentLevel).get("energy_released").asInt());
        ++currentLevel;
        if (currentLevel >= levels.size) {
            // bacit će exception negdje u loadLeve()
            // treba stavit game over screen ili nešto
        }
        loadLevel();
    }
}