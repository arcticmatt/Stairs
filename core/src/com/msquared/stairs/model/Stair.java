package com.msquared.stairs.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.screens.MenuScreen;
import com.msquared.stairs.view.WorldRenderer;

public class Stair {
	public static final float STARTING_VELOCITY = 0f;
	public static int lowYSpeed = -400;
	public static int highYSpeed = -50;
	public static int lowXSpeed = 10;
	public static int highXSpeed = 250;
	public static int startingSpeed = -400;
	public final static int MAX_HEIGHT_SCALAR = 12;
	public final static int MIN_HEIGHT_SCALAR = 1;
	public final static int MAX_WIDTH_SCALAR = 6;
	public final static int MIN_WIDTH_SCALAR = 1;
	public static boolean gameOver = false;
	public static boolean movingLeft;
	public static boolean movingRight;
	public static float x_divisor = 800f;
	public static float y_divisor = 900f;
	public static float x_divisor_hard = 400f;
	public static float y_divisor_hard = 450f;

	public float xPos;
	public float yPos;
	public float xVelo;
	public float yVelo;
	public Color color;
	public float height;
	public float width;
	float startingWidth;
	float startingHeight;

	public Stair(float xPos, float yPos, float wdth, float hght,
			Color stairColor) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = wdth;
		this.height = hght;
		yVelo = startingSpeed;
		xVelo = 0;
		this.startingWidth = wdth;
		this.startingHeight = 2;
		color = stairColor;
	}
	
	public Stair(Stair another) {
		this.xPos = another.xPos;
		this.yPos = another.yPos;
		this.width = another.width;
		this.height = another.height;
		this.xVelo = another.xVelo;
		this.yVelo = another.yVelo;
	}

	public Stair() {
		yVelo = startingSpeed;
		xVelo = 0;
	}

	public void init(float xPos, float yPos, float wdth, float hght,
			Color stairColor) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = wdth;
		this.height = hght;
		yVelo = startingSpeed;
		xVelo = 0;
		this.startingWidth = wdth;
		this.startingHeight = 2;
		color = stairColor;
	}
	

	public void reset() {
		this.xPos = WorldRenderer.CAMERA_WIDTH;
		this.yPos = WorldRenderer.CAMERA_HEIGHT;
		this.startingHeight = 2;
	}

	/*
	 * Updates Stair's position and bounds by calculating how far it moved using
	 * delta (time) and velocity.
	 */
	public void update(float delta) {
		yPos += yVelo * delta;
		xPos += xVelo * delta;

		if (!gameOver) {
			if (World.difficulty % 4 != 0){ 
				// Update velocity depending upon the stair's y position
				yVelo = (float) (-Math.pow((-860 + yPos), 2f) / y_divisor);
				yVelo = clamp(yVelo, lowYSpeed, highYSpeed);

				if (movingLeft) {
					xVelo = (float) (Math.pow((-860 + yPos), 2f) / x_divisor);
					xVelo = -clamp(xVelo, lowXSpeed, highXSpeed);
				} else if (movingRight) {
					xVelo = (float) (Math.pow((-860 + yPos), 2f) / x_divisor);
					xVelo = clamp(xVelo, lowXSpeed, highXSpeed);
					// velocity.x = 200f;
				} else {
					xVelo = 0;
				}
			}
			else {
				// Update velocity depending upon the stair's y position
				yVelo = (float) (-Math.pow((-860 + yPos), 2f) / y_divisor_hard);
				yVelo = clamp(yVelo, lowYSpeed, highYSpeed);

				if (movingLeft) {
					xVelo = (float) (Math.pow((-860 + yPos), 2f) / x_divisor_hard);
					xVelo = -clamp(xVelo, lowXSpeed, highXSpeed);
				} else if (movingRight) {
					xVelo = (float) (Math.pow((-860 + yPos), 2f) / x_divisor_hard);
					xVelo = clamp(xVelo, lowXSpeed, highXSpeed);
					// velocity.x = 200f;
				} else {
					xVelo = 0;
				}
			}
		} else {
			xVelo = 0;
			yVelo = -20f;
		}

		// Scale the size of the step depending upon y position
		// Width
		float oldWidth = width;

		float scalarWidth = Math.abs((yPos - 860) / 140);
		scalarWidth = clamp(scalarWidth, MIN_WIDTH_SCALAR, MAX_WIDTH_SCALAR);
		float newWidth = startingWidth * scalarWidth;

		// Height
		float scalarHeight = Math.abs((yPos - 860) / 70);
		scalarHeight = clamp(scalarHeight, MIN_HEIGHT_SCALAR, MAX_HEIGHT_SCALAR);
		float newHeight = startingHeight * scalarHeight;

		width = newWidth;
		height = newHeight;

		// bounds.setCenter(centerX, bounds.y + bounds.height / 2);
		xPos -= (newWidth - oldWidth) / 2f;
	}

	public float clamp(float val, float low, float high) {
		if (val < low)
			return low;
		else if (val > high)
			return high;
		else
			return val;
	}
}