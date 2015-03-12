package uk.co.skyem.projects.Z80emu;

import uk.co.skyem.projects.emuBus.*;

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

    public static void main(String[] args) {
		Memory memory = new Memory(1024);
		System.out.println(toHexString(memory.getByte(0)));
		byte[] datas = new byte[4];
		datas[0] = (byte)0xFF;
		datas[1] = (byte)0xEE;
		datas[3] = (byte)0xCC;
		memory.putBytes(0, datas);
		for (byte data : memory.getBytes(0,5)) {
			System.out.print(toHexString(data) + ' ');
		}
		System.out.println();
		System.out.println(toHexString(memory.getWord(2)));
		System.out.println(toHexString(memory.getWord(0)));
		System.out.println(toHexString(memory.getDWord(0)));
    }

}
