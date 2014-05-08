// Copyright 2014 Sebastian Kuerten
//
// This file is part of android-remote.
//
// android-remote is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// android-remote is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with android-remote. If not, see <http://www.gnu.org/licenses/>.

package de.topobyte.androidremote;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Util
{
	private static String CMD_SCREENCAP = "adb shell screencap -p";
	private static String CMD_TAP = "adb shell input tap %d %d";
	private static String CMD_SWIPE = "adb shell input swipe %d %d %d %d %d";
	private static String CMD_TEXT = "adb shell input text %s";
	private static String CMD_KEYEVENT = "adb shell input keyevent %d";

	public static boolean haveAdbInPath()
	{
		try {
			Runtime.getRuntime().exec("adb");
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	private static byte[] readFully(InputStream is) throws IOException
	{
		BufferedInputStream bis = new BufferedInputStream(is);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		boolean lastWasR = false;
		while (true) {
			int b = bis.read();
			if (b < 0) {
				break;
			}
			if (lastWasR) {
				if (b == '\n') {
					baos.write('\n');
					lastWasR = false;
				} else {
					baos.write('\r');
					if (b != '\r') {
						lastWasR = false;
						baos.write(b);
					}
				}
				continue;
			}
			if (b == '\r') {
				lastWasR = true;
				continue;
			}
			baos.write(b);
		}
		return baos.toByteArray();
	}

	public static byte[] getScreenshot() throws IOException
	{
		Process process = Runtime.getRuntime().exec(CMD_SCREENCAP);
		InputStream is = process.getInputStream();
		byte[] bytes = readFully(is);
		is.close();
		return bytes;
	}

	public static void sendTap(int x, int y) throws IOException
	{
		String cmd = String.format(CMD_TAP, x, y);
		System.out.println(cmd);
		Runtime.getRuntime().exec(cmd);
	}

	public static void sendSwipe(int x, int y, int x2, int y2, int duration)
			throws IOException
	{
		String cmd = String.format(CMD_SWIPE, x, y, x2, y2, duration);
		System.out.println(cmd);
		Runtime.getRuntime().exec(cmd);
	}

	public static void sendText(String text) throws IOException
	{
		String cmd = String.format(CMD_TEXT, text);
		System.out.println(cmd);
		Runtime.getRuntime().exec(cmd);
	}

	public static void sendKeyEvent(int key) throws IOException
	{
		String cmd = String.format(CMD_KEYEVENT, key);
		System.out.println(cmd);
		Runtime.getRuntime().exec(cmd);
	}
}
