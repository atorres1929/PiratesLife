package com.artifexiumgames.apirateslife.main;

import com.artifexiumgames.apirateslife.screens.MainMenu;
import com.artifexiumgames.apirateslife.screens.Splash;
import com.artifexiumgames.apirateslife.utils.Utils;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Main extends Game {

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		if (Utils.DISABLE_SPLASH){
			setScreen(new MainMenu());
		}
		else {
			setScreen(new Splash());
		}
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose() {

	}

}
