package com.artifexiumgames.apirateslife.screens;

import com.artifexiumgames.apirateslife.entity.CannonShot;
import com.artifexiumgames.apirateslife.entity.Player;
import com.artifexiumgames.apirateslife.entity.Ship;
import com.artifexiumgames.apirateslife.item.CannonType;
import com.artifexiumgames.apirateslife.utils.Utils;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Adam on 5/24/2016.
 */
public class MainMenu implements Screen{

    private Stage stage;
    private Skin skin;
    private Table table;
    private TextButton newGame;
    private TextureAtlas atlas;
    private Sprite menuBackground;
    private SpriteBatch batch;

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        atlas = new TextureAtlas("ui/buttons.pack");
        skin = new Skin(atlas);
        table = new Table(skin);
        table.setBounds(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        menuBackground = new Sprite(new Texture("backgrounds/menu.png"));
        menuBackground.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button.up");
        textButtonStyle.down = skin.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = Utils.corsivaBlack;
        newGame = new TextButton("New Game", textButtonStyle);
        newGame.pad(Utils.DEFAULT_BUTTON_PAD);
        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                ((Game) Gdx.app.getApplicationListener()).getScreen().dispose();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new BattleWorld(new Player("Player",
                        new Ship("Player Ship",Ship.ShipType.GUNBOAT, new CannonType(CannonType.CannonWeightType.THIRTYTWO_POUNDER, CannonType.CannonRangeType.LONG_CANNON),
                                CannonShot.CannonShotType.ROUNDSHOT, BattleWorld.WORLD_SIZE/2, BattleWorld.WORLD_SIZE/2, 0))));
            }
        });
        table.add(newGame);
        stage.addActor(table);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        menuBackground.draw(batch);
        batch.end();
        stage.act(delta);
        stage.draw();
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
        stage.dispose();
        skin.dispose();
        atlas.dispose();
        menuBackground.getTexture().dispose();
        batch.dispose();
    }
}
