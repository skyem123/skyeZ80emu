package uk.co.skyem.projects.Z80emu.util;


public class InternalException extends RuntimeException {
	public InternalException() {
		super("Internal error! Please report everything to skyem123 because its all his fault.\nSorry, there is no more info than I can give...");
	}

	public InternalException(Exception originalException) {
		super("Internal error! Please report everything to skyem123 because its all his fault.", originalException);
	}
}
