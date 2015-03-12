package uk.co.skyem.projects.Z80emu;

public class BusOffset implements IBusDevice {
	int offset = 0;
	IBusDevice toOffset;
	BusOffset(int offset, IBusDevice toOffset) {
		this.offset = offset;
		this.toOffset = toOffset;
	}
	public void putByte(int position, byte data) {
		toOffset.putByte(position - offset, data);
	}
	public byte getByte(int position) {
		return toOffset.getByte(position - offset);
	}
	public byte[] getBytes(int position, int amount) {
		return toOffset.getBytes(position - offset, amount);
	}
	public void putBytes(int position, byte[] data) {
		toOffset.putBytes(position, data);
	}
}
