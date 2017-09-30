package com.artifexiumgames.apirateslife.main;

import com.artifexiumgames.apirateslife.screens.Splash;
import com.badlogic.gdx.Game;

/**
 * Created by atorr on 5/24/2016.
 */
public class Main extends Game {

	@Override
	public void create () {
		setScreen(new Splash());
	}

	@Override
	public void render () {
		super.render();
	}

	@Override
	public void dispose() {

	}

}
