package com.meros.playn.core;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.CanvasSurface;
import playn.core.Image;
import playn.core.Pattern;
import playn.core.PlayN;
import playn.core.Surface;
import playn.core.gl.ImageGL;
import playn.core.gl.SurfaceGL;

public class Layer {

	CanvasImage myBuffer;
	boolean myBufferIsDirty = true;
	Tile myDummyTile = new Tile();

	int myHeight;
	Tile[][] myTiles;

	int myWidth;
	private int myDestroyedToTileRow = 0;

	public Layer(int aWidth, int aHeight) {
		myWidth = aWidth;
		myHeight = aHeight;

		
		myBuffer = PlayN.graphics().createImage(Math.max(aWidth, 1) * 10, Math.max(aHeight, 1) * 10);

		myTiles = new Tile[aWidth][aHeight];
	}

	public void draw(Surface aBuffer, int aOffsetX, int aOffsetY, Layer[] aOverLayers) {
		
		int firstTileX = (-aOffsetX)/10;
		int lastTileX = (int) ((aBuffer.width() - aOffsetX)/10);
		int firstTileY = (-aOffsetY)/10;
		int lastTileY = (int) ((aBuffer.height() - aOffsetY)/10);
		
		for (int x = firstTileX; x <= lastTileX; x++) {
			for (int y = firstTileY; y <= lastTileY; y++) {
				
				boolean dontDraw = false;
				
				for (Layer overLayer : aOverLayers)
				{
					Tile overLayerTile = overLayer.getTile(x, y);
					if (overLayerTile != null && overLayerTile.isOpaque())
					{
						dontDraw = true;
					}
				}
				
				if (dontDraw)
				{
					continue;
				}
				
				Tile tile = getTile(x, y);
				if (tile != null && tile.getImage() != null) {
					Image image = tile.getImage();

					int startX = aOffsetX + x * 10;
					int startY = aOffsetY + y * 10;
					
					aBuffer.drawImage(image, startX, startY);
				}
			}
		}
		
		/*
		
		if (myBufferIsDirty) {
			privUpdateBuffer(myBuffer.canvas());
			myBufferIsDirty = false;
		}
		
		int sX = myDestroyedToTileRow*10;
		int sY = 0;
		int sW = (int) (myBuffer.width()-sX);
		int sH = (int) myBuffer.height();

		aBuffer.drawImage(myBuffer, aOffsetX+sX, aOffsetY, sW, sH, sX, sY, sW, sH);
		*/
	}

	public int getHeight() {
		return myHeight;
	}

	public Tile getTile(int aX, int aY) {
		if (aX < myDestroyedToTileRow || aY < 0 || aX >= myTiles.length || aY >= myTiles[aX].length)
			return myDummyTile;

		return myTiles[aX][aY];
	}

	public int getWidth() {
		return myWidth;
	}

	public void setTile(int aX, int aY, Tile aTile) {
		myTiles[aX][aY] = aTile;

		myBufferIsDirty = true;
	}

	public void setDestroyedToTileRow(int aX) {
		myDestroyedToTileRow = aX;
	}
}
