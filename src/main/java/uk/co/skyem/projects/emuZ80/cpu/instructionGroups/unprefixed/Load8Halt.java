package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.Instruction;

public class Load8Halt extends Instruction {
	public Load8Halt(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		// Exception to this instruction.
		// Replaces LD(HL),(HL)
		if (splitInstruction.z == 6 && splitInstruction.y == 6) {
			// HALT
			instructionDecoder.cpuCore.halt();
		}

		// LD r[y],r[z]
		instructionDecoder.getRegister(InstructionDecoder.registerTable[splitInstruction.y]).setData(instructionDecoder.getRegister(InstructionDecoder.registerTable[splitInstruction.z]));
	}
}
