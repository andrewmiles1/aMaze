package com.burnt_toast.amazement;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class MainFrame extends Game {
	public static SpriteBatch batch;//le batch yo
	
	public static AssetManager assets = new AssetManager();//Asset Manager
	
	//getting screen width and height will be grabbed through Gdx.graphics.getWidth() or getHeight()
	
	
	//Color consts
	public static Color playerColor;
	public static Color textColor;
	public static Color logoColor;
	public static Color wallColor;
	//background color never changes.
	public static Color backgroundColor;
	//HSVtoRGB variables
	private static Color tempColor = new Color();
	static int r, g, b;
	static int i;
	static float f, p, q, t;
	
	private static float hue;
	
	//SCREENS
	MenuScreen menuScr;
	PlayScreen playScr;
	
	//Font and tools
	public static BitmapFont gameFont;
	public GlyphLayout glyphLayout;
	public FreeTypeFontGenerator generator;
	public FreeTypeFontParameter parameter;//used for loading fonts
	
	//Textures
	public Texture gameTexture;
	
	public MainFrame(){
		//load colors
		playerColor = new Color();
		textColor = new Color();
		logoColor = new Color();
		wallColor = new Color();
		tempColor = new Color();
		backgroundColor = new Color();
		HSVtoRGB(backgroundColor, 0, 0, 13);
	}
	
	
	@Override
	public void create() {
		//loading everything here because there's not a lot to load.
		

		
		//load font stuff
		generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans-Light.ttf"));
		parameter = new FreeTypeFontParameter();
		parameter.size = 50;//FIXME will probably need bigger font size
		gameFont = generator.generateFont(parameter);//generate font manually
		
		gameTexture = new Texture(Gdx.files.internal("MainTexture.png"));
		
		//load textures
		//gameTexture = MainFrame.assets.get("textures/mainTexture.png", Texture.class);
		
		//load sounds
		
		//loads screens
		menuScr = new MenuScreen(this);
		playScr = new PlayScreen(this);
		this.setScreen(menuScr);
		
		batch = new SpriteBatch();
		
	}
	/** Converts HSV color sytem to RGB
	 * 
	 * no return actually, assigns to passed color
	 */
	public static void HSVtoRGB (Color assignColor, float h, float s, float v) {

		h = (float)Math.max(0.0, Math.min(360.0, h));
		s = (float)Math.max(0.0, Math.min(100.0, s));
		v = (float)Math.max(0.0, Math.min(100.0, v));
		s /= 100;
		v /= 100;

		h /= 60;
		i = (int)Math.floor(h);
		f = h - i;
		p = v * (1 - s);
		q = v * (1 - s * f);
		t = v * (1 - s * (1 - f));
		switch (i) {
		case 0:
			r = Math.round(255 * v);
			g = Math.round(255 * t);
			b = Math.round(255 * p);
			break;
		case 1:
			r = Math.round(255 * q);
			g = Math.round(255 * v);
			b = Math.round(255 * p);
			break;
		case 2:
			r = Math.round(255 * p);
			g = Math.round(255 * v);
			b = Math.round(255 * t);
			break;
		case 3:
			r = Math.round(255 * p);
			g = Math.round(255 * q);
			b = Math.round(255 * v);
			break;
		case 4:
			r = Math.round(255 * t);
			g = Math.round(255 * p);
			b = Math.round(255 * v);
			break;
		default:
			r = Math.round(255 * v);
			g = Math.round(255 * p);
			b = Math.round(255 * q);
		}
		assignColor.r = r/255.0f;
		assignColor.g = g/255.0f;
		assignColor.b = b/255.0f;
		assignColor.a = 1;
		//return new Color(r / 255.0f, g / 255.0f, b / 255.0f, 1);
	}
	public static void changeColor() {
		hue = (float)(Math.random() * 360);
		HSVtoRGB(textColor, hue, 42, 98);
		HSVtoRGB(textColor, hue, 52, 97);
		HSVtoRGB(logoColor, hue, 57, 87);
		HSVtoRGB(wallColor, hue, 50, 57);
		outputColor(textColor);
	}
	public static void drawingPlayer() {
		batch.setColor(playerColor);
	}
	public static void drawingWalls() {
		batch.setColor(wallColor);
	}
	public static void drawingText() {

		gameFont.setColor(textColor);
		batch.setColor(textColor);
	}
	public static void drawingLogo() {
		batch.setColor(logoColor);
	}
	private static void outputColor(Color col) {
		System.out.println("Color values.");
		System.out.println("R:" + col.r);
		System.out.println("G:" + col.g);
		System.out.println("B:" + col.b);
		System.out.println("A:" + col.a);
	}
	/**
	 * copies color TWO to color ONE. 2 > 1
	 * @param col1 Color 1
	 * @param col2 Color 2
	 */
	private static void copyFromColor(Color col1, Color col2) {
		col1.a = col2.a;
		col1.b = col2.b;
		col1.g = col2.g;
		col1.r = col2.r;
	}
	
	
}
