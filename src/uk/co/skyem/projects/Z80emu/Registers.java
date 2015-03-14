package uk.co.skyem.projects.Z80emu;

public class Registers {

	private short programCounter;

	private boolean flagCarry;
	private boolean flagAddSub1;
	private boolean flagAddSub2;
	private boolean flagParityOverflow;
	private boolean flagZero;
	private boolean flagSign;
	private boolean flagX1;
	private boolean flagX2;

	Registers() {
		reset();
	}

	public void reset() {
		programCounter = 0;
		flagCarry = false;
		flagAddSub1 = false;
		flagAddSub2 = false;
		flagParityOverflow = false;
		flagZero = false;
		flagSign = false;
		flagX1 = false;
		flagX2 = false;
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

	public boolean getAddSubFlag1() {
		return flagAddSub1;
	}

	public boolean getAddSubFlag2() {
		return flagAddSub2;
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

	public boolean getXFlag1() {
		return flagX1;
	}

	public boolean getXFlag2() {
		return flagX2;
	}

	public void setCarryFlag(boolean flag) {
		flagCarry = flag;
	}

	public void setAddSubFlag1(boolean flag) {
		flagAddSub1 = flag;
	}

	public void setAddSubFlag2(boolean flag) {
		flagAddSub2 = flag;
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

	public void setXFlag1(boolean flag) {
		flagX1 = flag;
	}

	public void setXFlag2(boolean flag) {
		flagX2 = flag;
	}

	public byte getFlagsRegister() {
		byte result = 0;
		// TODO: is there a nicer way to do this?
		if (flagCarry) result += 1;
		if (flagAddSub1) result += 2;
		if (flagParityOverflow) result += 4;
		if (flagX1) result += 8;
		if (flagAddSub2) result += 16;
		if (flagX2) result += 32;
		if (flagZero) result += 64;
		if (flagSign) result += 128;
		return result;
	}

	public void setFlagsRegister(byte data) {
		// TODO: is there a nicer way to do this?
		if ((data | 0b11111110) == 0xFF) flagCarry = true;
		if ((data | 0b11111101) == 0xFF) flagAddSub1 = true;
		if ((data | 0b11111011) == 0xFF) flagParityOverflow = true;
		if ((data | 0b11110111) == 0xFF) flagX1 = true;
		if ((data | 0b11101111) == 0xFF) flagAddSub2 = true;
		if ((data | 0b11011111) == 0xFF) flagX2 = true;
		if ((data | 0b10111111) == 0xFF) flagZero = true;
		if ((data | 0b01111111) == 0xFF) flagSign = true;
	}
}
