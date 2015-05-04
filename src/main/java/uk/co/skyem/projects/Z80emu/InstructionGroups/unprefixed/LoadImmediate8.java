package uk.co.skyem.projects.Z80emu.instructionGroups.unprefixed;

import uk.co.skyem.projects.Z80emu.InstructionDecoder;
import uk.co.skyem.projects.Z80emu.instructionGroups.Instruction;

public class LoadImmediate8 extends Instruction{
	public LoadImmediate8(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {

	}
}
