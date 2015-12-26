package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.Core;
import uk.co.skyem.projects.emuZ80.cpu.Flags;
import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.Registers;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.Instruction;
import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder.Condition;

public class MiscInstruction extends Instruction {

	public MiscInstruction(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	private Registers registers = instructionDecoder.registers;
	private Core cpuCore = instructionDecoder.cpuCore;

	@Override
	public short runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		switch (splitInstruction.y) {
			case 0:
				// NOP -- Do nothing.
				break;
			case 1:
				// EX AF,AF' -- Swap AF with its shadow register.
				registers.swapRegisters(registers.REG_AF, registers.REG_AFS);
				break;
			case 2:
				// DJNZ d -- Decrement B by one, then relative jump if B is not zero.
				registers.REG_B.decrement();
				byte relJmp = splitInstruction.getByteInc();
				if (registers.REG_B.getData() != 0)
					return (short)(registers.getProgramCounter() + relJmp);
				break;
			case 3:
				// JR d -- Relative jump.
				return (short)(registers.getProgramCounter() + splitInstruction.getByteInc());
			case 4:
			case 5:
			case 6:
			case 7:
				// JR cc[y-4], d -- Relative jump if condition is met.
				Condition condition = InstructionDecoder.conditionTable[splitInstruction.y - 4];
				relJmp = splitInstruction.getByteInc();
				if (registers.flags.getFlag(condition.flagVal) == condition.expectedResult)
					return (short)(registers.getProgramCounter() + relJmp);
				break;
		}
		return splitInstruction.position;
	}
}
