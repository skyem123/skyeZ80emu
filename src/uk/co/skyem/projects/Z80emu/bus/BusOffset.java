package uk.co.skyem.projects.Z80emu.bus;

public class BusOffset implements IBusDevice {
	int offset = 0;
	IBusDevice toOffset;

	public BusOffset(int offset, IBusDevice toOffset) {
		this.offset = offset;
		this.toOffset = toOffset;
	}

	public int getOffset() {
		return offset;
	}

	public void changeOffset(int offset)  {
		this.offset = offset;
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
		toOffset.putBytes(position - offset, data);
	}

	public short getWord(int position) {
		return toOffset.getWord(position - offset);
	}

	public int getDWord(int position) {
		return toOffset.getDWord(position - offset);
	}

	public long getQWord(int position) {
		return toOffset.getQWord(position - offset);
	}

	public void putWord(int position, short data) {
		toOffset.putWord(position - offset, data);
	}

	public void putDWord(int position, int data) {
		toOffset.putDWord(position - offset, data);
	}

	public void putQWord(int position, long data) {
		toOffset.putQWord(position - offset, data);
	}
}
