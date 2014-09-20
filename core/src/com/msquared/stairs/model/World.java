package com.msquared.stairs.model;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Pool;
import com.msquared.stairs.Stairs;

public class World {

	public int width;
	public int height;
	public LinkedList<Stair> stairs = new LinkedList<Stair>();
	public Queue<Stair> roundChangeStairs = new LinkedList<Stair>();
	public Foot leftFoot;
	public Foot rightFoot;
	public boolean gameOver = false;
	public long gameOverTime = 0;
	public int score = 0;
	public int numStairs;
	public int gameOverType;
	Color white = new Color(255, 255, 255, 1);
	Color black = new Color(0, 0, 0, 1);
	public Stair lastStair;
	public static int difficulty;
	Random random;
	public int earlySelector;

	protected int[] randMinColors = { 220, 170, 130, 90, 0, 0 };
	protected int[] randMaxColors = { 25, 85, 125, 165, 255, 255 };

	protected int[][] classicXPositions = {

			{ 199, 184, 154, 326, 261, 145, 295 },

			{ 179, 190, 202, 198, 300, 188, 208,
					190, 232, 235, 323, 210, 235,
					233 },

			{ 233, 278, 214, 244, 303, 225, 216, 285,
					157, 274, 301, 249, 309, 213,
					262, 215, 274, 333, 188 },

			{ 287, 175, 268, 267, 259, 189,
					192, 282, 205, 246, 276, 229, 284,
					209, 191, 319 }

	};
	protected int[][] classicYPositions = {

			{ 569, 635, 682, 725, 770, 817, 860 },

			{ 461, 531, 581, 615, 644, 669, 693,
					717, 737, 762, 785, 808, 830,
					858 },

			{ 460, 523, 561, 590, 614,
				635, 652, 671, 687, 704,
				720, 736, 754, 770, 786,
				804, 821, 839, 855 },

			{ 583, 626,
			655, 678, 696, 712, 727, 741, 755,
			769, 784, 798, 812, 826, 840, 857 }
	};

    protected int[] easyStraightXPositions = { 186, 186, 186, 186, 186, 186, 186 };

    protected int[] easyStraightYPositions = { 570, 635, 681, 726, 771, 816, 860 };

	protected int[] mediumZigZagXPositions = { 244, 336, 465, 471, 357,
        241, 126, 9, -1, 119, 239, 359, 479 };

	protected int[] mediumZigZagYPositions = { 473, 541, 589, 626, 653,
        678, 703, 728, 753, 778, 803, 829, 850 };

    protected int[] hardZigZagXPositions = { 250, 400, 506, 341, 174, 7, -30,
    		143, 315, 485, 525, 355, 185, 15, -22 };

    protected int[] hardZigZagYPositions = { 492, 547, 588, 619, 644, 666,
    		688, 709, 731, 753, 774, 796, 818, 839, 860 };

    protected int[] hardSidesXPositions = { 78, 351, 93, 362, 103, 370, 110,
    		377, 115, 379, 115, 379, 115, 379, 115 };

    protected int[] hardSidesYPositions = { 492, 547, 588, 619, 644, 666, 688,
    		709, 731, 753, 774, 796, 818, 839, 860 };

    protected int[] hardRapidXPositions = { 216, 220, 220, 220, 220, 220, 220,
        220, 220, 220, 220, 220, 220, 220 };

    protected int[] hardRapidYPositions = { 709, 721, 733, 744, 756, 768, 779,
        791, 803, 814, 826, 838, 849, 860 };

    protected int[] hardNarrowXPositions = { 248, 250, 252, 253, 255, 256,
        256, 256, 256, 256, 256, 256, 256, 256 };

    protected int[] hardNarrowYPositions = { 644, 661, 677, 694, 711, 727, 744,
        761, 778, 794, 811, 828, 844, 860 };

	protected int[] insaneZigZagXPositions = { 246, 380, 476, 330, 184, 37,
			0, 152, 302, 452, 492, 342, 192, 42, 2, 152, 302,  452, 492 };

