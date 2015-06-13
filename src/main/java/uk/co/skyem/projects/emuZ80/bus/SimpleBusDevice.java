package uk.co.skyem.projects.emuZ80.bus;

import uk.co.skyem.projects.emuZ80.util.buffer.AbstractByteBuffer;

public abstract class SimpleBusDevice extends AbstractByteBuffer implements IBusDevice {
	int offset = 0;

	public SimpleBusDevice() { }

	public SimpleBusDevice(int offset) {
		this.offset = offset;
	}

	@Override
	public byte[] getBytes(int position, int amount) {
		byte[] result = new byte[amount];
		synchronized (this) {
			for (; amount > 0; amount--) {
				result[amount - 1] = this.getByte(position + amount - 1);
			}
		}
		return result;
	}

	@Override
	public int getOffset() {
		return offset;
	}

	@Override
	public void changeOffset(int offset) {
		this.offset = offset;
	}
}
