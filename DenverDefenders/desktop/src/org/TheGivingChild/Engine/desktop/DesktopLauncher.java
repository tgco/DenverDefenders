package org.TheGivingChild.Engine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.TheGivingChild.Engine.TGC_Engine;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Denver Defenders (in development)";
		config.width = 1024;
		config.height = 600;
		new LwjglApplication(new TGC_Engine(), config);
	}
}
