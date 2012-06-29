package com.meros.playn.core;

import playn.core.Canvas;

public abstract class Entity {
	//C++interface
	//	protected:
	//		float2 mPosition;
	//		float2 mVelocity;
	//		float2 mSize;
	//		bool mRemoved;
	//		int mFrameCounter;
	//		Room *mRoom;

	float2 mPosition = new float2();
	float2 mVelocity = new float2();
	float2 mSize = new float2();
	boolean mRemoved = false;
	int mFrameCounter = 0;
	Room mRoom;

	//
	//	public:
	//		Entity();
	//		virtual ~Entity();
	//
	//		struct  CollisionRect
	//		{
	//			float2 myTopLeft;
	//			float2 myBottomRight;
	//		};
	//		static bool Collides(const CollisionRect& aRect1, CollisionRect& aRect2);
	//
	//		virtual void setPosition(float2 position);
	//		virtual float2 getPosition();
	public void setPosition(float2 position)
	{
		mPosition = (float2) position.clone();
	}
	
	public float2 getPosition()
	{
		return (float2) mPosition.clone();
	}
	
	//		virtual int getDrawPositionX();
	public int getDrawPositionX()
	{
		return (int) (getPosition().x + 0.5);
	}
	//		virtual int getDrawPositionY();
	public int getDrawPositionY()
	{
		return (int) (getPosition().y + 0.5);
	}
	
	//		virtual int getLayer() = 0;
	public abstract int getLayer();
	
	//		virtual void setVelocity(float2 velocity);
	public void setVelocity(float2 velocity)
	{
		mVelocity = (float2) velocity.clone();
	}
	//		virtual float2 getVelocity();
	public float2 getVelocity()
	{
		return (float2) mVelocity.clone();
	}
	
	//		virtual void setSize(float2 size);
	public void setSize(float2 size)
	{
		mSize = (float2) size.clone();
	}
	//		virtual float2 getSize();
	public float2 getSize()
	{
		return (float2) mSize.clone();
	}
	//		virtual float2 getHalfSize();
	public float2 getHalfSize()
	{
		return (float2) mSize.multiply(0.5f);
	}
	
	//
	//		virtual CollisionRect getCollisionRect();
	//
	//		virtual void setRoom(Room *room);
	//		virtual Room *getRoom();
	//
	//		virtual unsigned int moveWithCollision(float2 delta);
	//		virtual unsigned int moveWithCollision();
	//
	//		virtual void update();
	public void update()
	{
		
	}
	
	//
	//		virtual void draw(BITMAP *buffer, int offsetX, int offsetY, int layer);
	public void draw(Canvas buffer, int offsetX, int offsetY, int layer)
	{
		int x = getDrawPositionX() + offsetX;
		int y = getDrawPositionY() + offsetY;
		int x1 = (int)(x - getHalfSize().x);
		int y1 = (int)(y - getHalfSize().y);
		int x2 = (int)(x + getHalfSize().x);
		int y2 = (int)(y + getHalfSize().y);
		buffer.strokeRect(x1, y1, x2-x1, y2-y1);
		buffer.drawLine(x - 3, y, x + 3, y);
		buffer.drawLine(x, y - 3, x, y + 3);
	}
	
	//
	//		virtual void remove();
	//		virtual bool isRemoved();
	//		virtual bool isDamagable();
	//		virtual bool isHookable();
	//		virtual void onDamage();
	//		virtual void onButtonUp(int aId);
	//		virtual void onButtonDown(int aId);
	//		virtual void onRespawn();
	//		virtual void onLevelComplete();
	//		virtual void onStartWallOfDeath();
	//		virtual void onBossFloorActivate();
	//		virtual void onBossWallActivate();
	//		virtual void onBossWallDeactivate();


}
