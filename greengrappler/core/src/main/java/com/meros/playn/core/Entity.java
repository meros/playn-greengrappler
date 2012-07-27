package com.meros.playn.core;

import java.util.EnumSet;

import playn.core.Surface;

import com.meros.playn.core.Collision.AbstractCollidable;
import com.meros.playn.core.Constants.Direction;
import com.meros.playn.core.floatpair.AbstractFloatPair;
import com.meros.playn.core.floatpair.FloatPair;
import com.meros.playn.core.floatpair.ImmutableFloatPair;

public abstract class Entity implements AbstractCollidable {

	protected int myFrameCounter = 0;
	protected boolean myRemoved = false;
	protected Room myRoom = null;

	private FloatPair myPosition = new FloatPair();
	protected FloatPair myVelocity = new FloatPair();

	private ImmutableFloatPair mySize = new ImmutableFloatPair();
	private ImmutableFloatPair myHalfSize = new ImmutableFloatPair();

	public void draw(Surface aBuffer, int offsetX, int offsetY, int layer) {
		// int x = getDrawPositionX() + offsetX;
		// int y = getDrawPositionY() + offsetY;
		// int x1 = (int) (x - getHalfSize().x);
		// int y1 = (int) (y - getHalfSize().y);
		// int x2 = (int) (x + getHalfSize().x);
		// int y2 = (int) (y + getHalfSize().y);
		// // buffer.strokeRect(x1, y1, x2-x1, y2-y1);
		// // buffer.drawLine(x - 3, y, x + 3, y);
		// // buffer.drawLine(x, y - 3, x, y + 3);
	}

	@Override
	public float getCollideTop() {
		return getPosition().getY() - getHalfSize().getY();
	}

	@Override
	public float getCollideLeft() {
		return getPosition().getX() - getHalfSize().getX();
	}

	@Override
	public float getCollideBottom() {
		return getPosition().getY() + getHalfSize().getY();
	}

	@Override
	public float getCollideRight() {
		return getPosition().getX() + getHalfSize().getX();
	}

	public boolean Collides(AbstractCollidable aOther) {
		return Collision.Collides(this, aOther);
	}

	public int getDrawPositionX() {
		return (int) (getPosition().getX() + 0.5);
	}

	public int getDrawPositionY() {
		return (int) (getPosition().getY() + 0.5);
	}

	public ImmutableFloatPair getHalfSize() {
		return myHalfSize;
	}

	public abstract int getLayer();

	public FloatPair getPosition() {
		return myPosition;
	}

	public Room getRoom() {
		return myRoom;
	}

	public ImmutableFloatPair getSize() {
		return mySize;
	}

	public FloatPair getVelocity() {
		return myVelocity;
	}

	public boolean isDamagable() {
		return false;
	}

	// virtual bool isHookable();
	public boolean isHookable() {
		return false;
	}

	// virtual bool isRemoved();
	public boolean isRemoved() {
		return myRemoved;
	}

	// virtual unsigned int moveWithCollision();
	public EnumSet<Direction> moveWithCollision() {
		return moveWithCollision(myVelocity.getX() / Constants.TICKS_PER_SECOND,
				myVelocity.getY() / Constants.TICKS_PER_SECOND);
	}

	private EnumSet<Direction> myMoveWithCollisionResult = EnumSet
			.noneOf(Direction.class);

	public EnumSet<Direction> moveWithCollision(float aDeltaX, float aDeltaY) {
		float deltaX = aDeltaX;
		float deltaY = aDeltaY;

		int substeps = (int) Math
				.ceil((Math.abs(aDeltaX) + Math.abs(aDeltaY)) * 0.2);
		deltaX /= substeps;
		deltaY /= substeps;

		myMoveWithCollisionResult.clear();
		ImmutableFloatPair halfSize = getHalfSize();

		for (int i = 0; i < substeps; i++) {
			myPosition.add(deltaX, 0);
			int x1 = (int) ((myPosition.getX() - halfSize.getX()) / myRoom
					.getTileWidth());
			int x2 = (int) ((myPosition.getX() + halfSize.getX()) / myRoom
					.getTileWidth());
			int y1n = (int) ((myPosition.getY() - halfSize.getY() + 0.01f) / myRoom
					.getTileHeight());
			int y2n = (int) ((myPosition.getY() + halfSize.getY() - 0.01f) / myRoom
					.getTileHeight());

			if (deltaX > 0) {
				for (int y = y1n; y <= y2n; y++) {
					if (myRoom.isCollidable(x2, y)) {

						deltaX = 0;

						myMoveWithCollisionResult.add(Direction.RIGHT);
						myPosition.set(
								x2 * myRoom.getTileWidth() - halfSize.getX(),
								myPosition.getY());
						break;
					}
				}
			} else if (deltaX < 0) {
				for (int y = y1n; y <= y2n; y++) {
					if (myRoom.isCollidable(x1, y)) {

						deltaX = 0;

						myMoveWithCollisionResult.add(Direction.LEFT);
						myPosition.set((x1 + 1) * myRoom.getTileWidth()
								+ halfSize.getX(), myPosition.getY());
						break;
					}
				}
			}

			myPosition.add(0, deltaY);
			int y1 = (int) ((myPosition.getY() - halfSize.getY()) / myRoom
					.getTileHeight());
			int y2 = (int) ((myPosition.getY() + halfSize.getY()) / myRoom
					.getTileHeight());
			int x1n = (int) ((myPosition.getX() - halfSize.getX() + 0.01f) / myRoom
					.getTileWidth());
			int x2n = (int) ((myPosition.getX() + halfSize.getX() - 0.01f) / myRoom
					.getTileWidth());

			if (deltaY > 0) {
				for (int x = x1n; x <= x2n; x++) {
					if (myRoom.isCollidable(x, y2)) {

						deltaY = 0;

						myMoveWithCollisionResult.add(Direction.DOWN);
						myPosition.set(myPosition.getX(),
								y2 * myRoom.getTileHeight() - halfSize.getY());
						break;
					}
				}
			} else if (deltaY < 0) {
				for (int x = x1n; x <= x2n; x++) {
					if (myRoom.isCollidable(x, y1)) {

						deltaY = 0;

						myMoveWithCollisionResult.add(Direction.UP);
						myPosition.set(
								myPosition.getX(),
								(y1 + 1) * myRoom.getTileHeight()
										+ halfSize.getY());
					}
				}
			}
		}

		return myMoveWithCollisionResult;
	}

	// virtual void onBossFloorActivate();
	public void onBossFloorActivate() {

	}

	// virtual void onBossWallActivate();
	public void onBossWallActivate() {

	}

	// virtual void onBossWallDeactivate();
	public void onBossWallDeactivate() {

	}

	// virtual void onButtonDown(int aId);
	public void onButtonDown(int aId) {

	}

	// virtual void onButtonUp(int aId);
	public void onButtonUp(int aId) {

	}

	// virtual void onDamage();
	public void onDamage() {

	}

	// virtual void onLevelComplete();
	public void onLevelComplete() {

	}

	// virtual void onRespawn();
	public void onRespawn() {

	}

	public void onStartWallOfDeath() {

	}

	public void remove() {
		myRemoved = true;
	}

	public void setPosition(AbstractFloatPair position) {
		myPosition.set(position);
	}

	public void setRoom(Room room) {
		myRoom = room;
	}

	public void setSize(ImmutableFloatPair size) {
		mySize = size;
		myHalfSize = size.multiply(0.5f);
	}

	public void setVelocity(AbstractFloatPair velocity) {
		myVelocity.set(velocity);
	}

	public void update() {
		myFrameCounter++;
	}

}
