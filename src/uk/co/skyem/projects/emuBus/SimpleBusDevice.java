package uk.co.skyem.projects.emuBus;

import uk.co.skyem.projects.Z80emu.Main;

public abstract class SimpleBusDevice implements IBusDevice {
	public byte[] getBytes(int position, int amount) {
		byte[] result = new byte[amount];
		for (; amount > 0; amount--) {
			result[amount - 1] = this.getByte(position + amount - 1);
		}
		return result;
	}

	public short getWord(int position) {
		return (short)( (short)( getByte(position) << 8) | getByte(position+1) );
	}

	public int getDWord(int position) {
		return (int)( (int)( getWord(position) << 16) | getWord(position+2) );
	}

	public long getQWord(int position) {
		return 0;
	}

	public void putWord(int position, short data) {

	}

	public void putDWord(int position, int data) {

	}

	public void putQWord(int position, long data) {

	}

	public void putBytes(int position, byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			//System.out.println(Main.toHexString(bytes[i]));
			putByte(position + i, bytes[i]);
		}
	}
}
