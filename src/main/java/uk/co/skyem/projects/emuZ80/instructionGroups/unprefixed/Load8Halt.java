package uk.co.skyem.projects.emuZ80.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.instructionGroups.Instruction;

public class Load8Halt extends Instruction {
	public Load8Halt(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {

	}
}
