package uk.co.skyem.projects.emuBus;

import uk.co.skyem.projects.emuBus.IBusDevice;

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

	public void putBytes(int position, byte[] bytes) {
		System.arraycopy(bytes, 0, storage, position-offset, bytes.length);
	}
}