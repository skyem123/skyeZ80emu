package uk.co.skyem.projects.Z80emu.bus;

import uk.co.skyem.projects.Z80emu.util.buffer.IByteBuffer;

/**
 * An object that can connect as a device on the bus.
 */
public interface IBusDevice extends IByteBuffer {

	public int getOffset();

	public void changeOffset(int offset);
}
