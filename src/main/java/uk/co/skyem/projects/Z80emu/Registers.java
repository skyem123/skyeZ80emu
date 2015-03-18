package uk.co.skyem.projects.Z80emu;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import uk.co.skyem.projects.Z80emu.Register.*;

public class Registers {

	protected List<Register> registers = new ArrayList<>();

	public Register getRegister(int register) {
		return registers.get(register);
	}

	public List<Register> getRegisters() {
		return Collections.unmodifiableList(registers);
	}

	public Register16 programCounter = register16();
	public Register8 interruptVector = register8();
	public Register8 refreshCounter = register8();

	public Register16 indexY = register16();
	public Register16 indexX = register16();
	public Register16 stackPointer = register16();

	public Register8 flags = register8();
	public Register8 shadowFlags = register8();

	public Register8 REG_A = register8();
	public Register8 REG_B = register8();
	public Register8 REG_C = register8();
	public Register8 REG_D = register8();
	public Register8 REG_E = register8();
	public Register8 REG_H = register8();
	public Register8 REG_L = register8();

	public Register16 REG_AF = register16(REG_A, flags);
	public Register16 REG_BC = register16(REG_B, REG_C);
	public Register16 REG_DE = register16(REG_D, REG_E);
	public Register16 REG_HL = register16(REG_H, REG_L);

	public Register8 REG_AS = register8();
	public Register8 REG_BS = register8();
	public Register8 REG_CS = register8();
	public Register8 REG_DS = register8();
	public Register8 REG_ES = register8();
	public Register8 REG_HS = register8();
	public Register8 REG_LS = register8();

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

	/**
	 * Clears all registers.
	 */
	public void clear() {
		registers.forEach(Register::clear);
	}

	public short getProgramCounter() {
		return programCounter.getData();
	}

	public void setProgramCounter(short index) {
		programCounter.setData(index);
	}

	public short incrementProgramCounter() {
		programCounter.increment();
		return programCounter.getData();
	}

	public short incrementProgramCounter(short value) {
		programCounter.increment(value);
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
