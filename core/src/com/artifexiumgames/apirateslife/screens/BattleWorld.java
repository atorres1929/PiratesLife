package com.artifexiumgames.apirateslife.screens;

import com.artifexiumgames.apirateslife.effect.FadeInFadeOutEffectArea;
import com.artifexiumgames.apirateslife.entity.CannonShot;
import com.artifexiumgames.apirateslife.entity.NPC_Ship;
import com.artifexiumgames.apirateslife.entity.Player;
import com.artifexiumgames.apirateslife.entity.Ship;
import com.artifexiumgames.apirateslife.event.Battle;
import com.artifexiumgames.apirateslife.item.CannonType;
import com.artifexiumgames.apirateslife.utils.Utils;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import aurelienribon.tweenengine.Tween;

/**
 * Created by Adam on 5/26/2016.
 */
public class BattleWorld implements Screen, InputProcessor, GestureDetector.GestureListener{

    private enum GameState{PAUSED, RUNNING}

    public static final int WORLD_SIZE = 10000;
    private final float STARTING_ZOOM = 0.25f;
    private final int EFFECT_MAX_DISTANCE_FROM_SHIP = 2000;
    private final int WAVE_NUMBER = 100;
    private final int WAVE_FADE_DELAY = 1;
    private final int CLOUD_NUMBER = 30;
    private GameState gameState;
    private Battle battle;
    private Sprite runningBackground;
    private FadeInFadeOutEffectArea waves;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private OrthographicCamera camera;
    private Viewport viewport;
    public BattleWorld(Player player){
        Array<Ship> npc_ships = new Array<Ship>();
        npc_ships.add(new NPC_Ship("Brigantine", Ship.ShipType.BRIGANTINE, new CannonType(CannonType.CannonWeightType.TWELVE_POUNDER, CannonType.CannonRangeType.LONG_CANNON),
                CannonShot.CannonShotType.ROUNDSHOT, player.getShip().getX()-500,player.getShip().getY(), 0, player.getShip(), NPC_Ship.AI_AGGRESSION_STATE.CHASE));
        npc_ships.add(new NPC_Ship("Gunboat", Ship.ShipType.GUNBOAT, new CannonType(CannonType.CannonWeightType.TWELVE_POUNDER, CannonType.CannonRangeType.LONG_CANNON),
                CannonShot.CannonShotType.ROUNDSHOT, player.getShip().getX()+500,player.getShip().getY(), 180, player.getShip(), NPC_Ship.AI_AGGRESSION_STATE.CHASE));

        player.getShip().setEnemies(npc_ships);

        for (Ship ship: npc_ships){
            ship.setEnemy(player.getShip());
        }
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        battle = new Battle(player, npc_ships, camera, batch);
        shapeRenderer = new ShapeRenderer();
        runningBackground = new Sprite(new Texture("backgrounds/sea.png"));
        waves = new FadeInFadeOutEffectArea(battle.getPlayerShip(), new Sprite(new Texture("effects/ocean/wave.png")), WAVE_FADE_DELAY, EFFECT_MAX_DISTANCE_FROM_SHIP, WAVE_NUMBER, Tween.INFINITY); //The animation will go on forever (Tween.INFINITY)
        runningBackground.setSize(WORLD_SIZE, WORLD_SIZE);
        viewport = new FillViewport(WORLD_SIZE, WORLD_SIZE, camera);
        camera.zoom = STARTING_ZOOM;
        gameState = GameState.RUNNING;

        //DEBUG
        for (NPC_Ship npc_ship: battle.getNPCShips()){
            npc_ship.setDebug(true);
        }

    }

    @Override
    public void show() {
        Utils.refreshVieport(viewport);
        if (Gdx.app.getType() == Application.ApplicationType.Desktop)
            Gdx.input.setInputProcessor(this);
        else if (Gdx.app.getType() == Application.ApplicationType.Android)
            Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    @Override
    public void render(float delta) {
        if (gameState == GameState.RUNNING) {
            Utils.DELTA = delta;
            Gdx.gl.glClearColor(102f / 255f, 188f / 255f, 255f / 255f, 1f); //Set Background to color of runningBackground
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            batch.setProjectionMatrix(camera.combined);
            shapeRenderer.setProjectionMatrix(camera.combined);
            batch.begin();
            runningBackground.draw(batch);
            waves.animate(batch, Utils.DELTA);
            battle.drawShips(batch);
            battle.update();
            batch.end();
            batch.flush();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(0, 0, WORLD_SIZE, WORLD_SIZE);
            battle.drawShapes(shapeRenderer);
            shapeRenderer.end();

        }
        else if (gameState == GameState.PAUSED){
            ((Game) Gdx.app.getApplicationListener()).setScreen(new PauseMenu(this));
        }
    }

    @Override
    public void resize(int width, int height) {
        Utils.refreshVieport(viewport);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        gameState = GameState.RUNNING;
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        waves.dispose();
        runningBackground.getTexture().dispose();
        battle.disposeTextures();
        shapeRenderer.dispose();
    }

    /*
     *******************************************************************
     *ANDROID
     *******************************************************************
     */

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        if (x < Gdx.graphics.getWidth()/4)
            battle.getPlayerShip().setDirection(Ship.Direction.LEFT);
        else if (x > Gdx.graphics.getWidth()*3/4)
            battle.getPlayerShip().setDirection(Ship.Direction.RIGHT);
        else
            battle.getPlayerShip().setDirection(Ship.Direction.STRAIGHT);

        if (x > Gdx.graphics.getWidth()/4 && x < Gdx.graphics.getWidth()/2){
            battle.getPlayerShip().fireLeftCannons();
        }
        else if (x < Gdx.graphics.getWidth()*3/4 && x > Gdx.graphics.getWidth()/2){
            battle.getPlayerShip().fireRightCannons();
        }

        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        if (gameState == GameState.RUNNING)
            gameState = GameState.PAUSED;
        else if (gameState == GameState.PAUSED)
            gameState = GameState.RUNNING;
        return true;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        if (initialDistance < distance && camera.zoom-0.01f > 0f)
            camera.zoom -= 0.01f;
        else if (camera.zoom+0.01f < 2f)
            camera.zoom += 0.01f;
        Utils.refreshVieport(viewport);
        return true;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }


    /*
     *******************************************************************
     *KEYBOARD
     *******************************************************************
     */
    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.A)
            battle.getPlayerShip().setDirection(Ship.Direction.LEFT);
        else if (keycode == Input.Keys.D)
            battle.getPlayerShip().setDirection(Ship.Direction.RIGHT);
        else if (keycode == Input.Keys.W)
            battle.getPlayerShip().incrementSailState();
        else if (keycode == Input.Keys.S)
            battle.getPlayerShip().decrementSailState();
        else if (keycode == Input.Keys.Q)
            battle.getPlayerShip().fireLeftCannons();
        else if (keycode == Input.Keys.E)
            battle.getPlayerShip().fireRightCannons();
        else if (keycode == Input.Keys.ESCAPE)
            gameState = GameState.PAUSED;
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.A)
            battle.getPlayerShip().setDirection(Ship.Direction.STRAIGHT);
        else if (keycode == Input.Keys.D)
            battle.getPlayerShip().setDirection(Ship.Direction.STRAIGHT);

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) { return false; }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if ((camera.zoom + amount*.1) > 0f && (camera.zoom +amount*.1) < .5f)
            camera.zoom += (float) amount*.1;
        camera.update();
        Utils.refreshVieport(viewport);
        return true;
    }
}
