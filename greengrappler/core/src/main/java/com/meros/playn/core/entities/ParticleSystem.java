package com.meros.playn.core.entities;

import java.util.ArrayList;
import java.util.Random;

import playn.core.Surface;

import com.meros.playn.core.Animation;
import com.meros.playn.core.Entity;
import com.meros.playn.core.FloatPair;
import com.meros.playn.core.ImmutableFloatPair;
import com.meros.playn.core.Time;
import com.meros.playn.core.UtilMethods;

public class ParticleSystem extends Entity {

	private Animation myAnimation;
	private int myAnimationSpeed;
	private int myBlinkTimeTicks;
	private int myFrameNum = 0;
	private ImmutableFloatPair myGravity;
	private FloatPair myInitialVel;
	private int myLifeTimeTicks;
	private int myMaxSpeed;
	private int myMinSpeed;
	private int myNumParticles;
	private ArrayList<FloatPair> myParticlesPos = new ArrayList<FloatPair>();
	private ArrayList<FloatPair> myParticlesVel = new ArrayList<FloatPair>();

	public ParticleSystem(Animation aAnimation, int aAnimationSpeed,
			int aLifeTime, int aBlinkTime, int aMinSpeed, int aMaxSpeed,
			int aNumParticles, ImmutableFloatPair aInitialVel, float aGravity) {
		myAnimation = aAnimation;
		myAnimationSpeed = aAnimationSpeed;
		myLifeTimeTicks = aLifeTime;
		myBlinkTimeTicks = aBlinkTime;
		myMinSpeed = aMinSpeed;
		myMaxSpeed = aMaxSpeed;
		myNumParticles = aNumParticles;
		myInitialVel = new FloatPair(aInitialVel);
		myGravity = new ImmutableFloatPair(0, aGravity/Time.TicksPerSecond);

		setSize(new ImmutableFloatPair(30, 30));
	}

	@Override
	public void draw(Surface aBuffer, int aOffsetX, int aOffsetY, int aLayer) {
		if (myLifeTimeTicks < myBlinkTimeTicks && myLifeTimeTicks % 2 == 0)
			return;

		Random random = new Random(0);
		for (int i = 0; i < myParticlesPos.size(); i++) {
			myAnimation.drawFrame(aBuffer, myFrameNum / myAnimationSpeed
					+ random.nextInt(), (int) (myParticlesPos.get(i).getX()
							+ aOffsetX - myAnimation.getFrameWidth() / 2),
							(int) (myParticlesPos.get(i).getY() + aOffsetY - myAnimation
									.getFrameHeight() / 2));
		}
	}

	@Override
	public int getLayer() {
		return 3;
	}

	@Override
	public void onRespawn() {
		remove();
	}

	@Override
	public void setPosition(ImmutableFloatPair position) {
		setPosition(position, 0.0f, false);
	}

	public void setPosition(ImmutableFloatPair position, float randomAmount,
			boolean randomVel) {
		super.setPosition(position);

		for (int i = 0; i < myNumParticles; i++) {
			FloatPair pos = 
					new FloatPair(getPosition()).add(new ImmutableFloatPair(
							(float) ((Math.random() * 2.0f - 1.0f) * randomAmount),
							(float) ((Math.random() * 2.0f - 1.0f) * randomAmount)));

			myParticlesPos.add(pos);

			float r0710 = (float) (Math.random() * 0.3 + 0.7);

			FloatPair velocity = randomVel ? new FloatPair(1, 1).multiply(r0710)
					: new FloatPair(UtilMethods.sincos(2 * 3.1415 * Math.random()));

			float factor = (float) (Math.random() * (myMaxSpeed - myMinSpeed) + myMinSpeed);

			velocity = velocity.multiply(factor);

			velocity = velocity.add(myInitialVel).divide(Time.TicksPerSecond);

			myParticlesVel.add(velocity);
		}
	}

	@Override
	public void update() {
		myFrameNum++;
		if (myLifeTimeTicks > 0) {
			for (int i = 0; i < myParticlesVel.size(); i++) {
				myParticlesPos.get(i).add(myParticlesVel.get(i));
				myParticlesVel.get(i).add(myGravity);
			}

			myLifeTimeTicks--;
		} else {
			remove();
		}
	}

}
