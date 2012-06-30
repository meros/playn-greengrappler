package com.meros.playn.core;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.*;

import java.util.HashMap;
import java.util.Map;

import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Image;

public class Resource {

	static Map<String, Image> mPreloaded = new HashMap<String, Image>();
	static Map<String, Image> mBitmaps = new HashMap<String, Image>();
	static Map<String, Animation> myAnimations = new HashMap<String, Animation>();

	//	    static void init();
	public static void init()
	{
	}

	public static Image getBitmap(String filename)
	{
		return getBitmap(filename, Color.rgb(255, 255, 255));
	}
	
	public static Image getBitmap(String filename, int color)
	{
		String key = filename + color;

		if (!mBitmaps.containsKey(key))
		{	
			Image image = mPreloaded.get(filename);

			CanvasImage bitmap = graphics().createImage(image.width(), image.height());

			int colorR = getr(color);
			int colorG = getg(color);
			int colorB = getb(color);
			int magicPink = Color.rgb(255, 0, 255);
			
			int rgb[] = new int[1];

			for(int y = 0; y < bitmap.height(); ++y) {
				for(int x = 0; x < bitmap.width(); ++x) {
					image.getRgb(x, y, 1, 1, rgb, 0, 1);
					int c = rgb[0];
					if(c != magicPink) {
						int r = getr(c);
						int g = getg(c);
						int b = getb(c);

						r = (colorR * r) / 255;
						g = (colorG * g) / 255;
						b = (colorB * b) / 255;
						c = Color.rgb(r, g, b);

						bitmap.canvas().setStrokeColor(c);
						bitmap.canvas().drawPoint(x, y);
					}
				}
			}

			mBitmaps.put(key, bitmap);
		}

		return mBitmaps.get(key);
	}
	
	public static void preLoad(String filename)
	{
		mPreloaded.put(filename, assets().getImage(filename));
	}
	
	public static boolean isDonePreloading()
	{
		return assets().isDone();
	}
	
	static Animation getAnimation(String filename, int aFrames)
	{
		String key = filename + aFrames;
		if(!myAnimations.containsKey(key)) {
			Animation animation = new Animation(filename, aFrames);
			myAnimations.put(key, animation);
		}

		return myAnimations.get(key);
	}

	private static int getb(int color) {
		return (color) & 0xFF;
	}

	private static int getg(int color) {
		return (color >> 8) & 0xFF;
	}

	private static int getr(int color) {
		return (color >> 16) & 0xFF;
	}

	//	    static BITMAP* getBitmap(const std::string& filename, unsigned int color);
	//		static BITMAP* getBitmap(const std::string& filename);
	//		static Animation* getAnimation(const std::string& filename, int aFrames);
	//		static Animation* getAnimation(const std::string& filename);
	//		static SAMPLE* getSample(const std::string& filename);
	//		static Font* getFont(const std::string& filename, unsigned int textColor);
	//		static Font* getFont(const std::string& filename);
	//		static std::string getRealFilename(const std::string& filename);
	//	    
	//	private:
	//	    Resource();
	//	    static std::map<std::string, BITMAP*> mBitmaps;
	//		static std::map<std::string, Font*> mFonts;
	//		static std::map<std::string, Animation*> myAnimations;
	//		static std::map<std::string, SAMPLE*> mSamples;
}
