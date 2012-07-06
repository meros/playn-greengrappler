package com.meros.playn.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import playn.core.Surface;
import playn.core.Color;

public class Room {

	Layer myBackgroundLayer;
	Layer myMiddleLayer;
	Layer myForegroundLayer;
	Layer myBackgroundLayerBackup;
	Layer myMiddleLayerBackup;
	Layer myForegroundLayerBackup;

	Hero mHero = null;
	boolean myHeroIsDead = false;
	int myFrameCounter = 0;

	Camera mCamera;
	float2 mCameraTopLeft = new float2();
	float2 mCameraBottomRight = new float2();

	boolean myIsCompleted = false;

	Font myFont = Resource.getFont("data/images/font.bmp");

	Set<Entity> mEntities = new HashSet<Entity>();
	Set<Entity> mDamagableEntities = new HashSet<Entity>();
	Set<Entity> mHookableEntities = new HashSet<Entity>();

	public Room(Layer aBackgroundLayer,		  
			Layer aMiddleLayer,		   
			Layer aForegroundLayer)
	{
		myBackgroundLayer = aBackgroundLayer;
		myMiddleLayer = aMiddleLayer;
		myForegroundLayer = aForegroundLayer;
	}

	public float getTileWidth() {
		return 10.0f;
	}

	public float getTileHeight() {
		return 10.0f;
	}

	public boolean isCollidable(int x, int y) {
		return myMiddleLayer.getTile(x, y).getCollide();
	}

