package com.swag.solutions.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.swag.solutions.LabGame;
import com.swag.solutions.MainGame;

public class AndroidLauncher extends AndroidApplication {
    protected LabGame labGame;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useAccelerometer = true;

        labGame = new LabGame();
		initialize(labGame, config);
	}

    @Override
    protected void onPause(){
        super.onPause();

    }

    @Override
    public void onBackPressed() {
        labGame.pause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        labGame.pause();
        return true;
    }

}
