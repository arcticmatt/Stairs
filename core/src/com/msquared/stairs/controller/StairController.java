package com.msquared.stairs.controller;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.model.Foot;
import com.msquared.stairs.model.Stair;
import com.msquared.stairs.model.World;
import com.msquared.stairs.view.WorldRenderer;

public class StairController {
	protected final int CAM_WIDTH = WorldRenderer.CAMERA_WIDTH;
	protected final int CAM_HEIGHT = WorldRenderer.CAMERA_HEIGHT;
	protected final int STRAIGHT_SELECTOR = 0;
	protected final int ZIG_ZAG_SELECTOR = 1;
	protected final int SIDES_SELECTOR = 2;
	protected final int RAND_SELECTOR = 5;
	protected final int RAPID_SELECTOR = 4;
	protected final int NARROW_SELECTOR = 3;
	protected final int NUM_LEVELS = 5;

	protected World world;
	protected Random random;
	public long time;
	protected long timeDiff = 0;

	protected long timeInterval;
	protected long regTimeInterval;
	protected long rapidTimeInterval;
	public long levelChangeTime;
	public long roundChangeTime;
	protected long levelTransitionTime;
	protected long roundTransitionTime;

	public static int levelSelector;
	public static int stairSelector;
	public static int roundSelector;
	public static int prevRoundSelector;
	public static int maxRound;

	public final static int startingYPos = WorldRenderer.CAMERA_HEIGHT;
	public int startingHeight = 2;

	public int straightWidthOriginal;
	public int zigZagWidthOriginal;
	public int sidesWidthOriginal;
	public int rapidWidthOriginal;
	public int narrowWidthOriginal;
	public int straightWidth;
	public int zigZagWidth;
	public int sidesWidth;
	public int rapidWidth;
	public int narrowWidth;

	protected int randTimeInterval;
	protected int randMinTime;
	protected int randMaxTime;
	public int randMinWidth;
	public int randMaxWidth;
	public int randMinPosition;
	public int randMaxPosition;

	Color white = new Color(255, 255, 255, 1);

	/*
	 * Dimensions for the different levels. The first column is the x position,
	 * and the second column is the width.
	 */
	protected int[][] straightLevel;
	protected int[][] zigZagLevel;
	protected int[][] sidesLevel;
	protected int[][] randLevel;
	protected int[][] rapidLevel;
	protected int[][] narrowLevel;
	protected int[][] currLevel;

	protected int straightPosition;
	protected int[] zigZagPositionsOriginal;
	protected int[] zigZagPositions;
	protected int[] sidesPositionsOriginal;
	protected int[] sidesPositions;
	protected int rapidPosition;
	protected int narrowPosition;

	protected int numStraight;
	protected int straightLimiter;
	protected int straightLimiterMin;
	protected int straightLimiterMax;
	protected int numZigZag;
	protected int zigZagLimiter;
	protected int zigZagLimiterMin;
	protected int zigZagLimiterMax;
	protected int numSides;
	protected int sidesLimiter;
	protected int sidesLimiterMin;
	protected int sidesLimiterMax;
	protected int numRand;
	protected int randLimiter;
	protected int randLimiterMin;
	protected int randLimiterMax;
	protected int numRapid;
	protected int rapidLimiter;
	protected int rapidLimiterMin;
	protected int rapidLimiterMax;
	protected int numNarrow;
	protected int narrowLimiter;
	protected int narrowLimiterMin;
	protected int narrowLimiterMax;

	/**
	 * LEVEL ARRAYS
	 */
	protected List<Integer> highXSpeeds;
	protected List<Integer> lowYSpeeds;
	protected List<Integer> jumpSpeeds;

	// STRAIGHT
	protected List<Float> straightWidthMults;
	protected List<Integer> straightTimes;

	// ZIG ZAG
	protected List<Float> zzWidthMults;
	protected int[][] zzBufferDeltas = new int[6][2];
	protected List<Integer> zzTimes;

