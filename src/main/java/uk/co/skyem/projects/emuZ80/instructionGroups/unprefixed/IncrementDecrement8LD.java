package uk.co.skyem.projects.emuZ80.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.ALU;
import uk.co.skyem.projects.emuZ80.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.Register;
import uk.co.skyem.projects.emuZ80.asm.Patterns;
import uk.co.skyem.projects.emuZ80.instructionGroups.Instruction;

public class IncrementDecrement8LD extends Instruction{
	ALU alu;
	public IncrementDecrement8LD(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
		this.alu = instructionDecoder.alu;
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		switch (splitInstruction.z) {
			case 3:
				// 16 bit INC/DEC
				// rp[p]
				Register.Register16 registerPair = instructionDecoder.getRegisterPair(InstructionDecoder.registerPairTable1[splitInstruction.p]);
				if (splitInstruction.q) {
					// DEC rp[p]
					alu.increment16(registerPair);
				} else {
					// INC rp[p]
					alu.decrement16(registerPair);
				}
				break;
			case 4:case 5:case 6:
				// r[y]
				Register.Register8 register = instructionDecoder.getRegister(InstructionDecoder.registerTable[splitInstruction.y]);
				switch (splitInstruction.z) {
					case 4:
						// 8 bit INC
						// INC r[y]
						alu.increment8(register);
						break;
					case 5:
						// 8 bit DEC
						// DEC r[y]
						alu.decrement8(register);
						break;
					case 6:
						// 8 bit immediate load
						alu.load8(register, splitInstruction.getByteInc());
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
