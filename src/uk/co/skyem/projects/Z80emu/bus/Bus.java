package uk.co.skyem.projects.Z80emu.bus;

import java.util.ArrayList;

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
			result = (byte) (result | connection.getByte(position));
		}
		return result;
	}

	public byte[] getBytes(int position, int amount) {
		byte[] result = new byte[amount];
		for (IBusDevice connection : connections) {
			byte[] bytes = connection.getBytes(position, amount);
			for (int i = 0; i < bytes.length; i++) {
				result[i] = (byte) (result[i] | bytes[i]);
			}
		}
		return result;
	}

	public void putBytes(int position, byte[] data) {
		for (IBusDevice connection : connections) {
			connection.putBytes(position, data);
		}
	}

	public short getWord(int position) {
		short result = 0x00;
		for (IBusDevice connection : connections) {
			result = (short) (result | connection.getWord(position));
		}
		return result;
	}

	public int getDWord(int position) {
		int result = 0x00;
		for (IBusDevice connection : connections) {
			result = (result | connection.getDWord(position));
		}
		return result;
	}

	public long getQWord(int position) {
		long result = 0x00;
		for (IBusDevice connection : connections) {
			result = (result | connection.getQWord(position));
		}
		return result;
	}

	public void putWord(int position, short data) {
		for (IBusDevice connection : connections) {
			connection.putWord(position, data);
		}
	}

	public void putDWord(int position, int data) {
		for (IBusDevice connection : connections) {
			connection.putDWord(position, data);
		}
	}

	public void putQWord(int position, long data) {
		for (IBusDevice connection : connections) {
			connection.putQWord(position, data);
		}
	}

	public void addConnection(IBusDevice connection) {
		connections.add(connection);
	}

	public void removeConnection(IBusDevice connection) {
		connections.remove(connection);
	}
}
