package com.msquared.stairs.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.view.WorldRenderer;

public class SplashScreen extends AbstractScreen implements Screen {
	private Stairs game;
	private TextureAtlas atlas;
	private Animation stairsAnimation;
	private static final float FRAME_DURATION = 0.020f;
	private float width;
	private float height;
	private float widthRatio;
	private float heightRatio;
	float stateTime;
	
	public SplashScreen(Stairs g) {
		super(g);
		game = g;
		//loadTextures();
	}
	

	private void loadTextures() {
//		atlas = new TextureAtlas(Gdx.files.internal("images/textures/splashani/splashani.pack"));
//		TextureRegion[] frames = new TextureRegion[180];
//		for (int i = 0; i < 180; i++) {
//			String index = "";
//			index += i + 1;
//			frames[i] = atlas.findRegion("tern" + index);
//		}
//		stairsAnimation = new Animation(FRAME_DURATION, frames);
	}
	@Override
	public void render(float delta) {
//		Float imagWidthOrig = (float) WorldRenderer.CAMERA_WIDTH;
//		Float imagHeightOrig = (float) WorldRenderer.CAMERA_HEIGHT;
//		//Float imagPadding = (imagWidth + imagWidthOrig);
//
//		stateTime += delta;
//		TextureRegion currentFrame = stairsAnimation.getKeyFrame(stateTime, true);
//		Image image = new Image(currentFrame);
//		image.setWidth(imagWidthOrig);
//		image.setHeight(imagHeightOrig);
//		image.setPosition(0, 0);
//		//Table.drawDebug(stage);
//		stage.addActor(image);
//		stage.draw();
//		if (stateTime >= FRAME_DURATION * 180) {
//			game.setScreen(new MenuScreen(game));
//		}
		
	}

	@Override
	public void show() {
		game.myRequestHandler.showAds(false);
		
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		
		heightRatio = this.height / WorldRenderer.CAMERA_HEIGHT;
		widthRatio = this.width / WorldRenderer.CAMERA_WIDTH;
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
		
	}
	
}
