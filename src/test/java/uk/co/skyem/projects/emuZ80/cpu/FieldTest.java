package uk.co.skyem.projects.emuZ80.cpu;

import org.junit.Test;
import uk.co.skyem.projects.emuZ80.bus.Bus;
import uk.co.skyem.projects.emuZ80.bus.Memory;
import uk.co.skyem.projects.emuZ80.bus.SimpleBusDevice;
import uk.co.skyem.projects.emuZ80.bus.SimpleIO;
import uk.co.skyem.projects.emuZ80.cpu.Core;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by gamemanj on 26/12/15.
 */
public class FieldTest {
	byte[] program = {
			(byte) 0xDB, 0x00, // in a,(0x00)
			(byte) 0x3C, // inc a
			(byte) 0xD3, 0x00, // out (0x00), a
			(byte) 0x38, 0x04, // jr c, 0x06 (past 2 next instructions)
			(byte) 0x3E, 0x00, // ld a, 0x00
			(byte) 0xD3, 0x01, // out (0x01), a (TRIGGER ERROR)
			0x18, (byte) 0x00 // jr 0x00
	};
	String[] errstr = {
			"Carry flag was not set by inc a (a == 0xFF)"
	};

	@Test
	public void seedTest() throws Exception {
		Bus cpuMemBus = new Bus();
		cpuMemBus.addConnection(new Memory(65536, 0));
		cpuMemBus.putBytes(0, program);
		Bus cpuIOBus = new Bus();
		cpuIOBus.addConnection(new SimpleBusDevice() {
			@Override
			public void putByte(int position, byte data) {
				assertThat(position).isEqualTo(0);
				assertThat(data).isEqualTo(0);
			}

			@Override
			public byte getByte(int position) {
				assertThat(position).isEqualTo(0);
				return (byte) 0xFF;
			}
		});
		Core cpu = new Core(cpuMemBus, cpuIOBus);
		for (int i = 0; i < 100; i++) {
			cpu.cycle();
		}
	}
}
