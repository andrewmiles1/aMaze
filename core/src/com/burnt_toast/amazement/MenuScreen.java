package com.burnt_toast.amazement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MenuScreen implements Screen, InputProcessor{
	
	private TextureRegion logoImg;
	
	public MenuScreen(MainFrame main){
		logoImg = new TextureRegion(main.gameTexture, 0, 0, 275, 53);
	}
	

	@Override
	public void show() {
		MainFrame.changeColor();
	}

	@Override
	public void render(float delta) {
		//ERASE
		//ha I can make the clear color fade too cool huh
		//except I have to make them ALL FLOATS
		if(ScreenFadeTool.isFading()) {
			Gdx.gl.glClearColor(34/255f * ScreenFadeTool.getFadeFactor(), 
					34/255f * ScreenFadeTool.getFadeFactor(),
					34/255f * ScreenFadeTool.getFadeFactor(), 1);
		}
		else{
			Gdx.gl.glClearColor(34/255f, 34/255f, 34/255f, 1);
		}
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//REDRAW
		MainFrame.batch.begin();
		MainFrame.drawingText();
		MainFrame.gameFont.draw(MainFrame.batch, "aMaze", Gdx.graphics.getWidth()/2-FontTools.getWidthOf("aMaze")/2,
				Gdx.graphics.getHeight()/4 * 3 + 5); /*5 for a buffer.*/
		//MainFrame.batch.draw(logoImg, Gdx.graphics.getWidth()/2-logoImg.getRegionWidth()/2, Gdx.graphics.getHeight()/4-logoImg.getRegionHeight()/2);
		MainFrame.batch.end();
		//INPUT
		
		//CALCULATE
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
		
	}

	@Override
	public boolean keyDown(int keycode) {
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
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
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
