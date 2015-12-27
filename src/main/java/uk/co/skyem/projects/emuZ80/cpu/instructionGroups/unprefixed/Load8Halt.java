package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.Register;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.Instruction;

public class Load8Halt extends Instruction {
	public Load8Halt(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	@Override
	public short runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		// Exception to this instruction.
		// Replaces LD(HL),(HL)
		if (splitInstruction.z == 6 && splitInstruction.y == 6) {
			// HALT
			instructionDecoder.halt();
			return splitInstruction.position;
		}

		// NOTE: we have to know in advance which one is (HL).
		//       They can't *BOTH* be (HL),
		//       but whichever one might be HL has to be executed first,
		//       else getRegister won't know one of them is (HL).
		//       Meaning ld h,(ix+*) could become ld ixh,(ix+*), and that's incorrect.
		//       Or the opposite way around - ld (ix+*),ixh for example.
		//       And if *that* happens,
		//       then it'll throw a nice exception for you to prevent situations like this
		//       from accidentally occurring, since messing these things up is really, really bad.

		// LD r[y],r[z]
		InstructionDecoder.Register y = InstructionDecoder.registerTable[splitInstruction.y];
		InstructionDecoder.Register z = InstructionDecoder.registerTable[splitInstruction.z];
		Register.Register8 yr, zr;
		if (z == InstructionDecoder.Register.HL) {
			zr = instructionDecoder.getRegister(z, splitInstruction);
			yr = instructionDecoder.getRegister(y, splitInstruction);
		} else {
			yr = instructionDecoder.getRegister(y, splitInstruction);
			zr = instructionDecoder.getRegister(z, splitInstruction);
		}
		yr.setData(zr);
		return splitInstruction.position;
	}
}
