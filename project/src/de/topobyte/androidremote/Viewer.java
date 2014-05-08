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

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;

public class Viewer
{

	public static void main(String[] args)
	{
		if (!Util.haveAdbInPath()) {
			System.err
					.println("Unable to execute adb. Have you set up the path correctly?");
			System.exit(1);
		}
		System.out.println("Okay, let's begin");

		double scale = 0.3;
		if (args.length > 0) {
			String argScale = args[0];
			double value = Double.parseDouble(argScale);
			if (value != 0) {
				scale = value;
			}
		}

		new Viewer(scale);
	}

	private JFrame frame;
	private ScreenshotPanel screenshotPanel;
	private AndroidDebugBridge adb;
	private IDevice device;

	public Viewer(double scale)
	{
		AndroidDebugBridge.init(false);
		adb = AndroidDebugBridge.createBridge();
		AndroidDebugBridge.addDeviceChangeListener(new DeviceListener());

		frame = new JFrame("Android Remote Control");
		frame.setSize(400, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new BorderLayout());
		frame.setContentPane(panel);

		screenshotPanel = new ScreenshotPanel(scale);
		panel.add(screenshotPanel, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);

		frame.addKeyListener(new DeviceKeyAdapter());
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
				startFetcher();
			}
		}

		@Override
		public void deviceDisconnected(IDevice device)
		{
			if (Viewer.this.device == device) {
				Viewer.this.device = null;
			}
		}

		@Override
		public void deviceChanged(IDevice device, int mask)
		{
			// ignore
		}
	}

	public void startFetcher()
	{

		ScreenshotFetcher screenshotFetcher = new ScreenshotFetcher(device) {

			@Override
			public void screenshotAvailabe(BufferedImage image)
			{
				update(image);
			}
		};
		Thread thread = new Thread(screenshotFetcher);
		thread.start();
	}

}
