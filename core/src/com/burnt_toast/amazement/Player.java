package com.burnt_toast.amazement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player
{
  private TextureRegion imageSmall;
  private Vector2 position;
  private float tileSize;
  private Maze maze;
  private float tempFl;
  private float distance;
  private int tempInt;
  private Vector2 tempPos;
  
  public Player(Maze passMaze)
  {
    this.position = new Vector2();
    
    this.imageSmall = new TextureRegion(MainFrame.gameTexture, 0, 103, 1, 1);
    this.maze = passMaze;
    this.tempPos = new Vector2();
  }
  
  public void setPos(float x, float y)
  {
    this.position.x = x;
    this.position.y = y;
  }
  
  public Vector2 getPos()
  {
    return this.position;
  }
  
  public void setSize(float passTileSize)
  {
    this.tileSize = passTileSize;
  }
  
  public void draw()
  {
    MainFrame.batch.draw(this.imageSmall, this.position.x, this.position.y, this.tileSize, this.tileSize);
  }
  
  public float round(float num, float factor, int direction)
  {
    this.tempFl = factor;
    while (this.tempFl < num) {
      this.tempFl += factor;
    }
    if (direction == 0) {
      if (this.tempFl - factor < factor / 2.0F) {
        direction = 1;
      } else {
        direction = -1;
      }
    }
    if (direction > 0) {
      return this.tempFl;
    }
    return this.tempFl - factor;
  }
  
  public boolean isOnTrackVert()
  {
    if ((this.position.x - this.maze.position.x) % this.maze.tileSize == 0.0F) {
      return true;
    }
    return false;
  }
  
  public boolean isOnTrackHoriz()
  {
    if ((this.position.y - this.maze.position.y) % this.maze.tileSize == 0.0F) {
      return true;
    }
    return false;
  }
  
  public void drag()
  {
    this.tempPos.x = (this.position.x - this.maze.position.x);
    this.tempPos.y = (this.position.y - this.maze.position.y);
    if (Math.abs(Gdx.input.getDeltaX()) > Math.abs(Gdx.input.getDeltaY()))
    {
      this.distance = (Gdx.input.getDeltaX() * (this.tileSize / 16.0F * 0.666F) / 2.0F);
      if (this.distance > 0.0F) {
        if (isOnTrackHoriz())
        {
          float checkClick = this.position.x;
          this.position.x = (this.maze.checkCollisionAndVis(this.position.x + this.tileSize, this.position.y, this.distance, 'r') - this.maze.tileSize);
        }
        else if (!this.maze.checkIfWallAt(this.position.x + this.tileSize, this.position.y + this.tileSize))
        {
          if (this.position.y + this.distance > this.maze.position.y + round(this.position.y - this.maze.position.y, this.tileSize, 1)) {
            this.position.y = (this.maze.position.y + round(this.position.y - this.maze.position.y, this.tileSize, 1));
          } else {
            this.position.y += this.distance;
          }
        }
        else if (!this.maze.checkIfWallAt(this.position.x + this.tileSize, this.position.y))
        {
          if (this.position.y - this.distance < this.maze.position.y + round(this.position.y - this.maze.position.y, this.tileSize, -1)) {
            this.position.y = (this.maze.position.y + round(this.position.y - this.maze.position.y, this.tileSize, -1));
          } else {
            this.position.y -= this.distance;
          }
        }
      }
      if (this.distance < 0.0F) {
        if (isOnTrackHoriz()) {
          this.position.x = this.maze.checkCollisionAndVis(this.position.x, this.position.y, this.distance, 'l');
        } else if (!this.maze.checkIfWallAt(this.position.x - this.tileSize, this.position.y + this.tileSize))
        {
          if (this.position.y - this.distance > this.maze.position.y + round(this.tempPos.y, this.tileSize, 1)) {
            this.position.y = (this.maze.position.y + round(this.tempPos.y, this.tileSize, 1));
          } else {
            this.position.y -= this.distance;
          }
        }
        else if (!this.maze.checkIfWallAt(this.position.x - this.tileSize, this.position.y)) {
          if (this.position.y + this.distance < this.maze.position.y + round(this.tempPos.y, this.tileSize, -1)) {
            this.position.y = (this.maze.position.y + round(this.tempPos.y, this.tileSize, -1));
          } else {
            this.position.y += this.distance;
          }
        }
      }
    }
    else
    {
      this.distance = (Gdx.input.getDeltaY() * (this.tileSize / 16.0F * 0.666F) / 2.0F);
      this.distance *= -1.0F;
      if (this.distance > 0.0F)
      {
        if (isOnTrackVert()) {
          this.position.y = (this.maze.checkCollisionAndVis(this.position.x, this.position.y + this.maze.tileSize, this.distance, 'u') - this.maze.tileSize);
        } else if (!this.maze.checkIfWallAt(this.position.x, this.position.y + this.tileSize))
        {
          if (this.position.x - this.distance < this.maze.position.x + round(this.tempPos.x, this.tileSize, -1)) {
            this.position.x = (this.maze.position.x + round(this.tempPos.x, this.tileSize, -1));
          } else {
            this.position.x -= this.distance;
          }
        }
        else if (!this.maze.checkIfWallAt(this.position.x + this.tileSize, this.position.y + this.tileSize)) {
          if (this.position.x + this.distance > this.maze.position.x + round(this.tempPos.x, this.tileSize, 1)) {
            this.position.x = (this.maze.position.x + round(this.tempPos.x, this.tileSize, 1));
          } else {
            this.position.x += this.distance;
          }
        }
      }
      else if (isOnTrackVert()) {
        this.position.y = this.maze.checkCollisionAndVis(this.position.x, this.position.y, this.distance, 'd');
      } else if (!this.maze.checkIfWallAt(this.position.x, this.position.y - this.tileSize))
      {
        if (this.position.x + this.distance < this.maze.position.x + round(this.tempPos.x, this.tileSize, -1)) {
          this.position.x = (this.maze.position.x + round(this.tempPos.x, this.tileSize, -1));
        } else {
          this.position.x += this.distance;
        }
      }
      else if (!this.maze.checkIfWallAt(this.position.x + this.tileSize, this.position.y - this.tileSize)) {
        if (this.position.x - this.distance > this.maze.position.x + round(this.tempPos.x, this.tileSize, 1)) {
          this.position.x = (this.maze.position.x + round(this.tempPos.x, this.tileSize, 1));
        } else {
          this.position.x -= this.distance;
        }
      }
    }
  }
  
  public int checkCollision(char direction, float distance)
  {
    if (direction == 'r') {}
    return 0;
  }
}