package main;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

public class MouseControl {

	public static final int ButtonLeft = 0;
	public static final int ButtonRight = 1;
	
	private static boolean pressed[];
	private static boolean released[];
	private static boolean down[];
	
	public static int lastButton;
	public static boolean lastState;

	private static ArrayList<Integer> pressedList = new ArrayList<Integer>();
	private static ArrayList<Integer> releasedList = new ArrayList<Integer>();

	public static void init() throws LWJGLException {

		Keyboard.create();
		Keyboard.enableRepeatEvents(false);

		int n = Mouse.getButtonCount();

		pressed = new boolean[n];
		released = new boolean[n];
		down = new boolean[n];

	}
	
	public static void update() {

		int size = pressedList.size();
		for (int i = 0; i < size; i++) {
			pressed[pressedList.get(i)] = false;
		}
		size = releasedList.size();
		for (int i = 0; i < size; i++) {
			released[releasedList.get(i)] = false;
		}
		pressedList.clear();
		releasedList.clear();

		while (Mouse.next()) {

			lastState = Mouse.getEventButtonState();
			lastButton = Mouse.getEventButton();

			try {
				int i = lastButton;
				boolean d = Mouse.isButtonDown(i);
				if (d) {
					if (!down[i]) {
						pressed[i] = true;
						pressedList.add(i);
					}
				} else {
					if (down[i]) {
						released[i] = true;
						releasedList.add(i);
					}
				}
				down[i] = d;

			} catch (ArrayIndexOutOfBoundsException e) {

			}
		}
	}
	
	public static boolean pressed(int mouseButton) {
		return pressed[mouseButton];
	}
	
	public static boolean released(int mouseButton) {
		return released[mouseButton];
	}
	
	public static boolean down(int mouseButton) {
		return down[mouseButton];
	}

}
