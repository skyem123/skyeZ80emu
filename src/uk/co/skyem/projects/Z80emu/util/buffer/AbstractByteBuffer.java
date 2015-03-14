package uk.co.skyem.projects.Z80emu.util.buffer;

public abstract class AbstractByteBuffer implements IByteBuffer {

	public Endian endian;

	public static enum Endian {
		LITTLE, BIG;
	}

	public AbstractByteBuffer() {
		this(Endian.BIG);
	}

	public AbstractByteBuffer(Endian endian) {
		this.endian = endian;
	}

	@Override
	public byte[] getBytes(int position, int amount) {
		byte[] result = new byte[amount];
		for (; amount > 0; amount--) {
			result[amount - 1] = this.getByte(position + amount - 1);
		}
		return result;
	}

	@Override
	public short getWord(int position) {
		if (endian == Endian.BIG) {
			return (short) (getByte(position + 1) & 0xFF | (getByte(position) << 8 & 0xFF00));
		} else {
			return (short) (getByte(position) & 0xFF | (getByte(position - 1) << 8 & 0xFF00));
		}
	}

	@Override
	public int getDWord(int position) {
		if (endian == Endian.BIG) {
			return getWord(position + 2) & 0xFFFF | (getWord(position) << 16 & 0xFFFF0000);
		} else {
			return getWord(position) & 0xFFFF | (getWord(position - 2) << 16 & 0xFFFF0000);
		}
	}

	@Override
	public long getQWord(int position) {
		if (endian == Endian.BIG) {
			return getDWord(position + 4) & 0xFFFFFFFFL | ((long) getDWord(position) << 32 & 0xFFFFFFFF00000000L);
		} else {
			return getDWord(position) & 0xFFFFFFFFL | ((long) getDWord(position - 4) << 32 & 0xFFFFFFFF00000000L);
		}
	}

	@Override
	public void putWord(int position, short data) {
		if (endian == Endian.BIG) {
			putByte(position + 1, (byte) (data & 0xFF));
			putByte(position, (byte) (data >>> 8));
		} else {
			putByte(position, (byte) (data & 0xFF));
			putByte(position - 1, (byte) (data >>> 8));
		}
	}

	@Override
	public void putDWord(int position, int data) {
		if (endian == Endian.BIG) {
			putWord(position + 2, (short) (data & 0xFFFF));
			putWord(position, (short) (data >>> 16));
		} else {
			putWord(position, (short) (data & 0xFFFF));
			putWord(position - 2, (short) (data >>> 16));
		}
	}

	@Override
	public void putQWord(int position, long data) {
		if (endian == Endian.BIG) {
			putDWord(position + 4, (int) (data & 0xFFFFFFFFL));
			putDWord(position, (int) (data >>> 32));
		} else {
			putDWord(position, (int) (data & 0xFFFFFFFFL));
			putDWord(position - 4, (int) (data >>> 32));
		}
	}

	@Override
	public void putBytes(int position, byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			putByte(position + i, bytes[i]);
		}
	}
}
