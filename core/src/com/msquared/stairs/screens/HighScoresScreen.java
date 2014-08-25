package com.msquared.stairs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.profile.Profile;
import com.msquared.stairs.utils.DefaultActorListener;
import com.msquared.stairs.view.WorldRenderer;

public class HighScoresScreen extends AbstractScreen {
	CharSequence gameScore;
	Color winningColor = new Color(0, 255, 0, 1);
	Color scoreColor = new Color(255, 0, 0, 1);
	Table table;
	Profile profile;
	LabelStyle scoreStyle;

	// Constructor to keep a reference to the main Game class
	public HighScoresScreen(Stairs game) {
		super(game);
		skin = getSkin();
		scoreStyle = skin.get("score", LabelStyle.class);
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
		game.myRequestHandler.showAds(false);
		
		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();
		float heightRatio = this.height / WorldRenderer.CAMERA_HEIGHT;
		float widthRatio = this.width / WorldRenderer.CAMERA_WIDTH;
		
		Float imagWidthOrig = 100f;
		Float imagHeightOrig = 100f;
		final Float imagWidth = imagWidthOrig * heightRatio * (1 / widthRatio);
		final Float imagHeight = imagHeightOrig;
		
		table = new Table(getSkin());
		table.defaults().spaceBottom(2f);
		if (!game.htmlGame) {
			showScoreProfile();
		} else {
			showScorePrefs();
		}
		
		table.row();
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
		table.add(menuImagButton).colspan(2).
			spaceTop(5f).size(imagWidth, imagHeight).align(Align.center).expandX().fillX();
        table.setFillParent(true);
        stage.addActor(table);
	}
	
