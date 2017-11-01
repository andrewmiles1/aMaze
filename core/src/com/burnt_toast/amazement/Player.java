package com.burnt_toast.amazement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player {
	private TextureRegion imageBig;
	private TextureRegion imageSmall;
	private Vector2 position;//bottom left of player is pos
	private float tileSize;
	private Maze maze;
	private float tempFl;
	private int tempInt;
	//this is for testing collision at new location.
	private Vector2 tempPos;
	
	public Player(Maze passMaze) {
		position = new Vector2();
		//These two images are scalable to whatever size of tile.
		imageBig = new TextureRegion(MainFrame.gameTexture, 0, 53, 50, 50);
		imageSmall = new TextureRegion(imageBig, 10, 10, 1, 1);
		this.maze = passMaze;
		tempPos = new Vector2();
	}
	
	public void setPos(float x, float y) {
		position.x = x;
		position.y = y;
	}
	public Vector2 getPos() {
		return position;
	}
	public void setSize(float passTileSize) {
		tileSize = passTileSize;
	}
	public void draw() {
		if(imageBig.getRegionWidth() < tileSize) {
			MainFrame.batch.draw(imageBig, position.x, position.y, tileSize, tileSize);
		}
		else {
			MainFrame.batch.draw(imageSmall, position.x, position.y, tileSize, tileSize);
		}
	}
	/**
	 * THIS ONLY WORKS FOR POSITIVE NUMBERS
	 * @param num
	 * @param factor
	 * @return
	 */
	public float round(float num, float factor, int direction) {
		tempFl = factor;
		//this gets the tempFl to the least greatest multiple of the factor
		//thats greater than it
		while(tempFl < num)tempFl+= factor;
		if(direction > 0) {
			return tempFl;
		}
		else {
			return (tempFl - factor);
		}
		/*
		if(tempFl-num < num-(tempFl-factor)) {
			return tempFl;
		}
		else {
			return (tempFl-factor);
		}
		*/
	}
	
	public void drag() {
		/*
		 *    Here is the dedicated spot for explaining the math I did to check if I 
		 * passed the mark. 
		 *    So, I first get the player position relative to the maze, using the
		 * rounding function. for this example, let's say we were going in the left direction,
		 * but because of a partial collision, we're now going down to match with the 
		 * corridor we want to go down. Every frame, this method will check if the player y
		 * position is too low to fit in that corridor. 
		 *    if((position.y - maze.position.y) < round(position.y - maze.position.y, tileSize)) {
		 *    or in english
		 *    if((relative player position to maze) < (the rounded position to find where that corridor is))
		 * Hopefully that makes sense.
		 */
		
		if(Math.abs(Gdx.input.getDeltaX()) > Math.abs(Gdx.input.getDeltaY())) {
			//going x direction
			tempFl = Gdx.input.getDeltaX();//set the speed to move
			if(tempFl > 0) {//if going right
				//going right
				switch(checkCollision('r', tempFl)) {
				case 0:
					//no collision so just move
					position.x += tempFl;
					break;
				case 2://move positive on opposite axis
					position.y += tempFl;
					
					//THE BELOW CODE DOESN'T WORK. Because it's using the bottom left position for the player.
					//check if we passed the mark
					if((position.y+tileSize - maze.position.y) > round(position.y+tileSize - maze.position.y, tileSize, 1)) {
						//if the position relative to maze.y is bigger than our target position to get to,
						//if we've passed the mark
						position.y = maze.position.y + round(position.y+tileSize - maze.position.y, tileSize, 1) - tileSize;
						//then set it there. Holy crap I'm way too lazy to explain that logic but yeah.
						//hopefully that's not code that's too hard to read.
					}
					//THE ABOVE CODE DOESN'T WORK
					break;
				case 3://move negative on opposite axis
					//position.y -= tempFl;
					break;
				//NOTICE there's not a case 1: becasue don't do anything
					//if that's the case.
				}//end switch

			}//end if going right
			
			if(tempFl < 0) {//if going left
				switch(checkCollision('l', tempFl)) {
				case 0:
					//no collision so just move.
					position.x += tempFl;//+= works because negative.
					break;
				case 2://move positive on opposite axis
					position.y -= tempFl;//-= because negative
					break;
				case 3://move negative on opposite axis
					position.y += tempFl;//+= because tempFl is negative.
					break;
				}//end switch
			}//end if going left
		}//end if x axis moving
		else {
			//going y direction.
			this.position.y -= Gdx.input.getDeltaY();
		}
	}
	/**
	 * checks the collision from the passed direction
	 * @return 0 = no collision, 1 = total collision, 
	 * 2 = partial collision to move in the opposite axis POSITIVE direction
	 * 3 = partial collision to move in the opposite axis NEGATIVE direction
	 */
	public int checkCollision(char direction, float distance) {
		//1 2
		//4 8 adding values for each corner
		tempInt = 0;//each corner added to see what hit.
		//set the tempInt position to where it would move.
		tempPos.x = position.x; tempPos.y = position.y;
		if(direction == 'u')tempPos.y += distance;
		else if(direction == 'r')tempPos.x += distance;
		else if(direction == 'd')tempPos.y -= distance;
		else if(direction == 'l')tempPos.x -= distance;
		
		//check the collision corners for that direction:
		//tile size in any calculations is to get to the corners
		//it's the substitute for the player size.
		if(direction == 'u' || direction == 'l') {//upper left corner
			if(maze.checkCollisionAndVis(tempPos.x, tempPos.y+tileSize)) {
				tempInt += 1;//top left corner value, cause DID collide
				System.out.println("!"+tempInt);
			}
			//upper left corner
		}
		if(direction == 'u' || direction == 'r') {//upper right corner
			if(maze.checkCollisionAndVis(position.x+tileSize, position.y+tileSize)) {
				tempInt += 2;//top right corner value, cause DID collide
				System.out.println("!"+tempInt);
			}
			//upper right corner
		}
		if(direction == 'd' || direction == 'r') {//lower right corner
			if(maze.checkCollisionAndVis(position.x+tileSize, position.y)) {
				tempInt += 8;//lower right corner value, cause DID collide.
				System.out.println("!"+tempInt);
			}
			//lower right corner
		}
		if(direction == 'd' || direction == 'l') {//lower left corner
			if(maze.checkCollisionAndVis(position.x, position.y)) {
				tempInt += 4;//lower left corner value, cause DID collide.
				System.out.println("!"+tempInt);
			}
			//lower left corner.
		}

		switch(tempInt) {
		//parshal collision values

		case 1:
			//only top left corner collided
			if(direction == 'u')return 2;
			if(direction == 'l')return 3;
			break;
		case 2:
			//only top right corner collided
			if(direction == 'u')return 3;
			if(direction == 'r')return 3;
			break;
		case 4:
			//only bottom left corner collided
			if(direction == 'd')return 2;
			if(direction == 'l')return 2;
			break;
		case 8:
			//only bottom right corner collided
			if(direction == 'd')return 3;
			if(direction == 'r')return 2;
			break;
		//totally collided cases:
		case 3: case 10: case 12: case 5:
			//completely collided.
			return tempInt;
		//totally didn't collide.
		case 0:
			//no collision
			return 0;
		}
		assert(false);
		return 1;//if we got to this point, and nothing else returned, then return.
	}

}
