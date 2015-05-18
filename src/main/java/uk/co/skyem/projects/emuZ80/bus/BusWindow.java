package uk.co.skyem.projects.emuZ80.bus;

public class BusWindow implements IBusDevice {
	int upperLimit = 0;
	int lowerLimit = 0;
	int offset = 0;
	IBusDevice toOffset;

	public BusWindow(int offset, int upper, int lower, IBusDevice toOffset) {
		this.offset = offset;
		this.toOffset = toOffset;
		this.upperLimit = upper;
		this.lowerLimit = lower;
	}

	public BusWindow(int offset, int upper, IBusDevice toOffset) {
		this.offset = offset;
		this.toOffset = toOffset;
		this.upperLimit = upper;
		this.lowerLimit = offset;
	}

	@Override
	public void putByte(int position, byte data) {
		if (!(position < lowerLimit || position > upperLimit)) {
			toOffset.putByte(position - offset, data);
		}
	}

	@Override
	public byte getByte(int position) {
		if (position < lowerLimit || position > upperLimit) {
			return (byte) 0x00;
		} else {
			return toOffset.getByte(position - offset);
		}
	}

	@Override
	public byte[] getBytes(int position, int amount) {
		if (position < lowerLimit || position + amount > upperLimit) {
			return new byte[amount];
		} else {
			return toOffset.getBytes(position - offset, amount);
		}
	}

	@Override
	public void putBytes(int position, byte[] data) {
		if (!(position < lowerLimit || position + data.length - 1 > upperLimit)) {
			toOffset.putBytes(position - offset, data);
		}
	}

	@Override
	public short getWord(int position) {
		if (position < lowerLimit || position + 1 > upperLimit) {
			return (short) 0x00;
		} else {
			return toOffset.getWord(position - offset);
		}
	}

	@Override
	public int getDWord(int position) {
		if (position < lowerLimit || position + 3 > upperLimit) {
			return 0x00;
		} else {
			return toOffset.getDWord(position - offset);
		}
	}

	@Override
	public long getQWord(int position) {
		if (position < lowerLimit || position + 7 > upperLimit) {
			return 0x00;
		} else {
			return toOffset.getQWord(position - offset);
		}
	}

	@Override
	public void putWord(int position, short data) {
		if (!(position < lowerLimit || position + 1 > upperLimit)) {
			toOffset.putWord(position - offset, data);
		}
	}

	@Override
	public void putDWord(int position, int data) {
		if (!(position < lowerLimit || position + 3 > upperLimit)) {
			toOffset.putDWord(position - offset, data);
		}
	}

	@Override
	public void putQWord(int position, long data) {
		if (!(position < lowerLimit || position + 7 > upperLimit)) {
			toOffset.putQWord(position - offset, data);
		}
	}

	public int getOffset() {
		return offset;
	}

	public void changeOffset(int offset) {
		this.offset = offset;
	}
}