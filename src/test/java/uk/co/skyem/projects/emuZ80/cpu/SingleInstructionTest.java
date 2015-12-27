package uk.co.skyem.projects.emuZ80.cpu;

import org.junit.Test;
import uk.co.skyem.projects.emuZ80.bus.Memory;
import uk.co.skyem.projects.emuZ80.bus.SimpleBusDevice;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by gamemanj on 26/12/15.
 */
public class SingleInstructionTest {

	public static Core executeTest(byte[] data, short[] positions) {
		Core c = new Core(new SimpleBusDevice() {
			@Override
			public void putByte(int position, byte bdata) {
				assertThat(position).isBetween(0, data.length - 1);
				data[position] = bdata;
			}

			@Override
			public byte getByte(int position) {
				assertThat(position).isBetween(0, data.length - 1);
				return data[position];
			}
		}, new Memory(65536, 0)); // using IO as memory, any better ideas?
		for (int i = 0; i < positions.length; i++) {
			c.step(); // NOTE: If step is ever fixed to work in T-States, then run this 4 times.
			System.out.println("Cycle complete, PC=" + c.registers.getProgramCounter());
			assertThat(c.registers.getProgramCounter()).isEqualTo(positions[i]);
		}
		return c;
	}

	@Test
	public void testNop() {
		short[] positions = new short[65536];
		for (int i = 0; i < 65536; i++)
			positions[i] = (short)((i + 1) & 0xFFFF);
		Core c = executeTest(new byte[65536], positions);
		// do a minor check just in case
		assertFlags(c.registers.flags, (byte) 0);
		assertThat(c.registers.REG_A.getData()).isEqualTo((byte)0);
	}

	@Test
	public void testLdAConst_AddAPtrHL() {
		Core c = executeTest(new byte[]{
				(byte)0x3E,(byte)0x12, // ld A, 0x12
				(byte)0x86 // add A,(HL) ; (HL) == 0x3E
		}, new short[] {
				0x02, 0x03
		});
		assertThat(c.registers.REG_A.getData()).isEqualTo((byte) 0x50);
		assertFlags(c.registers.flags,(byte) (Flags.HALF_CARRY));
		c = executeTest(new byte[]{
				(byte)0x3E,(byte)0xC2, // ld A, 0xC2
				(byte)0x86 // add A,(HL) ; (HL) == 0x3E
		}, new short[] {
				0x02, 0x03
		});
		assertThat(c.registers.REG_A.getData()).isEqualTo((byte) 0x00);
		assertFlags(c.registers.flags,(byte) (Flags.HALF_CARRY | Flags.PARITY_OVERFLOW | Flags.CARRY | Flags.ZERO));
	}

	// This may not be the way you want it debugged, but it's easier to debug this way than other ways
	private void assertFlags(Register.Register8 flagreg, byte flags) {
		int[] flagVals = {
				Flags.ADD_SUB,
				Flags.CARRY,
				Flags.HALF_CARRY,
				Flags.PARITY_OVERFLOW,
				Flags.SIGN,
				Flags.X_3,
				Flags.X_5,
				Flags.ZERO
		};
		String[] flagNames = {
				"ADD_SUB",
				"CARRY",
				"HALF_CARRY",
				"PARITY_OVERFLOW",
				"SIGN",
				"X_3",
				"X_5",
				"ZERO"
		};
		int errors = 0;
		for (int i = 0; i < flagVals.length; i++) {
			boolean flagWanted = (flagVals[i] & flags) != 0;
			boolean flagValue = flagreg.getFlag(flagVals[i]);
			if (flagWanted != flagValue) {
				System.out.println(flagNames[i] + "(" + flagVals[i] + "): wanted: " + flagWanted + " got: " + flagValue);
				errors++;
			}
		}
		assertThat(errors).isEqualTo(0);
		assertThat(flagreg.getData()).isEqualTo(flags);
	}
}
