package uk.co.skyem.projects.Z80emu;

/**
 * Created by skye on 2015-03-11.
 */
public interface IBusDevice {
	public void putByte(int position, byte data);
	public byte getByte(int position);
	public String getHexStringByte(int position);
}
