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
	protected short read16bits(short address) {
		return (short) (memoryBus.getByte(address) & 0xFF | (memoryBus.getByte(address - 1) << 8 & 0xFF00));
	}


	public void reset() {
		registers.clear();
	}

	public void cycle() {
		short position = registers.getProgramCounter();
		byte data = memoryBus.getByte(position);
		// Whenever opcode is fetched
		registers.incrementRefreshCounter();
		// Convert the data into an instruction
		// TODO: move this to a different class?
		switch (data) {
			case 0x00: // NOP
				break;
			case 0x01: // LD BC,nn
				// Put 16 bits (nn) into register BC
				registers.REG_BC.setData(read16bits((byte) (position + 2)));
				registers.incrementProgramCounter((short) 2);
				break;
			case 0x02: // LD (BC),A
				// Put the data in register A into the memory address specified in BC
				memoryBus.putByte(registers.REG_BC.getData(), registers.REG_A.getData());
				break;
			case 0x03: // INC BC
				// Increment (add one) to BC
				registers.REG_BC.increment();
				break;
			case 0x06: // LD B,n
				// Put 8 bits (n) into register B
				registers.REG_B.setData(memoryBus.getByte(position + 1));
				registers.incrementProgramCounter((short) 1);
				break;
			default:   // Error out
				break;
		}
		registers.incrementProgramCounter();

	}
}
