package uk.co.skyem.projects.Z80emu;

import uk.co.skyem.projects.Z80emu.Register.*;
import uk.co.skyem.projects.Z80emu.bus.IBusDevice;

public abstract class InstructionDecoder {
	IBusDevice memoryBus;
	Core cpuCore;

	InstructionDecoder(Core cpu) {
		memoryBus = cpu.memoryBus;
		cpuCore = cpu;
	}

	private void LDRegisterFixed(Register8 destination, byte data) {
		destination.setData(data);
	}

	private void LDRegisterFixed(Register16 destination, short data) {
		destination.setData(data);
	}

	private void LDRegisterRegister(Register8 destination, Register8 source) {
		destination.setData(source);
	}

	private void LDRegisterRegister(Register16 destination, Register16 source) {
		destination.setData(source);
	}

	private void LDMemoryRegister(short destination, Register8 source) {
		memoryBus.putByte(destination, source.getData());
	}

	private void LDMemoryRegister(short destination, Register16 source) {
		// TODO: Implement this
	}

	private void LDRegisterMemory(Register8 destination, short source) {
		destination.setData(memoryBus.getByte(source));
	}

	private void LDRegisterMemory(Register16 destination, short source) {
		destination.setData(cpuCore.read16bits(source));
	}
}
