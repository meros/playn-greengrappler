package com.meros.playn.core;

import playn.core.PlayN;

public class RoomLoader {

	public static Room LoadRoom(String myLevelFile) {
		RoomLoader loader = new RoomLoader();

		String roomData = Resource.getText(myLevelFile);

		String[] data = roomData.split("\n");

		int curri = 0;

		//Read width and height
		int width = Integer.parseInt(data[curri++]);
		int height = Integer.parseInt(data[curri++]);

		//Tile info
		int numTileTypes = Integer.parseInt(data[curri++]);

		Tile[] tiles = new Tile[numTileTypes];

		for (int i = 0; i < numTileTypes; i++)
		{
			String filename = data[curri++];
			int x = Integer.parseInt(data[curri++]);
			int y = Integer.parseInt(data[curri++]);
			int w = Integer.parseInt(data[curri++]);
			int h = Integer.parseInt(data[curri++]);

			boolean collide = Integer.parseInt(data[curri++]) == 1;
			boolean hookable = Integer.parseInt(data[curri++]) == 1;


			tiles[i] = new Tile(Resource.getBitmap(filename), x, y, w, h);
			tiles[i].setHook(hookable);
			tiles[i].setCollide(collide);
		}

		//Layer bg
		Layer backgroundLayer = new Layer(width, height);

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				int tileType = Integer.parseInt(data[curri++]);
				if (tileType != -1)
				{
					backgroundLayer.setTile(x, y, tiles[tileType]);
				}
			}
		}

		//Layer mid
		Layer middleLayer = new Layer(width, height);

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				int tileType = Integer.parseInt(data[curri++]);
				if (tileType != -1)
				{
					middleLayer.setTile(x, y, tiles[tileType]);
				}
			}
		}

		//Layer fg
		Layer foregroundLayer = new Layer(width, height);

		for (int x = 0; x < width; x++)
		{
			for (int y = 0; y < height; y++)
			{
				int tileType = Integer.parseInt(data[curri++]);
				if (tileType != -1)
				{
					foregroundLayer.setTile(x, y, tiles[tileType]);
				}
			}
		}

		Room room = new Room(backgroundLayer, middleLayer, foregroundLayer);
		room.setCamera(new Camera());
		room.addEntity(new Hero());

		return room;
	}

}
