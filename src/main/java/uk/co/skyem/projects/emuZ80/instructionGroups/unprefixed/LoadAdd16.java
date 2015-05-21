package uk.co.skyem.projects.emuZ80.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.instructionGroups.Instruction;

public class LoadAdd16 extends Instruction{
	public LoadAdd16(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		// rp[p]
		InstructionDecoder.RegisterPair registerPair = InstructionDecoder.registerPairTable1[splitInstruction.p];
		if (splitInstruction.q) {
			// ADD HL,rp[p]
			instructionDecoder.registers.REG_HL.add(instructionDecoder.getRegisterPairData(registerPair));
		} else {
			// LD rp[p],nn
			instructionDecoder.setRegisterPairData(registerPair, splitInstruction.getShortInc());
		}
	}
}
