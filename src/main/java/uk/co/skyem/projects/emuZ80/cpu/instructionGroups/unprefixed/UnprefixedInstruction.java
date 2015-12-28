package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.Register;
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
	// x == 3
	ALUImmediate aluImmediate = new ALUImmediate(instructionDecoder);

	@Override
	public short runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		switch (splitInstruction.x) {
			case 0:
				switch (splitInstruction.z) {
					case 0:
						return miscInstruction.runOpcode(splitInstruction);
					case 1:
						return loadAdd16.runOpcode(splitInstruction);
					case 2:
						return indirectLoad.runOpcode(splitInstruction);
					case 3:
					case 4:
					case 5:
					case 6:
						return incrementDecrement.runOpcode(splitInstruction);
					case 7:
						return miscOP.runOpcode(splitInstruction);
					default:
						throw new UnsupportedOperationException("We should never be here.");
				}
			case 1:
				return load8Halt.runOpcode(splitInstruction);
			case 2:
				return aluRegister.runOpcode(splitInstruction);
			case 3:
				switch (splitInstruction.z) {
					case 0:
						// RET (condition).
						InstructionDecoder.Condition condition = instructionDecoder.conditionTable[splitInstruction.y];
						if (instructionDecoder.alu.flags.getFlag(condition.flagVal) == condition.expectedResult)
							return instructionDecoder.popWord();
						break;
					case 1:
						if (!splitInstruction.q) {
							// POP rp2[p]
							instructionDecoder.getRegisterPair(InstructionDecoder.registerPairTable2[splitInstruction.p], splitInstruction).setData(instructionDecoder.popWord());
						} else {
							switch (splitInstruction.p) {
								case 0:
									return instructionDecoder.popWord();
								case 1:
									instructionDecoder.specialExceptionEXX();
									break;
								default:
									throw new UnsupportedOperationException("Unprefixed / x=3, z=1, p=" + splitInstruction.p + " NYI");
							}
						}
						break;
					case 2:
						// JP (condition),nn
						condition = instructionDecoder.conditionTable[splitInstruction.y];
						short target = splitInstruction.getShortInc();
						if (instructionDecoder.alu.flags.getFlag(condition.flagVal) == condition.expectedResult)
							return target;
						break;
					case 3:
						switch (splitInstruction.y) {
							case 0:
								// JP nn
								return splitInstruction.getShortInc();
							case 2:
								// OUT A, (n)
								// the non-extended instructions don't alter flags
								Register.Register8 reg = instructionDecoder.getRegister(InstructionDecoder.Register.A, splitInstruction);
								instructionDecoder.alu.ioRouter.putByte(instructionDecoder.getIOAddress_A(splitInstruction.getByteInc()), reg.getData());
								break;
							case 3:
								// IN A, (n)
								// the non-extended instructions don't alter flags
								reg = instructionDecoder.getRegister(InstructionDecoder.Register.A, splitInstruction);
								reg.setData(instructionDecoder.alu.ioRouter.getByte(instructionDecoder.getIOAddress_A(splitInstruction.getByteInc())));
								break;
							default:
								throw new UnsupportedOperationException("Unprefixed / x=3, z=3, y=" + splitInstruction.y + " NYI");
						}
						break;
					case 5:
						if (!splitInstruction.q) {
							// PUSH rp2[p]
							instructionDecoder.pushWord(instructionDecoder.getRegisterPair(InstructionDecoder.registerPairTable2[splitInstruction.p], splitInstruction).getData());
						} else {
							switch (splitInstruction.p) {
								case 0:
									// CALL nn
									short targ = splitInstruction.getShortInc();
									instructionDecoder.pushWord(splitInstruction.position);
									return targ;
								default:
									throw new UnsupportedOperationException("Unprefixed / x=3, z=5, p=" + splitInstruction.p + " NYI");
							}
						}
						break; // NOTE: Remove comment once there's an instruction that doesn't return or throw
					case 6:
						return aluImmediate.runOpcode(splitInstruction);
					default:
						throw new UnsupportedOperationException("Unprefixed / x=3, z=" + splitInstruction.z + " NYI");
				}
				break;
			default:
				throw new UnsupportedOperationException("We should never be here.");
		}
		return splitInstruction.position;
	}
}
