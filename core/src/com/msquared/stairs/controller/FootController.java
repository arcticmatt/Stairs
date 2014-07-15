package com.msquared.stairs.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.model.Foot;
import com.msquared.stairs.model.Foot.State;
import com.msquared.stairs.model.Stair;
import com.msquared.stairs.model.World;
import com.msquared.stairs.screens.GameScreen;

public class FootController {
	private static final int ALIVE = 0;
	public static final int TRIPPED = 1;
	public static final int FELL_OFF_LEFT = 2;
	public static final int FELL_OFF_RIGHT = 3;
	public static final int STEPPED_OFF_LEFT = 4;
	public static final int STEPPED_OFF_RIGHT = 5;
	public static final int SKIPPED_STEP_LEFT = 6;
	public static final int SKIPPED_STEP_RIGHT = 7;

	public static boolean jump_left = false;
	public static boolean jump_right = false;
	public static boolean dead_flag_left;
	public static boolean dead_flag_right;

	enum Keys {
		LEFT, RIGHT, RESTART
	}

	private World world;
	private Foot leftFoot;
	private Foot rightFoot;
	float secondStairTop = 0;
	Stair secondStair = null;
	Stair firstStair = null;
	Color deadColor = new Color(255, 0, 0, 1);

	Preferences prefs;
	Sound stepSound;
	boolean invincOn;

	public FootController(World world) {
		this.world = world;
		this.leftFoot = world.leftFoot;
		this.rightFoot = world.rightFoot;
		dead_flag_left = false;
		dead_flag_right = false;
		stepSound = Gdx.audio.newSound(Gdx.files.internal("sounds/step.wav"));
		prefs = Gdx.app.getPreferences("Preferences");
		invincOn = prefs.getBoolean("invincOn", false);
	}

	/** The main update method */
	public void update(float delta) {
		if (jump_left)
		{
			processInput(GameScreen.JUMP_LEFT);
		}
		else if (jump_right)
		{
			processInput(GameScreen.JUMP_RIGHT);
		}

		if (!invincOn) {
			// Checks for stepped off death
			if (!world.gameOver && leftFoot.state.equals(State.FALLING)
					&& dead_flag_left) {
				chooseDeath(STEPPED_OFF_LEFT);
			} else if (!world.gameOver && rightFoot.state.equals(State.FALLING)
					&& dead_flag_right) {
				chooseDeath(STEPPED_OFF_RIGHT);
			}
		}

		// Update both feet
		leftFoot.update(delta);
		rightFoot.update(delta);

		// Make sure the feet don't go below the screen (unless
		// the game is over)
		if (leftFoot.yPos < 0 && !world.gameOver) {
			leftFoot.yPos = 0f;
			leftFoot.yVelo = 0f;
			leftFoot.state = State.IDLE;
			leftFoot.grounded = true;
			if (prefs.getBoolean("soundsOn", true)) {
				stepSound.play(1f);
			}
		}
		if (rightFoot.yPos < 0 && !world.gameOver) {
			rightFoot.yPos = 0f;
			rightFoot.yVelo = 0f;
			rightFoot.state = State.IDLE;
			rightFoot.grounded = true;
			if (prefs.getBoolean("soundsOn", true)) {
				stepSound.play(1f);
			}
		}

		// Check whether the feet died, and how
		int deadOrAlive = checkFeetCollision(delta);
		if (invincOn) {
			deadOrAlive = ALIVE;
		}
		if (!world.gameOver && deadOrAlive != 0) {
			Gdx.app.log(Stairs.LOG, "Checking for death");
			chooseDeath(deadOrAlive);
		}
	}

