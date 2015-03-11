package uk.co.skyem.projects.Z80emu;

import java.util.ArrayList;

/**
 * Created by skye on 2015-03-11.
 */
public class Bus implements IBusDevice {
	// TODO: use hashmap?
	private ArrayList<IBusDevice> connections = new ArrayList<>();
	public void putByte(int position, byte data) {
		for (IBusDevice connection : connections) {
			connection.putByte(position, data);
		}
	}
	public byte getByte(int position) {
		byte result = 0x00;
		for (IBusDevice connection : connections) {
			result = (byte)(result | connection.getByte(position));
		}
		return result;
	}
	public String getHexStringByte(int position) {
		return Integer.toHexString(Byte.toUnsignedInt(this.getByte(position)));
	}
	public void addConnection(IBusDevice connection) {
		connections.add(connection);
	}
	public void removeConnection(IBusDevice connection) {
		connections.remove(connection);
	}
}
