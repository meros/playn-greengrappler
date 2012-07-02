package com.meros.playn.core;

import java.util.ArrayList;

import playn.core.Canvas;
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

	ArrayList<Entity> mEntities = new ArrayList<Entity>();
	ArrayList<Entity> mDamagableEntities = new ArrayList<Entity>();
	ArrayList<Entity> mHookableEntities = new ArrayList<Entity>();

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


		privDrawLayer(aBuffer, myBackgroundLayer);
		privDrawLayer(aBuffer, myMiddleLayer);

		for (int layer  = 0; layer < 5; layer++)
		{
			for (int i = 0; i < (int)mEntities.size(); i++)
			{
				if (mEntities.get(i).getLayer() != layer)
					continue;

				mEntities.get(i).draw(aBuffer, (int)mCamera.getOffset().x, (int)mCamera.getOffset().y, 0);
			}
		}

		privDrawLayer(aBuffer, myForegroundLayer);

		if (myIsCompleted)
		{
			myFont.drawCenter(aBuffer, "LEVEL COMPLETED!", 0, 0, 320, 240);
		}
	}

	private void privDrawLayer(Canvas aBuffer, Layer myForegroundLayer2) {
		// TODO Auto-generated method stub

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
		for (int i = 0; i < mEntities.size(); i++)
			mEntities.get(i).onLevelComplete();

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
		for (int i = 0; i < mEntities.size(); i++)
			mEntities.get(i).onBossFloorActivate();
	}

	public void broadcastBoosWallActivate()
	{
		for (int i = 0; i < mEntities.size(); i++)
			mEntities.get(i).onBossWallActivate();
	}

	public void broadcastBoosWallDectivate()
	{
		for (int i = 0; i < mEntities.size(); i++)
			mEntities.get(i).onBossWallDeactivate();
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
			for (int i = 0; i < mEntities.size(); i++)
			{
				mEntities.get(i).onRespawn();
			}

			getCamera().centerToHero(mHero);
			getCamera().onRespawn();
			myHeroIsDead = false;

		}

		if (mHero.isDead())
		{
			return;
		}

		for (int i = 0; i < (int)mEntities.size(); i++)
		{
			if (mEntities.get(i).isRemoved())
			{
				if (mEntities.get(i).isDamagable())
				{
					for (int j = 0; j < mDamagableEntities.size(); j++)
					{
						if (mDamagableEntities.get(j) == mEntities.get(j))
						{
							mDamagableEntities.remove(j);
							break;
						}
					}
				}

				if (mEntities.get(i).isHookable())
				{
					for (int j = 0; j < mHookableEntities.size(); j++)
					{
						if (mHookableEntities.get(j) == mEntities.get(i))
						{
							mHookableEntities.remove(j);
							break;
						}
					}
				}

				mEntities.remove(i);
				i--;
			}
		}

		for (int i = 0; i < (int)mEntities.size(); i++)
		{
			if (mHero.isDead())
				break;

			if (mEntities.get(i) != mHero)
				mEntities.get(i).update();
		}

		mHero.update();

		for (int i = 0; i < (int)mEntities.size(); i++)
		{
			Entity entity = mEntities.get(i);
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

	private float getHeightInTiles() {
		return myMiddleLayer.getHeight();
	}

	private float getWidthInTiles() {
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

	public ArrayList<Entity> getDamagableEntities()
	{
		return mDamagableEntities;
	}

	public ArrayList<Entity> getHookableEntities()
	{
		return mHookableEntities;
	}

	public void broadcastStartWallOfDeath()
	{
		for (int i = 0; i < mEntities.size(); i++)
			mEntities.get(i).onStartWallOfDeath();
	}

}

