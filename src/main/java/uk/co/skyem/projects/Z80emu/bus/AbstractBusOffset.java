package uk.co.skyem.projects.Z80emu.bus;

/**
 * Created by skye on 2015-03-25.
 */
public abstract class AbstractBusOffset implements IBusDevice {
	int offset = 0;
	IBusDevice toOffset;

	public AbstractBusOffset(int offset, IBusDevice toOffset) {
		this.offset = offset;
		this.toOffset = toOffset;
	}

	public int getOffset() {
		return offset;
	}

	public void changeOffset(int offset) {
		this.offset = offset;
	}

	@Override
	public void putByte(int position, byte data) {
		toOffset.putByte(position - offset, data);
	}

	@Override
	public byte getByte(int position) {
		return toOffset.getByte(position - offset);
	}

	@Override
	public byte[] getBytes(int position, int amount) {
		return toOffset.getBytes(position - offset, amount);
	}

	@Override
	public void putBytes(int position, byte[] data) {
		toOffset.putBytes(position - offset, data);
	}

	@Override
	public short getWord(int position) {
		return toOffset.getWord(position - offset);
	}

	@Override
	public int getDWord(int position) {
		return toOffset.getDWord(position - offset);
	}

	@Override
	public long getQWord(int position) {
		return toOffset.getQWord(position - offset);
	}

	@Override
	public void putWord(int position, short data) {
		toOffset.putWord(position - offset, data);
	}

	@Override
	public void putDWord(int position, int data) {
		toOffset.putDWord(position - offset, data);
	}

	@Override
	public void putQWord(int position, long data) {
		toOffset.putQWord(position - offset, data);
	}
}
