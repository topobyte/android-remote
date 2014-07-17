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

package de.topobyte.androidremote.toolkit;

import javax.swing.JFrame;

import com.android.ddmlib.AndroidDebugBridge;

import de.topobyte.androidremote.DeviceList;
import de.topobyte.androidremote.Util;

public class Toolkit
{
	public static void main(String[] args)
	{
		if (!Util.haveAdbInPath()) {
			System.err
					.println("Unable to execute adb. Have you set up the path correctly?");
			System.exit(1);
		}

		new Toolkit();
	}

	private AndroidDebugBridge adb;
	private DeviceList deviceList;
	private ToolkitFrame frame;

	public Toolkit()
	{
		AndroidDebugBridge.init(false);
		adb = AndroidDebugBridge.createBridge();

		deviceList = new DeviceList();
		AndroidDebugBridge.addDeviceChangeListener(deviceList);

		frame = new ToolkitFrame(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	public DeviceList getDeviceList()
	{
		return deviceList;
	}

}
