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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;

public class DeviceList implements IDeviceChangeListener
{

	private List<IDevice> devices = new ArrayList<IDevice>();

	@Override
	public void deviceConnected(IDevice device)
	{
		devices.add(device);
	}

	@Override
	public void deviceDisconnected(IDevice device)
	{
		devices.remove(device);
	}

	@Override
	public void deviceChanged(IDevice device, int changeMask)
	{
		// do nothing at the moment
	}

	public List<IDevice> getDevices()
	{
		return Collections.unmodifiableList(devices);
	}

}
