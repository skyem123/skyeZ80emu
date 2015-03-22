package uk.co.skyem.projects.Z80emu;

import uk.co.skyem.projects.Z80emu.Register.*;
import uk.co.skyem.projects.Z80emu.bus.IBusDevice;

public class InstructionDecoder {
	private IBusDevice memoryBus;
	private Registers registers;
	private Core cpuCore;


	// The real Z80 CPU reads it as two bytes... so let's do that.
	// TODO: use a bytebuffer!
	private short read16bits(short address) {
		// FIXME: is there another way to do this?
		Thread.currentThread().suspend();
		byte data1 = memoryBus.getByte(address);
		Thread.currentThread().suspend();
		byte data2 = memoryBus.getByte(address - 1);
		return (short) (data1 & 0xFF | (data2 << 8 & 0xFF00));
	}

	InstructionDecoder(Core cpu) {
		memoryBus = cpu.memoryBus;
		cpuCore = cpu;
		registers = cpu.registers;
		cycleThread.start();
	}

	private byte currentOpcode;
	private short position;

	// FIXME: Is this needed?
	private Thread cycleThread = new Thread(() -> {
		Thread.currentThread().suspend();
		while (true) {
			position = registers.getProgramCounter();
			currentOpcode = memoryBus.getByte(position);
			switch (currentOpcode) {
				case 0x00: // NOP
					System.out.println("NOP");
					break;
				case 0x01: // LD BC,nn
					System.out.println("LD BC,nn");
					// Put 16 bits (nn) into register BC
					registers.REG_BC.setData(read16bits((short) (position + 2)));
					registers.incrementProgramCounter((short) 2);
					break;
				case 0x02: // LD (BC),A
					System.out.println("LD (BC),A");
					// Put the data in register A into the memory address specified in BC
					memoryBus.putByte(registers.REG_BC.getData(), registers.REG_A.getData());
					break;
				case 0x03: // INC BC
					System.out.println("INC BC");
					registers.REG_BC.increment();
					break;
				case 0x06: // LD B,n
					System.out.println("LD BC,n");
					// Put 8 bits (n) into register B
					registers.REG_B.setData(memoryBus.getByte(position + 1));
					registers.incrementProgramCounter((short) 1);
					break;
				default:   // Error out
					break;
				}
				registers.incrementProgramCounter();
				// FIXME: is there another way to do this?
				Thread.currentThread().suspend();
		}
	});

	// TODO: Is there a more appropriate name?
	public void cycle() {
		// FIXME: is there another way to do this?
		cycleThread.resume();
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
		destination.setData(read16bits(source));
	}
}
