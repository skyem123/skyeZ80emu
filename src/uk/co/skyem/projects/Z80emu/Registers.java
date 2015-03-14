package uk.co.skyem.projects.Z80emu;

public class Registers {

	public short programCounter;

	public boolean flagCarry;
	public boolean flagAddSub1;
	public boolean flagAddSub2;
	public boolean flagParityOverflow;
	public boolean flagZero;
	public boolean flagSign;
	public boolean flagX1;
	public boolean flagX2;

	Registers() {
		reset();
	}

	public void reset() {
		programCounter = 0;
		flagCarry = flagAddSub1 = flagAddSub2 = flagParityOverflow = flagZero = flagSign = flagX1 = flagX2 = false;
	}

	public short incrementProgramCounter() {
		return programCounter++;
	}

	public short incrementProgramCounter(short value) {
		return programCounter += value;
	}

	public byte getFlagsRegister() {
		return	(byte)((flagCarry ? 1 : 0) |
				(flagAddSub1 ? 1 << 1 : 0) |
				(flagParityOverflow ? 1 << 2 : 0) |
				(flagX1 ? 1 << 3 : 0) |
				(flagAddSub2 ? 1 << 4 : 0) |
				(flagX2 ? 1 << 5 : 0) |
				(flagZero ? 1 << 6 : 0) |
				(flagSign ? 1 << 7 : 0));
	}

	public void setFlagsRegister(byte data) {
		flagCarry = (data & 1) != 0;
		flagAddSub1 = (data & 1 << 1) != 0;
		flagParityOverflow = (data & 1 << 2) != 0;
		flagX1 = (data & 1 << 3) != 0;
		flagAddSub2 = (data & 1 << 4) != 0;
		flagX2 = (data & 1 << 5) != 0;
		flagZero = (data & 1 << 6) != 0;
		flagSign = (data & 1 << 7) != 0;
	}
}
