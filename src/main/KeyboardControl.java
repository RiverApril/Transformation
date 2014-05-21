package main;

import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;

public class KeyboardControl {

	private static boolean pressed[];
	private static boolean released[];
	private static boolean down[];
	private static int time[];
	private static char keyChars[];

	private static boolean doesRepeat[];

	public static int lastKey;
	public static char lastChar;
	public static boolean lastState;

	private static ArrayList<Integer> pressedList = new ArrayList<Integer>();
	private static ArrayList<Integer> releasedList = new ArrayList<Integer>();

	public static boolean inputModeText = false;

	public static ArrayList<KeyBind> ketBindList = new ArrayList<KeyBind>();

	public static KeyBind keyMoveForward = new KeyBind("Move Forward", Keyboard.KEY_W, ketBindList);
	public static KeyBind keyMoveBackward = new KeyBind("Move Backward", Keyboard.KEY_S, ketBindList);
	public static KeyBind keyMoveLeft = new KeyBind("Move Left", Keyboard.KEY_A, ketBindList);
	public static KeyBind keyMoveRight = new KeyBind("Move Right", Keyboard.KEY_D, ketBindList);
	public static KeyBind keyMoveUp = new KeyBind("Move Up", Keyboard.KEY_SPACE, ketBindList);
	public static KeyBind keyMoveDown = new KeyBind("Move Down", Keyboard.KEY_LSHIFT, ketBindList);
	public static KeyBind keyRotateLeft = new KeyBind("Rotate Left", Keyboard.KEY_Q, ketBindList);
	public static KeyBind keyRotateRight = new KeyBind("Rotate Right", Keyboard.KEY_E, ketBindList);
	
	public static KeyBind keyEditX = new KeyBind("Edit X", Keyboard.KEY_R, ketBindList);
	public static KeyBind keyEditY = new KeyBind("Edit Y", Keyboard.KEY_F, ketBindList);
	public static KeyBind keyEditZ = new KeyBind("Edit Z", Keyboard.KEY_V, ketBindList);
	
	public static ArrayList<KeyBind> hiddenKeyBindList = new ArrayList<KeyBind>();

	public static KeyBind keyEscape = new KeyBind("Escape", Keyboard.KEY_ESCAPE, hiddenKeyBindList);
	public static KeyBind keyReturn = new KeyBind("Return", Keyboard.KEY_RETURN, hiddenKeyBindList);
	public static KeyBind keyControl = new KeyBind("Control", Keyboard.KEY_LCONTROL, hiddenKeyBindList);
	public static KeyBind keySpace = new KeyBind("Space", Keyboard.KEY_SPACE, hiddenKeyBindList);
	public static KeyBind keyV = new KeyBind("V", Keyboard.KEY_V, hiddenKeyBindList);
	
	public static KeyBind keyToggleThirdPerson = new KeyBind("Third Person", Keyboard.KEY_F5, hiddenKeyBindList);

	public static void init() throws LWJGLException {

		Keyboard.create();
		Keyboard.enableRepeatEvents(false);

		int n = Keyboard.KEYBOARD_SIZE;// .getKeyCount();

		pressed = new boolean[n];
		released = new boolean[n];
		down = new boolean[n];
		time = new int[n];
		keyChars = new char[n];

		doesRepeat = new boolean[n];

		for (int i = 0; i < n; i++) {
			doesRepeat[i] = false;
		}

		doesRepeat[Keyboard.KEY_BACK] = true;

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

		while (Keyboard.next()) {

			lastState = Keyboard.getEventKeyState();
			lastChar = Keyboard.getEventCharacter();
			lastKey = Keyboard.getEventKey();

			keyChars[lastKey] = lastChar;

			try {
				int i = lastKey;
				boolean d = Keyboard.isKeyDown(i);
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

		for (int i = 0; i < down.length; i++) {
			if (down[i]) {
				time[i] += 1;
				if ((time[i] > 30) && ((time[i] % 3) == 0) && doesRepeat[i] && inputModeText) {
					pressed[i] = true;
					pressedList.add(i);
				}
			} else {
				time[i] = 0;
			}
		}
	}

	private static boolean pressed(int i) {
		if (inputModeText) {
			return false;
		}
		return pressedIgnoreMode(i);
	}

	public static boolean pressedActualKey(int i) {
		return pressed(i);
	}

	public static boolean pressed(KeyBind bind) {
		return pressed(bind.key);
	}

	private static boolean released(int i) {
		if (inputModeText) {
			return false;
		}
		return releasedIgnoreMode(i);
	}

	public static boolean releasedActualKey(int i) {
		return released(i);
	}

	public static boolean released(KeyBind bind) {
		return released(bind.key);
	}

	private static boolean down(int i) {
		if (inputModeText) {
			return false;
		}
		return downIgnoreMode(i);
	}

	public static boolean downActualKey(int i) {
		return down(i);
	}

	public static boolean down(KeyBind bind) {
		return down(bind.key);
	}

	public static boolean pressedIgnoreMode(int i) {
		if (i > pressed.length) {
			return false;
		}
		return pressed[i];
	}

	public static boolean releasedIgnoreMode(int i) {
		if (i > released.length) {
			return false;
		}
		return released[i];
	}

	public static boolean downIgnoreMode(int i) {
		if (i > down.length) {
			return false;
		}
		return down[i];
	}

	public static boolean isUsableChar(int key) {
		return !((key == Keyboard.KEY_LSHIFT) || (key == Keyboard.KEY_RSHIFT) || (key == Keyboard.KEY_LCONTROL) || (key == Keyboard.KEY_RCONTROL) || (key == Keyboard.KEY_LMENU) || (key == Keyboard.KEY_RMENU) || (key == Keyboard.KEY_ESCAPE) || (key == Keyboard.KEY_LEFT) || (key == Keyboard.KEY_RIGHT) || (key == Keyboard.KEY_UP) || (key == Keyboard.KEY_DOWN) || (key == Keyboard.KEY_CAPITAL) || (key == Keyboard.KEY_ESCAPE) || (key == Keyboard.KEY_DELETE) || (key == Keyboard.KEY_F1) || (key == Keyboard.KEY_F2) || (key == Keyboard.KEY_F3) || (key == Keyboard.KEY_F4) || (key == Keyboard.KEY_F5) || (key == Keyboard.KEY_F6) || (key == Keyboard.KEY_F7) || (key == Keyboard.KEY_F8) || (key == Keyboard.KEY_F9) || (key == Keyboard.KEY_F10) || (key == Keyboard.KEY_F11) || (key == Keyboard.KEY_F12) || (key == Keyboard.KEY_F13) || (key == Keyboard.KEY_F14) || (key == Keyboard.KEY_F15));
	}

	public static void resetKey(int key) {
		pressed[key] = false;
		released[key] = false;
		down[key] = false;
		time[key] = 0;
	}

	public static char getChar(int key) {
		return keyChars[key];
	}

	public static boolean[] pressed() {
		return pressed;
	}

	public static boolean[] down() {
		return down;
	}

	public static boolean[] released() {
		return released;
	}

}
