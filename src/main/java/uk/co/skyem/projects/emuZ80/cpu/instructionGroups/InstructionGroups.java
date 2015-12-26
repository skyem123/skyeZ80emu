package uk.co.skyem.projects.emuZ80.cpu.instructionGroups;

import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed.UnprefixedInstruction;

public class InstructionGroups extends Instruction {

	public InstructionGroups(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	// NOTE: This *must* be below the constructor, since the instructions rely on the decoder...
	Instruction unprefixedInstruction = new UnprefixedInstruction(instructionDecoder);

	@Override
	public short runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		switch (splitInstruction.prefix) {
			case 0:
				return unprefixedInstruction.runOpcode(splitInstruction);
			default:
				throw new IllegalStateException("Someone forgot a prefix or we shouldn't be here");
		}
	}
}
