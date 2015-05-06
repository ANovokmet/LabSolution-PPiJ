package com.swag.solutions.Objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.swag.solutions.input.ShakeDetector;

/**
 * Created by Branimir on 6.5.2015..
 * Stvoren da se odvoji crtanje GUI-a i sl. od upravljanja količinom energije.
 * Ne znam još hoće li se mijenjati u postotcima ili određenim vrijednostima.
 * Ne znam još hoće li imati fiksnu ili promijenjivu stopu pada/rasta.
 * Ne znam još hoće li energija biti izražena u postotcima ili vrijednostima.
 */
public class EnergyContainer extends Actor {
    float currentEnergy;
    float maxEnergy;
    float dissipationRate; // u sekundi
    static final float SHAKE_INCREASE_RATE = 0.2f;
    ShakeDetector shakeDetector;

    public EnergyContainer(
            float percentFilled, float maxEnergy, float dissipationRate,
            ShakeDetector shakeDetector) {
        this.maxEnergy = (maxEnergy > 0.f) ? maxEnergy : 1.f;

        boolean validPercentage =
                (0.f < percentFilled) && (percentFilled < 1.f);
        currentEnergy = validPercentage ? (maxEnergy * percentFilled) : 0.5f;

        this.dissipationRate = (dissipationRate > 0.f) ? dissipationRate : 5.f;

        this.shakeDetector = shakeDetector;
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

    public boolean isEmpty() {
        return (currentEnergy == 0.f);
    }

    @Override
    public void act(float delta) {
        decreaseEnergyBy(dissipationRate * delta);
        if (shakeDetector.deviceBeingShaken()) {
            increaseEnergyBy(SHAKE_INCREASE_RATE * delta);
        }
    }
}
