package com.burnt_toast.amazement;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Player {
	private TextureRegion imageBig;
	private TextureRegion imageSmall;
	private Vector2 position;//bottom left of player is pos
	
	public Player() {
		position = new Vector2();
		//These two images are scalable to whatever size of tile.
		imageBig = new TextureRegion(MainFrame.gameTexture, 0, 53, 50, 50);
		imageSmall = new TextureRegion(imageBig, 10, 10, 1, 1);
	}
	
	public void setPos(float x, float y) {
		position.x = x;
		position.y = y;
	}
	public Vector2 getPos() {
		return position;
	}
	public void draw(float tileSize) {
		if(imageBig.getRegionWidth() < tileSize) {
			MainFrame.batch.draw(imageBig, position.x, position.y, tileSize, tileSize);
		}
		else {
			MainFrame.batch.draw(imageSmall, position.x, position.y, tileSize, tileSize);
		}
	}
	public void drag() {
		if(Math.abs(Gdx.input.getDeltaX()) > Math.abs(Gdx.input.getDeltaY())) {
			//going x direction
			this.position.x += Gdx.input.getDeltaX();
		}
		else {
			//going y direction.
			this.position.y -= Gdx.input.getDeltaY();
		}
	}

}