	// SIDES
	protected List<Float> sidesWidthMults;
	protected List<Integer> sidesTimes;

	// RAPID
	protected List<Float> rapidWidthMults;
	protected List<Integer> rapidTimes;

	// NARROW
	protected List<Float> narrowWidthMults;
	protected List<Integer> narrowTimes;

	// RAND
	protected int[][] randMinMaxPositions = new int[6][2];
	protected int[][] randMinMaxWidths = new int[6][2];
	protected int[][] randMinMaxTimes = new int[6][2];

	int testnum = 1;
	public Color stairColor;
	protected List<Color> zigZagColors;

	protected List<Color> sidesColors;

	protected List<Color> rapidColors;

	protected List<Color> narrowColors;

	protected int[] randMinColors = { 220, 170, 130, 90, 0, 0 };

	boolean levels;
	
	public int earlySelector;
	

	public StairController(World world, boolean bool) {
		this.world = world;
		this.random = Stairs.randomGenerator;
		this.time = System.currentTimeMillis();
		this.random.setSeed(time);
		int level = Stairs.getSharedPrefs().getInteger("mostRecent");
		if (level <= 4) {
			levels = true;
		} else {
			levels = false;
		}

		stairColor = white;
		initColors();
	}

	public StairController(World world) {
		this.world = world;
		this.random = Stairs.randomGenerator;
		this.time = System.currentTimeMillis();
		this.random.setSeed(time);
		int level = Stairs.getSharedPrefs().getInteger("mostRecent");
		if (level <= 4) {
			levels = true;
		} else {
			levels = false;
		}

		initColors();

		highXSpeeds = new ArrayList<Integer>(asList(250, 300, 400, 495, 600,
				600));
		lowYSpeeds = new ArrayList<Integer>(asList(-400, -500, -550, -600,
				-700, -700));
		jumpSpeeds = new ArrayList<Integer>(
				asList(500, 550, 600, 600, 700, 700));

		// STRAIGHT
		straightWidthMults = new ArrayList<Float>(asList(1f, .9f, .8f, .75f,
				.7f, .7f));
		straightTimes = new ArrayList<Integer>(asList(900, 825, 750, 675, 600,
				600));

		// ZIG ZAG
		zzWidthMults = new ArrayList<Float>(
				asList(1f, .9f, .8f, .77f, .7f, .7f));
		zzBufferDeltas[0][0] = 150;
		zzBufferDeltas[0][1] = 50;
		zzBufferDeltas[1][0] = 150;
		zzBufferDeltas[1][1] = 120;
		zzBufferDeltas[2][0] = 160;
		zzBufferDeltas[2][1] = 150;
		zzBufferDeltas[3][0] = 170;
		zzBufferDeltas[3][1] = 160;
		zzBufferDeltas[4][0] = 180;
		zzBufferDeltas[4][1] = 170;
		zzBufferDeltas[5][0] = 180;
		zzBufferDeltas[5][1] = 170;
		zzTimes = new ArrayList<Integer>(asList(500, 500, 470, 460, 430, 430));

		// SIDES
		sidesWidthMults = new ArrayList<Float>(asList(1f, .98f, .96f, .93f,
				.87f, .86f));
		sidesTimes = new ArrayList<Integer>(
				asList(500, 500, 470, 440, 420, 420));

		// RAPID
		rapidWidthMults = new ArrayList<Float>(asList(1f, .9f, .8f, .6f, .5f,
				.5f));
		rapidTimes = new ArrayList<Integer>(
				asList(300, 270, 255, 240, 220, 220));

		// NARROW
		narrowWidthMults = new ArrayList<Float>(asList(1f, .95f, .9f, .87f,
				.87f, .87f));
		narrowTimes = new ArrayList<Integer>(asList(450, 400, 375, 350, 325,
				325));

		// RAND
		randMinMaxPositions[0][0] = 145;
		randMinMaxPositions[0][1] = 340;
		randMinMaxPositions[1][0] = 153;
		randMinMaxPositions[1][1] = 327;
		randMinMaxPositions[2][0] = 162;
		randMinMaxPositions[2][1] = 318;
		randMinMaxPositions[3][0] = 147 + 4;
		randMinMaxPositions[3][1] = 332 - 4;
		randMinMaxPositions[4][0] = 138 + 4;
		randMinMaxPositions[4][1] = 341 - 4;
		randMinMaxPositions[5][0] = 138 + 4;
		randMinMaxPositions[5][1] = 341 - 4;

		randMinMaxWidths[0][0] = 40;
		randMinMaxWidths[0][1] = 70;
		randMinMaxWidths[1][0] = 40;
		randMinMaxWidths[1][1] = 60;
		randMinMaxWidths[2][0] = 35;
		randMinMaxWidths[2][1] = 55;
		randMinMaxWidths[3][0] = 35;
		randMinMaxWidths[3][1] = 47;
		randMinMaxWidths[4][0] = 35;
		randMinMaxWidths[4][1] = 45;
		randMinMaxWidths[5][0] = 35;
		randMinMaxWidths[5][1] = 45;

		randMinMaxTimes[0][0] = 450;
		randMinMaxTimes[0][1] = 700;
		randMinMaxTimes[1][0] = 400;
		randMinMaxTimes[1][1] = 550;
		randMinMaxTimes[2][0] = 350;
		randMinMaxTimes[2][1] = 475;
		randMinMaxTimes[3][0] = 320;
		randMinMaxTimes[3][1] = 400;
		randMinMaxTimes[4][0] = 300;
		randMinMaxTimes[4][1] = 360;
		randMinMaxTimes[5][0] = 300;
		randMinMaxTimes[5][1] = 360;

        // TIME INTERVALS
		this.randTimeInterval = random.nextInt(100) + 400;
		regTimeInterval = 420l;
		rapidTimeInterval = 220l;
		timeInterval = regTimeInterval;
		levelTransitionTime = 750l;
		roundTransitionTime = 1500l;
		roundChangeTime = 0;

		stairSelector = 0;
		prevRoundSelector = 0;
		roundSelector = 1;
		levelChangeTime = 0;
		roundChangeTime = 0;
		maxRound = 5;

		// Rand level initializers
		randMinTime = randMinMaxTimes[roundSelector][0];
		randMaxTime = randMinMaxTimes[roundSelector][1];
		randMinPosition = randMinMaxPositions[roundSelector][0];
		randMaxPosition = randMinMaxPositions[roundSelector][1];
		randMinWidth = randMinMaxWidths[roundSelector][0];
		randMaxWidth = randMinMaxWidths[roundSelector][1];

		// Straight level init
		numStraight = 0;
		straightLimiter = 0;
		straightWidthOriginal = 500;

		// Zig zag level init
		numZigZag = 50;
		zigZagLimiter = 20;
		zigZagLimiterMin = 15;
		zigZagLimiterMax = 25;
		zigZagWidthOriginal = 350;

		// Sides level init
		numSides = 50;
		sidesLimiter = 15;
		sidesLimiterMin = 12;
		sidesLimiterMax = 18;
		sidesWidthOriginal = 325;

		// Rand level init
		numRand = 100;
		randLimiter = 20;
		randLimiterMin = 15;
		randLimiterMax = 25;
		randTimeInterval = 500;

		// Rapid level init
		numRapid = 50;
		rapidLimiter = 12;
		rapidLimiterMin = 10;
		rapidLimiterMax = 14;
		rapidWidthOriginal = 1200;

		// Narrow level init
		numNarrow = 50;
		narrowLimiter = 10;
		narrowLimiterMin = 8;
		narrowLimiterMax = 12;
		narrowWidthOriginal = 200;

		// Set up initial level (overriden in the hard and easy controllers)
		if (!levels) {
			levelSelector = RAND_SELECTOR;
			currLevel = randLevel;
			randLimiter = 40;
			randLimiterMin = 30;
			randLimiterMax = 50;
		} else if (Stairs.getSharedPrefs().getBoolean("earlyOn", true)) {
			levelSelector = SIDES_SELECTOR;
			currLevel = sidesLevel;
		} else {
			levelSelector = ZIG_ZAG_SELECTOR;
			currLevel = zigZagLevel;
		}
		
		// Make levels corresponding to round selector
		makeNewRound(false);
        // Change constants corresponding to round selector
		changeRoundSpeeds();
		Gdx.app.log(Stairs.LOG, "Done making levels");
	}

