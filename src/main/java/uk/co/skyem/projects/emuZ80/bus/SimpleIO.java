package uk.co.skyem.projects.emuZ80.bus;

public class SimpleIO extends SimpleBusDevice {
	int offset;

	public SimpleIO(int offset) {
		super(offset);
	}

	@Override
	/**
	 * Read a character from the console.
	 */
	public byte getByte(int address) {
		if (address == this.offset) {
			try {
				return (byte) System.in.read();
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
		if (address == this.offset) {
			System.out.print(Character.toChars(data)[0]);
		}
	}
}