	public void showScoreProfile() {
		float scorePaddingLeft = 55f;
		float titlePadding = 20f;
		float scorePaddingRight = 125f;
		profile = game.getProfileManager().retrieveProfile();
		Skin skin = getSkin();
		LabelStyle labelStyle = skin.get("highscore", LabelStyle.class);
		Label scoreLabel = new Label("Highscores", labelStyle);
		scoreLabel.setAlignment(Align.center, Align.center);
		table.add(scoreLabel).colspan(2).center().expandX().fillX();
		table.row().spaceTop(10f).spaceBottom(10f);
		
		// Easy high scores
		table.add("E Levels").align(Align.left).padLeft(titlePadding);
		table.add("E Classic").align(Align.right).padRight(titlePadding);
		
		labelStyle = skin.get("score", LabelStyle.class);
		
		table.row();
		Label firstEasyLabel = getScoreLabelProfile(1, 1);
		Label firstEasyClassicLabel = getScoreLabelProfile(1, 13);
		table.add(firstEasyLabel).align(Align.left).padLeft(scorePaddingLeft);
		table.add(firstEasyClassicLabel).align(Align.left).padLeft(scorePaddingRight);
		
		table.row();
		Label secondEasyLabel = getScoreLabelProfile(2, 2);
		Label secondEasyClassicLabel = getScoreLabelProfile(2, 14);
		table.add(secondEasyLabel).align(Align.left).padLeft(scorePaddingLeft);
		table.add(secondEasyClassicLabel).align(Align.left).padLeft(scorePaddingRight);
		
		table.row();
		Label thirdEasyLabel = getScoreLabelProfile(3, 3);
		Label thirdEasyClassicLabel = getScoreLabelProfile(3, 15);
		table.add(thirdEasyLabel).align(Align.left).padLeft(scorePaddingLeft);
		table.add(thirdEasyClassicLabel).align(Align.left).padLeft(scorePaddingRight).spaceBottom(10f);
		
		// Medium high scores
		table.row();
		table.add("M Levels").align(Align.left).padLeft(titlePadding);
		table.add("M Classic").align(Align.right).padRight(titlePadding);
		
		table.row();
		Label firstMediumLabel = getScoreLabelProfile(1, 4);
		Label firstMediumClassicLabel = getScoreLabelProfile(1, 16);
		table.add(firstMediumLabel).align(Align.left).padLeft(scorePaddingLeft);
		table.add(firstMediumClassicLabel).align(Align.left).padLeft(scorePaddingRight);
		
		table.row();
		Label secondMediumLabel = getScoreLabelProfile(2, 5);
		Label secondMediumClassicLabel = getScoreLabelProfile(2, 17);
		table.add(secondMediumLabel).align(Align.left).padLeft(scorePaddingLeft);
		table.add(secondMediumClassicLabel).align(Align.left).padLeft(scorePaddingRight);
		
		table.row();
		Label thirdMediumLabel = getScoreLabelProfile(3, 6);
		Label thirdMediumClassicLabel = getScoreLabelProfile(3, 18);
		table.add(thirdMediumLabel).align(Align.left).padLeft(scorePaddingLeft);
		table.add(thirdMediumClassicLabel).align(Align.left).padLeft(scorePaddingRight).spaceBottom(10f);
		
		// Hard high scores
		table.row();
		table.add("H Levels").align(Align.left).padLeft(titlePadding);
		table.add("H Classic").align(Align.right).padRight(titlePadding);
		
		table.row();
		Label firstHardLabel = getScoreLabelProfile(1, 7);
		Label firstHardClassicLabel = getScoreLabelProfile(1, 19);
		table.add(firstHardLabel).align(Align.left).padLeft(scorePaddingLeft);
		table.add(firstHardClassicLabel).align(Align.left).padLeft(scorePaddingRight);
		
		table.row();
		Label secondHardLabel = getScoreLabelProfile(2, 8);
		Label secondHardClassicLabel = getScoreLabelProfile(2, 20);
		table.add(secondHardLabel).align(Align.left).padLeft(scorePaddingLeft);
		table.add(secondHardClassicLabel).align(Align.left).padLeft(scorePaddingRight);
		
		table.row();
		Label thirdHardLabel = getScoreLabelProfile(3, 9);
		Label thirdHardClassicLabel = getScoreLabelProfile(3, 21);
		table.add(thirdHardLabel).align(Align.left).padLeft(scorePaddingLeft);
		table.add(thirdHardClassicLabel).align(Align.left).padLeft(scorePaddingRight).spaceBottom(10f);
		
		// Insane high scores
		table.row();
		table.add("I Levels").align(Align.left).padLeft(titlePadding);
		table.add("I Classic").align(Align.right).padRight(titlePadding);
		
		table.row();
		Label firstInsaneLabel = getScoreLabelProfile(1, 10);
		Label firstInsaneClassicLabel = getScoreLabelProfile(1, 22);
		table.add(firstInsaneLabel).align(Align.left).padLeft(scorePaddingLeft);
		table.add(firstInsaneClassicLabel).align(Align.left).padLeft(scorePaddingRight);
		
		table.row();
		Label secondInsaneLabel = getScoreLabelProfile(2, 11);
		Label secondInsaneClassicLabel = getScoreLabelProfile(2, 23);
		table.add(secondInsaneLabel).align(Align.left).padLeft(scorePaddingLeft);
		table.add(secondInsaneClassicLabel).align(Align.left).padLeft(scorePaddingRight);
		
		table.row();
		Label thirdInsaneLabel = getScoreLabelProfile(3, 12);
		Label thirdInsaneClassicLabel = getScoreLabelProfile(3, 24);
		table.add(thirdInsaneLabel).align(Align.left).padLeft(scorePaddingLeft);
		table.add(thirdInsaneClassicLabel).align(Align.left).padLeft(scorePaddingRight).spaceBottom(0f);
	}
	
