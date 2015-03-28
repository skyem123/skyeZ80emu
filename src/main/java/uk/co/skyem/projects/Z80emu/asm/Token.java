package uk.co.skyem.projects.Z80emu.asm;

abstract class Token {

	public static abstract class Instruction extends Token {
		public void create(Assembler assembler, String arguments) {

		}

		public abstract void insert(Program program, int offset);
	}

	public static class Label extends Token {
		public final String name;

		public Label(String name) {
			this.name = name;
		}
	}

	public static abstract class ASMDirective extends Instruction {

	}

	public static abstract class CPUInstruction extends Instruction {

	}

	public static class instructionLD extends CPUInstruction {

		@Override
		public void insert(Program program, int offset) {

		}
	}

	// TODO: get this working.
	/*public static class ORG extends ASMDirective {

	}*/
}
