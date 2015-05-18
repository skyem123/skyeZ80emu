package uk.co.skyem.projects.emuZ80.bus;

import uk.co.skyem.projects.emuZ80.util.buffer.IByteBuffer;

/**
 * An object that can connect as a device on the bus.
 */
public interface IBusDevice extends IByteBuffer {

	/**
	 * Gets the offset of this bus device
	 * @return the current offset
	 */
	public int getOffset();

	/**
	 * Sets the offset of this bus device
	 * @param offset the offset to change to
	 */
	public void changeOffset(int offset);
}
