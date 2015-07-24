package uk.co.skyem.projects.emuZ80.cpu;
import javafx.scene.transform.Rotate;
import uk.co.skyem.projects.emuZ80.cpu.Register.Register16;
import uk.co.skyem.projects.emuZ80.cpu.Register.Register8;

public class ALU {

	/**
	 * Rotates a long.
	 *
	 * @param data The long to be rotated.
	 * @param amount The amount to rotate the byte by, if greater than 0, rotate right, if less than 0, rotate left.
	 * @param dataLength The length of the data. If 0, assumes long length (65 bits).
	 * @return The rotated byte.
	 */
	// TODO: optimise?
	public static long rotate(long data, int dataLength, int amount) {
		if (dataLength == 0) dataLength = Long.SIZE;
		if (amount > 0) {
			// Rotate Right
			// Shift the whole thing right.
			long rotated = data >>> amount;
			// Get the chopped off bits and move them.
			long removed = data << dataLength - amount;
			// stick the chopped off bits back on.
			rotated = rotated | removed;
			return rotated;
		} else if (amount < 0) {
			amount = Math.abs(amount);
			// Rotate Left
			// Shift the whole thing left.
			long rotated = data << amount;
			// Get the chopped off bits and move them.
			long removed = data >>> dataLength - amount;
			// stick the chopped off bits back on.
			rotated = rotated | removed;
			return rotated;
		} else {
			// No Rotation
			return data;
		}
	}

	/**
	 * Rotates an integer.
	 *
	 * @param data The integer to be rotated.
	 * @param amount The amount to rotate the byte by, if greater than 0, rotate right, if less than 0, rotate left.
	 * @param dataLength The length of the data. If 0, assumes integer length (32 bits).
	 * @return The rotated byte.
	 */
	public static int rotate(int data, int dataLength, int amount) {
		if (dataLength == 0) dataLength = Integer.SIZE;
		if (amount > 0) {
			return (data >>> amount) | (data << dataLength - amount);
		} else if (amount < 0) {
			amount = Math.abs(amount);
			return (data << amount) | (data >>> dataLength - amount);
		} else {
			// No Rotation
			return data;
		}
	}

	/**
	 * Rotates a short.
	 *
	 * @param data The integer to be rotated.
	 * @param amount The amount to rotate the byte by, if greater than 0, rotate right, if less than 0, rotate left.
	 * @param dataLength The length of the data. If 0, assumes byte length (8 bits).
	 * @return The rotated byte.
	 */
	public static short rotate(short data, int dataLength, int amount) {
		if (dataLength == 0) dataLength = Byte.SIZE;
		if (amount > 0) {
			return (short)((data >>> amount) | (data << dataLength - amount));
		} else if (amount < 0) {
			amount = Math.abs(amount);
			return (short)((data << amount) | (data >>> dataLength - amount));
		} else {
			// No Rotation
			return data;
		}
	}

	/**
	 * Rotates a byte.
	 *
	 * @param data The integer to be rotated.
	 * @param amount The amount to rotate the byte by, if greater than 0, rotate right, if less than 0, rotate left.
	 * @param dataLength The length of the data. If 0, assumes byte length (8 bits).
	 * @return The rotated byte.
	 */
	public static byte rotate(byte data, int dataLength, int amount) {
		if (dataLength == 0) dataLength = Byte.SIZE;
		if (amount > 0) {
			return (byte)((data >>> amount) | (data << dataLength - amount));
		} else if (amount < 0) {
			amount = Math.abs(amount);
			return (byte)((data << amount) | (data >>> dataLength - amount));
		} else {
			// No Rotation
			return data;
		}
	}

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

	private boolean getBit(int number, int bitNumber) {
		return ((number << bitNumber) & 0b1) == 1;
	}

	private boolean getBit(byte number, int bitNumber) {
		return ((number << bitNumber) & 0b1) == 1;
	}

	private boolean betweenInclusive(int x, int min, int max) {
		return x>=min && x<=max;
	}

