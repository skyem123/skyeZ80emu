package uk.co.skyem.projects.Z80emu.bus;

import uk.co.skyem.projects.Z80emu.util.buffer.AbstractByteBuffer;

public abstract class SimpleBusDevice extends AbstractByteBuffer implements IBusDevice {
	int offset = 0;

	@Override
	public byte[] getBytes(int position, int amount) {
		synchronized (this) {
			byte[] result = new byte[amount];
			for (; amount > 0; amount--) {
				result[amount - 1] = this.getByte(position + amount - 1);
			}
			return result;
		}
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