	public void initColors() {
		zigZagColors = new ArrayList<Color>(asList(new Color(255f / 255f,
				242f / 255f, 204f / 255f, 1), new Color(255f / 255f,
				235f / 255f, 176f / 255f, 1), new Color(255f / 255f,
				227f / 255f, 145f / 255f, 1), new Color(255f / 255f,
				217f / 255f, 108f / 255f, 1), new Color(232f / 255f,
				188f / 255f, 63f / 255f, 1), new Color(232f / 255f,
				188f / 255f, 63f / 255f, 1)));
		sidesColors = new ArrayList<Color>(asList(new Color(255f / 255f,
				212f / 255f, 216f / 255f, 1), new Color(255f / 255f,
				189f / 255f, 195f / 255f, 1), new Color(255f / 255f,
				133f / 255f, 145f / 255f, 1), new Color(255f / 255f,
				108f / 255f, 123f / 255f, 1), new Color(255f / 255f,
				71f / 255f, 90f / 255f, 1), new Color(255f / 255f, 71f / 255f,
				90f / 255f, 1)));
		rapidColors = new ArrayList<Color>(asList(new Color(228f / 255f,
				199f / 255f, 255f / 255f, 1), new Color(213f / 255f,
				168f / 255f, 255f / 255f, 1), new Color(192f / 255f,
				134f / 255f, 247f / 255f, 1), new Color(163f / 255f,
				96f / 255f, 226f / 255f, 1), new Color(140f / 255f, 60f / 255f,
				214f / 255f, 1), new Color(140f / 255f, 60f / 255f,
				214f / 255f, 1)));
		narrowColors = new ArrayList<Color>(asList(new Color(201f / 255f,
				229f / 255f, 255f / 255f, 1), new Color(179f / 255f,
				218f / 255f, 255f / 255f, 1), new Color(148f / 255f,
				203f / 255f, 255f / 255f, 1), new Color(108f / 255f,
				184f / 255f, 255f / 255f, 1), new Color(75f / 255f,
				158f / 255f, 235f / 255f, 1), new Color(75f / 255f,
				158f / 255f, 235f / 255f, 1)));
	}

