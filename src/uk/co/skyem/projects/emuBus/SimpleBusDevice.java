package uk.co.skyem.projects.emuBus;

public abstract class SimpleBusDevice implements IBusDevice {
	public byte[] getBytes(int position, int amount) {
		byte[] result = new byte[amount];
		for (; amount > 0; amount--) {
			result[amount - 1] = this.getByte(position + amount - 1);
		}
		return result;
	}

	public short getWord(int position) {
		return (short) (getByte(position + 1) & 0xFF | (getByte(position) << 8 & 0xFF00));
	}

	public int getDWord(int position) {
		return getWord(position + 2) & 0xFFFF | (getWord(position) << 16 & 0xFFFF0000);
	}

	public long getQWord(int position) {
		return getDWord(position + 4) & 0xFFFFFFFFL | ((long)getDWord(position) << 32 & 0xFFFFFFFF00000000L);
	}

	public void putWord(int position, short data) {
		putByte(position + 1, (byte)(data & 0xFF));
		putByte(position, (byte)(data >>> 8));
	}

	public void putDWord(int position, int data) {
		putWord(position + 2, (short) (data & 0xFFFF));
		putWord(position, (short) (data >>> 16));
	}

	public void putQWord(int position, long data) {
		putDWord(position + 4, (int)(data & 0xFFFFFFFFL));
		putDWord(position, (int)(data >>> 32));
	}

	public void putBytes(int position, byte[] bytes) {
		for (int i = 0; i < bytes.length; i++) {
			putByte(position + i, bytes[i]);
		}
	}
}
