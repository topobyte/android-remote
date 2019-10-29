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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import com.android.ddmlib.IDevice;

public class DevicePanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	private Toolkit toolkit;
	private IDevice device;

	private JLabel labelTitle;
	private DropApkPanel dropApk;
	private ScreenshotPanel screenshotPanel;

	private String deviceName = null;

	public DevicePanel(Toolkit toolkit, IDevice device)
	{
		this.toolkit = toolkit;
		this.device = device;
		deviceName = device.getName();
		setLayout(new GridBagLayout());

		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createBevelBorder(BevelBorder.LOWERED),
				BorderFactory.createEmptyBorder(5, 5, 5, 5)));

		labelTitle = new JLabel();
		updateTitle();
		dropApk = new DropApkPanel(toolkit, device);
		dropApk.setPreferredSize(new Dimension(200, 100));
		dropApk.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		JButton buttonListPackages = new JButton("List packages");

		JButton buttonLock = new JButton("Lock");
		JButton buttonUnlock = new JButton("Unlock");

		JButton buttonUp = new JButton("Up");
		JButton buttonDown = new JButton("Down");

		screenshotPanel = new ScreenshotPanel(toolkit, device);
		screenshotPanel.setBorder(BorderFactory
				.createTitledBorder("Screenshot"));
		screenshotPanel.getPathScreenshots().setText(
				toolkit.getDefaultScreenshotPath());
		screenshotPanel.getPatternFilenames().setText(
				toolkit.getDefaultScreenshotPattern(device));

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;

		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1.0;
		c.gridwidth = 6;
		c.gridheight = 1;
		add(labelTitle, c);

		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.0;
		c.gridwidth = 1;
		c.gridheight = 2;
		c.gridy++;
		c.gridx = 0;
		add(dropApk, c);

		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridheight = 1;
		c.gridx++;
		c.insets = new Insets(0, 2, 0, 2);
		add(buttonListPackages, c);
		c.gridx++;
		add(buttonLock, c);
		c.gridx++;
		add(buttonUnlock, c);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.weightx = 1.0;
		c.gridx++;
		add(buttonUp, c);
		c.weightx = 0.0;
		c.gridx++;
		add(buttonDown, c);

		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridx = 1;
		c.gridwidth = 5;
		c.gridy++;
		add(screenshotPanel, c);

		buttonListPackages.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				PackageDialog packageDialog = new PackageDialog(
						DevicePanel.this.toolkit, DevicePanel.this.device);
				packageDialog.setSize(400, 300);
				packageDialog.setVisible(true);
			}
		});

		buttonLock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				DevicePanel.this.toolkit.uninstallFromDevice(
						DevicePanel.this.device, new App(
								"de.topobyte.apps.freemium.unlock.citymaps"));
			}
		});

		buttonUnlock.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				String filePath = "/home/z/git/apks/unlock-city-maps/v3/UnlockCityMaps.apk";
				DevicePanel.this.toolkit.uploadToDevice(
						DevicePanel.this.device, new File(filePath));
			}
		});

		buttonUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				DevicePanel.this.toolkit.getToolkitFrame().getDeviceListPanel()
						.moveUp(DevicePanel.this);
			}
		});

		buttonDown.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				DevicePanel.this.toolkit.getToolkitFrame().getDeviceListPanel()
						.moveDown(DevicePanel.this);
			}
		});
	}

	public void update(IDevice device, int changeMask)
	{
		if (!device.getName().equals(deviceName)) {
			deviceName = device.getName();
			updateTitle();
			updateScreenshotPanel();
		}
	}

	private void updateTitle()
	{
		labelTitle.setText("Device: '" + device.getName() + "'");
	}

	private void updateScreenshotPanel()
	{
		screenshotPanel.getPatternFilenames().setText(
				toolkit.getDefaultScreenshotPattern(device));
	}

}