	/** The main update method */
	public void update(float delta) {
		/*
		 * See if there is a level change. If so, change the relevant variables
		 * to reflect the level change.
		 */
		boolean levelChange = false;
		switch (levelSelector) {
		case STRAIGHT_SELECTOR:
			stairColor = white;
			currLevel = straightLevel;
			timeInterval = straightTimes.get(roundSelector);
			if (stairSelector >= straightLimiter) {
				stairSelector = 0;
				if (roundSelector < maxRound) {
					levelSelector = ZIG_ZAG_SELECTOR;
				} else {
					do {
						levelSelector = random.nextInt(NUM_LEVELS) + 1;
					} while (levelSelector == STRAIGHT_SELECTOR);
					straightLimiter = random.nextInt(straightLimiterMax
							- straightLimiterMin + 1)
							+ straightLimiterMin;
				}
				timeInterval = regTimeInterval;
				levelChange = true;
				Gdx.app.log(Stairs.LOG, "Level change: Zig zag");
			}
			break;
		case ZIG_ZAG_SELECTOR:
			stairColor = zigZagColors.get(roundSelector);
			currLevel = zigZagLevel;
			timeInterval = zzTimes.get(roundSelector);
			if (stairSelector >= zigZagLimiter) {
				stairSelector = 0;
				if (roundSelector < maxRound) {
					levelSelector = SIDES_SELECTOR;
				} else {
					do {
						levelSelector = random.nextInt(NUM_LEVELS) + 1;
					} while (levelSelector == ZIG_ZAG_SELECTOR);
					zigZagLimiter = random.nextInt(zigZagLimiterMax
							- zigZagLimiterMin + 1)
							+ zigZagLimiterMin;
				}
				timeInterval = regTimeInterval;
				levelChange = true;
				Gdx.app.log(Stairs.LOG, "Level change: Sides");
			}
			break;
		case SIDES_SELECTOR:
			stairColor = sidesColors.get(roundSelector);
			currLevel = sidesLevel;
			timeInterval = sidesTimes.get(roundSelector);
			if (stairSelector >= sidesLimiter) {
				stairSelector = 0;
				if (roundSelector < maxRound) {
					levelSelector = RAND_SELECTOR;
				} else {
					do {
						levelSelector = random.nextInt(NUM_LEVELS) + 1;
					} while (levelSelector == SIDES_SELECTOR);
					sidesLimiter = random.nextInt(sidesLimiterMax
							- sidesLimiterMin + 1)
							+ sidesLimiterMin;
				}
				levelChange = true;
				Gdx.app.log(Stairs.LOG, "Level change: Random");
			}
			break;
		case RAND_SELECTOR:
			int randInt = random.nextInt(3);
			float randColor1 = random
					.nextInt(255 - randMinColors[roundSelector] + 1)
					+ randMinColors[roundSelector];
			float randColor2 = random
					.nextInt(255 - randMinColors[roundSelector] + 1)
					+ randMinColors[roundSelector];
			float randColor3 = random
					.nextInt(255 - randMinColors[roundSelector] + 1)
					+ randMinColors[roundSelector];
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
			currLevel = randLevel;
			timeInterval = getRandTimeInterval();
			if (stairSelector >= randLimiter) {
				stairSelector = 0;
				if (roundSelector < maxRound) {
					if (levels) {
						// if levels, switch to next level
						levelSelector = RAPID_SELECTOR;
					} else {
						// else (if classic) stay with random
						roundChange(true);
						levelSelector = RAND_SELECTOR;
					}
				} else {
					if (levels) {
						// if levels, switcht to a random level
						do {
							levelSelector = random.nextInt(NUM_LEVELS) + 1;
						} while (levelSelector == RAND_SELECTOR);
						randLimiter = random.nextInt(randLimiterMax
								- randLimiterMin + 1)
								+ randLimiterMin;
                        // make a new random level
                        makeNewRound(true);
					} else {
						// else (if classic) stay with random
						roundChange(true);
						levelSelector = RAND_SELECTOR;
						randLimiter = random.nextInt(randLimiterMax
								- randLimiterMin + 1)
								+ randLimiterMin;
					}
				}
				levelChange = true;
				Gdx.app.log(Stairs.LOG, "Level change: Rapid");
			}
			break;
		case RAPID_SELECTOR:
			stairColor = rapidColors.get(roundSelector);
			currLevel = rapidLevel;
			timeInterval = rapidTimes.get(roundSelector);
			if (stairSelector >= rapidLimiter) {
				stairSelector = 0;
				if (roundSelector < maxRound) {
					levelSelector = NARROW_SELECTOR;
				} else {
					do {
						levelSelector = random.nextInt(NUM_LEVELS) + 1;
					} while (levelSelector == RAPID_SELECTOR);
					rapidLimiter = random.nextInt(rapidLimiterMax
							- rapidLimiterMin + 1)
							+ rapidLimiterMin;
				}
				levelChange = true;
				Gdx.app.log(Stairs.LOG, "Level change: narrow");
			}
			break;
		case NARROW_SELECTOR:
			stairColor = narrowColors.get(roundSelector);
			currLevel = narrowLevel;
			timeInterval = narrowTimes.get(roundSelector);
			if (stairSelector >= narrowLimiter) {
				/*
				 * Round change stuff
				 */
				roundChange(false);
				stairSelector = 0;
				if (roundSelector < maxRound) {
					levelSelector = ZIG_ZAG_SELECTOR;
				} else {
					do {
						levelSelector = random.nextInt(NUM_LEVELS) + 1;
					} while (levelSelector == NARROW_SELECTOR);
					narrowLimiter = random.nextInt(narrowLimiterMax
							- narrowLimiterMin + 1)
							+ narrowLimiterMin;
				}
				levelChange = true;
				Gdx.app.log(Stairs.LOG, "Level change: zig zag");
			}
			break;
		default:
			break;
		}
		// If there is a level change, note the change time
		// to use to time the level transitions.
		if (levelChange) {
			levelChangeTime = System.currentTimeMillis();
		}
		// If a round change was made, delay the speed changes until the last
		// stair of the last round exits the stage
		if (roundSelector > prevRoundSelector && world.roundChangeStair != null
				&& world.roundChangeStair.yPos < 10) {
			// Change various speeds for the new round (gravity,
			// foot x speed, etc.)
			changeRoundSpeeds();
			prevRoundSelector++;
			Gdx.app.log(Stairs.LOG, "Change constants");
		}
		// Update all the stairs
		for (Stair stair : world.stairs) {
			stair.update(delta);
		}
		/*
		 * If we are past the level transition time and enough time has passed
		 * since spawning the last stair, spawn the next one in the level.
		 */
		timeDiff = System.currentTimeMillis() - time;
		if (System.currentTimeMillis() - levelChangeTime > levelTransitionTime
				&& timeDiff >= timeInterval
				&& System.currentTimeMillis() - roundChangeTime > roundTransitionTime) {
			time = System.currentTimeMillis();
			int xPos = currLevel[stairSelector][0];
			int width = currLevel[stairSelector][1];
			world.addStair(xPos, startingYPos, width, startingHeight,
					stairColor);
			stairSelector++;
			//Gdx.app.log(Stairs.LOG, "Time interval: " + timeInterval);
		}
	}

