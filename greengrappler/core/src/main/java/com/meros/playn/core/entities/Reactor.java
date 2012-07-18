package com.meros.playn.core.entities;

import java.util.Random;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.CollisionRect;
import com.meros.playn.core.Entity;
import com.meros.playn.core.Resource;
import com.meros.playn.core.Room;
import com.meros.playn.core.Sound;
import com.meros.playn.core.ImmutableFloatPair;

public class Reactor extends Entity {

	int BLOW_TIME = (60 * 4 + 30);
	int DAMAGE_MAX = 16;
	int FRAME_PER_DAMAGE = 4;

	boolean myAboutToBlow = false;
	int myDamage = 0;
	int myFrameCounter = 0;

	Animation myShell = Resource.getAnimation("data/images/reactor_shell.bmp",
			4);

	public Reactor() {
		setSize(new ImmutableFloatPair(30, 40));
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer) {
		int x = getDrawPositionX() + offsetX;
		int y = getDrawPositionY() + offsetY;

		if (myDamage >= DAMAGE_MAX)
			myShell.drawFrame(buffer, myDamage / FRAME_PER_DAMAGE - 1, x
					- (int) getSize().getX() / 2, y - (int) getSize().getY() / 2);
		else
			myShell.drawFrame(buffer, myDamage / FRAME_PER_DAMAGE, x
					- (int) getSize().getX() / 2, y - (int) getSize().getY() / 2);
	}

	@Override
	public CollisionRect getCollisionRect() {
		CollisionRect rect = new CollisionRect();
		rect.myTopLeft = getPosition().subtract(getHalfSize()).subtract(
				new ImmutableFloatPair(2, 2));
		rect.myBottomRight = getPosition().add(getHalfSize()).add(
				new ImmutableFloatPair(2, 2));
		return rect;
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public boolean isDamagable() {
		return true;
	}

	@Override
	public void onDamage() {
		if (myDamage >= DAMAGE_MAX)
			return;

		// 2,
		// 30,
		// 10,
		// 1,
		// 50,
		// 20,
		// -normalize(mRopeVelocity)*10,
		// 1.0);

		Random random = new Random();
		int numPs = random.nextInt() % 5 + 1;
		ParticleSystem ps = new ParticleSystem(Resource.getAnimation(
				"data/images/debris.bmp", 4), 10, 40, 10, 1, 50, numPs,
				new ImmutableFloatPair(0.0f, -20.0f), 2.0f);
		ps.setPosition(getPosition(), 10.0f, false);
		mRoom.addEntity(ps);

		myDamage++;
		if (myDamage == DAMAGE_MAX) {
			myFrameCounter = 1;
			myAboutToBlow = true;

			Sound.playSample("data/sounds/damage");
			mRoom.getCamera().addShake(4.0f, BLOW_TIME);
		} else {
			Sound.playSample("data/sounds/damage");
			mRoom.getCamera().addShake(1.0f, 20);
		}
	}

	@Override
	public void onRespawn() {
		myAboutToBlow = false;
		myDamage = 0;
	}

	@Override
	public void setRoom(Room room) {
		super.setRoom(room);
		setTilesCollidable(true);
	}

	public void setTilesCollidable(boolean aCollidable) {
		int sx = (int) ((getPosition().getX() - getHalfSize().getX()) / 10);
		int sy = (int) ((getPosition().getY() - getHalfSize().getY()) / 10);

		for (int x = sx; x < sx + 3; x++)
			for (int y = sy; y < sy + 4; y++)
				mRoom.setCollidable(x, y, aCollidable);
	}

	@Override
	public void update() {
		myFrameCounter++;

		if (myAboutToBlow && myFrameCounter % 60 == 0)
			Sound.playSample("data/sounds/alarm");

		if (myAboutToBlow && myFrameCounter % 20 == 0) {
			Random random = new Random();
			int numPs = random.nextInt() % 5 + 1;

			ParticleSystem ps = new ParticleSystem(Resource.getAnimation(
					"data/images/debris.bmp", 4), 10, 40, 10, 1, 50, numPs,
					new ImmutableFloatPair(0.0f, -20.0f), 2.0f);
			ps.setPosition(getPosition(), 10.0f, false);
			mRoom.addEntity(ps);
		}

		if (myAboutToBlow && myFrameCounter >= BLOW_TIME) {
			Sound.playSample("data/sounds/reactor_explosion");
			Sound.playSample("data/sounds/start");
			ParticleSystem ps = new ParticleSystem(Resource.getAnimation(
					"data/images/debris.bmp", 4), 20, 200, 20, 1, 50, 50,
					new ImmutableFloatPair(0.0f, -150.0f), 5.0f);
			ps.setPosition(getPosition(), 10.0f, false);
			mRoom.addEntity(ps);

			// Coin::SpawnDeathCoins(10, getPosition(), 0, mRoom);

			ReactorCore core = new ReactorCore();
			core.setPosition(getPosition());
			core.setVelocity(new ImmutableFloatPair(0, -50));
			mRoom.addEntity(core);

			setTilesCollidable(false);
			remove();
		}
	}

}
