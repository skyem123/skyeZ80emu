package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.ALU;
import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.Registers;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.Instruction;

public class MiscOP extends Instruction {
	private ALU alu;

	public MiscOP(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
		this.alu = instructionDecoder.alu;
	}

	@Override
	public short runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		switch (splitInstruction.y) {
			// TODO: Apparently these aren't implemented? According to the last TODOer.
			case 0:
				// RLCA
				alu.rotateRegisterLeftCarry(instructionDecoder.getRegister(InstructionDecoder.Register.A, splitInstruction));
				break;
			case 1:
				// RRCA
				alu.rotateRegisterRightCarry(instructionDecoder.getRegister(InstructionDecoder.Register.A, splitInstruction));
				break;
			case 2:
				// RLA
				alu.rotateRegisterLeftThroughCarry(instructionDecoder.getRegister(InstructionDecoder.Register.A, splitInstruction));
				break;
			case 3:
				// RRA
				alu.rotateRegisterRightThroughCarry(instructionDecoder.getRegister(InstructionDecoder.Register.A, splitInstruction));
				break;
			case 4:
				// DAA
				// Edits the accumulator after an operation with BCD input (ADD / SUB, etc...), to make the result BCD.
				alu.bcdAdjust(instructionDecoder.getRegister(InstructionDecoder.Register.A, splitInstruction));
				break;
			case 5:
				// CPL
				alu.complement(instructionDecoder.getRegister(InstructionDecoder.Register.A, splitInstruction));
				break;
			case 6:
				// SCF
				alu.setCarry(instructionDecoder.getRegister(InstructionDecoder.Register.A, splitInstruction).getData());
				break;
			case 7:
				// CCF
				alu.invertCarry(instructionDecoder.getRegister(InstructionDecoder.Register.A, splitInstruction).getData());
				break;
		}
		return splitInstruction.position;
	}
}
