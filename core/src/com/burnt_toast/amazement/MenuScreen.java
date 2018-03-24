package com.burnt_toast.amazement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.burnt_toast.maze_generator.MazeGenerator;

public class MenuScreen implements Screen, InputProcessor{
	
	//TODO LIST
	//unlocking levels
	//saving progress
	//loading bar
	//ads
	//online play(usernames?)
	
	
	
	//camera of the Ortho kind.
	private OrthographicCamera orthoCam;
	
	//stuff on screen
	private TextureRegion logoImg;
	private FadeNumDisplay levelSelectDisplay;
	
	//input stuff here
	private float dragDeadCounter;
	private float dragDeadZone;
	private boolean dragThisTouch;
	private Vector3 touchDownPoint;
	
	
	//single play stuff
	private Maze currentMaze;
	private TextureRegion wallPixel;
	private float normalTileScale;
	private Player player;
	private Rectangle backButton;
	
	//messy which screen I'm drawing system.
	ScreenTransitionTool screenTransTool;
	String temp;//this is to check what transition code is coming from the tool.
	String currentLayouts;//cool idea though I mean come on
	//so to know what to draw, you just see what the string contains.
	//if it's "mainsingle" then it draws both main and single. 
	//BECAUSE IT CONTAINS BOTH. cool eh?
	private final Vector2 mainRefPoint;//main menu - which is the single player menu for now.
	private final Vector2 singlePlayRefPoint;//single playing
	private final Vector2 multiRefPoint;//multi menu
	
	//text for the explanation of how to input
	private String explText1;
	private String explText2;//I realize this probably isn't the way to go about it but that's ok.
	private String lockText;
	
	private Preferences prefs;
	//array to keep track of finished levels:
	private boolean[] progressionArray;
	private char[] progStr;//for pulling out and putting into the preferences (it's the raw save state)
	
	public MenuScreen(MainFrame main){
		logoImg = new TextureRegion(main.gameTexture, 0, 0, 275, 53);
		//I changed the max to 200 because anything bigger was outrageous.
		levelSelectDisplay = new FadeNumDisplay("< ~ x ~ >", 5, 200, MainFrame.clickSound);
		
		dragDeadZone = 10;//how far until the screen triggers as a drag.
		dragThisTouch = false;//are they dragging as they touch, or just tapping
		touchDownPoint = new Vector3();
		
		explText1 = "Drag left and right to choose a size";
		explText2 = "Tap the screen to begin";
		lockText = "You can only play levels you've unlocked.";
		
		//camera stuff
		orthoCam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		orthoCam.position.set(Gdx.graphics.getWidth()/ 2f, Gdx.graphics.getHeight() / 2f, 0);
		orthoCam.update();
		
		//screen trans stuff.
		mainRefPoint = new Vector2(0, 0);
		singlePlayRefPoint = new Vector2(1, 0);//to the right of main
		multiRefPoint = new Vector2(0, Gdx.graphics.getHeight()*-1);//below main
		currentLayouts = "single";
		screenTransTool = new ScreenTransitionTool(orthoCam);
		
		//single player play stuff
		wallPixel = new TextureRegion(MainFrame.gameTexture, 0, 103, 1, 1);
		currentMaze = new Maze(wallPixel);
		normalTileScale = (int)(16f/500f * Gdx.graphics.getWidth());
		player = new Player(currentMaze);
		backButton = new Rectangle();
		//I'm not using the small font tools because I want the boundries to click on it 
		//to be large enough that it will register when you hit it.
		backButton.setWidth(FontTools.getWidthOf("< Save and go back"));
		backButton.setHeight(FontTools.getHeightOf("< Save and go back"));

		prefs = Gdx.app.getPreferences("burnt_toast_amazement");//the file the save stuff is saveed in.
		progressionArray = new boolean[99];//the max levels is 200 so yeah.
		progStr = new char[99];
		if(prefs.contains("Progression String")) {//read save state from preferences:
			progStr = prefs.getString("Progression String").toCharArray();
			for(int i = 0; i < 99; i++) {
				if(progStr[i] == '0') {
					progressionArray[i] = false;
				}
				else {
					progressionArray[i] = true;
				}
			}
		}

	}
	
