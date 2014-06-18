package com.msquared.stairs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.controller.FootController;
import com.msquared.stairs.controller.StairController;
import com.msquared.stairs.controller.StairControllerEasy;
import com.msquared.stairs.controller.StairControllerHard;
import com.msquared.stairs.controller.StairControllerInsane;
import com.msquared.stairs.model.Stair;
import com.msquared.stairs.model.World;
import com.msquared.stairs.utils.DefaultActorListener;
import com.msquared.stairs.utils.MusicManager;
import com.msquared.stairs.view.WorldRenderer;

public class GameScreen extends AbstractScreen implements Screen {

	private World world;
	private WorldRenderer renderer;
	private FootController feetController;
	private StairController stairsController;
	boolean leftPressed = false;
	boolean rightPressed = false;
	private int width, height;
	public static final String TRIP = "You tripped on the stairs!";
	public static final String LEFT_FELL = "Your left foot fell off the stairs!";
	public static final String RIGHT_FELL = "Your right foot fell off the stairs!";
	public static final String LEFT_STEP = "Your left foot stepped off the stairs!";
	public static final String RIGHT_STEP = "Your right foot stepped off the stairs!";
	public static final String SKIP = "You skipped a step!";
	public static final int JUMP_LEFT = 0;
	public static final int JUMP_RIGHT = 1;
	public static final int FALL_LEFT = 2;
	public static final int FALL_RIGHT = 3;
	public static final String MUSIC_EASY = "sounds/stairs_easy.mp3";
	public static final String MUSIC_MEDIUM = "sounds/stairs_medium.mp3";
	public static final String MUSIC_HARD = "sounds/stairs_hard.mp3";
	public static final String MUSIC_INSANE = "sounds/stairs_insane.mp3";
	public static final String MUSIC_EASY_ALT = "sounds/stairs_alt_easy.mp3";
	public static final String MUSIC_MEDIUM_ALT = "sounds/stairs_alt_medium.mp3";
	public static final String MUSIC_HARD_ALT = "sounds/stairs_alt_hard.mp3";
	public static final String MUSIC_INSANE_ALT = "sounds/stairs_alt_insane.mp3";
	private long touchTime;
	private boolean touched;
	private final long MAX_TOUCH_TIME = 400l;
	public static final int GAME_OVER_TIME = 1000;
	Music music;
	public int screenWidth;
	public int screenHeight;
	Preferences prefs;


	int prevScore;
	int score;
	String scoreString;
	Label scoreLabel;
	int difficulty;

