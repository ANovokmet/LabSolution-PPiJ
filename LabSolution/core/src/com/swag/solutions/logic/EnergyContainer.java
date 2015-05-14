package com.swag.solutions.logic;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.swag.solutions.LabGame;
import com.swag.solutions.Screens.EndScreen;
import com.swag.solutions.input.ShakeDetector;

/**
 * Created by Branimir on 6.5.2015..
 * Stvoren da se odvoji crtanje GUI-a i sl. od upravljanja količinom energije.
 * Maksimalna i trenutna energija izražene u određenim vrijednostima.
 * Energija se mijenja u određenim vrijednostima.
 * Stopa pada i rasta trešnjom je izražena u fiksnim postotcima.
 */
public class EnergyContainer extends Actor {
    private LabGame game;
    private float currentEnergy;
    private float maxEnergy;
    private float neededEnergy;

    private static final float SHAKE_INCREASE_RATE = 0.2f;
    private static final float DISSIPATION_RATE = 0.5f;
    private static final float START_PERCENT_FILLED = 0.8f;
    private ShakeDetector shakeDetector;

    public EnergyContainer(float maxEnergy, ShakeDetector shakeDetector, LabGame game) {
        this.maxEnergy = (maxEnergy > 0.f) ? maxEnergy : 1000.f;
        currentEnergy = this.maxEnergy * START_PERCENT_FILLED;
        this.shakeDetector = shakeDetector;
        this.game = game;
    }

    public float getCurrentEnergy() {
        return currentEnergy;
    }

    public void setNeededEnergy(float neededEnergy) {
        this.neededEnergy = neededEnergy;
    }

    public float neededEnergyPercentage() {
        return neededEnergy / maxEnergy;
    }

    public void increaseEnergyBy(float energyAmount) {
        currentEnergy += energyAmount;
        if (currentEnergy > maxEnergy) {
            currentEnergy = maxEnergy;
        }
    }

    public void decreaseEnergyBy(float energyAmount) {
        currentEnergy -= energyAmount;
        if (currentEnergy < 0.f) {
            currentEnergy = 0.f;
        }
    }

    public float percentFilled() {
        return currentEnergy / maxEnergy;
    }

    public boolean enoughEnergyForReaction() {
        return currentEnergy >= neededEnergy;
    }

    public void useNeededEnergy() {
        decreaseEnergyBy(neededEnergy);
    }

    @Override
    public void act(float delta) {
        decreaseEnergyBy(maxEnergy * DISSIPATION_RATE * delta);
        if (shakeDetector.deviceBeingShaken()) {
            increaseEnergyBy(maxEnergy * SHAKE_INCREASE_RATE * delta);
        }
        if (currentEnergy == 0.f) {
            game.setScreen(new EndScreen(game, false));
        }
    }
}
