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

package info.datahelix.apirateslife.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import info.datahelix.apirateslife.utils.SpriteAccessor;

/**
 * Created 5/25/2016
 * @author Adam Torres
 */
public class Splash implements Screen{


    private SpriteBatch batch;
    private Sprite splash;
    private TweenManager tweenManager;
    private final int FADE_DELAY = 2;
    @Override
    public void show() {
        batch = new SpriteBatch();
        splash = new Sprite(new Texture("backgrounds/title.png"));
        splash.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(splash, SpriteAccessor.ALPHA, FADE_DELAY).target(1).repeatYoyo(1, FADE_DELAY).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int i, BaseTween<?> baseTween) {
                ((Game) Gdx.app.getApplicationListener()).getScreen().dispose();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new MainMenu() );
            }
        }).start(tweenManager);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);
        batch.begin();
        splash.draw(batch);
        batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        splash.getTexture().dispose();
    }

}