	public void chooseDeath(int deadOrAlive) {
		switch (deadOrAlive) {
		case TRIPPED:
			// If tripped, both feet turn yellow
			// and slide off the screen
			leftFoot.color = deadColor;
			rightFoot.color = deadColor;
			leftFoot.gameOver = true;
			rightFoot.gameOver = true;
			leftFoot.yPos = 0f;
			rightFoot.yPos = 0f;
			world.gameOver = true;
			world.gameOverTime = System.currentTimeMillis();
			Stair.gameOver = true;
			world.gameOverType = TRIPPED;
			break;
		case FELL_OFF_LEFT:
			// If the left foot falls off (idle and out of bounds)
			// it turns yellow and slides off. The right foot freezes.
			leftFoot.color = deadColor;
			leftFoot.gameOver = true;
			rightFoot.footStill = true;
			world.gameOver = true;
			world.gameOverTime = System.currentTimeMillis();
			Stair.gameOver = true;
			world.gameOverType = FELL_OFF_LEFT;
			break;
		case FELL_OFF_RIGHT:
			// If the right foot falls off (idle and out of bounds)
			// it turns yellow and slides off. The left foot freezes.
			rightFoot.color = deadColor;
			rightFoot.gameOver = true;
			leftFoot.footStill = true;
			world.gameOver = true;
			world.gameOverTime = System.currentTimeMillis();
			Stair.gameOver = true;
			world.gameOverType = FELL_OFF_RIGHT;
			break;
		case STEPPED_OFF_LEFT:
			/*
			 * If the left foot steps off, it turns yellow and slides off
			 * (full speed until y <5, then slo-mo. The right foot stays
			 * idle and doesn't slide off.
			 */
			leftFoot.color = deadColor;
			leftFoot.gameOver = true;
			leftFoot.footSteppedOff = true;
			world.gameOver = true;
			world.gameOverTime = System.currentTimeMillis();
			Stair.gameOver = true;
			world.gameOverType = STEPPED_OFF_LEFT;
			break;
		case STEPPED_OFF_RIGHT:
			/*
			 * If the right foot steps off, it turns yellow and slides off
			 * (full speed until y <5, then slo-mo. The left foot stays idle
			 * and doesn't slide off.
			 */
			rightFoot.color = deadColor;
			rightFoot.gameOver = true;
			rightFoot.footSteppedOff = true;
			world.gameOver = true;
			world.gameOverTime = System.currentTimeMillis();
			Stair.gameOver = true;
			world.gameOverType = STEPPED_OFF_RIGHT;
			break;
		case SKIPPED_STEP_LEFT:
			// If the left foot skips a step, it turns yellow
			// and goes down slo-mo. The right foot stays idle.
			leftFoot.color = deadColor;
			leftFoot.gameOver = true;
			world.gameOver = true;
			world.gameOverTime = System.currentTimeMillis();
			Stair.gameOver = true;
			world.gameOverType = SKIPPED_STEP_LEFT;
			break;
		case SKIPPED_STEP_RIGHT:
			// If the right foot skips a step, it turns yellow
			// and goes down slo-mo. The left foot stays idle.
			rightFoot.color = deadColor;
			rightFoot.gameOver = true;
			world.gameOver = true;
			world.gameOverTime = System.currentTimeMillis();
			Stair.gameOver = true;
			world.gameOverType = SKIPPED_STEP_RIGHT;
			break;
		}
	}

