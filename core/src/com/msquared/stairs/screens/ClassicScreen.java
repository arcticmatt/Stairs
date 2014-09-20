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

public class ClassicScreen extends AbstractScreen implements Screen {
	CharSequence gameScore;
	float stateTime;
	TextureRegion currentFrame;
	GameScreen gameScreen;
	Profile profile;
	int mediumHighScore;
	int hardHighScore;
	boolean hardUnlocked;
	boolean insaneUnlocked;
	Texture easyTexClassic;
	Texture easyTexClassicDown;
	Texture mediumTexClassic;
	Texture mediumTexClassicDown;
	Texture hardTexClassic;
	Texture hardTexClassicDown;
	Texture hardTexClassicLocked;
	Texture insaneTexClassic;
	Texture insaneTexClassicDown;
	Texture insaneTexClassicLocked;
	Texture easyTexClassicInverted;
	Texture easyTexClassicDownInverted;
	Texture mediumTexClassicInverted;
	Texture mediumTexClassicDownInverted;
	Texture hardTexClassicInverted;
	Texture hardTexClassicDownInverted;
	Texture hardTexClassicLockedInverted;
	Texture insaneTexClassicInverted;
	Texture insaneTexClassicDownInverted;
	Texture insaneTexClassicLockedInverted;
	
