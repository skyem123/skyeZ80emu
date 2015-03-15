package uk.co.skyem.projects.Z80emu.asm;

abstract class Token {

	public static abstract class Instruction extends Token {
		public String line;

		public String operation;
		// FIXME: Is this the correct name?
		public String operands;

		public Instruction(String line) {
			this.line = line;
			operation = line.split("\\s", 1)[0];
		}

		public abstract void insert(Program program, int offset);
	}

	public static abstract class ASMDirective extends Instruction {
		public ASMDirective(String code) {
			super(code);
		}
	}

	public static abstract class CPUInstruction extends Instruction {
		public CPUInstruction(String code) {
			super(code);
		}
	}
}
