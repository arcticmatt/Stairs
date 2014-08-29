package com.msquared.stairs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.profile.Profile;
import com.msquared.stairs.utils.DefaultActorListener;
import com.msquared.stairs.view.WorldRenderer;

/**
 * A simple options screen.
 */
public class SettingsScreen extends AbstractScreen {
	Table table;
	Texture musicTexOn;
	Texture musicTexChecked;
	ImageButton musicToggle;
	Texture soundTexOn;
	Texture soundTexChecked;
	ImageButton soundToggle;
	Texture earlyTexOn;
	Texture earlyTexChecked;
	ImageButton earlyToggle;
	Texture songTexOn;
	Texture songTexChecked;
	ImageButton songToggle;
	Texture invincTexOn;
	Texture invincTexChecked;
	ImageButton invincToggle;
	Texture blockTexOn;
	Texture blockTexChecked;
	ImageButton blockToggle;
	Profile profile;
	Preferences prefs;
	int insaneHighScoreLevels;
	int insaneHighScoreClassic;

	public SettingsScreen(Stairs game) {
		super(game);
		blockTexOn = new Texture("images/buttons/toggles/btn_block_on.png");
		blockTexChecked = new Texture("images/buttons/toggles/btn_block_off.png");
		invincTexOn = new Texture("images/buttons/toggles/btn_invinc_on.png");
		invincTexChecked = new Texture("images/buttons/toggles/btn_invinc_off.png");
		earlyTexOn = new Texture("images/buttons/toggles/btn_early_on.png");
		earlyTexChecked = new Texture("images/buttons/toggles/btn_early_off.png");
		songTexOn = new Texture("images/buttons/toggles/btn_song1.png");
		songTexChecked = new Texture("images/buttons/toggles/btn_song2.png");
		musicTexOn = new Texture("images/buttons/toggles/btn_music_on.png");
		musicTexChecked = new Texture("images/buttons/toggles/btn_music_off.png");
		soundTexOn = new Texture("images/buttons/toggles/btn_sound_on.png");
		soundTexChecked = new Texture("images/buttons/toggles/btn_sound_off.png");
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//		table.debug();
//		Table.drawDebug(stage);
		stage.draw();

	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		profile = game.getProfileManager().retrieveProfile();		
		prefs = Stairs.getSharedPrefs();
		// Show ads
		game.myRequestHandler.showAds(true);

		table = new Table(getSkin());

		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		float heightRatio = this.height / WorldRenderer.CAMERA_HEIGHT;
		float widthRatio = this.width / WorldRenderer.CAMERA_WIDTH;

		Float imagWidthOrig = 100f;
		Float imagHeightOrig = 100f;
		final Float imagWidth = imagWidthOrig * heightRatio * (1 / widthRatio);
		final Float imagHeight = imagHeightOrig;

		Float settingWidthOrig = 65f;
		Float settingHeightOrig = 65f;
		final Float settingWidth = settingWidthOrig * heightRatio * (1 / widthRatio);
		final Float settingHeight = settingHeightOrig;
		Float settingPadding = 40 - (settingWidth - settingWidthOrig);

		Float defaultSpaceBottom = 30f;
		Float mainButtonspaceTop = 70f;
		
		table.defaults().spaceBottom(defaultSpaceBottom);
		table.defaults().padLeft(0f);
		table.defaults().padRight(0f);
		
		// Invincible toggle (only display if score above 50 on either insane level)
		insaneHighScoreLevels = profile.getHighScore(10);
		insaneHighScoreClassic = profile.getHighScore(22);
		boolean showInvinc = insaneHighScoreLevels >= 50 || insaneHighScoreClassic >= 50 || Stairs.PAID_VERSION;
		if (showInvinc) {
			table.row().expandX().fillX();
			TextureRegionDrawable invincUp = new TextureRegionDrawable(new TextureRegion(invincTexOn));
			TextureRegionDrawable invincChecked = new TextureRegionDrawable(new TextureRegion(invincTexChecked));
			final ImageButtonStyle invincStyleOn = new ImageButtonStyle();
			invincStyleOn.up = invincUp;
			invincStyleOn.down = invincChecked;
			invincStyleOn.checked = invincChecked;
			final ImageButtonStyle invincStyleOff = new ImageButtonStyle();
			invincStyleOff.up = invincUp;
			invincStyleOff.down = invincUp;
			invincStyleOff.checked = invincChecked;                                     
			invincToggle = new ImageButton(invincStyleOn);
			if (prefs.getBoolean("invincOn", false)) {
				invincToggle.setChecked(false);
				invincToggle.setStyle(invincStyleOn);
			} else {
				invincToggle.setChecked(true);
				invincToggle.setStyle(invincStyleOff);
			}
			invincToggle.addListener(new DefaultActorListener() {
				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					if (!(x < 0 || x > settingWidth || y < 0 || y > settingHeight)) {
						boolean invincOn = prefs.getBoolean("invincOn", false);
						if (invincOn) {
							prefs.putBoolean("invincOn", false);
							prefs.flush();
							invincToggle.setChecked(true);
							// Sleep for a little so that clicking really fast
							// doesn't cause the button to bug out
							sleep(200);
							invincToggle.setStyle(invincStyleOff);
						} else {
							prefs.putBoolean("invincOn", true);
							prefs.flush();
							invincToggle.setChecked(false);
							// Sleep for a little so that clicking really fast
							// doesn't cause the button to bug out
							sleep(200);
							invincToggle.setStyle(invincStyleOn);
						}
					}
				}
			});
			if (Stairs.PAID_VERSION) {
				addPaidVersionButtons(settingPadding, settingWidth, settingHeight);
			} else {
				table.add(invincToggle).size(settingWidth, settingHeight).colspan(4)
						.align(Align.center);
			}
		}
		
		
		table.row().expandX().fillX();
		
