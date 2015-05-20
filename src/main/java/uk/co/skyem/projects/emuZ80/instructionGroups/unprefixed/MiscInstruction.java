package uk.co.skyem.projects.emuZ80.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.Register;
import uk.co.skyem.projects.emuZ80.instructionGroups.Instruction;

public class MiscInstruction extends Instruction {

	public MiscInstruction(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		switch (splitInstruction.y) {
			case 0:
				// NOP -- Do nothing.
				break;
			case 1:
				// EX AF,AF' -- Swap AF with its shadow register.
				Register.Register16 AFT = instructionDecoder.registers.REG_AF;
				instructionDecoder.registers.REG_AF = instructionDecoder.registers.REG_AFS;
				instructionDecoder.registers.REG_AFS = AFT;
				break;
			case 2:
				// DJNZ d -- Decrement B by one, then relative jump if B is not zero.
				instructionDecoder.registers.REG_B.decrement();
				instructionDecoder.cpuCore.relativeJump(splitInstruction.buffer.getByte(splitInstruction.position++));
				break;
			case 3:
				// JR d -- Relative jump.
				// TODO
				break;
			case 4:case 5:case 6:case 7:
				// JR cc[y-4], d -- Relative jump if condition is met.
				// TODO
				break;
		}
	}
}
