package uk.co.skyem.projects.Z80emu;

public class Main {

	private static String byteToHexString(byte data) {
		return Integer.toHexString(Byte.toUnsignedInt(data));
	}

    public static void main(String[] args) {
		Memory memory = new Memory(1024);
		Bus bus = new Bus();
		bus.addConnection(memory);
		BusOffset offset = new BusOffset(0, bus);
		System.out.println(byteToHexString(offset.getByte(0)));
		byte[] datas = new byte[2];
		datas[0] = (byte)0xFF;
		datas[1] = (byte)0xEE;
		bus.putBytes(0, datas);
		for (byte data : offset.getBytes(0,2)) {
			System.out.print(byteToHexString(data) + ' ');
		}
		System.out.println();

    }

}
