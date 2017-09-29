package com.burnt_toast.amazement;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class MainFrame extends Game {
	public static SpriteBatch batch;//le batch yo
	
	public static AssetManager assets = new AssetManager();//Asset Manager
	
	public static final int SCREEN_WIDTH = 0;//16:10 ratio
	public static final int SCREEN_HEIGHT = 0;
	
	//SCREENS
	MenuScreen menuScr;
	PlayScreen playScr;
	
	//Font and tools
	public BitmapFont gameFont;
	public GlyphLayout glyphLayout;
	public FreeTypeFontGenerator generator;
	public FreeTypeFontParameter parameter;//used for loading fonts
	
	//Textures
	public Texture gameTexture;
	
	public MainFrame(){
		
		//load font stuff
		generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans-Light.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 12;//FIXME will probably need bigger font size
		gameFont = generator.generateFont(parameter);//generate font manually
		
		//load textures
		gameTexture = MainFrame.assets.get("textures/mainTileset.png", Texture.class);
		
		//load sounds
		
		menuScr = new MenuScreen(this);
		playScr = new PlayScreen(this);
	}
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
