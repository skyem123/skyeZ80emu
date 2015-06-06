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

	/** Check if number is bigger than 8 bits. **/
	private boolean checkCarry8(short result) {
		return result > 0xFF;
	}

	/** Check if number is bigger than 8 bits. **/
	private boolean checkCarry8(int result) {
		return result > 0xFF;
	}

	/** Check if a number is bigger than 4 bits. **/
	private boolean checkCarry4(int result) {
		return result > 0xF;
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

		// check and set the carry flag
		flags.setFlag(Flags.CARRY, checkCarry16(result));

		// set the addition / subtraction flag
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

	/** Sets a register to a 16 bit immediate value **/
	public void load16(Register16 register, short data) {
		register.setData(data);
	}

	public void increment16(Register16 register) {
		register.increment();
	}

	public void decrement16(Register16 register) {
		register.increment();
	}

	private byte incDec8SetFlags(int result) {
		Register8 flags = getFlags();
		// Check to see if there is an overflow
		// TODO: Is this correct?
		flags.setFlag(Flags.PARITY_OVERFLOW, checkCarry8(result));

		// set the addition / subtraction flag
		flags.setFlag(Flags.ADD_SUB, false);

		// set the X_5 and X_3 flags
		flags.setFlag(Flags.X_3, getBit(result, 3));
		flags.setFlag(Flags.X_5, getBit(result, 5));

		// set the H flag
		flags.setFlag(Flags.HALF_CARRY, checkCarry4(result));

		// set the Z flag
		flags.setFlag(Flags.ZERO, result == 0);

		byte resultByte = (byte)result;

		// set the S flag
		flags.setFlag(Flags.SIGN, resultByte < 0);

		return resultByte;
	}

	public void increment8(Register8 register) {
		int result = register.getData() + 1;

		// set the register with the result after setting flags
		register.setData(incDec8SetFlags(result));
	}

	public void decrement8(Register8 register) {
		int result = register.getData() - 1;

		// set the register with the result after setting flags
		register.setData(incDec8SetFlags(result));
	}

	public void load8(Register8 register, byte data) {
		register.setData(data);
	}
}
