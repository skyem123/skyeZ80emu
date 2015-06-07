package uk.co.skyem.projects.emuZ80.util.buffer;

/**
 * Allows you to put in bytes, shorts, ints and longs with different endian modes.<br>
 * If you are using this with two different threads that set different endian modes, make sure to synchronise the code
 * that sets the endian and accesses the buffer! Use: {@code synchronized (byteBuffer){}}
 */
public abstract class AbstractByteBuffer implements IByteBuffer {

	// TODO: Make sure that the way the ByteBuffer handles BIG endian fetches is the same as the Z80
	public static enum Endian {
		BIG, LITTLE, LITTLE_ALT
	}

	private Endian endian;

	public AbstractByteBuffer() {
		this(Endian.BIG);
	}

	public AbstractByteBuffer(Endian endian) {
		this.endian = endian;
	}

	/**
	 * Gets the endian of this buffer
	 * @return the endian that this buffer is set to
	 */
	public Endian getEndian() {
		synchronized (this) {
			return endian;
		}
	}

	/**
	 * Sets the endian of this buffer
	 * @param endian the endian to set the buffer to
	 */
	public void setEndian(Endian endian) {
		synchronized (this) {
			this.endian = endian;
		}
	}

	@Override
	public byte[] getBytes(int position, int amount) {
		synchronized (this) {
			byte[] result = new byte[amount];
			for (; amount > 0; amount--) {
				result[amount - 1] = this.getByte(position + amount - 1);
			}
			return result;
		}
	}

	@Override
	public short getWord(int position) {
		synchronized (this) {
			switch (endian) {
				case BIG:
					return (short) (getByte(position + 1) & 0xFF | (getByte(position) << 8 & 0xFF00));
				case LITTLE:
					return (short) (getByte(position) & 0xFF | (getByte(position + 1) << 8 & 0xFF00));
				case LITTLE_ALT:
					return (short) (getByte(position) & 0xFF | (getByte(position - 1) << 8 & 0xFF00));
			}
		}
		throw new IllegalStateException("We should never be here.");
	}

	@Override
	public int getDWord(int position) {
		synchronized (this) {
			switch (endian) {
				case BIG:
					return getWord(position + 2) & 0xFFFF | (getWord(position) << 16 & 0xFFFF0000);
				case LITTLE:
					return getWord(position) & 0xFFFF | (getWord(position + 2) << 16 & 0xFFFF0000);
				case LITTLE_ALT:
					return getWord(position) & 0xFFFF | (getWord(position - 2) << 16 & 0xFFFF0000);
			}
		}
		throw new IllegalStateException("We should never be here.");
	}

	@Override
	public long getQWord(int position) {
		synchronized (this) {
			switch (endian) {
				case BIG:
					return getDWord(position + 4) & 0xFFFFFFFFL | ((long) getDWord(position) << 32 & 0xFFFFFFFF00000000L);
				case LITTLE:
					return getDWord(position) & 0xFFFFFFFFL | ((long) getDWord(position + 4) << 32 & 0xFFFFFFFF00000000L);
				case LITTLE_ALT:
					return getDWord(position) & 0xFFFFFFFFL | ((long) getDWord(position - 4) << 32 & 0xFFFFFFFF00000000L);
			}
		}
		throw new IllegalStateException("We should never be here.");
	}

	@Override
	public void putWord(int position, short data) {
		synchronized (this) {
			switch (endian) {
				case BIG:
					putByte(position + 1, (byte) (data & 0xFF));
					putByte(position, (byte) (data >>> 8));
					break;
				case LITTLE:
					putByte(position, (byte) (data & 0xFF));
					putByte(position + 1, (byte) (data >>> 8));
					break;
				case LITTLE_ALT:
					putByte(position, (byte) (data & 0xFF));
					putByte(position - 1, (byte) (data >>> 8));
					break;
				default:
					throw new IllegalStateException("We should never be here.");
			}
		}
	}

	@Override
	public void putDWord(int position, int data) {
		synchronized (this) {
			switch (endian) {
				case BIG:
					putWord(position + 2, (short) (data & 0xFFFF));
					putWord(position, (short) (data >>> 16));
					break;
				case LITTLE:
					putWord(position, (short) (data & 0xFFFF));
					putWord(position + 2, (short) (data >>> 16));
					break;
				case LITTLE_ALT:
					putWord(position, (short) (data & 0xFFFF));
					putWord(position - 2, (short) (data >>> 16));
					break;
				default:
					throw new IllegalStateException("We should never be here.");
			}
		}
	}

	@Override
	public void putQWord(int position, long data) {
		synchronized (this) {
			switch (endian) {
				case BIG:
					putDWord(position + 4, (int) (data & 0xFFFFFFFFL));
					putDWord(position, (int) (data >>> 32));
					break;
				case LITTLE:
					putDWord(position, (int) (data & 0xFFFFFFFFL));
					putDWord(position + 4, (int) (data >>> 32));
					break;
				case LITTLE_ALT:
					putDWord(position, (int) (data & 0xFFFFFFFFL));
					putDWord(position - 4, (int) (data >>> 32));
					break;
				default:
					throw new IllegalStateException("We should never be here.");
			}
		}
	}

	@Override
	public void putBytes(int position, byte[] bytes) {
		synchronized (this) {
			for (int i = 0; i < bytes.length; i++) {
				putByte(position + i, bytes[i]);
			}
		}
	}
}
