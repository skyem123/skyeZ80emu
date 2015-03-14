package uk.co.skyem.projects.Z80emu;

import uk.co.skyem.projects.Z80emu.Registers;

import javax.print.DocFlavor;

/**
 * A register is an object that can hold data, in the form of a primitive.
 * You can set and get individual flags, the flags are located in {@link Registers}
 *
 * @author Vic Nightfall
 */
public abstract class Register<T extends Number> {

	private Register() {
	}

	public abstract boolean getFlag(int flag);

	public abstract void setFlag(int flag, boolean value);

	public abstract void toggleFlag(int flag);

	public abstract T getData();

	public abstract void setData(T data);

	@SuppressWarnings({"unchecked"})
	public final void clear() {
		setData((T) (Integer) 0);
	}

	public static final class Register8 extends Register<Byte> {

		public Register8() {
			Registers.registers.add(this);
		}

		public byte data;

		@Override
		public boolean getFlag(int flag) {
			return (data & flag) != 0;
		}

		@Override
		public void setFlag(int flag, boolean value) {
			if (value) {
				data = (byte) (data | flag);
			} else {
				data = (byte) (data & ~flag);
			}
		}

		@Override
		public void toggleFlag(int flag) {
			data = (byte) (data ^ flag);
		}

		@Override
		public Byte getData() {
			return data;
		}

		@Override
		public void setData(Byte data) {
			this.data = data;
		}
	}

	public static final class Register16 extends Register<Short> {

		private final Register8 lower;
		private final Register8 upper;

		public Register16() {
			this(new Register8(), new Register8());
		}

		public Register16(Register8 upper, Register8 lower) {
			this.lower = lower;
			this.upper = upper;
		}

		@Override
		public boolean getFlag(int flag) {
			if(flag > 127) {
				return upper.getFlag(flag >> 8);
			} else {
				return lower.getFlag(flag);
			}
		}

		@Override
		public void setFlag(int flag, boolean value) {
			if(flag > 127) {
				upper.setFlag(flag >> 8, value);
			} else {
				lower.setFlag(flag, value);
			}
		}

		@Override
		public void toggleFlag(int flag) {
			if(flag > 127) {
				upper.toggleFlag(flag >> 8);
			} else {
				lower.toggleFlag(flag);
			}
		}

		@Override
		public Short getData() {
			return (short) (lower.getData() | upper.getData() >> 8);
		}

		@Override
		public void setData(Short data) {
			byte data1 = (byte) (data & 0xFF);
			byte data2 = (byte) ((data & 0xFF00) >> 8);

			lower.setData(data1);
			upper.setData(data2);
		}
	}
}
