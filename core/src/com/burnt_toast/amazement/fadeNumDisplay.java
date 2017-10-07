package com.burnt_toast.amazement;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class fadeNumDisplay {
	//this class uses an exponential funciton to fade a number as the finger is scrolled.
	//despite my best efforts to make it modular to multpile uses I gave up a little, 
	//because android below 7 doesn't support lambda methods.
	private int currentNumber;
	
	private float currentTransp;
	//on the graph for the equation of transparency this is the x.
	private float transPlaceholder;
	//this is how the number is displayed on screen:
	private String displayStyle;
	private float tempA; //placeholder to hold the color alpha value so this can change them.

	
	/**
	 * display style should have ~ where the number should go
	 * @param passDisplayStyle how the number is displayed.
	 */
	public fadeNumDisplay(String passDisplayStyle) {
		currentTransp = 1;
		passDisplayStyle.replaceAll("~", Integer.toString(currentNumber));
		this.displayStyle = passDisplayStyle;
	}
	
	public void setNumber(int num) {currentNumber = num;}
	public int getNumber() {return currentNumber;}
	
	/**
	 * needs batch to have started by method call. Makes text color fully visible.
	 * @param x draw at this x. 
	 * @param y draw at this y.
	 */
	public void draw(float x, float y) {
		MainFrame.textColor.a = this.currentTransp;
		MainFrame.batch.setColor(MainFrame.textColor);
		MainFrame.gameFont.draw(MainFrame.batch, displayStyle, x, y);
		MainFrame.textColor.a = 1;//back to fully visible.
	}
	public void drawCenteredX(float screenWidth, float y) {
		draw(screenWidth/2-FontTools.getWidthOf(displayStyle)/2, y);
	}
	public void playerDragged(float amountX) {
		transPlaceholder += amountX;
		if(transPlaceholder > 1) {
			transPlaceholder = -1;
		}
		else if(transPlaceholder < -1) {
			transPlaceholder = 1;
		}
		currentTransp = (float) (-1.5 * Math.pow(transPlaceholder, 4) + 1);
	}
}
