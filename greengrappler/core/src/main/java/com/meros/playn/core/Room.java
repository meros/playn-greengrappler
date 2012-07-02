package com.meros.playn.core;

import java.util.ArrayList;

import playn.core.Canvas;

public class Room {

	Hero mHero;
	ArrayList<Entity> mEntities = new ArrayList<Entity>();
	ArrayList<Entity> mDamagableEntities = new ArrayList<Entity>();
	ArrayList<Entity> mHookableEntities = new ArrayList<Entity>();

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
	
	public boolean isHookable(int tileX, int tileY) {
		// TODO Auto-generated method stub
		return tileY == 0;
	}

	public Hero getHero() {
		return mHero;
	}

	public void addEntity(Entity aEntity)
	{
		if (aEntity instanceof Hero)
		{
			mHero = (Hero)aEntity;
		}

		aEntity.setRoom(this);
		mEntities.add(aEntity);

		if (aEntity.isDamagable())
			mDamagableEntities.add(aEntity);

		if (aEntity.isHookable())
			mHookableEntities.add(aEntity);
	}

	public boolean damageDone(int i, int j) {
		// TODO Auto-generated method stub
		return false;
	}

	public Entity findHookableEntity(float2 mRopePosition) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onDraw(Canvas aBuffer) {
		// TODO Auto-generated method stub
		
	}

	public boolean isCompleted() {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLogic() {
		// TODO Auto-generated method stub
		
	}

}
