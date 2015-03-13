package uk.co.skyem.projects.emuBus;

/**
 * An object that can connect as a device on the bus.
 */
public interface IBusDevice {

	/**
	 * Gives the device a byte.
	 *
	 * @param position The 'address' of the data
	 * @param data The data being given to the device
	 */
	public void putByte(int position, byte data);

	/**
	 * Gets a byte from the device.
	 *
	 * @param position The 'address' of the data
	 * @return The data from the device
	 */
	public byte getByte(int position);

	/**
	 * Gets a number of bytes from the device.
	 *
	 * @param position The start 'address' of the data
	 * @param amount The number of bytes to get
	 * @return a byte array with the data from the device
	 */
	public byte[] getBytes(int position, int amount);

	/**
	 * Gives a number of bytes to the device.
	 *
	 * @param position The start 'address' of the data
	 * @param data a byte array with the data to give to the device
	 */
	public void putBytes(int position, byte[] data);

	/**
	 * Gets a word (two bytes) from the device.
	 *
	 * @param position The 'address' of the data
	 * @return The data from the device
	 */
	public short getWord(int position);

	/**
	 * Gets a dword (two words, or four bytes) from the device.
	 *
	 * @param position The 'address' of the data
	 * @return The data from the device
	 */
	public int getDWord(int position);

	/**
	 * Gets a qword (two dwords, or four words, or eight bytes) from the device.
	 *
	 * @param position The 'address' of the data
	 * @return The data from the device
	 */
	public long getQWord(int position);

	/**
	 * Gives the device a word (two bytes).
	 *
	 * @param position The 'address' of the data
	 * @param data The data being given to the device
	 */
	public void putWord(int position, short data);

	/**
	 * Gives the device a dword (two words, or four bytes).
	 *
	 * @param position The 'address' of the data
	 * @param data The data being given to the device
	 */
	public void putDWord(int position, int data);

	/**
	 * Gives the device a qword (two dwords, or four words, or eight bytes).
	 *
	 * @param position The 'address' of the data
	 * @param data The data being given to the device
	 */
	public void putQWord(int position, long data);
}
