package com.fy.engineserver.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fy.engineserver.activity.fairyBuddha.FairyBuddhaManager;

public class TestDDC {
	static List<String> l = new ArrayList<String>();

	public static void main(String[] args) {
		l.add("aa");
		l.add("bb");
		FairyBuddhaManager fbm = new FairyBuddhaManager();
		fbm.disk.put(1, (Serializable) l);
		System.out.println(fbm.disk.get(1));
		l.add("cc");
		System.out.println(fbm.disk.get(1));
		l.clear();
		System.out.println(fbm.disk.get(1));
	}
}
