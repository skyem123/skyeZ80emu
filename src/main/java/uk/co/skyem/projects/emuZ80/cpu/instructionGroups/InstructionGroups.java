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
			// NOTE BEFORE YOU ADD BIT PREFIX!!!!
			// 0xCB isn't a prefix, it's an opcode.
			// Else it wouldn't be possible to prefix it, since there can only be 1 prefix in the Z80.
			// (see: the fact ED ignores indexes)
			case 0:
				return unprefixedInstruction.runOpcode(splitInstruction);
			// NOTE BEFORE YOU ADD BIT PREFIX!!!! SEE ABOVE
			default:
				throw new IllegalStateException("Someone forgot a prefix or we shouldn't be here");
		}
	}
}
