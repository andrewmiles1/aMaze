package com.burnt_toast.amazement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

//assists in transitioning the screen
public class ScreenTransitionTool {
	private boolean moving;//if the screen is transitioning right now.
	private Vector2 targetPosition;//the place the screen is moving to
	private OrthographicCamera orthoCam;
	private char direction;
	private String transCode;
	
	//speed and accel
	private float currentSpeed;
	private float maxSpeed;
	

	public ScreenTransitionTool(OrthographicCamera passCam) {
		this.orthoCam = passCam;
		targetPosition = new Vector2();
		maxSpeed = 30;
	}
	
	public void start(String passCode, char passDirect) {
		direction = passDirect;
		transCode = passCode;

		switch(direction) {
		case 'u':
			targetPosition.x = Gdx.graphics.getWidth()/2;
			targetPosition.y = Gdx.graphics.getHeight() * 1.5f;
			break;
		case 'r':
			targetPosition.x = Gdx.graphics.getWidth() * 1.5f;
			targetPosition.y = Gdx.graphics.getHeight()/2;
			break;
		case 'd':
			targetPosition.x = Gdx.graphics.getWidth()/2;
			targetPosition.y = -1 * Gdx.graphics.getHeight()/2;
			break;
		case 'l':
			targetPosition.x = -1 * Gdx.graphics.getWidth()/2;
			targetPosition.y = Gdx.graphics.getHeight()/2;
			break;
		}
		moving = true;
	}
	
	/**
	 * sends camera to passed position.
	 * passed position is target center of screen.
	 * This method also starts the camera moving.
	 * @param x center x of desired screen pos
	 * @param y center y of desired screen pos.
	 */
	public void setToCenter(float x, float y) {
		targetPosition.x = x;
		targetPosition.y = y;
		currentSpeed = 2f;
		moving = true;
	}
	/**
	 * sends camera to passed position
	 * passed position is target bottom left of screen.
	 * does the math from gdx.graphics.getWidth().
	 * This method also starts the camera moving
	 * @param x bottom left x of desired screen pos
	 * @param y bottom left y of desidred screen pos
	 */
	public void sendToBotLeft(float x, float y) {
		targetPosition.x = x + Gdx.graphics.getWidth()/2;
		targetPosition.y = y + Gdx.graphics.getHeight()/2;
		currentSpeed = 2f;
		moving = true;
	}
	
	public String update() {
		
		if(moving) {
			if(orthoCam.position.x == targetPosition.x &&
					orthoCam.position.y == targetPosition.y) {
				//if we've reached target position
				if(orthoCam.position.x == Gdx.graphics.getWidth()/2 &&
					orthoCam.position.y == Gdx.graphics.getHeight()/2) {
					//and we're back to the origin, 0,0
					moving = false;
					return "";//we done, no more work here.
				}
				else {
					//we've reached the change screen point.
					//we have to move the camera to the other
					//position giving it the illusion that
					//it pans linearly to the direction to the next screen.
					switch(direction) {
					case 'u':
						orthoCam.position.x = Gdx.graphics.getWidth()/2;
						orthoCam.position.y = -1 * Gdx.graphics.getHeight()/2;
						break;
					case 'r':
						orthoCam.position.x = -1 * Gdx.graphics.getWidth()/2;
						orthoCam.position.y = Gdx.graphics.getHeight()/2;
						break;
					case 'd':
						orthoCam.position.x = Gdx.graphics.getWidth()/2;
						orthoCam.position.y = Gdx.graphics.getHeight() * 1.5f;

						break;
					case 'l':
						orthoCam.position.x = Gdx.graphics.getWidth() * 1.5f;
						orthoCam.position.y = Gdx.graphics.getHeight()/2;
						break;
					}
					targetPosition.x = Gdx.graphics.getWidth()/2;
					targetPosition.y = Gdx.graphics.getHeight()/2;
					return transCode;
				}
			}
			if(currentSpeed < 0) {
				currentSpeed = 0;
			}
			currentSpeed += getAccel();
			System.out.println(currentSpeed);
		}
		
		if(orthoCam.position.x != targetPosition.x) {//if x isn't there yet
			if(Math.abs(orthoCam.position.x - targetPosition.x) < currentSpeed) {
				//if we're close enough to the target that we'll pass it
				orthoCam.position.x = targetPosition.x;//then set it.
			}
			if(orthoCam.position.x > targetPosition.x) {
				//if target is to left of current pos
				orthoCam.position.x -= currentSpeed;
			}
			if(orthoCam.position.x < targetPosition.x) {
				//if target is to right of current pos
				orthoCam.position.x += currentSpeed;
			}
		}
		if(orthoCam.position.y != targetPosition.y) {
			System.out.println("Really?");
			//checking
			if(Math.abs(orthoCam.position.y - targetPosition.y) < currentSpeed) {
				//if we're close enough to the target that we'll pass it
				orthoCam.position.y = targetPosition.y;//then set it.
			}
			//actually moving
			if(orthoCam.position.y > targetPosition.y) {
				//if target is below current pos
				orthoCam.position.y -= currentSpeed;
			}
			if(orthoCam.position.y < targetPosition.y) {
				//if target is above current pos
				orthoCam.position.y += currentSpeed;
			}
		}
		orthoCam.update();
		return "";
	}
	
	public void updateOld() {
		if(moving) {
			if(orthoCam.position.x == targetPosition.x &&
					orthoCam.position.y == targetPosition.y) {
				moving = false;
			}
			if(currentSpeed < 0) {
				currentSpeed = 0;
			}
			currentSpeed += getAccel();
		}
		
		if(orthoCam.position.x != targetPosition.x) {//if x isn't there yet
			if(Math.abs(orthoCam.position.x - targetPosition.x) < currentSpeed) {
				//if we're close enough to the target that we'll pass it
				orthoCam.position.x = targetPosition.x;//then set it.
			}
			if(orthoCam.position.x > targetPosition.x) {
				//if target is to left of current pos
				orthoCam.position.x -= currentSpeed;
			}
			if(orthoCam.position.x < targetPosition.x) {
				//if target is to right of current pos
				orthoCam.position.x += currentSpeed;
			}
		}
		if(orthoCam.position.y != targetPosition.y) {
			//checking
			if(Math.abs(orthoCam.position.y - targetPosition.y) < currentSpeed) {
				//if we're close enough to the target that we'll pass it
				orthoCam.position.y = targetPosition.y;//then set it.
			}
			//actually moving
			if(orthoCam.position.y > targetPosition.y) {
				//if target is below current pos
				orthoCam.position.y -= currentSpeed;
			}
			if(orthoCam.position.y < targetPosition.y) {
				//if target is above current pos
				orthoCam.position.y += currentSpeed;
			}
		}
		orthoCam.update();
	}
	
	private float getAccel() {
		
		if(MainFrame.distForm(orthoCam.position, targetPosition) < 200) {
			return (float) (-1 * Math.abs(0.0001 * MainFrame.distForm(orthoCam.position, targetPosition)));
		}
		else if(currentSpeed < maxSpeed) {
			return (float) Math.abs(0.5 * currentSpeed + 1);
		}
		else return 0;//I don't want the speed deacceling if it's too far.
	}
	/**
	 * @return if we're currently transitioning
	 */
	public boolean isTransitioning() {
		return moving;
	}

}