	/** Multiplies the widths of a level by a given multiplier */
	public void multiplyLevelWidths(int[][] level, float multiplier) {
		for (int i = 0; i < level.length; i++) {
			level[i][1] *= multiplier;
		}
	}

	/** Generates a straight level */
	public int[][] generateStraightLevel(int width, int numStairs,
			boolean randWidth) {
		int[][] straightLevel = new int[numStairs][2];
		for (int i = 0; i < numStairs; i++) {
			straightLevel[i][0] = straightPosition;
			straightLevel[i][1] = width;
		}

		return straightLevel;
	}

	/** Generates a zig zag level */
	public int[][] generateZigZagLevel(int width, int numStairs,
			boolean randWidth) {
		int[][] zigZagLevel = new int[numStairs][2];
		for (int i = 0; i < numStairs; i++) {
			zigZagLevel[i][0] = zigZagPositions[i];
			zigZagLevel[i][1] = width;
		}
		return zigZagLevel;
	}

	/** Generates a sides level */
	public int[][] generateSidesLevel(int width, int numStairs,
			boolean randWidth) {
		int[][] sidesLevel = new int[numStairs][2];
		for (int i = 0; i < numStairs; i++) {
			sidesLevel[i][0] = sidesPositions[i];
			sidesLevel[i][1] = width;
		}
		return sidesLevel;
	}

