package com.burnt_toast.amazement;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class FontTools {
	public static GlyphLayout glyphLayout = new GlyphLayout();

	
	public static float getHeightOf(String str){
		glyphLayout.setText(MainFrame.gameFont, str);
		return glyphLayout.height;
	}
	public static float getWidthOf(String str){
		glyphLayout.setText(MainFrame.gameFont, str);
		return glyphLayout.width;
	}

}
