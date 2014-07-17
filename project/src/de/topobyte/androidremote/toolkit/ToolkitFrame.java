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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

public class ToolkitFrame extends JFrame
{

	private static final long serialVersionUID = 1L;

	private JPanel mainPanel;
	private JTabbedPane tabs;

	public ToolkitFrame(Toolkit toolkit)
	{
		super("ADB Toolkit");
		mainPanel = new JPanel(new GridBagLayout());
		setContentPane(mainPanel);

		tabs = new JTabbedPane();

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		mainPanel.add(tabs, c);

		DeviceListPanel deviceListPanel = new DeviceListPanel(toolkit);
		JScrollPane jspDeviceList = new JScrollPane(deviceListPanel);
		tabs.add("Devices", jspDeviceList);
	}
}
