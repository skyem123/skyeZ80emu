package uk.co.skyem.projects.emuZ80.bus;

import uk.co.skyem.projects.emuZ80.util.buffer.AbstractByteBuffer;
import uk.co.skyem.projects.emuZ80.util.buffer.IByteHandler;

import java.nio.charset.Charset;

// NOTE: For sanity purposes, this ignores the upper 8 bits.
// Luckily, nobody cared.
public class SimpleIO extends AbstractByteBuffer implements IByteHandler {
	int offset;

	public SimpleIO(int offset) {
		this.offset = offset;
	}

	@Override
	/**
	 * Read a character from the console.
	 */
	public byte getByte(int address) {
		address &= 0xFF;
		if (address == this.offset) {
			try {
				int b = System.in.read() & 0xFF;
				return (byte) b;
			} catch (java.io.IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			return (byte) 0x00;
		}
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	/**
	 * Write a character to the console.
	 */
	public void putByte(int address, byte data) {
		address &= 0xFF;
		if (address == this.offset) {
			System.out.print(new String(new byte[]{data}, Charset.forName("ASCII")));
		}
	}
}
