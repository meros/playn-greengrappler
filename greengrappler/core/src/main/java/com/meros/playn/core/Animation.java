package com.meros.playn.core;

import java.util.ArrayList;

import playn.core.Canvas;
import playn.core.Image;

import static playn.core.PlayN.*;

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
	//	void drawFrame(BITMAP *aBuffer, int aFrame, int aX, int aY, bool aHFlip = false, bool aVFlip = false, Blending aBlending = Blending_None) const;
	//	//void drawFrame(BITMAP *dest, int frame, int x, int y, bool aHFlip, int aFillColor) const;
	//	void drawRotatedFrame(BITMAP *aBuffer, int aFrame, int aX, int aY, int aAngle, bool aVFlip = false) const;

	int myFrameWidth;
	int myFrameHeight;

	ArrayList<Image> myFrames = new ArrayList<Image>();

	public Animation(String aFilename)
	{
		Image allFrames = assets().getImage(aFilename);
		
		while (allFrames.isReady())
		{
			
		}
		
		myFrameWidth = myFrameHeight = (int)allFrames.height();
		int count = (int)allFrames.width() / myFrameWidth;

		privFillFramesList(allFrames, count);	
	}

	public Animation(String aFilename, int aNumberOfFrames)
	{
		Image allFrames = assets().getImage(aFilename);
		
		while (allFrames.isReady())
		{
			
		}
		
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
		return myFrames.get(aFrame % myFrames.size());
	}
	
	void drawFrame(Canvas aBuffer, int aFrame, int aX, int aY)
	{
		aBuffer.drawImage(getFrame(aFrame), aX, aY);
	}
}
