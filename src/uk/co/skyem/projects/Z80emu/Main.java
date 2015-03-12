package uk.co.skyem.projects.Z80emu;

public class Main {

	private static String byteToHexString(byte data) {
		return Integer.toHexString(Byte.toUnsignedInt(data));
	}

    public static void main(String[] args) {
		Memory memory = new Memory(1024, 1);
		Bus bus = new Bus();
		bus.addConnection(memory);
		System.out.println(byteToHexString(bus.getByte(0)));
		bus.putByte(0, (byte)0xFF);
		System.out.println(byteToHexString(bus.getByte(0)));
		memory.putByte(1, (byte)0xEE);
		for (byte data : memory.getBytes(0,2)) {
			System.out.print(byteToHexString(data) + ' ');
		}
		System.out.println();
    }

}
