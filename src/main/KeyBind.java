package main;

import java.util.ArrayList;

public class KeyBind {

	public String name = "";
	public int key = 0;

	public KeyBind(String name, int key, ArrayList<KeyBind> ketBindList) {
		this.name = name;
		this.key = key;
		ketBindList.add(this);
	}

}
