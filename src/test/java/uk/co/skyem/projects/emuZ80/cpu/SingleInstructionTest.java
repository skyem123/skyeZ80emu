package uk.co.skyem.projects.emuZ80.cpu;

import org.junit.Test;
import uk.co.skyem.projects.emuZ80.bus.Memory;
import uk.co.skyem.projects.emuZ80.bus.SimpleBusDevice;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by gamemanj on 26/12/15.
 */
public class SingleInstructionTest {

	public static Core executeTest(byte[] data, short[] positions, byte initialFlags) {
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
		c.registers.flags.setData(initialFlags);
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
		Core c = executeTest(new byte[65536], positions, (byte)0);
		// do a minor check just in case
		assertFlags(c.registers.flags, (byte) 0);
		assertThat(c.registers.REG_A.getData()).isEqualTo((byte)0);
	}

	public void LdAConst_Adc_A_PtrHL_Subtest(boolean carryIn, byte vA, byte vAO, byte vF) {
		Core c = executeTest(new byte[]{
				(byte)0x3E,(byte)vA, // ld A, 0x12
				(byte)0x8E // adc A,(HL) ; (HL) == 0x50
		}, new short[] {
				0x02, 0x03
		}, (byte)(carryIn ? Flags.CARRY : 0));
		assertThat(c.registers.REG_A.getData()).isEqualTo((byte) vAO);
		assertFlags(c.registers.flags,vF);
	}

	@Test
	public void testLdAConst_AddAdc_A_PtrHL() {
		Core c = executeTest(new byte[]{
				(byte)0x3E,(byte)0x12, // ld A, 0x12
				(byte)0x86 // add A,(HL) ; (HL) == 0x50
		}, new short[] {
				0x02, 0x03
		}, (byte)0);
		assertThat(c.registers.REG_A.getData()).isEqualTo((byte) 0x50);
		assertFlags(c.registers.flags,(byte) (Flags.HALF_CARRY));
		c = executeTest(new byte[]{
				(byte)0x3E,(byte)0xC2, // ld A, 0xC2
				(byte)0x86 // add A,(HL) ; (HL) == 0x00 (carry)
		}, new short[] {
				0x02, 0x03
		}, (byte)0);
		assertThat(c.registers.REG_A.getData()).isEqualTo((byte) 0x00);
		assertFlags(c.registers.flags,(byte) (Flags.HALF_CARRY | Flags.CARRY | Flags.ZERO));

		LdAConst_Adc_A_PtrHL_Subtest(false, (byte) 0x80, (byte) 0xBE, (byte) (Flags.SIGN | Flags.X_3 | Flags.X_5));
		LdAConst_Adc_A_PtrHL_Subtest(true, (byte) 0x80, (byte) 0xBF, (byte) (Flags.SIGN | Flags.X_3 | Flags.X_5));

		LdAConst_Adc_A_PtrHL_Subtest(false, (byte) 0x81, (byte) 0xBF, (byte) (Flags.SIGN | Flags.X_3 | Flags.X_5));
		LdAConst_Adc_A_PtrHL_Subtest(true, (byte) 0x81, (byte) 0xC0, (byte) (Flags.SIGN | Flags.HALF_CARRY));

		LdAConst_Adc_A_PtrHL_Subtest(true, (byte) 0x01, (byte) 0x40, (byte) (Flags.HALF_CARRY));

		LdAConst_Adc_A_PtrHL_Subtest(false, (byte) 0xC1, (byte) 0xFF, (byte) (Flags.SIGN | Flags.X_3 | Flags.X_5));
		LdAConst_Adc_A_PtrHL_Subtest(true, (byte) 0xC1, (byte) 0x00, (byte) (Flags.HALF_CARRY | Flags.CARRY | Flags.ZERO));

		// check parity overflow
		LdAConst_Adc_A_PtrHL_Subtest(false, (byte) 0x7F, (byte) 0xBD, (byte) (Flags.X_3 | Flags.X_5 | Flags.HALF_CARRY | Flags.SIGN | Flags.PARITY_OVERFLOW));
		// operands with different signs never cause overflow.
		LdAConst_Adc_A_PtrHL_Subtest(false, (byte) 0xF0, (byte) 0x2E, (byte) (Flags.CARRY | Flags.X_3 | Flags.X_5));
	}

	@Test
	public void testIncReg16() {
		for (int i = 0; i < 5; i++) {
			int tgt = (i << 4);
			byte[] data = new byte[] {
					(byte)(tgt | 0x03)
			};
			byte[] decdata = new byte[] {
					(byte)(tgt | 0x0B)
			};
			// index 4 is special - test IX!
			if (i == 4) {
				data = new byte[] {
						(byte)0xDD,(byte)0x23
				};
				decdata = new byte[] {
						(byte)0xDD,(byte)0x2B
				};
			}
			System.out.println("testing inc register " + i + " (note: 4 == IX)");
			Core c = executeTest(data, new short[] {
					(short)((i == 4) ? 0x02 : 0x01)
			},(byte)0);
			Register.Register16[] regs = new Register.Register16[] {c.registers.REG_BC, c.registers.REG_DE, c.registers.REG_HL, c.registers.stackPointer, c.registers.REG_IX};
			assertFlags(c.registers.flags, (byte) 0);
			assertThat(regs[i].getData()).isEqualTo((short)1);
			System.out.println("testing dec");
			c = executeTest(decdata, new short[] {
					(short)((i == 4) ? 0x02 : 0x01)
			},(byte)0);
			regs = new Register.Register16[] {c.registers.REG_BC, c.registers.REG_DE, c.registers.REG_HL, c.registers.stackPointer, c.registers.REG_IX};
			assertFlags(c.registers.flags, (byte) 0);
			assertThat(regs[i].getData()).isEqualTo((short)0xFFFF);
			System.out.println("tests on this register completed.");
		}
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