	protected int[] insaneZigZagYPositions = { 538, 586, 624, 651, 673,
			691, 706, 719, 732, 744, 758, 771, 783, 797, 809, 823, 835, 848, 860 };

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
		this.random = Stairs.randomGenerator;
		earlySelector = random.nextInt(4) + 1;
	}

	public void createDemoWorld() {
		// If earlyOn pref is not on, don't spawn early stairs. Else, do.
		boolean earlyOn = Stairs.getSharedPrefs().getBoolean("earlyOn", true);
		if (earlyOn) {
			Gdx.app.log(Stairs.LOG, "earlyOn");
			if (difficulty == Stairs.EASY_LEVELS) {
				addEasyStraight();
				//addStair(187, 880, 1000 / 6, 2, white);
			} else if (difficulty == Stairs.MEDIUM_LEVELS) {
				addMediumZigZag();
			} else if (difficulty == Stairs.HARD_LEVELS) {
				switch (earlySelector) {
				case 1:
					addHardZigZag();
					break;
				case 2:
					addHardSides();
					break;
				case 3:
					addHardNarrow();
					break;
				case 4:
					addHardRapid();
					break;
				}
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
		addClassicLevel(3, 3, 44, 47);
	}

	public void addClassicLevel(int round, int diff, int minWidth, int maxWidth) {
		Color stairColor = white;

		int maxX;
		int minX;
		int x;
		int y;
		int randWidth;
		int randBool = 0;
		for (int i = 0; i < classicXPositions[diff].length; i++) {
			stairColor = selectStairColorForRand(round);

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

    public void addEasyStraight() {
        Color stairColor;
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
            stairColor = black;
        } else {
            stairColor = white;
        }
        int x;
        int y;

        int width = 166;
        for (int i = 0; i < easyStraightXPositions.length - 1; i++) {
            x = (int) easyStraightXPositions[i];
            y = (int) easyStraightYPositions[i] + 10;
            addStair(x, y, width, 2, stairColor);
        }
    }

	public void addMediumZigZag() {
        Color stairColor;
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
            stairColor = new Color(0f / 255f,
                    13f / 255f, 51f / 255f, 1);
        } else {
            stairColor = new Color(255f / 255f,
                    235f / 255f, 176f / 255f, 1);
        }
		int x;
		int y;
        /*
         * Calculated from:
         * zigZagWidth = (int) ((zigZagWidthOriginal * zzWidthMults
         * .get(roundSelector)) / Stair.MAX_WIDTH_SCALAR);
         */
		int width = 52;
		for (int i = 0; i < mediumZigZagXPositions.length - 1; i++) {
			x = (int) mediumZigZagXPositions[i];
			y = (int) mediumZigZagYPositions[i];
			addStair(x, y, width, 2, stairColor);
		}
	}

    public void addHardZigZag() {
        Color stairColor;
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
            stairColor = new Color(23f / 255f,
                    67f / 255f, 192f / 255f, 1);
        } else {
            stairColor = new Color(232f / 255f,
                    188f / 255f, 63f / 255f, 1);
        }
		int x;
		int y;
        /*
         * Calculated from:
         * zigZagWidth = (int) ((zigZagWidthOriginal * zzWidthMults
         * .get(roundSelector)) / Stair.MAX_WIDTH_SCALAR);
         */
		int width = 41;
		for (int i = 0; i < hardZigZagXPositions.length - 1; i++) {
			x = (int) hardZigZagXPositions[i];
			y = (int) hardZigZagYPositions[i];
			addStair(x, y, width, 2, stairColor);
		}
	}

    public void addHardSides() {
        Color stairColor;
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
            stairColor = new Color(0f / 255f,
                    184f / 255f, 165f / 255f, 1);
        } else {
            stairColor = new Color(255f / 255f,
                    71f / 255f, 90f / 255f, 1);
        }
		int x;
		int y;
        /*
         * Calculated from:
         * sidesWidth = (int) ((sidesWidthOriginal * sidesWidthMults
		 * .get(roundSelector)) / Stair.MAX_WIDTH_SCALAR);
         */
		int width = 47;
		for (int i = 0; i < hardSidesXPositions.length - 1; i++) {
			x = (int) hardSidesXPositions[i];
			y = (int) hardSidesYPositions[i];
			addStair(x, y, width, 2, stairColor);
		}
	}

    public void addHardRapid() {
        Color stairColor;
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
            stairColor = new Color(115f / 255f,
                    195f / 255f, 41f / 255f, 1);
        } else {
            stairColor = new Color(140f / 255f,
                    60f / 255f, 214f / 255f, 1);
        }
		int x;
		int y;
        /*
         * Calculated from:
         * rapidWidth = (int) ((rapidWidthOriginal * rapidWidthMults
		 * .get(roundSelector)) / Stair.MAX_WIDTH_SCALAR);
         */
		int width = 100;
		for (int i = 0; i < hardRapidXPositions.length - 1; i++) {
			x = (int) hardRapidXPositions[i];
			y = (int) hardRapidYPositions[i];
			addStair(x, y, width, 2, stairColor);
		}
	}

    public void addHardNarrow() {
        Color stairColor;
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
            stairColor = new Color(180f / 255f,
                    97f / 255f, 20f / 255f, 1);
        } else {
            stairColor = new Color(75f / 255f,
                    158f / 255f, 235f / 255f, 1);
        }
		int x;
		int y;
        /*
         * Calculated from:
         * narrowWidth = (int) ((narrowWidthOriginal * narrowWidthMults
		 * .get(roundSelector)) / Stair.MAX_WIDTH_SCALAR);
         */
		int width = 29;
		for (int i = 0; i < hardNarrowXPositions.length - 1; i++) {
			x = (int) hardNarrowXPositions[i];
			y = (int) hardNarrowYPositions[i];
			addStair(x, y, width, 2, stairColor);
		}
	}

	public void addInsaneZigZag() {
        Color stairColor;
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
            stairColor = new Color(0f / 255f,
                    38f / 255f, 147f / 255f, 1);
        } else {
            stairColor = new Color(255f / 255f,
                    227f / 255f, 145f / 255f, 1);
        }
		int x;
		int y;
        /*
         * Calculated from:
         * zigZagWidth = (int) ((zigZagWidthOriginal * zzWidthMults
         * .get(roundSelector)) / Stair.MAX_WIDTH_SCALAR);
         */
		int width = 47;
		for (int i = 0; i < insaneZigZagXPositions.length - 2; i++) {
			x = (int) insaneZigZagXPositions[i];
			y = (int) insaneZigZagYPositions[i];
			addStair(x, y, width, 2, stairColor);
		}
	}

    public Color selectStairColorForRand(int roundSelector) {
    	Color stairColor;
		int randInt = random.nextInt(3);
		float randColor1;
		float randColor2;
		float randColor3;
		float placeholderColor;
		if (Stairs.getSharedPrefs().getBoolean("invertOn")) {
			randColor1 = random.nextInt(randMaxColors[roundSelector]);
			randColor2 = random.nextInt(randMaxColors[roundSelector]);
			randColor3 = random.nextInt(randMaxColors[roundSelector]);
			placeholderColor = 0f;
		} else {
			randColor1 = random.nextInt(255 - randMinColors[roundSelector] + 1)
					+ randMinColors[roundSelector];
			randColor2 = random.nextInt(255 - randMinColors[roundSelector] + 1)
					+ randMinColors[roundSelector];
			randColor3 = random.nextInt(255 - randMinColors[roundSelector] + 1)
					+ randMinColors[roundSelector];
			placeholderColor = 255f;
		}
		switch (randInt) {
		case 0:
			stairColor = new Color(placeholderColor, randColor2 / 255f, randColor3 / 255f,
					1);
			break;
		case 1:
			stairColor = new Color(randColor1 / 255f, placeholderColor, randColor3 / 255f,
					1);
			break;
		case 2:
			stairColor = new Color(randColor1 / 255f, randColor2 / 255f, placeholderColor,
					1);
			break;
		default:
			stairColor = new Color(randColor1 / 255f, randColor2 / 255f, placeholderColor,
					1);
		}
		return stairColor;
	}
}
