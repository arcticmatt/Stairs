package com.msquared.stairs.controller;

import static java.util.Arrays.asList;
import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.msquared.stairs.Stairs;
import com.msquared.stairs.model.Stair;
import com.msquared.stairs.model.World;

public class StairControllerInsane extends StairController {

	public StairControllerInsane(World world) {
		super(world);
	}

	public StairControllerInsane(World world, boolean bool) {
		super(world, bool);

		Gdx.app.log(Stairs.LOG, "Insane");

		highXSpeeds = new ArrayList<Integer>(asList(600, 300, 600, 760, 1100, 1100));
		lowYSpeeds = new ArrayList<Integer>(asList(-1000, -500, -1000, -1100, -1200, -1200));
		jumpSpeeds = new ArrayList<Integer>(asList(700, 700, 700, 700, 700, 700));

        // STRAIGHT
        straightWidthMults = new ArrayList<Float>(asList(1f, .85f, .7f, .65f, .5f, .5f));
        straightTimes = new ArrayList<Integer>(asList(300, 300, 300, 300, 300, 300));

		// ZIG ZAG
		zzWidthMults = new ArrayList<Float>(asList(1f, .9f, .8f, .77f, .72f, .72f));
	    zzBufferDeltas[0][0] = 150;
	    zzBufferDeltas[0][1] = 150;
	    zzBufferDeltas[1][0] = 150;
	    zzBufferDeltas[1][1] = 150;
	    zzBufferDeltas[2][0] = 150;
	    zzBufferDeltas[2][1] = 150;
	    zzBufferDeltas[3][0] = 170;
	    zzBufferDeltas[3][1] = 170;
	    zzBufferDeltas[4][0] = 190;
	    zzBufferDeltas[4][1] = 190;
	    zzBufferDeltas[5][0] = 190;
	    zzBufferDeltas[5][1] = 190;
		zzTimes = new ArrayList<Integer>(asList(250, 250, 250, 240, 230, 230));

		// SIDES
		sidesWidthMults = new ArrayList<Float>(asList(1f, .98f, .94f, .92f, .90f, .90f));
		sidesTimes = new ArrayList<Integer>(asList(250, 250, 270, 260, 250, 250));

		// RAPID
		rapidWidthMults = new ArrayList<Float>(asList(1f, .9f, .8f, .6f, .5f, .5f));
		rapidTimes = new ArrayList<Integer>(asList(250, 250, 180, 160, 140, 140));

		// NARROW
		narrowWidthMults = new ArrayList<Float>(asList(1f, .95f, .9f, .9f, .9f, .9f));
		narrowTimes = new ArrayList<Integer>(asList(250, 250, 260, 240, 230, 230));

		// RAND
		randMinMaxPositions[0][0] = 145;
	    randMinMaxPositions[0][1] = 340;
	    randMinMaxPositions[1][0] = 153;
	    randMinMaxPositions[1][1] = 327;
	    randMinMaxPositions[2][0] = 162;
	    randMinMaxPositions[2][1] = 318;
	    // Starts here
	    randMinMaxPositions[3][0] = 151;
	    randMinMaxPositions[3][1] = 328;
	    randMinMaxPositions[4][0] = 138;
	    randMinMaxPositions[4][1] = 341;
	    randMinMaxPositions[5][0] = 138;
	    randMinMaxPositions[5][1] = 341;

	    randMinMaxWidths[0][0] = 40;
	    randMinMaxWidths[0][1] = 70;
	    randMinMaxWidths[1][0] = 40;
	    randMinMaxWidths[1][1] = 60;
	    randMinMaxWidths[2][0] = 35;
	    randMinMaxWidths[2][1] = 55;
	    // Starts here
	    randMinMaxWidths[3][0] = 44;
	    randMinMaxWidths[3][1] = 47;
	    randMinMaxWidths[4][0] = 43;
	    randMinMaxWidths[4][1] = 45;
	    randMinMaxWidths[5][0] = 43;
	    randMinMaxWidths[5][1] = 45;

	    randMinMaxTimes[0][0] = 450;
	    randMinMaxTimes[0][1] = 700;
	    randMinMaxTimes[1][0] = 400;
	    randMinMaxTimes[1][1] = 550;
	    randMinMaxTimes[2][0] = 250;
	    randMinMaxTimes[2][1] = 325;
	    // Starts here
	    randMinMaxTimes[3][0] = 260;
	    randMinMaxTimes[3][1] = 310;
	    randMinMaxTimes[4][0] = 250;
	    randMinMaxTimes[4][1] = 275;
	    randMinMaxTimes[5][0] = 250;
	    randMinMaxTimes[5][1] = 275;

        // TIME INTERVALS
		this.randTimeInterval = random.nextInt(100) + 400;
		regTimeInterval = 420l;
		rapidTimeInterval = 220l;
		timeInterval = regTimeInterval;
		levelTransitionTime = 700l;
		roundTransitionTime = 1250l;
		roundChangeTime = 0;

		stairSelector = 0;
		prevRoundSelector = 0;
		int mostRecent = Stairs.getSharedPrefs().getInteger("mostRecent", 1);
		if (mostRecent == 8) {
			roundSelector = 3;
		} else {
			roundSelector = 2;
		}
		Gdx.app.log(Stairs.LOG, "Round selector" + roundSelector);
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
        straightLimiter = 0;
        straightWidthOriginal = 1000;

        // Zig zag level init
		numZigZag = 50;
		zigZagLimiter = 15;
		zigZagLimiterMin = 12;
		zigZagLimiterMax = 18;
		zigZagWidthOriginal = 350;
		// UNDO
		zigZagLimiter = 25;

        // Sides levle init
		numSides = 50;
		sidesLimiter = 12;
		sidesLimiterMin = 8;
		sidesLimiterMax = 16;
		sidesWidthOriginal = 350;

        // Rand level init
		numRand = 50;
		randLimiter = 15;
		randLimiterMin = 12;
		randLimiterMax = 18;
		randTimeInterval = 500;

        // Rapid level init
		numRapid = 50;
		rapidLimiter = 8;
		rapidLimiterMin = 6;
		rapidLimiterMax = 10;
		rapidWidthOriginal = 1200;

        // Narrow level init
		numNarrow = 50;
		narrowLimiter = 6;
		narrowLimiterMin = 5;
		narrowLimiterMax = 7;
		narrowWidthOriginal = 220;

		// Set up initial level (overriden in the hard and easy controllers)
		if (!levels) {
			levelSelector = RAND_SELECTOR;
			currLevel = randLevel;
			levelTransitionTime = 700l;
			roundTransitionTime = 700l;
			randLimiter = 30;
			randLimiterMin = 20;
			randLimiterMax = 40;
		} else if (Stairs.getSharedPrefs().getBoolean("earlyOn", true)) {
			levelSelector = SIDES_SELECTOR;
			currLevel = sidesLevel;
		} else {
			levelSelector = ZIG_ZAG_SELECTOR;
			currLevel = zigZagLevel;
		}
		
		//onlyRapid();

        // Make levels corresponding to round selector
		makeNewRound(false);
        // Chagne constants corresponding to round selector
		changeRoundSpeeds(roundSelector);
	}


}
