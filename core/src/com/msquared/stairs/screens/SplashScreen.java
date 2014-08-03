package com.msquared.stairs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.view.WorldRenderer;

public class SplashScreen extends AbstractScreen implements Screen {
	private Stairs game;
	private static final float FRAME_DURATION = 0.020f;
	float stateTime;
	float splashWidth;
	float splashHeight;
	Texture staticFrame;
	
	public SplashScreen(Stairs g) {
		super(g);
		game = g;
		staticFrame = new Texture(Gdx.files.internal("images/anis/splash_ani/tern180.png"));
		game.assetManager.load("images/anis/menu_ani/menu_ani.pack", TextureAtlas.class);	
		splashWidth = (float) WorldRenderer.CAMERA_WIDTH;
		splashHeight = (float) WorldRenderer.CAMERA_HEIGHT;
	}
	

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		stateTime += delta;

		if (game.assetManager.update() && stateTime >= FRAME_DURATION * 75) {
			game.setScreen(game.menuScreen);
		} else {
			spriteBatch.begin();
			spriteBatch.draw(staticFrame, 0, 0, splashWidth, splashHeight);
	     	spriteBatch.end();
		}
	}

	@Override
	public void show() {
		game.myRequestHandler.showAds(false);
		camera = getCamera();
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.disableBlending();
	}
	
	@Override
	public void hide() {
		staticFrame.dispose();
		spriteBatch.dispose();
	}

	
}
