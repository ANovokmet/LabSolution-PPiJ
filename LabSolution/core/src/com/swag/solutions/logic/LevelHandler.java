package com.swag.solutions.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.swag.solutions.Objects.HudElement;
import com.swag.solutions.Objects.ReactionArea;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Branimir on 12.5.2015..
 */
public class LevelHandler {
    private int currentLevel;
    private JsonValue levels;
    private Map<Integer,Integer> neededReactants;
    private Map<Integer,Integer> resultMolecule;
    private JsonValue molecules;

    private EnergyContainer energyContainer;
    private HudElement hudElement;
    private ReactionArea reactionArea;

    public LevelHandler(EnergyContainer enContainer, HudElement hudElement,
                        ReactionArea reactionArea) {
        this.currentLevel = 0;
        this.energyContainer = enContainer;
        this.hudElement = hudElement;
        this.reactionArea = reactionArea;

        JsonReader jsonReader = new JsonReader();
        FileHandle levelsFile = Gdx.files.internal("data/levels.json");
        levels = jsonReader.parse(levelsFile.readString());

        FileHandle moleculesFile = Gdx.files.internal("data/all.json");
        molecules = jsonReader.parse(moleculesFile.readString());

        neededReactants = new HashMap<>();
        resultMolecule = new HashMap<>();

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

    public boolean isReactionFulfilled(){
        Map<Integer,Integer> currentReactants =
                reactionArea.getCurrentReactants();
        return currentReactants.equals(neededReactants);
    }
}
