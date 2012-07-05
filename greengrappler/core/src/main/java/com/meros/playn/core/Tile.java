package com.meros.playn.core;

import playn.core.Surface;
import playn.core.Image;

public class Tile {
	
	Image myTileImage = null;
	int myX;
	int myY;
	int myH;
	int myW;
	
	boolean myCollide = false;
	boolean myHook = false;
	
	public Tile clone()
	{
		Tile tile = new Tile(myTileImage, myX, myY, myW, myH);
		tile.setCollide(myCollide);
		tile.setHook(myHook);
		
		return tile;
	}
	
	public Tile(Image aTilemap, int aX, int aY, int aW, int aH)
	{
		myTileImage = aTilemap;
		myX = aX;
		myY = aY;
		myH = aH;
		myW = aW;
	}

	public Tile() {
	}

	public void onDraw(
		Surface 	aBuffer,
		int		aX,
		int		aY)
	{
		if (myTileImage == null)
			return;
		
		aBuffer.drawImage(myTileImage, aX, aY, myW, myH, myX, myY, myW, myH);
	}

	public int	 getWidth()
	{
		return myW;
	}

	public int	 getHeight()
	{
		return myH;
	}

	public void setCollide(
		boolean aCollide)
	{
		myCollide = aCollide;
	}

	public boolean getCollide()
	{
		return myCollide;
	}

	public void setHook(
		boolean aHook)
	{
		myHook = aHook;
	}

	public boolean getHook()
	{
		return myHook;
	}
}
