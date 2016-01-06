package uk.co.skyem.projects.emuZ80.cpu;

import uk.co.skyem.projects.emuZ80.util.buffer.IByteHandler;

/**
 * As part of Project: "Let's try and keep anything that shouldn't be touching Core or Registers from
 * even having a pointer to it, because it is WAY TOO EASY to make a mistake in this, and that scares me."
 * By walling these groves that we call objects,
 * it's going to be a lot easier to keep potential mistakes from screwing everything up.
 *
 * And given the IX/IY situation, we need this kind of code separation.
 * It's not an optional thing.
 *
 * A small bonus... it also means we can do IO without rewriting it.
 * Created by gamemanj on 27/12/15.
 */
public class MemoryRouter {

	private IByteHandler memoryBus;

	public MemoryRouter(IByteHandler targetBus) {
		memoryBus = targetBus;
	}

	public void putByte(short address, Byte data) {
		memoryBus.putByte(safeShortToInt(address), data);
	}

	public void putWord(short address, Short data) {
		memoryBus.putByte(Short.toUnsignedInt(address), (byte)(data & 0x00FF));
		memoryBus.putByte(Short.toUnsignedInt(++address), (byte)((data & 0xFF00)>>8));
	}

	public byte getByte(short address) {
		return memoryBus.getByte(Short.toUnsignedInt(address));
	}

	public short getWord(short address) {
		byte l = memoryBus.getByte(Short.toUnsignedInt(address));
		byte h = memoryBus.getByte(Short.toUnsignedInt(++address));
		int lh = ((h << 8) & 0xFF00) | (l & 0xFF);
		return (short) (lh);
	}
}
