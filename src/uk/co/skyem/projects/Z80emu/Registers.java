package uk.co.skyem.projects.Z80emu;

public class Registers {

	private short programCounter;

	private boolean flagCarry;
	private boolean flagAddSub;
	private boolean flagParityOverflow;
	private boolean flagZero;
	private boolean flagSign;
	private boolean flagX1;

	Registers() {
		reset();
	}

	public void reset() {
		programCounter = 0;
		flagCarry = false;
		flagAddSub = false;
		flagParityOverflow = false;
		flagZero = false;
		flagSign = false;
		flagX1 = false;
	}

	public short getProgramCounter() {
		return programCounter;
	}

	public short setProgramCounter(short value) {
		return programCounter = value;
	}

	public short incrementProgramCounter() {
		return programCounter++;
	}

	public short incrementProgramCounter(short value) {
		return programCounter += value;
	}

	public boolean getCarryFlag() {
		return flagCarry;
	}

	public boolean getAddSubFlag() {
		return flagAddSub;
	}

	public boolean getParityOverflowFlag() {
		return flagParityOverflow;
	}

	public boolean getZeroFlag() {
		return flagZero;
	}

	public boolean getSignFlag() {
		return flagSign;
	}

	public boolean getX1Flag() {
		return flagX1;
	}

	public void setCarryFlag(boolean flag) {
		flagCarry = flag;
	}

	public void setAddSubFlag(boolean flag) {
		flagAddSub = flag;
	}

	public void setParityOverflowFlag(boolean flag) {
		flagParityOverflow = flag;
	}

	public void setZeroFlag(boolean flag) {
		flagZero = flag;
	}

	public void setSignFlag(boolean flag) {
		flagSign = flag;
	}

	public void setX1Flag(boolean flag) {
		flagX1 = flag;
	}

	public byte getFlagsRegister() {
		// TODO: Make this code nicer
		byte result = 0;
		if (flagCarry) result =+ 1;
		if (flagAddSub) result =+ 2;
		if (flagParityOverflow) result =+ 4;
		if (flagX1) result =+32;
		if (flagZero) result =+ 8;
		if (flagSign) result =+ 16;
		if (flagX1) result =+32;
		return result;
	}
}
