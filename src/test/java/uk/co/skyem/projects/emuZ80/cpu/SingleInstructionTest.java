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
			assertThat(c.registers.getProgramCounter()).isEqualTo(positions[i]).describedAs("PC check after instruction " + i);
		}
		return c;
	}

	@Test
	public void testNop() {
		short[] positions = new short[65536];
		for (int i = 0; i < 65536; i++)
			positions[i] = (short) ((i + 1) & 0xFFFF);
		Core c = executeTest(new byte[65536], positions, (byte) 0);
		// do a minor check just in case
		assertFlags(c.registers.flags, (byte) 0);
		assertThat(c.registers.REG_A.getData()).isEqualTo((byte) 0);
	}

	public void LdAConst_Adc_A_PtrHL_Subtest(boolean carryIn, byte vA, byte vAO, byte vF) {
		Core c = executeTest(new byte[]{
				(byte) 0x3E, (byte) vA, // ld A, 0x12
				(byte) 0x8E // adc A,(HL) ; (HL) == 0x50
		}, new short[]{
				0x02, 0x03
		}, (byte) (carryIn ? Flags.CARRY : 0));
		assertThat(c.registers.REG_A.getData()).isEqualTo((byte) vAO);
		assertFlags(c.registers.flags, vF);
	}

	@Test
	public void testLdAConst_AddAdc_A_PtrHL() {
		Core c = executeTest(new byte[]{
				(byte) 0x3E, (byte) 0x12, // ld A, 0x12
				(byte) 0x86 // add A,(HL) ; (HL) == 0x50
		}, new short[]{
				0x02, 0x03
		}, (byte) 0);
		assertThat(c.registers.REG_A.getData()).isEqualTo((byte) 0x50);
		assertFlags(c.registers.flags, (byte) (Flags.HALF_CARRY));
		c = executeTest(new byte[]{
				(byte) 0x3E, (byte) 0xC2, // ld A, 0xC2
				(byte) 0x86 // add A,(HL) ; (HL) == 0x00 (carry)
		}, new short[]{
				0x02, 0x03
		}, (byte) 0);
		assertThat(c.registers.REG_A.getData()).isEqualTo((byte) 0x00);
		assertFlags(c.registers.flags, (byte) (Flags.HALF_CARRY | Flags.CARRY | Flags.ZERO));

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
	public void testIncDecReg16() {
		for (int i = 0; i < 5; i++) {
			int tgt = (i << 4);
			byte[] data = new byte[]{
					(byte) (tgt | 0x03)
			};
			byte[] decdata = new byte[]{
					(byte) (tgt | 0x0B)
			};
			// index 4 is special - test IX!
			if (i == 4) {
				data = new byte[]{
						(byte) 0xDD, (byte) 0x23
				};
				decdata = new byte[]{
						(byte) 0xDD, (byte) 0x2B
				};
			}
			System.out.println("testing inc register " + i + " (note: 4 == IX)");
			Core c = executeTest(data, new short[]{
					(short) ((i == 4) ? 0x02 : 0x01)
			}, (byte) 0);
			Register.Register16[] regs = new Register.Register16[]{c.registers.REG_BC, c.registers.REG_DE, c.registers.REG_HL, c.registers.stackPointer, c.registers.REG_IX};
			assertFlags(c.registers.flags, (byte) 0);
			assertThat(regs[i].getData()).isEqualTo((short) 1);
			System.out.println("testing dec");
			c = executeTest(decdata, new short[]{
					(short) ((i == 4) ? 0x02 : 0x01)
			}, (byte) 0);
			regs = new Register.Register16[]{c.registers.REG_BC, c.registers.REG_DE, c.registers.REG_HL, c.registers.stackPointer, c.registers.REG_IX};
			assertFlags(c.registers.flags, (byte) 0);
			assertThat(regs[i].getData()).isEqualTo((short) 0xFFFF);
			System.out.println("tests on this register completed.");
		}
	}

	@Test
	public void testIncDecLdI8WithIX() {
		testIncDecLdI8Main(true);
	}

	@Test
	public void testIncDecLdI8NoIX() {
		testIncDecLdI8Main(false);
	}

	public void testIncDecLdI8Main(boolean index) {
		// Columns.
		// This table maps to positions on the X axis on http://clrhome.org/table/
		// where IncDecLd8 instructions are.
		// There are two blocks of 3 columns each, and 4 rows.
		// 0x4 - 5 - 6 has B D H (HL).
		// 0xC - D - E has C E L A.
		// Ofc, if Index mode is enabled, more "fun" occurs, since now it becomes:
		// 0x4 - 5 - 6 has B D IXH (IX).
		// 0xC - D - E has C E IXL A.
		// And ofc alterations need to be made to reflect that.
		byte[] columns = {
				0x04,
				0x05,
				0x06,
				// --
				(byte) 0x0C,
				(byte) 0x0D,
				(byte) 0x0E,
		};

		for (int i = 0; i < 6; i++) {
			int mode = i % 3;
			// mode: inc, dec, ldi
			for (int reg = 0; reg < 4; reg++) {
				byte[] data = new byte[0];
				int r = reg | (i > 2 ? 4 : 0);
				int a = reg << 4;
				System.out.println(new String[]{"INC ", "DEC ", "LDI "}[mode] + new String[]{
						"B", "D", (index ? "IXH" : "H"), (index ? "(IX + 0)" : "(HL)"),
						"C", "E", (index ? "IXL" : "L"), "A",
				}[r] + " ; (" + Integer.toHexString(columns[i] | a) + ")");
				if (index) {
					if (r == 3) {
						// (IX), requires offset
						switch (mode) {
							case 0:
							case 1:
								data = new byte[]{
										0x00, // NOP (used for HL indirect)
										(byte) 0xDD, // IX
										(byte) (columns[i] | a), // inc/dec (reg)
										0x00, // offset
								};
								break;
							case 2:
								data = new byte[]{
										0x00,
										(byte) 0xDD, // IX
										(byte) (columns[i] | a), // ldi
										0x00, // offset
										0x55 // 0x55 ; ldi (IX + 0), 0x55
								};
								break;
						}
					} else {
						// does not require offset
						switch (mode) {
							case 0:
							case 1:
								data = new byte[]{
										0x00, // NOP (used for HL indirect)
										(byte) 0xDD, // IX
										(byte) (columns[i] | a), // inc/dec (reg)
								};
								break;
							case 2:
								data = new byte[]{
										0x00,
										(byte) 0xDD, // IX
										(byte) (columns[i] | a),
										0x55 // ldi (reg), 0x55
								};
								break;
						}
					}
				} else {
					switch (mode) {
						case 0:
						case 1:
							data = new byte[]{
									0x00, // NOP (used for HL indirect)
									(byte) (columns[i] | a), // inc/dec (reg)
							};
							break;
						case 2:
							data = new byte[]{
									0x00,
									(byte) (columns[i] | a), 0x55 // ldi (reg), 0x55
							};
							break;
					}
				}
				Core c = executeTest(data, new short[]{
						0x01,
						(short) (((mode == 2) ? 0x03 : 0x02) + (index ? ((r == 3) ? 0x02 : 0x01) : 0x00))
				}, (byte) 0);
				byte correctVal = 0x00;
				byte correctFlags = 0x00;
				switch (mode) {
					case 0:
						correctVal = 0x01;
						correctFlags = 0x00;
						break;
					case 1:
						correctVal = (byte) 0xFF;
						correctFlags = (byte) (Flags.ADD_SUB | Flags.X_3 | Flags.X_5 | Flags.SIGN | Flags.HALF_CARRY);
						break;
					case 2:
						correctVal = 0x55;
						correctFlags = 0x00;
						break;
				}
				Register.Register8[] setA = new Register.Register8[]{c.registers.REG_B, c.registers.REG_D, (index ? c.registers.REG_IXH : c.registers.REG_H), null};
				Register.Register8[] setB = new Register.Register8[]{c.registers.REG_C, c.registers.REG_E, (index ? c.registers.REG_IXL : c.registers.REG_L), c.registers.REG_A};
				Register.Register8[] set = (r < 4) ? setA : setB;
				byte outputVal = 0x00;
				if (set[reg] == null) {
					// NOTE: If it's written anywhere but here, that's an error in itself
					outputVal = data[0];
				} else {
					outputVal = set[reg].getData();
				}
				assertThat(outputVal).isEqualTo(correctVal);
				assertFlags(c.registers.flags, correctFlags);
			}
		}
	}

	@Test
	public void testLd8RegRegNoIX() {
		testLd8RegRegRun(false);
	}

	@Test
	public void testLd8RegRegWithIX() {
		testLd8RegRegRun(true);
	}

	@Test
	public void testHalt() {
		Core c = executeTest(new byte[]{
				0x76
		}, new short[]{
				0x1
		}, (byte) 0);
		assertThat(c.halted()).isTrue();
	}

	public void testLd8RegRegRun(boolean index) {
		for (int i = 0x40; i < 0x80; i++) {
			int left = (i & 0x38) >> 3;
			int right = i & 0x7;
			byte[] regLdIs = {
					0x06,//B
					0x0E,//C
					0x16,//D
					0x1E,//E
					0x26,//H
					0x2E,//L
					0x36,//(HL)
					0x3E,//A
			};
			String[] regNames = {
					"B", "C", "D", "E",
					"H", "L", (index ? "(IX + 6=6)" : "(HL=0)"), "A"
			};
			boolean leftHL = left == 6;
			boolean rightHL = right == 6;
			boolean isHalt = (leftHL && rightHL);
			boolean hasLeftOffset = leftHL && index;
			boolean hasRightOffset = rightHL && index;

			byte resultVal = (byte) (~i); // should make a good per-test marker
			// NOTE: here the assumption is made that the LDI8 tests worked,
			// because we need to load registers as the CPU would have loaded them.
			// Most of these operations will be:
			byte[] data = {
					regLdIs[right],
					resultVal,
					(byte) i
			};
			short[] ja = {
					0x02,
					0x03,
			};
			// the following conditions would need custom test cases.
			// As it is, it's enough to prove the instruction logic is sound.
			// (also, Halt has it's own test case anyway)
			if (isHalt)
				continue;
			if (index) {
				if (hasLeftOffset)
					continue;
				if (hasRightOffset)
					continue;
			}
			if (leftHL) {
				if ((right == 5) || (right == 4))
					continue;
			}
			// ---
			System.out.println("About to execute LD " + regNames[left] + ", " + regNames[right]);
			Core c = executeTest(data, ja, (byte) 0);
			if (leftHL) {
				assertThat(data[hasLeftOffset ? 6 : 0]).isEqualTo(resultVal);
			} else {
				Register.Register8[] regs = {
						c.registers.REG_B,
						c.registers.REG_C,
						c.registers.REG_D,
						c.registers.REG_E,
						c.registers.REG_H,
						c.registers.REG_L,
						null,
						c.registers.REG_A
				};
				assertThat(regs[left].getData()).isEqualTo(resultVal);
			}
		}
	}

	// jumps are easier to test than anything else, really
	@Test
	public void testCoAbJumpsTrue() {
		testCoAbJumpsRun(true);
	}

	@Test
	public void testCoAbJumpsFalse() {
		testCoAbJumpsRun(false);
	}

	// shouldPass: should the jumps actually jump
	public void testCoAbJumpsRun(boolean shouldPass) {
		byte[] memory = new byte[0x3];
		// 11CCC010
		//0xC2 | (condition)
		for (int condition = 0; condition < 8; condition++) {
			int flag = InstructionDecoder.conditionTable[condition].flagVal;
			boolean flagState = InstructionDecoder.conditionTable[condition].expectedResult;
			if (!shouldPass)
				flagState = !flagState;
			memory[0] = (byte) (0xC2 | (condition << 3));
			memory[1] = (byte) 0x00;
			memory[2] = (byte) 0x80;
			// expectedResult says if the flag should be true
			Core c = executeTest(memory, new short[]{
					(short) (shouldPass ? 0x8000 : 0x03)
			}, (byte) (flagState ? flag : ~flag));
			// executeTest did the required checks on PC
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
