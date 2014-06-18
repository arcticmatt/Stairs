package com.msquared.stairs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
	protected BitmapFont font;
	protected SpriteBatch spriteBatch;
	protected Rectangle viewport;
	protected Camera camera;
	protected Skin skin;
	protected TextureAtlas atlas;
	protected Table table;
	protected Stage stage;
	protected OrthographicCamera cam;
	
	public AbstractScreen(Stairs game) {
		this.game = game;
		stage = new Stage(new StretchViewport(WorldRenderer.CAMERA_WIDTH, 
				WorldRenderer.CAMERA_HEIGHT));
	}

	public BitmapFont getFont() {
		if (font == null) {
			font = new BitmapFont();
		}
		return font;
	}

	public SpriteBatch getBatch() {
		if (spriteBatch == null) {
			spriteBatch = new SpriteBatch();
		}
		return spriteBatch;
	}
	
	public Camera getCamera() {
		if (camera == null) {
			camera = new OrthographicCamera(WorldRenderer.CAMERA_WIDTH,
					WorldRenderer.CAMERA_HEIGHT);
		}
		return camera;
	}

	public TextureAtlas getAtlas() {
		if (atlas == null) {
			atlas = new TextureAtlas(
					Gdx.files.internal("image-atlases/pages.atlas"));
		}
		return atlas;
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
		cam.update();
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		// Changed jars, this became necessary (along with the new
		// stage constructor with the stretch viewport)
		cam = new OrthographicCamera(width, height);                // #6
	    cam.position.set(width / 2, height / 2, 0); 
		stage.getViewport().update(width, height, true);
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		if (spriteBatch != null) {
			spriteBatch.dispose();
		}
	}


}