	public boolean isHookable(int x, int y) {
		return myMiddleLayer.getTile(x, y).getHook();
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

	public boolean damageDone(int aX, int aY) {
		for (Entity entity : mDamagableEntities)
		{
			Entity.CollisionRect rect = entity.new CollisionRect();
			rect.myTopLeft = new float2(aX, aY);
			rect.myBottomRight = new float2(aX+1, aY+1);


			if (Entity.Collides(rect, entity.getCollisionRect()))
			{
				entity.onDamage();
				return true;
			}
		}

		return false;
	}

	public Entity findHookableEntity(float2 mRopePosition) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onDraw(Surface aBuffer) {
		if (mHero.isDead())
		{
			if (myFrameCounter == 0 || myFrameCounter == 5 || myFrameCounter == 10)
				aBuffer.setFillColor(Color.rgb(231,215,156));
			else
				aBuffer.setFillColor(Color.rgb(57,56,41));

			aBuffer.fillRect(0, 0, 320, 240);

			if (myFrameCounter < 60)
				mHero.draw(aBuffer, (int)mCamera.getOffset().x, (int)mCamera.getOffset().y, 0);
			return;
		}


		myBackgroundLayer.draw(aBuffer, (int)mCamera.getOffset().x, (int)mCamera.getOffset().y);
		myMiddleLayer.draw(aBuffer, (int)mCamera.getOffset().x, (int)mCamera.getOffset().y);

		for (int layer  = 0; layer < 5; layer++)
		{
			for (Entity entity : mEntities)
			{
				if (entity.getLayer() != layer)
					continue;

				entity.draw(aBuffer, (int)mCamera.getOffset().x, (int)mCamera.getOffset().y, 0);
			}
		}

		myForegroundLayer.draw(aBuffer, (int)mCamera.getOffset().x, (int)mCamera.getOffset().y);

		if (myIsCompleted)
		{
			myFont.drawCenter(aBuffer, "LEVEL COMPLETED!", 0, 0, 320, 240);
		}
	}

	public boolean isCompleted() {
		if (myIsCompleted && myFrameCounter > 60 * 4)
		{
			Music.stop();
			Input.enable();
			return true;
		}

		return false;
	}

	public void setCompleted()
	{
		for (Entity entity : mEntities)
		{
			entity.onLevelComplete();
		}

		myFrameCounter = 0;
		myIsCompleted = true;
		mHero.imortal();
		Input.disable();
		Music.stop();
	}

	public void createTileBackup()
	{
		//TODO: deep copy!?
		myBackgroundLayerBackup = myBackgroundLayer;
		myMiddleLayerBackup = myMiddleLayer;
		myForegroundLayerBackup = myForegroundLayer;
	}

	public void restoreTileBackup()
	{
		//TODO: deep copy!?
		myBackgroundLayer = myBackgroundLayerBackup;
		myMiddleLayer = myMiddleLayerBackup;
		myForegroundLayer = myForegroundLayerBackup;
	}

	public void broadcastBoosFloorActivate()
	{
		for (Entity entity : mEntities)
		{
			entity.onBossFloorActivate();
		}
	}

	public void broadcastBoosWallActivate()
	{
		for (Entity entity : mEntities)
		{
			entity.onBossWallActivate();
		}
	}

	public void broadcastBoosWallDectivate()
	{
		for (Entity entity : mEntities)
		{
			entity.onBossWallDeactivate();
		}
	}


	public void onLogic() {
		myFrameCounter++;

		if (myIsCompleted && myFrameCounter == 60)
			Music.playSong("data/music/level_completed.xm");

		if (mHero.isDead() && !myHeroIsDead)
		{
			myFrameCounter = 0;
			myHeroIsDead = true;
			return;
		}

		if (mHero.isDead() && myFrameCounter > 120)
		{
			mHero.respawn();

			Iterator<Entity> it = mEntities.iterator();
			while (it.hasNext())
			{
				it.next().onRespawn();
			}

			getCamera().centerToHero(mHero);
			getCamera().onRespawn();
			myHeroIsDead = false;

		}

		if (mHero.isDead())
		{
			return;
		}

		Set<Entity> entitiesToRemove = new HashSet<Entity>();

		for (Entity entity : mEntities)
		{
			if (entity.isRemoved())
			{
				if (entity.isDamagable())
				{
					mDamagableEntities.remove(entity);
				}

				if (entity.isHookable())
				{
					mHookableEntities.remove(entity);
				}

				entitiesToRemove.add(entity);
			}
		}

		mEntities.removeAll(entitiesToRemove);

		Set<Entity> entToUpdate = new HashSet<Entity>(mEntities);
		for(Entity entity : entToUpdate)
		{	
			if (mHero.isDead())
				break;

			if (entity != mHero)
				entity.update();
		}

		mHero.update();

		for (Entity entity : mEntities)
		{
			if (entity.getPosition().x - entity.getHalfSize().x > getWidthInTiles()*getTileWidth() ||
					entity.getPosition().y - entity.getHalfSize().y > getHeightInTiles()*getTileHeight() ||
					entity.getPosition().x + entity.getHalfSize().x < 0 ||
					entity.getPosition().y + entity.getHalfSize().y < 0)
			{			
				entity.remove();
			}

		}

		getCamera().onLogic(getHero(), mCameraTopLeft, mCameraBottomRight);
	}

	public float getHeightInTiles() {
		return myMiddleLayer.getHeight();
	}

	public float getWidthInTiles() {
		return myMiddleLayer.getWidth();
	}

	public Camera getCamera()
	{
		return mCamera;
	}

	public void setCamera(Camera aCamera)
	{
		mCamera = aCamera;
	}

	public void setCameraRect( float2 aTopLeft, float2 aBottomRight )
	{
		mCameraTopLeft = aTopLeft;
		mCameraBottomRight = aBottomRight;
	}

	public Set<Entity> getDamagableEntities()
	{
		return mDamagableEntities;
	}

	public Set<Entity> getHookableEntities()
	{
		return mHookableEntities;
	}

	public void broadcastStartWallOfDeath()
	{
		for (Entity entity : mEntities)
			entity.onStartWallOfDeath();
	}

	public void setHookable(int aX, int aY, boolean aHook) {
		myMiddleLayer.getTile(aX, aY).setHook(aHook);

	}

	public void setCollidable(int aX, int aY, boolean aCollide) {
		myMiddleLayer.getTile(aX, aY).setCollide(aCollide);
	}

	public boolean rayCast(float2 origin, float2 direction, boolean cullBeyondDirection,
			float2 out) {
		if (direction.isZero())
		{
			return false;
		}

		int ix = (int) ((int)Math.floor(origin.x) / getTileWidth());
		int iy = (int) ((int)Math.floor(origin.y) / getTileHeight());
		float dx = (origin.x / getTileWidth() - ix);
		float dy = (origin.y / getTileHeight() - iy);
		float tx = direction.x > 0 ? 1 : 0;
		float ty = direction.y > 0 ? 1 : 0;

		int minX = 0;
		int maxX = (int) getWidthInTiles();
		int minY = 0;
		int maxY = (int) getHeightInTiles();
		if ( cullBeyondDirection ) {
			minX = Math.max(minX, ix + (int)(Math.min(0.0f, direction.x) / getTileWidth() - 1.0f));
			maxX = Math.min(maxX, ix + (int)(Math.max(0.0f, direction.x) / getTileWidth() + 2.0f));
			minY = Math.max(minY, iy + (int)(Math.min(0.0f, direction.y) / getTileHeight() - 1.0f));
			maxY = Math.min(maxY, iy + (int)(Math.max(0.0f, direction.y) / getTileHeight() + 2.0f));
		}
		boolean hit = false;
		while (ix >= minX && ix < maxX && iy >= minY && iy < maxY )
		{
			if ( isCollidable(ix, iy) ) {
				hit = true;
				break;
			}

			if ( direction.y == 0 || (direction.x != 0 && ((tx-dx)/direction.x < (ty-dy) / direction.y)) )
			{
				dy += direction.y * (tx - dx) / direction.x;
				if ( direction.x > 0 )
				{
					ix++;
					dx = 0;
				} else {
					ix--;
					dx = 1;
				}
			} else {
				dx += direction.x * (ty - dy) / direction.y;
				if ( direction.y > 0 )
				{
					iy++;
					dy = 0;
				} else {
					iy--;
					dy = 1;
				}
			}
		}
		if ( hit ) {
			out.x = ix;
			out.y = iy;
			float2 tileCenter = new float2(ix * getTileWidth() + getTileWidth() / 2, iy * getTileHeight() + getTileHeight() / 2);
			if ( cullBeyondDirection && origin.subtract(tileCenter).lengthCompare(direction) > 0 ) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

}

