package uk.co.skyem.projects.emuZ80.cpu;

import uk.co.skyem.projects.emuZ80.bus.IBusDevice;
import uk.co.skyem.projects.emuZ80.util.buffer.AbstractByteBuffer;
import uk.co.skyem.projects.emuZ80.util.buffer.IByteHandler;

public class Core {
	// These were protected anyway, and they're a sign-extension accident waiting to happen. --gamemanj
	// Don't touch them, btw
	private IBusDevice memoryBus;
	private IBusDevice IOBus;
	// --
	Registers registers;
	InstructionDecoder instructionDecoder;
	ALU alu;
	private boolean halted = true;

	public Core(IBusDevice memory, IBusDevice io) {
		memoryBus = memory;
		IOBus = io;
		// registers must exist before instructionDecoder and maybe ALU
		registers = new Registers();
		// ALU must exist before instructionDecoder
		alu = new ALU(this);
		instructionDecoder = new InstructionDecoder(this);
		reset();
	}

	// Instruction should never call this!!! Use the return value instead!!!
	public void jump(short address) {
		registers.programCounter.setData(address);
	}

	public void reset() {
		registers.clear();
		halted = false;
	}

	public void cycle() {
		instructionDecoder.cycle();
	}

	public void halt() {
		halt(true);
	}

	public void halt(boolean state) {
		this.halted = state;
	}

	public void unhalt() {
		halt(false);
	}

	public boolean halted() {
		return halted;
	}

	// NOTE: The following do the required signed/unsigned conversions.
	// Java will probably sign-extend - that's probably not what you want.
	private int safeShortToInt(short address) {
		int i = address;
		i &= 0xFFFF; // get rid of any sign-extension
		return i;
	}

	public void putMemoryByte(short address, Byte data) {
		memoryBus.putByte(safeShortToInt(address), data);
	}

	public void putMemoryWord(short address, Short data) {
		memoryBus.putByte(safeShortToInt(address++), (byte)(data & 0xFF));
		memoryBus.putByte(safeShortToInt(address), (byte)((data & 0xFF00)>>8));
	}

	public byte getMemoryByte(short address) {
		return memoryBus.getByte(safeShortToInt(address));
	}

	public short getMemoryWord(short address) {
		byte l = memoryBus.getByte(safeShortToInt(address++));
		byte h = memoryBus.getByte(safeShortToInt(address));
		return (short) (l | (h << 8));
	}
}
