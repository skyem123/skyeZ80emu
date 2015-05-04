package uk.co.skyem.projects.Z80emu.instructionGroups;

import uk.co.skyem.projects.Z80emu.InstructionDecoder;

public interface IOpcodeHandler {
	// abstract static is not allowed...
	static void runOpcode(InstructionDecoder.DecodedInstruction decodedInstruction) {}
}
