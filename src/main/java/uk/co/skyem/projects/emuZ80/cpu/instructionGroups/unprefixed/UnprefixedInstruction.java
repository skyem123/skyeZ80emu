package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.Instruction;

public class UnprefixedInstruction extends Instruction {
	public UnprefixedInstruction(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
	}

	// x == 0
	MiscInstruction miscInstruction = new MiscInstruction(instructionDecoder);
	LoadAdd16 loadAdd16 = new LoadAdd16(instructionDecoder);
	IndirectLoad indirectLoad = new IndirectLoad(instructionDecoder);
	IncrementDecrement8LDI incrementDecrement = new IncrementDecrement8LDI(instructionDecoder);
	MiscOP miscOP = new MiscOP(instructionDecoder);

	// x == 1
	Load8Halt load8Halt = new Load8Halt(instructionDecoder);
	// x == 2
	ALURegister aluRegister = new ALURegister(instructionDecoder);

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
					case 3:
					case 4:
					case 5:
					case 6:
						incrementDecrement.runOpcode(splitInstruction);
						break;
					case 7:
						miscOP.runOpcode(splitInstruction);
						break;
					default:
						throw new IllegalStateException("We should never be here.");
				}
				break;
			case 1:
				load8Halt.runOpcode(splitInstruction);
				break;
			case 2:
				aluRegister.runOpcode(splitInstruction);
				break;
			default:
				throw new IllegalStateException("We should never be here.");
		}
	}
}
