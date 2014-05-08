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
}
