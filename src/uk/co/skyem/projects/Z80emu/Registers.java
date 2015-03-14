package uk.co.skyem.projects.Z80emu;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import uk.co.skyem.projects.Z80emu.Register.*;

public class Registers {

	protected static List<Register.Register8> registers = new ArrayList<>();

	public static final int FLAG_CARRY = 1;
	public static final int FLAG_ADD_SUB_1 = 1 << 1;
	public static final int FLAG_PARITY_OVERFLOW = 1 << 2;
	public static final int FLAG_X_1 = 1 << 3;
	public static final int FLAG_ADD_SUB_2 = 1 << 4;
	public static final int FLAG_X_2 = 1 << 5;
	public static final int FLAG_ZERO = 1 << 6;
	public static final int FLAG_SIGN = 1 << 7;

	public Register.Register8 getRegister(int register) {
		return registers.get(register);
	}

	public List<Register.Register8> getRegisters() {
		return Collections.unmodifiableList(registers);
	}

	public static Register16 programCounter = new Register16();
	public static Register16 stackPointer = new Register16();

	public static Register8 flags = new Register8();
	public static Register8 shadowFlags = new Register8();

	public static Register8 REG_A = new Register8();
	public static Register8 REG_B = new Register8();
	public static Register8 REG_C = new Register8();
	public static Register8 REG_D = new Register8();
	public static Register8 REG_E = new Register8();
	public static Register8 REG_H = new Register8();
	public static Register8 REG_L = new Register8();

	public static Register16 REG_AF = new Register16(REG_A, flags);
	public static Register16 REG_BC = new Register16(REG_B, REG_C);
	public static Register16 REG_DE = new Register16(REG_D, REG_E);
	public static Register16 REG_HL = new Register16(REG_H, REG_L);

	public static Register8 REG_AS = new Register8();
	public static Register8 REG_BS = new Register8();
	public static Register8 REG_CS = new Register8();
	public static Register8 REG_DS = new Register8();
	public static Register8 REG_ES = new Register8();
	public static Register8 REG_HS = new Register8();
	public static Register8 REG_LS = new Register8();

	public static Register16 REG_AFS = new Register16(REG_AS, shadowFlags);
	public static Register16 REG_BCS = new Register16(REG_BS, REG_C);
	public static Register16 REG_DES = new Register16(REG_DS, REG_E);
	public static Register16 REG_HLS = new Register16(REG_HS, REG_L);

	/**
	 * Clears all registers.
	 */
	public static void clear() {
		for(Register.Register8 register : registers) {
			register.clear();
		}
	}

	public static short getProgramCounter() {
		return programCounter.getData();
	}

	public static void setProgramCounter(short index) {
		programCounter.setData(index);
	}

	public static short incrementProgramCounter() {
		programCounter.setData((short) (programCounter.getData() + 1));
		return programCounter.getData();
	}

	public static short incrementProgramCounter(short value) {
		programCounter.setData((short) (programCounter.getData() + value));
		return programCounter.getData();
	}
}
