package uk.co.skyem.projects.emuZ80;

public class Flags {

	public static final int CARRY = 1;
	public static final int ADD_SUB_1 = 1 << 1;
	public static final int PARITY_OVERFLOW = 1 << 2;
	public static final int X_1 = 1 << 3;
	public static final int ADD_SUB_2 = 1 << 4;
	public static final int X_2 = 1 << 5;
	public static final int ZERO = 1 << 6;
	public static final int SIGN = 1 << 7;

	private Flags() {
	}
}
