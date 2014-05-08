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

public class DeviceGeometry
{

	private double scale;
	private int width, height;
	private int displayWidth, displayHeight;

	public DeviceGeometry(double scale)
	{
		this.scale = scale;
	}

	public double getScale()
	{
		return scale;
	}

	public int getWidth()
	{
		return width;
	}

	public void setWidth(int width)
	{
		this.width = width;
		updateValues();
	}

	public int getHeight()
	{
		return height;
	}

	public void setHeight(int height)
	{
		this.height = height;
		updateValues();
	}

	public int getDisplayWidth()
	{
		return displayWidth;
	}

	public int getDisplayHeight()
	{
		return displayHeight;
	}

	public void setScale(double scale)
	{
		this.scale = scale;
		updateValues();
	}

	private void updateValues()
	{
		displayWidth = (int) Math.round(width * scale);
		displayHeight = (int) Math.round(height * scale);
	}

}
