package uk.co.skyem.projects.Z80emu;

import uk.co.skyem.projects.Z80emu.bus.IBusDevice;

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

	// The real Z80 CPU reads it as two bytes... so let's do that.
	// TODO: use a bytebuffer!
	protected short read16bits(short address) {
		return (short) (memoryBus.getByte(address) & 0xFF | (memoryBus.getByte(address - 1) << 8 & 0xFF00));
	}

	public void reset() {
		registers.clear();
	}

	public void cycle() {
		instructionDecoder.cycle();
	}
}
