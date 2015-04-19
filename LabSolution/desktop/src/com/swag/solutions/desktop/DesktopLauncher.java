package com.swag.solutions.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.swag.solutions.LabGame;
import com.swag.solutions.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height=600;
        config.width=480;
		new LwjglApplication(new LabGame(), config);
	}
}
