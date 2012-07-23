package com.meros.playn.core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.PlayN;
import playn.core.Surface;
import pythagoras.f.Point;

import com.meros.playn.core.Constants.Buttons;

public class Input implements Keyboard.Listener, playn.core.Touch.Listener {

	static Set<Buttons> mIsHeld = new HashSet<Buttons>();
	static Set<Buttons> mIsPressed = new HashSet<Buttons>();
	static Set<Buttons> mIsReleased = new HashSet<Buttons>();


	//Keyboard
	static Map<Key, Buttons> mKeyMap = new HashMap<Key, Buttons>();


	//Touch
	public interface HitTranslator
	{
		public abstract void translateHit(Point aHitpoint);
	}

	private static HitTranslator myHitTranslator;

	public class TouchArea
	{
		private int myX;
		private int myY;
		private int myW;
		private int myH;
		private Buttons myButton;
		private boolean myIsSticky;

		public TouchArea(int aX, int aY, int aW, int aH, Buttons aButton, boolean aIsSticky)
		{
			myX = aX;
			myY = aY;
			myW = aW;
			myH = aH;
			myButton = aButton;
			myIsSticky = aIsSticky;
		}

		public boolean hits(int aX, int aY)
		{
			return aX >= myX && aX <= myX + myW && aY >= myY && aY <= myY + myH;
		}

		public Buttons getButton()
		{
			return myButton;
		}

		public boolean isSticky()
		{
			return myIsSticky;
		}
	}

	private static Set<TouchArea> myTouchAreas = new HashSet<TouchArea>();

	private class Touch
	{
		Set<TouchArea> myActiveAreas = new HashSet<TouchArea>();

		public Touch(float aX, float aY) {
			onTouchMove(aX, aY);
		}

		private void enter(TouchArea area)
		{
			if (myActiveAreas.contains(area))
				return;

			myActiveAreas.add(area);
			Input.onButtonDown(area.getButton());
		}


		private void exit(TouchArea area) {
			if (!myActiveAreas.contains(area))
				return;

			myActiveAreas.remove(area);
			Input.onButtonUp(area.getButton());			
		}

		public void onTouchEnd() {
			Set<TouchArea> actives = new HashSet<TouchArea>();
			actives.addAll(myActiveAreas);
			for(TouchArea area : actives)
			{
				exit(area);
			}
		}

		public void onTouchMove(float aX, float aY) {
			Point p = new Point(aX, aY);
			myHitTranslator.translateHit(p);

			Set<TouchArea> actives = new HashSet<TouchArea>();
			actives.addAll(myActiveAreas);
			for(TouchArea area : actives)
			{
				if (!area.hits((int)p.x, (int)p.y))
				{
					exit(area);
				}
			}

			for (TouchArea area : myTouchAreas)
			{
				if (area.hits((int)p.x, (int)p.y))
				{
					enter(area);
				}
			}
		}
	}

	static Map<Integer, Touch> mTouches = new HashMap<Integer, Touch>();

	public static void disable() {
		// TODO Auto-generated method stub

	}

	public static void enable() {
		// TODO Auto-generated method stub

	}

