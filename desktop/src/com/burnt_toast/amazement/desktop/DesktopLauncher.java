package com.burnt_toast.amazement.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.burnt_toast.amazement.MainFrame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 10 * 50;
		config.height = 16 * 50;
		new LwjglApplication(new MainFrame(), config);
	}
}
