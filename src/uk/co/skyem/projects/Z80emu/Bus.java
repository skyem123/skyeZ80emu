package uk.co.skyem.projects.Z80emu;

import java.util.ArrayList;

/**
 * Created by skye on 2015-03-11.
 */
public class Bus implements IBusDevice {
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
	public byte[] getBytes(int position, int amount) {
		byte[] result = new byte[amount];
		for (IBusDevice connection : connections) {
			byte[] bytes = connection.getBytes(position, amount);
			for (int i = 0; i < bytes.length; i++) {
				result[i] = (byte)(result[i] | bytes[i]);
			}
		}
		return result;
	}
	public void addConnection(IBusDevice connection) {
		connections.add(connection);
	}
	public void removeConnection(IBusDevice connection) {
		connections.remove(connection);
	}
}
