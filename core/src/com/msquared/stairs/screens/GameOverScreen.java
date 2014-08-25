package com.msquared.stairs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.profile.Profile;
import com.msquared.stairs.utils.DefaultActorListener;
import com.msquared.stairs.view.WorldRenderer;

public class GameOverScreen extends AbstractScreen{
	private int score = 0;
	CharSequence gameScore;
	Color winningColor = new Color(0, 255, 0, 1);
	Color scoreColor = new Color(255, 0, 0, 1);
	int difficulty;
	Stage stageDispose;
	Texture restartTex;
	Texture restartTexDown;
	
	// Constructor to keep a reference to the main Game class
	// Also keeps a reference to the Stage from the GameScreen to dispose.
	public GameOverScreen(final Stairs game, final int difficulty, Stage stageToDispose) {
		super(game);
		stageDispose = stageToDispose;
		this.difficulty = difficulty;
		
		stage.addListener(new InputListener() {
	        @Override
	    	public boolean keyDown(InputEvent event, int keycode) {
	    		if (keycode == Keys.SPACE || keycode == Keys.RIGHT
	    				|| keycode == Keys.LEFT || keycode == Keys.UP
	    				|| keycode == Keys.DOWN)
	    			game.setScreen(new GameScreen(game, difficulty, stage));
					return false;
	    	}
	    });
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		// Don't show ads
		game.myRequestHandler.showAds(true);
		
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		float heightRatio = this.height / WorldRenderer.CAMERA_HEIGHT;
		float widthRatio = this.width / WorldRenderer.CAMERA_WIDTH;
		
		Float imagWidthOrig = 100f;
		Float imagHeightOrig = 100f;
		final Float imagWidth = imagWidthOrig * heightRatio * (1 / widthRatio);
		final Float imagHeight = imagHeightOrig;
		Float imagPadding = 140f - (imagWidth - imagWidthOrig);
		
		table = super.getTable();
		table.defaults().spaceBottom(20);
		if (!game.htmlGame) {
			showScoreProfile(difficulty);
		} else {
			showScorePrefs(difficulty);
		}
			
		table.row();
		restartTex = new Texture("images/buttons/misc/btn_restart.png");
		restartTexDown = new Texture("images/buttons/misc/btn_restart_down.png");
		TextureRegionDrawable restartUp = new TextureRegionDrawable(new TextureRegion(restartTex));
		TextureRegionDrawable restartDown = new TextureRegionDrawable(new TextureRegion(restartTexDown));
		ImageButtonStyle restartStyle = new ImageButtonStyle();
		restartStyle.up = restartUp;
		restartStyle.down = restartDown;
		ImageButton restartImagButton = new ImageButton(restartStyle);
		restartImagButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!(x < 0 || x > imagWidth || y < 0 || y > imagHeight)) {
					game.setScreen(new GameScreen(game, difficulty, stage));
				}
			}
		});
		table.add(restartImagButton).size(imagWidth, imagHeight).align(Align.left).padLeft(imagPadding);;
		
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
				if (!(x < 0 || x > imagWidth || y < 0 || y > imagHeight)) {
					// Stop and dispose of the current music
					game.musicManager.stop();
					game.setScreen(game.menuScreen);
					stage.dispose();
				}
			}
		});
		table.add(menuImagButton).size(imagWidth, imagHeight).align(Align.right).padRight(imagPadding);
        table.setFillParent(true);
        stage.addActor(table);
	}
	
	// Show score using highscores from profile
	public void showScoreProfile(int difficulty) {
		int levelNum = Stairs.getSharedPrefs().getInteger("mostRecent", Stairs.EASY_LEVELS);
		boolean levels;
		if (levelNum <= 4) {
			levels = true;
		} else {
			levels = false;
		}
		Profile profile = game.getProfileManager().retrieveProfile();
		String highScoresString = "";
		int[] places = new int[3];
		int place = 0;
		if (difficulty == Stairs.EASY_LEVELS || difficulty == Stairs.EASY_CLASSIC) {
			if (levels) {
				highScoresString = "Easy Levels";
				place = profile.isEasyHighScore(score);
				places[0] = 1;
				places[1] = 2;
				places[2] = 3;
			} else {
				highScoresString = "Easy Classic";
				place = profile.isEasyClassicHighScore(score);
				places[0] = 13;
				places[1] = 14;
				places[2] = 15;
			}
		}
		else if (difficulty == Stairs.MEDIUM_LEVELS || difficulty == Stairs.MEDIUM_CLASSIC) {
			if (levels) {
				highScoresString = "Medium Levels";
				place = profile.isMediumHighScore(score);
				places[0] = 4;
				places[1] = 5;
				places[2] = 6;
			} else {
				highScoresString = "Medium Classic";
				place = profile.isMediumClassicHighScore(score);
				places[0] = 16;
				places[1] = 17;
				places[2] = 18;
			}
		}
		else if (difficulty == Stairs.HARD_LEVELS || difficulty == Stairs.HARD_CLASSIC) {
			if (levels) {
				highScoresString = "Hard Levels";
				place = profile.isHardHighScore(score);
				places[0] = 7;
				places[1] = 8;
				places[2] = 9;
			} else {
				highScoresString = "Hard Classic";
				place = profile.isHardClassicHighScore(score);
				places[0] = 19;
				places[1] = 20;
				places[2] = 21;
			}
		} else if (difficulty == Stairs.INSANE_LEVELS || difficulty == Stairs.INSANE_CLASSIC){
			if (levels) {
				highScoresString = "Insane Levels";
				place = profile.isInsaneHighScore(score);
				places[0] = 10;
				places[1] = 11;
				places[2] = 12;
			} else {
				highScoresString = "Insane Classic";
				place = profile.isInsaneClassicHighScore(score);
				places[0] = 22;
				places[1] = 23;
				places[2] = 24;
			}
		}
		
		if (place != 0) {
			Skin skin = getSkin();
			LabelStyle labelStyle = skin.get("highscore", LabelStyle.class);
			Label scoreLabel = new Label("NEW HIGH SCORE!", labelStyle);
			scoreLabel.setAlignment(Align.center, Align.center);
			table.add(scoreLabel).colspan(2).center().expandX().fillX();
			table.row();
		} else {
			Label scoreLabel = new Label("You scored: " + score, getSkin(), "mscore");
			scoreLabel.setAlignment(Align.center, Align.center);
			scoreLabel.setColor(scoreColor);
			table.add(scoreLabel).colspan(2).center().expandX().fillX();
			table.row();
		}
		Label highScoresLabel = new Label(highScoresString, getSkin(), "mscore");
		table.add(highScoresLabel).colspan(2).center().spaceTop(50f);
		table.row();
		
		// First high score
		String firstHighScore = "1. " + profile.getHighScore(places[0]);
		Label firstHighScoreLabel = new Label(firstHighScore, getSkin(), "mscore");
		firstHighScoreLabel.setAlignment(Align.center, Align.center);
		if (place == 1)
			firstHighScoreLabel.setColor(winningColor);
		table.row();
		table.add(firstHighScoreLabel).colspan(2).center();
		
		// First high score
		String secondHighScore = "2. " + profile.getHighScore(places[1]);
		Label secondHighScoreLabel = new Label(secondHighScore, getSkin(), "mscore");
		secondHighScoreLabel.setAlignment(Align.center, Align.center);
		if (place == 2)
			secondHighScoreLabel.setColor(winningColor);
		table.row();
		table.add(secondHighScoreLabel).colspan(2).center();

		// First high score
		String thirdHighScore = "3. " + profile.getHighScore(places[2]);
		Label thirdHighScoreLabel = new Label(thirdHighScore, getSkin(), "mscore");
		thirdHighScoreLabel.setAlignment(Align.center, Align.center);
		if (place == 3) 
			thirdHighScoreLabel.setColor(winningColor);
		table.row();
		table.add(thirdHighScoreLabel).colspan(2).center().spaceBottom(50f);
	}
	
	// Show score using highscores from prefs
	public void showScorePrefs(int difficulty) {
		Preferences prefs = Stairs.getSharedPrefs();
		String highScoresLabel = "";
		int[] scores = new int[3];
		int place = 0;
		if (difficulty == MenuScreen.EASY) {
			highScoresLabel = "High Scores: Easy";
			scores[0] = prefs.getInteger("easyFirst", 0);
			scores[1] = prefs.getInteger("easySecond", 0);
			scores[2] = prefs.getInteger("easyThird", 0);
			for (int i = 0; i < 3; i++) {
				if (score > scores[i]) {
					place = i + 1;
					int temp = scores[i];
					scores[i] = score;
					while (i < 2) {
						int temp2 = scores[i + 1];
						scores[i + 1] = temp;
						temp = temp2;
						i++;
					}
					break;
				}
			}
			prefs.putInteger("easyFirst", scores[0]);
			prefs.putInteger("easySecond",  scores[1]);
			prefs.putInteger("easyThird", scores[2]);
		}
		else if (difficulty == MenuScreen.MEDIUM) {
			highScoresLabel = "High Scores: Medium";
			scores[0] = prefs.getInteger("mediumFirst", 0);
			scores[1] = prefs.getInteger("mediumSecond", 0);
			scores[2] = prefs.getInteger("mediumThird", 0);
			for (int i = 0; i < 3; i++) {
				if (score > scores[i]) {
					place = i + 1;
					int temp = scores[i];
					scores[i] = score;
					while (i < 2) {
						int temp2 = scores[i + 1];
						scores[i + 1] = temp;
						temp = temp2;
						i++;
					}
					break;
				}
			}
			prefs.putInteger("mediumFirst", scores[0]);
			prefs.putInteger("mediumSecond",  scores[1]);
			prefs.putInteger("mediumThird", scores[2]);
		}
		else if (difficulty == MenuScreen.HARD) {
			highScoresLabel = "High Scores: Hard";
			scores[0] = prefs.getInteger("hardFirst", 0);
			scores[1] = prefs.getInteger("hardSecond", 0);
			scores[2] = prefs.getInteger("hardThird", 0);
			for (int i = 0; i < 3; i++) {
				if (score > scores[i]) {
					place = i + 1;
					int temp = scores[i];
					scores[i] = score;
					while (i < 2) {
						int temp2 = scores[i + 1];
						scores[i + 1] = temp;
						temp = temp2;
						i++;
					}
					break;
				}
			}
			prefs.putInteger("hardFirst", scores[0]);
			prefs.putInteger("hardSecond",  scores[1]);
			prefs.putInteger("hardThird", scores[2]);
		} else if (difficulty == MenuScreen.INSANE){
			highScoresLabel = "High Scores: Insane";
			scores[0] = prefs.getInteger("insaneFirst", 0);
			scores[1] = prefs.getInteger("insaneSecond", 0);
			scores[2] = prefs.getInteger("insaneThird", 0);
			for (int i = 0; i < 3; i++) {
				if (score > scores[i]) {
					place = i + 1;
					int temp = scores[i];
					scores[i] = score;
					while (i < 2) {
						int temp2 = scores[i + 1];
						scores[i + 1] = temp;
						temp = temp2;
						i++;
					}
					break;
				}
			}
			prefs.putInteger("insaneFirst", scores[0]);
			prefs.putInteger("insaneSecond",  scores[1]);
			prefs.putInteger("insaneThird", scores[2]);
		}
		
		if (place != 0) {
			LabelStyle labelStyle = getSkin().get("highscore", LabelStyle.class);
			Label scoreLabel = new Label("NEW HIGH SCORE!", labelStyle);
			scoreLabel.setAlignment(Align.center, Align.center);
			table.add(scoreLabel).colspan(2).center().expandX().fillX();
			table.row();
		} else {
			Label scoreLabel = new Label("You scored: " + score, getSkin());
			scoreLabel.setAlignment(Align.center, Align.center);
			scoreLabel.setColor(scoreColor);
			table.add(scoreLabel).colspan(2).center().expandX().fillX();
			table.row();
		}
		table.add(highScoresLabel).colspan(2).center().spaceTop(50f);
		table.row();
		
		// First high score
		String firstHighScore = "1. " + scores[0];
		Label firstHighScoreLabel = new Label(firstHighScore, getSkin());
		firstHighScoreLabel.setAlignment(Align.center, Align.center);
		if (place == 1)
			firstHighScoreLabel.setColor(winningColor);
		table.row();
		table.add(firstHighScoreLabel).colspan(2).center();
		
		// First high score
		String secondHighScore = "2. " + scores[1];
		Label secondHighScoreLabel = new Label(secondHighScore, getSkin());
		secondHighScoreLabel.setAlignment(Align.center, Align.center);
		if (place == 2)
			secondHighScoreLabel.setColor(winningColor);
		table.row();
		table.add(secondHighScoreLabel).colspan(2).center();

		// First high score
		String thirdHighScore = "3. " + scores[2];
		Label thirdHighScoreLabel = new Label(thirdHighScore, getSkin());
		thirdHighScoreLabel.setAlignment(Align.center, Align.center);
		if (place == 3) 
			thirdHighScoreLabel.setColor(winningColor);
		table.row();
		table.add(thirdHighScoreLabel).colspan(2).center().spaceBottom(50f);
	}
	
	public void setScore(int gameScore) {
		score = gameScore;
	}

	@Override
	public void hide() {
		if (stageDispose != null) {
			stageDispose.dispose();
		}
		restartTex.dispose();
		restartTexDown.dispose();
		dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
	}
}
