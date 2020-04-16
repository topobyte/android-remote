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
import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.android.ddmlib.IDevice;

public class DropApkPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	private Toolkit toolkit;
	private IDevice device;

	private JPanel inner;

	public DropApkPanel(Toolkit toolkit, IDevice device)
	{
		this.toolkit = toolkit;
		this.device = device;
		setLayout(new BorderLayout());

		inner = new JPanel(new BorderLayout());
		add(inner, BorderLayout.CENTER);

		JLabel dropLabel = new JLabel("Drop APK here");
		dropLabel.setHorizontalAlignment(SwingConstants.CENTER);
		inner.add(dropLabel, BorderLayout.CENTER);

		new DropTarget(this, new DropTargetHandler());
	}

	public void setDragHighlight(boolean b)
	{
		if (b) {
			inner.setBorder(BorderFactory.createLineBorder(Color.RED));
		} else {
			inner.setBorder(null);
		}
	}

	private void uploadFiles(List<File> files)
	{
		toolkit.uploadToDevice(device, files);
	}

	class DropTargetHandler implements DropTargetListener
	{

		protected void processDrag(DropTargetDragEvent dtde)
		{
			if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				dtde.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
			} else {
				dtde.rejectDrag();
			}
		}

		@Override
		public void dragEnter(DropTargetDragEvent dtde)
		{
			processDrag(dtde);
			setDragHighlight(true);
		}

		@Override
		public void dragOver(DropTargetDragEvent dtde)
		{
			processDrag(dtde);
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent dtde)
		{
			processDrag(dtde);
		}

		@Override
		public void dragExit(DropTargetEvent dte)
		{
			setDragHighlight(false);
		}

		@Override
		public void drop(DropTargetDropEvent dtde)
		{
			Transferable transferable = dtde.getTransferable();

			if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)
					&& dtde.getDropAction() != DnDConstants.ACTION_NONE) {
				dtde.acceptDrop(dtde.getDropAction());
				try {

					List transferData = (List) transferable
							.getTransferData(DataFlavor.javaFileListFlavor);
					if (transferData != null && transferData.size() > 0) {
						importFiles(transferData);
						dtde.dropComplete(true);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				dtde.rejectDrop();
			}

			setDragHighlight(false);
		}

	}

	@SuppressWarnings("rawtypes")
	public void importFiles(List data)
	{
		List<File> files = new ArrayList<>();

		for (Object item : data) {
			File file = (File) item;
			files.add(file);
		}

		uploadFiles(files);
	}

}
