package com.msquared.stairs.screens;

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
import com.msquared.stairs.profile.Profile;
import com.msquared.stairs.utils.DefaultActorListener;
import com.msquared.stairs.view.WorldRenderer;

public class LevelsScreen extends AbstractScreen implements Screen {
	private int score = 0;
	CharSequence gameScore;
	private static final float FRAME_DURATION = 0.1f;
	private Animation stairsAnimation;
	float stateTime;
	TextureRegion currentFrame;
	GameScreen gameScreen;
	Profile profile;
	int mediumHighScore;
	int hardHighScore;
	boolean hardUnlocked;
	boolean insaneUnlocked;
	Preferences prefs;
	
	// Constructor to keep a reference to the main Game class
	public LevelsScreen(Stairs game) {
		super(game);
		spriteBatch = getBatch();
		camera = getCamera();
		font = getFont();
		stateTime = 0f;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.input.setInputProcessor(stage);
		stage.draw();
	}

	@Override
	public void show() {
		profile = game.getProfileManager().retrieveProfile();
		profile.resetScores();
		Gdx.input.setInputProcessor(stage);
		// Show ads
		game.myRequestHandler.showAds(true);
		table = new Table(getSkin());
		prefs = Gdx.app.getPreferences("Preferences");

		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		float heightRatio = this.height / WorldRenderer.CAMERA_HEIGHT;
		float widthRatio = this.width / WorldRenderer.CAMERA_WIDTH;

		table.setFillParent(true);
		table.setY(0);
		table.add("").width(540).expandX().colspan(2).center();
		table.row();

		Float imagWidthOrig = 100f;
		Float imagHeightOrig = 100f;
		final Float imagWidth = imagWidthOrig * heightRatio * (1 / widthRatio);
		final Float imagHeight = imagHeightOrig;
		Float imagPadding = 185f - (imagWidth - imagWidthOrig);

		final Float buttonWidth = 400f * heightRatio * (1 / widthRatio);
		final Float buttonHeight = 71f;
		Float buttonSpacing = 30f;

		// Texture.setEnforcePotImages(false);
		/*
		 * Levels buttons
		 */
		table.row();
		Texture easyTexLevels = new Texture(
				"images/buttons/levels/btn_easy_levels.png");
		Texture easyTexLevelsDown = new Texture(
				"images/buttons/levels/btn_easy_levels_down.png");
		TextureRegionDrawable easyLevelsUp = new TextureRegionDrawable(
				new TextureRegion(easyTexLevels));
		TextureRegionDrawable easyLevelsDown = new TextureRegionDrawable(
				new TextureRegion(easyTexLevelsDown));
		ImageButtonStyle easyLevelsStyle = new ImageButtonStyle();
		easyLevelsStyle.up = easyLevelsUp;
		easyLevelsStyle.down = easyLevelsDown;
		ImageButton easyLevelsImagButton = new ImageButton(easyLevelsStyle);
		easyLevelsImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > buttonWidth || y < 0 || y > buttonHeight)) {
					super.touchUp(event, x, y, pointer, button);
					prefs.putInteger("mostRecent", game.EASY_LEVELS);
					prefs.flush();
					gameScreen = new GameScreen(game, game.EASY_LEVELS);
					game.setScreen(gameScreen);
				}
			}
		});
		table.add(easyLevelsImagButton).size(buttonWidth, buttonHeight)
				.spaceBottom(buttonSpacing).spaceTop(100).colspan(2).center()
				.expandX().fillX();
		table.row();

		Texture mediumTexLevels = new Texture(
				"images/buttons/levels/btn_medium_levels.png");
		Texture mediumTexLevelsDown = new Texture(
				"images/buttons/levels/btn_medium_levels_down.png");
		TextureRegionDrawable mediumLevelsUp = new TextureRegionDrawable(
				new TextureRegion(mediumTexLevels));
		TextureRegionDrawable mediumLevelsDown = new TextureRegionDrawable(
				new TextureRegion(mediumTexLevelsDown));
		ImageButtonStyle mediumLevelsStyle = new ImageButtonStyle();
		mediumLevelsStyle.up = mediumLevelsUp;
		mediumLevelsStyle.down = mediumLevelsDown;
		ImageButton mediumLevelsImagButton = new ImageButton(mediumLevelsStyle);
		mediumLevelsImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > buttonWidth || y < 0 || y > buttonHeight)) {
					super.touchUp(event, x, y, pointer, button);
					prefs.putInteger("mostRecent", game.MEDIUM_LEVELS);
					prefs.flush();
					gameScreen = new GameScreen(game, game.MEDIUM_LEVELS);
					game.setScreen(gameScreen);
				}
				Gdx.app.log(Stairs.LOG, "x coord: " + x + " y coord: " + y);
			}
		});
		table.add(mediumLevelsImagButton).size(buttonWidth, buttonHeight)
				.uniform().spaceBottom(buttonSpacing).colspan(2).center();
		table.row();

		mediumHighScore = profile.getHighScore(4);
		Texture hardTexLevels;
		Texture hardTexLevelsDown;
		hardUnlocked = mediumHighScore >= 150;
		// final boolean hardUnlocked = false;
		if (hardUnlocked) {
			hardTexLevels = new Texture(
					"images/buttons/levels/btn_hard_levels.png");
			hardTexLevelsDown = new Texture(
					"images/buttons/levels/btn_hard_levels_down.png");
		} else {
			hardTexLevels = new Texture(
					"images/buttons/levels/btn_hard_levels_locked.png");
			hardTexLevelsDown = new Texture(
					"images/buttons/levels/btn_hard_levels_locked.png");
		}
		TextureRegionDrawable hardLevelsUp = new TextureRegionDrawable(
				new TextureRegion(hardTexLevels));
		TextureRegionDrawable hardLevelsDown = new TextureRegionDrawable(
				new TextureRegion(hardTexLevelsDown));
		ImageButtonStyle hardLevelsStyle = new ImageButtonStyle();
		hardLevelsStyle.up = hardLevelsUp;
		hardLevelsStyle.down = hardLevelsDown;
		ImageButton hardLevelsImagButton = new ImageButton(hardLevelsStyle);
		hardLevelsImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > buttonWidth || y < 0 || y > buttonHeight)
						&& hardUnlocked) {
					super.touchUp(event, x, y, pointer, button);
					prefs.putInteger("mostRecent", game.HARD_LEVELS);
					prefs.flush();
					gameScreen = new GameScreen(game, game.HARD_LEVELS);
					game.setScreen(gameScreen);
				}
			}
		});
		table.add(hardLevelsImagButton).size(buttonWidth, buttonHeight)
				.uniform().spaceBottom(buttonSpacing).colspan(2).center();
		table.row();

		hardHighScore = profile.getHighScore(7);
		Texture insaneTexLevels;
		Texture insaneTexLevelsDown;
		insaneUnlocked = hardHighScore >= 100;
		// final boolean insaneUnlocked = false;
		if (insaneUnlocked) {
			insaneTexLevels = new Texture(
					"images/buttons/levels/btn_insane_levels.png");
			insaneTexLevelsDown = new Texture(
					"images/buttons/levels/btn_insane_levels_down.png");
		} else {
			insaneTexLevels = new Texture(
					"images/buttons/levels/btn_insane_levels_locked.png");
			insaneTexLevelsDown = new Texture(
					"images/buttons/levels/btn_insane_levels_locked.png");
		}
		TextureRegionDrawable insaneLevelsUp = new TextureRegionDrawable(
				new TextureRegion(insaneTexLevels));
		TextureRegionDrawable insaneLevelsDown = new TextureRegionDrawable(
				new TextureRegion(insaneTexLevelsDown));
		ImageButtonStyle insaneLevelsStyle = new ImageButtonStyle();
		insaneLevelsStyle.up = insaneLevelsUp;
		insaneLevelsStyle.down = insaneLevelsDown;
		ImageButton insaneLevelsImagButton = new ImageButton(insaneLevelsStyle);
		insaneLevelsImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > buttonWidth || y < 0 || y > buttonHeight)
						&& insaneUnlocked) {
					super.touchUp(event, x, y, pointer, button);
					prefs.putInteger("mostRecent", game.INSANE_LEVELS);
					prefs.flush();
					gameScreen = new GameScreen(game, game.INSANE_LEVELS);
					game.setScreen(gameScreen);
				}
			}
		});
		table.add(insaneLevelsImagButton).size(buttonWidth, buttonHeight)
				.uniform().spaceBottom(buttonSpacing).colspan(2).center();
		table.row();

		/*
		 * Menu button
		 */
		Texture menuTex = new Texture("images/buttons/misc/btn_menu.png");
		Texture menuTexDown = new Texture(
				"images/buttons/misc/btn_menu_down.png");
		TextureRegionDrawable menuUp = new TextureRegionDrawable(
				new TextureRegion(menuTex));
		TextureRegionDrawable menuDown = new TextureRegionDrawable(
				new TextureRegion(menuTexDown));
		ImageButtonStyle menuStyle = new ImageButtonStyle();
		menuStyle.up = menuUp;
		menuStyle.down = menuDown;
		ImageButton menuImagButton = new ImageButton(menuStyle);
		menuImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > imagWidth || y < 0 || y > imagHeight)) {
					super.touchUp(event, x, y, pointer, button);
					// Stop and dispose of the current music
					game.musicManager.stop();
					game.setScreen(game.menuScreen);
				}
			}
		});
		table.add(menuImagButton).size(imagWidth, imagHeight)
				.align(Align.center).colspan(2);

		table.setFillParent(true);
		table.debug();
		stage.addActor(table);

		Gdx.app.log(Stairs.LOG, "Imag width " + imagWidth + " Imag height "
				+ imagHeight);

		stage.addActor(table);

	}
	
	public void setScore(int gameScore) {
		score = gameScore;
	}

	@Override
	public void hide() {
		super.hide();
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
