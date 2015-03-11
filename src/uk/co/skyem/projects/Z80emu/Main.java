package uk.co.skyem.projects.Z80emu;

public class Main {

    public static void main(String[] args) {
		Memory memory = new Memory(1024);
		Bus bus = new Bus();
		bus.addConnection(memory);
		System.out.println(bus.getHexStringByte(0));
		bus.putByte(0, (byte)0xFF);
		System.out.println(bus.getHexStringByte(0));
    }
}
