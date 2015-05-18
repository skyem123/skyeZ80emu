package uk.co.skyem.projects.emuZ80;

import uk.co.skyem.projects.emuZ80.bus.IBusDevice;
import uk.co.skyem.projects.emuZ80.util.buffer.AbstractByteBuffer;
import uk.co.skyem.projects.emuZ80.util.buffer.IByteBuffer;

public class Core {
	IBusDevice memoryBus;
	IBusDevice IOBus;
	Registers registers = new Registers();
	InstructionDecoder instructionDecoder;

	public Core(IBusDevice memory, IBusDevice IO) {
		memoryBus = memory;
		IOBus = IO;
		instructionDecoder = new InstructionDecoder(this);
		reset();
	}

	// The real Z80 CPU reads bytes at a time... so let's do that.
	IByteBuffer memoryBuffer = new AbstractByteBuffer(AbstractByteBuffer.Endian.LITTLE) {
		@Override
		public void putByte(int position, byte data) {
			memoryBus.putByte(position, data);
		}

		@Override
		public byte getByte(int position) {
			return memoryBus.getByte(position);
		}
	};

	public void reset() {
		registers.clear();
	}

	public void cycle() {
		instructionDecoder.cycle();
	}
}
