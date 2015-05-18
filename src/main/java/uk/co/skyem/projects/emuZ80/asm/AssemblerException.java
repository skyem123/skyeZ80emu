package uk.co.skyem.projects.emuZ80.asm;

public class AssemblerException extends Exception {

	public AssemblerException(int lineNumber, String line, String cause) {
		super(cause + " at line [" + lineNumber + "]: \"" + line + "\"");
	}

	public static class InvalidInstructionException extends AssemblerException {
		public InvalidInstructionException(String name, int lineNumber, String line) {
			super(lineNumber, line, "Found an invalid Instruction \"" + name + "\"");
		}
	}

	public static class InvalidASMDirectiveException extends AssemblerException {
		public InvalidASMDirectiveException(String name, int lineNumber, String line) {
			super(lineNumber, line, "Found an invalid ASM directive \"" + name + "\", Specified greedy for ASM directive, maybe you are looking for a normal Instruction?");
		}
	}

	public static class SyntaxException extends AssemblerException {
		public SyntaxException(int lineNumber, String line, String cause) {
			super(lineNumber, line, cause);
		}
		public SyntaxException(int lineNumber, String line) {
			super(lineNumber, line , "Syntax Error");
		}
	}
}
