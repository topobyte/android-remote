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

import java.io.IOException;

public abstract class ScreenshotFetcher implements Runnable
{

	public abstract void screenshotAvailabe(byte[] bytes);

	@Override
	public void run()
	{
		while (true) {
			try {
				byte[] screenshot = Util.getScreenshot();
				screenshotAvailabe(screenshot);
			} catch (IOException e) {
				System.err.println("Error while fetching screenshot: "
						+ e.getMessage());
			}
		}
	}

}
