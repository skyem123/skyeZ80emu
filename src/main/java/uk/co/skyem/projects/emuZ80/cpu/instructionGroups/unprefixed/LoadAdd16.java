package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.ALU;
import uk.co.skyem.projects.emuZ80.cpu.Flags;
import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.Register;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.Instruction;

public class LoadAdd16 extends Instruction {
	ALU alu;

	public LoadAdd16(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
		alu = instructionDecoder.alu;
	}

	@Override
	public short runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		// rp[p]
		InstructionDecoder.RegisterPair registerPair = InstructionDecoder.registerPairTable1[splitInstruction.p];
		if (splitInstruction.q) {
			// ADD HL,rp[p]
			Register.Register16 hl = instructionDecoder.getRegisterPair(InstructionDecoder.RegisterPair.HL, splitInstruction);
			alu.add16Register(hl, instructionDecoder.getRegisterPair(registerPair, splitInstruction), false, Flags.X_3 | Flags.X_5 | Flags.HALF_CARRY | Flags.ADD_SUB | Flags.CARRY);
		} else {
			// LD rp[p],nn
			alu.load16(instructionDecoder.getRegisterPair(registerPair, splitInstruction), splitInstruction.getShortInc());
		}
		return splitInstruction.position;
	}
}
