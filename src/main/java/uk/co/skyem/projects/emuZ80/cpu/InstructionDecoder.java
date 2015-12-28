package uk.co.skyem.projects.emuZ80.cpu;

import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.IndexPrefix;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.InstructionGroups;
import uk.co.skyem.projects.emuZ80.cpu.Register.*;

public class InstructionDecoder {
	// NOTE: This field is internal to InstructionDecoder, Instructions must not access them.
	// Why? Because there's a pretty good chance you'll break *something* if you do.
	// Even accesses from inside InstructionDecoder should be kept in mind,
	// in case there are any more exceptions that need to be added (meaning more stuff to route through the getRegister gateways)
	private Registers rawRegisters;
	public ALU alu;

	private InstructionGroups instructionGroups;

	InstructionDecoder(Registers rawReg, ALU cpuAlu) {
		rawRegisters = rawReg;
		alu = cpuAlu;
		// instructionGroups needs everything initialized
		instructionGroups = new InstructionGroups(this);
	}

	/*
	 * ----------START Functions Instructions Should Use
	 */

	public void specialExceptionEXAF() {
		rawRegisters.swapRegisters(rawRegisters.REG_AF, rawRegisters.REG_AFS);
	}

	public void specialExceptionEXPtrSPHL(SplitInstruction idx) {
		short sp = rawRegisters.stackPointer.getData();
		MemoryRegister8 lower = new MemoryRegister8(alu.memRouter, sp);
		MemoryRegister8 upper = new MemoryRegister8(alu.memRouter, (short) ((sp + 1) & 0xFFFF));
		rawRegisters.swapRegisters(new Register16(upper, lower), getRegisterPair(RegisterPair.HL, idx));
	}

	public void specialExceptionEXX() {
		// NOTE: EXX is unaffected by IX/IY, unlike EXPtrSPHL
		rawRegisters.swapRegisters(rawRegisters.REG_BC, rawRegisters.REG_BCS);
		rawRegisters.swapRegisters(rawRegisters.REG_DE, rawRegisters.REG_DES);
		rawRegisters.swapRegisters(rawRegisters.REG_HL, rawRegisters.REG_HLS);
	}

	/*
	 * NOTE: This function is the main gateway for 8-bit
	 * register access. This *CAN* have side-effects under the following 3 conditions:
	 * 1. You use HL, the virtual indirect register, and you have memory-mapped IO.
	 * 2. You use HL, and the prefix was set, so SplitInstruction is changed to disable future accesses.
	 *    (see: " and any other instances of H and L will be unaffected.")
	 * 3. You do both, at the same time. Because why not.
	 *
	 * Any access to HL MUST be performed before reading more bytes from the following:
	 * Other registers
	 * Other usages of H, L and the HL RegisterPair in the same instruction
	 * ESPECIALLY AND PARTICULARLY immediate data
	 *
	 * The relevant splitinstruction field will be modified so that all future accesses work correctly.
	 * This is horrible, but it prevents as much potential for screwups as possible.
	 * Note that the system will automatically detect and prevent screwups.
	 * If you see the exception "Ensure HL-Indirect Register8 access happens before other HL accesses!",
	 * then you've screwed up, and you should do what it it says.
	 *
	 * Please, FFS, follow these guidelines or IX/IY will break.
	 * Please?
	 */
	public uk.co.skyem.projects.emuZ80.cpu.Register.Register8 getRegister(Register register, SplitInstruction ins) {
		switch (register) {
			case A:
				// NOTE: If this was supposed to be REG_L, leave a comment
				return rawRegisters.REG_A;
			case B:
				return rawRegisters.REG_B;
			case C:
				return rawRegisters.REG_C;
			case D:
				return rawRegisters.REG_D;
			case E:
				return rawRegisters.REG_E;
			case H:
				ins.screwupDetectFlag = true;
				switch (ins.index) {
					case IX:
						return rawRegisters.REG_IXH;
					case IY:
						return rawRegisters.REG_IYH;
					case None:
						return rawRegisters.REG_H;
					default:
						throw new UnsupportedOperationException("Shouldn't go here - unknown IndexPrefix");
				}
			case L:
				ins.screwupDetectFlag = true;
				switch (ins.index) {
					case IX:
						return rawRegisters.REG_IXL;
					case IY:
						return rawRegisters.REG_IYL;
					case None:
						return rawRegisters.REG_L;
					default:
						throw new UnsupportedOperationException("Shouldn't go here - unknown IndexPrefix");
				}
			case HL:
				if (ins.screwupDetectFlag)
					throw new UnsupportedOperationException("Someone forgot this: Read from HL-Indirect before any other HL-based register. This is for IX/IY compatibility's sake.");
				ins.screwupDetectFlag = true;
				Register16 reg = rawRegisters.REG_HL;
				byte ofs = 0;
				switch (ins.index) {
					case IX:
						reg = rawRegisters.REG_IX;
						ofs = ins.getByteInc();
						break;
					case IY:
						reg = rawRegisters.REG_IY;
						ofs = ins.getByteInc();
						break;
					case None:
						break;
					default:
						throw new UnsupportedOperationException("Shouldn't go here - unknown IndexPrefix");
				}
				ins.index = IndexPrefix.None; // future accesses in the same instruction are not affected.
				// why & 0xFFFF? Oracle JVMs don't like casting to short, THAT'S WHY
				return new MemoryRegister8(alu.memRouter, (short)((reg.getData() + Byte.toUnsignedInt(ofs)) & 0xFFFF));
		}
		throw new RuntimeException("This shouldn't happen / get here.");
	}
	public uk.co.skyem.projects.emuZ80.cpu.Register.Register16 getRegisterPair(RegisterPair registerPair, SplitInstruction idx) {
		switch (registerPair) {
			case BC:
				return rawRegisters.REG_BC;
			case DE:
				return rawRegisters.REG_DE;
			case HL:
				idx.screwupDetectFlag = true;
				switch (idx.index) {
					case IX:
						return rawRegisters.REG_IX;
					case IY:
						return rawRegisters.REG_IY;
					case None:
						return rawRegisters.REG_HL;
					default:
						throw new UnsupportedOperationException("Shouldn't go here - unknown IndexPrefix");
				}
			case SP:
				return rawRegisters.stackPointer;
			case AF:
				return rawRegisters.REG_AF;
		}
		throw new RuntimeException("This shouldn't happen / get here.");
	}

