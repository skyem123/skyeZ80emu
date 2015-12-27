package uk.co.skyem.projects.emuZ80.cpu;

import org.junit.Test;
import uk.co.skyem.projects.emuZ80.bus.Memory;
import uk.co.skyem.projects.emuZ80.bus.SimpleBusDevice;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by gamemanj on 26/12/15.
 */
public class DoesJavaSignExtendTest {
	@Test
	public void testJavaSignExtends() {
		short s = (short)0xFFFF;
		int i = s;
		System.out.println("0xffff sign extended to " + i);
		assertThat(i).isEqualTo(-1);
	}

	@Test
	public void testDoesCoreFixThis() {
		short i = 0;
		MemoryRouter core = new MemoryRouter(new SimpleBusDevice() {
			@Override
			public void putByte(int position, byte data) {
				assertThat(position).isBetween(0, 65535);
			}

			@Override
			public byte getByte(int position) {
				assertThat(position).isBetween(0, 65535);
				return 0;
			}
		});
		while (true) {
			core.putByte(i, (byte)0);
			core.putWord(i, (short)0);
			core.getByte(i);
			core.getWord(i);
			i++;
			if (i == 0)
				break;
		}
	}
}
