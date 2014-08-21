package com.msquared.stairs.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.controller.FootController;
import com.msquared.stairs.utils.DefaultActorListener;
import com.msquared.stairs.view.WorldRenderer;

public class MenuScreen extends AbstractScreen implements Screen {
	CharSequence gameScore;
	private static final float FRAME_DURATION = 0.05f;
	private Animation stairsAnimation;
	float stateTime;
	TextureRegion currentFrame;
	GameScreen gameScreen;
	public static final int EASY = 1;
	public static final int MEDIUM = 2;
	public static final int HARD = 3;
	public static final int INSANE = 4;
	public static final int EASY_CLASSIC = 5;
	public static final int MEDIUM_CLASSIC = 6;
	public static final int HARD_CLASSIC = 7;
	public static final int INSANE_CLASSIC = 8;
	Preferences prefs;
	float heightRatio;
	float widthRatio;
	float aniWidth;
	float aniHeight;
	float aniXPos;
	float aniYPos;
	
	// Textures
	Texture levelsTex;
	Texture levelsTexDown;
	Texture classicTex;
	Texture classicTexDown;
	Texture mostRecentTex;
	Texture mostRecentTexDown;
	Texture highScoresTex;
	Texture highScoresTexDown;
	Texture settingsTex;
	Texture settingsTexDown;
	TextureAtlas atlas;
	
	SpriteBatch spriteBatch;
	boolean loaded;
	
	// Constructor to keep a reference to the main Game class
	public MenuScreen(final Stairs game) {
		super(game);
		stateTime = 0f;
		heightRatio = 1;
		widthRatio = 1;
		loaded = false;
		levelsTex = new Texture("images/buttons/levels/btn_levels.png");
		levelsTexDown = new Texture(
				"images/buttons/levels/btn_levels_down.png");
		classicTex = new Texture(
				"images/buttons/classic/btn_classic.png");
		classicTexDown = new Texture(
				"images/buttons/classic/btn_classic_down.png");
		highScoresTex = new Texture(
				"images/buttons/misc/btn_highscores.png");
		highScoresTexDown = new Texture(
				"images/buttons/misc/btn_highscores_down.png");
		settingsTex = new Texture(
				"images/buttons/misc/btn_settings.png");
		settingsTexDown = new Texture(
				"images/buttons/misc/btn_settings_down.png");
	}
	
