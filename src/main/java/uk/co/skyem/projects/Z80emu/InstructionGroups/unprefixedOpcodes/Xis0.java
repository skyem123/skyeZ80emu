package uk.co.skyem.projects.Z80emu.instructionGroups.unprefixedOpcodes;

import uk.co.skyem.projects.Z80emu.InstructionDecoder;
import uk.co.skyem.projects.Z80emu.instructionGroups.IOpcodeHandler;

public class Xis0 implements IOpcodeHandler{
	private Xis0(){}

	public static void runOpcode(InstructionDecoder.DecodedInstruction decodedInstruction) {
		switch (decodedInstruction.z) {
			case 0: // z == 0  // Misc instructions and relative jumps
				MiscInstruction.runOpcode(decodedInstruction);
				break;
			case 1: // z == 1  // 16bit load immediate and add

				break;
			case 2: // z == 2  // Indirect loading
				switch (decodedInstruction.p) {
					case 0: // LD (BC), A or LD A,(BC) (depending on q)
						break;
					case 1: // LD (BC), A or LD A,(BC) (depending on q)
						break;
					case 2: // LD (nn),HL or LD HL,(nn) (depending on q)
						break;
					case 3: // LD A,(DE) or LD (DE), A (depending on q)
						break;
				}
				break;
			case 3: // z == 3  // 16-bit increment / decrement
				break;
		}
	}

	public static class LoadAdd16 implements IOpcodeHandler {
		public static void runOpcode(InstructionDecoder.DecodedInstruction decodedInstruction) {
			if (decodedInstruction.q) { // immediate load
				// LD rp[p], nn
			} else { // add
				// ADD HL,rp[p]
			}
		}
	}

	public static class MiscOP implements IOpcodeHandler {
	}

	public static class IndirectLoad implements IOpcodeHandler {
	}

	public static class IncrementDecrement implements IOpcodeHandler {
	}

	public static class MiscInstruction implements IOpcodeHandler {
		public static void runOpcode(InstructionDecoder.DecodedInstruction decodedInstruction) {
			switch (decodedInstruction.y){
				case 0: // NOP
					System.out.println("NOP");
					break;
				case 1: // EX AF,AF'
					break;
				case 2: // DJNZ d(isplacement)
					break;
				case 3: // JR d(isplacement)
					break;
				case 4:case 5:case 6:case 7: // JR cc[y-4],d
					InstructionDecoder.Condition condition = InstructionDecoder.conditionTable[decodedInstruction.y - 4];
					break;
			}
		}
	}

	public static class LoadImmediate8 {
	}
}
