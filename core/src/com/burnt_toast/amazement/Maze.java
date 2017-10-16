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
	int temp;
	
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
	public float getX() { return position.y; }
	public float getY() { return position.x; }
	
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
		}
		//set the visibility here
	}
	public void activateBlock(int xInd, int yInd) {
		
	}
	
	/**
	 * checks the collision of x and y, and also changes visibility for pos
	 * @param x of player
	 * @param y of player
	 * @return true if collided, false if not.
	 */
	public boolean checkCollisionAndVis(float x, float y) {
		//places x and y relative to 0,0 from maze pos
		x -= position.x;
		y -= position.y;
		//so then I can set to what maze index they're in
		x = (int)(x / tileSize);
		y = (int)(y / tileSize);
		//bam basically just hashed map that baby.
		//so now we check the array representation
		temp = maze[(int)x][(int)y];
		if(temp == 1) {
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
	}
	
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