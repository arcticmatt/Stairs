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
	Texture invertTexOn;
	Texture invertTexChecked;
	ImageButton invertToggle;
	Texture lockedTex;
	Profile profile;
	Preferences prefs;

	public SettingsScreen(Stairs game) {
		super(game);
		// Locked/Paid textures
		blockTexOn = new Texture("images/buttons/toggles/btn_block_on.png");
		blockTexChecked = new Texture("images/buttons/toggles/btn_block_off.png");
		invincTexOn = new Texture("images/buttons/toggles/btn_invinc_on.png");
		invincTexChecked = new Texture("images/buttons/toggles/btn_invinc_off.png");
		invertTexOn = new Texture("images/buttons/toggles/btn_invinc_on.png");
		invertTexChecked = new Texture("images/buttons/toggles/btn_invinc_off.png");
		
		// Regular textures
		earlyTexOn = new Texture("images/buttons/toggles/btn_early_on.png");
		earlyTexChecked = new Texture("images/buttons/toggles/btn_early_off.png");
		songTexOn = new Texture("images/buttons/toggles/btn_song1.png");
		songTexChecked = new Texture("images/buttons/toggles/btn_song2.png");
		musicTexOn = new Texture("images/buttons/toggles/btn_music_on.png");
		musicTexChecked = new Texture("images/buttons/toggles/btn_music_off.png");
		soundTexOn = new Texture("images/buttons/toggles/btn_sound_on.png");
		soundTexChecked = new Texture("images/buttons/toggles/btn_sound_off.png");
		lockedTex = new Texture("images/buttons/toggles/btn_locked.png");
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

		/*
		 * Add unlockable buttons (invincible toggle, block mode toggle). Adds 
		 * a new row before adding these buttons.
		 */
		addUnlockableButtons(settingPadding, settingWidth, settingHeight);

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

	public void addUnlockableButtons(float padding, final float settingWidth, final float settingHeight) {
        int mediumHighScoreLevels = profile.getHighScore(4);
        int mediumHighScoreClassic = profile.getHighScore(16);
        int hardHighScoreLevels = profile.getHighScore(7);
        int hardHighScoreClassic = profile.getHighScore(19);
		int insaneHighScoreLevels = profile.getHighScore(10);
		int insaneHighScoreClassic = profile.getHighScore(22);
		
        // Make new row
        table.row().expandX().fillX();
		
		// Add color scheme toggle button
		boolean showInvert = hardHighScoreLevels >= 50 || hardHighScoreClassic >= 50 || Stairs.PAID_VERSION;
		TextureRegionDrawable invertUp;
        TextureRegionDrawable invertChecked;
        if (showInvert) {
        	invertUp = new TextureRegionDrawable(new TextureRegion(invincTexOn));
        	invertChecked = new TextureRegionDrawable(new TextureRegion(invincTexChecked));
        } else {
        	invertUp = new TextureRegionDrawable(new TextureRegion(lockedTex));
        	invertChecked = new TextureRegionDrawable(new TextureRegion(lockedTex));
        }
        final ImageButtonStyle invertStyleOn = new ImageButtonStyle();
        invertStyleOn.up = invertUp;
        invertStyleOn.down = invertChecked;
        invertStyleOn.checked = invertChecked;
        final ImageButtonStyle invertStyleOff = new ImageButtonStyle();
        invertStyleOff.up = invertUp;
        invertStyleOff.down = invertUp;
        invertStyleOff.checked = invertChecked;
        invertToggle = new ImageButton(invertStyleOn);
        if (prefs.getBoolean("invertOn", false)) {
        	invertToggle.setChecked(false);
        	invertToggle.setStyle(invertStyleOn);
        } else {
        	invertToggle.setChecked(true);
        	invertToggle.setStyle(invertStyleOff);
        }
        if (showInvert) {
        	invertToggle.addListener(new DefaultActorListener() {
                @Override
                public void touchUp(InputEvent event, float x, float y,
                    int pointer, int button) {
                    if (!(x < 0 || x > settingWidth || y < 0 || y > settingHeight)) {
                        boolean invincOn = prefs.getBoolean("invertOn", false);
                        if (invincOn) {
                            prefs.putBoolean("invertOn", false);
                            prefs.flush();
                            invertToggle.setChecked(true);
                            // Sleep for a little so that clicking really fast
                            // doesn't cause the button to bug out
                            sleep(200);
                            invertToggle.setStyle(invertStyleOff);
                        } else {
                            prefs.putBoolean("invertOn", true);
                            prefs.flush();
                            invertToggle.setChecked(false);
                            // Sleep for a little so that clicking really fast
                            // doesn't cause the button to bug out
                            sleep(200);
                            invertToggle.setStyle(invertStyleOn);
                        }
                    }
                }
            });
        }
		table.add(invertToggle).size(settingWidth, settingHeight).colspan(1)
			.align(Align.center).padLeft(padding);

        // Add insane button
		boolean showInvinc = insaneHighScoreLevels >= 50 || insaneHighScoreClassic >= 50 || Stairs.PAID_VERSION;
        TextureRegionDrawable invincUp;
        TextureRegionDrawable invincChecked;
        if (showInvinc) {
			invincUp = new TextureRegionDrawable(new TextureRegion(invincTexOn));
			invincChecked = new TextureRegionDrawable(new TextureRegion(invincTexChecked));
        } else {
			invincUp = new TextureRegionDrawable(new TextureRegion(lockedTex));
			invincChecked = new TextureRegionDrawable(new TextureRegion(lockedTex));
        }
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
        if (showInvinc) {
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
        }
        table.add(invincToggle).size(settingWidth, settingHeight).colspan(1)
				.align(Align.center);

        // Add block button
        boolean showBlock = mediumHighScoreLevels >= 50 || mediumHighScoreClassic >= 50 || Stairs.PAID_VERSION;
        TextureRegionDrawable blockUp;
        TextureRegionDrawable blockChecked;
        if (showBlock) {
            blockUp = new TextureRegionDrawable(
                    new TextureRegion(blockTexOn));
            blockChecked = new TextureRegionDrawable(
                    new TextureRegion(blockTexChecked));
        } else {
            blockUp = new TextureRegionDrawable(
                    new TextureRegion(lockedTex));
            blockChecked = new TextureRegionDrawable(
                    new TextureRegion(lockedTex));
        }
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
        if (showBlock) {
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
        }
		table.add(blockToggle).size(settingWidth, settingHeight).colspan(1)
				.align(Align.center);

        // Add last dummy button
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
