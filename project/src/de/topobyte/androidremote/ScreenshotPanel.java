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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class ScreenshotPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	private double scale;
	private BufferedImage image;

	private int w, h;

	public ScreenshotPanel(double scale)
	{
		this.scale = scale;
		setPreferredSize(new Dimension(200, 200));
	}

	public void setImage(BufferedImage image)
	{
		this.image = image;
		w = (int) Math.round(image.getWidth() * scale);
		h = (int) Math.round(image.getHeight() * scale);
		setPreferredSize(new Dimension(w, h));
	}

	@Override
	public void paint(Graphics graphics)
	{
		super.paint(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		if (image != null) {
			Image scaled = image.getScaledInstance(w, h,
					BufferedImage.SCALE_SMOOTH);

			g.drawImage(scaled, 0, 0, null);
		}
	}

}
