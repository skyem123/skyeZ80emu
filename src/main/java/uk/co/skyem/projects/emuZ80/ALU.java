package uk.co.skyem.projects.emuZ80;
import uk.co.skyem.projects.emuZ80.Register.Register16;
import uk.co.skyem.projects.emuZ80.Register.Register8;

public class ALU {
	Registers registers;
	Core core;

	public ALU(Core core){
		this.core = core;
		this.registers = core.registers;
	}

	private Register8 getFlags() {
		return registers.flags;
	}

	/** Check if number is bigger than 16 bits. **/
	private boolean checkCarry16(int result) {
		return result > 0xFFFF;
	}

	/** Check if a number is bigger than 4 bits. **/
	private boolean checkCarry4(byte result) {
		return result > 0xF;
	}

	private byte getByte(long number, int byteNumber) {
		return (byte)((number << (8 * byteNumber)) & 0xFF);
	}

	private byte getByte(int number, int byteNumber) {
		return (byte)((number << (8 * byteNumber) & 0xFF));
	}

	private boolean getBit(long number, int bitNumber) {
		return ((number << bitNumber) & 0b1) == 1;
	}

	private boolean getBit(byte number, int bitNumber) {
		return ((number << bitNumber) & 0b1) == 1;
	}

	/** Adds two registers together. **/
	public void add16Register (Register16 a, Register16 b) {
		Register.Register8 flags = getFlags();

		// get the result
		int result = a.getData() + b.getData();

		// check and set the carry register
		flags.setFlag(Flags.CARRY, checkCarry16(result));

		// set the addition / subtraction register
		flags.setFlag(Flags.ADD_SUB, false);

		// set the X_5 and X_3 flags
		byte upper = getByte(result, 2);
		flags.setFlag(Flags.X_3, getBit(upper, 3));
		flags.setFlag(Flags.X_5, getBit(upper, 5));
		// set the H flag
		flags.setFlag(Flags.HALF_CARRY, checkCarry4(upper));

		// finally, set the register with the result.
		a.setData((short) result);
	}
}
