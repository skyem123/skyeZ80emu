package uk.co.skyem.projects.Z80emu;

import uk.co.skyem.projects.Z80emu.asm.Assembler;
import uk.co.skyem.projects.Z80emu.bus.Bus;
import uk.co.skyem.projects.Z80emu.bus.Memory;
import uk.co.skyem.projects.Z80emu.bus.SimpleIO;
import uk.co.skyem.projects.Z80emu.util.Console;

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
		Console.init("skyeZ80emu");
		logger.log(Level.INFO, "test2!");
		logger.log(Level.INFO, "test3!");

		/*
		String text = "" +
				"				DI                    ; Disable interrupt\n\n\n" +
				"				LD     SP,RAMTOP      ; Set stack pointer to top off ram\n" +
				"      			IM     1              ; Set interrupt mode 1\n" +
				"            	JP     $100           ; jump to Start of program\n" +
				"            	.blah     \"I am a string   \\\\\\\"he   llo!\\\"    with spaces\"";
		Assembler assembler = new Assembler("NOP\nNOP\nNOP\nLD\nLD\nNOP\nLD\nLD\nLD\nLD\n"
			+ "Nothing:\n"
			+ "nop\n"
			+ "Load: LD");
		//System.out.println(Arrays.toString(assembler.preparse()));



		Scanner input = new Scanner(System.in);
		assembler.tokenize(assembler.preparse());
		*/

		Bus bus = new Bus();
		Memory memory = new Memory(1024);
		bus.addConnection(memory);
		System.out.println(toHexString(bus.getByte(0)));
		byte[] datas = new byte[4];
		datas[0] = (byte) 0xAA;
		datas[1] = (byte) 0xEE;
		datas[3] = (byte) 0xCC;
		bus.putBytes(0, datas);
		bus.putQWord(4, 0xDEADL);
		for (byte data : bus.getBytes(0, 16)) {
			System.out.print(toHexString(data) + ' ');
		}
		System.out.println();
		System.out.println(toHexString(bus.getWord(2)));
		System.out.println(toHexString(bus.getWord(0)));
		System.out.println(toHexString(bus.getDWord(0)));
		System.out.println(toHexString(bus.getQWord(4)));
		bus.addConnection(new SimpleIO(1025));
		bus.putByte(1025, (byte) 65);
		bus.putByte(1025, (byte) 65);
		bus.putByte(1025, bus.getByte(1025));
		bus.putByte(1025, bus.getByte(1025));
		System.out.println("\nOkay, now lets start the virtual Z80...\n" +
			"First, we need ROM...");
		// Load the ROM from a file
		Memory cpuROM = new Memory(32768, 0);
		byte[] ROMdata = new byte[4];
		//ROMdata[0] = (byte) 0x01; // LD BC
		//ROMdata[1] = (byte) 0x42; //      ,$42
		//ROMdata[2] = (byte) 0xFE; //          FE
		cpuROM.putBytes(0, ROMdata);
		System.out.println("Next, we need RAM...");
		Memory cpuMemory = new Memory(32768, 32768);
		System.out.println("Don't forget IO!");
		SimpleIO cpuIO = new SimpleIO(0);
		System.out.println("Now for the buses!");
		Bus cpuMemBus = new Bus();
		Bus cpuIOBus = new Bus();
		System.out.println("Now connect things to those buses!");
		cpuIOBus.addConnection(cpuIO);
		cpuMemBus.addConnection(cpuMemory);
		cpuMemBus.addConnection(cpuROM);
		System.out.println("Okay, so now get the cpu and give it the buses...");
		Core cpu = new Core(cpuMemBus, cpuIOBus);

		// Load code to 1000h
		// bus.putBytes(0x1000, assembledCode)

		/*
		Thread processor1 = new Thread(() -> {
			try {
				while (true) {
					// You can do that here
					bus.putQWord(0, 0xAAAAAAAAAAAAAAAAL);
					System.out.println(toHexString(bus.getQWord(0)));
					Thread.sleep(100);
				}
			} catch (Exception e) {

			}
		});
		Thread processor2 = new Thread(() -> {
			try {
				while (true) {
					bus.putQWord(0, 0xFFFFFFFFFFFFFFFFL);
					System.out.println(toHexString(bus.getQWord(0)));
					// This one is slower
					Thread.sleep(300);
				}
			} catch (Exception e) {

			}
		});
		processor1.start();
		processor2.start();

		*/

		// TODO: Run the thing.
		while (System.in.read() != "s".getBytes()[0]) {
			System.out.println(toHexString(cpu.registers.getProgramCounter()));
			cpu.cycle();
		}
		System.out.println("\nDone!");

	}

}
