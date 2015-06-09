package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.ALU;
import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.Instruction;

public class LoadAdd16 extends Instruction{
	ALU alu;
	public LoadAdd16(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
		alu = instructionDecoder.alu;
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		// rp[p]
		InstructionDecoder.RegisterPair registerPair = InstructionDecoder.registerPairTable1[splitInstruction.p];
		if (splitInstruction.q) {
			// ADD HL,rp[p]
			alu.add16Register(instructionDecoder.registers.REG_HL, instructionDecoder.getRegisterPair(registerPair));
		} else {
			// LD rp[p],nn
			alu.load16(instructionDecoder.getRegisterPair(registerPair), splitInstruction.getShortInc());
		}
	}
}
