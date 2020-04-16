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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import com.android.ddmlib.IDevice;

import de.topobyte.androidremote.Util;

public class Toolbar extends JToolBar
{

	private static final long serialVersionUID = 1L;

	private JFrame frame;
	private ScreenshotPanel screenshotPanel;
	private IDevice device;

	public Toolbar(JFrame frame, ScreenshotPanel screenshotPanel)
	{
		this.frame = frame;
		this.screenshotPanel = screenshotPanel;

		setFloatable(false);

		JButton zoomIn = new JButton("+");
		JButton zoomOut = new JButton("-");
		JButton power = new JButton("o");
		JButton powerLong = new JButton("O");

		zoomIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event)
			{
				double scale = Toolbar.this.screenshotPanel.getScale();
				setNewScale(scale * 1.25);
			}
		});

		zoomOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event)
			{
				double scale = Toolbar.this.screenshotPanel.getScale();
				setNewScale(scale / 1.25);
			}
		});

		power.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event)
			{
				sendPowerButtonShort();
			}
		});

		powerLong.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event)
			{
				sendPowerButtonLong();
			}
		});

		add(zoomIn);
		add(zoomOut);
		add(power);
		add(powerLong);
	}

	public void setDevice(IDevice device)
	{
		this.device = device;
	}

	protected void sendPowerButtonShort()
	{
		String name = device.getName();
		if (name.startsWith("htc-nexus_one-")) {
			try {
				Runtime.getRuntime()
						.exec("adb shell sendevent /dev/input/event5 1 116 1");
				Runtime.getRuntime()
						.exec("adb shell sendevent /dev/input/event5 0 0 0");
				Runtime.getRuntime()
						.exec("adb shell sendevent /dev/input/event5 1 116 0");
				Runtime.getRuntime()
						.exec("adb shell sendevent /dev/input/event5 0 0 0");
			} catch (IOException e) {
				System.err.println(
						"Error while sending event: " + e.getMessage());
			}
		} else if (name.startsWith("lge-nexus_4-")
				|| name.startsWith("lge-nexus_5-")) {
			try {
				Util.sendKeyEvent(26);
			} catch (IOException e) {
				System.err.println(
						"Error while sending event: " + e.getMessage());
			}
		}
	}

	protected void sendPowerButtonLong()
	{
		String name = device.getName();
		if (name.startsWith("htc-nexus_one-")) {
			try {
				Runtime.getRuntime()
						.exec("adb shell sendevent /dev/input/event5 1 116 1");
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// ignore
				}
				Runtime.getRuntime()
						.exec("adb shell sendevent /dev/input/event5 1 116 0");
			} catch (IOException e) {
				System.err.println(
						"Error while sending event: " + e.getMessage());
			}
		} else if (name.startsWith("lge-nexus_4-")
				|| name.startsWith("lge-nexus_5-")) {
			try {
				Runtime.getRuntime()
						.exec("adb shell sendevent /dev/input/event0 1 116 1");
				Runtime.getRuntime()
						.exec("adb shell sendevent /dev/input/event0 0 0 0");
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// ignore
				}
				Runtime.getRuntime()
						.exec("adb shell sendevent /dev/input/event0 1 116 0");
				Runtime.getRuntime()
						.exec("adb shell sendevent /dev/input/event0 0 0 0");
			} catch (IOException e) {
				System.err.println(
						"Error while sending event: " + e.getMessage());
			}
		}
	}

	protected void setNewScale(double scale)
	{
		screenshotPanel.setScale(scale);
		frame.pack();
		frame.repaint();
	}

}
