package com.meros.playn.core;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.PlayN;

public class Layer {

	CanvasImage myBuffer;
	boolean myBufferIsDirty = true;
	Tile myDummyTile = new Tile();

	int myHeight;
	Tile[][] myTiles;

	int myWidth;

	public Layer(int aWidth, int aHeight) {
		myWidth = aWidth;
		myHeight = aHeight;

		
		myBuffer = PlayN.graphics().createImage(Math.max(aWidth, 1) * 10, Math.max(aHeight, 1) * 10);

		myTiles = new Tile[aWidth][aHeight];
	}

	public void draw(Canvas aBuffer, int aOffsetX, int aOffsetY) {
		if (myBufferIsDirty) {
			privUpdateBuffer(myBuffer.canvas());
			myBufferIsDirty = false;
		}

		aBuffer.drawImage(myBuffer, aOffsetX, aOffsetY);
	}

	public float getHeight() {
		return myHeight;
	}

	public Tile getTile(int aX, int aY) {

		if (aX < 0 || aY < 0 || aX >= myTiles.length || aY >= myTiles[aX].length)
			return myDummyTile;

		return myTiles[aX][aY];
	}

	public float getWidth() {
		return myWidth;
	}

	private void privUpdateBuffer(Canvas aBuffer) {
		for (int x = 0; x < getWidth(); ++x) {
			for (int y = 0; y < getHeight(); ++y) {
				Tile tile = getTile(x, y);

				if (tile != null) {
					tile.onDraw(aBuffer, x * tile.getWidth(),
							y * tile.getHeight());
				}
			}
		}
	}

	public void setTile(int aX, int aY, Tile aTile) {
		myTiles[aX][aY] = aTile;

		myBufferIsDirty = true;
	}
}
