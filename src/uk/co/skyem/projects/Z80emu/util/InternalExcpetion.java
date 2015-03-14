package uk.co.skyem.projects.Z80emu.util;

/**
 * Created by skye on 2015-03-14.
 */
public class InternalExcpetion extends RuntimeException {
	public InternalExcpetion() {
		super("Internal error! Please report everything to skyem123 because its all his fault.");
	}
}
