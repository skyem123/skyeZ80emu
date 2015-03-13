package uk.co.skyem.projects.Z80emu;

import uk.co.skyem.projects.emuBus.*;
import uk.co.skyem.projects.misc.Vic.Console;

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

    public static void main(String[] args) {
		Console.init("skyeZ80emu");

		Bus bus = new Bus();
		Memory memory = new Memory(1024);
		bus.addConnection(memory);
		System.out.println(toHexString(bus.getByte(0)));
		byte[] datas = new byte[4];
		datas[0] = (byte)0xAA;
		datas[1] = (byte)0xEE;
		datas[3] = (byte)0xCC;
		bus.putBytes(0, datas);
		bus.putQWord(4, 0xDEADL);
		for (byte data : bus.getBytes(0,16)) {
			System.out.print(toHexString(data) + ' ');
		}
		System.out.println();
		System.out.println(toHexString(bus.getWord(2)));
		System.out.println(toHexString(bus.getWord(0)));
		System.out.println(toHexString(bus.getDWord(0)));
		System.out.println(toHexString(bus.getQWord(4)));
		bus.addConnection(new SimpleIO(1025));
		bus.putByte(1025, (byte)65);
		bus.putByte(1025, (byte) 65);
		bus.putByte(1025, bus.getByte(1025));
		bus.putByte(1025, bus.getByte(1025));

		System.out.println("\nOkay, now lets start the virtual Z80...\n" +
						   "First, we need ROM...");
		// Load the ROM from a file
		Memory cpuROM = new Memory(32768, 0);
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
		Z80Cpu cpu = new Z80Cpu(cpuMemBus, cpuIOBus);
		// TODO: Run the thing.
    }

}
