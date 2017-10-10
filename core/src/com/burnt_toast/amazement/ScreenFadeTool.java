package com.burnt_toast.amazement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreenFadeTool {
	private static boolean fadeIn;
	private static boolean fadeOut;
	private static float fadeTracker;//transparency amount from 0 to 1
	private static float fadeFactor = 1.5f;
	private static String fadeCode;
	private static SpriteBatch tempBatch;
	private static BitmapFont tempFont;
	
	public static boolean isFading() {
		//if either is true, return true,
		//otherwise return false.
		return (fadeIn || fadeOut)? true:false;
	}
	public static String updateFade() {
		if(fadeIn) {
			if(fadeTracker <1) {
				fadeTracker += fadeFactor * Gdx.graphics.getDeltaTime();
			}
			if(fadeTracker > 1) {
				fadeTracker = 1;
				fadeIn = false;
				return fadeCode;
			}
		}
		else if(fadeOut) {
			if(fadeTracker > 0) {
				fadeTracker -= fadeFactor * Gdx.graphics.getDeltaTime();
			}
			else if(fadeTracker < 0) {
				fadeTracker = 0;
				fadeOut = false;
				return fadeCode;
			}
		}
		return "none lol";
	}
	/**
	 * updates the alpha/color to the font or batch you pass to it
	 * to effectively fade.
	 * @param t whatever you want updated/faded
	 * @return returns the 'fade code' you entered when you told it to fade,
	 *  So you know what to do when done. Returns "NONE" if no fade code.
	 */
	public static <T> T fadeItem(T t) {

		if(t.getClass() == SpriteBatch.class) {
			//if sprite batch
			tempBatch = (SpriteBatch)t;
			tempBatch.setColor(tempBatch.getColor().r, 
					tempBatch.getColor().g, tempBatch.getColor().b, fadeTracker);
		}
		else if(t.getClass() == BitmapFont.class) {
			//if bitmap font
			tempFont = (BitmapFont)t;
			tempFont.setColor(tempFont.getColor().r,
					tempFont.getColor().g, tempFont.getColor().b, fadeTracker);
		}
		else if(t.getClass() == Color.class) {
			((Color)t).a = fadeTracker;
		}
		return null;
	}
	public static void fadeIn(String passFadeCode) {
		fadeCode = passFadeCode;
		if(fadeOut)fadeOut = false;
		fadeIn = true;//make sure that it's set to fade in
	}
	public static void fadeOut(String passFadeCode) {
		fadeCode = passFadeCode;
		if(fadeOut)fadeOut = false;
		fadeOut = true;//make sure that it's set to fade out
	}
	/**
	 * I made this so that using the fade in the clear method for 
	 * @return the amount of fade in float from 0-1 0 being black and 1 being not.
	 */
	public static float getFadeTracker() {return fadeTracker;}

}
