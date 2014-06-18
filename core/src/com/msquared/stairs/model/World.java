package com.msquared.stairs.model;

import java.util.LinkedList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.screens.GameScreen;
import com.msquared.stairs.screens.MenuScreen;

public class World {
	
	public int width;
	public int height;
	public LinkedList<Stair> stairs = new LinkedList<Stair>();
	public Foot leftFoot;
	public Foot rightFoot;
	public boolean gameOver = false;
	public long gameOverTime = 0;
	public int score = 0;
	public int numStairs;
	public int gameOverType;
	Color white = new Color(255, 255, 255, 1);
	public Stair lastStair;
	public Stair roundChangeStair;
	public static int difficulty;
	Random random;
	
	protected int[] randMinColors = { 220, 170, 130, 90, 0, 0 };
	
	protected float[][] classicXPositions = {
			
			{ 194.89f, 217.96f, 296.06f, 283.84f, 166.83f, 320.00f, 146.00f, 146.00f },
			
			{ 179.46f, 189.95f, 201.68f, 197.53f, 299.69f, 187.51f, 207.54f,
					190.37f, 232.00f, 235.00f, 323.00f, 210.00f, 235.00f,
					233.00f },

			{ 233.00f, 278.02f, 214.49f, 244.01f, 303.35f, 225.43f, 216.13f, 284.83f,
					157.22f, 274.42f, 301.00f, 249.00f, 309.00f, 213.00f,
					262.00f, 215.00f, 274.00f, 333.00f, 188.00f },

			{ 286.91f, 174.87f, 267.90f, 266.54f, 258.96f, 188.95f,
					192.0f, 282.0f, 205.0f, 246.0f, 276.0f, 229.0f, 284.0f,
					209.0f, 191.0f, 319.0f } 
					
	};
	protected float[][] classicYPositions = { 
			
			{ 444.04f, 580.05f, 655.10f, 705.20f, 750.10f, 790.47f, 835.07f, 870.00f },
			
			{ 461.06f, 531.47f, 581.10f, 615.16f, 644.42f, 669.43f, 693.18f,
					716.89f, 737.34f, 761.89f, 784.80f, 808.17f, 829.85f,
					858.00f },
			
			{ 460.00f, 523.38f, 561.29f, 590.31f, 614.07f,
				635.47f, 652.39f, 671.06f, 687.38f, 703.95f,
				720.00f, 736.10f, 753.62f, 769.92f, 786.15f,
				803.59f, 821.12f, 838.68f, 850.00f },
			
			{ 582.57f, 625.72f,
			654.68f, 677.56f, 696.37f, 711.80f, 726.58f, 740.68f, 754.825f,
			769.42f, 784.48f, 798.23f, 811.82f, 825.68f, 839.84f, 853f } 
	};
	
	protected float[] mediumZigZagXPositions = { 204.65f, 336.82f, 465.41f,
			471.80f, 356.74f, 241.40f, 126.03f, 9.00f, -1.0f, 119.0f, 239.0f,
			359.0f, 479.0f };
	
	protected float[] mediumZigZagYPositions = { 481.17f, 546.70f, 592.95f,
			627.36f, 653.99f, 679.08f, 704.01f, 729.06f, 754.01f, 779.03f,
			804.05f, 829.04f, 860.0f };
	
	protected float[] insaneZigZagXPositions = { 221.94f, 379.94f, 475.86f, 330.39f, 184.00f, 36.91f, -.6500f, 151.43f, 
			302.00f, 452.00f, 492.00f, 342.00f, 192.00f, 42.00f, 2.0f, 152.0f, 
			302.0f, 452.0f, 492.0f };
	
	protected float[] insaneZigZagYPositions = { 557.04f, 605.70f, 641.77f, 669.35f, 691.32f, 709.0f, 723.85f, 736.56f, 
			749.11f, 761.54f, 774.04f, 786.46f, 799.00f, 811.54f, 824.05f, 836.50f, 
			849.00f, 861.54f, 880.0f };

	private final Pool<Stair> stairPool = new Pool<Stair>() {
		@Override
		protected Stair newObject() {
			return new Stair();
		}
	};
	


	public void addStair(float xPos, float yPos, float width, float height, Color stairColor) {
		Stair stair = stairPool.obtain();
		stair.init(xPos, yPos, width, height, stairColor);
		stairs.add(stair);
		numStairs++;
		lastStair = stair;
		if (stairs.getFirst().yPos <= 550) {
			String blah = "l";
			int five = blah.length();
		}
	}
	
	
	public void addStair(Stair stair) {
		stairs.add(stair);
		numStairs++;
		lastStair = stair;
	}
	
	public void deleteFirstStair() {
		Stair stair = stairs.getFirst();
		stairs.remove();
		stairPool.free(stair);
		numStairs--;
	}
	
	
	
	public World() {
		numStairs = 0;
		leftFoot = new Foot(160, 0, true);
		rightFoot = new Foot(380 - Foot.footSize, 0, false);
		this.random = new Random(System.currentTimeMillis());
	}
	
