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

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;

import de.topobyte.androidremote.Util;
import de.topobyte.shared.preferences.SharedPreferences;
import de.topobyte.swing.util.SwingUtils;

public class Viewer
{

	public static void main(String[] args)
	{
		if (!Util.haveAdbInPath()) {
			System.err.println(
					"Unable to execute adb. Have you set up the path correctly?");
			System.exit(1);
		}

		SwingUtils.setUiScale(SharedPreferences.getUIScale());

		double scale = 0.3;
		if (args.length > 0) {
			String argScale = args[0];
			try {
				double value = Double.parseDouble(argScale);
				if (value != 0) {
					scale = value;
				}
			} catch (NumberFormatException e) {
				System.out.println(
						"usage: " + Viewer.class.getSimpleName() + " [scale]");
				System.exit(1);
			}
		}

		System.out.println("ADB, let's go");

		new Viewer(scale);
	}

	private DeviceInfo info;
	private IDevice device;

	private JFrame frame;
	private Toolbar toolbar;
	private ScreenshotPanel screenshotPanel;
	private AndroidDebugBridge adb;
	private ScreenshotFetcher screenshotFetcher;

	public Viewer(double scale)
	{
		frame = new JFrame("Android Remote Control");
		frame.setSize(400, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new BorderLayout());
		frame.setContentPane(panel);

		screenshotPanel = new ScreenshotPanel(this, scale);
		toolbar = new Toolbar(frame, screenshotPanel);

		screenshotPanel.setFocusable(true);

		panel.add(toolbar, BorderLayout.NORTH);
		panel.add(screenshotPanel, BorderLayout.CENTER);

		AndroidDebugBridge.init(false);
		adb = AndroidDebugBridge.createBridge();
		AndroidDebugBridge.addDeviceChangeListener(new DeviceListener());

		frame.pack();
		frame.setVisible(true);

		screenshotPanel.addKeyListener(new DeviceKeyAdapter());
	}

	public IDevice getDevice()
	{
		return device;
	}

	public DeviceInfo getDeviceInfo()
	{
		return info;
	}

	protected void update(final Image image)
	{
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run()
			{
				if (image != null) {
					updatePanel(image);
				} else {
					System.err.println("Invalid image");
				}
			}
		});
	}

	private void updatePanel(Image image)
	{
		boolean sizeChanged = screenshotPanel.setImage(image);
		if (sizeChanged) {
			frame.pack();
		}
		screenshotPanel.repaint();
	}

	private class DeviceListener implements IDeviceChangeListener
	{

		@Override
		public void deviceConnected(IDevice device)
		{
			if (Viewer.this.device == null) {
				Viewer.this.device = device;
				toolbar.setDevice(device);

				fetchInfo();
				printInfo(info);
				startFetcher();
			}
		}

		@Override
		public void deviceDisconnected(IDevice device)
		{
			if (Viewer.this.device == device) {
				Viewer.this.device = null;
				Viewer.this.info = null;
				screenshotFetcher.finish();
			}
		}

		@Override
		public void deviceChanged(IDevice device, int mask)
		{
			fetchInfo();
			printInfo(info);
		}
	}

	public void startFetcher()
	{
		screenshotFetcher = new ScreenshotFetcher(device) {

			@Override
			public void screenshotAvailabe(BufferedImage image)
			{
				update(image);
			}
		};
		Thread thread = new Thread(screenshotFetcher);
		thread.start();
	}

	public void fetchInfo()
	{
		String name = device.getName();
		int apiLevel = 1;
		try {
			String api = device
					.getPropertyCacheOrSync(IDevice.PROP_BUILD_API_LEVEL);
			apiLevel = Integer.parseInt(api);
		} catch (Exception e) {
			System.err.println("Error while fetching api level");
		}
		info = new DeviceInfo(name, apiLevel);
	}

	public void printInfo(DeviceInfo info)
	{
		System.out.println("device name: '" + info.getName() + "'");
		System.out.println("api level: " + info.getApiLevel());
	}

}
