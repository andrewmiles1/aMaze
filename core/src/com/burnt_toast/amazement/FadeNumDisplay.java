package com.burnt_toast.amazement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FadeNumDisplay {
	//this class uses an exponential funciton to fade a number as the finger is scrolled.
	//despite my best efforts to make it modular to multpile uses I gave up a little, 
	//because android below 7 doesn't support lambda methods.
	private int currentNum;
	private int minNum;
	private int maxNum;
	
	private float currentTransp;
	//on the graph for the equation of transparency this is the x.
	private static float transPlaceholder;
	//this is how the number is displayed on screen:
	private String displayString;
	private String referenceDispStr;
	
	//drag acceleration stuff.
	private  float dragSpeed;//how fast they were dragging
	private  float dragDeaccel;//how fast it deaccelerates.
	
	//sound stuff.
	Sound soundEff;


	
	/**
	 * display style should have ~ where the number should go
	 * Min and Max number HAVE TO BE ODD.
	 * @param passDisplayStyle how the number is displayed.
	 */
	public FadeNumDisplay(String passDisplayString, int passMinNum, int passMaxNum, Sound passSound) {
		this.soundEff = passSound;
		//These next two HAVE TO BE ODD.
		if(passMinNum % 2 == 0) {//if even
			System.out.println("MIN NUM TO DISP WAS NOT ODD." + passMinNum);
			//then make it odd.
			passMinNum++;
		}
		if(passMaxNum % 2 == 0) {//if even
			System.out.println("MAX NUM TO DISP WAS NOT ODD. -1" + passMaxNum);
			//then make it not even
			passMaxNum--;
		}
		this.minNum = passMinNum;
		this.maxNum = passMaxNum;
		
		this.currentNum = this.minNum;
		currentTransp = 1;
		this.referenceDispStr = passDisplayString;
		this.displayString =	passDisplayString.replaceAll("~", Integer.toString(currentNum));
		this.dragDeaccel = 15f;
	}
	
	public void setNumber(int num) {currentNum = num;}
	public int getNumber() {return currentNum;}
	/**
	 * returns the display string as it would appear on screen.
	 * @return 
	 */
	public String getStr() { return displayString; }
	/**
	 * set's the current display style. any tildas in the string
	 * will be replaced with the current number. (where you want the number displayed.)
	 * @param passDisplayStyle
	 */
	public void setDisplayStr(String passDisplayStyle) {
		this.displayString = passDisplayStyle.replaceAll("~", Integer.toString(currentNum));
	}
	public int getMinNum() {
		return minNum;
	}
	public void setMinNum(int minNum) {
		this.minNum = minNum;
	}
	public int getMaxNum() {
		return maxNum;
	}
	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
	public void stop() {
		this.dragSpeed = 0;
	}
	
	/**
	 * needs batch to have started by method call. Makes text color fully visible.
	 * @param x draw at this x. 
	 * @param y draw at this y.
	 */
	public void draw(float x, float y) {
		MainFrame.gameFont.draw(MainFrame.batch, displayString, x, y);
		
	}
	public void drawCenteredX(float screenWidth, float y) {
		draw(screenWidth/2-FontTools.getWidthOf(displayString)/2, y);
	}
	public void playerDragged(float amountX) {
		if(currentNum == maxNum && amountX > 0 && transPlaceholder >= 0) {
			//if at the maximum number
			transPlaceholder = 0;
			//don't move the placeholder to the right. already at max.
		}
		else if(currentNum == minNum && transPlaceholder <=0 && amountX < 0) {
			//if at minimum number
			transPlaceholder = 0;
			//don't move the placeholder left.
		}
		else {//We're not at max or min so we good.
			transPlaceholder += amountX / 40;
			// divided by 20 so that the numbers incremembent slower
		}
		if(transPlaceholder > 1) {//gone to the right

			transPlaceholder = -1;
			//don't go over maximum number, but add one 
			currentNum = (currentNum == maxNum)?maxNum:currentNum + 2;
			//update the dislpay string
			displayString = referenceDispStr.replaceAll("~", Integer.toString(currentNum));
			this.soundEff.play(1.0f);
		}
		else if(transPlaceholder < -1) {
			transPlaceholder = 1;
			currentNum = (currentNum == minNum)?minNum:currentNum - 2;
			//update the dislpay string
			displayString = referenceDispStr.replaceAll("~", Integer.toString(currentNum));
			this.soundEff.play(1.0f);
		}
		//accel stuff
		dragSpeed = amountX / Gdx.graphics.getDeltaTime();
		if(dragSpeed > 60000) dragSpeed = 70000;

		
	}
	public void update() {
		if(Gdx.input.isTouched()) {
			if(Gdx.input.getDeltaX() < 1) {
				dragSpeed = 0;
			}
		}
		else {//if screen isn't being touched right now
			if(dragSpeed > 0) {
			playerDragged(dragSpeed * Gdx.graphics.getDeltaTime());
			dragSpeed -= getDeaccel();
			}
			else {
				dragSpeed = 0;
			}
		}
	}//end update
	private float getDeaccel() {
		if(dragSpeed > 6000) {
			return 3000;
		}
		else {
			return (float) (Math.abs(0.005) * dragSpeed)+20;
		}
		//return (float) (Math.pow(1.0007, dragSpeed - 0.7)+20);
	}
}//end class
