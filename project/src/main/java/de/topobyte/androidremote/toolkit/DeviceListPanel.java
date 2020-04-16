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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;

import de.topobyte.androidremote.DeviceList;

public class DeviceListPanel extends JPanel implements IDeviceChangeListener
{
	private static final long serialVersionUID = 1L;

	private Toolkit toolkit;

	private Map<IDevice, DevicePanel> deviceToPanel = new HashMap<>();
	private List<DevicePanel> devicePanels = new ArrayList<>();

	private Map<DevicePanel, Integer> panelToY = new HashMap<>();
	private Map<Integer, DevicePanel> yToPanel = new HashMap<>();

	private GridBagConstraints c;
	private int yCounter = 0;

	private JPanel filler;

	public DeviceListPanel(Toolkit toolkit)
	{
		this.toolkit = toolkit;
		AndroidDebugBridge.addDeviceChangeListener(this);
		setLayout(new GridBagLayout());

		c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 0.0;

		filler = new JPanel();
		c.weighty = 1.0;
		add(filler, c);

		DeviceList deviceList = toolkit.getDeviceList();
		for (IDevice device : deviceList.getDevices()) {
			DevicePanel devicePanel = createPanel(device);
			addDevicePanel(devicePanel);
		}
	}

	private DevicePanel createPanel(IDevice device)
	{
		DevicePanel devicePanel = new DevicePanel(toolkit, device);
		deviceToPanel.put(device, devicePanel);
		devicePanels.add(devicePanel);
		return devicePanel;
	}

	private void addDevicePanel(DevicePanel devicePanel)
	{
		remove(filler);

		c.gridy = yCounter++;
		c.weighty = 0.0;
		add(devicePanel, c);
		panelToY.put(devicePanel, c.gridy);
		yToPanel.put(c.gridy, devicePanel);

		c.gridy = yCounter++;
		c.weighty = 1.0;
		add(filler, c);
	}

	protected void updateList()
	{
		DeviceList deviceList = toolkit.getDeviceList();
		for (IDevice device : deviceList.getDevices()) {
			System.out.println(device.hashCode());
			System.out.println(device.getName());
			if (deviceToPanel.containsKey(device)) {
				// update
			} else {
				createPanel(device);
			}
		}
	}

	@Override
	public void deviceConnected(IDevice device)
	{
		DevicePanel devicePanel = createPanel(device);
		addDevicePanel(devicePanel);
		revalidate();
	}

	@Override
	public void deviceDisconnected(IDevice device)
	{
		DevicePanel devicePanel = deviceToPanel.get(device);
		if (devicePanel == null) {
			return;
		}
		remove(devicePanel);
		devicePanels.remove(devicePanel);
		deviceToPanel.remove(devicePanel);
		revalidate();
	}

	@Override
	public void deviceChanged(IDevice device, int changeMask)
	{
		DevicePanel devicePanel = deviceToPanel.get(device);
		if (devicePanel == null) {
			return;
		}
		devicePanel.update(device, changeMask);
	}

	public void moveUp(DevicePanel panel)
	{
		int index = devicePanels.indexOf(panel);
		if (index == 0 || devicePanels.size() < 2) {
			return;
		}
		DevicePanel panel2 = devicePanels.get(index - 1);
		swap(panel, panel2, index, index - 1);
	}

	public void moveDown(DevicePanel panel)
	{
		int index = devicePanels.indexOf(panel);
		if (index >= devicePanels.size() - 1) {
			return;
		}
		DevicePanel panel2 = devicePanels.get(index + 1);
		swap(panel, panel2, index, index + 1);
	}

	private void swap(DevicePanel panel, DevicePanel panel2, int index,
			int index2)
	{
		int y = panelToY.get(panel);
		int y2 = panelToY.get(panel2);
		remove(panel);
		remove(panel2);

		c.weighty = 0.0;

		c.gridy = y2;
		add(panel, c);
		panelToY.put(panel, c.gridy);
		yToPanel.put(c.gridy, panel);

		c.gridy = y;
		add(panel2, c);
		panelToY.put(panel2, c.gridy);
		yToPanel.put(c.gridy, panel2);

		devicePanels.set(index, panel2);
		devicePanels.set(index2, panel);

		revalidate();
	}
}
