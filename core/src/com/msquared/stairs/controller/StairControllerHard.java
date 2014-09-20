package com.msquared.stairs.controller;

import static java.util.Arrays.asList;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.model.World;

public class StairControllerHard extends StairController {

	public StairControllerHard(World world) {
		super(world);
	}

	public StairControllerHard(World world, boolean bool, int earlySelector) {
		super(world, bool);
		this.earlySelector = earlySelector;

		highXSpeeds = new ArrayList<Integer>(asList(250, 300, 400, 495, 600, 600));
		lowYSpeeds = new ArrayList<Integer>(asList(-400, -500, -550, -600, -700,
	    -700));
		jumpSpeeds = new ArrayList<Integer>(asList(500, 550, 600, 600, 700, 700));

        // STRAIGHT
        straightWidthMults = new ArrayList<Float>(asList(1f, .9f, .8f, .75f, .7f, .7f));
        straightTimes = new ArrayList<Integer>(asList(900, 825, 750, 675, 600, 600));

		// ZIG ZAG
		zzWidthMults = new ArrayList<Float>(asList(1f, .9f, .8f, .77f, .74f, .74f));
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
		narrowWidthMults = new ArrayList<Float>(asList(1f, .95f, .9f, .87f, .87f, .87f));
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

        // TIME INTERVALS
		this.randTimeInterval = random.nextInt(100) + 400;
		regTimeInterval = 420l;
		rapidTimeInterval = 220l;
		timeInterval = regTimeInterval;
		levelTransitionTime = 800l;
		roundTransitionTime = levelTransitionTime;
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

        // Straight level init
        numStraight = 0;
        straightLimiter = 0;
        straightWidthOriginal = 500;

        // Zig zag level init
		numZigZag = 50;
		zigZagLimiterMin = 15;
		zigZagLimiterMax = 20;
		zigZagLimiter = random.nextInt(zigZagLimiterMax
				- zigZagLimiterMin + 1)
				+ zigZagLimiterMin;
		zigZagWidthOriginal = 358;

        // Sides level init
		numSides = 50;
		sidesLimiterMin = 12;
		sidesLimiterMax = 16;
		sidesLimiter = random.nextInt(sidesLimiterMax
				- sidesLimiterMin + 1)
				+ sidesLimiterMin;
		sidesWidthOriginal = 348;

        // Rand level init
		numRand = 100;
		randLimiterMin = 15;
		randLimiterMax = 20;
		randLimiter = random.nextInt(randLimiterMax
				- randLimiterMin + 1)
				+ randLimiterMin;

        // Rapid level init
		numRapid = 50;
		rapidLimiterMin = 9;
		rapidLimiterMax = 12;
		rapidLimiter = random.nextInt(rapidLimiterMax
				- rapidLimiterMin + 1)
				+ rapidLimiterMin;
		rapidWidthOriginal = 1200;

        // Narrow level init
		numNarrow = 50;
		narrowLimiterMin = 8;
		narrowLimiterMax = 12;
		narrowLimiter = random.nextInt(narrowLimiterMax
				- narrowLimiterMin + 1)
				+ narrowLimiterMin;
		narrowWidthOriginal = 227;

		Gdx.app.log(Stairs.LOG, "levelSelector before = " + levelSelector);
		Gdx.app.log(Stairs.LOG, "earlySelector = " + earlySelector);
		// Hard has its own level stuff because it starts random
		if (!levels) {
			levelSelector = RAND_SELECTOR;
			currLevel = randLevel;
			randLimiter = 30;
			randLimiterMin = 20;
			randLimiterMax = 40;
			levelTransitionTime = 1000l;
			roundTransitionTime = 1000l;
		} else {
			do {
				levelSelector = random.nextInt(NUM_LEVELS) + 1;
			} while (levelSelector == earlySelector);
		}
		Gdx.app.log(Stairs.LOG, "levelSelector after = " + levelSelector);
		
		//onlyNarrow();

		// Make levels corresonding to round selector
		makeNewRound(false);
        // Change constants corresponding to round selector
		changeRoundSpeeds(roundSelector);

		Gdx.app.log(Stairs.LOG, "Done making levels");
	}

}
