package com.msquared.stairs.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	boolean switcher;
	
	float imagTime;
	int imagPicker;
	Image displayImag;
	TextureRegion currentFrame;
	Texture staticFrame;
	Image staticImag;
	
	public SplashScreen(Stairs g) {
		super(g);
		game = g;
		//loadTextures();
		staticFrame = new Texture(Gdx.files.internal("images/anis/splash_ani/tern180.png"));
		//game.assetManager.load("images/anis/splash_ani/splash_ani.pack", TextureAtlas.class);
		game.assetManager.load("images/anis/menu_ani/menu_ani.pack", TextureAtlas.class);
		switcher = true;
	}
	

	private void loadTextures() {
		atlas = game.assetManager.get("images/anis/splash_ani/splash_ani.pack", TextureAtlas.class);
		TextureRegion[] frames = new TextureRegion[180];
		for (int i = 0; i < 180; i++) {
			String index = "";
			index += i + 1;
			frames[i] = atlas.findRegion("tern" + index);
		}
		stairsAnimation = new Animation(FRAME_DURATION, frames);
	}
	@Override
	public void render(float delta) {
		Float imagWidthOrig = (float) WorldRenderer.CAMERA_WIDTH;
		Float imagHeightOrig = (float) WorldRenderer.CAMERA_HEIGHT;
		//Float imagPadding = (imagWidth + imagWidthOrig);

		stateTime += delta;
//		currentFrame = stairsAnimation.getKeyFrame(stateTime, true);
//		Image image = new Image(currentFrame);
//		image.setWidth(imagWidthOrig);
//		image.setHeight(imagHeightOrig);
//		image.setPosition(0, 0);
		//Table.drawDebug(stage);
		
//		stage.clear();
//		stage.addActor(staticImag);
//		stage.draw();

		
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		float width = imagWidthOrig;
		float height = imagHeightOrig;
		if (game.assetManager.update() && stateTime >= FRAME_DURATION * 75) {
//			if (switcher) {
//				loadTextures();
//				switcher = false;
//			}
//			stateTime += delta;
//			currentFrame = stairsAnimation.getKeyFrame(stateTime, true);
//			spriteBatch.begin();
//			spriteBatch.draw(currentFrame, 0, 0, width, height);
//	     	spriteBatch.end();
			game.setScreen(game.menuScreen);
		} else {
			spriteBatch.begin();
			spriteBatch.draw(staticFrame, 0, 0, width, height);
	     	spriteBatch.end();
		}
     	
//		spriteBatch.begin();
//		spriteBatch.draw(staticFrame, 0, 0, width, height);
//     	spriteBatch.end();
//		if (stateTime >= FRAME_DURATION * 180) {
//			game.setScreen(game.menuScreen);
//		}
		
	}
	
//	private Image getImage(float delta) {
//		imagTime += delta;
//		if (imagTime >= FRAME_DURATION && imagPicker < 180) {
//			imagPicker++;
//			imagTime = 0;
//		}
//		Texture tex = new Texture(Gdx.files.internal("images/anis/terns/tern" + imagPicker + ".png"));
//		Image currImag = new Image(tex);
//		return currImag;
//	}

	@Override
	public void show() {
		game.myRequestHandler.showAds(false);
		
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		
		heightRatio = this.height / WorldRenderer.CAMERA_HEIGHT;
		widthRatio = this.width / WorldRenderer.CAMERA_WIDTH;
		
		camera = getCamera();
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.disableBlending();
	}

	
}
