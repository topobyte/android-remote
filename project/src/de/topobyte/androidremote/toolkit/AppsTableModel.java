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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.AbstractTableModel;

public class AppsTableModel extends AbstractTableModel
{

	private static final long serialVersionUID = 1L;

	private List<App> apps = new ArrayList<App>();
	private Map<App, Boolean> selection = new HashMap<App, Boolean>();

	public void update(String listOfPackages)
	{
		Pattern pattern = Pattern.compile("package:(.*)");
		String[] lines = listOfPackages.split("\n");
		for (String line : lines) {
			if (!line.contains("topobyte")) {
				continue;
			}
			Matcher matcher = pattern.matcher(line.trim());
			if (matcher.matches()) {
				String packageName = matcher.group(1);
				App app = new App(packageName);
				apps.add(app);
				selection.put(app, false);
			}
		}
		Collections.sort(apps, new Comparator<App>() {

			@Override
			public int compare(App o1, App o2)
			{
				return o1.getPackageName().compareTo(o2.getPackageName());
			}
		});
		fireTableDataChanged();
	}

	@Override
	public int getRowCount()
	{
		return apps.size();
	}

	@Override
	public int getColumnCount()
	{
		return 2;
	}

	@Override
	public String getColumnName(int columnIndex)
	{
		switch (columnIndex) {
		default:
		case 0:
			return "Package";
		case 1:
			return "Mark";
		}
	}

	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		switch (columnIndex) {
		default:
		case 0:
			return String.class;
		case 1:
			return Boolean.class;
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		switch (columnIndex) {
		default:
		case 0:
			return false;
		case 1:
			return true;
		}
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		App app = apps.get(rowIndex);
		switch (columnIndex) {
		default:
		case 0:
			return app.getPackageName();
		case 1:
			return selection.get(app);
		}
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex)
	{
		App app = apps.get(rowIndex);
		if (columnIndex == 1) {
			boolean v = (Boolean) aValue;
			selection.put(app, v);
		}
	}

	public List<App> getSelectedApps()
	{
		List<App> results = new ArrayList<App>();
		for (App app : apps) {
			Boolean value = selection.get(app);
			if (value != null && value) {
				results.add(app);
			}
		}
		return results;
	}

}
