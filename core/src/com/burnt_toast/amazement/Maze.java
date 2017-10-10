package com.burnt_toast.amazement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.burnt_toast.maze_generator.MazeGenerator;

public class Maze {
	int[][] currentMaze;
	MazeGenerator mazeGen;
	Vector2 position;
	
	public Maze() {
		mazeGen = new MazeGenerator();
		position = new Vector2();
	}
	
	public void createMaze(int size) {
		currentMaze = mazeGen.generateMaze(size);
		//position.x = singlePlayRefPoint.x+Gdx.graphics.getWidth()/2-currentLvTileSize*currentMaze.length/2;
		//position.y = singlePlayRefPoint.y+Gdx.graphics.getHeight()-Gdx.graphics.getWidth()/2-currentLvTileSize*currentMaze.length/2;
	}
}
