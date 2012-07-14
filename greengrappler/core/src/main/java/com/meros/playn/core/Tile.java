package com.meros.playn.core;

import playn.core.Surface;
import playn.core.Image;

public class Tile {

	private final boolean myCollide;
	private final boolean myHook;
	private final Image myTileImage;
	
	public Tile() {
		myTileImage = null;
		myHook = false;
		myCollide = false;
	}

	public Tile(Image aTilemap, int aX, int aY, int aW, int aH, boolean aHookable, boolean aCollidable) {
		myTileImage = aTilemap.subImage(aX, aY, aW, aH);
				
		myHook = aHookable;
		myCollide = aCollidable;
	}

	public boolean getCollide() {
		return myCollide;
	}

	public int getHeight() {
		return (int) myTileImage.height();
	}

	public boolean getHook() {
		return myHook;
	}

	public int getWidth() {
		return (int) myTileImage.width();
	}

	public Image getImage() {
		return myTileImage;
	}

	public boolean isOpaque() {
		return myTileImage != null;
	}
}
