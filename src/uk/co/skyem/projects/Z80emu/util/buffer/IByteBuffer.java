package uk.co.skyem.projects.Z80emu.util.buffer;

public interface IByteBuffer {
	/**
	 * Gives the buffer a byte.
	 *
	 * @param position The 'address' of the data
	 * @param data     The data being given to the device
	 */
	public void putByte(int position, byte data);

	/**
	 * Gets a byte from the buffer.
	 *
	 * @param position The 'address' of the data
	 * @return The data from the device
	 */
	public byte getByte(int position);

	/**
	 * Gets a number of bytes from the buffer.
	 *
	 * @param position The start 'address' of the data
	 * @param amount   The number of bytes to get
	 * @return a byte array with the data from the buffer
	 */
	public byte[] getBytes(int position, int amount);

	/**
	 * Gives a number of bytes to the buffer.
	 *
	 * @param position The start 'address' of the data
	 * @param data     a byte array with the data to give to the buffer
	 */
	public void putBytes(int position, byte[] data);

	/**
	 * Gets a word (two bytes) from the buffer.
	 *
	 * @param position The 'address' of the data
	 * @return The data from the buffer
	 */
	public short getWord(int position);

	/**
	 * Gets a dword (two words, or four bytes) from the buffer.
	 *
	 * @param position The 'address' of the data
	 * @return The data from the buffer
	 */
	public int getDWord(int position);

	/**
	 * Gets a qword (two dwords, or four words, or eight bytes) from the buffer.
	 *
	 * @param position The 'address' of the data
	 * @return The data from the buffer
	 */
	public long getQWord(int position);

	/**
	 * Gives the buffer a word (two bytes).
	 *
	 * @param position The 'address' of the data
	 * @param data     The data being given to the buffer
	 */
	public void putWord(int position, short data);

	/**
	 * Gives the buffer a dword (two words, or four bytes).
	 *
	 * @param position The 'address' of the data
	 * @param data     The data being given to the buffer
	 */
	public void putDWord(int position, int data);

	/**
	 * Gives the buffer a qword (two dwords, or four words, or eight bytes).
	 *
	 * @param position The 'address' of the data
	 * @param data     The data being given to the buffer
	 */
	public void putQWord(int position, long data);
}
