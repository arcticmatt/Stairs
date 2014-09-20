package com.msquared.stairs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.view.WorldRenderer;

public class AbstractScreen implements Screen {

	Stairs game;
	protected float width, height;
	protected SpriteBatch spriteBatch;
	protected Rectangle viewport;
	protected Camera camera;
	protected Skin skin;
	protected Table table;
	protected Stage stage;
	protected OrthographicCamera cam;
	
	public AbstractScreen(Stairs game) {
		this.game = game;
		stage = new Stage(new StretchViewport(WorldRenderer.CAMERA_WIDTH, 
				WorldRenderer.CAMERA_HEIGHT));
	}

	public SpriteBatch getBatch() {
		if (spriteBatch == null) {
			spriteBatch = new SpriteBatch();
		}
		return spriteBatch;
	}
	
	public Camera getCamera() {
		camera = new OrthographicCamera(WorldRenderer.CAMERA_WIDTH,
				WorldRenderer.CAMERA_HEIGHT);
		camera.position.set(270f, 430f, 0);
		camera.update();
		return camera;
	}

	protected Skin getSkin() {
		if (skin == null) {
			FileHandle skinFile = Gdx.files.internal("skin/uiskin.json");
			skin = new Skin(skinFile);
		}
		return skin;
	}
	
	protected Table getTable()
    {
        if( table == null ) {
            table = new Table( getSkin() );
            table.setFillParent( true );
            /*if( Tyrian.DEV_MODE ) {
                table.debug();
            }
            stage.addActor( table );*/
        }
        return table;
    }
	
	protected boolean tableExists()
	{
		if (table == null) {
			return false;
		}
		return true;
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		camera.update();
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		// Changed jars, this became necessary (along with the new
		// stage constructor with the stretch viewport)
		camera = new OrthographicCamera(width, height);
	    camera.position.set(width / 2, height / 2, 0); 
	    camera.update();
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		stage.clear();	
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		if (spriteBatch != null) {
			spriteBatch.dispose();
		}
		if (skin != null) {
			skin.dispose();
		}
	}
	
	public void clearColor() {
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
			Gdx.gl.glClearColor(.949f, .949f, .949f, 1);
		} else {
			Gdx.gl.glClearColor(.101f, .101f, .101f, 1);
		}
	}


}