	/** Generates a rapid level */
	public int[][] generateRapidLevel(int width, int numStairs, int position,
			boolean randWidth) {
		int[][] rapidLevel = new int[numStairs][2];
		for (int i = 0; i < numStairs; i++) {
			rapidLevel[i][0] = position;
			rapidLevel[i][1] = width;
		}
		return rapidLevel;
	}

	/** Generates a narrow level */
	public int[][] generateNarrowLevel(int width, int numStairs,
			boolean randWidth) {
		int[][] narrowLevel = new int[numStairs][2];
		for (int i = 0; i < numStairs; i++) {
			narrowLevel[i][0] = narrowPosition;
			narrowLevel[i][1] = width;
		}

		return narrowLevel;
	}

	/**
	 * Makes an array with the positions for the zig zag level. These positions
	 * are the positions at which the stairs will spawn at.
	 */
	public int[] makeZigZagPositions(int numStairs, int buffer, int delta) {
		int position;
		int[] zigZagPositions = new int[numStairs];
		zigZagPositionsOriginal = makeZigZagPositionsOriginal(numStairs,
				buffer, delta);
		for (int i = 0; i < numStairs; i++) {
			position = zigZagPositionsOriginal[i]
					+ (zigZagWidthOriginal - zigZagWidth) / 2;
			zigZagPositions[i] = position;
		}
		return zigZagPositions;
	}