	// Constructor to keep a reference to the main Game class
	public ClassicScreen(Stairs game) {
		super(game);
		stateTime = 0f;
		easyTexClassic = new Texture(
				"images/buttons/classic/btn_easy_classic.png");
		easyTexClassicDown = new Texture(
				"images/buttons/classic/btn_easy_classic_down.png");
		mediumTexClassic = new Texture(
				"images/buttons/classic/btn_medium_classic.png");
		mediumTexClassicDown = new Texture(
				"images/buttons/classic/btn_medium_classic_down.png");
		hardTexClassic = new Texture(
				"images/buttons/classic/btn_hard_classic.png");
		hardTexClassicDown = new Texture(
				"images/buttons/classic/btn_hard_classic_down.png");
		hardTexClassicLocked = new Texture(
				"images/buttons/classic/btn_hard_classic_locked.png");
		insaneTexClassic = new Texture(
				"images/buttons/classic/btn_insane_classic.png");
		insaneTexClassicDown = new Texture(
				"images/buttons/classic/btn_insane_classic_down.png");
		insaneTexClassicLocked = new Texture(
				"images/buttons/classic/btn_insane_classic_locked.png");
		
		easyTexClassicInverted = new Texture(
				"images/buttons/classic_inverted/btn_easy_classic_inverted.png");
		easyTexClassicDownInverted = new Texture(
				"images/buttons/classic_inverted/btn_easy_classic_down_inverted.png");
		mediumTexClassicInverted = new Texture(
				"images/buttons/classic_inverted/btn_medium_classic_inverted.png");
		mediumTexClassicDownInverted = new Texture(
				"images/buttons/classic_inverted/btn_medium_classic_down_inverted.png");
		hardTexClassicInverted = new Texture(
				"images/buttons/classic_inverted/btn_hard_classic_inverted.png");
		hardTexClassicDownInverted = new Texture(
				"images/buttons/classic_inverted/btn_hard_classic_down_inverted.png");
		hardTexClassicLockedInverted = new Texture(
				"images/buttons/classic_inverted/btn_hard_classic_locked_inverted.png");
		insaneTexClassicInverted = new Texture(
				"images/buttons/classic_inverted/btn_insane_classic_inverted.png");
		insaneTexClassicDownInverted = new Texture(
				"images/buttons/classic_inverted/btn_insane_classic_down_inverted.png");
		insaneTexClassicLockedInverted = new Texture(
				"images/buttons/classic_inverted/btn_insane_classic_locked_inverted.png");
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
		 * Classic buttons
		 */
		table.row();
		TextureRegionDrawable easyClassicUp;
		TextureRegionDrawable easyClassicDown;
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
			easyClassicUp = new TextureRegionDrawable(
				new TextureRegion(easyTexClassicInverted));
			easyClassicDown = new TextureRegionDrawable(
				new TextureRegion(easyTexClassicDownInverted));
		} else {
			easyClassicUp = new TextureRegionDrawable(
					new TextureRegion(easyTexClassic));
			easyClassicDown = new TextureRegionDrawable(
					new TextureRegion(easyTexClassicDown));
		}
		ImageButtonStyle easyClassicStyle = new ImageButtonStyle();
		easyClassicStyle.up = easyClassicUp;
		easyClassicStyle.down = easyClassicDown;
		ImageButton easyClassicImagButton = new ImageButton(easyClassicStyle);
		easyClassicImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > buttonWidth || y < 0 || y > buttonHeight)) {
					Stairs.getSharedPrefs().putInteger("mostRecent", Stairs.EASY_CLASSIC);
					Stairs.getSharedPrefs().flush();
					gameScreen = new GameScreen(game, Stairs.EASY_CLASSIC, null);
					game.setScreen(gameScreen);
				}
			}
		});
		table.add(easyClassicImagButton).size(buttonWidth, buttonHeight)
				.spaceBottom(buttonSpacing).spaceTop(100).colspan(2).center()
				.expandX().fillX();
		table.row();

		TextureRegionDrawable mediumClassicUp;
		TextureRegionDrawable mediumClassicDown;
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
			mediumClassicUp = new TextureRegionDrawable(
				new TextureRegion(mediumTexClassicInverted));
			mediumClassicDown = new TextureRegionDrawable(
				new TextureRegion(mediumTexClassicDownInverted));
		} else {
			mediumClassicUp = new TextureRegionDrawable(
					new TextureRegion(mediumTexClassic));
			mediumClassicDown = new TextureRegionDrawable(
					new TextureRegion(mediumTexClassicDown));
		}
		ImageButtonStyle mediumClassicStyle = new ImageButtonStyle();
		mediumClassicStyle.up = mediumClassicUp;
		mediumClassicStyle.down = mediumClassicDown;
		ImageButton mediumClassicImagButton = new ImageButton(
				mediumClassicStyle);
		mediumClassicImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > buttonWidth || y < 0 || y > buttonHeight)) {
					Stairs.getSharedPrefs().putInteger("mostRecent", Stairs.MEDIUM_CLASSIC);
					Stairs.getSharedPrefs().flush();
					gameScreen = new GameScreen(game, Stairs.MEDIUM_CLASSIC, null);
					game.setScreen(gameScreen);
				}
			}
		});
		table.add(mediumClassicImagButton).size(buttonWidth, buttonHeight)
				.uniform().spaceBottom(buttonSpacing).colspan(2).center();
		table.row();

		mediumHighScore = profile.getHighScore(16);
		hardUnlocked = mediumHighScore >= 120 || Stairs.PAID_VERSION;
		TextureRegionDrawable hardClassicUp;
		TextureRegionDrawable hardClassicDown;
		if (hardUnlocked) {
			if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
				hardClassicUp = new TextureRegionDrawable(
						new TextureRegion(hardTexClassicInverted));
				hardClassicDown = new TextureRegionDrawable(
						new TextureRegion(hardTexClassicDownInverted));
			} else {
				hardClassicUp = new TextureRegionDrawable(
						new TextureRegion(hardTexClassic));
				hardClassicDown = new TextureRegionDrawable(
						new TextureRegion(hardTexClassicDown));
			}
		} else {
			if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
				hardClassicUp = new TextureRegionDrawable(
						new TextureRegion(hardTexClassicLockedInverted));
				hardClassicDown = new TextureRegionDrawable(
						new TextureRegion(hardTexClassicLockedInverted));
			} else {
				hardClassicUp = new TextureRegionDrawable(
						new TextureRegion(hardTexClassicLocked));
				hardClassicDown = new TextureRegionDrawable(
						new TextureRegion(hardTexClassicLocked));
			}
		}
		ImageButtonStyle hardClassicStyle = new ImageButtonStyle();
		hardClassicStyle.up = hardClassicUp;
		hardClassicStyle.down = hardClassicDown;
		ImageButton hardClassicImagButton = new ImageButton(hardClassicStyle);
		hardClassicImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > buttonWidth || y < 0 || y > buttonHeight)
						&& hardUnlocked) {
					Stairs.getSharedPrefs().putInteger("mostRecent", Stairs.HARD_CLASSIC);
					Stairs.getSharedPrefs().flush();
					gameScreen = new GameScreen(game, Stairs.HARD_CLASSIC, null);
					game.setScreen(gameScreen);
				}
			}
		});
		table.add(hardClassicImagButton).size(buttonWidth, buttonHeight)
				.uniform().spaceBottom(buttonSpacing).colspan(2).center();
		table.row();

		hardHighScore = profile.getHighScore(19);
		insaneUnlocked = hardHighScore >= 75 || Stairs.PAID_VERSION;
		TextureRegionDrawable insaneClassicUp;
		TextureRegionDrawable insaneClassicDown;
		if (insaneUnlocked) {
			if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
				insaneClassicUp = new TextureRegionDrawable(
						new TextureRegion(insaneTexClassicInverted));
				insaneClassicDown = new TextureRegionDrawable(
						new TextureRegion(insaneTexClassicDownInverted));
			} else {
				insaneClassicUp = new TextureRegionDrawable(
						new TextureRegion(insaneTexClassic));
				insaneClassicDown = new TextureRegionDrawable(
						new TextureRegion(insaneTexClassicDown));
			}
		} else {
			if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
				insaneClassicUp = new TextureRegionDrawable(
						new TextureRegion(insaneTexClassicLockedInverted));
				insaneClassicDown = new TextureRegionDrawable(
						new TextureRegion(insaneTexClassicLockedInverted));
			} else {
				insaneClassicUp = new TextureRegionDrawable(
						new TextureRegion(insaneTexClassicLocked));
				insaneClassicDown = new TextureRegionDrawable(
						new TextureRegion(insaneTexClassicLocked));
			}
		}
		ImageButtonStyle insaneClassicStyle = new ImageButtonStyle();
		insaneClassicStyle.up = insaneClassicUp;
		insaneClassicStyle.down = insaneClassicDown;
		ImageButton insaneClassicImagButton = new ImageButton(
				insaneClassicStyle);
		insaneClassicImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > buttonWidth || y < 0 || y > buttonHeight)
						&& insaneUnlocked) {
					Stairs.getSharedPrefs().putInteger("mostRecent", Stairs.INSANE_CLASSIC);
					Stairs.getSharedPrefs().flush();
					gameScreen = new GameScreen(game, Stairs.INSANE_CLASSIC, null);
					game.setScreen(gameScreen);
				}
			}
		});
		table.add(insaneClassicImagButton).size(buttonWidth, buttonHeight)
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

