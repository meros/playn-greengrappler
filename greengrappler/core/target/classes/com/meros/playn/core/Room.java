package com.meros.playn.core;

public class Room {

	public float getTileWidth() {
		// TODO Auto-generated method stub
		return 15.0f;
	}

	public float getTileHeight() {
		// TODO Auto-generated method stub
		return 15.0f;
	}

	public boolean isCollidable(int x, int y) {
		// TODO Auto-generated method stub
		return y > 10;
	}

}