	/**
	 * Makes an array with the positions for the zig zag level. These positions
	 * are the positions at which the stairs will end up at.
	 */
	public int[] makeZigZagPositionsOriginal(int numStairs, int buffer,
			int delta) {
		boolean right = true;
		int position = 100;
		int[] zigZagPositionsOriginal = new int[numStairs];
		for (int i = 0; i < numStairs; i++) {
			if (position + zigZagWidthOriginal > CAM_WIDTH + buffer) {
				position = CAM_WIDTH - zigZagWidthOriginal + buffer;
				zigZagPositionsOriginal[i] = position;
				position -= delta;
				right = false;
			} else if (position < 0 - buffer && !right) {
				position = 0 - buffer;
				zigZagPositionsOriginal[i] = position;
				position += delta;
				right = true;
			} else if (right) {
				zigZagPositionsOriginal[i] = position;
				position += delta;
				right = true;
			} else if (!right) {
				zigZagPositionsOriginal[i] = position;
				position -= delta;
				right = false;
			}
		}
		return zigZagPositionsOriginal;
	}

	/**
	 * Makes an array with the positions for the sides level. These positions
	 * are the positions at which the stairs will spawn at.
	 */
	public int[] makeSidesPositions(int numStairs, int width) {
		int position;
		int[] sidesPositions = new int[numStairs];
		sidesPositionsOriginal = makeSidesPositionsOriginal(numStairs, width);
		for (int i = 0; i < numStairs; i++) {
			position = sidesPositionsOriginal[i]
					+ (width * Stair.MAX_WIDTH_SCALAR - sidesWidth) / 2;
			sidesPositions[i] = position;
		}
		return sidesPositions;
	}

	/**
	 * Makes an array with the positions for the sides level. These positions
	 * are the positions at which the stairs will end up at.
	 */
	public int[] makeSidesPositionsOriginal(int numStairs, int width) {
		int position = 0;
		int[] sidePositionsOriginal = new int[numStairs];
		for (int i = 0; i < numStairs; i++) {
			if (i % 2 == 0) {
				position = 0;
				sidePositionsOriginal[i] = position;
			} else {
				position = CAM_WIDTH - width * Stair.MAX_WIDTH_SCALAR;
				sidePositionsOriginal[i] = position;
			}
		}
		return sidePositionsOriginal;
	}

