package uk.co.skyem.projects.emuZ80.bus;

public class Memory extends SimpleBusDevice {
	private byte[] storage;

	public Memory(int size) {
		storage = new byte[size];
	}

	public Memory(int size, int offset) {
		this.offset = offset;
		this.storage = new byte[size];
	}

	@Override
	public int getSize() {
		return storage.length;
	}

	@Override
	public void putByte(int position, byte data) {
		synchronized (this) {
			if (!(position < offset || position >= storage.length + offset)) {
				storage[position - offset] = data;
			}
		}
	}

	@Override
	// TODO: should this be synchronized
	public byte getByte(int position) {
		synchronized (this) {
			if (position < offset || position >= storage.length + offset) {
				return (byte) 0x00;
			} else {
				return storage[position - offset];
			}
		}
	}

	@Override
	public void putBytes(int position, byte[] bytes) {
		synchronized (this) {
			System.arraycopy(bytes, 0, storage, position - offset, bytes.length);
		}
	}

	@Override
	public byte[] getBytes(int position, int amount) {
		byte[] copy = new byte[amount];
		System.arraycopy(storage, position, copy, 0, amount);
		return copy;
	}
}
