package com.meros.playn.core;

import java.util.HashMap;
import java.util.Map;

import playn.core.Canvas;
import playn.core.Image;

public class Font {

	Map<String, Image> myGlyphToBitmap = new HashMap<String, Image>();
	int myHeight = 0;

	public Font(Image aGlyphImage, char aStartChar, char aEndChar) {
		String glyphs = "";
		for (char theChar = aStartChar; theChar <= aEndChar; theChar++) {
			glyphs += theChar;
		}

		privInit(aGlyphImage, glyphs);
	}

	// class Font
	// {
	// public:
	// Font(BITMAP *aGlyphImage, const std::string& aGlyphs);
	// Font(BITMAP *aGlyphImage, char aStartChar, char aEndChar);
	//
	// void draw( BITMAP* aBuffer, const std::string& aText, int aX, int aY,
	// Blending aBlending = Blending_None);
	// void drawWrap(BITMAP* aBuffer, const std::string& aText, int aX, int aY,
	// int aMaxWidth = -1, int aNumberOfCharacters = -1);
	// void drawCenter(BITMAP* aBuffer, const std::string& aText, int aX, int
	// aY, int aWidth, int aHeight);
	// void drawCenterY(BITMAP* aBuffer, const std::string& aText, int aX, int
	// aY, int aHeight);
	// void drawCenterYAlignRight(BITMAP* aBuffer, const std::string& aText, int
	// aX, int aY, int aHeight);
	// void drawGlyph(BITMAP* aBuffer, char aChar, int aX, int aY);
	//
	// unsigned int getWidth(const std::string& aText);
	// unsigned int getHeight() const;
	// private:
	// void privInit(BITMAP *aGlyphImage, const std::string& aGlyphs);
	// BITMAP* getBitmapForGlyph(const char glyph);
	// unsigned int myHeight;
	// std::map<char, BITMAP*> myGlyphToBitmap;
	//
	// };

	void privInit(Image aGlyphImage, String aGlyphs) {
		Image glyphImage = aGlyphImage;
		int separatingColor = getpixel(glyphImage, 0, 0);

		int x = 0;
		int scanLine = 0;
		int currGlyphIndex = 0;
		int lastRowHeight = 0;
		char specialGlyph = (char) 200;

		while (scanLine < glyphImage.height()) {
			x = 0;
			while (x < glyphImage.width()) {
				int color = getpixel(glyphImage, x, scanLine);

				if (color != separatingColor) // glyph found!
				{
					int y1 = scanLine;
					while ((y1 < glyphImage.height())
							&& getpixel(glyphImage, x, y1) != separatingColor) // find
																				// bottom
																				// of
																				// glyph
					{
						y1++;
					}
					int x1 = x;
					while ((x1 < glyphImage.width())
							&& getpixel(glyphImage, x1, scanLine) != separatingColor) // find
																						// right
																						// edge
																						// of
																						// glyph
					{
						x1++;
					}

					int width = x1 - x;
					int height = y1 - scanLine;
					lastRowHeight = height;

					char currGlyph = aGlyphs.length() <= currGlyphIndex ? specialGlyph++
							: aGlyphs.charAt(currGlyphIndex++);
					Image bitmap = glyphImage.subImage(x, scanLine, width,
							height);
					myGlyphToBitmap.put("" + currGlyph, bitmap);

					x = x1;
				}
				x += 1;
			}
			scanLine += lastRowHeight + 1;
		}
		if (!myGlyphToBitmap.containsKey("" + '\n')
				&& myGlyphToBitmap.containsKey("" + ' '))
			myGlyphToBitmap.put("" + '\n', myGlyphToBitmap.get("" + ' ')); // have
																			// something
																			// to
																			// draw
																			// when
																			// newline

		myHeight = lastRowHeight;
	}

	private int getpixel(Image glyphImage, int x, int y) {
		int rgb[] = new int[1];
		glyphImage.getRgb(x, y, 1, 1, rgb, 0, 1);
		return rgb[0];
	}

	public void draw(Canvas aBuffer, String aText, int aX, int aY) {
		int x = aX;
		int y = aY;

		// TODO: blending?? bool addBlend = aBlending == Blending_Add;
		// setBlending(aBlending);

		for (int i = 0; i < aText.length(); i++) {
			Image bitmap = getBitmapForGlyph(aText.charAt(i));
			// TODO: no blending implemented!
			// if (aBlending == Blending_None)
			// {
			aBuffer.drawImage(bitmap, x, y);
			// }
			// else if (addBlend)
			// {
			// draw_trans_sprite(aBuffer, bitmap, x, y);
			// }
			// else
			// {
			// draw_lit_sprite(aBuffer, bitmap, x, y, 0);
			// }
			x += bitmap.width();
		}
	}

	private Image getBitmapForGlyph(char glyph) {
		Image defaultBitmap = myGlyphToBitmap.get("" + ' '); // TODO make bitmap
		Image bitmap = defaultBitmap;
		if (myGlyphToBitmap.containsKey("" + glyph)) {
			bitmap = myGlyphToBitmap.get("" + glyph);
		}
		return bitmap;
	}

	public void drawCenter(Canvas aBuffer, String aText, int aX, int aY,
			int aWidth, int aHeight) {
		int x = aX + aWidth / 2 - getWidth(aText) / 2;
		int y = aY + aHeight / 2 - getHeight() / 2;

		for (int i = 0; i < aText.length(); i++) {
			Image bitmap = getBitmapForGlyph(aText.charAt(i));
			aBuffer.drawImage(bitmap, x, y);
			x += bitmap.width();
		}
	}

	private int getHeight() {
		return myHeight;
	}

	private int getWidth(String aText) {
		int width = 0;
		for (int i = 0; i < aText.length(); i++) {
			Image bitmap = getBitmapForGlyph(aText.charAt(i));
			width += bitmap.width();

		}
		return width;
	}
}
