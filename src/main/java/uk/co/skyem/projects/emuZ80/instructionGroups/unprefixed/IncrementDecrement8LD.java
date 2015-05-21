package uk.co.skyem.projects.emuZ80.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.Register;
import uk.co.skyem.projects.emuZ80.asm.Patterns;
import uk.co.skyem.projects.emuZ80.instructionGroups.Instruction;

public class IncrementDecrement8LD extends Instruction{
	public IncrementDecrement8LD(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		switch (splitInstruction.z) {
			case 3:
				// 16 bit INC/DEC
				// rp[p]
				InstructionDecoder.RegisterPair registerPair = InstructionDecoder.registerPairTable1[splitInstruction.p];
				if (splitInstruction.q) {
					// DEC rp[p]
					instructionDecoder.getRegisterPair(registerPair).decrement();
				} else {
					// INC rp[p]
					instructionDecoder.getRegisterPair(registerPair).increment();
				}
				break;
			case 4:case 5:case 6:
				// r[y]
				Register.Register8 register = instructionDecoder.getRegister(InstructionDecoder.registerTable[splitInstruction.y]);
				switch (splitInstruction.z) {
					case 4:
						// 8 bit INC
						// INC r[y]
						register.increment();
						break;
					case 5:
						// 8 bit DEC
						// DEC r[y]
						register.decrement();
						break;
					case 6:
						// 8 bit immediate load
						register.setData(splitInstruction.getByteInc());
						break;
					default:
						throw new RuntimeException("We should NEVER be here...");
				}
				break;
			default:
				throw new RuntimeException("We should NEVER be here...");
		}
	}
}
