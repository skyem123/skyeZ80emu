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

	// The real Z80 CPU reads it as two bytes... so let's do that.
	private short read16bits(short address) {
		short data = (short) (memoryBus.getByte(address) & 0xFF | (memoryBus.getByte(address - 1) << 8 & 0xFF00));
		System.out.println(Main.toHexString(data));
		return data;
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
				registers.incrementProgramCounter((short) 2);
				registers.REG_BC.setData(read16bits(registers.getProgramCounter()));
				break;
			default:   // Error out
				break;
		}

	}
}