	/**
	 * Push a byte onto the stack
	 **/
	public void pushByte(byte b) {
		rawRegisters.stackPointer.decrement();
		alu.memRouter.putByte(rawRegisters.stackPointer.getData(), b);
	}

	/**
	 * Push a word onto the stack
	 * h is stored first, then l, so when read it's little-endian
	 **/
	public void pushWord(short v) {
		pushByte((byte)((v & 0xFF00) >> 8));
		pushByte((byte)(v & 0xFF));
	}

	/**
	 * Pop a word from the stack
	 **/
	public short popWord() {
		short value = alu.memRouter.getWord(rawRegisters.stackPointer.getData());
		rawRegisters.stackPointer.increment((short) 2);
		return value;
	}

	/**
	 * Pop a byte from the stack
	 **/
	public byte popByte() {
		byte v = alu.memRouter.getByte(rawRegisters.stackPointer.getData());
		rawRegisters.stackPointer.increment();
		return v;
	}

	public short getProgramCounter() {
		return rawRegisters.getProgramCounter();
	}

	public void halt() {
		rawRegisters.halted = true;
	}

	// Get the full IO address for everything in the 0xED block.
	// The reason it's like this is more complex than that really,
	// but this only happens in (and happens in all) Extended Prefix IO opcodes.
	// Note that this; http://www.z80.info/zip/z80-documented.pdf
	// says: The INI/INIR/IND/INDR instructions use BC after decrementing B, and the
	//       OUTI/OTIR/OUTD/OTDR instructions before.
	public short getIOAddress_EXT(boolean ini_ind) {
		return (short) ((rawRegisters.REG_BC.getData() - (ini_ind ? 0x10 : 0)) & 0xFFFF);
	}

	// Get the full IO address for in A, (*) and out A, (*)
	public short getIOAddress_A(byte immediate) {
		return (short) (((rawRegisters.REG_A.getData() << 8) & 0xFF00) | (immediate & 0xFF));
	}

	/*
	 * ----------END Things Instructions Should Use
	 */

	// 8 Bit registers
	// HL is (HL)
	// TODO: Use actual registers instead? Enum has link to registers?
	public enum Register {
		B, C, D, E, H, L, HL, A
	}

	public static final Register[] registerTable = {
			Register.B, Register.C, Register.D, Register.E, Register.H, Register.L, Register.HL, Register.A
	};


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

	// The conditions now contain the situation in which they're supposed to trigger.
	// This saves copy and pasting a switch
	// flags.getFlag(c.flagVal) == c.expectedResult
	public enum Condition {
		NZ(Flags.ZERO, false), Z(Flags.ZERO, true),
		NC(Flags.CARRY, false), C(Flags.CARRY, true),
		PO(Flags.PARITY_OVERFLOW, false), PE(Flags.PARITY_OVERFLOW, true),
		P(Flags.SIGN, false), M(Flags.SIGN, true);
		public final int flagVal;
		public final boolean expectedResult;
		Condition(int flag, boolean val) {
			flagVal = flag;
			expectedResult = val;
		}
	}

