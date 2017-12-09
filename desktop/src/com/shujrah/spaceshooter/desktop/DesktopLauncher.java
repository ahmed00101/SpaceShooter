package com.shujrah.spaceshooter.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.shujrah.spaceshooter.MyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = 330;
		config.height = 660;
		config.title = "Waqar shujrah's Space Shooter";
		//config.foregroundFPS = 60;
		//config.fullscreen = true;
		config.x = 0;
		config.y = -10;
		new LwjglApplication(new MyGame(), config);
	}
}
