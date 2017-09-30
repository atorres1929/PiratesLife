package com.artifexiumgames.apirateslife.screens;

import com.artifexiumgames.apirateslife.utils.SpriteAccessor;
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

/**
 * Created by Adam on 5/25/2016.
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
