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

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class DropApkPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	public DropApkPanel()
	{
		setLayout(new BorderLayout());
		JLabel dropLabel = new JLabel("Drop APK here");
		dropLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(dropLabel, BorderLayout.CENTER);
	}
}
