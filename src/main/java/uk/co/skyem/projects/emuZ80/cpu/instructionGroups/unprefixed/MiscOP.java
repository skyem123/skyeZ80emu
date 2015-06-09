package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.ALU;
import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.Instruction;

public class MiscOP extends Instruction{
	ALU alu;
	public MiscOP(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
		this.alu = instructionDecoder.alu;
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		switch(splitInstruction.y) {
			// TODO: Implement instructions.
			case 0:
				// RLCA
				break;
			case 1:
				// RRCA
				break;
			case 2:
				// RLA
				break;
			case 3:
				// RRA
				break;
			case 4:
				// DAA
				break;
			case 5:
				// CPL
				break;
			case 6:
				// SCF
				break;
			case 7:
				// CCF
				break;
		}
	}
}
