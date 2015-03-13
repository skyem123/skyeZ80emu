package uk.co.skyem.projects.Z80emu;

public class Registers {

	private short programCounter;

	private boolean flagCarry;
	private boolean flagAddSub;
	private boolean flagParityOverflow;
	private boolean flagZero;
	private boolean flagSign;
	private boolean flagX;

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
		flagX = false;
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
}
