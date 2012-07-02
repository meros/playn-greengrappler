package com.meros.playn.core;

public class RoomLoader {

	public static Room LoadRoom(String myLevelFile) {
		Room room = new Room(new Layer(), new Layer(), new Layer());
		Hero hero = new Hero();
		Camera camera = new Camera();
		hero.setPosition(new float2(20, 20));
		room.addEntity(hero);
		room.setCamera(camera);
		
		return room;
	}

}
