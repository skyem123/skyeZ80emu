package uk.co.skyem.projects.emuZ80.cpu.instructionGroups;

import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;

public abstract class Instruction {
	public InstructionDecoder instructionDecoder;

	public Instruction(InstructionDecoder instructionDecoder) {

	}

	public abstract void runOpcode(InstructionDecoder.SplitInstruction splitInstruction);
}
