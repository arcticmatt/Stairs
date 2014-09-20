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
	Texture easyTexLevels;
	Texture easyTexLevelsDown;
	Texture mediumTexLevels;
	Texture mediumTexLevelsDown;
	Texture hardTexLevels;
	Texture hardTexLevelsDown;
	Texture hardTexLevelsLocked;
	Texture insaneTexLevels;
	Texture insaneTexLevelsDown;
	Texture insaneTexLevelsLocked;
	Texture easyTexLevelsInverted;
	Texture easyTexLevelsDownInverted;
	Texture mediumTexLevelsInverted;
	Texture mediumTexLevelsDownInverted;
	Texture hardTexLevelsInverted;
	Texture hardTexLevelsDownInverted;
	Texture hardTexLevelsLockedInverted;
	Texture insaneTexLevelsInverted;
	Texture insaneTexLevelsDownInverted;
	Texture insaneTexLevelsLockedInverted;
	
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
		hardTexLevels = new Texture(
				"images/buttons/levels/btn_hard_levels.png");
		hardTexLevelsDown = new Texture(
				"images/buttons/levels/btn_hard_levels_down.png");
		hardTexLevelsLocked = new Texture(
				"images/buttons/levels/btn_hard_levels_locked.png");
		insaneTexLevels = new Texture(
				"images/buttons/levels/btn_insane_levels.png");
		insaneTexLevelsDown = new Texture(
				"images/buttons/levels/btn_insane_levels_down.png");
		insaneTexLevelsLocked = new Texture(
				"images/buttons/levels/btn_insane_levels_locked.png");
		
		easyTexLevelsInverted = new Texture(
				"images/buttons/levels_inverted/btn_easy_levels_inverted.png");
		easyTexLevelsDownInverted = new Texture(
				"images/buttons/levels_inverted/btn_easy_levels_down_inverted.png");
		mediumTexLevelsInverted = new Texture(
				"images/buttons/levels_inverted/btn_medium_levels_inverted.png");
		mediumTexLevelsDownInverted = new Texture(
				"images/buttons/levels_inverted/btn_medium_levels_down_inverted.png");
		hardTexLevelsInverted = new Texture(
				"images/buttons/levels_inverted/btn_hard_levels_inverted.png");
		hardTexLevelsDownInverted = new Texture(
				"images/buttons/levels_inverted/btn_hard_levels_down_inverted.png");
		hardTexLevelsLockedInverted = new Texture(
				"images/buttons/levels_inverted/btn_hard_levels_locked_inverted.png");
		insaneTexLevelsInverted = new Texture(
				"images/buttons/levels_inverted/btn_insane_levels_inverted.png");
		insaneTexLevelsDownInverted = new Texture(
				"images/buttons/levels_inverted/btn_insane_levels_down_inverted.png");
		insaneTexLevelsLockedInverted = new Texture(
				"images/buttons/levels_inverted/btn_insane_levels_locked_inverted.png");
	}
	
	@Override
	public void render(float delta) {
		clearColor();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.input.setInputProcessor(stage);
		stage.draw();
	}

	@Override
	public void show() {
		profile = game.getProfileManager().retrieveProfile();
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
		table.add("").width(WorldRenderer.CAMERA_WIDTH).expandX().colspan(2).center();
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
		TextureRegionDrawable easyLevelsUp;
		TextureRegionDrawable easyLevelsDown;
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
			easyLevelsUp = new TextureRegionDrawable(
				new TextureRegion(easyTexLevelsInverted));
			easyLevelsDown = new TextureRegionDrawable(
				new TextureRegion(easyTexLevelsDownInverted));
		} else {
			easyLevelsUp = new TextureRegionDrawable(
					new TextureRegion(easyTexLevels));
			easyLevelsDown = new TextureRegionDrawable(
					new TextureRegion(easyTexLevelsDown));
		}
		ImageButtonStyle easyLevelsStyle = new ImageButtonStyle();
		easyLevelsStyle.up = easyLevelsUp;
		easyLevelsStyle.down = easyLevelsDown;
		ImageButton easyLevelsImagButton = new ImageButton(easyLevelsStyle);
		easyLevelsImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > buttonWidth || y < 0 || y > buttonHeight)) {
					Stairs.getSharedPrefs().putInteger("mostRecent", Stairs.EASY_LEVELS);
					Stairs.getSharedPrefs().flush();
					gameScreen = new GameScreen(game, Stairs.EASY_LEVELS, null);
					game.setScreen(gameScreen);
				}
			}
		});
		table.add(easyLevelsImagButton).size(buttonWidth, buttonHeight)
				.spaceBottom(buttonSpacing).spaceTop(100).colspan(2).center()
				.expandX().fillX();
		table.row();

		TextureRegionDrawable mediumLevelsUp;
		TextureRegionDrawable mediumLevelsDown;
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
			mediumLevelsUp = new TextureRegionDrawable(
				new TextureRegion(mediumTexLevelsInverted));
			mediumLevelsDown = new TextureRegionDrawable(
				new TextureRegion(mediumTexLevelsDownInverted));
		} else {
			mediumLevelsUp = new TextureRegionDrawable(
					new TextureRegion(mediumTexLevels));
			mediumLevelsDown = new TextureRegionDrawable(
					new TextureRegion(mediumTexLevelsDown));
		}
		ImageButtonStyle mediumLevelsStyle = new ImageButtonStyle();
		mediumLevelsStyle.up = mediumLevelsUp;
		mediumLevelsStyle.down = mediumLevelsDown;
		ImageButton mediumLevelsImagButton = new ImageButton(mediumLevelsStyle);
		mediumLevelsImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > buttonWidth || y < 0 || y > buttonHeight)) {
					Stairs.getSharedPrefs().putInteger("mostRecent", Stairs.MEDIUM_LEVELS);
					Stairs.getSharedPrefs().flush();
					gameScreen = new GameScreen(game, Stairs.MEDIUM_LEVELS, null);
					game.setScreen(gameScreen);
				}
			}
		});
		table.add(mediumLevelsImagButton).size(buttonWidth, buttonHeight)
				.uniform().spaceBottom(buttonSpacing).colspan(2).center();
		table.row();

		mediumHighScore = profile.getHighScore(4);
		hardUnlocked = mediumHighScore >= 150 || Stairs.PAID_VERSION;
		TextureRegionDrawable hardLevelsUp;
		TextureRegionDrawable hardLevelsDown;
		if (hardUnlocked) {
			if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
				hardLevelsUp = new TextureRegionDrawable(
						new TextureRegion(hardTexLevelsInverted));
				hardLevelsDown = new TextureRegionDrawable(
						new TextureRegion(hardTexLevelsDownInverted));
			} else {
				hardLevelsUp = new TextureRegionDrawable(
						new TextureRegion(hardTexLevels));
				hardLevelsDown = new TextureRegionDrawable(
						new TextureRegion(hardTexLevelsDown));
			}
		} else {
			if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
				hardLevelsUp = new TextureRegionDrawable(
						new TextureRegion(hardTexLevelsLockedInverted));
				hardLevelsDown = new TextureRegionDrawable(
						new TextureRegion(hardTexLevelsLockedInverted));
			} else {
				hardLevelsUp = new TextureRegionDrawable(
						new TextureRegion(hardTexLevelsLocked));
				hardLevelsDown = new TextureRegionDrawable(
						new TextureRegion(hardTexLevelsLocked));
			}
		}
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
					Stairs.getSharedPrefs().putInteger("mostRecent", Stairs.HARD_LEVELS);
					Stairs.getSharedPrefs().flush();
					gameScreen = new GameScreen(game, Stairs.HARD_LEVELS, null);
					game.setScreen(gameScreen);
				}
			}
		});
		table.add(hardLevelsImagButton).size(buttonWidth, buttonHeight)
				.uniform().spaceBottom(buttonSpacing).colspan(2).center();
		table.row();

		hardHighScore = profile.getHighScore(7);
		insaneUnlocked = hardHighScore >= 100 || Stairs.PAID_VERSION;
		TextureRegionDrawable insaneLevelsUp;
		TextureRegionDrawable insaneLevelsDown;
		if (insaneUnlocked) {
			if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
				insaneLevelsUp = new TextureRegionDrawable(
						new TextureRegion(insaneTexLevelsInverted));
				insaneLevelsDown = new TextureRegionDrawable(
						new TextureRegion(insaneTexLevelsDownInverted));
			} else {
				insaneLevelsUp = new TextureRegionDrawable(
						new TextureRegion(insaneTexLevels));
				insaneLevelsDown = new TextureRegionDrawable(
						new TextureRegion(insaneTexLevelsDown));
			}
		} else {
			if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
				insaneLevelsUp = new TextureRegionDrawable(
						new TextureRegion(insaneTexLevelsLockedInverted));
				insaneLevelsDown = new TextureRegionDrawable(
						new TextureRegion(insaneTexLevelsLockedInverted));
			} else {
				insaneLevelsUp = new TextureRegionDrawable(
						new TextureRegion(insaneTexLevelsLocked));
				insaneLevelsDown = new TextureRegionDrawable(
						new TextureRegion(insaneTexLevelsLocked));
			}
		}
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
					Stairs.getSharedPrefs().putInteger("mostRecent", Stairs.INSANE_LEVELS);
					Stairs.getSharedPrefs().flush();
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
		TextureRegionDrawable menuUp;
		TextureRegionDrawable menuDown;
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
			menuUp = new TextureRegionDrawable(new TextureRegion(game.menuTexInverted));
			menuDown = new TextureRegionDrawable(new TextureRegion(game.menuTexDownInverted));
		} else {
			menuUp = new TextureRegionDrawable(new TextureRegion(game.menuTex));
			menuDown = new TextureRegionDrawable(new TextureRegion(game.menuTexDown));
		}
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
	}
}