	public void makeNewRound(boolean onlyRand) {
		if (!onlyRand) {
			// STRAIGHT
			straightWidth = (int) ((straightWidthOriginal * straightWidthMults
					.get(roundSelector)) / Stair.MAX_WIDTH_SCALAR);
			straightPosition = (CAM_WIDTH - straightWidth) / 2;
			straightLevel = generateStraightLevel(straightWidth, numStraight,
					false);

			// ZIG ZAG
			zigZagWidth = (int) ((zigZagWidthOriginal * zzWidthMults
					.get(roundSelector)) / Stair.MAX_WIDTH_SCALAR);
			zigZagPositions = makeZigZagPositions(numZigZag,
					zzBufferDeltas[roundSelector][0],
					zzBufferDeltas[roundSelector][1]);
			zigZagLevel = generateZigZagLevel(zigZagWidth, numZigZag, false);

			// SIDES
			sidesWidth = (int) ((sidesWidthOriginal * sidesWidthMults
					.get(roundSelector)) / Stair.MAX_WIDTH_SCALAR);
			sidesPositions = makeSidesPositions(numSides, sidesWidth);
			sidesLevel = generateSidesLevel(sidesWidth, numSides, false);

			// RAPID
			rapidWidth = (int) ((rapidWidthOriginal * rapidWidthMults
					.get(roundSelector)) / Stair.MAX_WIDTH_SCALAR);
			rapidPosition = (CAM_WIDTH - rapidWidth) / 2;
			rapidLevel = generateRapidLevel(rapidWidth, numRapid,
					rapidPosition, false);

			// NARROW
			narrowWidth = (int) ((narrowWidthOriginal * narrowWidthMults
					.get(roundSelector)) / Stair.MAX_WIDTH_SCALAR);
			narrowPosition = (CAM_WIDTH - narrowWidth) / 2;
			narrowLevel = generateNarrowLevel(narrowWidth, numNarrow, false);
		}

		// RAND
		randLevel = generateRandLevel(numRand,
				randMinMaxWidths[roundSelector][0],
				randMinMaxWidths[roundSelector][1],
				randMinMaxPositions[roundSelector][0],
				randMinMaxPositions[roundSelector][1]);
		randMinTime = randMinMaxTimes[roundSelector][0];
		randMaxTime = randMinMaxTimes[roundSelector][1];

		Gdx.app.log(Stairs.LOG, "Making new levels");
	}

	public void changeRoundSpeeds() {
		Gdx.app.log(Stairs.LOG, "Changing round speed " + roundSelector);
		// Changing gravity, x speed, jump speed
		Foot.footGravity = lowYSpeeds.get(roundSelector);
		Foot.jumpVelo = jumpSpeeds.get(roundSelector);
		Stair.lowYSpeed = lowYSpeeds.get(roundSelector);
		Stair.highXSpeed = highXSpeeds.get(roundSelector);
	}

	public int getNextRoundSelector() {
		if (roundSelector < 5) {
			return roundSelector + 1;
		}
		return roundSelector;
	}

	/** Generates a random level (random width and positions, within reason) */
	public int[][] generateRandLevel(int numStairs, int minStairWidth,
			int maxStairWidth, int minXPos, int maxXPos) {
		int randWidth, randXPos;
		int[][] randLevel = new int[numStairs][2];
		for (int i = 0; i < numStairs; i++) {
			randWidth = random.nextInt(maxStairWidth - minStairWidth + 1)
					+ minStairWidth;
			randXPos = random.nextInt(maxXPos - minXPos + 1) + minXPos;
			randLevel[i][0] = randXPos;
			randLevel[i][1] = randWidth;
		}
		return randLevel;

	}

	public int getRandTimeInterval() {
		return random.nextInt(randMaxTime - randMinTime + 1) + randMinTime;
	}

	public void randLevelChange() {
		levelSelector = random.nextInt(4) + 1;
	}

	public void roundChange(boolean classic) {
		if (roundSelector < maxRound) {
			roundSelector++;
			makeNewRound(false);
			Gdx.app.log(Stairs.LOG, "ROUND #" + roundSelector);
		} else {
			roundTransitionTime = levelTransitionTime;
		}
		// Used for knowing when to increase gravity
		roundChangeTime = System.currentTimeMillis();
		world.roundChangeStair = world.lastStair;
        if (classic) {
            makeNewRound(true);
        }
	}
}