	/** Adds two registers together. **/
	public void add16Register (Register16 a, Register16 b) {
		Register8 flags = getFlags();

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

	/** Loads a register with a value from a memory address **/
	public void indirectLoad8(Register8 register, short address) {
		register.setData(core.memoryBuffer.getByte(address));
	}

	/** Load contents of register into memory location **/
	public void memoryLoad8(short address, Register8 register) {
		core.memoryBuffer.putByte(address, register.getData());
	}

	/** Loads a register with a value from a memory address **/
	public void indirectLoad16(Register16 register, short address) {
		register.setData(core.memoryBuffer.getWord(address));
	}

	/** Load contents of register into memory location **/
	public void memoryLoad16(short address, Register16 register) {
		core.memoryBuffer.putWord(address, register.getData());
	}

	/** Rotate register one bit to the left **/
	public void rotateRegisterLeft(Register register) {
		// These flags are reset.
		getFlags().setFlag(Flags.HALF_CARRY, false);
		getFlags().setFlag(Flags.ADD_SUB, false);
		register.rotateLeft(1);
	}

	/** Rotate register one bit to the right **/
	public void rotateRegisterRight(Register register) {
		// These flags are reset.
		getFlags().setFlag(Flags.HALF_CARRY, false);
		getFlags().setFlag(Flags.ADD_SUB, false);
		register.rotateRight(1);
	}

	/** Set the carry flag to the LSB and rotate the register one bit to the right. **/
	public void rotateRegisterRightCarry(Register register) {
		// Get the LSB and put it into the carry flag.
		getFlags().setFlag(Flags.CARRY, (register.getData().byteValue() & 0b1) == 0b1);
		// Rotate the register
		rotateRegisterRight(register);
	}

	/** Set the carry flag to the LSB and rotate the register one bit to the right. **/
	public void rotateRegisterLeftCarry(Register register) {
		// Get the MSB and put it into the carry flag.
		// TODO: Does this work?
		getFlags().setFlag(Flags.CARRY, (register.getData().longValue() & (0b1L << (register.getSize() - 1))) == 0b1);
		// Rotate the register
		rotateRegisterLeft(register);
	}

	/** Rotate a register right though carry. The carry flag is used as an extra bit. **/
	public void rotateRegisterRightThroughCarry(Register register) {
		// Get the previous contents of the carry flag
		boolean oldCarry = getFlags().getFlag(Flags.CARRY);
		// Get the LSB and put it into the carry flag.
		getFlags().setFlag(Flags.CARRY, (register.getData().byteValue() & 0b1) == 0b1);
		// Rotate the register
		rotateRegisterRight(register);
		// Copy the flag into the register.
		// TODO: Does this work?
		register.setFlag(0b1 << (register.getSize() - 1), oldCarry);
	}

	/** Rotate a register left though carry. The carry flag is used as an extra bit. **/
	public void rotateRegisterLeftThroughCarry(Register register) {
		// Get the previous contents of the carry flag
		boolean oldCarry = getFlags().getFlag(Flags.CARRY);
		// Get the MSB and put it into the carry flag.
		// TODO: Does this work?
		getFlags().setFlag(Flags.CARRY, (register.getData().longValue() & (0b1L << (register.getSize() - 1))) == 0b1);
		// Rotate the register
		rotateRegisterLeft(register);
		// Copy the flag into the register.
		register.setFlag(0b1, oldCarry);
	}

	/** Edits the accumulator after an operation with BCD input (ADD / SUB, etc...), to make the result BCD. **/
	public void bcdAdjust(Register8 toAdjust) {
		// Get the flags register
		Register8 flags = getFlags();

		// Get the data in the register
		int data = toAdjust.getData();

		// Get the lower nibble
		int lower = data & 0x0F;
		// Get the upper nibble
		int upper = (data >>> 4) & 0x0F;

		// Get the H flag
		boolean flagH = flags.getFlag(Flags.HALF_CARRY);
		// Get the C flag
		boolean flagC = flags.getFlag(Flags.CARRY);
		// Get the N flag
		boolean flagN = flags.getFlag(Flags.ADD_SUB);

		// Set the C flag
		if (!flagC) {
			if (betweenInclusive(upper, 0x09, 0x0F) && betweenInclusive(lower, 0x0A, 0x0F)) {
				flags.setFlag(Flags.CARRY, true);
			} else if (betweenInclusive(upper, 0x0A, 0x0F) && betweenInclusive(lower, 0x00, 0x09)) {
				flags.setFlag(Flags.CARRY, true);
			}
		}
		// Set the H flag
		if (!flagN) {
			if (betweenInclusive(lower, 0x00, 0x09)) {
				flags.setFlag(Flags.HALF_CARRY, false);
			} else if (betweenInclusive(lower, 0x0A, 0x0F)) {
				flags.setFlag(Flags.HALF_CARRY, true);
			}
		} else {
			if (flagH) {
				if (betweenInclusive(lower, 0x06, 0x0F)) {
					flags.setFlag(Flags.HALF_CARRY, false);
				}
				if (betweenInclusive(lower, 0x00, 0x05)) {
					flags.setFlag(Flags.HALF_CARRY, true);
				}
			}
		}

		// If the lower nibble is greater than 9 or there is a half carry, add 0x06 to the register
		if (lower > 0x09 && flagH)
			data += 0x06;
		// If the upper nibble is greater than 9 or there is a carry, add 0x60 to the register
		else if(upper > 0x09 && flagC)
			data += 0x60;

		// Set the S flag if the MSB is true
		flags.setFlag(Flags.SIGN, getBit(data, 7));
		// Set the Z flag if the result is 0
		if (data == 0) flags.setFlag(Flags.ZERO, true);
		else flags.setFlag(Flags.ZERO, false);
		// Set the P flag if the result is even
		flags.setFlag(Flags.PARITY_OVERFLOW, getBit(data, 0));

		// Set the other flags
		flags.setFlag(Flags.X_3, getBit(data, 3));
		flags.setFlag(Flags.X_5, getBit(data, 5));


		toAdjust.setData((byte) data);
	}

	/** Invert register **/
	public void complement(Register8 register) {
		int data = ~register.getData() & 0xFF;
		register.setData((byte) data);

		Register8 flags = getFlags();
		flags.setFlag(Flags.HALF_CARRY, true);
		flags.setFlag(Flags.ADD_SUB, true);

		flags.setFlag(Flags.X_3, getBit(data, 3));
		flags.setFlag(Flags.X_5, getBit(data, 5));
	}

	/** Sets the carry flag and some other flags **/
	public void setCarry() {
		Register8 flags = getFlags();

		flags.setFlag(Flags.CARRY, true);
		flags.setFlag(Flags.HALF_CARRY, false);
		flags.setFlag(Flags.ADD_SUB, false);

		byte regA = registers.REG_A.getData();
		flags.setFlag(Flags.X_3, getBit(regA, 3));
		flags.setFlag(Flags.X_5, getBit(regA, 5));
	}

	/** Invert the carry flag **/
	public void invertCarry() {
		Register8 flags = getFlags();

		flags.setFlag(Flags.HALF_CARRY, flags.getFlag(Flags.CARRY));
		flags.toggleFlag(Flags.CARRY);
		flags.setFlag(Flags.ADD_SUB, false);

		byte regA = registers.REG_A.getData();
		flags.setFlag(Flags.X_3, getBit(regA, 3));
		flags.setFlag(Flags.X_5, getBit(regA, 5));
	}
}
