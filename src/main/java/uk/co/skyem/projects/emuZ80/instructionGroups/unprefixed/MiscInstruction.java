package uk.co.skyem.projects.emuZ80.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.Core;
import uk.co.skyem.projects.emuZ80.Flags;
import uk.co.skyem.projects.emuZ80.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.Registers;
import uk.co.skyem.projects.emuZ80.instructionGroups.Instruction;
import uk.co.skyem.projects.emuZ80.InstructionDecoder.Condition;

public class MiscInstruction extends Instruction {

	public MiscInstruction(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}
	private Registers registers = instructionDecoder.registers;
	private Core cpuCore = instructionDecoder.cpuCore;

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
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
				if (registers.REG_B.getData() != 0)
					cpuCore.relativeJump(splitInstruction.getByteInc());
				break;
			case 3:
				// JR d -- Relative jump.
				cpuCore.relativeJump(splitInstruction.getByteInc());
				break;
			case 4:case 5:case 6:case 7:
				// JR cc[y-4], d -- Relative jump if condition is met.
				Condition condition = InstructionDecoder.conditionTable[splitInstruction.y - 4];
				switch (condition) {
					case NZ: // Jump if no zero flag
						if (!registers.flags.getFlag(Flags.ZERO))
							cpuCore.relativeJump(splitInstruction.getByteInc());
						break;
					case Z: // Jump if zero flag
						if (registers.flags.getFlag(Flags.ZERO))
							cpuCore.relativeJump(splitInstruction.getByteInc());
						break;
					case NC: // Jump if no carry flag
						if (!registers.flags.getFlag(Flags.CARRY))
							cpuCore.relativeJump(splitInstruction.getByteInc());
						break;
					case C: // Jump if carry flag
						if (registers.flags.getFlag(Flags.CARRY))
							cpuCore.relativeJump(splitInstruction.getByteInc());
						break;
					case PO: // Parity odd / Parity flag is false
						if (!registers.flags.getFlag(Flags.PARITY_OVERFLOW))
							cpuCore.relativeJump(splitInstruction.getByteInc());
						break;
					case PE:
						if (registers.flags.getFlag(Flags.PARITY_OVERFLOW))
							cpuCore.relativeJump(splitInstruction.getByteInc());
						break;
					case P: // Jump if sign positive / sign flag is false
						if (!registers.flags.getFlag(Flags.SIGN))
							cpuCore.relativeJump(splitInstruction.getByteInc());
						break;
					case M: // Jump if sign positive / sign flag is true
						if (registers.flags.getFlag(Flags.SIGN))
							cpuCore.relativeJump(splitInstruction.getByteInc());
						break;
				}
				break;
		}
	}
}
