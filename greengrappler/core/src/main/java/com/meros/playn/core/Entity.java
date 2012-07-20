package com.meros.playn.core;

import java.util.EnumSet;

import playn.core.Surface;

import com.meros.playn.core.Constants.Direction;

public abstract class Entity implements Collidable {

	protected int mFrameCounter = 0;
	protected boolean mRemoved = false;
	protected Room mRoom = null;

	private FloatPair mPosition = new FloatPair();
	protected FloatPair mVelocity = new FloatPair();

	private ImmutableFloatPair mSize = new ImmutableFloatPair();
	private ImmutableFloatPair mHalfSize = new ImmutableFloatPair();

	// virtual void draw(BITMAP *buffer, int offsetX, int offsetY, int layer);
	public void draw(Surface aBuffer, int offsetX, int offsetY, int layer) {
		//		int x = getDrawPositionX() + offsetX;
		//		int y = getDrawPositionY() + offsetY;
		//		int x1 = (int) (x - getHalfSize().x);
		//		int y1 = (int) (y - getHalfSize().y);
		//		int x2 = (int) (x + getHalfSize().x);
		//		int y2 = (int) (y + getHalfSize().y);
		//		// buffer.strokeRect(x1, y1, x2-x1, y2-y1);
		//		// buffer.drawLine(x - 3, y, x + 3, y);
		//		// buffer.drawLine(x, y - 3, x, y + 3);
	}
	
	public float getCollideTop()
	{
		return getPosition().getY() - getHalfSize().getY();				
	}
	
	public float getCollideLeft()
	{
		return getPosition().getX() - getHalfSize().getX();		
	}

	public float getCollideBottom()
	{
		return getPosition().getY() + getHalfSize().getY();						
	}

	public float getCollideRight()
	{
		return getPosition().getX() + getHalfSize().getX();
	}

	public boolean Collides(Collidable aOther)
	{
		return CollisionMethods.Collides(this, aOther);
	}

	// virtual int getDrawPositionX();
	public int getDrawPositionX() {
		return (int) (getPosition().getX() + 0.5);
	}

	// virtual int getDrawPositionY();
	public int getDrawPositionY() {
		return (int) (getPosition().getY() + 0.5);
	}

	// virtual float2 getHalfSize();
	public ImmutableFloatPair getHalfSize() {
		return mHalfSize;
	}

	// virtual int getLayer() = 0;
	public abstract int getLayer();

	// virtual float2 getPosition();
	public FloatPair getPosition() {
		return mPosition;
	}

	// virtual Room *getRoom();
	public Room getRoom() {
		return mRoom;
	}

	// virtual float2 getSize();
	public ImmutableFloatPair getSize() {
		return mSize;
	}

	// virtual float2 getVelocity();
	public FloatPair getVelocity() {
		return mVelocity;
	}

	// virtual bool isDamagable();
	public boolean isDamagable() {
		return false;
	}

	// virtual bool isHookable();
	public boolean isHookable() {
		return false;
	}

	// virtual bool isRemoved();
	public boolean isRemoved() {
		return mRemoved;
	}

	// virtual unsigned int moveWithCollision();
	public EnumSet<Direction> moveWithCollision() {
		return moveWithCollision(
				mVelocity.getX()/Time.TicksPerSecond,
				mVelocity.getY()/Time.TicksPerSecond);
	}

	public EnumSet<Direction> moveWithCollision(float aDeltaX, float aDeltaY) {
		float deltaX = aDeltaX;
		float deltaY = aDeltaY;
		
		int substeps = (int) Math
				.ceil((Math.abs(aDeltaX) + Math.abs(aDeltaY)) * 0.2);
		deltaX /= substeps;
		deltaY /= substeps;
		
		EnumSet<Direction> result = EnumSet.noneOf(Direction.class);
		ImmutableFloatPair halfSize = getHalfSize();

		for (int i = 0; i < substeps; i++) {
			mPosition.add(deltaX, 0);
			int x1 = (int) ((mPosition.getX() - halfSize.getX()) / mRoom.getTileWidth());
			int x2 = (int) ((mPosition.getX() + halfSize.getX()) / mRoom.getTileWidth());
			int y1n = (int) ((mPosition.getY() - halfSize.getY() + 0.01f) / mRoom
					.getTileHeight());
			int y2n = (int) ((mPosition.getY() + halfSize.getY() - 0.01f) / mRoom
					.getTileHeight());

			if (deltaX > 0) {
				for (int y = y1n; y <= y2n; y++) {
					if (mRoom.isCollidable(x2, y)) {
						
						deltaX = 0;
						
						result.add(Direction.RIGHT);
						mPosition.set(
								x2 * mRoom.getTileWidth() - halfSize.getX(),
								mPosition.getY());
						break;
					}
				}
			} else if (deltaX < 0) {
				for (int y = y1n; y <= y2n; y++) {
					if (mRoom.isCollidable(x1, y)) {
						
						deltaX = 0;
						
						result.add(Direction.LEFT);
						mPosition.set((x1 + 1) * mRoom.getTileWidth()
								+ halfSize.getX(), mPosition.getY());
						break;
					}
				}
			}

			mPosition.add(0, deltaY);
			int y1 = (int) ((mPosition.getY() - halfSize.getY()) / mRoom.getTileHeight());
			int y2 = (int) ((mPosition.getY() + halfSize.getY()) / mRoom.getTileHeight());
			int x1n = (int) ((mPosition.getX() - halfSize.getX() + 0.01f) / mRoom
					.getTileWidth());
			int x2n = (int) ((mPosition.getX() + halfSize.getX() - 0.01f) / mRoom
					.getTileWidth());

			if (deltaY > 0) {
				for (int x = x1n; x <= x2n; x++) {
					if (mRoom.isCollidable(x, y2)) {
						
						deltaY = 0;
						
						result.add(Direction.DOWN);
						mPosition.set(mPosition.getX(), y2 * mRoom.getTileHeight() - halfSize.getY());
						break;
					}
				}
			} else if (deltaY < 0) {
				for (int x = x1n; x <= x2n; x++) {
					if (mRoom.isCollidable(x, y1)) {
						
						deltaY = 0;
						
						result.add(Direction.UP);
						mPosition.set(mPosition.getX(), (y1 + 1) * mRoom.getTileHeight()
								+ halfSize.getY());
					}
				}
			}
		}

		return result;
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

	// virtual void onStartWallOfDeath();
	public void onStartWallOfDeath() {

	}

	//
	// virtual void remove();
	public void remove() {
		mRemoved = true;
	}

	// virtual void setPosition(float2 position);
	public void setPosition(AbstractFloatPair position) {
		mPosition.set(position);
	}

	//
	// virtual void setRoom(Room *room);
	public void setRoom(Room room) {
		mRoom = room;
	}

	// virtual void setSize(float2 size);
	public void setSize(ImmutableFloatPair size) {
		mSize = size;
		mHalfSize = size.multiply(0.5f);
	}

	// virtual void setVelocity(float2 velocity);
	public void setVelocity(AbstractFloatPair velocity) {
		mVelocity.set(velocity);
	}

	// virtual void update();
	public void update() {
		mFrameCounter++;
	}

}
