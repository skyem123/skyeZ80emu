package uk.co.skyem.projects.emuZ80.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.instructionGroups.Instruction;

public class UnprefixedInstruction extends Instruction {
	public UnprefixedInstruction(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	// x == 0
	Instruction miscInstruction = new MiscInstruction(instructionDecoder);
	Instruction loadAdd16 = new LoadAdd16(instructionDecoder);
	Instruction indirectLoad = new IndirectLoad(instructionDecoder);
	Instruction incrementDecrement = new IncrementDecrement(instructionDecoder);
	Instruction loadImmediate8 = new LoadImmediate8(instructionDecoder);
	Instruction miscOP = new MiscOP(instructionDecoder);

	// x == 1
	Instruction load8Halt = new Load8Halt(instructionDecoder);
	// x == 2
	Instruction aluRegister = new ALURegister(instructionDecoder);

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		switch (splitInstruction.x) {
			case 0:
				switch (splitInstruction.z) {
					case 0:
						miscInstruction.runOpcode(splitInstruction);
						break;
					case 1:
						loadAdd16.runOpcode(splitInstruction);
						break;
					case 2:
						indirectLoad.runOpcode(splitInstruction);
						break;
					case 3:case 4:case 5:
						incrementDecrement.runOpcode(splitInstruction);
						break;
					case 6:
						loadImmediate8.runOpcode(splitInstruction);
						break;
					case 7:
						miscOP.runOpcode(splitInstruction);
						break;
				}
				break;
			case 1:
				load8Halt.runOpcode(splitInstruction);
				break;
			case 2:
				aluRegister.runOpcode(splitInstruction);
		}
	}
}