	// Constructor to keep a reference to the main Game class
	public GameScreen(Stairs game, int diff) {
		super(game);

		Stair.gameOver = false;
		Stair.movingLeft = false;
		Stair.movingRight = false;
		Stair.lowYSpeed = -400;
		Stair.highXSpeed = 250;
		FootController.jump_left = false;
		FootController.jump_right = false;
		difficulty = diff;
		prefs = Gdx.app.getPreferences("Preferences");

		world = new World();
		renderer = new WorldRenderer(world);
		
		world.difficulty = difficulty;
		switch (difficulty % 4) {
		case MenuScreen.EASY:
			stairsController = new StairControllerEasy(world, true);
			break;
		case MenuScreen.MEDIUM:
			stairsController = new StairController(world);
			break;
		case MenuScreen.HARD:
			stairsController = new StairControllerHard(world, true);
			break;
		case 0:
			stairsController = new StairControllerInsane(world, true);
			break;
		}
		world.createDemoWorld();

		
		feetController = new FootController(world);

		prevScore = 0;

		/* Set up score label */
		scoreString = "" + world.score;
		scoreLabel = new Label(scoreString, getSkin(), "mscore");
		float labelHeight = scoreLabel.getTextBounds().height;
		float xPos = 12;
		float yPos;
		if (true) {
			yPos = WorldRenderer.CAMERA_HEIGHT - 18;
		} else {
			yPos = WorldRenderer.CAMERA_HEIGHT - 38;
		}
		scoreLabel.setPosition(xPos, yPos - labelHeight);
		stage.addActor(scoreLabel);
		stage.addListener(new InputListener() {
	        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	    		if (x < 540 / 2 && !rightPressed && y < (860 - 860/9)) {
	    			leftPressed = true;
	    			feetController.processInput(JUMP_LEFT);
	    			FootController.jump_left = true;
	    		} else if (x >= 540 / 2 && !leftPressed && y < (860 - 860/9)) {
	    			rightPressed = true;
	    			feetController.processInput(JUMP_RIGHT);
	    			FootController.jump_right = true;
	    		}
	    		return true;
	        }
	        public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
	        	if (leftPressed) {
	    			leftPressed = false;
	    			feetController.processInput(FALL_LEFT);
	    			FootController.jump_left = false;
	    		} else if (rightPressed) {
	    			rightPressed = false;
	    			feetController.processInput(FALL_RIGHT);
	    			FootController.jump_right = false;
	    		}
	        }
	        
	        @Override
	    	public boolean keyDown(InputEvent event, int keycode) {
	    		if (keycode == Keys.LEFT && !rightPressed) {
	    			leftPressed = true;
	    			feetController.processInput(JUMP_LEFT);
	    			FootController.jump_left = true;
	    		} else if (keycode == Keys.RIGHT && !leftPressed) {
	    			rightPressed = true;
	    			feetController.processInput(JUMP_RIGHT);
	    			FootController.jump_right = true;
	    		}
	    		return true;
	    	}

	    	@Override
	    	public boolean keyUp(InputEvent event, int keycode) {
	    		if (keycode == Keys.LEFT) {
	    			leftPressed = false;
	    			feetController.processInput(FALL_LEFT);
	    			FootController.jump_left = false;
	    		}
	    		if (keycode == Keys.RIGHT) {
	    			rightPressed = false;
	    			feetController.processInput(FALL_RIGHT);
	    			FootController.jump_right = false;
	    		}
	    		return true;
	    	}
	        
	    });

		touchTime = 0;
		touched = false;
	}

	@Override
	public void render(float delta) {
		if (!game.paused) {
			if (world.gameOver) {
				Gdx.input.setInputProcessor(null);
				if (System.currentTimeMillis() - world.gameOverTime > GAME_OVER_TIME) {
					GameOverScreen gameOverScreen = new GameOverScreen(game,
							difficulty);
					gameOverScreen.setScore(world.score);
					game.setScreen(gameOverScreen);
				}
			}

			if (delta > .020f && !world.gameOver) {
				Gdx.app.log(Stairs.LOG, "Delta time: " + delta);
			}

			Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

			stairsController.update(delta);
			feetController.update(delta);
			renderer.render();

			updateScore();

			stage.draw();

			if (world.gameOver) {
				renderGameOver();
			}
		} else {
			long change = (long) (delta * 1000);
			stairsController.levelChangeTime += change;
			stairsController.roundChangeTime += change;
			stairsController.time += change;
			Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			stage.draw();
			renderer.render();
			
		}
	}

	public void updateScore() {
		scoreString = "" + world.score;
		scoreLabel.setText(scoreString);
	}

	public void renderGameOver() {
		String gameOverText = "";
		int gameOverType = world.gameOverType;
		if (gameOverType == FootController.TRIPPED) {
			gameOverText = TRIP;
		} else if (gameOverType == FootController.FELL_OFF_LEFT) {
			gameOverText = LEFT_FELL;
		} else if (gameOverType == FootController.FELL_OFF_RIGHT) {
			gameOverText = RIGHT_FELL;
		} else if (gameOverType == FootController.STEPPED_OFF_LEFT) {
			gameOverText = LEFT_STEP;
		} else if (gameOverType == FootController.STEPPED_OFF_RIGHT) {
			gameOverText = RIGHT_STEP;
		} else if (gameOverType == FootController.SKIPPED_STEP_LEFT
				|| gameOverType == FootController.SKIPPED_STEP_RIGHT) {
			gameOverText = SKIP;
		}
		// gameOverText = SKIP;

		Table table = new Table(getSkin());
		Label gameOverLabel = new Label(gameOverText, getSkin());
		gameOverLabel.setWrap(true);
		gameOverLabel.setAlignment(Align.center, Align.center);
		gameOverLabel.setColor(new Color(255, 0, 102, 1));

		float labelWidth = 500f;
		float labelHeight = 200f;

		table.add(gameOverLabel).width(labelWidth).height(labelHeight)
				.expandX().expandY().center();
		table.center();
		float xPos = (WorldRenderer.CAMERA_WIDTH - labelWidth) / 2;
		float yPos = (WorldRenderer.CAMERA_HEIGHT - labelHeight) / 2;
		table.setPosition(xPos + labelWidth / 2, yPos + labelHeight / 2);
		// table.debug();
		stage.addActor(table);
		stage.draw();
		// Table.drawDebug(stage);
		table.clearChildren();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		
		// NO ADS!
		game.myRequestHandler.showAds(false);
		
		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();
		
		Texture levelsTex;
		if (!prefs.getBoolean("invincOn", false)) {
			levelsTex = new Texture("images/buttons/misc/pause1.png");
		} else {
			levelsTex = new Texture("images/buttons/misc/btn_stop.png");
		}
		Image pauseButton = new Image(levelsTex);
		float labelHeight = pauseButton.getHeight();
		float xPos = 485;
		float yPos;
		if (true) {
			yPos = WorldRenderer.CAMERA_HEIGHT - 14;
		} else {
			yPos = WorldRenderer.CAMERA_HEIGHT - 38;
		}
		pauseButton.setPosition(xPos, yPos - labelHeight);
		pauseButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!prefs.getBoolean("invincOn", false)) {
					game.paused = !game.paused;
					Gdx.app.log(Stairs.LOG, "PAUSE");
				} else {
					GameOverScreen gameOverScreen = new GameOverScreen(game,
							difficulty);
					gameOverScreen.setScore(world.score);
					game.setScreen(gameOverScreen);
				}
			}
		});
		stage.addActor(pauseButton);
		
		Gdx.app.log(Stairs.LOG, "Music on " + prefs.getBoolean("musicOn", true));
		// Play the right music
		if (prefs.getBoolean("musicOn", true)) {
			if (difficulty == MenuScreen.EASY) {
				if (prefs.getBoolean("songFirst", true)) {
					game.musicManager.play(MUSIC_EASY, true);
				} else if (!prefs.getBoolean("songFirst", false) ) {
					game.musicManager.play(MUSIC_EASY_ALT, false);
				}
			} else if (difficulty == MenuScreen.MEDIUM) {
				if (prefs.getBoolean("songFirst", true)) {
					game.musicManager.play(MUSIC_MEDIUM, true);
				} else if (!prefs.getBoolean("songFirst", false)){
					game.musicManager.play(MUSIC_MEDIUM_ALT, false);
				}
			} else if (difficulty == MenuScreen.HARD) {
				if (prefs.getBoolean("songFirst", true)) {
					game.musicManager.play(MUSIC_HARD, true);
				} else if (!prefs.getBoolean("songFirst", false)){
					game.musicManager.play(MUSIC_HARD_ALT, false);
				}
			} else if (difficulty == MenuScreen.INSANE) {
				Gdx.app.log(Stairs.LOG, "Insane level");
				if (prefs.getBoolean("songFirst", true) ) {
					Gdx.app.log(Stairs.LOG, "Play main song");
					game.musicManager.play(MUSIC_INSANE, true);
				} else if (!prefs.getBoolean("songFirst", false)) {
					Gdx.app.log(Stairs.LOG, "Play alt song");
					game.musicManager.play(MUSIC_INSANE_ALT, false);
				}
			}
		}
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
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