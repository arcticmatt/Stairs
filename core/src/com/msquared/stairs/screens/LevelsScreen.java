package com.msquared.stairs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.profile.Profile;
import com.msquared.stairs.utils.DefaultActorListener;
import com.msquared.stairs.view.WorldRenderer;

public class LevelsScreen extends AbstractScreen implements Screen {
	CharSequence gameScore;
	float stateTime;
	TextureRegion currentFrame;
	GameScreen gameScreen;
	Profile profile;
	int mediumHighScore;
	int hardHighScore;
	boolean hardUnlocked;
	boolean insaneUnlocked;
	Preferences prefs;
	Texture easyTexLevels;
	Texture easyTexLevelsDown;
	Texture mediumTexLevels;
	Texture mediumTexLevelsDown;
	Texture hardTexLevels;
	Texture hardTexLevelsDown;
	Texture insaneTexLevels;
	Texture insaneTexLevelsDown;
	
	// Constructor to keep a reference to the main Game class
	public LevelsScreen(Stairs game) {
		super(game);
		stateTime = 0f;
		easyTexLevels = new Texture(
				"images/buttons/levels/btn_easy_levels.png");
		easyTexLevelsDown = new Texture(
				"images/buttons/levels/btn_easy_levels_down.png");
		mediumTexLevels = new Texture(
				"images/buttons/levels/btn_medium_levels.png");
		mediumTexLevelsDown = new Texture(
				"images/buttons/levels/btn_medium_levels_down.png");
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
		prefs = Gdx.app.getPreferences("Preferences");
		//profile.resetScores();
		Gdx.input.setInputProcessor(stage);
		// Show ads
		game.myRequestHandler.showAds(true);
		
		table = new Table(getSkin());

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

		final Float buttonWidth = 400f * heightRatio * (1 / widthRatio);
		final Float buttonHeight = 71f;
		Float buttonSpacing = 30f;

		/*
		 * Levels buttons
		 */
		table.row();
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
					prefs.putInteger("mostRecent", Stairs.EASY_LEVELS);
					prefs.flush();
					gameScreen = new GameScreen(game, Stairs.EASY_LEVELS, null);
					game.setScreen(gameScreen);
				}
			}
		});
		table.add(easyLevelsImagButton).size(buttonWidth, buttonHeight)
				.spaceBottom(buttonSpacing).spaceTop(100).colspan(2).center()
				.expandX().fillX();
		table.row();

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
					prefs.putInteger("mostRecent", Stairs.MEDIUM_LEVELS);
					prefs.flush();
					gameScreen = new GameScreen(game, Stairs.MEDIUM_LEVELS, null);
					game.setScreen(gameScreen);
				}
			}
		});
		table.add(mediumLevelsImagButton).size(buttonWidth, buttonHeight)
				.uniform().spaceBottom(buttonSpacing).colspan(2).center();
		table.row();

		mediumHighScore = profile.getHighScore(4);
		hardUnlocked = mediumHighScore >= 150;
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
					prefs.putInteger("mostRecent", Stairs.HARD_LEVELS);
					prefs.flush();
					gameScreen = new GameScreen(game, Stairs.HARD_LEVELS, null);
					game.setScreen(gameScreen);
				}
			}
		});
		table.add(hardLevelsImagButton).size(buttonWidth, buttonHeight)
				.uniform().spaceBottom(buttonSpacing).colspan(2).center();
		table.row();

		hardHighScore = profile.getHighScore(7);
		insaneUnlocked = hardHighScore >= 100;
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
					prefs.putInteger("mostRecent", Stairs.INSANE_LEVELS);
					prefs.flush();
					gameScreen = new GameScreen(game, Stairs.INSANE_LEVELS, null);
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
		TextureRegionDrawable menuUp = new TextureRegionDrawable(
				new TextureRegion(game.menuTex));
		TextureRegionDrawable menuDown = new TextureRegionDrawable(
				new TextureRegion(game.menuTexDown));
		ImageButtonStyle menuStyle = new ImageButtonStyle();
		menuStyle.up = menuUp;
		menuStyle.down = menuDown;
		ImageButton menuImagButton = new ImageButton(menuStyle);
		menuImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > imagWidth || y < 0 || y > imagHeight)) {
					game.setScreen(game.menuScreen);
				}
			}
		});
		table.add(menuImagButton).size(imagWidth, imagHeight)
				.align(Align.center).colspan(2);
		table.setFillParent(true);
		stage.addActor(table);
	}
	
	@Override
	public void hide() {
		super.hide();
		hardTexLevels.dispose();
		hardTexLevelsDown.dispose();
		insaneTexLevels.dispose();
		insaneTexLevelsDown.dispose();
	}
}