	public void showScorePrefs() {
		Preferences prefs = Stairs.getSharedPrefs();
		Skin skin = getSkin();
		LabelStyle labelStyle = skin.get("highscore", LabelStyle.class);
		Label scoreLabel = new Label("Highscores", labelStyle);
		scoreLabel.setAlignment(Align.center, Align.center);
		table.add(scoreLabel).colspan(2).center().expandX().fillX();
		table.row().spaceTop(10f).spaceBottom(10f);
		
		table.add("Easy").expandX().center();
		
		skin = getSkin();
		labelStyle = skin.get("score", LabelStyle.class);
		
		// Easy high scores
		String firstEasyScore = "1. " + prefs.getInteger("easyFirst", 0);
		Label firstEasyScoreLabel = new Label(firstEasyScore, labelStyle);
		firstEasyScoreLabel.setAlignment(Align.center, Align.center);
		table.row();
		table.add(firstEasyScoreLabel).expandX();
		
		String secondEasyScore = "2. " + prefs.getInteger("easySecond", 0);
		Label secondEasyScoreLabel = new Label(secondEasyScore, labelStyle);
		secondEasyScoreLabel.setAlignment(Align.center, Align.center);
		table.row();
		table.add(secondEasyScoreLabel).expandX();
		
		String thirdEasyScore = "3. " + prefs.getInteger("easyThird", 0);
		Label thirdEasyScoreLabel = new Label(thirdEasyScore, labelStyle);
		thirdEasyScoreLabel.setAlignment(Align.center, Align.center);
		table.row();
		table.add(thirdEasyScoreLabel).expandX().spaceBottom(10f);
		
		// Medium high scores
		table.row();
		table.add("Medium").expandX().center().setWidgetHeight(2f);
		
		String firstMediumScore = "1. " + prefs.getInteger("mediumFirst", 0);
		Label firstMediumScoreLabel = new Label(firstMediumScore, labelStyle);
		firstMediumScoreLabel.setAlignment(Align.center, Align.center);
		table.row();
		table.add(firstMediumScoreLabel).expandX();
		
		String secondMediumScore = "2. " + prefs.getInteger("mediumSecond", 0);
		Label secondMediumScoreLabel = new Label(secondMediumScore, labelStyle);
		secondMediumScoreLabel.setAlignment(Align.center, Align.center);
		table.row();
		table.add(secondMediumScoreLabel).expandX();
		
		String thirdMediumScore = "3. " + prefs.getInteger("mediumThird", 0);
		Label thirdMediumScoreLabel = new Label(thirdMediumScore,labelStyle);
		thirdMediumScoreLabel.setAlignment(Align.center, Align.center);
		table.row();
		table.add(thirdMediumScoreLabel).expandX().spaceBottom(10f);
		
		// Hard high scores
		table.row();
		table.add("Hard").expandX().center();

		String firstHardScore = "1. " + prefs.getInteger("hardFirst", 0);
		Label firstHardScoreLabel = new Label(firstHardScore, labelStyle);
		firstHardScoreLabel.setAlignment(Align.center, Align.center);
		table.row();
		table.add(firstHardScoreLabel).expandX();

		String secondHardScore = "2. " + prefs.getInteger("hardSecond", 0);
		Label secondHardScoreLabel = new Label(secondHardScore, labelStyle);
		secondHardScoreLabel.setAlignment(Align.center, Align.center);
		table.row();
		table.add(secondHardScoreLabel).expandX();

		String thirdHardScore = "3. " + prefs.getInteger("hardThird", 0);
		Label thirdHardScoreLabel = new Label(thirdHardScore, labelStyle);
		thirdHardScoreLabel.setAlignment(Align.center, Align.center);
		table.row();
		table.add(thirdHardScoreLabel).expandX().spaceBottom(10f);
		
		// Insane high scores
		table.row();
		table.add("Insane").expandX().center();

		String firstInsaneScore = "1. " + prefs.getInteger("insaneFirst", 0);
		Label firstInsaneScoreLabel = new Label(firstInsaneScore, labelStyle);
		firstInsaneScoreLabel.setAlignment(Align.center, Align.center);
		table.row();
		table.add(firstInsaneScoreLabel).expandX();

		String secondInsaneScore = "2. " + prefs.getInteger("insaneSecond", 0);
		Label secondInsaneScoreLabel = new Label(secondInsaneScore, labelStyle);
		secondInsaneScoreLabel.setAlignment(Align.center, Align.center);
		table.row();
		table.add(secondInsaneScoreLabel).expandX();

		String thirdInsaneScore = "3. " + prefs.getInteger("insaneThird", 0);
		Label thirdInsaneScoreLabel = new Label(thirdInsaneScore, labelStyle);
		thirdInsaneScoreLabel.setAlignment(Align.center, Align.center);
		table.row();
		table.add(thirdInsaneScoreLabel).expandX().spaceBottom(50f);
	}
	
	public Label getScoreLabelProfile(int place, int key) {
		String labelString = "" + place +". " + profile.getHighScore(key);
		Label label = new Label(labelString, scoreStyle);
		label.setAlignment(Align.center, Align.center);
		return label;
	}
}
