package uk.co.skyem.projects.Z80emu.asm;

abstract class Token {

	public static abstract class Opcode extends Token {
		public String line;

		public String operation;
		// FIXME: Is this the correct name?
		public String operands;

		public Opcode(String line) {
			this.line = line;
			operation = line.split("\\s",1)[0];
		}
	}

	public static class ASMDirective extends Opcode {
		public ASMDirective(String code) {
			super(code);
		}
	}

	public static class CPUInstruction extends Opcode {
		public CPUInstruction(String code) {
			super(code);
		}
	}

}
