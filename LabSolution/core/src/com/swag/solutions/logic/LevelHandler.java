package com.swag.solutions.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.swag.solutions.LabGame;
import com.swag.solutions.hud.HintButton;
import com.swag.solutions.hud.HudElement;
import com.swag.solutions.Screens.EndScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

/**
 * Created by Branimir on 12.5.2015..
 * Učitava levele i proslijeđuje bitne informacije ostalim objektima ili
 * objekti te informacije uzimaju od njega.
 */
public class LevelHandler extends Observable{
    private LabGame game;

    private int currentLevel;
    private JsonValue levels;
    private Map<Integer, Integer> neededReactants;
    private Map<Integer, Integer> resultMolecules;
    private JsonValue molecules;

    private EnergyContainer energyContainer;
    private HudElement hudElement;
    private Solution solution;
    private HintButton hintButton;

    public LevelHandler(EnergyContainer enContainer, HudElement hudElement, HintButton hintButton, LabGame game) {
        this.game = game;
        this.currentLevel = 0;
        this.energyContainer = enContainer;
        this.hudElement = hudElement;
        this.solution = null;
        this.hintButton = hintButton;

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
        //for (int i = 0; i < (levels.size + 1); i++) {
        for (int i = 0; i < reactants.size; ++i) {
            JsonValue reactant = reactants.get(i);
            neededReactants.put(
                    reactant.get("id").asInt(),
                    reactant.get("quantity").asInt());
        }

        JsonValue result = level.get("results").get(0);  //vise produkata, prvi je glavni
        resultMolecules.clear();
        resultMolecules.put(
                result.get("id").asInt(), result.get("quantity").asInt());

        energyContainer.setNeededEnergy(level.get("energy_needed").asFloat());

        JsonValue molecule = molecules.get(result.get("id").asInt());
        hudElement.setMoleculeTitle(molecule.get("formula").asString());

        hintButton.loadHints(level.get("hints"), level.get("hint_free"));

        solution.ensureReactionSatisfiability(neededReactants, molecules);
    }

    public Map<Integer, Integer> getNeededReactants() {
        return neededReactants;
    }

    public void nextLevel() {
        energyContainer.useNeededEnergy();
        energyContainer.increaseEnergyBy(
                levels.get(currentLevel).get("energy_released").asInt());

        solution.addResultMolecules(resultMolecules, molecules);

        ++currentLevel;
        if (currentLevel >= levels.size) {
            game.setScreen(new EndScreen(game, true));
        }
        else {
            super.setChanged();
            super.notifyObservers();
            loadLevel();
        }
    }
}