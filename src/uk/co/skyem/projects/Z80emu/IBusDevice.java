package uk.co.skyem.projects.Z80emu;

public interface IBusDevice {
	public void putByte(int position, byte data);

	public byte getByte(int position);

	public byte[] getBytes(int position, int amount);

	public void putBytes(int position, byte[] data);
}
