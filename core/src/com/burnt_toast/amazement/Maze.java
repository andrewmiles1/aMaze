package com.burnt_toast.amazement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.burnt_toast.maze_generator.MazeGenerator;

public class Maze {
	int[][] maze;//1 is wall 2 is non visible 0 is visible
	MazeGenerator mazeGen;
	Vector2 position;
	TextureRegion tile;
	float tileSize;
	float temp;
	
	public Maze() {
		mazeGen = new MazeGenerator();
		position = new Vector2();
		tile = new TextureRegion();
	}
	
	public Maze(TextureRegion passTile) {
		mazeGen = new MazeGenerator();
		position = new Vector2();
		tile = passTile;
	}
	
	public void setPos(float x, float y) {
		position.x = x;
		position.y = y;
	}
	public void setPosX(float x) {
		position.x = x;
	}
	public void setPosY(float y) {
		position.y = y;
	}
	public float getX() { return position.x; }
	public float getY() { return position.y; }
	
	public void createMaze(int size, float tileSize) {
		maze = mazeGen.generateMaze(size+1);
		this.tileSize = tileSize;
		
		//make everything invisible
		for(int i = 0; i < maze.length; i++) {
			for(int k = 0; k < maze[0].length; k++) {
				if(maze[i][k] == 0) {
					maze[i][k] = 2;
				}
			}
		}//end double for
		//set the visibility here
	}//end create maze
	
	public void activateBlock(int xInd, int yInd) {
		
	}
	public boolean checkCollisionAndVis(int x, int y) {
		return checkCollisionAndVis((int)x, (int)y);
	}
	
	public int translateX(float x) {
		return (int)((x - position.x)/tileSize);
	}
	public int translateY(float y) {
		return (int)((y - position.y)/tileSize);
	}
	public float checkCollisionAndVis(float x, float y, float distance, char direction) {

		if(direction == 'l' || direction == 'r') {//get distance.
			//works for left too, because it'll be negative.
			//places the x to the position x in the maze and
			//then see's what tile we'll be moving to.
			distance += (x);
			temp = distance;
			distance = translateX(distance);
		}
		else {
			distance += (y);
			temp = distance;
			distance = translateY(distance);
		}
		x = translateX(x);
		y = translateY(y);
		if(direction == 'r') {//if going right
			for(int i = (int) x; i <= distance; i++) {
				//loop through boxes that it would go through. 
				if(maze[i][(int) y] == 1) {
					return (i * tileSize + position.x);
				}
			}
		}
		else if(direction == 'l'){//direction is left
			for(int i = (int) x; i >= distance; i--) {//goes in opposite direction.
				//loop through boxes that it would go through. 
				if(maze[i][(int) y] == 1) {
					return (i * tileSize + position.x + tileSize);
				}
			}
		}
		else if(direction == 'u') {
			for(int i = (int)y; i <= distance; i++) {
				if(maze[(int)x][i] == 1) {
					return (i * tileSize + position.y);
				}
			}
		}//end up
		else if(direction == 'd') {
			for(int i = (int)y; i >= distance; i--) {
				if(maze[(int)x][i] == 1) {
					return (i * tileSize + position.y+tileSize);
				}
			}
		}//end down
		return temp;//nothing caught, return the "didn't collide" response.
	}
	public boolean checkIfWallAt(float x, float y){
		try {
			x = Math.abs(translateX(x));
			y = Math.abs(translateY(y));
			if(maze[(int)x][(int)y] == 1) {
				return true;//if there's a wall there, then true
			}
			else return false;//if not, then false
		}
		catch(Exception e) {
			System.out.println("Uh oh.");
		}
		return false;
	}
	/**
	 * checks the collision of x and y, and also changes visibility for pos
	 * @param x of player
	 * @param y of player
	 * @return true if collided, false if not.
	 */
	public boolean checkCollisionAndVis(float x, float y, char direction) {
		//places x and y relative to 0,0 from maze pos
		x -= position.x;
		y -= position.y;
		//check to see if it's right at the walls edge
		//ok so these are if the thing lands RIGHT ON the next wall.
		//for going right or going up only.
		if(direction == 'r' && x % tileSize == 0) {
			x -= 0.1;
			System.out.println("Yeah");
		}
		if(direction == 'u' && y % tileSize == 0) {
			y -= 0.1;
		}
		//so then I can set to what maze index they're in
		//try rounding x and y up before dividing. I'm still getting hashing issues.
		x = (x / ((int)tileSize));
		y = (y / ((int)tileSize));
		//bam basically just hashed map that baby.
		//so now we check the array representation
		
		temp = maze[(int)x][(int)y];
		System.out.println(x + ", " + y);
		if(temp == 1) {
			System.out.println("Collision?");
			return true;//collided.
		}
		else {
			//didn't collide, activate that block if invisible neighbors
			if(maze[(int)x+1][(int)y] == 2 ||//right
					maze[(int)x-1][(int)y] == 2 ||//left
					maze[(int)x][(int)y+1] == 2 ||//up
					maze[(int)x][(int)y-1] == 2) {//down
				//this check shouldn't ever go outside of bounds,
				//we're never checking from the walls. If that makes sense.
				activateBlock((int)x, (int)y);
				//it has invisible neighbors, so activate the block.

			}
			return false;
		}
	}//end check vis
	
	/**
	 * @return returns only the tile size of the maze
	 */
	public int getSize() {
		return maze.length;
	}
	/**
	 * returns total graphical size of maze
	 * @return
	 */
	public float getTotalSize() {
		return tileSize * maze.length;
	}
	
	public float getStartingPosX() {
		return position.x + tileSize;
	}
	public float getStartingPosY() {
		return position.y + tileSize;
	}
	
	public void draw(SpriteBatch batch) {
		MainFrame.drawingWalls();
		for(int i = 0; i < maze.length; i++) {//row
			for(int k = 0; k < maze[0].length; k++) {//column
				if((maze[i][k] == 1)) {
					MainFrame.batch.draw(tile,// singlePlayRefPoint.x + i * currentLvTileSize, singlePlayRefPoint.y + k * currentLvTileSize,
					position.x + tileSize*i,//mmmm ain't that nice.
					position.y + tileSize*k,//these two lines used to look super long and ugly. See where mazePos is set in touchDown
					tileSize, tileSize);
				}
			}
		}//end double for
	}
	public int[][] getIntArray() {
		return maze;
	}


}
