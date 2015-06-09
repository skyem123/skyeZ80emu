package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.Instruction;

public class ALURegister extends Instruction {
	public ALURegister(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {

	}
}
