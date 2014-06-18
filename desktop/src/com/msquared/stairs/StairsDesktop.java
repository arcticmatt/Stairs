package com.msquared.stairs;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.msquared.stairs.utils.IActivityRequestHandler;

public class StairsDesktop implements IActivityRequestHandler {
	private static StairsDesktop application;
	public static void main(String[] args) {
		if (application == null) {
			application = new StairsDesktop();
		}
		new LwjglApplication(new Stairs(false, false, application), "Stairs", 540, 860);
	}

	@Override
	public void showAds(boolean show) {
		// Empty method
	}

}
