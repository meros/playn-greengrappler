package com.meros.playn.core;

public class Camera {

	float2 mOffset = new float2();
	float2 mShakeOffset = new float2();

	int myShakeTime = 0;
	float myShakeAmount = 0.0f;


	//ArrayList<Pair<float2, float2>> myRects = new ArrayList<Pair<float2, float2>>(); 

	public float2 getOffset() {
		return new float2((int)(mOffset.x + mShakeOffset.x+0.5), (int)(mOffset.y + mShakeOffset.y+0.5));
	}

	public void addShake(float aAmount, int aShakeTime)
	{
		myShakeAmount = aAmount;
		myShakeTime = aShakeTime;
	}

	public void centerToHero(Hero aHero) {
		float2 heropos = new float2(-aHero.getDrawPositionX() + 320/2, -aHero.getDrawPositionY() + (2*240)/3);
		mOffset = heropos;
	}

	public void onRespawn() {
		myShakeTime = 0;
		myShakeAmount = 0.0f;
	}

	public void onLogic(Hero aHero, float2 aTopLeft,
			float2 aBottomRight) {
		boolean foundRect = false;
		float2 desiredOffset = new float2();
		

		//float2 heroRealPos = aHero.getPosition();
		//TODO:		for (int i = 0; i < myRects.size(); i++)
		//		{
		//			float2 topleft = myRects.get(i).first;
		//			float2 bottomright = myRects.get(i).second;
		//
		//			if (topleft.x < heroRealPos.x && topleft.y <heroRealPos.y &&
		//				bottomright.x > heroRealPos.x && bottomright.y > heroRealPos.y)
		//			{
		//				desiredOffset = -topleft;
		//				desiredOffset.y += 10;
		//				foundRect = true;
		//			}
		//		}

		if (!foundRect) {
			float2 heropos = new float2(-aHero.getDrawPositionX() + 320/2, -aHero.getDrawPositionY() + (2*240)/3);

			desiredOffset = heropos;
		}

		mOffset = mOffset.add((desiredOffset.subtract(mOffset)).multiply((float) 0.1));


		myShakeTime--;

		if (myShakeTime > 0)
		{
			mShakeOffset.x = (float) (myShakeAmount*(Math.random()-0.5f));
			mShakeOffset.y = (float) (myShakeAmount*(Math.random()-0.5f));
		}
		if (myShakeTime < 0)
		{
			mShakeOffset = new float2();
		}

		if (mOffset.x+aTopLeft.x > 0)
		{
			mOffset.x = -aTopLeft.x;
		}

		if (mOffset.y+aTopLeft.y > 10)
		{
			mOffset.y = 10-aTopLeft.y;
		}

		if (mOffset.x+aBottomRight.x < 320)
		{
			mOffset.x = 320-aBottomRight.x;
		}

		if (mOffset.y+aBottomRight.y < 240)
		{
			mOffset.y = 240-aBottomRight.y;
		}	
	}

}
