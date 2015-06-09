package uk.co.skyem.projects.emuZ80.cpu.instructionGroups;

import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed.UnprefixedInstruction;

public class InstructionGroups extends Instruction {
	Instruction unprefixedInstruction = new UnprefixedInstruction(instructionDecoder);

	public InstructionGroups(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		switch (splitInstruction.prefix) {
			case 0:
				unprefixedInstruction.runOpcode(splitInstruction);
		}
	}
}
