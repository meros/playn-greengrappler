package com.meros.playn.core;

import java.util.ArrayList;
import java.util.Random;

import playn.core.Surface;

public class ParticleSystem extends Entity {



	private Animation myAnimation;
	private int myLifeTimeTicks;
	private int myBlinkTimeTicks;
	private int myMinSpeed;
	private int myMaxSpeed;
	private int myNumParticles;
	private float2 myInitialVel;
	private float myGravity;
	private ArrayList<float2> myParticlesPos = new ArrayList<float2>();
	private ArrayList<float2> myParticlesVel = new ArrayList<float2>();
	private int myAnimationSpeed;
	private int myFrameNum = 0;

	public ParticleSystem(Animation aAnimation,
			int aAnimationSpeed,
			int aLifeTime,
			int aBlinkTime,
			int aMinSpeed,
			int aMaxSpeed,
			int aNumParticles,
			float2 aInitialVel,
			float aGravity) {
		myAnimation = aAnimation;
		myAnimationSpeed = aAnimationSpeed;
		myLifeTimeTicks = aLifeTime;
		myBlinkTimeTicks = aBlinkTime;
		myMinSpeed = aMinSpeed;
		myMaxSpeed = aMaxSpeed;
		myNumParticles = aNumParticles;
		myInitialVel = aInitialVel;
		myGravity = aGravity;

		setSize(new float2(30, 30));
	}

	@Override
	public void update()
	{
		myFrameNum ++;
		if (myLifeTimeTicks > 0)
		{
			for (int i = 0; i < myParticlesVel.size(); i++)
			{
				myParticlesPos.set(i, myParticlesPos.get(i).add( myParticlesVel.get(i).divide(Time.TicksPerSecond)));
				myParticlesVel.get(i).y += myGravity;
			}

			myLifeTimeTicks--;
		}
		else
		{
			remove();
		}
	}

	@Override
	public void setPosition(float2 position)
	{
		setPosition(position, 0.0f, false);
	}
	
	public void setPosition(float2 position, float randomAmount, boolean randomVel)
	{
		super.setPosition(position);

		for (int i = 0; i < myNumParticles; i++)
		{
			float2 pos = getPosition();
			pos = pos.add(
					new float2(
							(float)((Math.random() * 2.0f - 1.0f) * randomAmount),
							(float)((Math.random() * 2.0f - 1.0f) * randomAmount)));
			
			myParticlesPos.add(pos);
			
			float r0710 = (float) (Math.random()*0.3+0.7);

			float2 velocity = randomVel ? new float2(1,1).multiply(r0710) : UtilMethods.sincos(2* 3.1415 * Math.random());
			
			float factor = (float) (Math.random()*(myMaxSpeed-myMinSpeed)+myMinSpeed);
			
			velocity = velocity.multiply(factor);

			velocity = velocity.add(myInitialVel);

			myParticlesVel.add(velocity);
		}
	}

	@Override
	public void draw(Surface aBuffer, int aOffsetX, int aOffsetY, int aLayer)
	{
		if (myLifeTimeTicks  < myBlinkTimeTicks  && myLifeTimeTicks % 2 == 0)
			return;

		Random random = new Random(0);
		for (int i = 0; i < myParticlesPos.size(); i++)
		{
			myAnimation.drawFrame(
					aBuffer, 
					myFrameNum/myAnimationSpeed + random.nextInt(), 
					(int)(myParticlesPos.get(i).x + aOffsetX - myAnimation.getFrameWidth()/2), 
					(int)(myParticlesPos.get(i).y + aOffsetY - myAnimation.getFrameHeight()/2));
		}
	}

	@Override
	public int getLayer() {
		return 3;
	}
	
	@Override
	public void onRespawn()
	{
		remove();
	}

}
