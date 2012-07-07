package com.meros.playn.core;

import playn.core.Surface;
import playn.core.Image;

public class Tile {

	boolean myCollide = false;
	int myH;
	boolean myHook = false;
	Image myTileImage = null;
	int myW;

	int myX;
	int myY;

	public Tile() {
	}

	public Tile(Image aTilemap, int aX, int aY, int aW, int aH) {
		myTileImage = aTilemap;
		myX = aX;
		myY = aY;
		myH = aH;
		myW = aW;
	}

	public Tile clone() {
		Tile tile = new Tile(myTileImage, myX, myY, myW, myH);
		tile.setCollide(myCollide);
		tile.setHook(myHook);

		return tile;
	}

	public boolean getCollide() {
		return myCollide;
	}

	public int getHeight() {
		return myH;
	}

	public boolean getHook() {
		return myHook;
	}

	public int getWidth() {
		return myW;
	}

	public void onDraw(Surface aBuffer, int aX, int aY) {
		if (myTileImage == null)
			return;

		// TODO: this causes artifacts
		aBuffer.drawImage(myTileImage, aX, aY, myW, myH, myX, myY, myW, myH);
	}

	public void setCollide(boolean aCollide) {
		myCollide = aCollide;
	}

	public void setHook(boolean aHook) {
		myHook = aHook;
	}
}