	public void createDemoWorld() {
		this.random = new Random(System.currentTimeMillis());
		// If earlyOn pref is not on, don't spawn early stairs. Else, do.
		final Preferences prefs = Gdx.app.getPreferences("Preferences");
		boolean earlyOn = prefs.getBoolean("earlyOn", true);
		if (earlyOn) {
			if (difficulty == Stairs.EASY_LEVELS) {
				addStair(187, 510, 1000 / 6, 5, white);
				addStair(187, 620, 1000 / 6, 5, white);
				addStair(187, 684, 1000 / 6, 5, white);
				addStair(187, 734, 1000 / 6, 4, white);
				addStair(187, 784, 1000 / 6, 2, white);
				addStair(187, 834, 1000 / 6, 2, white);
				addStair(187, 870, 1000 / 6, 2, white);
			} else if (difficulty == Stairs.MEDIUM_LEVELS) {
				addMediumZigZag();
			} else if (difficulty == Stairs.HARD_LEVELS) {
				addHardClassic();
			} else if (difficulty == Stairs.INSANE_LEVELS) {
				addInsaneZigZag();
			} else if (difficulty == Stairs.EASY_CLASSIC) {
				addEasyClassic();
			} else if (difficulty == Stairs.MEDIUM_CLASSIC) {
				addMediumClassic();
			} else if (difficulty == Stairs.INSANE_CLASSIC) {
				addInsaneClassic();
			} else if (difficulty == Stairs.HARD_CLASSIC) {
				addHardClassic();
			} else if (false) {
				// addStair(210, 100, 120, 6, white);
				addStair(218, 684, 105, 5, white);
				addStair(225, 709, 90, 4, white);
				addStair(228, 734, 83, 4, white);
				addStair(228, 759, 83, 3, white);
				addStair(228, 784, 83, 2, white);
				addStair(228, 809, 83, 2, white);
				addStair(228, 834, 83, 2, white);
			}
		}

	}
	
	public void addEasyClassic() {
		addClassicLevel(0, 0, 50, 70);
	}

	public void addMediumClassic() {
		addClassicLevel(1, 1, 40, 60);
	}
	
	public void addHardClassic() {
		addClassicLevel(5, 2, 35, 45);
	}
	
	public void addInsaneClassic() {
		addClassicLevel(3, 3, 35, 47);
	}

	public void addClassicLevel(int round, int diff, int minWidth, int maxWidth) {
		Color stairColor = white;
		int randInt;
		float randColor1;
		float randColor2;
		float randColor3;
		
		int maxX;
		int minX;
		int x;
		int y;
		int randWidth;
		int randBool = 0;
		for (int i = 0; i < classicXPositions[diff].length; i++) {
			randInt = random.nextInt(3);
			randColor1 = random
					.nextInt(255 - randMinColors[round] + 1)
					+ randMinColors[round];
			randColor2 = random
					.nextInt(255 - randMinColors[round] + 1)
					+ randMinColors[round];
			randColor3 = random
					.nextInt(255 - randMinColors[round] + 1)
					+ randMinColors[round];
			switch (randInt) {
			case 0:
				stairColor = new Color(255f, randColor2 / 255f,
						randColor3 / 255f, 1);
				break;
			case 1:
				stairColor = new Color(randColor1 / 255f, 255f,
						randColor3 / 255f, 1);
				break;
			case 2:
				stairColor = new Color(randColor1 / 255f, randColor2 / 255f,
						255f, 1);
				break;
			}
			
			x = (int) classicXPositions[diff][i];
			y = (int) classicYPositions[diff][i];
			if (x <= 270) {
				minX = x;
				maxX = x + 2 * (270 - x);
				randBool = random.nextInt(2);
				if (randBool == 0)
					x = minX;
				else
					x = maxX;
			} else if (x > 270) {
				maxX = x;
				minX = x + 2 * (270 - x);
				randBool = random.nextInt(2);
				if (randBool == 0)
					x = minX;
				else
					x = maxX;
			}
			randWidth = random.nextInt(maxWidth - minWidth + 1)
					+ minWidth;
			addStair(x, y, randWidth, 2, stairColor);
		}
	}
	
	public void addMediumZigZag() {
		Color stairColor = new Color(255f / 255f,
				235f / 255f, 176f / 255f, 1);
		int x;
		int y;
		int width = 52;
		for (int i = 0; i < mediumZigZagXPositions.length - 1; i++) {
			x = (int) mediumZigZagXPositions[i];
			y = (int) mediumZigZagYPositions[i];
			addStair(x, y, width, 2, stairColor);
		}
	}
	
	public void addInsaneZigZag() {
		Color stairColor = new Color(255f / 255f,
				227f / 255f, 145f / 255f, 1);
		int x;
		int y;
		int width = 47;
		for (int i = 0; i < insaneZigZagXPositions.length - 2; i++) {
			x = (int) insaneZigZagXPositions[i];
			y = (int) insaneZigZagYPositions[i];
			addStair(x, y, width, 2, stairColor);
		}
	}
}
