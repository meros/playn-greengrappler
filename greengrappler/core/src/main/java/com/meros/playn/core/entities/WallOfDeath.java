package com.meros.playn.core.entities;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Entity;
import com.meros.playn.core.PlayerSkill;
import com.meros.playn.core.Resource;
import com.meros.playn.core.Sound;
import com.meros.playn.core.UtilMethods;
import com.meros.playn.core.float2;

public class WallOfDeath extends Entity {

	private boolean myInited = false;
	private boolean myBoost = false;
	private float mySpeed = 0.0f;
	private int myFrameCounter = 0;
	private boolean myRunning = false;
	private float mySoundCountdown = 10.0f;
	private Animation mySaw = Resource.getAnimation("data/images/saw.bmp");
	private float2 myOriginalPosition;

	public WallOfDeath()
	{
		setSize(new float2(10, 12 * 100));
	}

	@Override
	public void draw(Surface buffer, int offsetX, int offsetY, int layer )
	{
		int x = (int) (getDrawPositionX() + offsetX - getSize().x / 2);
		int y = (int) (getDrawPositionY() + offsetY - getSize().y / 2);

		int saws = (int)getSize().y / 12;

		for (int i = 0; i < saws; i++)
		{
			mySaw.drawFrame(buffer, myFrameCounter / 5 + i, x, y + i * 12);
		}
		//Entity::draw(buffer, offsetX, offsetY, layer);
	}

	@Override
	public void update()
	{
		myFrameCounter++;

		if (!myInited)
		{
			myOriginalPosition = getPosition();
			myInited = true;
		}

		if (!myRunning)
			return;

		if (getPosition().x > 2950.0f) {
			ParticleSystem ps = new ParticleSystem(mySaw, 2, 100, 20, 10, 20, 1, new float2(0.0f, -50.0f), 4.0f);

			int x = (int) (getDrawPositionX() - getSize().x / 2);
			int y = (int) (getDrawPositionY() - getSize().y / 2);
			int saws = (int)getSize().y / 12;

			for (int i = 0; i < saws; i++)
			{
				ps.setPosition(new float2(x, y + i * 12), 0.1f, true);
			}

			mRoom.addEntity(ps);
			remove();
			return;
		}

		for (int xo = 0; xo < 3; xo++) {
				int xt = xo + (int)((getPosition().x - getSize().x / 2) / mRoom.getTileWidth());
				mRoom.destroyToTileRow(xt);
		}

		float heroX = mRoom.getHero().getPosition().x;
		float x = getPosition().x;

		float distance = heroX - x;
		if (distance > 190 && !myBoost)
		{
			setPosition(new float2(heroX - 190, getPosition().y));
		}

		if (distance < 80)
		{
			myBoost = false;
		}

		if (myBoost)
		{
			mySpeed = (int) UtilMethods.lerp(5.0f, 9.0f, PlayerSkill.get());
		}
		else
		{
			float wantedSpeed = UtilMethods.lerp(1.0f, 1.9f, PlayerSkill.get());
			mySpeed = UtilMethods.lerp(mySpeed, wantedSpeed, 0.05f);
		}

		mySoundCountdown -= mySpeed;
		if (mySoundCountdown <= 0.0f) {
			mySoundCountdown += 10.0f;
			Sound.playSample("data/sounds/damage");
		}


		setPosition(new float2(getPosition().x + mySpeed, getPosition().y));

		if (mRoom.getHero().Collides(this))
		{
			mRoom.getHero().kill();
		}
	}

	@Override
	public void onRespawn()
	{
		mRoom.destroyToTileRow(-1);
		setPosition(myOriginalPosition);
		myRunning = false;
		myBoost = false;
		mySoundCountdown = 10.0f;
	}

	@Override
	public void onStartWallOfDeath()
	{
		if (!myRunning) {
			myRunning = true;
		} else {
			myBoost = true;
		}
	}
	
	@Override
	public int getLayer() {
		return 1;
	}

}
