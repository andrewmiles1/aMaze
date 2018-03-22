package com.burnt_toast.amazement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.burnt_toast.maze_generator.MazeGenerator;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class Maze
{
  int[][] maze;
  MazeGenerator mazeGen;
  Vector2 position;
  TextureRegion wallPixel;
  TextureRegion lightPixel;
  TextureRegion exitPixel;
  private LinkedList<Float> fadeQueue;
  private ArrayList<LinkedList<ArrayList<ArrayList<Float>>>> fadeQueue_new;
  float tileSize;
  float temp;
  
  public Maze()
  {
    this.mazeGen = new MazeGenerator();
    this.position = new Vector2();
    this.wallPixel = new TextureRegion();
    this.fadeQueue = new LinkedList();
    this.fadeQueue.addLast(Float.valueOf(0.0F));
  }
  
  public Maze(TextureRegion passWallTile)
  {
    this.mazeGen = new MazeGenerator();
    this.position = new Vector2();
    this.wallPixel = new TextureRegion(MainFrame.gameTexture, 0, 103, 1, 1);
    this.fadeQueue = new LinkedList();
    this.fadeQueue.addLast(Float.valueOf(0.0F));
  }
  
  public void setPos(float x, float y)
  {
    this.position.x = x;
    this.position.y = y;
  }
  
  public void setPosX(float x)
  {
    this.position.x = x;
  }
  
  public void setPosY(float y)
  {
    this.position.y = y;
  }
  
  public float getX()
  {
    return this.position.x;
  }
  
  public float getY()
  {
    return this.position.y;
  }
  
  public void createMaze(int size, float tileSize)
  {
    this.fadeQueue.clear();
    this.fadeQueue.addLast(Float.valueOf(0.0F));
    this.maze = this.mazeGen.generateMaze(size + 1);
    this.tileSize = tileSize;
    for (int i = 0; i < this.maze.length; i++) {
      for (int k = 0; k < this.maze[0].length; k++) {
        if (this.maze[i][k] == 0) {
          this.maze[i][k] = 2;
        }
      }
    }
    for (int i = 1; i < this.maze.length; i++)
    {
      if (this.maze[i][1] == 1) {
        break;
      }
      this.maze[i][1] = 0;
      if (this.maze[i][2] == 2) {
        this.maze[i][2] = 3;
      }
    }
    for (int i = 1; i < this.maze.length; i++)
    {
      if (this.maze[1][i] == 1) {
        break;
      }
      this.maze[1][i] = 0;
      if (this.maze[2][i] == 2) {
        this.maze[2][i] = 3;
      }
    }
  }
  
  public void activateBlock(int xInd, int yInd)
  {
    if ((this.maze[xInd][yInd] == 1) || ((this.maze[(xInd + 1)][yInd] != 3) && (this.maze[(xInd - 1)][yInd] != 3) && (this.maze[xInd][(yInd - 1)] != 3) && (this.maze[xInd][(yInd + 1)] != 3))) {
      return;
    }
    int tempInd = 1;
    for (int i = 1; i < this.fadeQueue.size(); i++) {
      if (((Float)this.fadeQueue.get(i)).floatValue() <= 0.0F)
      {
        tempInd = i * -1;
        this.fadeQueue.set(i, Float.valueOf(1.0F));
        break;
      }
    }
    if (tempInd == 1)
    {
      this.fadeQueue.addLast(Float.valueOf(1.0F));
      System.out.print("FADEQUEUE: ");
      float fl;
      for (int i = 0; i < fadeQueue.size(); i++) {
        System.out.println(fadeQueue.get(i) + "; ");
      }
      System.out.println();
      tempInd = (this.fadeQueue.size() - 1) * -1;
    }
    for (int i = xInd; i < this.maze.length; i++)
    {
      if (this.maze[i][yInd] == 1) {
        break;
      }
      if (this.maze[i][yInd] > 0) {
        this.maze[i][yInd] = tempInd;
      }
      if (this.maze[i][(yInd + 1)] == 2) {
        this.maze[i][(yInd + 1)] = 3;
      }
      if (this.maze[i][(yInd - 1)] == 2) {
        this.maze[i][(yInd - 1)] = 3;
      }
    }
    for (int i = xInd; i >= 0; i--)
    {
      if (this.maze[i][yInd] == 1) {
        break;
      }
      if (this.maze[i][yInd] > 0) {
        this.maze[i][yInd] = tempInd;
      }
      if (this.maze[i][(yInd + 1)] == 2) {
        this.maze[i][(yInd + 1)] = 3;
      }
      if (this.maze[i][(yInd - 1)] == 2) {
        this.maze[i][(yInd - 1)] = 3;
      }
    }
    for (int i = yInd; i < this.maze.length; i++)
    {
      if (this.maze[xInd][i] == 1) {
        break;
      }
      if (this.maze[xInd][i] > 0) {
        this.maze[xInd][i] = tempInd;
      }
      if (this.maze[(xInd - 1)][i] == 2) {
        this.maze[(xInd - 1)][i] = 3;
      }
      if (this.maze[(xInd + 1)][i] == 2) {
        this.maze[(xInd + 1)][i] = 3;
      }
    }
    for (int i = yInd; i >= 0; i--)
    {
      if (this.maze[xInd][i] == 1) {
        break;
      }
      if (this.maze[xInd][i] > 0) {
        this.maze[xInd][i] = tempInd;
      }
      if (this.maze[(xInd - 1)][i] == 2) {
        this.maze[(xInd - 1)][i] = 3;
      }
      if (this.maze[(xInd + 1)][i] == 2) {
        this.maze[(xInd + 1)][i] = 3;
      }
    }
  }
  
  public boolean checkCollisionAndVis(int x, int y)
  {
    return checkCollisionAndVis(x, y);
  }
  
  public int translateX(float x)
  {
    return (int)((x - this.position.x) / this.tileSize);
  }
  
  public int translateY(float y)
  {
    return (int)((y - this.position.y) / this.tileSize);
  }
  
  public float checkCollisionAndVis(float x, float y, float distance, char direction)
  {
    if ((direction == 'l') || (direction == 'r'))
    {
      distance += x;
      this.temp = distance;
      distance = translateX(distance);
    }
    else
    {
      distance += y;
      this.temp = distance;
      distance = translateY(distance);
    }
    x = translateX(x);
    y = translateY(y);
    if (direction == 'r') {
      for (int i = (int)x; i <= distance; i++)
      {
        activateBlock(i, (int)y);
        if (this.maze[i][((int)y)] == 1) {
          return i * this.tileSize + this.position.x;
        }
      }
    } else if (direction == 'l') {
      for (int i = (int)x; i >= distance; i--)
      {
        activateBlock(i, (int)y);
        if (this.maze[i][((int)y)] == 1) {
          return i * this.tileSize + this.position.x + this.tileSize;
        }
      }
    } else if (direction == 'u') {
      for (int i = (int)y; i <= distance; i++)
      {
        activateBlock((int)x, i);
        if (this.maze[((int)x)][i] == 1) {
          return i * this.tileSize + this.position.y;
        }
      }
    } else if (direction == 'd') {
      for (int i = (int)y; i >= distance; i--)
      {
        activateBlock((int)x, i);
        if (this.maze[((int)x)][i] == 1) {
          return i * this.tileSize + this.position.y + this.tileSize;
        }
      }
    }
    return this.temp;
  }
  
  public boolean checkIfWallAt(float x, float y)
  {
    try
    {
      x = Math.abs(translateX(x));
      y = Math.abs(translateY(y));
      if (this.maze[((int)x)][((int)y)] == 1) {
        return true;
      }
      return false;
    }
    catch (Exception e)
    {
      System.out.println("Uh oh.");
    }
    return false;
  }
  
  public int getSize()
  {
    return this.maze.length;
  }
  
  public float getTotalSize()
  {
    return this.tileSize * this.maze.length;
  }
  
  public float getStartingPosX()
  {
    return this.position.x + this.tileSize;
  }
  
  public float getStartingPosY()
  {
    return this.position.y + this.tileSize;
  }
  
  public float getFadeAmt(float current)
  {
    if (current < 0.001D) {
      return 0.0F;
    }
    if (current < 0.2D) {
      return (float)(current - current * 5.0D * Gdx.graphics.getDeltaTime());
    }
    return current - 6.0F * Gdx.graphics.getDeltaTime();
  }
  
  public void draw(SpriteBatch batch)
  {
    for (int i = 0; i < this.fadeQueue.size(); i++)
    {
      if (((Float)this.fadeQueue.get(i)).floatValue() > 0.0F) {
        this.fadeQueue.set(i, Float.valueOf(getFadeAmt(((Float)this.fadeQueue.get(i)).floatValue())));
      }
      if (((Float)this.fadeQueue.get(i)).floatValue() < 0.0F) {
        this.fadeQueue.set(i, Float.valueOf(0.0F));
      }
    }
    MainFrame.drawingWalls();
    for (int i = 0; i < this.maze.length; i++) {
      for (int k = 0; k < this.maze[0].length; k++)
      {
        MainFrame.drawingWalls();
        if ((this.maze[i][k] == 1) || (this.maze[i][k] == 2))
        {
          MainFrame.batch.draw(this.wallPixel, this.position.x + this.tileSize * i, this.position.y + this.tileSize * k, this.tileSize, this.tileSize);
        }
        else if (this.maze[i][k] < 0)
        {
          if (((Float)this.fadeQueue.get(this.maze[i][k] * -1)).floatValue() <= 0.0F) {
            this.maze[i][k] = 0;
          }
          MainFrame.batch.setColor(batch.getColor().r, batch.getColor().g, 
            batch.getColor().b, ((Float)this.fadeQueue.get(this.maze[i][k] * -1)).floatValue());
          MainFrame.batch.draw(this.wallPixel, this.position.x + this.tileSize * i, this.position.y + this.tileSize * k, this.tileSize, this.tileSize);
          
          MainFrame.batch.setColor(batch.getColor().r, batch.getColor().g, 
            batch.getColor().b, 1.0F);
        }
        else if (this.maze[i][k] == 3)
        {
          MainFrame.drawingText();
          MainFrame.batch.draw(this.wallPixel, this.position.x + this.tileSize * i, this.position.y + this.tileSize * k, this.tileSize, this.tileSize);
        }
        MainFrame.drawingLogo();
        MainFrame.batch.draw(this.wallPixel, this.position.x + (this.maze.length - 2) * this.tileSize, this.position.y + (this.maze.length - 2) * this.tileSize, this.tileSize, this.tileSize);
      }
    }
  }
  
  public int[][] getIntArray()
  {
    return this.maze;
  }
}