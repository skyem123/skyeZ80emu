package uk.co.skyem.projects.Z80emu;

/**
 * Created by skye on 2015-03-11.
 */
public class Memory implements IBusDevice{
    private byte[] storage;
	private int offset;
    Memory(int size) {
        storage = new byte[size];
    }
	Memory(int size, int offset) {
		// TODO: offset
		this.offset = offset;
		this.storage = new byte[size];
	}
	public void putByte(int position, byte data) {
		// TODO: Bounds check
		storage[position-offset] = data;
	}
	public byte getByte(int position) {
		// TODO: Bounds check
		return storage[position-offset];
	}
	public String getHexStringByte(int position) {
		return Integer.toHexString(Byte.toUnsignedInt(this.getByte(position)));
	}
}
