package uk.co.skyem.projects.emuZ80.cpu.instructionGroups.unprefixed;

import uk.co.skyem.projects.emuZ80.cpu.ALU;
import uk.co.skyem.projects.emuZ80.cpu.InstructionDecoder;
import uk.co.skyem.projects.emuZ80.cpu.Registers;
import uk.co.skyem.projects.emuZ80.cpu.instructionGroups.Instruction;

public class MiscOP extends Instruction{
	private ALU alu;
	private Registers registers;
	public MiscOP(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
		this.alu = instructionDecoder.alu;
		this.registers = instructionDecoder.registers;
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		switch(splitInstruction.y) {
			// TODO: Implement instructions.
			case 0:
				// RLCA
				alu.rotateRegisterLeftCarry(registers.REG_A);
				break;
			case 1:
				// RRCA
				alu.rotateRegisterRightCarry(registers.REG_A);
				break;
			case 2:
				// RLA
				alu.rotateRegisterLeftThroughCarry(registers.REG_A);
				break;
			case 3:
				// RRA
				alu.rotateRegisterRightThroughCarry(registers.REG_A);
				break;
			case 4:
				// DAA
				// Edits the accumulator after an operation with BCD input (ADD / SUB, etc...), to make the result BCD.
				break;
			case 5:
				// CPL
				break;
			case 6:
				// SCF
				break;
			case 7:
				// CCF
				break;
		}
	}
}
