package uk.co.skyem.projects.Z80emu.asm;

abstract class Token {

	public static abstract class Instruction extends Token {
		public Instruction(String arguments) {

		}

		public abstract void insert(Program program, int offset);
	}

	public static class Label extends Token {
		public String label;

		public Label(String label) {
			this.label = label;
		}
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

	public static class instructionLD extends CPUInstruction {
		public instructionLD(String code) {
			super(code);
		}

		@Override
		public void insert(Program program, int offset) {

		}
	}

	// TODO: get this working.
	/*public static class ORG extends ASMDirective {

	}*/
}
