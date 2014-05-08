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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

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

		new Viewer();
	}

	private JFrame frame;
	private ScreenshotPanel screenshotPanel;

	public Viewer()
	{
		double scale = 0.3;

		frame = new JFrame("Android Remote Control");
		frame.setSize(400, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel(new BorderLayout());
		frame.setContentPane(panel);

		screenshotPanel = new ScreenshotPanel(scale);
		panel.add(screenshotPanel, BorderLayout.CENTER);

		frame.pack();
		frame.setVisible(true);

		ScreenshotFetcher screenshotFetcher = new ScreenshotFetcher() {

			@Override
			public void screenshotAvailabe(byte[] bytes)
			{
				update(bytes);
			}
		};
		Thread thread = new Thread(screenshotFetcher);
		thread.start();

		frame.addKeyListener(new DeviceKeyAdapter());
	}

	protected void update(byte[] bytes)
	{
		try {
			final BufferedImage image = ImageIO.read(new ByteArrayInputStream(
					bytes));
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run()
				{
					if (image != null) {
						update(image);
					} else {
						System.err.println("Invalid image");
					}
				}
			});
		} catch (IOException e) {
			System.err.println("Error while decoding screenshot: "
					+ e.getMessage());
		}
	}

	private void update(BufferedImage image)
	{
		boolean sizeChanged = screenshotPanel.setImage(image);
		if (sizeChanged) {
			frame.pack();
		}
		screenshotPanel.repaint();
	}

}
