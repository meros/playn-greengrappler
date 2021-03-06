package se.darkbits.greengrappler;

import se.darkbits.greengrappler.floatpair.ImmutableFloatPair;

public class RoomLoader {

	public static Room LoadRoom(String myLevelFile) {
		String roomData = Resource.getText(myLevelFile);

		String[] data = roomData.split("\n");

		int curri = 0;

		// Tile info
		int numTileTypes = Integer.parseInt(data[curri++]);

		Tile[] tiles = new Tile[numTileTypes];
		Tile emptyTile = new Tile();

		for (int i = 0; i < numTileTypes; i++) {
			String filename = data[curri++];
			int x = Integer.parseInt(data[curri++]);
			int y = Integer.parseInt(data[curri++]);
			int w = Integer.parseInt(data[curri++]);
			int h = Integer.parseInt(data[curri++]);

			boolean collide = Integer.parseInt(data[curri++]) == 1;
			boolean hookable = Integer.parseInt(data[curri++]) == 1;

			tiles[i] = new Tile(Resource.getBitmap(filename), x, y, w, h,
					hookable, collide);
			// tiles[i].setHook(hookable);
			// tiles[i].setCollide(collide);
		}

		// Layer bg
		// Read width and height
		int width = Integer.parseInt(data[curri++]);
		int height = Integer.parseInt(data[curri++]);
		Layer backgroundLayer = new Layer(width, height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int tileType = Integer.parseInt(data[curri++]);
				if (tileType != -1) {
					backgroundLayer.setTile(x, y, tiles[tileType]);
				}
			}
		}

		// Layer mid
		width = Integer.parseInt(data[curri++]);
		height = Integer.parseInt(data[curri++]);
		Layer middleLayer = new Layer(width, height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int tileType = Integer.parseInt(data[curri++]);
				if (tileType != -1) {
					middleLayer.setTile(x, y, tiles[tileType]);
				} else {
					middleLayer.setTile(x, y, emptyTile);
				}
			}
		}

		// Layer fg
		width = Integer.parseInt(data[curri++]);
		height = Integer.parseInt(data[curri++]);
		Layer foregroundLayer = new Layer(width, height);

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int tileType = Integer.parseInt(data[curri++]);
				if (tileType != -1) {
					foregroundLayer.setTile(x, y, tiles[tileType]);
				}
			}
		}

		Room room = new Room(backgroundLayer, middleLayer, foregroundLayer,
				new Camera(new ImmutableFloatPair(0, 0),
						new ImmutableFloatPair(middleLayer.getWidth() * 10,
								middleLayer.getHeight() * 10)));

		// Entities
		width = Integer.parseInt(data[curri++]);
		height = Integer.parseInt(data[curri++]);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int entityId = Integer.parseInt(data[curri++]);
				if (entityId != -1) {
					Entity entity = EntityFactory.create(entityId);

					if (entity == null) {
						continue;
					}

					ImmutableFloatPair pos = new ImmutableFloatPair(
							10 * x + 10 / 2, (10 * (y + 1))
									- entity.getHalfSize().getY());

					entity.setPosition(pos);
					room.addEntity(entity);
				}
			}
		}

		int cameraRectCount = Integer.parseInt(data[curri++]);

		for (int i = 0; i < cameraRectCount; i++) {
			int x = Integer.parseInt(data[curri++]);
			int y = Integer.parseInt(data[curri++]);
			int w = Integer.parseInt(data[curri++]);
			int h = Integer.parseInt(data[curri++]);

			room.getCamera().addRect(x, y, w, h);
		}

		return room;
	}

}
