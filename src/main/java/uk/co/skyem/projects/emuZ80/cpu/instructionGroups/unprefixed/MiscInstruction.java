package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.*;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.Instruction;
import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder.Condition;

public class MiscInstruction extends Instruction {

	public MiscInstruction(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	@Override
	public short runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		switch (splitInstruction.y) {
			case 0:
				// NOP -- Do nothing.
				break;
			case 1:
				// EX AF,AF' -- Swap AF with its shadow register.
				instructionDecoder.specialExceptionEXAF();
				break;
			case 2:
				// DJNZ d -- Decrement B by one, then relative jump if B is not zero.
				Register.Register8 b = instructionDecoder.getRegister(InstructionDecoder.Register.B, splitInstruction);
				b.decrement();
				byte relJmp = splitInstruction.getByteInc();
				if (b.getData() != 0)
					return (short)((instructionDecoder.getProgramCounter() + relJmp) & 0xFFFF);
				break;
			case 3:
				// JR d -- Relative jump.
				// As byte is signed, there are no problems here.
				return (short)((instructionDecoder.getProgramCounter() + splitInstruction.getByteInc()) & 0xFFFF);
			case 4:
			case 5:
			case 6:
			case 7:
				// JR cc[y-4], d -- Relative jump if condition is met.
				Condition condition = InstructionDecoder.conditionTable[splitInstruction.y - 4];
				relJmp = splitInstruction.getByteInc();
				if (instructionDecoder.alu.flags.getFlag(condition.flagVal) == condition.expectedResult)
					return (short)((instructionDecoder.getProgramCounter() + relJmp) & 0xFFFF);
				break;
		}
		return splitInstruction.position;
	}
}
