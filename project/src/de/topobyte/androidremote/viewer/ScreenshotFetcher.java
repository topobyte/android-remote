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

package de.topobyte.androidremote.viewer;

import java.awt.image.BufferedImage;
import java.io.IOException;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.TimeoutException;

public abstract class ScreenshotFetcher implements Runnable
{

	private IDevice device;
	private boolean valid = true;

	public ScreenshotFetcher(IDevice device)
	{
		this.device = device;
	}

	public abstract void screenshotAvailabe(BufferedImage image);

	public void finish()
	{
		valid = false;
	}

	@Override
	public void run()
	{
		while (valid) {
			boolean success = false;
			try {
				BufferedImage image = Util.getScreenshot(device);
				screenshotAvailabe(image);
				success = true;
			} catch (TimeoutException e) {
				System.err.println("Error while fetching screenshot: "
						+ e.getMessage());
			} catch (AdbCommandRejectedException e) {
				System.err.println("Error while fetching screenshot: "
						+ e.getMessage());
			} catch (IOException e) {
				System.err.println("Error while fetching screenshot: "
						+ e.getMessage());
			}
			if (!success) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
	}

}
