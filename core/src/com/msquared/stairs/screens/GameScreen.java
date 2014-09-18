package com.msquared.stairs.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.controller.FootController;
import com.msquared.stairs.controller.StairController;
import com.msquared.stairs.controller.StairControllerEasy;
import com.msquared.stairs.controller.StairControllerHard;
import com.msquared.stairs.controller.StairControllerInsane;
import com.msquared.stairs.model.Stair;
import com.msquared.stairs.model.World;
import com.msquared.stairs.utils.DefaultActorListener;
import com.msquared.stairs.view.WorldRenderer;

public class GameScreen extends AbstractScreen implements Screen {

	private World world;
	private WorldRenderer renderer;
	private FootController feetController;
	private StairController stairsController;
	boolean leftPressed = false;
	boolean rightPressed = false;
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
	public static final String MUSIC_EASY = "sounds/music_clark_easy.mp3";
	public static final String MUSIC_MEDIUM = "sounds/music_clark_medium.mp3";
	public static final String MUSIC_HARD = "sounds/music_clark_hard.mp3";
	public static final String MUSIC_INSANE = "sounds/music_clark_insane.mp3";
	public static final String MUSIC_EASY_ALT = "sounds/music_easy.mp3";
	public static final String MUSIC_MEDIUM_ALT = "sounds/music_medium.mp3";
	public static final String MUSIC_HARD_ALT = "sounds/music_hard.mp3";
	public static final String MUSIC_INSANE_ALT = "sounds/music_insane.mp3";
	public static final int GAME_OVER_TIME = 1000;
	Music music;
	public int screenWidth;
	public int screenHeight;
	Stage stageDispose;
	Texture pauseTex;

	int prevScore;
	int score;
	String scoreString;
	Label scoreLabel;
	int difficulty;

	// Constructor to keep a reference to the main Game class.
	// Also keeps a reference to the Stage from the GameOverScreen to dispose.
	public GameScreen(Stairs game, int diff, Stage stageToDispose) {
		super(game);

		// Initialize Stair vars
		stageDispose = stageToDispose;
		Stair.gameOver = false;
		Stair.movingLeft = false;
		Stair.movingRight = false;
		Stair.lowYSpeed = -400;
		Stair.highXSpeed = 250;
		FootController.jump_left = false;
		FootController.jump_right = false;
		difficulty = diff;

		// Initialize world, renderer, controllers
		world = new World();
		renderer = new WorldRenderer(world);
		World.difficulty = difficulty;
		switch (difficulty % 4) {
		case MenuScreen.EASY:
			stairsController = new StairControllerEasy(world, true);
			break;
		case MenuScreen.MEDIUM:
			stairsController = new StairController(world);
			break;
		case MenuScreen.HARD:
			stairsController = new StairControllerHard(world, true, world.earlySelector);
			break;
		case 0:
			stairsController = new StairControllerInsane(world, true);
			break;
		}
		Gdx.app.log("BLAH", "EARLYSELECTOR = " + world.earlySelector + " and " + stairsController.earlySelector);
		feetController = new FootController(world);
		prevScore = 0;

		/* Set up score label */
		scoreString = "" + world.score;
		String labelStyle;
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
			labelStyle = "mscoreblack";
		} else {
			labelStyle = "mscore";
		}
		scoreLabel = new Label(scoreString, getSkin(), labelStyle);
		float labelHeight = scoreLabel.getTextBounds().height;
		float xPos = 12;
		float yPos = WorldRenderer.CAMERA_HEIGHT - 21;
		scoreLabel.setPosition(xPos, yPos - labelHeight);
		stage.addActor(scoreLabel);

