package com.meros.playn.core;

import java.util.ArrayList;

import playn.core.Canvas;
import playn.core.Image;

public class Animation {
	//  C++ interface	
	//	Animation(const std::string& aFilename);
	//	Animation(const std::string& aFilename, int aNumberOfFrames);
	//	~Animation();
	//
	//	BITMAP *getFrame(int aFrame) const;
	//	int getFrameCount() const;
	//	int getFrameWidth() const;
	//	int getFrameHeight() const;
	
	//	//void drawFrame(BITMAP *dest, int frame, int x, int y, bool aHFlip, int aFillColor) const;
	//	void drawRotatedFrame(BITMAP *aBuffer, int aFrame, int aX, int aY, int aAngle, bool aVFlip = false) const;

	int myFrameWidth;
	int myFrameHeight;

	ArrayList<Image> myFrames = new ArrayList<Image>();

	public Animation(String aFilename)
	{
		Image allFrames = Resource.getBitmap(aFilename);

		myFrameWidth = myFrameHeight = (int)allFrames.height();
		int count = (int)allFrames.width() / myFrameWidth;

		privFillFramesList(allFrames, count);	
	}

	public Animation(String aFilename, int aNumberOfFrames)
	{
		Image allFrames = Resource.getBitmap(aFilename);

		myFrameWidth = myFrameHeight = (int)allFrames.height();

		myFrameWidth =  (int)allFrames.width() / aNumberOfFrames;
		myFrameHeight = (int)allFrames.height();
		int count = aNumberOfFrames;

		privFillFramesList(allFrames, count);

	}

	void privFillFramesList(Image aAllFrames, int aCount)
	{
		myFrames.ensureCapacity(aCount);

		int i;
		for (i = 0; i < aCount; i++) 
		{
			Image subImage = aAllFrames.subImage(i*myFrameWidth, 0, myFrameWidth, myFrameHeight);
			myFrames.add(subImage);
		}
	}

	Image getFrame(int aFrame)
	{
		return myFrames.get(Math.abs(aFrame % myFrames.size()));
	}

	public int getFrameWidth() {
		return myFrameWidth;
	}
	
	public int getFrameHeight() {
		return myFrameHeight;
	}
	
	//	void drawFrame(BITMAP *aBuffer, int aFrame, int aX, int aY, bool aHFlip = false, bool aVFlip = false, Blending aBlending = Blending_None) const;
	void drawFrame(Canvas aBuffer, int aFrame, int aX, int aY, boolean aHFlip, boolean aVFlip)
	{
		//TODO: dummy
		aBuffer.save();
		if (aHFlip)
			aBuffer.transform(-1, 0, 0, 1, getFrameWidth()+aX*2, 0);
		if (aVFlip)
			aBuffer.transform(1, 0, 0, -1, 0, getFrameHeight()+aY*2);
			
		aBuffer.drawImage(getFrame(aFrame), aX, aY);
		aBuffer.restore();
	}

	public void drawFrame(Canvas aBuffer, int aFrame, int aX, int aY) {
		drawFrame(aBuffer, aFrame, aX, aY, false, false);
	}
}

