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
	private int temp;
	
	public Player(Maze passMaze) {
		position = new Vector2();
		//These two images are scalable to whatever size of tile.
		imageBig = new TextureRegion(MainFrame.gameTexture, 0, 53, 50, 50);
		imageSmall = new TextureRegion(imageBig, 10, 10, 1, 1);
		this.maze = passMaze;
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
	public void drag() {
		if(Math.abs(Gdx.input.getDeltaX()) > Math.abs(Gdx.input.getDeltaY())) {
			//going x direction
			this.position.x += Gdx.input.getDeltaX();
		}
		else {
			//going y direction.
			this.position.y -= Gdx.input.getDeltaY();
		}
	}
	/**
	 * checks the collision from the passed direction
	 * @return 0 = no collision, 1 = total collision, 
	 * 2 = partial collision in the opposite axis POSITIVE direction
	 * 3 = partial collision in the opposite axis NEGATIVE direction
	 */
	private int checkCollision(char direction, float distance) {
		//1 2
		//4 8 adding values for each corner
		temp = 0;//each corner added to see what hit.
		if(direction == 'u' || direction == 'l') {//upper left corner
			if(!maze.checkCollisionAndVis(position.x, position.y+tileSize+distance)) {
				temp += 1;//top left corner value, cause DIDN'T collide
			}
			//upper left corner
		}
		//FIXME check the other 3 corners.
		switch(temp) {
		//parshal collision values
		case 1:
			//only top left corner didn't collide
			if(direction == 'u')return 3;
			if(direction == 'l')return 2;
			break;
		//FIXME check the other 3 corners
		//totally didn't collide values
		case 3:case 6:case 12:case 9:
			return 0;//didn't collide.
		}
		return 1;//if we got to this point, and nothing else returned, then return.
	}

}
