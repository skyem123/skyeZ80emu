package uk.co.skyem.projects.Z80emu;

import uk.co.skyem.projects.Z80emu.bus.IBusDevice;

public class Core {
	IBusDevice memoryBus;
	IBusDevice IOBus;
	Registers registers = new Registers();

	public Core(IBusDevice memory, IBusDevice IO) {
		memoryBus = memory;
		IOBus = IO;
		reset();
	}

	public void reset() {
		registers.clear();
	}

	public void cycle() {
		byte data = memoryBus.getByte(registers.getProgramCounter());
		// Convert the data into an instruction
		// TODO: move this to a different class?
		switch (data) {
			case 0x00: // NOP
				registers.incrementProgramCounter();
				break;
			case 0x01: // LD BC,nn (load 16 bits into register BC)
				break;
			default:   // Error out
				break;
		}

	}
}
