package uk.co.skyem.projects.emuZ80.cpu;

import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.InstructionGroups;
import uk.co.skyem.projects.emuZ80.util.buffer.IByteHandler;
import uk.co.skyem.projects.emuZ80.cpu.Register.*;

public class InstructionDecoder {
	private IByteHandler memoryBuffer;
	public Registers registers;
	public Core cpuCore;
	public ALU alu;

	private InstructionGroups instructionGroups;

	InstructionDecoder(Core cpu) {
		memoryBuffer = cpu.memoryBuffer;
		cpuCore = cpu;
		registers = cpu.registers;
		instructionGroups = new InstructionGroups(this);
		alu = cpu.alu;
	}

	// 8 Bit registers
	// HL is (HL)
	// TODO: Use actual registers instead? Enum has link to registers?
	public enum Register {
		B, C, D, E, H, L, HL, A
	}

	public static final Register[] registerTable = {
			Register.B, Register.C, Register.D, Register.E, Register.H, Register.L, Register.HL, Register.A
	};

	public uk.co.skyem.projects.emuZ80.cpu.Register.Register8 getRegister(Register register) {
		switch (register) {
			case B:
				return registers.REG_B;
			case C:
				return registers.REG_C;
			case D:
				return registers.REG_D;
			case E:
				return registers.REG_E;
			case H:
				return registers.REG_H;
			case L:
				return registers.REG_L;
			case A:
				return registers.REG_L;
			case HL:
				return new MemoryRegister8(memoryBuffer, registers.REG_HL.getData());
		}
		throw new RuntimeException("This shouldn't happen / get here.");
	}

	// Register Pairs featuring SP
	public enum RegisterPair {
		BC, DE, HL, SP, AF
	}

	public static final RegisterPair[] registerPairTable1 = {
			RegisterPair.BC, RegisterPair.DE, RegisterPair.HL, RegisterPair.SP
	};

	public static final RegisterPair[] registerPairTable2 = {
			RegisterPair.BC, RegisterPair.DE, RegisterPair.HL, RegisterPair.AF
	};

	public void setRegisterPairData(RegisterPair registerPair, short data) {
		switch (registerPair) {
			case BC:
				registers.REG_BC.setData(data);
				break;
			case DE:
				registers.REG_DE.setData(data);
				break;
			case HL:
				registers.REG_HL.setData(data);
				break;
			case SP:
				registers.stackPointer.setData(data);
				break;
			case AF:
				registers.REG_AF.setData(data);
				break;
		}
	}

	public short getRegisterPairData(RegisterPair registerPair) {
		switch (registerPair) {
			case BC:
				return registers.REG_BC.getData();
			case DE:
				return registers.REG_DE.getData();
			case HL:
				return registers.REG_HL.getData();
			case SP:
				return registers.stackPointer.getData();
			case AF:
				return registers.REG_AF.getData();
		}
		throw new RuntimeException("This shouldn't happen / get here.");
	}

	public uk.co.skyem.projects.emuZ80.cpu.Register.Register16 getRegisterPair(RegisterPair registerPair) {
		switch (registerPair) {
			case BC:
				return registers.REG_BC;
			case DE:
				return registers.REG_DE;
			case HL:
				return registers.REG_HL;
			case SP:
				return registers.stackPointer;
			case AF:
				return registers.REG_AF;
		}
		throw new RuntimeException("This shouldn't happen / get here.");
	}

	public enum Condition {
		NZ, Z, NC, C, PO, PE, P, M
	}

	public static final Condition[] conditionTable = {
			Condition.NZ, Condition.Z, Condition.NC, Condition.C, Condition.PO, Condition.PE, Condition.P, Condition.M
	};

	// Arithmetic and logic operations
	public enum AluOP {
		ADD_A, ACD_A, SUB, SBC_A, AND, XOR, OR, CP
	}

	public static final AluOP[] AluTable = {
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
			{}, {}, {}, {},
			{BlockInstruction.LDI, BlockInstruction.CPI, BlockInstruction.INI, BlockInstruction.OUTI},
			{BlockInstruction.LDD, BlockInstruction.CPD, BlockInstruction.IND, BlockInstruction.OUTD},
			{BlockInstruction.LDIR, BlockInstruction.CPIR, BlockInstruction.INIR, BlockInstruction.OTIR},
			{BlockInstruction.LDDR, BlockInstruction.CPDR, BlockInstruction.INDR, BlockInstruction.OTDR},
	};

	public static class SplitInstruction {
		SplitInstruction(byte prefix, byte opcode, boolean secondPrefix, byte secondPrefixDisplacement, IByteHandler buffer, int position) {
			this.prefix = prefix;
			this.opcode = opcode;
			this.secondPrefix = secondPrefix;
			this.secondPrefixDisplacement = secondPrefixDisplacement;
			this.buffer = buffer;
			this.position = position;
			// split up the opcode for further processing
			this.x = (byte) ((0b11000000 & this.opcode) >>> 6);
			this.y = (byte) ((0b00111000 & this.opcode) >>> 3);
			this.z = (byte) (0b00000111 & this.opcode);
			this.p = (byte) (0b110 & y);
			this.q = (0b001 & y) == 0b1;
		}

		// Used to get more data, like the immediate value and displacement.
		public IByteHandler buffer;
		// position MUST be the byte after the opcode. also, MUST be incremented if data is fetched so that the CPU doesn't think the displacement byte / immediate data is the next instruction.
		public int position;

		// The instruction
		public byte prefix, opcode, secondPrefixDisplacement;
		short immediateData;
		boolean secondPrefix;

		// The split up opcode
		public byte x, y, z, p;
		public boolean q;

		public byte getByteInc() {
			return buffer.getByte(position++);
		}

		public short getShortInc() {
			return buffer.getWord(position++);
		}
	}

	public SplitInstruction decode(IByteHandler buffer, int position) {
		byte prefix = 0;
		byte opcode;
		boolean secondPrefix = false;
		byte secondPrefixDisplacement = 0;

		// The shortest way I could do this...
		// Find out the prefix (if there is one)
		switch ((int) buffer.getByte(position)) {
			case 0xDD:
			case 0xFD:
			case 0xCB:
			case 0xED:
				prefix = buffer.getByte(position++);
		}
		// Is there a second prefix?
		if (prefix == 0xFD || prefix == 0xDD)
			if (buffer.getByte(position) == 0xCB) {
				secondPrefix = true;
				// Get the displacement byte
				secondPrefixDisplacement = buffer.getByte(++position);
				++position;
			}
		// Get the opcode of the instruction
		opcode = buffer.getByte(position++);

		return new SplitInstruction(prefix, opcode, secondPrefix, secondPrefixDisplacement, buffer, position);
	}

	// TODO: What are disassembly tables? Would they do better for this?
	public void runOpcode(SplitInstruction splitInstruction) {
		instructionGroups.runOpcode(splitInstruction);
	}

	// TODO: Is there a more appropriate name?
	public void cycle() {
		short position = registers.getProgramCounter();
		// TODO: Short circuit for NOP?
		// FIXME! Does this actually work?
		runOpcode(decode(memoryBuffer, position));
		// TODO: Set the program counter to the final position (set by the instruction?)
		registers.incrementProgramCounter();
	}
}
