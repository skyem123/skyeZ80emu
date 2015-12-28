package uk.co.skyem.projects.emuZ80;

import uk.co.skyem.projects.emuZ80.asm.Assembler;
import uk.co.skyem.projects.emuZ80.bus.Memory;
import uk.co.skyem.projects.emuZ80.bus.SimpleIO;
import uk.co.skyem.projects.emuZ80.cpu.Core;
import uk.co.skyem.projects.emuZ80.cpu.Flags;
import uk.co.skyem.projects.emuZ80.cpu.MemoryRouter;
import uk.co.skyem.projects.emuZ80.util.Console;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

	public static String toHexString(byte data) {
		return Integer.toHexString(Byte.toUnsignedInt(data));
	}

	public static String toHexString(short data) {
		return Integer.toHexString(Short.toUnsignedInt(data));
	}

	public static String toHexString(int data) {
		return Integer.toHexString(data);
	}

	public static String toHexString(long data) {
		return Long.toHexString(data);
	}

	public static String toBinString(byte data) {
		return Integer.toBinaryString(Byte.toUnsignedInt(data));
	}

	public static String toBinString(short data) {
		return Integer.toBinaryString(Short.toUnsignedInt(data));
	}

	public static String toBinString(int data) {
		return Integer.toBinaryString(data);
	}

	public static void main(String[] args) throws Exception {
		Logger logger = Logger.getGlobal();
		// cool, but loading fonts takes time
		//Console.init("skyeZ80emu");

		int runningOpcodes = 0;
		System.out.println("Exception-throwing ops are:");
		for (int op = 0; op < 0x100; op++) {
			Memory memory = new Memory(1, 0);
			Core cpuCore = new Core(memory, new Memory(1, 0));
			memory.putByte(0, (byte) op);
			try {
				cpuCore.step();
				runningOpcodes++;
			} catch (Exception e) {
				// opcode is not running
				System.out.print(toHexString(op) + " ");
			}
		}
		int runningExtOpcodes = 0;
		System.out.println();
		System.out.println("Exception-throwing EXTops are:");
		for (int op = 0; op < 0x100; op++) {
			Memory memory = new Memory(2, 0);
			Core cpuCore = new Core(memory, new Memory(1, 0));
			memory.putByte(0, (byte) 0xED);
			memory.putByte(1, (byte) op);
			try {
				cpuCore.step();
				runningExtOpcodes++;
			} catch (Exception e) {
				// opcode is not running
				System.out.print("ED." + toHexString(op) + " ");
			}
		}

		System.out.println();
		System.out.println("Self-test complete, " + runningOpcodes + " of 256 opcodes that do not throw exceptions. (Including unimplemented prefixes) Please run tests for further detail.");
		System.out.println("Implemented EXTops: " + runningExtOpcodes + ". IX and IY prefixes have already been implemented, and BIT counts as an instruction");

		// The assembler doesn't work, so for now...
		FileInputStream fis = new FileInputStream("calculator.bin");
		Memory memory = new Memory(65536, 0);
		int len = fis.available();
		for (int i = 0; i < len; i++)
			memory.putByte(i, (byte) fis.read());
		fis.close();
		Core cpuCore = new Core(memory, new SimpleIO(0));
		while (true) {
			try {
				//System.out.println("Step @ x" + toHexString(cpuCore.registers.programCounter.getData()) + " : A = " + cpuCore.registers.REG_A.getData() + " : SP = " + cpuCore.registers.stackPointer.getData());
				cpuCore.step();
			} catch (Exception e) {
				System.err.println("Step @ x" + toHexString(cpuCore.registers.programCounter.getData()) + " : A = " + cpuCore.registers.REG_A.getData() + " : SP = " + cpuCore.registers.stackPointer.getData());
				throw e;
			}
			if (cpuCore.halted()) {
				System.out.println("HALT @ x" + (cpuCore.registers.programCounter.getData() - 1) + " : A = " + cpuCore.registers.REG_A.getData());
				System.out.println("SP = " + cpuCore.registers.stackPointer.getData());
				MemoryRouter memRouter = new MemoryRouter(memory);
				System.out.println("(SP) = " + memRouter.getWord(cpuCore.registers.stackPointer.getData()));
				System.out.println("(SP)L = " + memRouter.getByte(cpuCore.registers.stackPointer.getData()));
				System.out.println("(SP)H = " + memRouter.getByte((short) (cpuCore.registers.stackPointer.getData() + 1)));
				listFlag(Flags.ADD_SUB, "ADD/SUB", cpuCore.registers.flags.getData());
				listFlag(Flags.CARRY, "CARRY", cpuCore.registers.flags.getData());
				listFlag(Flags.HALF_CARRY, "HALF_CARRY", cpuCore.registers.flags.getData());
				listFlag(Flags.PARITY_OVERFLOW, "PARITY_OVERFLOW", cpuCore.registers.flags.getData());
				listFlag(Flags.SIGN, "SIGN", cpuCore.registers.flags.getData());
				listFlag(Flags.X_3, "X_3", cpuCore.registers.flags.getData());
				listFlag(Flags.X_5, "X_5", cpuCore.registers.flags.getData());
				listFlag(Flags.ZERO, "ZERO", cpuCore.registers.flags.getData());
				System.in.read();
				cpuCore.unhalt();
			}
		}
	}

	private static void listFlag(int flags, String name, byte data) {
		if ((data & flags) == flags) {
			name += " +";
		} else {
			name += " -";
		}
		System.out.println(name);
	}

}