	/** Checks for deaths, and returns the type of death **/
	private int checkFeetCollision(float delta) {
		int numStairs = world.numStairs;
		// Check to make sure stairs are on the screen
		if (numStairs > 0) {
			/*
			 * The next chunk checks to make sure every stair is stepped on. It
			 * does this by checking that the left or right foot does not go
			 * above the second step
			 */
			Stair firstStair = world.stairs.getFirst();
			if (numStairs > 1 && leftFoot.state.equals(State.IDLE)
					&& rightFoot.state.equals(State.IDLE)) {
				secondStair = world.stairs.get(1);
				secondStairTop = secondStair.yPos + secondStair.height;
			}
			if (secondStairTop > 0 && secondStair != null && numStairs > 1) {
				if (leftFoot.yPos > secondStairTop) {
					return SKIPPED_STEP_LEFT;
				} else if (rightFoot.yPos > secondStairTop) {
					return SKIPPED_STEP_RIGHT;
				}
			}

			/*
			 * The next block checks if a stair has just exited the bottom of
			 * the screen, and if so, checks if a death has occured by tripping,
			 * falling, or stepping off
			 */
			if (firstStair.yPos + firstStair.height <= 0) {
				// Store the left and right values of the first stair
				// to check if a foot steps off
				float firstStairLeft = firstStair.xPos;
				float firstStairRight = firstStair.xPos + firstStair.width;
				float leftFootLeft = leftFoot.xPos;
				float leftFootRight = leftFoot.xPos + leftFoot.width;
				float rightFootLeft = rightFoot.xPos;
				float rightFootRight = rightFoot.xPos + rightFoot.width;

				if (leftFoot.state.equals(State.IDLE)
						&& rightFoot.state.equals(State.IDLE)) {
					// If both feet are idle, there is a collision
					return TRIPPED;
				} else if (leftFoot.state.equals(State.IDLE)) {
					if (leftFootRight < firstStairLeft
							|| leftFootLeft > firstStairRight) {
						/*
						 * If the left foot is idle, but it is out of bounds of
						 * the step that just passed it, you fall
						 */
						return FELL_OFF_LEFT;
					} else if (rightFootRight < firstStairLeft
							|| rightFootLeft > firstStairRight) {
						if (rightFoot.state.equals(State.FALLING)) {
							/*
							 * If you step off the stairs, you fall. This means
							 * that when a stair passes off the screen, if the
							 * right foot is falling and is out of bounds of the
							 * step that passed of the screen, you fall.
							 */
							return STEPPED_OFF_RIGHT;
						} else if (rightFoot.state.equals(State.JUMPING)) {
							dead_flag_right = true;
							return ALIVE;
						}
					}
				} else if (rightFoot.state.equals(State.IDLE)) {
					if (rightFootRight < firstStairLeft
							|| rightFootLeft > firstStairRight) {
						/*
						 * If the right foot is idle, but it is out of bounds of
						 * the step that just passed it, you fall
						 */
						return FELL_OFF_RIGHT;
					} else if (leftFootRight < firstStairLeft
							|| leftFootLeft > firstStairRight) {
						if (leftFoot.state.equals(State.FALLING)) {
							/*
							 * If you step off the stairs, you fall. This means
							 * that when a stair passes off the screen, if the
							 * left foot is falling and is out of bounds of the
							 * step that passed of the screen, you fall.
							 */
							return STEPPED_OFF_LEFT;
						} else if (leftFoot.state.equals(State.JUMPING)) {
							dead_flag_left = true;
							return ALIVE;
						}
					}
				}
				world.deleteFirstStair();
				if (!invincOn)
					world.score++;
			}
		}
		// Update the position of the top of the second stair.
		// This is used to check that no steps are skipped.
		if (secondStairTop != 0 && secondStair != null) {
			secondStairTop += secondStair.yVelo * delta;
		}

		return ALIVE;
	}

	/** Change Bob's state and parameters based on input controls */
	public void processInput(int input) {
		switch (input) {
		case GameScreen.JUMP_LEFT:
			if (leftFoot.state.equals(State.IDLE)
					&& rightFoot.state.equals(State.IDLE)) {
				// If the left key is pressed and the state of both
				// feet is idle, jump the left foot
				leftFoot.state = State.JUMPING;
				leftFoot.yVelo = Foot.jumpVelo;
				leftFoot.grounded = false;
				Stair.movingLeft = true;
			}
			break;
		case GameScreen.JUMP_RIGHT:
			if (rightFoot.state.equals(State.IDLE)
					&& leftFoot.state.equals(State.IDLE)) {
				// If the right key is pressed and the state of both
				// feet is idle, jump the right foot
				rightFoot.state = State.JUMPING;
				rightFoot.yVelo = Foot.jumpVelo;
				rightFoot.grounded = false;
				Stair.movingRight = true;
			}
			break;
		case GameScreen.FALL_LEFT:
			leftFoot.state = State.FALLING;
			leftFoot.yVelo = Foot.footGravity;
			leftFoot.grounded = false;
			Stair.movingLeft = false;
			break;
		case GameScreen.FALL_RIGHT:
			rightFoot.state = State.FALLING;
			rightFoot.yVelo = Foot.footGravity;
			rightFoot.grounded = false;
			Stair.movingRight = false;
			break;
		default:
			break;
		}
	}
}