		// Early toggle
		TextureRegionDrawable earlyUp = new TextureRegionDrawable(new TextureRegion(earlyTexOn));
		TextureRegionDrawable earlyChecked = new TextureRegionDrawable(new TextureRegion(earlyTexChecked));
		final ImageButtonStyle earlyStyleOn = new ImageButtonStyle();
		earlyStyleOn.up = earlyUp;
		earlyStyleOn.down = earlyChecked;
		earlyStyleOn.checked = earlyChecked;
		final ImageButtonStyle earlyStyleOff = new ImageButtonStyle();
		earlyStyleOff.up = earlyUp;
		earlyStyleOff.down = earlyUp;
		earlyStyleOff.checked = earlyChecked;                                     
		earlyToggle = new ImageButton(earlyStyleOn);
		if (prefs.getBoolean("earlyOn", true)) {
			earlyToggle.setChecked(false);
			earlyToggle.setStyle(earlyStyleOn);
		} else {
			earlyToggle.setChecked(true);
			earlyToggle.setStyle(earlyStyleOff);
		}
		earlyToggle.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > settingWidth || y < 0 || y > settingHeight)) {
					boolean earlyOn = prefs.getBoolean("earlyOn", true);
					if (earlyOn) {
						prefs.putBoolean("earlyOn", false);
						prefs.flush();
						earlyToggle.setChecked(true);
						// Sleep for a little so that clicking really fast
						// doesn't cause the button to bug out
						sleep(200);
						earlyToggle.setStyle(earlyStyleOff);
					} else {
						prefs.putBoolean("earlyOn", true);
						prefs.flush();
						earlyToggle.setChecked(false);
						// Sleep for a little so that clicking really fast
						// doesn't cause the button to bug out
						sleep(200);
						earlyToggle.setStyle(earlyStyleOn);
					}
				}
			}
		});
		table.add(earlyToggle).size(settingWidth, settingHeight)
				.align(Align.center).colspan(1).uniform().padLeft(settingPadding);

		// Song toggle
		TextureRegionDrawable songUp = new TextureRegionDrawable(
				new TextureRegion(songTexOn));
		TextureRegionDrawable songChecked = new TextureRegionDrawable(
				new TextureRegion(songTexChecked));
		final ImageButtonStyle songStyleOn = new ImageButtonStyle();
		songStyleOn.up = songUp;
		songStyleOn.down = songChecked;
		songStyleOn.checked = songChecked;
		final ImageButtonStyle songStyleOff = new ImageButtonStyle();
		songStyleOff.up = songUp;
		songStyleOff.down = songUp;
		songStyleOff.checked = songChecked;
		songToggle = new ImageButton(songStyleOn);
		if (prefs.getBoolean("songFirst", true)) {
			songToggle.setChecked(false);
			songToggle.setStyle(songStyleOn);
		} else {
			songToggle.setChecked(true);
			songToggle.setStyle(songStyleOff);
		}
		songToggle.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > settingWidth || y < 0 || y > settingHeight)) {
					boolean songOn = prefs.getBoolean("songFirst", true);
					if (songOn) {
						prefs.putBoolean("songFirst", false);
						prefs.flush();
						songToggle.setChecked(true);
						// Sleep for a little so that clicking really fast
						// doesn't cause the button to bug out
						sleep(200);
						songToggle.setStyle(songStyleOff);
					} else {
						prefs.putBoolean("songFirst", true);
						prefs.flush();
						songToggle.setChecked(false);
						// Sleep for a little so that clicking really fast
						// doesn't cause the button to bug out
						sleep(200);
						songToggle.setStyle(songStyleOn);
					}
				}
			}
		});
		table.add(songToggle).size(settingWidth, settingHeight)
				.align(Align.center).colspan(1).uniform();

		// Music toggle
		TextureRegionDrawable musicUp = new TextureRegionDrawable(new TextureRegion(musicTexOn));
		TextureRegionDrawable musicChecked = new TextureRegionDrawable(new TextureRegion(musicTexChecked));
		final ImageButtonStyle musicStyleOn = new ImageButtonStyle();
		musicStyleOn.up = musicUp;
		musicStyleOn.down = musicChecked;
		musicStyleOn.checked = musicChecked;
		final ImageButtonStyle musicStyleOff = new ImageButtonStyle();
		musicStyleOff.up = musicUp;
		musicStyleOff.down = musicUp;
		musicStyleOff.checked = musicChecked;
		musicToggle = new ImageButton(musicStyleOn);
		if (prefs.getBoolean("musicOn", true)) {
			musicToggle.setChecked(false);
			musicToggle.setStyle(musicStyleOn);
		} else {
			musicToggle.setChecked(true);
			musicToggle.setStyle(musicStyleOff);
		}
		musicToggle.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > settingWidth || y < 0 || y > settingHeight)) {
					boolean musicOn = prefs.getBoolean("musicOn", true);
					if (musicOn) {
						prefs.putBoolean("musicOn", false);
						prefs.flush();
						musicToggle.setChecked(true);
						// Sleep for a little so that clicking really fast
						// doesn't cause the button to bug out
						sleep(200);
						musicToggle.setStyle(musicStyleOff);
					} else {
						prefs.putBoolean("musicOn", true);
						prefs.flush();
						musicToggle.setChecked(false);
						// Sleep for a little so that clicking really fast
						// doesn't cause the button to bug out
						sleep(200);
						musicToggle.setStyle(musicStyleOn);
					}
				}
			}
		});
		table.add(musicToggle).size(settingWidth, settingHeight)
				.align(Align.center).colspan(1).uniform();


		// Sound of feet toggle
		TextureRegionDrawable soundUp = new TextureRegionDrawable(new TextureRegion(soundTexOn));
		TextureRegionDrawable soundChecked = new TextureRegionDrawable(new TextureRegion(soundTexChecked));
		final ImageButtonStyle soundStyleOn = new ImageButtonStyle();
		soundStyleOn.up = soundUp;
		soundStyleOn.down = soundChecked;
		soundStyleOn.checked = soundChecked;
		final ImageButtonStyle soundStyleOff = new ImageButtonStyle();
		soundStyleOff.up = soundUp;
		soundStyleOff.down = soundUp;
		soundStyleOff.checked = soundChecked;
		soundToggle = new ImageButton(soundStyleOn);
		if (prefs.getBoolean("soundsOn", true)) {
			soundToggle.setChecked(false);
			soundToggle.setStyle(soundStyleOn);
		} else {
			soundToggle.setChecked(true);
			soundToggle.setStyle(soundStyleOff);
		}
		soundToggle.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > settingWidth || y < 0 || y > settingHeight)) {
					boolean soundsOn = prefs.getBoolean("soundsOn", true);
					if (soundsOn) {
						prefs.putBoolean("soundsOn", false);
						prefs.flush();
						soundToggle.setChecked(true);
						// Sleep for a little so that clicking really fast
						// doesn't cause the button to bug out
						sleep(200);
						soundToggle.setStyle(soundStyleOff);
					} else {
						prefs.putBoolean("soundsOn", true);
						prefs.flush();
						soundToggle.setChecked(false);
						// Sleep for a little so that clicking really fast
						// doesn't cause the button to bug out
						sleep(200);
						soundToggle.setStyle(soundStyleOn);
					}
				}
			}
		});
		table.add(soundToggle).size(settingWidth, settingHeight)
				.align(Align.center).colspan(1).uniform().padRight(settingPadding);
		table.row().expandX().fillX().spaceTop(mainButtonspaceTop);

		// Main menu button
		TextureRegionDrawable menuUp = new TextureRegionDrawable(new TextureRegion(game.menuTex));
		TextureRegionDrawable menuDown = new TextureRegionDrawable(new TextureRegion(game.menuTexDown));
		ImageButtonStyle menuStyle = new ImageButtonStyle();
		menuStyle.up = menuUp;
		menuStyle.down = menuDown;
		ImageButton menuImagButton = new ImageButton(menuStyle);
		menuImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > imagWidth || y < 0 || y > imagWidth)) {
					game.setScreen(game.menuScreen);
				}
			}
		});
		table.add(menuImagButton).size(imagWidth, imagHeight).align(Align.center).colspan(4);
		
		if (prefs.getBoolean("showTutorialAdvice", true)) {
			Label tutorialAdvice = new Label("To watch a tutorial, touch the animation on the main menu",
					getSkin(), "mscore");
			table.row();
			tutorialAdvice.setAlignment(Align.center, Align.center);
			tutorialAdvice.setWrap(true);
			table.add(tutorialAdvice).colspan(4).center().expandX().fillX();
			prefs.putBoolean("showTutorialAdvice", false);
			prefs.flush();
		}
		table.setFillParent(true);
		stage.addActor(table);
	}
	
	public void addPaidVersionButtons(float padding, final float settingWidth, final float settingHeight) {
		table.add().size(settingWidth, settingHeight).colspan(1)
				.align(Align.center).padLeft(padding);
		
		table.add(invincToggle).size(settingWidth, settingHeight).colspan(1)
				.align(Align.center);
		
		TextureRegionDrawable blockUp = new TextureRegionDrawable(
				new TextureRegion(blockTexOn));
		TextureRegionDrawable blockChecked = new TextureRegionDrawable(
				new TextureRegion(blockTexChecked));
		final ImageButtonStyle blockStyleOn = new ImageButtonStyle();
		blockStyleOn.up = blockUp;
		blockStyleOn.down = blockChecked;
		blockStyleOn.checked = blockChecked;
		final ImageButtonStyle blockStyleOff = new ImageButtonStyle();
		blockStyleOff.up = blockUp;
		blockStyleOff.down = blockUp;
		blockStyleOff.checked = blockChecked;
		blockToggle = new ImageButton(blockStyleOn);
		if (prefs.getBoolean("blockOn", false)) {
			blockToggle.setChecked(false);
			blockToggle.setStyle(blockStyleOn);
		} else {
			blockToggle.setChecked(true);
			blockToggle.setStyle(blockStyleOff);
		}
		blockToggle.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > settingWidth || y < 0 || y > settingHeight)) {
					boolean blockOn = prefs.getBoolean("blockOn", false);
					if (blockOn) {
						prefs.putBoolean("blockOn", false);
						prefs.flush();
						blockToggle.setChecked(true);
						// Sleep for a little so that clicking really fast
						// doesn't cause the button to bug out
						sleep(200);
						blockToggle.setStyle(blockStyleOff);
					} else {
						prefs.putBoolean("blockOn", true);
						prefs.flush();
						blockToggle.setChecked(false);
						// Sleep for a little so that clicking really fast
						// doesn't cause the button to bug out
						sleep(200);
						blockToggle.setStyle(blockStyleOn);
					}
				}
			}
		});
		table.add(blockToggle).size(settingWidth, settingHeight).colspan(1)
				.align(Align.center);
		
		table.add().size(settingWidth, settingHeight).colspan(1)
				.align(Align.center).padRight(padding);
	}
	
	public  void sleep(int time) {
		// Causes thread to sleep
		try {
		    Thread.sleep(time);
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
}
