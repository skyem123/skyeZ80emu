package uk.co.skyem.projects.emuZ80.bus;

import uk.co.skyem.projects.emuZ80.util.buffer.IByteHandler;

import java.util.ArrayList;

public class Bus implements IByteHandler {
	private ArrayList<IByteHandler> connections = new ArrayList<>();

	public void putByte(int position, byte data) {
		for (IByteHandler connection : connections) {
			connection.putByte(position, data);
		}
	}

	@Override
	public byte getByte(int position) {
		byte result = 0x00;
		for (IByteHandler connection : connections) {
			result = (byte) (result | connection.getByte(position));
		}
		return result;
	}

	@Override
	public byte[] getBytes(int position, int amount) {
		byte[] result = new byte[amount];
		for (IByteHandler connection : connections) {
			byte[] bytes = connection.getBytes(position, amount);
			for (int i = 0; i < bytes.length; i++) {
				result[i] = (byte) (result[i] | bytes[i]);
			}
		}
		return result;
	}

	@Override
	public void putBytes(int position, byte[] data) {
		for (IByteHandler connection : connections) {
			connection.putBytes(position, data);
		}
	}

	@Override
	public short getWord(int position) {
		short result = 0x00;
		for (IByteHandler connection : connections) {
			result = (short) (result | connection.getWord(position));
		}
		return result;
	}

	@Override
	public int getDWord(int position) {
		int result = 0x00;
		for (IByteHandler connection : connections) {
			result = (result | connection.getDWord(position));
		}
		return result;
	}

	@Override
	public long getQWord(int position) {
		long result = 0x00;
		for (IByteHandler connection : connections) {
			result = (result | connection.getQWord(position));
		}
		return result;
	}

	@Override
	public void putWord(int position, short data) {
		for (IByteHandler connection : connections) {
			connection.putWord(position, data);
		}
	}

	@Override
	public void putDWord(int position, int data) {
		for (IByteHandler connection : connections) {
			connection.putDWord(position, data);
		}
	}

	@Override
	public void putQWord(int position, long data) {
		for (IByteHandler connection : connections) {
			connection.putQWord(position, data);
		}
	}

	public void addConnection(IByteHandler connection) {
		connections.add(connection);
	}

	public void removeConnection(IByteHandler connection) {
		connections.remove(connection);
	}
}
