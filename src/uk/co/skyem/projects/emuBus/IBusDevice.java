package uk.co.skyem.projects.emuBus;

public interface IBusDevice {
	public void putByte(int position, byte data);

	public byte getByte(int position);

	public byte[] getBytes(int position, int amount);

	public void putBytes(int position, byte[] data);

	public short getWord(int position);

	public int getDWord(int position);

	public long getQWord(int position);

	public void putWord(int position, short data);

	public void putDWord(int position, int data);

	public void putQWord(int position, long data);
}
