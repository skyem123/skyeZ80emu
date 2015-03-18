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
	// TODO: use a bytebuffer!
	private short read16bits(short address) {
		return (short) (memoryBus.getByte(address) & 0xFF | (memoryBus.getByte(address - 1) << 8 & 0xFF00));
	}


	public void reset() {
		registers.clear();
	}

	public void cycle() {
		byte data = memoryBus.getByte(registers.getProgramCounter());
		// Whenever opcode is fetched
		registers.incrementRefreshCounter();
		// Convert the data into an instruction
		// TODO: move this to a different class?
		switch (data) {
			case 0x00: // NOP
				registers.incrementProgramCounter();
				break;
			case 0x01: // LD BC,nn
				// Put 16 bits (nn) into register BC
				registers.REG_BC.setData(read16bits((byte) (registers.getProgramCounter() + 2)));
				registers.incrementProgramCounter((short) 3);
				break;
			case 0x02: // LD (BC),A
				// Put the data in register A into the memory address specified in BC
				memoryBus.putByte(registers.REG_BC.getData(), registers.REG_A.getData());
				break;
			case 0x03: // INC BC
				// Increment (add one) to BC
				registers.REG_BC.increment();
				break;
			default:   // Error out
				break;
		}

	}
}
