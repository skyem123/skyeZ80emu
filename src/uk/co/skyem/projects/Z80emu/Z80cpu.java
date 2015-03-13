package uk.co.skyem.projects.Z80emu;

import uk.co.skyem.projects.emuBus.IBusDevice;

public class Z80Cpu {
	IBusDevice memoryBus;
	IBusDevice IOBus;

	short programCounter;

	public Z80Cpu(IBusDevice memory, IBusDevice IO) {
		memoryBus = memory;
		IOBus = IO;
		reset();
	}

	public void reset() {
		programCounter = 0;
	}

	public void cycle() {

	}
}
