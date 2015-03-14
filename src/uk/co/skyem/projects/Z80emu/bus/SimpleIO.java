package uk.co.skyem.projects.Z80emu.bus;

public class SimpleIO extends SimpleBusDevice{
	int address;

	public SimpleIO(int address) {
		this.address = address;
	}

	@Override
	public byte getByte(int address) {
		if (address == this.address) {
			try { return (byte)System.in.read(); }
			catch (java.io.IOException e) { throw new RuntimeException(e); }
		} else { return (byte)0x00; }
	}

	@Override
	public void putByte(int address, byte data) {
		if (address == this.address) {
			System.out.print(Character.toChars(data)[0]);
		}
	}
}