	public static void init() {
		PlayN.keyboard().setListener(new Input());
		PlayN.touch().setListener(new Input());

		mKeyMap.put(Key.DOWN, Buttons.Down);
		mKeyMap.put(Key.UP, Buttons.Up);
		mKeyMap.put(Key.LEFT, Buttons.Left);
		mKeyMap.put(Key.RIGHT, Buttons.Right);
		mKeyMap.put(Key.ENTER, Buttons.Fire);
		mKeyMap.put(Key.Z, Buttons.Fire);
		mKeyMap.put(Key.ESCAPE, Buttons.ForceQuit);
		mKeyMap.put(Key.A, Buttons.Jump);
		mKeyMap.put(Key.P, Buttons.Exit);

		Input input = new Input();
		myTouchAreas.add(input.new TouchArea(616+172,465,172,234, Buttons.Fire, true));
		myTouchAreas.add(input.new TouchArea(616,465,172,234, Buttons.Jump, true));
		
		ImmutableFloatPair dpadCenter = new ImmutableFloatPair(165, 555);
		int smallWidth = 160;
		int largeWidth = 400;

		myTouchAreas.add(input.new TouchArea(
				(int) (dpadCenter.getX()-largeWidth/2),
				(int) (dpadCenter.getY()-largeWidth/2),
				smallWidth,
				largeWidth, Buttons.Left, false));
		myTouchAreas.add(input.new TouchArea(
				(int) (dpadCenter.getX()+largeWidth/2-smallWidth),
				(int) (dpadCenter.getY()-largeWidth/2),
				smallWidth,
				largeWidth, Buttons.Right, false));
		myTouchAreas.add(input.new TouchArea(
				(int) (dpadCenter.getX()-largeWidth/2),
				(int) (dpadCenter.getY()-largeWidth/2),
				largeWidth,
				smallWidth, Buttons.Up, false));
		myTouchAreas.add(input.new TouchArea(
				(int) (dpadCenter.getX()-largeWidth/2),
				(int) (dpadCenter.getY()+largeWidth/2-smallWidth),
				largeWidth,
				smallWidth, Buttons.Down, false));
		
		int pauseAreaSize = 100;
		myTouchAreas.add(input.new TouchArea(
				(int) (960-pauseAreaSize),
				(int) (0),
				pauseAreaSize,
				pauseAreaSize, Buttons.Exit, false));
		
	}

	public static boolean isHeld(Buttons aButton) {
		return mIsHeld.contains(aButton);
	}

	public static boolean isPressed(Buttons aButton) {
		return mIsPressed.contains(aButton);
	}

	public static boolean isReleased(Buttons aButton) {
		return mIsReleased.contains(aButton);
	}

	public static void onButtonDown(Buttons aButton) {
		mIsPressed.add(aButton);
		mIsHeld.add(aButton);
	}

	public static void onButtonUp(Buttons aButton) {
		mIsHeld.remove(aButton);
		mIsReleased.add(aButton);
	}

	public static void update() {
		mIsPressed.clear();
		mIsReleased.clear();
	}

	@Override
	public void onKeyDown(Event event) {
		if (mKeyMap.containsKey(event.key())) {
			if (!mIsHeld.contains(mKeyMap.get(event.key()))) {
				event.setPreventDefault(true);
				onButtonDown(mKeyMap.get(event.key()));
			}
		}
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
		//Do nothing
	}

	@Override
	public void onKeyUp(Event event) {
		if (mKeyMap.containsKey(event.key())) {
			event.setPreventDefault(true);
			onButtonUp(mKeyMap.get(event.key()));
		}
	}

	@Override
	public void onTouchEnd(playn.core.Touch.Event[] touches) {
		for (playn.core.Touch.Event event : touches)
		{
			if (mTouches.containsKey(event.id()))
			{
				mTouches.get(event.id()).onTouchEnd();
				mTouches.remove(event.id());
			}
		}
	}

	@Override
	public void onTouchMove(playn.core.Touch.Event[] touches) {
		for (playn.core.Touch.Event event : touches)
		{
			if (mTouches.containsKey(event.id()))
			{
				mTouches.get(event.id()).onTouchMove(event.localX(), event.localY());
			}
		}
	}

	@Override
	public void onTouchStart(playn.core.Touch.Event[] touches) {
		for (playn.core.Touch.Event event : touches)
		{
			mTouches.put(event.id(), new Touch(event.localX(), event.localY()));			
		}
	}

	public static void onDraw(Surface surface)
	{
	}

	public static void setTouchTranslator(HitTranslator aHitTranslator) {
		myHitTranslator = aHitTranslator;
	}
}
