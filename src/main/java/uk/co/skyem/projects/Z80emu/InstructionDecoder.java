package uk.co.skyem.projects.Z80emu;

import uk.co.skyem.projects.Z80emu.Register.*;
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

	// 8 Bit registers
	// HL is (HL)
	private enum RegisterTable {
		B, C, D, E, H, L, HL, A
	}

	// Register Pairs featuring SP
	private enum RegisterPairTable {
		BC, DE, HL, SP
	}

	// Register Pairs featuring AF
	private enum RegisterPair2Table {
		BC, DE, HL, AF
	}

	private enum ConditionTable {
		NZ, Z, NC, C, PO, PE, P, M
	}

	// Arithmetic and logic operations
	private enum AluTable {
		ADD_A, ACD_A, SUB, SBC_A, AND, XOR, OR, CP
	}

	// Rotation and shift operations
	private enum RotationTable {
		RLC, RRC, RL, RR, SLA, SRA, SLL, SRL
	}

	private enum InterruptModeTable {
		ZERO, ZERO_ONE, ONE, TWO, ZERO_B, ZERO_ONE_B, ONE_B, TWO_B
	}

	private enum BlockInstructions {
		LDI, CPI, INI, OUTI,
		LDD, CPD, IND, OUTD,
		LDIR, CPIR, INIR, OTIR,
		LDDR, CPDR, INDR, OTDR
	}

	private BlockInstructions[][] BlockInstructionTable = {
		{},{},{},{},
		{BlockInstructions.LDI, BlockInstructions.CPI, BlockInstructions.INI, BlockInstructions.OUTI},
		{BlockInstructions.LDD, BlockInstructions.CPD, BlockInstructions.IND, BlockInstructions.OUTD},
		{BlockInstructions.LDIR, BlockInstructions.CPIR, BlockInstructions.INIR, BlockInstructions.OTIR},
		{BlockInstructions.LDDR, BlockInstructions.CPDR, BlockInstructions.INDR, BlockInstructions.OTDR},
	};

	// TODO: Make it fetch the least data necessary.
	public void decode(byte[] data) {
		byte prefix = 0;
		byte opcode;
		boolean secondPrefix = false;
		byte displacement;
		short immediateData;

		int position = 0;
		// The cleanest way I could do this...
		// Find out the prefix (if there is one)
		switch ((int) data[position]) {
			case 0xDD:case 0xFD:case 0xCB:case 0xED:
				prefix = data[position++];
		}
		// Is there a second prefix?
		if (prefix == 0xFD || prefix == 0xDD)
			if (data[position] == 0xCB) {
				secondPrefix = true;
				// Get the displacement byte
				displacement = data[++position];
				++position;
			}
		// Get the opcode of the instruction
		opcode = data[position++];
		// Get the immediate data (if there is no second prefix)
		immediateData = secondPrefix ? data[position] : 0;

		// split up the opcode for further processing
		byte x = (byte) ((0b11000000 & opcode) >>> 6);
		byte y = (byte) ((0b00111000 & opcode) >>> 3);
		byte z = (byte)  (0b00000111 & opcode);
		byte p = (byte)  (0b110 & y);
		boolean q = (0b001 & y) == 0b1;
	}

	public void decode(long data) {
		// Split the long into a byte array to give to decode
	}

	// TODO: Is there a more appropriate name?
	public void cycle() {
		short position = registers.getProgramCounter();
		byte currentOpcode = memoryBuffer.getByte(position);
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
			default:   // Be unpredictable! \o/
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
