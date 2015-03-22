package uk.co.skyem.projects.Z80emu;

import uk.co.skyem.projects.Z80emu.Register.*;
import uk.co.skyem.projects.Z80emu.bus.IBusDevice;
import uk.co.skyem.projects.Z80emu.util.buffer.IByteBuffer;

public class InstructionDecoder {
	private IByteBuffer memoryBuffer;
	private Registers registers;
	private Core cpuCore;

	InstructionDecoder(Core cpu) {
		memoryBuffer = cpu.memoryBuffer;
		cpuCore = cpu;
		registers = cpu.registers;
	}

	private byte currentOpcode;
	private short position;

	// TODO: Is there a more appropriate name?
	public void cycle() {
		position = registers.getProgramCounter();
		currentOpcode = memoryBuffer.getByte(position);
		switch (currentOpcode) {
			case 0x00: // NOP
				System.out.println("NOP");
				break;
			case 0x01: // LD BC,nn
				System.out.println("LD BC,nn");
				// Put 16 bits (nn) into register BC
				LDRegisterMemory(registers.REG_BC, position + 1);
				registers.incrementProgramCounter((short) 2);
				break;
			case 0x02: // LD (BC),A
				System.out.println("LD (BC),A");
				// Put the data in register A into the memory address specified in BC
				LDMemoryRegister(registers.REG_BC.getData(), registers.REG_A);
				break;
			case 0x03: // INC BC
				System.out.println("INC BC");
				registers.REG_BC.increment();
				break;
			case 0x06: // LD B,n
				System.out.println("LD B,n");
				// Put 8 bits (n) into register B
				LDRegisterMemory(registers.REG_B, position + 1);
				registers.incrementProgramCounter((short) 1);
				break;
			default:   // Error out
				break;
		}
		registers.incrementProgramCounter();
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
		memoryBuffer.putByte(destination, source.getData());
	}

	private void LDMemoryRegister(short destination, Register16 source) {
		// TODO: Check that this is what it does.
		memoryBuffer.putWord(destination + 1, source.getData());
	}

	private void LDRegisterMemory(Register8 destination, int source) {
		destination.setData(memoryBuffer.getByte(source));
	}

	private void LDRegisterMemory(Register16 destination, int source) {
		// TODO: Check that this is what it does.
		destination.setData(memoryBuffer.getWord(source + 1));
	}
}
