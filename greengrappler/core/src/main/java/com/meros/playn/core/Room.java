package com.meros.playn.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import playn.core.Color;
import playn.core.Surface;

import com.meros.playn.core.entities.Hero;
import com.meros.playn.core.entities.ParticleSystem;

public class Room {
	private final Camera mCamera;
	Set<Entity> mDamagableEntities = new HashSet<Entity>();
	Set<Entity> mEntities = new HashSet<Entity>();
	Set<Entity> mEntitiesToAdd = new HashSet<Entity>();
	Hero mHero = null;

	Set<Entity> mHookableEntities = new HashSet<Entity>();
	Layer myBackgroundLayer;

	Font myFont = Resource.getFont("data/images/font.bmp");
	Layer myForegroundLayer;
	boolean myHookableArray[][];
	boolean myCollidableArray[][];

	int myFrameCounter = 0;

	boolean myHeroIsDead = false;

	boolean myIsCompleted = false;
	Layer myMiddleLayer;
	private int myDestroyedToTileRow = -1;

	public Room(
			Layer aBackgroundLayer, 
			Layer aMiddleLayer,
			Layer aForegroundLayer,
			Camera aCamera) {

		myBackgroundLayer = aBackgroundLayer;
		myMiddleLayer = aMiddleLayer;
		myForegroundLayer = aForegroundLayer;

		mCamera = aCamera;

		myHookableArray = new boolean[getWidthInTiles()][getHeightInTiles()];
		myCollidableArray = new boolean[getWidthInTiles()][getHeightInTiles()];

		for (int x = 0; x < getWidthInTiles(); x++)
		{
			for (int y = 0; y < getHeightInTiles(); y++)
			{
				myHookableArray[x][y] = myMiddleLayer.getTile(x, y).getHook();
				myCollidableArray[x][y] = myMiddleLayer.getTile(x, y).getCollide();
			}
		}
	}

	public void addEntity(Entity aEntity) {
		if (aEntity instanceof Hero) {
			mHero = (Hero) aEntity;
		}

		aEntity.setRoom(this);
		mEntitiesToAdd.add(aEntity);
	}

	public void broadcastBoosFloorActivate() {
		for (Entity entity : mEntities) {
			entity.onBossFloorActivate();
		}
	}

	public void broadcastBoosWallActivate() {
		for (Entity entity : mEntities) {
			entity.onBossWallActivate();
		}
	}

	public void broadcastBoosWallDectivate() {
		for (Entity entity : mEntities) {
			entity.onBossWallDeactivate();
		}
	}

	public void broadcastStartWallOfDeath() {
		for (Entity entity : mEntities)
			entity.onStartWallOfDeath();
	}

	public boolean damageDone(int aX, int aY) {
		for (Entity entity : mDamagableEntities) {
			CollisionRect rect = new CollisionRect();
			rect.myTopLeft = new float2(aX, aY);
			rect.myBottomRight = new float2(aX + 1, aY + 1);

			if (rect.Collides(entity.getCollisionRect())) {
				entity.onDamage();
				return true;
			}
		}

		return false;
	}

	public Entity findHookableEntity(float2 position) {
		for (Entity entity : mHookableEntities)
		{
			float2 toEntity = entity.getPosition().subtract(position);
			if (Math.abs(toEntity.x) < entity.getHalfSize().x && Math.abs(toEntity.y) < entity.getHalfSize().y) {
				return entity;
			}
		}

		return null;
	}

	public Camera getCamera() {
		return mCamera;
	}

	public Set<Entity> getDamagableEntities() {
		return mDamagableEntities;
	}

	public int getHeightInTiles() {
		return myMiddleLayer.getHeight();
	}

	public Hero getHero() {
		return mHero;
	}

	public Set<Entity> getHookableEntities() {
		return mHookableEntities;
	}

	public float getTileHeight() {
		return 10.0f;
	}

	public float getTileWidth() {
		return 10.0f;
	}

	public int getWidthInTiles() {
		return myMiddleLayer.getWidth();
	}

	public boolean isCollidable(int aX, int aY) {
		if (aX < 0 || aX >= myCollidableArray.length || aY < 0 || aY >= myCollidableArray[aX].length)
			return false;
		
		if (aX < myDestroyedToTileRow)
			return false;

		return myCollidableArray[aX][aY];
	}

	public boolean isCompleted() {
		if (myIsCompleted && myFrameCounter > 60 * 4) {
			Music.stop();
			Input.enable();
			return true;
		}

		return false;
	}

