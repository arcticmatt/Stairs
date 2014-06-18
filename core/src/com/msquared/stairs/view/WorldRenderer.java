package com.msquared.stairs.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.msquared.stairs.model.Foot;
import com.msquared.stairs.model.Stair;
import com.msquared.stairs.model.World;

public class WorldRenderer {
	public static final int CAMERA_WIDTH = 540;
	public static final int CAMERA_HEIGHT = 860;
	public static final float ASPECT_RATIO = (float) CAMERA_WIDTH / (float) CAMERA_HEIGHT;

	private World world;
	private OrthographicCamera cam;
	Foot leftFoot;
	Foot rightFoot;

	private int width;
	private int height;
	private float ppuX; // pixels per unit on the X axis
	private float ppuY; // pixels per unit on the Y axis

	SpriteBatch batch;
	Texture tex;
	TextureRegion rectTex;

	Color white = new Color(255, 255, 255, 1);

	public WorldRenderer(World world) {
		this.world = world;
		this.cam = new OrthographicCamera(CAMERA_WIDTH, CAMERA_HEIGHT);
		this.cam.position.set(270f, 430f, 0);
		this.cam.update();

		tex = new Texture(Gdx.files.internal("skin/ptsans_00.png"));
		rectTex = new TextureRegion(tex, tex.getWidth() - 2,
				tex.getHeight() - 2, 1, 1);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(cam.combined);
		batch.disableBlending();
	}

	public void render() {
		// render blocks
		batch.begin();
		batch.setColor(white);
		float x1, y1;
		for (Stair stair : world.stairs) {
			batch.setColor(stair.color);
			x1 = stair.xPos;
			y1 = stair.yPos;
			batch.draw(rectTex, x1, y1, stair.width, stair.height);
		}
		// render feet
		leftFoot = world.leftFoot;
		batch.setColor(leftFoot.color);
		float xLeft = leftFoot.xPos;
		float yLeft = leftFoot.yPos;
		batch.draw(rectTex, xLeft, yLeft, leftFoot.width, leftFoot.height);

		rightFoot = world.rightFoot;
		batch.setColor(rightFoot.color);
		float xRight = rightFoot.xPos;
		float yRight = rightFoot.yPos;
		batch.draw(rectTex, xRight, yRight, rightFoot.width, rightFoot.height);
		batch.end();
	}

	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
		ppuX = (float) width / CAMERA_WIDTH;
		ppuY = (float) height / CAMERA_HEIGHT;
	}
}
