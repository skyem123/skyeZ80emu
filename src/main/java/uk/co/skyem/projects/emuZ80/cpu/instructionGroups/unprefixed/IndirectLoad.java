package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.ALU;
import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.Register;
import uk.co.skyem.projects.emuZ80.cpu.Registers;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.Instruction;
import uk.co.skyem.projects.emuZ80.cpu.Register.*;

public class IndirectLoad extends Instruction {
	ALU alu;

	public IndirectLoad(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
		this.alu = instructionDecoder.alu;
	}

	@Override
	public short runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		Register register;
		short address;

		switch (splitInstruction.p) {
			// the logic makes a lot more sense when you add register to each case
			case 0:
				register = instructionDecoder.getRegister(InstructionDecoder.Register.A, splitInstruction);
				address = instructionDecoder.getRegisterPair(InstructionDecoder.RegisterPair.BC, splitInstruction).getData();
				break;
			case 1:
				register = instructionDecoder.getRegister(InstructionDecoder.Register.A, splitInstruction);
				address = instructionDecoder.getRegisterPair(InstructionDecoder.RegisterPair.DE, splitInstruction).getData();
				break;
			case 2:
				register = instructionDecoder.getRegister(InstructionDecoder.Register.HL, splitInstruction);
				address = splitInstruction.getShortInc();
				break;
			case 3:
				register = instructionDecoder.getRegister(InstructionDecoder.Register.A, splitInstruction);
				address = splitInstruction.getShortInc();
				break;
			default:
				throw new UnsupportedOperationException("We should never be here.");
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
		return splitInstruction.position;
	}
}
