package uk.co.skyem.projects.emuZ80.cpu;

public class Flags {

	public static final int CARRY = 1 << 0;
	public static final int ADD_SUB = 1 << 1;
	public static final int PARITY_OVERFLOW = 1 << 2;
	// copy of bit 3 (start counting from 0)
	public static final int X_3 = 1 << 3;
	public static final int HALF_CARRY = 1 << 4;
	// copy of bit 5
	public static final int X_5 = 1 << 5;
	public static final int ZERO = 1 << 6;
	public static final int SIGN = 1 << 7;

	private Flags() {
	}
}
