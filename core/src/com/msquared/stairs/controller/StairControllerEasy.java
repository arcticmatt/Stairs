package com.msquared.stairs.controller;

import static java.util.Arrays.asList;
import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.model.Stair;
import com.msquared.stairs.model.World;

public class StairControllerEasy extends StairController {

	public StairControllerEasy(World world) {
		super(world);
	}

	public StairControllerEasy(World world, boolean bool) {
		super(world, bool);

		highXSpeeds = new ArrayList<Integer>(asList(200, 225, 250, 275, 300, 300));
		lowYSpeeds = new ArrayList<Integer>(asList(-300, -350, -400, -450, -500,
	    -500));
		jumpSpeeds = new ArrayList<Integer>(asList(400, 400, 450, 450, 500, 500));

		// STRAIGHT
		straightWidthMults = new ArrayList<Float>(asList(1f, .85f, .7f, .65f, .5f, .5f));
		straightTimes = new ArrayList<Integer>(asList(1125, 985, 900, 825, 730, 730));

		// ZIG ZAG
		zzWidthMults = new ArrayList<Float>(asList(1f, .92f, .85f, .80f, .77f, .77f));
	    zzBufferDeltas[0][0] = 150;
	    zzBufferDeltas[0][1] = 50;
	    zzBufferDeltas[1][0] = 150;
	    zzBufferDeltas[1][1] = 90;
	    zzBufferDeltas[2][0] = 150;
	    zzBufferDeltas[2][1] = 100;
	    zzBufferDeltas[3][0] = 150;
	    zzBufferDeltas[3][1] = 110;
	    zzBufferDeltas[4][0] = 150;
	    zzBufferDeltas[4][1] = 120;
	    zzBufferDeltas[5][0] = 150;
	    zzBufferDeltas[5][1] = 120;
		//zzTimes = new ArrayList<Integer>(asList(925, 825, 750, 675, 600, 600));
		zzTimes = new ArrayList<Integer>(asList(1125, 985, 900, 825, 730, 730));

		// SIDES
		sidesWidthMults = new ArrayList<Float>(asList(1f, .98f, .96f, .95f, .94f,
			.94f));
		//sidesTimes = new ArrayList<Integer>(asList(925, 825, 750, 675, 600, 600));
		sidesTimes = new ArrayList<Integer>(asList(1125, 985, 900, 825, 730, 730));

		// RAPID
		rapidWidthMults = new ArrayList<Float>(asList(1f, .9f, .8f, .6f, .5f, .5f));
		//rapidTimes = new ArrayList<Integer>(asList(600, 520, 450, 390, 350, 350));
		rapidTimes = new ArrayList<Integer>(asList(800, 680, 600, 540, 480, 480));

		// NARROW
		narrowWidthMults = new ArrayList<Float>(asList(1f, .94f, .88f, .82f, .80f, .80f));
		//narrowTimes = new ArrayList<Integer>(asList(820, 725, 650, 575, 500, 500));
		narrowTimes = new ArrayList<Integer>(asList(1125, 985, 900, 825, 730, 730));

		// RAND
		randMinMaxPositions[0][0] = 145;
	    randMinMaxPositions[0][1] = 340;
	    randMinMaxPositions[1][0] = 135;
	    randMinMaxPositions[1][1] = 350;
	    randMinMaxPositions[2][0] = 147;
	    randMinMaxPositions[2][1] = 332;
	    randMinMaxPositions[3][0] = 145;
	    randMinMaxPositions[3][1] = 335;

	    randMinMaxPositions[4][0] = 138+10;
	    randMinMaxPositions[4][1] = 341-10;
	    randMinMaxPositions[5][0] = 138+10;
	    randMinMaxPositions[5][1] = 341-10;

	    randMinMaxWidths[0][0] = 60;
	    randMinMaxWidths[0][1] = 70;
	    randMinMaxWidths[1][0] = 55;
	    randMinMaxWidths[1][1] = 65;
	    randMinMaxWidths[2][0] = 50;
	    randMinMaxWidths[2][1] = 60;
	    randMinMaxWidths[3][0] = 45;
	    randMinMaxWidths[3][1] = 55;
	    randMinMaxWidths[4][0] = 40;
	    randMinMaxWidths[4][1] = 50;
	    randMinMaxWidths[5][0] = 40;
	    randMinMaxWidths[5][1] = 50;
	    

        randMinMaxTimes[0][0] = 1050;
	    randMinMaxTimes[0][1] = 1150;
	    randMinMaxTimes[1][0] = 935;
	    randMinMaxTimes[1][1] = 1035;
	    randMinMaxTimes[2][0] = 850;
	    randMinMaxTimes[2][1] = 950;
	    randMinMaxTimes[3][0] = 775;
	    randMinMaxTimes[3][1] = 875;
	    randMinMaxTimes[4][0] = 680;
	    randMinMaxTimes[4][1] = 780;
	    randMinMaxTimes[5][0] = 680;
	    randMinMaxTimes[5][1] = 780;

        // TIME INTERVALS
		this.randTimeInterval = random.nextInt(100) + 400;
		regTimeInterval = 420l;
		rapidTimeInterval = 220l;
		timeInterval = regTimeInterval;
		levelTransitionTime = 1350l;
		roundTransitionTime = 2500l;
		roundChangeTime = 0;

		stairSelector = 0;
		prevRoundSelector = 0;
		roundSelector = 0;
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
		numStraight = 50;
		straightLimiter = 10;
		straightLimiterMin = 10;
		straightLimiterMax = 15;
		straightWidthOriginal = 1000;
		straightWidth = straightWidthOriginal / Stair.MAX_WIDTH_SCALAR;
		straightPosition = (CAM_WIDTH - straightWidth) / 2;
		straightLevel = generateStraightLevel(straightWidth, numStraight, false);

		// Zig zag level init
		numZigZag = 50;
		zigZagLimiter = 10;
		zigZagLimiterMin = 10;
		zigZagLimiterMax = 15;
		zigZagWidthOriginal = 380;

		// Sides level init
		numSides = 50;
		sidesLimiter = 10;
		sidesLimiterMin = 10;
		sidesLimiterMax = 15;
		sidesWidthOriginal = 375;

		// Rand level init
		numRand = 50;
		randLimiter = 10;
		randLimiterMin = 10;
		randLimiterMax = 15;
		randTimeInterval = 500;

		// Rapid level init
		numRapid = 50;
		rapidLimiter = 8;
		rapidLimiterMin = 8;
		rapidLimiterMax = 12;
		rapidWidthOriginal = 1200;

		// Narrow level init
		numNarrow = 50;
		narrowLimiter = 8;
		narrowLimiterMin = 6;
		narrowLimiterMax = 10;
		narrowWidthOriginal = 240;

		// Easy has its own level stuff because it can start straight
		if (!levels) {
			levelSelector = RAND_SELECTOR;
			currLevel = randLevel;
			randLimiter = 35;
            randLimiterMin = 25;
            randLimiterMax = 45;
		} else if (Stairs.getSharedPrefs().getBoolean("earlyOn", true)) {
			levelSelector = ZIG_ZAG_SELECTOR;
			currLevel = zigZagLevel;
		} else {
			levelSelector = STRAIGHT_SELECTOR;
			currLevel = straightLevel;
		}

		// Make levels corresponding to round selector
		makeNewRound(false);
		// Change constants corresponding to round selector
		changeRoundSpeeds(roundSelector);
	}


}
