package uk.co.skyem.projects.Z80emu.instructionGroups.unprefixed;

import uk.co.skyem.projects.Z80emu.InstructionDecoder;
import uk.co.skyem.projects.Z80emu.instructionGroups.Instruction;

public class IndirectLoad extends Instruction{
	public IndirectLoad(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {

	}
}
