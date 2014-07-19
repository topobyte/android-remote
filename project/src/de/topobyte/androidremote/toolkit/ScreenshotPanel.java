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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.android.ddmlib.IDevice;

public class ScreenshotPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	private JTextField pathScreenshots;
	private JButton takeScreenshot;

	public ScreenshotPanel(final Toolkit toolkit, final IDevice device)
	{
		setLayout(new GridBagLayout());
		JLabel labelPath = new JLabel("Path:");
		pathScreenshots = new JTextField();
		takeScreenshot = new JButton("Take");

		Dimension ps = pathScreenshots.getPreferredSize();
		pathScreenshots.setPreferredSize(new Dimension(500, ps.height));

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;

		add(labelPath, c);
		c.gridx++;
		add(pathScreenshots, c);
		c.gridwidth = 2;
		c.gridx = 0;
		c.gridy++;
		add(takeScreenshot, c);

		takeScreenshot.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				toolkit.takeScreenshot(device, pathScreenshots.getText());
			}
		});
	}

	public JTextField getPathScreenshots()
	{
		return pathScreenshots;
	}

}
