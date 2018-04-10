/*
 *     Copyright (C) 2017 Adam Torres
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package info.datahelix.apirateslife.main;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import info.datahelix.apirateslife.entity.Player;
import info.datahelix.apirateslife.entity.Ships.Player_Ships.Brigantine;
import info.datahelix.apirateslife.screens.BattleWorld;
import info.datahelix.apirateslife.screens.Splash;
import info.datahelix.apirateslife.utils.Utils;

public class Main extends Game {

	@Override
	public void create () {
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		if (Utils.DISABLE_SPLASH){
			setScreen(new BattleWorld(new Player("Player",
					new Brigantine("Player Ship", BattleWorld.WORLD_SIZE/2, BattleWorld.WORLD_SIZE/2, 0))));
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
