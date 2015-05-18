package com.swag.solutions.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.swag.solutions.LabGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height=800;
        config.width=480;
		new LwjglApplication(new LabGame(new DesktopGoogleServices()), config);
	}
}
