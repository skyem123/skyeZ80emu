package uk.co.skyem.projects.Z80emu;

import uk.co.skyem.projects.Z80emu.Register.*;
import uk.co.skyem.projects.Z80emu.Registers;
import uk.co.skyem.projects.Z80emu.asm.Arguments;
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
	// TODO: Use actual registers instead? Enum has link to registers?
	private enum Register {
		B, C, D, E, H, L, HL, A
	}

	private static final Register[] registerTable = {
		Register.B, Register.C, Register.D, Register.E, Register.H, Register.L, Register.HL, Register.A
	};

	// Register Pairs featuring SP
	private enum RegisterPair {
		BC, DE, HL, SP, AF
	}

	private static final RegisterPair[] registerPairTable1 = {
		RegisterPair.BC, RegisterPair.DE, RegisterPair.HL, RegisterPair.SP
	};

	private static final RegisterPair[] registerPairTable2 = {
		RegisterPair.BC, RegisterPair.DE, RegisterPair.HL, RegisterPair.AF
	};

	private enum Condition {
		NZ, Z, NC, C, PO, PE, P, M
	}

	private static final Condition[] conditionTable = {
		Condition.NZ, Condition.Z, Condition.NC, Condition.C, Condition.PO, Condition.PE, Condition.P, Condition.M
	};

	// Arithmetic and logic operations
	private enum AluOP {
		ADD_A, ACD_A, SUB, SBC_A, AND, XOR, OR, CP
	}

	private static final AluOP[] AluTable = {
		AluOP.ADD_A, AluOP.ACD_A, AluOP.SUB, AluOP.SBC_A, AluOP.AND, AluOP.OR, AluOP.OR, AluOP.CP
	};

	// Rotation and shift operations
	private enum RotationOP {
		RLC, RRC, RL, RR, SLA, SRA, SLL, SRL
	}

	private static final RotationOP[] rotationTable = {
		RotationOP.RLC, RotationOP.RRC, RotationOP.RL, RotationOP.RR, RotationOP.SLA, RotationOP.SRA, RotationOP.SLL, RotationOP.SRL
	};

	private enum InterruptMode {
		ZERO, ZERO_ONE, ONE, TWO
	}

	private static final InterruptMode[] interruptModeTable = {
		InterruptMode.ZERO, InterruptMode.ZERO_ONE, InterruptMode.ONE, InterruptMode.TWO, InterruptMode.ZERO, InterruptMode.ZERO_ONE, InterruptMode.ONE, InterruptMode.TWO
	};

	private enum BlockInstruction {
		LDI, CPI, INI, OUTI,
		LDD, CPD, IND, OUTD,
		LDIR, CPIR, INIR, OTIR,
		LDDR, CPDR, INDR, OTDR
	}

	private static final BlockInstruction[][] BlockInstructionTable = {
		{},{},{},{},
		{ BlockInstruction.LDI, BlockInstruction.CPI, BlockInstruction.INI, BlockInstruction.OUTI},
		{ BlockInstruction.LDD, BlockInstruction.CPD, BlockInstruction.IND, BlockInstruction.OUTD},
		{ BlockInstruction.LDIR, BlockInstruction.CPIR, BlockInstruction.INIR, BlockInstruction.OTIR},
		{ BlockInstruction.LDDR, BlockInstruction.CPDR, BlockInstruction.INDR, BlockInstruction.OTDR},
	};

	private static class DecodedInstruction {
		DecodedInstruction(byte prefix, byte opcode, boolean secondPrefix, byte displacement, short immediateData) {
			this.prefix = prefix;
			this.opcode = opcode;
			this.secondPrefix = secondPrefix;
			this.displacement = displacement;
			this.immediateData = immediateData;
			// split up the opcode for further processing
			this.x = (byte) ((0b11000000 & this.opcode) >>> 6);
			this.y = (byte) ((0b00111000 & this.opcode) >>> 3);
			this.z = (byte)  (0b00000111 & this.opcode);
			this.p = (byte)  (0b110 & y);
			this.q = (0b001 & y) == 0b1;
		}

		// The instruction
		public byte prefix, opcode, displacement;
		short immediateData;
		boolean secondPrefix;

		// The split up opcode
		public byte x, y, z, p;
		public boolean q;
	}

	// TODO: Make it fetch the least data necessary.
	public DecodedInstruction decode(byte[] data) {
		byte prefix = 0;
		byte opcode;
		boolean secondPrefix = false;
		byte displacement = 0;
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

		return new DecodedInstruction(prefix, opcode, secondPrefix, displacement, immediateData);
	}

	// TODO: Better name
	// TODO: switch-case or if-else?
	public void runOpcode(DecodedInstruction decodedInstruction) {
		switch (decodedInstruction.x) {
			case 0: // x == 0
				switch (decodedInstruction.z) {
					case 0: // z == 0  // Misc instructions and relative jumps
						switch (decodedInstruction.y){
							case 0: // NOP
								break;
							case 1: // EX AF,AF'
								break;
							case 2: // DJNZ d(isplacement)
								break;
							case 3: // JR d(isplacement)
								break;
							case 4:case 5:case 6:case 7: // JR cc[y-4],d
								Condition condition = conditionTable[decodedInstruction.y - 4];
								break;
						}
						break;
					case 1: // z == 1  // 16bit load immediate and add
						if (decodedInstruction.q) { // immediate load
							// LD rp[p], nn
						} else { // add
							// ADD HL,rp[p]
						}
				}
				break;
			case 1:
				break;
		}
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
