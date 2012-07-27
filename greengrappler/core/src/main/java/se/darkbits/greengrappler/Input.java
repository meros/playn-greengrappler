package se.darkbits.greengrappler;

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
import se.darkbits.greengrappler.Constants.Buttons;
import se.darkbits.greengrappler.floatpair.ImmutableFloatPair;


public class Input implements Keyboard.Listener, playn.core.Touch.Listener {

	private static boolean myEnabled = true;
	static Set<Buttons> myIsHeld = new HashSet<Buttons>();
	static Set<Buttons> myIsPressed = new HashSet<Buttons>();
	static Set<Buttons> myIsReleased = new HashSet<Buttons>();

	// Keyboard
	static Map<Key, Buttons> myKeyMap = new HashMap<Key, Buttons>();

	// Touch
	public interface AbstractHitTranslator {
		public abstract void translateHit(Point aHitpoint);
	}

	private static AbstractHitTranslator myHitTranslator;

	public class TouchArea {
		private int myX;
		private int myY;
		private int myW;
		private int myH;
		private Buttons myButton;
		private boolean myIsSticky;

		public TouchArea(int aX, int aY, int aW, int aH, Buttons aButton,
				boolean aIsSticky) {
			myX = aX;
			myY = aY;
			myW = aW;
			myH = aH;
			myButton = aButton;
			myIsSticky = aIsSticky;
		}

		public boolean hits(int aX, int aY) {
			return aX >= myX && aX <= myX + myW && aY >= myY && aY <= myY + myH;
		}

		public Buttons getButton() {
			return myButton;
		}

		public boolean isSticky() {
			return myIsSticky;
		}
	}

	private static Set<TouchArea> myTouchAreas = new HashSet<TouchArea>();

	private class Touch {
		Set<TouchArea> myActiveAreas = new HashSet<TouchArea>();

		public Touch(float aX, float aY) {
			onTouchMove(aX, aY);
		}

		private void enter(TouchArea area) {
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
			for (TouchArea area : actives) {
				exit(area);
			}
		}

		public void onTouchMove(float aX, float aY) {
			Point p = new Point(aX, aY);
			myHitTranslator.translateHit(p);

			Set<TouchArea> actives = new HashSet<TouchArea>();
			actives.addAll(myActiveAreas);
			for (TouchArea area : actives) {
				if (!area.hits((int) p.x, (int) p.y)) {
					exit(area);
				}
			}

			for (TouchArea area : myTouchAreas) {
				if (area.hits((int) p.x, (int) p.y)) {
					enter(area);
				}
			}
		}
	}

	static Map<Integer, Touch> myTouches = new HashMap<Integer, Touch>();
	public static void init() {
		PlayN.keyboard().setListener(new Input());
		PlayN.touch().setListener(new Input());

		myKeyMap.put(Key.DOWN, Buttons.DOWN);
		myKeyMap.put(Key.UP, Buttons.UP);
		myKeyMap.put(Key.LEFT, Buttons.LEFT);
		myKeyMap.put(Key.RIGHT, Buttons.RIGHT);
		myKeyMap.put(Key.ENTER, Buttons.FIRE);
		myKeyMap.put(Key.Z, Buttons.FIRE);
		myKeyMap.put(Key.ESCAPE, Buttons.FORCE_QUIT);
		myKeyMap.put(Key.A, Buttons.JUMP);
		myKeyMap.put(Key.P, Buttons.EXIT);
		myKeyMap.put(Key.BACK, Buttons.EXIT);

		Input input = new Input();
		myTouchAreas.add(input.new TouchArea(616 + 172, 465, 172, 234,
				Buttons.FIRE, true));
		myTouchAreas.add(input.new TouchArea(616, 465, 172, 234, Buttons.JUMP,
				true));

		ImmutableFloatPair dpadCenter = new ImmutableFloatPair(165, 555);
		int smallWidth = 160;
		int largeWidth = 400;

		myTouchAreas.add(input.new TouchArea(
				(int) (dpadCenter.getX() - largeWidth / 2), (int) (dpadCenter
						.getY() - largeWidth / 2), smallWidth, largeWidth,
				Buttons.LEFT, false));
		myTouchAreas.add(input.new TouchArea((int) (dpadCenter.getX()
				+ largeWidth / 2 - smallWidth),
				(int) (dpadCenter.getY() - largeWidth / 2), smallWidth,
				largeWidth, Buttons.RIGHT, false));
		myTouchAreas.add(input.new TouchArea(
				(int) (dpadCenter.getX() - largeWidth / 2), (int) (dpadCenter
						.getY() - largeWidth / 2), largeWidth, smallWidth,
				Buttons.UP, false));
		myTouchAreas.add(input.new TouchArea(
				(int) (dpadCenter.getX() - largeWidth / 2), (int) (dpadCenter
						.getY() + largeWidth / 2 - smallWidth), largeWidth,
				smallWidth, Buttons.DOWN, false));

		int pauseAreaSize = 100;
		myTouchAreas.add(input.new TouchArea((960 - pauseAreaSize), (0),
				pauseAreaSize, pauseAreaSize, Buttons.EXIT, false));

	}

	public static boolean isHeld(Buttons aButton) {
		if (!myEnabled)
			return false;

		return myIsHeld.contains(aButton);
	}

	public static boolean isPressed(Buttons aButton) {
		if (!myEnabled)
			return false;

		return myIsPressed.contains(aButton);
	}

	public static boolean isReleased(Buttons aButton) {
		if (!myEnabled)
			return false;

		return myIsReleased.contains(aButton);
	}

	public static void onButtonDown(Buttons aButton) {
		myIsPressed.add(aButton);
		myIsHeld.add(aButton);
	}

	public static void onButtonUp(Buttons aButton) {
		myIsHeld.remove(aButton);
		myIsReleased.add(aButton);
	}

	public static void update() {
		myIsPressed.clear();
		myIsReleased.clear();
	}

	@Override
	public void onKeyDown(Event event) {
		if (myKeyMap.containsKey(event.key())) {
			if (!myIsHeld.contains(myKeyMap.get(event.key()))) {
				event.setPreventDefault(true);
				onButtonDown(myKeyMap.get(event.key()));
			}
		}
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
		// Do nothing
	}

	@Override
	public void onKeyUp(Event event) {
		if (myKeyMap.containsKey(event.key())) {
			event.setPreventDefault(true);
			onButtonUp(myKeyMap.get(event.key()));
		}
	}

	@Override
	public void onTouchEnd(playn.core.Touch.Event[] touches) {
		for (playn.core.Touch.Event event : touches) {
			if (myTouches.containsKey(event.id())) {
				myTouches.get(event.id()).onTouchEnd();
				myTouches.remove(event.id());
			}
		}
	}

	@Override
	public void onTouchMove(playn.core.Touch.Event[] touches) {
		for (playn.core.Touch.Event event : touches) {
			if (myTouches.containsKey(event.id())) {
				myTouches.get(event.id()).onTouchMove(event.localX(),
						event.localY());
			}
		}
	}

	@Override
	public void onTouchStart(playn.core.Touch.Event[] touches) {
		for (playn.core.Touch.Event event : touches) {
			myTouches.put(event.id(), new Touch(event.localX(), event.localY()));
		}
	}

	public static void onDraw(Surface surface) {
	}

	public static void setTouchTranslator(AbstractHitTranslator aHitTranslator) {
		myHitTranslator = aHitTranslator;
	}

	public static void enable() {
		myEnabled = true;
	}

	public static void disable() {
		myEnabled = false;
	}
}
