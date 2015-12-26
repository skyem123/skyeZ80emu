package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.ALU;
import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.Register;
import uk.co.skyem.projects.emuZ80.cpu.Registers;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.Instruction;
import uk.co.skyem.projects.emuZ80.cpu.Register.*;

public class IndirectLoad extends Instruction {
	Registers registers;
	ALU alu;

	public IndirectLoad(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
		this.registers = instructionDecoder.registers;
		this.alu = instructionDecoder.alu;
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		// This is true most of the time.
		Register register = registers.REG_A;
		short address;

		switch (splitInstruction.p) {
			case 0:
				address = registers.REG_BC.getData();
				break;
			case 1:
				address = registers.REG_DE.getData();
				break;
			case 2:
				register = registers.REG_HL;
				// No, I don't need a break, I also need to run the next case as well.
			case 3:
				address = splitInstruction.getShortInc();
				break;
			default:
				throw new IllegalStateException("We should never be here.");
		}

		if (splitInstruction.q) {
			// Load contents of register into memory location
			if (register instanceof Register8) {
				alu.memoryLoad8(address, (Register8) register);
			} else if (register instanceof Register16) {
				alu.memoryLoad16(address, (Register16) register);
			}
		} else {
			// Load data at memory location into register
			if (register instanceof Register8) {
				alu.indirectLoad8((Register8) register, address);
			} else if (register instanceof Register16) {
				alu.indirectLoad16((Register16) register, address);
			}
		}
	}
}
