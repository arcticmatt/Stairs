package com.msquared.stairs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.msquared.stairs.profile.ProfileManager;
import com.msquared.stairs.screens.ClassicScreen;
import com.msquared.stairs.screens.GameOverScreen;
import com.msquared.stairs.screens.GameScreen;
import com.msquared.stairs.screens.HighScoresScreen;
import com.msquared.stairs.screens.LevelsScreen;
import com.msquared.stairs.screens.MenuScreen;
import com.msquared.stairs.screens.SettingsScreen;
import com.msquared.stairs.screens.SplashScreen;
import com.msquared.stairs.utils.IActivityRequestHandler;
import com.msquared.stairs.utils.MusicManager;
import com.sun.net.ssl.internal.www.protocol.https.Handler;

public class Stairs extends Game {
	
	public boolean htmlGame;
	public boolean iphoneGame;
	public SplashScreen splashScreen;
	public MenuScreen menuScreen;
	public HighScoresScreen highScoresScreen;
	public GameScreen easyScreen;
	public GameScreen mediumScreen;
	public GameScreen hardScreen;
	public GameScreen insaneScreen;
	public SettingsScreen settingsScreen;
	public ClassicScreen classicScreen;
	public LevelsScreen levelsScreen;
	public Music gameMusicEasy;
	public Music gameMusicMedium;
	public Music gameMusicHard;
	public Music gameMusicInsane;
	public Music gameMusicEasyAlt;
	public Music gameMusicMediumAlt;
	public Music gameMusicHardAlt;
	public Music gameMusicInsaneAlt;
	public static final String LOG = "STAIRS";
	public MusicManager musicManager;
	public static final int EASY_LEVELS = 1;
	public static final int MEDIUM_LEVELS = 2;
	public static final int HARD_LEVELS = 3;
	public static final int INSANE_LEVELS = 4;
	public static final int EASY_CLASSIC = 5;
	public static final int MEDIUM_CLASSIC = 6;
	public static final int HARD_CLASSIC = 7;
	public static final int INSANE_CLASSIC = 8;
	public boolean paused;
	public IActivityRequestHandler myRequestHandler;
	
	public AssetManager assetManager;
	private ProfileManager profileManager;
	
	public Stairs(boolean html, boolean iphone, IActivityRequestHandler handler) {
		myRequestHandler = handler;
		htmlGame = html;
		iphoneGame = iphone;
		paused = false;
		assetManager = new AssetManager();
	}
	
	@Override
	public void create() {
		splashScreen = new SplashScreen(this);
		menuScreen = new MenuScreen(this);
		highScoresScreen = new HighScoresScreen(this);
		classicScreen = new ClassicScreen(this);
		levelsScreen = new LevelsScreen(this);
		settingsScreen = new SettingsScreen(this);
		musicManager = new MusicManager();
		
		if (!htmlGame) {
			profileManager = new ProfileManager();
			profileManager.retrieveProfile();
		}
		
		setScreen(splashScreen);
	}
	
	public ProfileManager getProfileManager() {
		return profileManager;
	}
	
	@Override
	public void pause() {
		super.pause();
		
		// Persist the profile, b/c we don't know if the player will come back
		profileManager.persist();
	}
}
