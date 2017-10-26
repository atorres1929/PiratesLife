package info.datahelix.apirateslife.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import info.datahelix.apirateslife.main.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "A Pirate's Life";
		new LwjglApplication(new Main(), config);
	}
}
