package com.meros.playn.core;

import java.util.HashSet;
import java.util.HashMap;

import com.meros.playn.core.Constants.Buttons;

import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;

public class Input implements Keyboard.Listener {

	static HashMap<Buttons, Integer> mIsHeld = new HashMap<Buttons, Integer>();
	static HashSet<Buttons> mIsPressed = new HashSet<Buttons>();
	
	public static void update()
	{
		mIsPressed.clear();
	}
	
	public static boolean isHeld(Buttons aButton) {
		// TODO Auto-generated method stub
		return aButton == Buttons.Right;
	}

	public static boolean isPressed(Buttons aButton) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean isReleased(Buttons aButton) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static void onButtonDown(Buttons aButton)
	{
		
	}
	
	public static void onButtonUp(Buttons aButton)
	{
		
	}

	@Override
	public void onKeyDown(Event event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyTyped(TypedEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onKeyUp(Event event) {
		// TODO Auto-generated method stub
		
	}
}