	private void loadTextures() {
		int num = 96;
		if (!game.iphoneGame) { 
			atlas = game.assetManager.get("images/anis/menu_ani/menu_ani.pack", TextureAtlas.class);
		} else {
			atlas = new TextureAtlas(Gdx.files.internal("images/anis/menu_ani/menu_ani.pack"));
		}
		TextureRegion[] frames = new TextureRegion[num];
		for (int i = 1; i <= num; i++) {
			frames[i-1] = atlas.findRegion("ani" + i);
		}
		stairsAnimation = new Animation(FRAME_DURATION, frames);
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.101f, .101f, .101f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stateTime += delta;
		currentFrame = stairsAnimation.getKeyFrame(stateTime, true);
		spriteBatch.begin();
		spriteBatch.draw(currentFrame, aniXPos, aniYPos, aniWidth, aniHeight);
     	spriteBatch.end();
		stage.draw();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		prefs = Gdx.app.getPreferences("Preferences");
		
		/* 
		 * Add listener so that when user clicks on animation area, he gets taken to 
		 * tutorial video
		 */
		stage.addListener(new InputListener() {     
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				if (x >= aniXPos && x <= aniXPos + aniWidth
	        			&& y >= aniYPos && y <= aniYPos + aniHeight) {
		    		return true;
	        	}
				return false;
	        }
	        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	        	if (x >= aniXPos && x <= aniXPos + aniWidth
	        			&& y >= aniYPos + 50 && y <= aniYPos + aniHeight) {
	        		Gdx.net.openURI("http://www.stairsthegame.com/mobile.html");
	        	}
	        }
		});
		
		// Show ads
		game.myRequestHandler.showAds(true);
		
		if (!loaded) {
			loadTextures();
			loaded = true;
		}
		camera = getCamera();
		spriteBatch = new SpriteBatch();
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.disableBlending();
		
		table = new Table(getSkin());

		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		heightRatio = this.height / WorldRenderer.CAMERA_HEIGHT;
		widthRatio = this.width / WorldRenderer.CAMERA_WIDTH;
		aniWidth = 361.5f * heightRatio * (1 / widthRatio);
		aniHeight = 482f;
		aniXPos = (WorldRenderer.CAMERA_WIDTH - aniWidth) / 2;
		aniYPos = 403;

		table.setFillParent(true);
		table.setY(-150);
		table.add("").width(540).expandX().colspan(2).center();
		table.row();

		Float imagWidthOrig = 65f;
		Float imagHeightOrig = 65f;
		final Float imagWidth = imagWidthOrig * heightRatio * (1 / widthRatio);
		final Float imagHeight = imagHeightOrig;
		Float imagPadding = 180f - (imagWidth - imagWidthOrig);

		final Float buttonWidth = 400f * heightRatio * (1 / widthRatio);
		final Float buttonHeight = 71f;
		Float buttonSpacing = 30f;

		/*
		 * Levels buttons
		 */
		table.row();
		TextureRegionDrawable levelsTR = new TextureRegionDrawable(
				new TextureRegion(levelsTex));
		TextureRegionDrawable levelsTRDown = new TextureRegionDrawable(
				new TextureRegion(levelsTexDown));
		ImageButtonStyle levelsStyle = new ImageButtonStyle();
		levelsStyle.up = levelsTR;
		levelsStyle.down = levelsTRDown;
		ImageButton levelsImagButton = new ImageButton(levelsStyle);
		levelsImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > buttonWidth || y < 0 || y > buttonHeight)) {
					game.setScreen(game.levelsScreen);
				}
			}
		});
		table.add(levelsImagButton).size(buttonWidth, buttonHeight)
				.spaceBottom(buttonSpacing).spaceTop(67).colspan(2).center()
				.expandX().fillX();
		table.row();

		TextureRegionDrawable classicTR = new TextureRegionDrawable(
				new TextureRegion(classicTex));
		TextureRegionDrawable classicTRDown = new TextureRegionDrawable(
				new TextureRegion(classicTexDown));
		ImageButtonStyle classicStyle = new ImageButtonStyle();
		classicStyle.up = classicTR;
		classicStyle.down = classicTRDown;
		ImageButton classicImagButton = new ImageButton(classicStyle);
		classicImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > buttonWidth || y < 0 || y > buttonHeight)) {
					game.setScreen(game.classicScreen);
				}
			}
		});
		table.add(classicImagButton).size(buttonWidth, buttonHeight).uniform()
				.spaceBottom(buttonSpacing).colspan(2).center();
		table.row();

		ArrayList<String> mostRecentLevels = getMostRecentLevel();
		String handleUp = mostRecentLevels.get(0);
		String handleDown = mostRecentLevels.get(1);
		mostRecentTex = new Texture(handleUp);
		mostRecentTexDown = new Texture(handleDown);
		TextureRegionDrawable mostRecentUp = new TextureRegionDrawable(
				new TextureRegion(mostRecentTex));
		TextureRegionDrawable mostRecentDown = new TextureRegionDrawable(
				new TextureRegion(mostRecentTexDown));
		ImageButtonStyle mostRecentStyle = new ImageButtonStyle();
		mostRecentStyle.up = mostRecentUp;
		mostRecentStyle.down = mostRecentDown;
		ImageButton mostRecentImagButton = new ImageButton(mostRecentStyle);
		mostRecentImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > buttonWidth || y < 0 || y > buttonHeight)) {
					int difficulty = prefs.getInteger("mostRecent", 1);
					gameScreen = new GameScreen(game, difficulty, null);
					game.setScreen(gameScreen);
				}
			}
		});
		table.add(mostRecentImagButton).size(buttonWidth, buttonHeight)
				.uniform().spaceBottom(buttonSpacing).colspan(2).center();
		table.row();

		/*
		 * Misc buttons
		 */
		TextureRegionDrawable highScoresUp = new TextureRegionDrawable(
				new TextureRegion(highScoresTex));
		TextureRegionDrawable highScoresDown = new TextureRegionDrawable(
				new TextureRegion(highScoresTexDown));
		ImageButtonStyle highScoresStyle = new ImageButtonStyle();
		highScoresStyle.up = highScoresUp;
		highScoresStyle.down = highScoresDown;
		ImageButton highScoresImagButton = new ImageButton(highScoresStyle);
		highScoresImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > imagWidth || y < 0 || y > imagHeight)) {
					game.setScreen(game.highScoresScreen);
				}
			}
		});
		table.add(highScoresImagButton).size(imagWidth, imagHeight)
				.align(Align.left).padLeft(imagPadding);

		TextureRegionDrawable settingsUp = new TextureRegionDrawable(
				new TextureRegion(settingsTex));
		TextureRegionDrawable settingsDown = new TextureRegionDrawable(
				new TextureRegion(settingsTexDown));
		ImageButtonStyle settingsStyle = new ImageButtonStyle();
		settingsStyle.up = settingsUp;
		settingsStyle.down = settingsDown;
		ImageButton settingsImagButton = new ImageButton(settingsStyle);
		settingsImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > imagWidth || y < 0 || y > imagHeight)) {
					game.setScreen(game.settingsScreen);
				}
			}
		});
		table.add(settingsImagButton).size(imagWidth, imagHeight)
				.align(Align.right).padRight(imagPadding);
		stage.addActor(table);
	}
	
	public ArrayList<String> getMostRecentLevel() {
		ArrayList<String> mostRecentLevels = new ArrayList<String>();
		String mostRecentUp;
		String mostRecentDown;
		if (prefs.getInteger("mostRecent", 1) == Stairs.EASY_LEVELS) {
			mostRecentUp = "images/buttons/levels/btn_easy_levels.png";
			mostRecentDown = "images/buttons/levels/btn_easy_levels_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		} else if (prefs.getInteger("mostRecent", 1) == Stairs.MEDIUM_LEVELS) {
			mostRecentUp = "images/buttons/levels/btn_medium_levels.png";
			mostRecentDown = "images/buttons/levels/btn_medium_levels_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		} else if (prefs.getInteger("mostRecent", 1) == Stairs.HARD_LEVELS) {
			mostRecentUp = "images/buttons/levels/btn_hard_levels.png";
			mostRecentDown = "images/buttons/levels/btn_hard_levels_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		} else if (prefs.getInteger("mostRecent", 1) == Stairs.INSANE_LEVELS) {
			mostRecentUp = "images/buttons/levels/btn_insane_levels.png";
			mostRecentDown = "images/buttons/levels/btn_insane_levels_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		} else if (prefs.getInteger("mostRecent", 1) == Stairs.EASY_CLASSIC) {
			mostRecentUp = "images/buttons/classic/btn_easy_classic.png";
			mostRecentDown = "images/buttons/classic/btn_easy_classic_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		} else if (prefs.getInteger("mostRecent", 1) == Stairs.MEDIUM_CLASSIC) {
			mostRecentUp = "images/buttons/classic/btn_medium_classic.png";
			mostRecentDown = "images/buttons/classic/btn_medium_classic_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		} else if (prefs.getInteger("mostRecent", 1) == Stairs.HARD_CLASSIC) {
			mostRecentUp = "images/buttons/classic/btn_hard_classic.png";
			mostRecentDown = "images/buttons/classic/btn_hard_classic_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		} else if (prefs.getInteger("mostRecent", 1) == Stairs.INSANE_CLASSIC) {
			mostRecentUp = "images/buttons/classic/btn_insane_classic.png";
			mostRecentDown = "images/buttons/classic/btn_insane_classic_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		}
		return mostRecentLevels;
	}

	@Override
	public void hide() {
		super.hide();
		mostRecentTex.dispose();
		mostRecentTexDown.dispose();
	}
}
