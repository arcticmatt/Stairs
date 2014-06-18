package com.msquared.stairs.screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.utils.DefaultActorListener;
import com.msquared.stairs.view.WorldRenderer;

public class MenuScreen extends AbstractScreen implements Screen {
	private int score = 0;
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
	
	// Constructor to keep a reference to the main Game class
	public MenuScreen(Stairs game) {
		super(game);
		camera = getCamera();
		font = getFont();
		stateTime = 0f;
		//loadTextures();
		prefs = Gdx.app.getPreferences("Preferences");
		heightRatio = 1;
		widthRatio = 1;
	}
	
	private void loadTextures() {
		atlas = new TextureAtlas(Gdx.files.internal("images/anis/menu_ani/menuani.pack"));
		TextureRegion[] frames = new TextureRegion[96];
		for (int i = 1; i <= 96; i++) {
			frames[i-1] = atlas.findRegion("ani" + i);
		}
		stairsAnimation = new Animation(FRAME_DURATION, frames);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.input.setInputProcessor(stage);
		

//		Float imagWidthOrig = 65f;
//		Float imagHeightOrig = 65f;
//		final Float imagWidth = imagWidthOrig * heightRatio * (1 / widthRatio);
//		final Float imagHeight = imagHeightOrig;
//		Float imagPadding = 180f - (imagWidth - imagWidthOrig);
//
//		stateTime += delta;
//		currentFrame = stairsAnimation.getKeyFrame(stateTime, true);
//		Image image = new Image(currentFrame);
//		image.setWidth(361.5f * (this.height / WorldRenderer.CAMERA_HEIGHT)
//				* (WorldRenderer.CAMERA_WIDTH / this.width));
//		image.setHeight(482f);
//		image.setPosition((WorldRenderer.CAMERA_WIDTH - image.getWidth()) / 2, 450);
//		table.debug();
//		//Table.drawDebug(stage);
//		stage.addActor(image);
		stage.draw();

		// table.clearChildren();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);

		table = new Table(getSkin());

		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		
		heightRatio = this.height / WorldRenderer.CAMERA_HEIGHT;
		widthRatio = this.width / WorldRenderer.CAMERA_WIDTH;

		table.setFillParent(true);
		table.setY(-150);
		table.add("").width(540).expandX().colspan(2).center();
		table.row();

		Float imagWidthOrig = 65f;
		Float imagHeightOrig = 65f;
		final Float imagWidth = imagWidthOrig * heightRatio * (1 / widthRatio);
		final Float imagHeight = imagHeightOrig;
		Float imagPadding = 180f - (imagWidth - imagWidthOrig);
		
		Gdx.app.log(Stairs.LOG, "Height ratio: " + heightRatio + " Width ratio: " + widthRatio);

		final Float buttonWidth = 400f * heightRatio * (1 / widthRatio);
		final Float buttonHeight = 71f;
		Float buttonSpacing = 30f;

		// Texture.setEnforcePotImages(false);
		/*
		 * Levels buttons
		 */
		table.row();
		levelsTex = new Texture("images/buttons/levels/btn_levels.png");
		levelsTexDown = new Texture(
				"images/buttons/levels/btn_levels_down.png");
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
					super.touchUp(event, x, y, pointer, button);
					dispose();
					game.setScreen(game.levelsScreen);
				}
			}
		});
		table.add(levelsImagButton).size(buttonWidth, buttonHeight)
				.spaceBottom(buttonSpacing).spaceTop(60).colspan(2).center()
				.expandX().fillX();
		table.row();

		classicTex = new Texture(
				"images/buttons/classic/btn_classic.png");
		classicTexDown = new Texture(
				"images/buttons/classic/btn_classic_down.png");
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
					super.touchUp(event, x, y, pointer, button);
					game.setScreen(game.classicScreen);
				}
				Gdx.app.log(Stairs.LOG, "x coord: " + x + " y coord: " + y);
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
					super.touchUp(event, x, y, pointer, button);
					int difficulty = prefs.getInteger("mostRecent", 1);
					gameScreen = new GameScreen(game, difficulty);
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
		highScoresTex = new Texture(
				"images/buttons/misc/btn_highscores.png");
		highScoresTexDown = new Texture(
				"images/buttons/misc/btn_highscores_down.png");
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
					super.touchUp(event, x, y, pointer, button);
					HighScoresScreen scoresScreen = new HighScoresScreen(game);
					game.setScreen(scoresScreen);
				}
			}
		});
		table.add(highScoresImagButton).size(imagWidth, imagHeight)
				.align(Align.left).padLeft(imagPadding);

		settingsTex = new Texture(
				"images/buttons/misc/btn_settings.png");
		settingsTexDown = new Texture(
				"images/buttons/misc/btn_settings_down.png");
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
					super.touchUp(event, x, y, pointer, button);
					game.setScreen(game.settingsScreen);
				}
			}
		});
		table.add(settingsImagButton).size(imagWidth, imagHeight)
				.align(Align.right).padRight(imagPadding);

		Gdx.app.log(Stairs.LOG, "Imag width " + imagWidth + " Imag height "
				+ imagHeight);

		stage.addActor(table);
		// Show ads
		game.myRequestHandler.showAds(true);

	}
	
	public ArrayList<String> getMostRecentLevel() {
		ArrayList<String> mostRecentLevels = new ArrayList<String>();
		String mostRecentUp;
		String mostRecentDown;
		if (prefs.getInteger("mostRecent", 1) == game.EASY_LEVELS) {
			mostRecentUp = "images/buttons/levels/btn_easy_levels.png";
			mostRecentDown = "images/buttons/levels/btn_easy_levels_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		} else if (prefs.getInteger("mostRecent", 1) == game.MEDIUM_LEVELS) {
			mostRecentUp = "images/buttons/levels/btn_medium_levels.png";
			mostRecentDown = "images/buttons/levels/btn_medium_levels_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		} else if (prefs.getInteger("mostRecent", 1) == game.HARD_LEVELS) {
			mostRecentUp = "images/buttons/levels/btn_hard_levels.png";
			mostRecentDown = "images/buttons/levels/btn_hard_levels_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		} else if (prefs.getInteger("mostRecent", 1) == game.INSANE_LEVELS) {
			mostRecentUp = "images/buttons/levels/btn_insane_levels.png";
			mostRecentDown = "images/buttons/levels/btn_insane_levels_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		} else if (prefs.getInteger("mostRecent", 1) == game.EASY_CLASSIC) {
			mostRecentUp = "images/buttons/classic/btn_easy_classic.png";
			mostRecentDown = "images/buttons/classic/btn_easy_classic_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		} else if (prefs.getInteger("mostRecent", 1) == game.MEDIUM_CLASSIC) {
			mostRecentUp = "images/buttons/classic/btn_medium_classic.png";
			mostRecentDown = "images/buttons/classic/btn_medium_classic_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		} else if (prefs.getInteger("mostRecent", 1) == game.HARD_CLASSIC) {
			mostRecentUp = "images/buttons/classic/btn_hard_classic.png";
			mostRecentDown = "images/buttons/classic/btn_hard_classic_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		} else if (prefs.getInteger("mostRecent", 1) == game.INSANE_CLASSIC) {
			mostRecentUp = "images/buttons/classic/btn_insane_classic.png";
			mostRecentDown = "images/buttons/classic/btn_insane_classic_down.png";
			mostRecentLevels.add(mostRecentUp);
			mostRecentLevels.add(mostRecentDown);
			return mostRecentLevels;
		}
		return mostRecentLevels;
	}
	
	public void setScore(int gameScore) {
		score = gameScore;
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
		/*levelsTex.dispose();
		levelsTexDown.dispose();
		classicTex.dispose();
		classicTexDown.dispose();
		mostRecentTex.dispose();
		mostRecentTexDown.dispose();
		highScoresTex.dispose();
		highScoresTexDown.dispose();
		settingsTex.dispose();
		settingsTexDown.dispose();
		atlas.dispose();
		stage.dispose();*/
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
		Gdx.input.setInputProcessor(null);
	}
	

}
