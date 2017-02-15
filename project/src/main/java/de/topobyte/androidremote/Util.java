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

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

public class Util
{
	private static String CMD_SCREENCAP = "adb shell screencap -p";
	private static String CMD_TAP = "adb shell input tap %d %d";
	private static String CMD_SWIPE = "adb shell input swipe %d %d %d %d";
	private static String CMD_SWIPE_DURATION = "adb shell input swipe %d %d %d %d %d";
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

	public static BufferedImage getScreenshot(IDevice device)
			throws TimeoutException, AdbCommandRejectedException, IOException
	{
		RawImage rawImage = device.getScreenshot();
		System.out.println("bpp: "
				+ rawImage.bpp
				+ ", size: "
				+ rawImage.width
				+ " x "
				+ rawImage.height
				+ ", "
				+ String.format("%X %X %X", rawImage.getRedMask(),
						rawImage.getGreenMask(), rawImage.getBlueMask()));

		BufferedImage image = new BufferedImage(rawImage.width,
				rawImage.height, BufferedImage.TYPE_INT_ARGB);

		long t1 = System.currentTimeMillis();
		WritableRaster raster = image.getRaster();
		for (int j = 0; j < rawImage.height; j++) {
			int s = j * rawImage.width * 4;
			for (int i = 0; i < rawImage.width; i++) {
				int t = s + i * 4;
				byte r = rawImage.data[t + 0];
				byte g = rawImage.data[t + 1];
				byte b = rawImage.data[t + 2];
				byte a = rawImage.data[t + 3];
				raster.setPixel(i, j, new int[] { r, g, b, a });
			}
		}
		long t2 = System.currentTimeMillis();
		System.out.println("time: " + (t2 - t1));

		return image;
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
		// String cmd = String.format(CMD_SWIPE_DURATION, x, y, x2, y2,
		// duration);
		String cmd = String.format(CMD_SWIPE, x, y, x2, y2);
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

	public static String executeShellCommand(IDevice device, String command)
			throws TimeoutException, AdbCommandRejectedException,
			ShellCommandUnresponsiveException, IOException
	{
		CommandResultGatherer gatherer = new CommandResultGatherer();
		device.executeShellCommand(command, gatherer);
		return gatherer.getOutput();
	}
}