		// Add controls to stage
		stage.addListener(new InputListener() {
	        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
	    		if (x < WorldRenderer.CAMERA_WIDTH / 2 
	    				&& !rightPressed 
	    				&& y < (WorldRenderer.CAMERA_HEIGHT - WorldRenderer.CAMERA_HEIGHT/9)) {
	    			leftPressed = true;
	    			feetController.processInput(JUMP_LEFT);
	    			FootController.jump_left = true;
	    		} else if (x >= WorldRenderer.CAMERA_WIDTH / 2 
	    				&& !leftPressed 
	    				&& y < (WorldRenderer.CAMERA_HEIGHT - WorldRenderer.CAMERA_HEIGHT/9)) {
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
	}

	@Override
	public void render(float delta) {
		if (!game.paused) {
			if (world.gameOver) {
				Gdx.input.setInputProcessor(null);
				if (System.currentTimeMillis() - world.gameOverTime > GAME_OVER_TIME) {
					GameOverScreen gameOverScreen = new GameOverScreen(game,
							difficulty, stage);
					gameOverScreen.setScore(world.score);
					game.setScreen(gameOverScreen);
					renderer.off = true;
				}
			}

			if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
				Gdx.gl.glClearColor(.95f, .95f, .95f, 1);
			} else {
				Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
			}
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
			// If game is paused, have to make sure the stairs controller
			// doesn't spawn the next stair right on top of the current one
			long change = (long) (delta * 1000);
			stairsController.levelChangeTime += change;
			stairsController.roundChangeTime += change;
			stairsController.time += change;
			if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
				Gdx.gl.glClearColor(.95f, .95f, .95f, 1);
			} else {
				Gdx.gl.glClearColor(.1f, .1f, .1f, 1);
			}
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

		Table table = new Table(getSkin());
		Label gameOverLabel = new Label(gameOverText, getSkin(), "mscore");
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
		stage.addActor(table);
		stage.draw();
		table.clearChildren();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		// NO ADS!
		game.myRequestHandler.showAds(false);

		screenWidth = Gdx.graphics.getWidth();
		screenHeight = Gdx.graphics.getHeight();

		// Pause button/kill button
		if (!Stairs.getSharedPrefs().getBoolean("invincOn", false)) {
			if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
				pauseTex = new Texture("images/buttons/misc_inverted/btn_pause_inverted.png");
			} else {
				pauseTex = new Texture("images/buttons/misc/btn_pause.png");
			}
		} else {
			pauseTex = new Texture("images/buttons/misc/btn_stop.png");
		}
		Image pauseButton = new Image(pauseTex);
		float labelHeight = pauseButton.getHeight();
		float xPos = WorldRenderer.CAMERA_WIDTH - pauseButton.getWidth() - 14;
		float yPos = WorldRenderer.CAMERA_HEIGHT - 14;
		pauseButton.setPosition(xPos, yPos - labelHeight);
		pauseButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!Stairs.getSharedPrefs().getBoolean("invincOn", false)) {
					game.paused = !game.paused;
					if (game.paused) {
						game.musicManager.pause();
					} else {
						game.musicManager.play();
					}
				} else {
					GameOverScreen gameOverScreen = new GameOverScreen(game,
							difficulty, stage);
					gameOverScreen.setScore(world.score);
					game.setScreen(gameOverScreen);
				}
			}
		});
		stage.addActor(pauseButton);

		// Play the right music
		if (Stairs.getSharedPrefs().getBoolean("musicOn", true)) {
			if (difficulty == MenuScreen.EASY || difficulty == MenuScreen.EASY + 4) {
				if (Stairs.getSharedPrefs().getBoolean("songFirst", true)) {
					game.musicManager.play(MUSIC_EASY, true);
				} else {
					game.musicManager.play(MUSIC_EASY_ALT, false);
				}
			} else if (difficulty == MenuScreen.MEDIUM || difficulty == MenuScreen.MEDIUM + 4) {
				if (Stairs.getSharedPrefs().getBoolean("songFirst", true)) {
					game.musicManager.play(MUSIC_MEDIUM, true);
				} else {
					game.musicManager.play(MUSIC_MEDIUM_ALT, false);
				}
			} else if (difficulty == MenuScreen.HARD || difficulty == MenuScreen.HARD + 4) {
				if (Stairs.getSharedPrefs().getBoolean("songFirst", true)) {
					game.musicManager.play(MUSIC_HARD, true);
				} else {
					game.musicManager.play(MUSIC_HARD_ALT, false);
				}
			} else if (difficulty == MenuScreen.INSANE || difficulty == MenuScreen.INSANE + 4) {
				if (Stairs.getSharedPrefs().getBoolean("songFirst", true) ) {
					game.musicManager.play(MUSIC_INSANE, true);
				} else {
					game.musicManager.play(MUSIC_INSANE_ALT, false);
				}
			}
		}

		world.createDemoWorld();
	}

	@Override
	public void resize(int width, int height) {
		this.width = width;
		this.height = height;
		// Changed jars, this became necessary (along with the new
		// stage constructor with the stretch viewport)
		camera = new OrthographicCamera(width, height);
	    camera.position.set(width / 2, height / 2, 0);
	    camera.update();
		stage.getViewport().update(width, height, true);
        Float heightRatio = this.height / WorldRenderer.CAMERA_HEIGHT;
        Float widthRatio = this.width / WorldRenderer.CAMERA_WIDTH;
        Float newFootHeight = world.leftFoot.height * widthRatio * (1 / heightRatio);
        Gdx.app.log(Stairs.LOG, "Resize feet");
		world.leftFoot.height = newFootHeight;
		world.rightFoot.height = newFootHeight;
	}

	@Override
	public void hide() {
		if (stageDispose != null) {
			stageDispose.dispose();
		}
		dispose();
	}

	@Override
	public void dispose() {
		super.dispose();
		renderer.dispose();
		pauseTex.dispose();
	}
}
