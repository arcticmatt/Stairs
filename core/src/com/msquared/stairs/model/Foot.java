package com.msquared.stairs.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.msquared.stairs.Stairs;

public class Foot {
	public static int footGravity;
	public static int jumpVelo;

	public enum State {
		IDLE, JUMPING, FALLING, BETWEEN, DYING
	}

	public static int footSize = 50;

	public State state = State.IDLE;
	public Color color;
	public boolean grounded = false;
	public boolean gameOver = false;
	public boolean footSteppedOff = false;
	public boolean footStill = false;
	public boolean left = false;

	public float xPos;
	public float yPos;
	public float xVelo;
	public float yVelo;
	public float height;
	public float width;

	/** Constructor. Initializes position and bounds */
	public Foot(float xPos, float yPos, boolean isLeftFoot) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.height = footSize;
		this.width = footSize;
		this.left = isLeftFoot;

		Foot.footGravity = -400;
		Foot.jumpVelo = 500;
		color = new Color(0, 1, 0, 1);
	}

	/*
	 * Updates Foot's position and bounds by calculating how far it moved using
	 * delta (time) and velocity.
	 */
	public void update(float delta) {
		if (footStill) {
			yVelo = 0;
		} else if ((gameOver && !footSteppedOff)
				|| (footSteppedOff && yPos < 5)) {
			yVelo = -20f;
		} else if (footSteppedOff && yPos >= 5) {
			yVelo = footGravity;
		}

		if (yPos > 200) {
			state = State.FALLING;
			yVelo = footGravity;
			grounded = false;
			Stair.movingLeft = false;
			Stair.movingRight = false;
		}

		yPos += yVelo * delta;
	}

}