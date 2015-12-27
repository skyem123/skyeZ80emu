package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.Flags;
import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.Register;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.Instruction;

public class ALURegister extends Instruction {
	public ALURegister(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	@Override
	public short runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		InstructionDecoder.AluOP op = InstructionDecoder.AluTable[splitInstruction.y];
		Register.Register8 b = instructionDecoder.getRegister(InstructionDecoder.registerTable[splitInstruction.z],splitInstruction);
		Register.Register8 a = instructionDecoder.getRegister(InstructionDecoder.Register.A,splitInstruction);
		int r = 0;
		switch (op) {
			case ADD_A:
				r = instructionDecoder.alu.add8(a.getData(), b.getData(), false, 0xFF) & 0xFF;
				break;
			case ADC_A:
				r = instructionDecoder.alu.add8(a.getData(), b.getData(), instructionDecoder.alu.flags.getFlag(Flags.CARRY), 0xFF) & 0xFF;
				break;
			default:
				throw new UnsupportedOperationException("TODO: Implement");
		}
		a.setData((byte) r);

		return splitInstruction.position;
	}
}
