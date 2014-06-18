package com.msquared.stairs.profile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;

public class ProfileManager {
	
	// The location of the profile data file
	private static final String PROFILE_DATA_FILE = "data/profile-v1.json";
	
	// The loaded profile (may be null)
	private Profile profile;
	
	public ProfileManager() {
		// Empty constructor
	}
	
	/**
	 * Retrieves the player's profile, creating one if needed.
	 */
	public Profile retrieveProfile() {
		// Create the handle for the profile data file
		FileHandle profileDataFile = Gdx.files.local(PROFILE_DATA_FILE);
		
		// If the profile is already loaded, just return it
		if (profile != null)
			return profile;
		
		// Create the new JSON utility object
		Json json = new Json();
		
		// Check if the profile data file exists
		if (profileDataFile.exists()) {
			// Load the profile from the data file
			try {
				// Read the file as text
				String profileAsText = profileDataFile.readString().trim();
				
				// Decode the contents (if it is base64 encoded)
				if( profileAsText.matches( "^[A-Za-z0-9/+=]+$" ) ) {
                    profileAsText = Base64Coder.decodeString( profileAsText );
                }

				// Restore the state
				profile = json.fromJson(Profile.class, profileAsText);
			} catch (Exception e) {
				//Recover by creating a fresh new profile data file;
				// note that the player will lose all game progress
				profile = new Profile();
				persist(profile);
			}
		} else {
			// Create a new profile data file
			profile = new Profile();
			persist(profile);
		}
		
		return profile;
	}
	
	/**
	 * Persists the given profile
	 */
	protected void persist(Profile profile) {
		// Create the handle for the profile data file
		FileHandle profileDataFile = Gdx.files.local(PROFILE_DATA_FILE);
	
		// Create the new JSON utility object
		Json json = new Json();
		
		// Convert the given profile to text
		String profileAsText =json.toJson(profile);
		
		// Encode the text
		profileAsText = Base64Coder.encodeString(profileAsText);
		
		// Write the profile data file
		profileDataFile.writeString(profileAsText, false);
	}
	
	/**
	 * Persists the player's profile.
	 * <p>
	 * If no profile is available, this method does nothing.
	 */
	public void persist() {
		if (profile != null) {
			persist(profile);
		}
	}
}
