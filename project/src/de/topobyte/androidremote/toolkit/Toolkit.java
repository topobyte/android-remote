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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
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

	private String separator = System.getProperty("line.separator");

	private void message(String message)
	{
		System.out.println(message);
		frame.getDebugOutputPanel().push(message);
		frame.getDebugOutputPanel().push(separator);
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
					upload(file);
				}
				message("Finished uploading files");
			}

			private void upload(File file)
			{
				message("Uploading file: '" + file.getAbsolutePath() + "'");
				File tmpFile = null;
				try {
					tmpFile = File.createTempFile("androidtoolkit", ".apk");
					Path source = Paths.get(file.getAbsolutePath());
					Path target = Paths.get(tmpFile.getAbsolutePath());
					Files.copy(source, target,
							StandardCopyOption.REPLACE_EXISTING);
					String result = device.installPackage(
							tmpFile.getAbsolutePath(), true);
					if (result == null) {
						message("Success");
					} else {
						message(result);
					}
				} catch (IOException e) {
					message("Error while copying APK to temporary location: "
							+ e.getMessage());
					e.printStackTrace();
				} catch (InstallException e) {
					message("Error while installing: " + e.getMessage());
				} finally {
					if (tmpFile != null) {
						tmpFile.delete();
					}
				}
			}

		};
		new Thread(uploadTask).start();
	}

	public void uninstallFromDevice(IDevice device, List<App> apps)
	{
		for (App app : apps) {
			message("Uninstalling: " + app.getPackageName());
			try {
				String result = device.uninstallPackage(app.getPackageName());
				if (result == null) {
					message("Success");
				} else {
					message(result);
				}
			} catch (InstallException e) {
				message("Error while uninstalling: " + e.getMessage());
			}
		}
	}

	public String getDefaultScreenshotPath()
	{
		return "/home/z/git/google-play/stadtplan-ng/screenshots";
	}

	public String getDefaultScreenshotPattern(IDevice device)
	{
		if (device.getName().contains("nook")) {
			return "Tablet%02d.png";
		}
		return "Screen%02d.png";
	}

	public void takeScreenshot(IDevice device, String pathDir, String pattern)
	{
		File dir = new File(pathDir);
		if (!dir.exists()) {
			boolean success = dir.mkdirs();
			if (!success) {
				message("Unable to create directory");
				return;
			}
		}
		if (!dir.exists()) {
			message("Unable to create directory");
			return;
		}
		if (!dir.canWrite()) {
			message("Unable to write to directory");
			return;
		}

		boolean varies = false;
		try {
			varies = !String.format(pattern, 1).equals(
					String.format(pattern, 2));
		} catch (Exception e) {
			message("Error processing the filename pattern");
			return;
		}
		File file = null;
		int i = 1;
		while (true) {
			String filename = String.format(pattern, i);
			file = new File(dir, filename);
			if (!file.exists()) {
				break;
			}
			if (!varies) {
				break;
			}
			i++;
		}
		if (file.exists()) {
			message("File already exists");
			return;
		}

		try {
			message("Capturing screenshot to: '" + file.getAbsolutePath() + "'");
			BufferedImage image = Util.getScreenshot(device);
			ImageIO.write(image, "PNG", file);
			message("Success");
		} catch (Exception e) {
			message("Unable to capture screenshot");
			message(e.getClass().getSimpleName() + ": " + e.getMessage());
		}
	}
}
