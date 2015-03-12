package uk.co.skyem.projects.Z80emu;

public class Memory implements IBusDevice {
	private byte[] storage;
	private int offset = 0;

	Memory(int size) {
		storage = new byte[size];
	}

	Memory(int size, int offset) {
		this.offset = offset;
		this.storage = new byte[size];
	}

	public void putByte(int position, byte data) {
		if (!(position < offset || position >= storage.length + offset)) {
			storage[position - offset] = data;
		}
	}

	public byte getByte(int position) {
		if (position < offset || position >= storage.length + offset) {
			return (byte) 0x00;
		} else {
			return storage[position - offset];
		}
	}

	public byte[] getBytes(int position, int amount) {
		byte[] result = new byte[amount];
		for (; amount > 0; amount--) {
			result[amount - 1] = this.getByte(position + amount - 1);
		}
		return result;
	}

	public void putBytes(int position, byte[] bytes) {
		System.arraycopy(bytes, 0, storage, position-offset, bytes.length);
	}
}
