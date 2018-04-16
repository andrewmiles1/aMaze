package com.burnt_toast.amazement;

import android.os.Bundle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidGraphics;
import com.burnt_toast.amazement.MainFrame;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MainFrame(), config);
	}
	@Override
	protected void onPause(){
		super.onPause();
		Gdx.graphics.requestRendering();

	}
	@Override
	protected void onResume(){
		super.onResume();
	}

}
