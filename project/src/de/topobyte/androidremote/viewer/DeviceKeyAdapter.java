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

package de.topobyte.androidremote.viewer;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import de.topobyte.androidremote.Util;

public class DeviceKeyAdapter extends KeyAdapter
{

	@Override
	public void keyPressed(KeyEvent ev)
	{
		super.keyPressed(ev);
		System.out.println(ev.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent ev)
	{
		super.keyReleased(ev);
		System.out.println(ev.getKeyCode());

		System.out.println(ev.getKeyCode());
		if (ev.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
			sendKeyEvent(67);
			return;
		} else if (ev.getKeyCode() == KeyEvent.VK_ENTER) {
			sendKeyEvent(66);
			return;
		} else if (ev.getKeyCode() == KeyEvent.VK_SPACE) {
			sendKeyEvent(62);
			return;
		}
	}

	@Override
	public void keyTyped(KeyEvent ev)
	{
		super.keyTyped(ev);

		char keyChar = ev.getKeyChar();
		if (keyChar != KeyEvent.CHAR_UNDEFINED) {
			System.out.println("typed: '" + keyChar + "'");
			try {
				Util.sendText("" + keyChar);
			} catch (IOException e) {
				System.err.println("Error while sending text: "
						+ e.getMessage());
			}
		}
	}

	private void sendKeyEvent(int code)
	{
		try {
			Util.sendKeyEvent(code);
		} catch (IOException e) {
			System.err.println("Error while sending key event: "
					+ e.getMessage());
		}
	}

}