	public boolean isHookable(int aX, int aY) {
		if (aX < 0 || aX >= myHookableArray.length || aY < 0 || aY >= myHookableArray[aX].length)
			return false;
		
		if (aX < myDestroyedToTileRow)
			return false;

		return myHookableArray[aX][aY];
	}

	public void onDraw(Surface aBuffer) {
		if (mHero.isDead()) {
			if (myFrameCounter == 0 || myFrameCounter == 5
					|| myFrameCounter == 10)
				aBuffer.setFillColor(Color.rgb(231, 215, 156));
			else
				aBuffer.setFillColor(Color.rgb(57, 56, 41));

			aBuffer.fillRect(0, 0, 320, 240);

			if (myFrameCounter < 60)
				mHero.draw(aBuffer, (int) mCamera.getOffset().x,
						(int) mCamera.getOffset().y, 0);
			return;
		}

		Layer[] bgLayers = {myMiddleLayer, myForegroundLayer};
		myBackgroundLayer.draw(aBuffer, (int) mCamera.getOffset().x,
				(int) mCamera.getOffset().y, bgLayers);
		Layer[] midLayers = {myForegroundLayer};
		myMiddleLayer.draw(aBuffer, (int) mCamera.getOffset().x,
				(int) mCamera.getOffset().y, midLayers);

		List<Entity> entitiesToDraw = new ArrayList<Entity>();
		entitiesToDraw.addAll(mEntities);
		Collections.sort(entitiesToDraw, new Comparator<Entity>() {
			@Override
			public int compare(Entity aEnt1, Entity aEnt2) {
				if (aEnt1.getLayer() != aEnt2.getLayer())
					return aEnt1.getLayer() - aEnt2.getLayer();

				return aEnt1.getClass().getName().compareTo(aEnt2.getClass().getName());
			}
		});

		for (Entity entity: entitiesToDraw) {
			entity.draw(aBuffer, (int) mCamera.getOffset().x,
					(int) mCamera.getOffset().y, 0);
		}

		Layer[] fgLayers = {};
		myForegroundLayer.draw(aBuffer, (int) mCamera.getOffset().x,
				(int) mCamera.getOffset().y, fgLayers);

		if (myIsCompleted) {
			myFont.drawCenter(aBuffer, "LEVEL COMPLETED!", 0, 0, 320, 240);
		}
	}

	public void onLogic() {
		myFrameCounter++;

		for (Entity entity : mEntitiesToAdd)
		{
			if (entity.isDamagable())
				mDamagableEntities.add(entity);

			if (entity.isHookable())
				mHookableEntities.add(entity);
		}
		mEntities.addAll(mEntitiesToAdd);
		mEntitiesToAdd.clear();


		if (myIsCompleted && myFrameCounter == 60)
			Music.playSong("data/music/level_completed.xm");

		if (mHero.isDead() && !myHeroIsDead) {
			myFrameCounter = 0;
			myHeroIsDead = true;
			return;
		}

		if (mHero.isDead() && myFrameCounter > 120) {
			mHero.respawn();

			Iterator<Entity> it = mEntities.iterator();
			while (it.hasNext()) {
				it.next().onRespawn();
			}

			getCamera().centerToHero(mHero);
			getCamera().onRespawn();
			myHeroIsDead = false;
		}

		if (mHero.isDead()) {
			return;
		}

		Set<Entity> itemsToRemove = null;

		for(Entity entity : mEntities)
		{
			if (entity.isRemoved()) {
				if (itemsToRemove == null)
				{
					itemsToRemove = new HashSet<Entity>();
				}

				itemsToRemove.add(entity);
			}
		}

		if (itemsToRemove != null)
		{
			mEntities.removeAll(itemsToRemove);
			mHookableEntities.removeAll(itemsToRemove);
			mDamagableEntities.removeAll(itemsToRemove);
		}

		for (Entity entity : mEntities) {
			if (mHero.isDead())
				break;

			if (entity != mHero)
				entity.update();
		}

		mHero.update();

		for (Entity entity : mEntities) {
			if (entity.getPosition().x - entity.getHalfSize().x > getWidthInTiles()
					* getTileWidth()
					|| entity.getPosition().y - entity.getHalfSize().y > getHeightInTiles()
					* getTileHeight()
					|| entity.getPosition().x + entity.getHalfSize().x < 0
					|| entity.getPosition().y + entity.getHalfSize().y < 0) {
				entity.remove();
			}

		}

		getCamera().update(getHero());
	}

