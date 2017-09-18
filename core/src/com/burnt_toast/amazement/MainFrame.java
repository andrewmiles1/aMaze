package com.burnt_toast.amazement;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class MainFrame extends Game {
	SpriteBatch batch;//le batch yo
	
	//SCREENS
	SplashScreen splashScr;
	MenuScreen menuScr;
	PlayScreen playScr;
	
	//Font and tools
	public BitmapFont gameFont;
	public GlyphLayout glyphLayout;
	public FreeTypeFontGenerator generator;
	public FreeTypeFontParameter parameter;//used for loading fonts
	
	public MainFrame(){
		splashScr = new SplashScreen(this);
		menuScr = new MenuScreen(this);
		playScr = new PlayScreen(this);
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}
	
	
}
