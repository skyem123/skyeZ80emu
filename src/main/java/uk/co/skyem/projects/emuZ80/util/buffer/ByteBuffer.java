package uk.co.skyem.projects.emuZ80.util.buffer;

import java.util.Arrays;

public class ByteBuffer extends AbstractByteBuffer {

	private byte[] value;

	public ByteBuffer(int initialSize) {
		value = new byte[initialSize];
	}

	public ByteBuffer() {
		this(0);
	}

	public ByteBuffer(Endian endian, int initialSize) {
		super(endian);
		value = new byte[initialSize];
	}

	public ByteBuffer(Endian endian) {
		this(endian, 0);
	}

	@Override
	public int getSize() {
		return value.length;
	}

	@Override
	public void putByte(int position, byte data) {
		synchronized (this) {
			value[position] = data;
		}
	}

	@Override
	public byte getByte(int position) {
		synchronized (this) {
			return value[position];
		}
	}

	@Override
	public byte[] getBytes(int position, int amount) {
		byte[] copy = new byte[amount];
		System.arraycopy(value, position, copy, 0, amount);
		return copy;
	}

	public void insert(int offset, byte[] data) {
		synchronized (this) {
			if (offset < 0) {
				throw new IllegalArgumentException("Can't insert at negative position index " + offset);
			}
			int size = value.length;

			// Expand the byte array to fit in the extra opcodes
			value = Arrays.copyOf(value, size + data.length + Math.max(0, offset - value.length));

			if (offset <= size) {
				// Move everything after the offset to the end of the byte array
				System.arraycopy(value, 0, value, offset + data.length, size - offset);
			}
			// Insert the data
			System.arraycopy(data, 0, value, offset, data.length);
		}
	}

	public void insert(int offset, ByteBuffer buffer) {
		if (buffer.getSize() == -1) {
			throw new IllegalArgumentException("Can't insert a buffer of infinite size!");
		}
		insert(offset, buffer.getBytes(0, buffer.getSize()));
	}

	public void append(byte[] data) {
		synchronized (this) {
			int size = value.length;
			value = Arrays.copyOf(value, value.length + data.length);
			System.arraycopy(data, 0, value, size, data.length);
		}
	}

	public void append(ByteBuffer buffer) {
		if (buffer.getSize() == -1) {
			throw new IllegalArgumentException("Can't append a buffer of infinite size!");
		}
		append(buffer.getBytes(0, buffer.getSize()));
	}

	public int appendByte(byte data) {
		synchronized (this) {
			value = Arrays.copyOf(value, value.length + 1);

			int position = value.length - 1;
			putByte(position, data);
			return position;
		}
	}

	public int appendWord(short data) {
		synchronized (this) {
			value = Arrays.copyOf(value, value.length + 2);

			int position = value.length - 1 - (getEndian() == Endian.BIG ? 1 : 0);
			putWord(position, data);
			return position;
		}
	}

	public int appendDWord(int data) {
		synchronized (this) {
			value = Arrays.copyOf(value, value.length + 4);

			int position = value.length - 1 - (getEndian() == Endian.BIG ? 3 : 0);
			putDWord(position, data);
			return position;
		}
	}

	public int appendQWord(long data) {
		synchronized (this) {
			value = Arrays.copyOf(value, value.length + 8);

			int position = value.length - 1 - (getEndian() == Endian.BIG ? 7 : 0);
			putQWord(position, data);
			return position;
		}
	}
}
