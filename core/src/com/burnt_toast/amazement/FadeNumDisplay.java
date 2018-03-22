package com.burnt_toast.amazement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import java.io.PrintStream;

public class FadeNumDisplay
{
  private int currentNum;
  private int minNum;
  private int maxNum;
  private float currentTransp;
  private static float transPlaceholder;
  private String displayString;
  private String referenceDispStr;
  private float dragSpeed;
  private float dragDeaccel;
  Sound soundEff;
  
  public FadeNumDisplay(String passDisplayString, int passMinNum, int passMaxNum, Sound passSound)
  {
    this.soundEff = passSound;
    if (passMinNum % 2 == 0)
    {
      System.out.println("MIN NUM TO DISP WAS NOT ODD." + passMinNum);
      
      passMinNum++;
    }
    if (passMaxNum % 2 == 0)
    {
      System.out.println("MAX NUM TO DISP WAS NOT ODD. -1" + passMaxNum);
      
      passMaxNum--;
    }
    this.minNum = passMinNum;
    this.maxNum = passMaxNum;
    
    this.currentNum = this.minNum;
    this.currentTransp = 1.0F;
    this.referenceDispStr = passDisplayString;
    this.displayString = passDisplayString.replaceAll("~", Integer.toString(this.currentNum));
    this.dragDeaccel = 15.0F;
  }
  
  public void setNumber(int num)
  {
    this.currentNum = num;
    this.displayString = this.referenceDispStr.replaceAll("~", Integer.toString(this.currentNum));
  }
  
  public int getNumber()
  {
    return this.currentNum;
  }
  
  public String getStr()
  {
    return this.displayString;
  }
  
  public void setDisplayStr(String passDisplayStyle)
  {
    this.displayString = passDisplayStyle.replaceAll("~", Integer.toString(this.currentNum));
  }
  
  public int getMinNum()
  {
    return this.minNum;
  }
  
  public void setMinNum(int minNum)
  {
    this.minNum = minNum;
  }
  
  public int getMaxNum()
  {
    return this.maxNum;
  }
  
  public void setMaxNum(int maxNum)
  {
    this.maxNum = maxNum;
  }
  
  public void stop()
  {
    this.dragSpeed = 0.0F;
  }
  
  public void draw(float x, float y)
  {
    MainFrame.gameFont.draw(MainFrame.batch, this.displayString, x, y);
  }
  
  public void drawCenteredX(float screenWidth, float y)
  {
    draw(screenWidth / 2.0F - FontTools.getWidthOf(this.displayString) / 2.0F, y);
  }
  
  public void playerDragged(float amountX)
  {
    if ((this.currentNum == this.maxNum) && (amountX > 0.0F) && (transPlaceholder >= 0.0F)) {
      transPlaceholder = 0.0F;
    } else if ((this.currentNum == this.minNum) && (transPlaceholder <= 0.0F) && (amountX < 0.0F)) {
      transPlaceholder = 0.0F;
    } else {
      transPlaceholder += amountX / 40.0F;
    }
    if (transPlaceholder > 1.0F)
    {
      transPlaceholder = -1.0F;
      
      this.currentNum = (this.currentNum == this.maxNum ? this.maxNum : this.currentNum + 2);
      
      this.displayString = this.referenceDispStr.replaceAll("~", Integer.toString(this.currentNum));
      this.soundEff.play(0.3F);
    }
    else if (transPlaceholder < -1.0F)
    {
      transPlaceholder = 1.0F;
      this.currentNum = (this.currentNum == this.minNum ? this.minNum : this.currentNum - 2);
      
      this.displayString = this.referenceDispStr.replaceAll("~", Integer.toString(this.currentNum));
      this.soundEff.play(0.3F);
    }
    this.dragSpeed = (amountX / Gdx.graphics.getDeltaTime());
    if (this.dragSpeed > 60000.0F) {
      this.dragSpeed = 70000.0F;
    }
  }
  
  public void update()
  {
    if (Gdx.input.isTouched())
    {
      if ((Gdx.input.getDeltaX() < 1) && (Gdx.input.getDeltaX() > -1)) {
        this.dragSpeed = 0.0F;
      }
    }
    else if (this.dragSpeed > 0.0F)
    {
      playerDragged(this.dragSpeed * Gdx.graphics.getDeltaTime());
      this.dragSpeed -= getDeaccel();
      if (this.dragSpeed < 0.0F) {
        this.dragSpeed = 0.0F;
      }
    }
    else if (this.dragSpeed < 0.0F)
    {
      playerDragged(this.dragSpeed * Gdx.graphics.getDeltaTime());
      this.dragSpeed -= getDeaccel();
      if (this.dragSpeed > 0.0F) {
        this.dragSpeed = 0.0F;
      }
    }
  }
  
  private float getDeaccel()
  {
    if (this.dragSpeed > 6000.0F) {
      return 3000.0F;
    }
    if (this.dragSpeed < -6000.0F) {
      return -3000.0F;
    }
    return (float)(Math.abs(0.005D) * this.dragSpeed) + (this.dragSpeed > 0.0F ? 20 : -20);
  }
}