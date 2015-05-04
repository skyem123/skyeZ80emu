package uk.co.skyem.projects.Z80emu.instructionGroups;

import uk.co.skyem.projects.Z80emu.InstructionDecoder;

public abstract class Instruction {
	public InstructionDecoder instructionDecoder;

	public Instruction(InstructionDecoder instructionDecoder) {

	}

	public abstract void runOpcode(InstructionDecoder.SplitInstruction splitInstruction);
}
