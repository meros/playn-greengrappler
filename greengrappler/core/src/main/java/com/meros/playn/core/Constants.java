package com.meros.playn.core;

public class Constants {
	enum Direction
	{
		None(0),
		Left(1),
		Right(2),
		Up(4),
		Down(8);
		
		public int value;
		Direction(int value){
			this.value = value;
		}
	}
	
	enum Buttons
	{
		ToggleFullscreen,
		ToggleSlowMotion,
		ToggleVSync,
		ToggleWideScreen,
		Exit,
		Jump,
		Fire,
		Left,
		Right,
		Up,
		Down,
		ForceQuit
	};
}
