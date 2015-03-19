package uk.co.skyem.projects.Z80emu;

import uk.co.skyem.projects.Z80emu.Register.*;
import uk.co.skyem.projects.Z80emu.bus.IBusDevice;

public class InstructionDecoder {
	private IBusDevice memoryBus;
	private Registers registers;
	private Core cpuCore;

	InstructionDecoder(Core cpu) {
		memoryBus = cpu.memoryBus;
		cpuCore = cpu;
		registers = cpu.registers;
	}

	private byte currentOpcode;
	private short position;
	private int instuctionCycles;

	// TODO: Is there a more appropriate name?
	public void cycle() {
		if (instuctionCycles > 0) { // Continue an instruction
			--instuctionCycles;
			switch (currentOpcode) {
				case 0x00: // NOP
					break;
				case 0x01: // LD BC,nn
					// No clue what to do...
					break;
				case 0x02: // LD (BC),A
					// No clue what to do...
					break;
				case 0x03: // INC BC
					// No clue what to do...
					break;
				case 0x06: // LD B,n
					// No clue what to do...
					break;
				default:   // Error out
					break;
			}
		} else { // Start a new instruction
			position = registers.getProgramCounter();
			currentOpcode = memoryBus.getByte(position);
			switch (currentOpcode) {
				case 0x00: // NOP
					instuctionCycles = 3;
					break;
				case 0x01: // LD BC,nn
					instuctionCycles = 9;
					// Put 16 bits (nn) into register BC
					registers.REG_BC.setData(cpuCore.read16bits((byte) (position + 2)));
					registers.incrementProgramCounter((short) 2);
					break;
				case 0x02: // LD (BC),A
					instuctionCycles = 6;
					// Put the data in register A into the memory address specified in BC
					memoryBus.putByte(registers.REG_BC.getData(), registers.REG_A.getData());
					break;
				case 0x03: // INC BC
					instuctionCycles = 6;
					// Increment (add one) to BC
					registers.REG_BC.increment();
					break;
				case 0x06: // LD B,n
					instuctionCycles = 6;
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


	private void LDRegisterFixed(Register8 destination, byte data) {
		destination.setData(data);
	}

	private void LDRegisterFixed(Register16 destination, short data) {
		destination.setData(data);
	}

	private void LDRegisterRegister(Register8 destination, Register8 source) {
		destination.setData(source);
	}

	private void LDRegisterRegister(Register16 destination, Register16 source) {
		destination.setData(source);
	}

	private void LDMemoryRegister(short destination, Register8 source) {
		memoryBus.putByte(destination, source.getData());
	}

	private void LDMemoryRegister(short destination, Register16 source) {
		// TODO: Implement this
	}

	private void LDRegisterMemory(Register8 destination, short source) {
		destination.setData(memoryBus.getByte(source));
	}

	private void LDRegisterMemory(Register16 destination, short source) {
		destination.setData(cpuCore.read16bits(source));
	}
}
