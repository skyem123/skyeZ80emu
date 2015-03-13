package uk.co.skyem.projects.emuBus;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SimpleIO extends SimpleBusDevice{
	int address;
	BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	public SimpleIO(int address) {
		this.address = address;
	}

	public byte getByte(int address) {
		if (address == this.address) {
			try { return (byte)System.in.read(); }
			catch (java.io.IOException e) { return (byte)0x00; }
		} else { return (byte)0x00; }
	}

	public void putByte(int address, byte data) {
		if (address == this.address) {
			System.out.print(Character.toChars(data)[0]);
		}
	}
}