	public class OutInt
	{
		public int myInt;
	}

	public boolean rayCast(float2 origin, float2 direction,
			boolean cullBeyondDirection, OutInt aOutX, OutInt aOutY) {
		if (direction.isZero()) {
			return false;
		}

		int ix = (int) ((int) Math.floor(origin.x) / getTileWidth());
		int iy = (int) ((int) Math.floor(origin.y) / getTileHeight());
		float dx = (origin.x / getTileWidth() - ix);
		float dy = (origin.y / getTileHeight() - iy);
		float tx = direction.x > 0 ? 1 : 0;
		float ty = direction.y > 0 ? 1 : 0;

		int minX = 0;
		int maxX = getWidthInTiles();
		int minY = 0;
		int maxY = getHeightInTiles();
		if (cullBeyondDirection) {
			minX = Math.max(minX,
					ix
					+ (int) (Math.min(0.0f, direction.x)
							/ getTileWidth() - 1.0f));
			maxX = Math.min(maxX,
					ix
					+ (int) (Math.max(0.0f, direction.x)
							/ getTileWidth() + 2.0f));
			minY = Math.max(minY,
					iy
					+ (int) (Math.min(0.0f, direction.y)
							/ getTileHeight() - 1.0f));
			maxY = Math.min(maxY,
					iy
					+ (int) (Math.max(0.0f, direction.y)
							/ getTileHeight() + 2.0f));
		}
		boolean hit = false;
		while (ix >= minX && ix < maxX && iy >= minY && iy < maxY) {
			if (isCollidable(ix, iy)) {
				hit = true;
				break;
			}

			if (direction.y == 0
					|| (direction.x != 0 && ((tx - dx) / direction.x < (ty - dy)
							/ direction.y))) {
				dy += direction.y * (tx - dx) / direction.x;
				if (direction.x > 0) {
					ix++;
					dx = 0;
				} else {
					ix--;
					dx = 1;
				}
			} else {
				dx += direction.x * (ty - dy) / direction.y;
				if (direction.y > 0) {
					iy++;
					dy = 0;
				} else {
					iy--;
					dy = 1;
				}
			}
		}
		if (hit) {
			aOutX.myInt = ix;
			aOutY.myInt = iy;
			float2 tileCenter = new float2(ix * getTileWidth() + getTileWidth()
					/ 2, iy * getTileHeight() + getTileHeight() / 2);
			if (cullBeyondDirection
					&& origin.subtract(tileCenter).lengthCompare(direction) > 0) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public void setCollidable(int aX, int aY, boolean aCollide) {
		if (aX < 0 || aX >= myCollidableArray.length || aY < 0 || aY >= myCollidableArray[aX].length)
			return;

		myCollidableArray[aX][aY] = aCollide;
	}

	public void setCompleted() {
		for (Entity entity : mEntities) {
			entity.onLevelComplete();
		}

		myFrameCounter = 0;
		myIsCompleted = true;
		mHero.imortal();
		Input.disable();
		Music.stop();
	}

	public void setHookable(int aX, int aY, boolean aHook) {
		if (aX < 0 || aX >= myHookableArray.length || aY < 0 || aY >= myHookableArray[aX].length)
			return;

		myHookableArray[aX][aY] = aHook;
	}

	public void broadcastButtonUp(int aId) {
		for (Entity entity : mEntities)
		{
			entity.onButtonUp(aId);
		}
	}

	public void broadcastButtonDown(int aId) {
		for (Entity entity : mEntities)
		{
			entity.onButtonDown(aId);
		}
	}

	public void destroyToTileRow(int aX) {
		myDestroyedToTileRow = aX;
		
		for (int y = 0; y < myMiddleLayer.getHeight(); y++)
		{
			boolean spawnDebris = myMiddleLayer.getTile(aX, y).getCollide();
			if (spawnDebris) {
				ParticleSystem ps = new ParticleSystem(Resource.getAnimation("data/images/debris.bmp", 4), 20, 100, 20, 1, 50, 3, new float2(0.0f, -50.0f), 5.0f);
				ps.setPosition(new float2(aX * getTileWidth() + getTileWidth() * 0.75f, y * getTileHeight() + getTileHeight() * 0.75f));
				addEntity(ps);
			}
		}

		myMiddleLayer.setDestroyedToTileRow(aX);
		myForegroundLayer.setDestroyedToTileRow(aX);
	}

	public Set<Entity> getEntities() {
		return mEntities;
	}

}
