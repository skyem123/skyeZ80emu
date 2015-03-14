package uk.co.skyem.projects.Z80emu.util.buffer;

import uk.co.skyem.projects.Z80emu.util.buffer.AbstractByteBuffer;

import java.util.Arrays;

/**
 * Created by skye on 2015-03-14.
 */
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
	public void putByte(int position, byte data) {

	}

	@Override
	public byte getByte(int position) {
		return 0;
	}

	public void insert(int offset, byte[] data) {
		if (offset < 0) {
			throw new IllegalArgumentException("Can't insert at negative position index " + offset);
		}
		int size = value.length;

		// Expand the byte array to fit in the extra opcodes
		value = Arrays.copyOf(value, size + data.length);
		// Move everything after the offset to the end of the byte array
		System.arraycopy(value, offset, value, offset + data.length, size - offset);
		// Insert the data
		System.arraycopy(data, 0, value, offset, data.length);
	}

	public void append(byte[] opcodes) {
		int size = value.length;
		value = Arrays.copyOf(value, value.length + opcodes.length);
		System.arraycopy(opcodes, 0, value, size, opcodes.length);
	}
}
