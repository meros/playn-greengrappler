package com.meros.playn.core;

public class Layer {
	
	int myWidth;
	int myHeight;
	
	Tile[][] myTiles;

	public Layer(int aWidth, int aHeight) {
		myWidth = aWidth;
		myHeight = aHeight;
		
		myTiles = new Tile[aWidth][aHeight];
	}
	
	public void setTile(int aX,	int aY,	Tile aTile)
	{
		myTiles[aX][aY] = aTile;
	}

	public Tile getTile(int aX, int aY)
	{
		
		if (aX < 0)
			return null;
		if (aY < 0)
			return null;
		if (aX > getWidth())
			return null;
		if (aY > getHeight())
			return null;
		
		return myTiles[aX][aY];
	}
	
	public float getHeight() {
		// TODO Auto-generated method stub
		return myHeight;
	}

	public float getWidth() {
		// TODO Auto-generated method stub
		return myWidth;
	}

}
