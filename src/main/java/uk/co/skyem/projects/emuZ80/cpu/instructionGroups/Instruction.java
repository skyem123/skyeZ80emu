package uk.co.skyem.projects.emuZ80.cpu.instructionGroups;

import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;

public abstract class Instruction {
	public InstructionDecoder instructionDecoder;

	public Instruction(InstructionDecoder instructionDecoder) {
		this.instructionDecoder = instructionDecoder;
	}

	/**
	 * Runs an opcode. Do *NOT* use setInstructionCounter from here unless you don't mind your alterations being overwritten.
	 * Instead, use your return value. That's what it's there for!
	 * @param splitInstruction The instruction.
	 * @return The position to set the PC to. Usually, you should return splitInstruction.position, which will go forward as you read data.
	 */
	public abstract short runOpcode(InstructionDecoder.SplitInstruction splitInstruction);
}
