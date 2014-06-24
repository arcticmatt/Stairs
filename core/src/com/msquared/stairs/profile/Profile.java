package com.msquared.stairs.profile;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

/**
 * The player's profile.
 * <p>
 * This class is used to store the game progress, and is persisted to the file
 * system when the game exists.
 *
 * @see ProfileManager
 */

public class Profile implements Serializable {

	private Map<Integer, Integer> highScores;
	
	public Profile() {
		highScores = new HashMap<Integer, Integer>();
		// Levels highscores
		highScores.put(1,  0);
		highScores.put(2, 0);
		highScores.put(3, 0);
		highScores.put(4, 0);
		highScores.put(5, 0);
		highScores.put(6, 0);
		highScores.put(7, 0);
		highScores.put(8, 0);
		highScores.put(9, 0);
		highScores.put(10, 0);
		highScores.put(11, 0);
		highScores.put(12, 0);
		
		// Classic highscores
		highScores.put(13,  0);
		highScores.put(14, 0);
		highScores.put(15, 0);
		highScores.put(16, 0);
		highScores.put(17, 0);
		highScores.put(18, 0);
		highScores.put(19, 0);
		highScores.put(20, 0);
		highScores.put(21, 0);
		highScores.put(22, 0);
		highScores.put(23, 0);
		highScores.put(24, 0);
	}
	
	public Map<Integer, Integer> getHighScores() {
		return highScores;
	}
	
	public int getHighScore(int id) {
		if (highScores == null)
			return 0;
		Integer highScore = highScores.get(id);
		return highScore;
	}
	
	public int isHighScore(int score, int key1, int key2, int key3) {
		int firstPast = highScores.get(key1);
		int secondPast = highScores.get(key2);
		if (score > highScores.get(key1)) {
			highScores.put(key1, score);
			highScores.put(key2, firstPast);
			highScores.put(key3, secondPast);
			return 1;
		} else if (score > highScores.get(key2)) {
			highScores.put(key2, score);
			highScores.put(key3, secondPast);
			return 2;
		} else if (score > highScores.get(key3)) {
			highScores.put(key3, score);
			return 3;
		} else {
			return 0;
		}
	}
	
	public int isEasyHighScore(int score) {
		return isHighScore(score, 1, 2, 3);
	}
	
	public int isMediumHighScore(int score) {
		return isHighScore(score, 4, 5, 6);
	}
	
	public int isHardHighScore(int score) {
		return isHighScore(score, 7, 8, 9);
	}
	
	public int isInsaneHighScore(int score) {
		return isHighScore(score, 10, 11, 12);
	}
	
	public int isEasyClassicHighScore(int score) {
		return isHighScore(score, 13, 14, 15);
	}
	
	public int isMediumClassicHighScore(int score) {
		return isHighScore(score, 16, 17, 18);
	}
	
	public int isHardClassicHighScore(int score) {
		return isHighScore(score, 19, 20, 21);
	}
	
	public int isInsaneClassicHighScore(int score) {
		return isHighScore(score, 22, 23, 24);
	}
	
	@Override
	public void write(Json json) {
		// TODO Auto-generated method stub
		json.writeValue("highScores", highScores);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		// TODO Auto-generated method stub
		// read the some basic properties
        Map<String, Integer> highScores = json.readValue("highScores", HashMap.class,
        		Integer.class, jsonData);
        for (String id : highScores.keySet()) {
        	int scoreId = Integer.valueOf(id);
        	Integer highScore = highScores.get(id);
        	this.highScores.put(scoreId, highScore);
        }
	}
	
	public void resetScores() {
		// Levels highscores
		highScores.put(1, 0);
		highScores.put(2, 0);
		highScores.put(3, 0);
		highScores.put(4, 0);
		highScores.put(5, 0);
		highScores.put(6, 0);
		highScores.put(7, 0);
		highScores.put(8, 0);
		highScores.put(9, 0);
		highScores.put(10, 0);
		highScores.put(11, 0);
		highScores.put(12, 0);

		// Classic highscores
		highScores.put(13, 0);
		highScores.put(14, 0);
		highScores.put(15, 0);
		highScores.put(16, 0);
		highScores.put(17, 0);
		highScores.put(18, 0);
		highScores.put(19, 0);
		highScores.put(20, 0);
		highScores.put(21, 0);
		highScores.put(22, 0);
		highScores.put(23, 0);
		highScores.put(24, 0);
	}
	
	public void setHighScores() {
		// Levels highscores
		highScores.put(1, 0);
		highScores.put(2, 0);
		highScores.put(3, 0);
		highScores.put(4, 150);
		highScores.put(5, 0);
		highScores.put(6, 0);
		highScores.put(7, 100);
		highScores.put(8, 0);
		highScores.put(9, 0);
		highScores.put(10, 100);
		highScores.put(11, 0);
		highScores.put(12, 0);

		// Classic highscores
		highScores.put(13, 0);
		highScores.put(14, 0);
		highScores.put(15, 0);
		highScores.put(16, 150);
		highScores.put(17, 0);
		highScores.put(18, 0);
		highScores.put(19, 100);
		highScores.put(20, 0);
		highScores.put(21, 0);
		highScores.put(22, 100);
		highScores.put(23, 0);
		highScores.put(24, 0);
	}

}
