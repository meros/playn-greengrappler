package se.darkbits.greengrappler.entities;

import playn.core.Surface;
import se.darkbits.greengrappler.Entity;
import se.darkbits.greengrappler.Resource;
import se.darkbits.greengrappler.Room;
import se.darkbits.greengrappler.Constants.Direction;
import se.darkbits.greengrappler.floatpair.ImmutableFloatPair;
import se.darkbits.greengrappler.media.Animation;


public class BossWall extends Entity {

	private Animation myWall = Resource.getAnimation("data/images/wall.bmp", 1);
	private Direction myDirection;
	private int myFrameCounter = 0;
	private boolean myActive = false;

	public BossWall(Direction aDirection) {
		myDirection = aDirection;
		setSize(new ImmutableFloatPair(10, 100));
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer) {
		int x = (int) (getDrawPositionX() + offsetX - getHalfSize().getX());
		int y = (int) (getDrawPositionY() + offsetY - getHalfSize().getY());

		myWall.drawFrame(buffer, 0, x, y, myDirection == Direction.LEFT, false);
		// Entity::draw(buffer, offsetX, offsetY, layer);
	}

	@Override
	public void update() {
		myFrameCounter++;

		if (myRoom.getHero().Collides(this)) {
			myRoom.getHero().kill();
		}

		if (!myActive)
			return;

		if (myDirection == Direction.RIGHT && myFrameCounter % 200 == 0) {
			BossSaw saw = new BossSaw(myDirection);
			saw.setPosition(new ImmutableFloatPair(getPosition().getX(),
					getPosition().getY() + getHalfSize().getY() - 10));
			myRoom.addEntity(saw);
		}

		if (myDirection == Direction.RIGHT && (100 + myFrameCounter) % 200 == 0) {
			BossSaw saw = new BossSaw(myDirection);
			saw.setPosition(new ImmutableFloatPair(getPosition().getX(),
					getPosition().getY() + getHalfSize().getY() - 30));
			myRoom.addEntity(saw);
		}

		if (myDirection == Direction.RIGHT && (180 + myFrameCounter) % 200 == 0) {
			BossSaw saw = new BossSaw(myDirection);
			saw.setPosition(new ImmutableFloatPair(getPosition().getX(),
					getPosition().getY() + getHalfSize().getY() - 50));
			myRoom.addEntity(saw);
		}

		if (myDirection == Direction.LEFT && myFrameCounter % 350 == 0) {
			BossSaw saw = new BossSaw(myDirection);
			saw.setPosition(new ImmutableFloatPair(getPosition().getX(),
					getPosition().getY() + getHalfSize().getY() - 10));
			myRoom.addEntity(saw);
		}

		if (myDirection == Direction.LEFT && (100 + myFrameCounter) % 350 == 0) {
			BossSaw saw = new BossSaw(myDirection);
			saw.setPosition(new ImmutableFloatPair(getPosition().getX(),
					getPosition().getY() + getHalfSize().getY() - 30));
			myRoom.addEntity(saw);
		}

		if (myDirection == Direction.LEFT && (300 + myFrameCounter) % 350 == 0) {
			BossSaw saw = new BossSaw(myDirection);
			saw.setPosition(new ImmutableFloatPair(getPosition().getX(),
					getPosition().getY() + getHalfSize().getY() - 50));
			myRoom.addEntity(saw);
		}

	}

	public void setTilesCollidable(boolean aCollidable) {
		int sx = (int) ((getPosition().getX() - getHalfSize().getX()) / 10);
		int sy = (int) ((getPosition().getY() - getHalfSize().getY()) / 10);
		int height = (int) ((getSize().getY()) / 10);

		for (int y = sy; y < sy + height; y++)
			myRoom.setCollidable(sx, y, aCollidable);
	}

	@Override
	public void setRoom(Room room) {
		super.setRoom(room);
		setTilesCollidable(true);
	}

	@Override
	public void onBossWallActivate() {
		myActive = true;
		myFrameCounter = 0;
	}

	@Override
	public void onRespawn() {
		myActive = false;
	}

	@Override
	public void onBossWallDeactivate() {
		myActive = false;
	}

	@Override
	public int getLayer() {
		return 3;
	}

}