	public int getBlockSizeForLv(int mazeSize) {
		if((mazeSize) * normalTileScale > Gdx.graphics.getWidth()) {
			return (int)(Gdx.graphics.getWidth()/mazeSize);
		}
		else {
			return (int)(normalTileScale);
		}
	}
	

	@Override
	public void show() {
		MainFrame.changeColor();
		Gdx.input.setInputProcessor(this);
		MainFrame.gameFont.setColor(MainFrame.textColor);
		MainFrame.gameFontSmall.setColor(MainFrame.textColor);
		ScreenFadeTool.fadeIn("none");

		//update the cam for showing a screen I guess.
		orthoCam.update();
		
		//TEMP CODE FOR TESTING SO I Don'T HAVE TO DO STUFF
		progressionArray[0] = true;
	}

	@Override
	public void render(float delta) {
		//ERASE
		//ha I can make the clear color fade too cool huh
		//except I have to make them ALL FLOATS
		if(ScreenFadeTool.isFading()) {
			ScreenFadeTool.updateFade();
			//FIXME temporary change, will go back to code below instead of above.
			Gdx.gl.glClearColor(34/255f * ScreenFadeTool.getFadeTracker(), 
					34/255f * ScreenFadeTool.getFadeTracker(),
					34/255f * ScreenFadeTool.getFadeTracker(), 1);
			//ScreenFadeTool.fadeItem(MainFrame.textColor);
			//ScreenFadeTool.fadeItem(MainFrame.logoColor);
			ScreenFadeTool.fadeItem(MainFrame.batch);
		}
		else{
			Gdx.gl.glClearColor(34/255f, 34/255f, 34/255f, 1);
		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		MainFrame.batch.setProjectionMatrix(orthoCam.combined);
		orthoCam.update();
		
		//REDRAW
		//TEST CODE FOR THING:
		MainFrame.handleDude.printMessage("");
		MainFrame.batch.begin();
		if(currentLayouts.contains("single")) {
			MainFrame.drawingText();
			MainFrame.gameFont.draw(MainFrame.batch, "aMaze", Gdx.graphics.getWidth()/2-FontTools.getWidthOf("aMaze")/2,
					Gdx.graphics.getHeight()/4 * 3 + FontTools.getHeightOf("aMaze") + 5); /*5 for a buffer.*/
			if(progressionArray[0] == true) {//if first level is completed
				levelSelectDisplay.drawCenteredX(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/4 * 3);
				
				MainFrame.gameFontSmall.draw(MainFrame.batch, explText1,
						Gdx.graphics.getWidth()/2-FontTools.getWidthOfSmall(explText1)/2,
						Gdx.graphics.getHeight()/4*3 - FontTools.getHeightOf(levelSelectDisplay.getStr()) - 10);//5 is buffer.
				MainFrame.gameFontSmall.draw(MainFrame.batch, explText2,
						Gdx.graphics.getWidth()/2-FontTools.getWidthOfSmall(explText2)/2,
						Gdx.graphics.getHeight()/4*3 - FontTools.getHeightOfSmall(explText2) - 
						FontTools.getHeightOf(levelSelectDisplay.getStr()) - 20);//minus 5 for a buffer
				MainFrame.gameFontSmall.draw(MainFrame.batch, "Scroll up for Multiplayer \\/", 
						Gdx.graphics.getWidth()/2-FontTools.getWidthOfSmall("Scroll up for Multiplayer \\/")/2,
						FontTools.getHeightOf("Scroll up for Multiplayer \\/") + 5);//plus 5 for buffer.
			}
			else {//if firstr level is not completed.
				MainFrame.gameFontSmall.draw(MainFrame.batch, "Tap to Begin",
						Gdx.graphics.getWidth()/2 - FontTools.getWidthOfSmall("Tap to Begin")/2,
						Gdx.graphics.getHeight()/4 * 3);
			}
			//MainFrame.gameFont.draw(MainFrame.batch, explText, FontTools.getWidthOf(explText),
					//Gdx.graphics.getHeight()/4 * 3 - FontTools.getHeightOf(levelSelectDisplay.getStr()));

		}
		if(currentLayouts.contains("splay")) {//single player mode, playing screen
			//[row - x][column - y] I think.
			MainFrame.drawingText();
			MainFrame.gameFont.draw(MainFrame.batch, (currentMaze.getSize()-2) + " x " + (currentMaze.getSize()-2),
					singlePlayRefPoint.x+ Gdx.graphics.getWidth()/2-FontTools.getWidthOf((currentMaze.getSize()-2) + " x " + (currentMaze.getSize()-2))/2,
					singlePlayRefPoint.y+ (Gdx.graphics.getHeight() - currentMaze.getSize() * currentMaze.tileSize)/2 -
					FontTools.getHeightOf((currentMaze.getSize()-2) + " x " + (currentMaze.getSize()-2)));
			MainFrame.gameFontSmall.draw(MainFrame.batch, "< Save and go back", 2, FontTools.getHeightOf("< Save and go back"));
			currentMaze.draw(MainFrame.batch);
			MainFrame.drawingPlayer();
			player.draw();
		}
		
		if(currentLayouts.contains("multi")) {//multi player mode, just MENU
			MainFrame.drawingText();
			MainFrame.gameFont.draw(MainFrame.batch, "Multiplayer", Gdx.graphics.getWidth()/2-FontTools.getWidthOf("Multiplayer")/2,
					Gdx.graphics.getHeight()/4 * 3 + FontTools.getHeightOf("Multiplayer") / 2);
		}
		
		MainFrame.batch.end();
		//INPUT
		
		//CALCULATE
		if(currentLayouts.contains("single")) {
			levelSelectDisplay.update();
		}
		if(currentLayouts.equals("splay")) {
			if(currentMaze.translateX(player.getPos().x) == currentMaze.getSize()-2 &&
					currentMaze.translateY(player.getPos().y) == currentMaze.getSize()-2 &&
					!screenTransTool.isTransitioning()){
				MainFrame.completionSound.play();
				if(levelSelectDisplay.getNumber() == levelSelectDisplay.getMaxNum()){
					//go back to main menu if we've finished max
					screenTransTool.start("single", 'l');
				}
				else {
					//go up a level
					levelSelectDisplay.setNumber(levelSelectDisplay.getNumber()+2);//go up a level
					levelSelectDisplay.update();
					if(progressionArray[(levelSelectDisplay.getNumber()+1)/2-4] == false){//if level wasn't finished yet,
						progressionArray[(levelSelectDisplay.getNumber()+1)/2-4] = true;//mark as finished!
						levelSelectDisplay.setProgMax(levelSelectDisplay.getNumber());//change display max
					}
					screenTransTool.start("splay", 'd');
				}
				
			}
		}
		
		//transition stuff
		if(screenTransTool.isTransitioning()) {
			if(!(temp = screenTransTool.update()).equals("")) {
				currentLayouts = temp;
				MainFrame.changeColor();
				if(temp.equals("splay")) {
					currentMaze.createMaze(levelSelectDisplay.getNumber(), getBlockSizeForLv(levelSelectDisplay.getNumber()+2));
					
					currentMaze.setPosX(Gdx.graphics.getWidth()/2-currentMaze.getTotalSize()/2);
					currentMaze.setPosY(Gdx.graphics.getHeight()-Gdx.graphics.getWidth()/2-currentMaze.tileSize*currentMaze.getSize()/2);
					player.setPos(currentMaze.getStartingPosX(), currentMaze.getStartingPosY());
					player.setSize(currentMaze.tileSize);
				}
			}
			
		}
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void dispose() {
		for(int i = 0; i < 99; i++) {
			if(progressionArray[i]) {//if this here is true
				progStr[i] = '1';//level unlocked in save state
			}
			else {//but if not
				progStr[i] = '0';//then it's not unlocked.
			}
		}
		prefs.flush();
	}
	
	public void save() {
		
	}

	@Override
	public boolean keyDown(int keycode) {
		if(currentLayouts.contains("splay")) {
			if(keycode == Keys.BACK) {
				screenTransTool.start("main", 'l');
			}
			if(keycode == Keys.A) {
				System.out.println(player.checkCollision('r', 1));
				System.out.println("x" + player.getPos().x + " " + currentMaze.getX() + " " + currentMaze.tileSize);
				System.out.println("y" + player.getPos().y + " " + currentMaze.getY() + " " + currentMaze.tileSize);
			}
			if(keycode == Keys.UP) {
				System.out.println(player.isOnTrackVert());
			}
			if(keycode == Keys.RIGHT) {
				System.out.println(player.isOnTrackHoriz());
			}
		}
		
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		touchDownPoint.x = screenX;
		touchDownPoint.y = screenY;
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(currentLayouts.contains("single")) {
			if(!dragThisTouch) {
				System.out.println("Didn't drag.");
				levelSelectDisplay.playerDragged(0);
				screenTransTool.start("splay", 'r');
			}
			else if(dragThisTouch) {//if we did drag this touch
				if(orthoCam.position.y != Gdx.graphics.getHeight() / 2) {
					//then we dragged up for multi at least a little bit.
				}
			}
			
			dragDeadCounter = 0;
			dragThisTouch = false;
		}//end if single
		touchDownPoint.x = screenX;
		touchDownPoint.y = screenY;
		orthoCam.unproject(touchDownPoint);
		if(currentLayouts.contains("splay")) {
			if(!screenTransTool.isTransitioning() && backButton.contains(touchDownPoint.x, touchDownPoint.y)) {
				//go back and save
				screenTransTool.start("single", 'l');
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(screenTransTool.isTransitioning())return false;//if transitioning, no input.
		if(currentLayouts.contains("single")) {
			if(Gdx.input.getDeltaX() > 0) {
				//used to be minus equals but now it's not so it'll go the opposite way
				dragDeadCounter += MainFrame.distForm(touchDownPoint.x, touchDownPoint.y, screenX, screenY);
			}
			else {
				//used to be plus equals but now it's not so it'll go the oppostie way
				dragDeadCounter -= MainFrame.distForm(touchDownPoint.x, touchDownPoint.y, screenX, screenY);
			}
			
			if(Math.abs(dragDeadCounter) >= dragDeadZone) {//passed threshold, we're dragging.

				dragThisTouch = true;
			}
			if(dragThisTouch && Math.abs(Gdx.input.getDeltaX()) > 1) {
				// multiplied by -1 so it'll go the right way.
				levelSelectDisplay.playerDragged(Gdx.input.getDeltaX() * -1);
			}
			if(Math.abs(Gdx.input.getDeltaY()) > Math.abs(Gdx.input.getDeltaX()) && !screenTransTool.isTransitioning()) {
				//if the dragging up and down is bigger then the dragging of left and right, then,
				if(orthoCam.position.y <= Gdx.graphics.getHeight()/2) {//as long as camera isn't going up
					orthoCam.position.y += Gdx.input.getDeltaY();//move screen up with drag
					if(orthoCam.position.y > Gdx.graphics.getHeight()/2)//if we've dragged to far,
						orthoCam.position.y = Gdx.graphics.getHeight()/2;//then reset.
				}
				if(orthoCam.position.y < Gdx.graphics.getHeight()/2 - Gdx.graphics.getHeight()/4) {
					//transition screen here.
					screenTransTool.start("multi", 'd');
				}
			}
		}//end if on single menu
		if(currentLayouts.contains("multi")) {//if in multi menu
			
		}
		if(currentLayouts.contains("splay")) {
			player.drag();
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
