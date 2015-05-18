package uk.co.skyem.projects.emuZ80.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.instructionGroups.Instruction;

public class LoadImmediate8 extends Instruction{
	public LoadImmediate8(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {

	}
}
