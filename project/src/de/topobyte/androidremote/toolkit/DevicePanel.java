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

	public DevicePanel(Toolkit toolkit, IDevice device)
	{
		this.toolkit = toolkit;
		this.device = device;
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

		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;

		c.anchor = GridBagConstraints.WEST;
		c.weightx = 1.0;
		c.gridwidth = 2;
		add(labelTitle, c);

		c.anchor = GridBagConstraints.NORTHWEST;
		c.weightx = 0.0;
		c.gridwidth = 1;
		c.gridy++;
		c.gridx = 0;
		add(dropApk, c);
		c.gridx = 1;
		c.insets = new Insets(0, 2, 0, 2);
		add(buttonListPackages, c);
	}

	private void updateTitle()
	{
		labelTitle.setText("Device: '" + device.getName() + "'");
	}

	public void update(IDevice device, int changeMask)
	{
		updateTitle();
	}
}
