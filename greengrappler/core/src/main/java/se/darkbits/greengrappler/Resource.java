package se.darkbits.greengrappler;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.log;

import java.util.HashMap;
import java.util.Map;

import playn.core.CanvasImage;
import playn.core.Color;
import playn.core.Image;
import playn.core.PlayN;
import playn.core.ResourceCallback;
import se.darkbits.greengrappler.media.Animation;
import se.darkbits.greengrappler.media.Font;


public class Resource {

	static Map<String, Image> mBitmaps = new HashMap<String, Image>();

	static Map<String, Image> mPreloadedImages = new HashMap<String, Image>();
	static Map<String, String> mPreloadedTexts = new HashMap<String, String>();
	static Map<String, Animation> myAnimations = new HashMap<String, Animation>();

	static Map<String, Font> myFonts = new HashMap<String, Font>();

	public static Animation getAnimation(String filename) {
		String key = filename;
		if (!myAnimations.containsKey(key)) {
			Animation animation = new Animation(filename);
			myAnimations.put(key, animation);
		}

		return myAnimations.get(key);
	}

	public static Animation getAnimation(String filename, int aFrames) {
		String key = filename + aFrames;
		if (!myAnimations.containsKey(key)) {
			Animation animation = new Animation(filename, aFrames);
			myAnimations.put(key, animation);
		}

		return myAnimations.get(key);
	}

	private static int getb(int color) {
		return (color) & 0xFF;
	}

	public static Image getBitmap(String filename) {
		return getBitmap(filename, Color.rgb(255, 255, 255));
	}

	public static Image getBitmap(String filename, int color) {
		String key = filename + color;

		if (!mBitmaps.containsKey(key)) {
			Image image = mPreloadedImages.get(filename);

			log().debug("Fixing " + filename + ", is ready: " + image.isReady());

			CanvasImage bitmap = graphics().createImage(image.width(),
					image.height());

			int colorR = getr(color);
			int colorG = getg(color);
			int colorB = getb(color);
			int magicPink = Color.rgb(255, 0, 255);

			int rgb[] = new int[1];

			for (int y = 0; y < bitmap.height(); ++y) {
				for (int x = 0; x < bitmap.width(); ++x) {
					image.getRgb(x, y, 1, 1, rgb, 0, 1);
					int c = rgb[0];
					if (c != magicPink) {

						int r = getr(c);
						int g = getg(c);
						int b = getb(c);

						r = (colorR * r) / 255;
						g = (colorG * g) / 255;
						b = (colorB * b) / 255;
						c = Color.rgb(r, g, b);

						bitmap.canvas().setFillColor(c);
						bitmap.canvas().fillRect(x, y, 1, 1);
					}
				}
			}

			mBitmaps.put(key, bitmap);
			return bitmap;
		}

		return mBitmaps.get(key);
	}

	static public Font getFont(String filename) {
		return getFont(filename, Color.rgb(255, 255, 255));
	}

	static public Font getFont(String filename, int textColor) {
		String key = filename + textColor;
		if (!myFonts.containsKey(key)) {
			Font font = new Font(getBitmap(filename, textColor), ' ', 'z');

			myFonts.put(key, font);
		}

		return myFonts.get(key);
	}

	private static int getg(int color) {
		return (color >> 8) & 0xFF;
	}

	private static int getr(int color) {
		return (color >> 16) & 0xFF;
	}

	public static String getText(String aFilename) {
		return mPreloadedTexts.get(aFilename);
	}

	// static void init();
	public static void init() {
	}

	protected static void injectText(String filename, String resource) {
		mPreloadedTexts.put(filename, resource);
	}

	public static boolean isDonePreloading() {
		return assets().getPendingRequestCount() == 0;
	}

	public static void preLoad(String filename) {
		mPreloadedImages.put(filename, assets().getImage(filename));
	}

	public static void preLoadSound(String sound) {
		PlayN.log().debug("Preloading " + sound);
		assets().getSound(sound);
	}

	public static void preLoadText(final String filename) {
		assets().getText(filename, new ResourceCallback<String>() {

			@Override
			public void done(String resource) {
				Resource.injectText(filename, resource);
			}

			@Override
			public void error(Throwable err) {
			}

		});
	}

	// static BITMAP* getBitmap(const std::string& filename, unsigned int
	// color);
	// static BITMAP* getBitmap(const std::string& filename);
	// static Animation* getAnimation(const std::string& filename, int aFrames);
	// static Animation* getAnimation(const std::string& filename);
	// static SAMPLE* getSample(const std::string& filename);
	// static Font* getFont(const std::string& filename, unsigned int
	// textColor);
	// static Font* getFont(const std::string& filename);
	// static std::string getRealFilename(const std::string& filename);
	//
	// private:
	// Resource();
	// static std::map<std::string, BITMAP*> mBitmaps;
	// static std::map<std::string, Font*> mFonts;
	// static std::map<std::string, Animation*> myAnimations;
	// static std::map<std::string, SAMPLE*> mSamples;
}
