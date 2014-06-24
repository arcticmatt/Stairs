package com.msquared.stairs.controller;

import static java.util.Arrays.asList;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.model.Stair;
import com.msquared.stairs.model.World;

public class StairControllerHard extends StairController {
	
	public StairControllerHard(World world) {
		super(world);
		// TODO Auto-generated constructor stub
	}
	
	public StairControllerHard(World world, boolean bool) {
		super(world, bool);
		
		highXSpeeds = new ArrayList<Integer>(asList(250, 300, 400, 495, 600, 600));
		lowYSpeeds = new ArrayList<Integer>(asList(-400, -500, -550, -600, -700,
	    -700));
		jumpSpeeds = new ArrayList<Integer>(asList(500, 550, 600, 600, 700, 700));
		
		// STRAIGHT
		straightWidthMults = new ArrayList<Float>(asList(1f, .9f, .8f, .75f, .7f, .7f));
		straightTimes = new ArrayList<Integer>(asList(900, 825, 750, 675, 600, 600));

		// ZIG ZAG
		zzWidthMults = new ArrayList<Float>(asList(1f, .9f, .8f, .77f, .7f, .7f));
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
		sidesWidthMults = new ArrayList<Float>(asList(1f, .96f, .96f, .92f, .86f,
	    .86f));
		sidesTimes = new ArrayList<Integer>(asList(500, 500, 470, 440, 420, 420));

		// RAPID
		rapidWidthMults = new ArrayList<Float>(asList(1f, .9f, .8f, .6f, .5f, .5f));
		rapidTimes = new ArrayList<Integer>(asList(300, 270, 255, 240, 220, 220));
		
		// NARROW
		narrowWidthMults = new ArrayList<Float>(asList(1f, .95f, .9f, .85f, .85f, .85f));
		narrowTimes = new ArrayList<Integer>(asList(450, 400, 375, 350, 325, 325));

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


		
		stairColor = white;
		
		this.randTimeInterval = random.nextInt(100) + 400;
		regTimeInterval = 420l;
		rapidTimeInterval = 220l;
		timeInterval = regTimeInterval;
		levelTransitionTime = 700l;
		roundTransitionTime = 700l;
		roundChangeTime = 0;
		
		stairSelector = 0;
		prevRoundSelector = 0;
		roundSelector = 5;
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

		// Make straight level
		numStraight = 0;
		straightLimiter = 0;
		straightWidthOriginal = 500;
		straightWidth = straightWidthOriginal / Stair.MAX_WIDTH_SCALAR;
		straightPosition = (CAM_WIDTH - straightWidth) / 2;
		straightLevel = generateStraightLevel(straightWidth, numStraight, false);
		
		// Make zig zag level
		numZigZag = 50;
		zigZagLimiterMin = 15;
		zigZagLimiterMax = 20;
		zigZagLimiter = random.nextInt(zigZagLimiterMax
				- zigZagLimiterMin + 1)
				+ zigZagLimiterMin;
		zigZagWidthOriginal = 350;
		zigZagWidth = zigZagWidthOriginal / Stair.MAX_WIDTH_SCALAR;
		zigZagPositions = makeZigZagPositions(numZigZag, zzBufferDeltas[roundSelector][0], 
				zzBufferDeltas[roundSelector][1]);
		zigZagLevel = generateZigZagLevel(zigZagWidth, numZigZag, false);
		
		// Make sides level
		numSides = 50;
		sidesLimiterMin = 12;
		sidesLimiterMax = 16;
		sidesLimiter = random.nextInt(sidesLimiterMax
				- sidesLimiterMin + 1)
				+ sidesLimiterMin;
		sidesWidthOriginal = 325;
		sidesWidth = sidesWidthOriginal / Stair.MAX_WIDTH_SCALAR;
		sidesPositions = makeSidesPositions(numSides, sidesWidth);
		sidesLevel = generateSidesLevel(sidesWidth, numSides, false);
		
		// Make random level
		numRand = 100;
		randLimiterMin = 15;
		randLimiterMax = 20;
		randLimiter = random.nextInt(randLimiterMax
				- randLimiterMin + 1)
				+ randLimiterMin;
		randLevel = generateRandLevel(numRand, randMinWidth, randMaxWidth,
				randMinPosition, randMaxPosition);
		randTimeInterval = 500;
		
		// Make rapid level
		numRapid = 50;
		rapidLimiterMin = 6;
		rapidLimiterMax = 10;
		rapidLimiter = random.nextInt(rapidLimiterMax
				- rapidLimiterMin + 1)
				+ rapidLimiterMin;
		rapidWidthOriginal = 1200;
		rapidWidth = rapidWidthOriginal / Stair.MAX_WIDTH_SCALAR;
		rapidPosition = (CAM_WIDTH - rapidWidth) / 2;
		rapidLevel = generateRapidLevel(rapidWidth, numRapid, rapidPosition, false);
		
		// Make narrow level
		numNarrow = 50;
		narrowLimiter = 10;
		narrowLimiterMin = 6;
		narrowLimiterMax = 10;
		narrowLimiter = random.nextInt(narrowLimiterMax
				- narrowLimiterMin + 1)
				+ narrowLimiterMin;
		narrowWidthOriginal = 200;
		narrowWidth = rapidWidthOriginal / Stair.MAX_WIDTH_SCALAR;
		narrowPosition = (CAM_WIDTH - narrowWidth) / 2;
		narrowLevel = generateNarrowLevel(narrowWidth, numNarrow, false);
		
		levelSelector = STRAIGHT_SELECTOR;
		currLevel = straightLevel;
		
		// Hard has its own level stuff because it starts random
		if (!levels) {
			levelSelector = RAND_SELECTOR;
			currLevel = randLevel;
			randLimiter = 30;
			randLimiterMin = 15;
			randLimiterMax = 35;
			levelTransitionTime = 650l;
			roundTransitionTime = 650l;
		} else {
			do {
				levelSelector = random.nextInt(NUM_LEVELS) + 1;
			} while (levelSelector == RAND_SELECTOR);
		}
		
		// Used to test different rounds
		makeNewRound(false);
		changeRoundSpeeds();
		
		Gdx.app.log(Stairs.LOG, "Done making levels");
	}

}
