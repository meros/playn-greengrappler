package com.meros.playn.core;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.PlayN;
import playn.core.Surface;

public class Layer {

	int myWidth;
	int myHeight;
	
	boolean myBufferIsDirty = true;
	CanvasImage myBuffer;

	Tile[][] myTiles;

	public Layer(int aWidth, int aHeight) {
		myWidth = aWidth;
		myHeight = aHeight;
		
		myBuffer = PlayN.graphics().createImage(aWidth*10, aHeight*10);

		myTiles = new Tile[aWidth][aHeight];
	}

	public void setTile(int aX,	int aY,	Tile aTile)
	{
		myTiles[aX][aY] = aTile;
		
		myBufferIsDirty = true;
	}

	public Tile getTile(int aX, int aY)
	{

		if (aX < 0)
			return new Tile();
		if (aY < 0)
			return new Tile();
		if (aX >= getWidth())
			return new Tile();
		if (aY >= getHeight())
			return new Tile();

		return myTiles[aX][aY];
	}

	public float getHeight() {
		return myHeight;
	}

	public float getWidth() {
		return myWidth;
	}

	public void draw(Surface aBuffer, int aOffsetX, int aOffsetY) {
		if (myBufferIsDirty)
		{
			privUpdateBuffer(myBuffer.canvas());
			myBufferIsDirty = false;
		}
		
		aBuffer.drawImage(myBuffer, 0, 0, 320, 240, -aOffsetX, -aOffsetY, 320, 240);
	}
	
	private void privUpdateBuffer(Canvas aBuffer)
	{
		for (int x = (int) 0; x < getWidth(); ++x) 
		{
			for (int y = (int) 0; y < getHeight(); ++y) 
			{		
				Tile tile = getTile(x, y);
				
				if (tile != null)
				{
					tile.onDraw(aBuffer, x*tile.getWidth(), y*tile.getHeight());					
				}
			}
		}
	}
}
