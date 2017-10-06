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

import info.datahelix.apirateslife.entity.CannonShot;
import info.datahelix.apirateslife.entity.Player;
import info.datahelix.apirateslife.entity.Ship;
import info.datahelix.apirateslife.item.CannonType;
import info.datahelix.apirateslife.utils.Utils;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
 * Created 5/24/2016
 * @author Adam Torres
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
        textButtonStyle.font = new BitmapFont(Gdx.files.internal("fonts/corsiva_black.fnt"));
        newGame = new TextButton("New Game", textButtonStyle);
        newGame.pad(Utils.DEFAULT_BUTTON_PAD);
        newGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                ((Game) Gdx.app.getApplicationListener()).getScreen().dispose();
                ((Game) Gdx.app.getApplicationListener()).setScreen(new BattleWorld(new Player("Player",
                        new Ship("Player Ship",Ship.ShipType.BRIGANTINE, new CannonType(CannonType.CannonWeightType.THIRTYTWO_POUNDER, CannonType.CannonRangeType.LONG_CANNON),
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
