package uk.co.skyem.projects.Z80emu.asm;

abstract class Token {

	public static abstract class Opcode extends Token {
		public String code;
		// FIXME: Is this the correct name?
		public String operands;

		public Opcode(String code) {
			this.code = code;
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
