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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.InstallException;

import de.topobyte.androidremote.DeviceList;
import de.topobyte.androidremote.Util;

public class Toolkit
{
	public static void main(String[] args)
	{
		if (!Util.haveAdbInPath()) {
			System.err
					.println("Unable to execute adb. Have you set up the path correctly?");
			// System.exit(1);
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

	public void uploadToDevice(IDevice device, List<File> files)
	{
		List<File> toUpload = new ArrayList<File>();
		for (File file : files) {
			if (!file.getName().endsWith(".apk")) {
				System.out.println("ignoring non-APK: '"
						+ file.getAbsolutePath() + "'");
				continue;
			}
			if (!file.isFile()) {
				System.out.println("ignoring directory: '"
						+ file.getAbsolutePath() + "'");
				continue;
			}
			toUpload.add(file);
		}
		reallyUploadToDevice(device, toUpload);
	}

	private void reallyUploadToDevice(final IDevice device,
			final List<File> files)
	{
		Runnable uploadTask = new Runnable() {

			@Override
			public void run()
			{
				for (File file : files) {
					message("Uploading file: '" + file.getAbsolutePath() + "'");
					try {
						device.installPackage(file.getAbsolutePath(), true);
					} catch (InstallException e) {
						message("Error while installing: " + e.getMessage());
					}
				}
				message("Finished uploading files");
			}

		};
		new Thread(uploadTask).start();
	}

	private String separator = System.getProperty("line.separator");

	private void message(String message)
	{
		System.out.println(message);
		frame.getDebugOutputPanel().push(message);
		frame.getDebugOutputPanel().push(separator);
	}

}
