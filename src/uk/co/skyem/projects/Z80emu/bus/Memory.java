package uk.co.skyem.projects.Z80emu.bus;

public class Memory extends SimpleBusDevice {
	private byte[] storage;
	private int offset = 0;

	public Memory(int size) {
		storage = new byte[size];
	}

	public Memory(int size, int offset) {
		this.offset = offset;
		this.storage = new byte[size];
	}

	@Override
	public void putByte(int position, byte data) {
		if (!(position < offset || position >= storage.length + offset)) {
			storage[position - offset] = data;
		}
	}

	@Override
	public byte getByte(int position) {
		if (position < offset || position >= storage.length + offset) {
			return (byte) 0x00;
		} else {
			return storage[position - offset];
		}
	}

	@Override
	public void putBytes(int position, byte[] bytes) {
		System.arraycopy(bytes, 0, storage, position-offset, bytes.length);
	}
}
