package uk.co.skyem.projects.Z80emu.asm;

public class AssemblerException extends Exception {

	public AssemblerException(int lineNumber, String line, String cause) {
		super(cause + " at line [" + lineNumber + "]: \"" + line + "\"");
	}

	public static class InvalidTokenException extends AssemblerException {
		public InvalidTokenException(String name, boolean assemblerDirective, int lineNumber, String line) {
			super(lineNumber, line, "Found an invalid " + (assemblerDirective ? "ASM directive " : "Instruction ") + name);
		}
	}
}
