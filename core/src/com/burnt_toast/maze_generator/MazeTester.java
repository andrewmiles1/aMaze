package com.burnt_toast.maze_generator;

import java.util.LinkedList;

public class MazeTester {

	public static void main(String[] args){
		MazeGenerator mazeGen = new MazeGenerator();
		int[][] testMaze = mazeGen.generateMaze(6, true);
		//FADEQUEUE: 0.0;1.4375706E-17;2.1500166E-16;2.3843869E-14;6.3961277E-13;1.0;
		mazeGen.generateMaze(10);
	}
	public void getNum(Integer passInt){
		//
	}
	public void getNum(int passInt){
		
	}
}
