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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class DeviceMouseAdapter extends MouseAdapter
{

	private DeviceInfo info;

	public DeviceMouseAdapter(DeviceInfo info)
	{
		this.info = info;
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		if (!inDeviceBounds(e)) {
			return;
		}
		int x = e.getX();
		int y = e.getY();
		double deviceX = x / info.getScale();
		double deviceY = y / info.getScale();
		doPress((int) Math.round(deviceX), (int) Math.round(deviceY));
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		super.mouseReleased(e);
		int x = e.getX();
		int y = e.getY();
		double deviceX = x / info.getScale();
		double deviceY = y / info.getScale();
		if (deviceX < 0) {
			deviceX = 0;
		}
		if (deviceX > info.getWidth()) {
			deviceX = info.getWidth();
		}
		if (deviceY < 0) {
			deviceY = 0;
		}
		if (deviceY > info.getHeight()) {
			deviceY = info.getHeight();
		}
		doRelease((int) Math.round(deviceX), (int) Math.round(deviceY));
	}

	private long timePress = 0;
	private int pressX = 0;
	private int pressY = 0;

	private void doPress(int deviceX, int deviceY)
	{
		System.out.println("press: " + deviceX + ", " + deviceY);
		timePress = System.currentTimeMillis();
		pressX = deviceX;
		pressY = deviceY;
	}

	private void doRelease(int deviceX, int deviceY)
	{
		System.out.println("release: " + deviceX + ", " + deviceY);
		long timeRelease = System.currentTimeMillis();

		int duration = (int) (timeRelease - timePress);
		if (duration < 100) {
			try {
				Util.sendTap(deviceX, deviceY);
			} catch (IOException e) {
				System.err.println("Error while sending mouse press: "
						+ e.getMessage());
			}
		} else {
			try {
				Util.sendSwipe(pressX, pressY, deviceX, deviceY, duration);
			} catch (IOException e) {
				System.err.println("Error while sending mouse release: "
						+ e.getMessage());
			}
		}
	}

	private boolean inDeviceBounds(MouseEvent e)
	{
		return e.getX() <= info.getDisplayWidth()
				&& e.getY() <= info.getDisplayHeight();
	}

}