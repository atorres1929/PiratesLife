package com.artifexiumgames.apirateslife.screens;

import com.artifexiumgames.apirateslife.utils.Utils;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Adam on 5/28/2016.
 */
public class PauseMenu implements Screen{

    private BattleWorld battleWorld;
    private Stage stage;
    private TextureAtlas atlas;
    private Skin skin;
    private Table table;

    public PauseMenu(BattleWorld battleWorld){
        this.battleWorld = battleWorld;
    }
    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        atlas = new TextureAtlas("ui/buttons.pack");
        skin = new Skin(atlas);
        table = new Table(skin);
        table.setBounds(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("button.up");
        textButtonStyle.down = skin.getDrawable("button.down");
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = Utils.corsivaBlack;

        Label label = new Label("Pause", new Label.LabelStyle(Utils.corsivaTitleWhite, Color.WHITE));
        final String[] buttonNames = new String[]{"Continue Battle", "Options", "Exit Game"};
        final TextButton[] textButtons = new TextButton[buttonNames.length];
        for (int i = 0; i < textButtons.length; i++){
            textButtons[i] = new TextButton(buttonNames[i], textButtonStyle);
            textButtons[i].setName(buttonNames[i]);
            textButtons[i].pad(Utils.DEFAULT_BUTTON_PAD);
        }

        textButtons[0].addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                battleWorld.resume();
                ((Game) Gdx.app.getApplicationListener()).setScreen(battleWorld);
            }
        });

        textButtons[1].addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });

        textButtons[2].addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });

        table.add(label);
        table.row();
        for(int i = 0; i < textButtons.length; i++){
            table.add(textButtons[i]);
            table.row();
        }
        stage.addActor(table);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
    }
}
