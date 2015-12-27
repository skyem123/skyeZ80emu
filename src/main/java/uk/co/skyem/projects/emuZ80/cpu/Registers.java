package uk.co.skyem.projects.emuZ80.cpu;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import uk.co.skyem.projects.emuZ80.cpu.Register.*;

/**
 * THE ONLY THINGS THAT SHOULD ACCESS THIS CLASS ARE:
 * External Debuggers and such. They're allowed, they're not in-CPU code that could screw up with IX-prefixes
 * InstructionDecoder
 * Occasionally Core
 * THINGS THAT SHOULD NEVER EVER TOUCH THIS CLASS:
 * Anything in instructionGroups. Anything.
 * If it touches something in this class, it's a bug.
 * The reason for this is because of IX/IY.
 * If it wasn't for that, this would be a *LOT* easier.
 */
public class Registers {

	protected List<Register> registers = new ArrayList<>();

	public Register getRegister(int register) {
		return registers.get(register);
	}

	public List<Register> getRegisters() {
		return Collections.unmodifiableList(registers);
	}

	// halted is a flag, and it sort of fits here?
	public boolean halted = false;

	// NOTE: The order of this is important
	// KEEP IN SYNC WITH ASSEMBLER/ARGUMENTS
	public Register16 programCounter = register16();
	public Register8 interruptVector = register8();
	public Register8 refreshCounter = register8();

	public Register16 stackPointer = register16();

	// NOTE: This instance is referred to by the ALU, because it needs to modify flags.
	//       Core is responsible for doing this.
	//       Anything else that needs to modify flags can do so via the ALU's flags instance.
	//       Believe it or not, it'll be a lot easier to debug IX/IY problems
	//       when people aren't messing up the Find Usages on this class.
	//       To restate: DO NOT ACCESS THIS CLASS!!!
	public Register8 flags = register8();

	public Register8 REG_A = register8();
	public Register8 REG_B = register8();
	public Register8 REG_C = register8();
	public Register8 REG_D = register8();
	public Register8 REG_E = register8();
	public Register8 REG_H = register8();
	public Register8 REG_L = register8();
	public Register8 REG_IXH = register8();
	public Register8 REG_IXL = register8();
	public Register8 REG_IYH = register8();
	public Register8 REG_IYL = register8();

	public Register16 REG_AF = register16(REG_A, flags);
	public Register16 REG_BC = register16(REG_B, REG_C);
	public Register16 REG_DE = register16(REG_D, REG_E);
	public Register16 REG_HL = register16(REG_H, REG_L);

	public Register16 REG_IX = register16(REG_IXH, REG_IXL);
	public Register16 REG_IY = register16(REG_IYH, REG_IYL);

	public Register8 REG_AS = register8();
	public Register8 REG_BS = register8();
	public Register8 REG_CS = register8();
	public Register8 REG_DS = register8();
	public Register8 REG_ES = register8();
	public Register8 REG_HS = register8();
	public Register8 REG_LS = register8();

	public Register8 shadowFlags = register8();
	public Register16 REG_AFS = register16(REG_AS, shadowFlags);
	public Register16 REG_BCS = register16(REG_BS, REG_CS);
	public Register16 REG_DES = register16(REG_DS, REG_ES);
	public Register16 REG_HLS = register16(REG_HS, REG_LS);

	public Register8 register8() {
		Register8 register = new Register8();
		registers.add(register);
		return register;
	}

	public Register16 register16() {
		Register16 register = new Register16();
		registers.add(register);
		return register;
	}

	public Register16 register16(Register8 upper, Register8 lower) {
		return new Register16(upper, lower);
	}

	// TODO: This should work b, but it isn't very pretty.
	// Note that the Z80 only swaps register pairs.
	public void swapRegisters(Register16 a, Register16 b) {
		short oldData = a.getData();
		a.setData(b);
		b.setData(oldData);
	}

	/**
	 * Clears all registers.
	 */
	public void clear() {
		halted = false;
		registers.forEach(Register::clear);
	}

	public short getProgramCounter() {
		return programCounter.getData();
	}

	public byte incrementRefreshCounter() {
		refreshCounter.increment();
		return refreshCounter.getData();
	}

	public byte incrementRefreshCounter(byte value) {
		refreshCounter.increment(value);
		return refreshCounter.getData();
	}
}
