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

import java.io.ByteArrayOutputStream;

import com.android.ddmlib.IShellOutputReceiver;

public class CommandResultGatherer implements IShellOutputReceiver
{

	private String output = null;
	private ByteArrayOutputStream baos = new ByteArrayOutputStream();

	@Override
	public void addOutput(byte[] data, int offset, int length)
	{
		baos.write(data, offset, length);
	}

	@Override
	public void flush()
	{
		byte[] bytes = baos.toByteArray();
		output = new String(bytes);
	}

	@Override
	public boolean isCancelled()
	{
		return false;
	}

	public String getOutput()
	{
		return output;
	}

}
