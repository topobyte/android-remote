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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import de.topobyte.androidremote.Util;
import de.topobyte.swing.util.DocumentAdapter;

public class PackageDialog extends JDialog
{

	private static final long serialVersionUID = 1L;

	private Toolkit toolkit;
	private IDevice device;

	private JTextField inputFilter;
	private AppsTableModel tableModel;
	private JTable table;

	public PackageDialog(Toolkit toolkit, IDevice device)
	{
		this.toolkit = toolkit;
		this.device = device;

		setTitle("Packages for: " + device.getName());

		inputFilter = new JTextField();
		inputFilter.setText("topobyte");

		tableModel = new AppsTableModel();
		tableModel.setFilter(inputFilter.getText());
		table = new JTable(tableModel);
		JScrollPane jsp = new JScrollPane(table);

		table.getColumnModel().getColumn(0).setPreferredWidth(800);
		table.getColumnModel().getColumn(1).setPreferredWidth(50);

		inputFilter.getDocument().addDocumentListener(new DocumentAdapter() {

			@Override
			public void update(DocumentEvent event)
			{
				String filter = inputFilter.getText();
				tableModel.setFilter(filter);
			}
		});

		updateList();

		JPanel main = new JPanel(new GridBagLayout());
		setContentPane(main);

		GridBagConstraints c = new GridBagConstraints();
		JButton buttonUninstall = new JButton("uninstall");
		JButton buttonClear = new JButton("clear data");

		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 0.0;
		c.anchor = GridBagConstraints.WEST;
		main.add(buttonUninstall, c);

		c.gridx++;
		main.add(buttonClear, c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		main.add(inputFilter, c);

		c.gridy = 2;
		c.weighty = 1.0;
		main.add(jsp, c);

		buttonUninstall.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				uninstallSelected();
			}
		});

		buttonClear.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				clearSelected();
			}
		});
	}

	protected void uninstallSelected()
	{
		List<App> apps = tableModel.getSelectedApps();
		toolkit.uninstallFromDevice(device, apps);
	}

	protected void clearSelected()
	{
		List<App> apps = tableModel.getSelectedApps();
		toolkit.clearOnDevice(device, apps);
	}

	private void updateList()
	{
		Runnable listPackages = new Runnable() {

			@Override
			public void run()
			{
				try {
					String result = Util.executeShellCommand(
							PackageDialog.this.device, "pm list packages");
					tableModel.update(result);
				} catch (TimeoutException e) {
					e.printStackTrace();
				} catch (AdbCommandRejectedException e) {
					e.printStackTrace();
				} catch (ShellCommandUnresponsiveException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		new Thread(listPackages).start();
	}

}