	public static final Condition[] conditionTable = {
			Condition.NZ, Condition.Z, Condition.NC, Condition.C, Condition.PO, Condition.PE, Condition.P, Condition.M
	};

	// Arithmetic and logic operations
	public enum AluOP {
		ADD_A, ADC_A, SUB, SBC_A, AND, XOR, OR, CP
	}

	public static final AluOP[] AluTable = {
			AluOP.ADD_A, AluOP.ADC_A, AluOP.SUB, AluOP.SBC_A, AluOP.AND, AluOP.XOR, AluOP.OR, AluOP.CP
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

	/*
	 * Used to be the decoded instruction, but now it tracks some secondary state of the instruction
	 * as it's executed. Stuff that's important, but generally shouldn't be noticable by most instructions.
	 * Correction. Most correctly written instructions.
	 */
	public static class SplitInstruction {

		SplitInstruction(byte prefix, byte opcode, MemoryRouter buffer, short position, IndexPrefix ip) {
			this.index = ip;
			this.prefix = prefix;
			this.opcode = opcode;
			this.buffer = buffer;
			this.position = position;
			// split up the opcode for further processing
			this.x = (byte) ((0b11000000 & this.opcode) >>> 6);
			this.y = (byte) ((0b00111000 & this.opcode) >>> 3);
			this.z = (byte) (0b00000111 & this.opcode);
			this.p = (byte) ((0b110 & y) >> 1);
			this.q = (0b001 & y) == 0b1;
		}

		// Used to get more data, like the immediate value and displacement.
		public MemoryRouter buffer;
		// position MUST be the byte after the opcode. also, MUST be incremented if data is fetched so that the CPU doesn't think the displacement byte / immediate data is the next instruction.
		public short position;

		// This is for your own good. It detects bugs that could mess with IX/IY handling.
		// This, along with the isolation of registers, should make the IX/IY system foolproof.
		// Assuming no fools take a hole punch to that isolation, or to this flag...
		public boolean screwupDetectFlag = false;

		// The instruction
		// NOTE: secondPrefix has been replaced with the prefix/IndexPrefix system
		// IndexPrefix is the IX/IY modifier...
		// while prefix is the 0xCB/OxED -type prefix.
		// In the case of 0xCB, the opcode isn't read.
		public IndexPrefix /*a certain magical*/ index;
		public byte prefix, opcode;

		// The split up opcode
		public byte x, y, z, p;
		public boolean q;

		public byte getByteInc() {
			screwupDetectFlag = true;
			return buffer.getByte(position++);
		}

		public short getShortInc() {
			screwupDetectFlag = true;
			short w = buffer.getWord(position);
			position += 2;
			return w;
		}
	}

	public SplitInstruction decode(short position, MemoryRouter buffer) {
		IndexPrefix index = IndexPrefix.None;
		byte prefix = 0; // can be CB or ED
		byte opcode;

		// this has to cover a *lot* of cases
		boolean readingPrefix = true;
		while (readingPrefix) {
			byte potentialPrefix = buffer.getByte(position);
			switch (Byte.toUnsignedInt(potentialPrefix)) {
				// magical index prefixes
				case 0xDD:
					index = IndexPrefix.IX;
					position++;
					break;
				case 0xFD:
					index = IndexPrefix.IY;
					position++;
					break;
				// general prefixes
				case 0xED:
					// ED is a prefix, so it disables other prefixes - like the Index prefix
					// The quote below should explain why there can only be 1 prefix:
					index = IndexPrefix.None; // "If the next byte is a DD, ED or FD prefix, the current DD prefix is ignored..."
					prefix = potentialPrefix;
					position++;
					readingPrefix = false;
					break;
				// 0xDB is NOT A PREFIX!!!
				// It's an opcode - that's why the displacement byte goes first after it.
				default:
					// no prefix, break without advancing position
					readingPrefix = false;
					break;
			}
		}

		// Get the opcode of the instruction
		opcode = buffer.getByte(position++);

		return new SplitInstruction(prefix, opcode, buffer, position, index);
	}

	// TODO: What are disassembly tables? Would they do better for this?
	public short runOpcode(SplitInstruction splitInstruction) {
		return instructionGroups.runOpcode(splitInstruction);
	}

	// This is the only thing that should ever write to programCounter.

	public void step() {
		short position = rawRegisters.getProgramCounter();
		short newPosition = runOpcode(decode(position, alu.memRouter));
		rawRegisters.programCounter.setData(newPosition);
	}

}
