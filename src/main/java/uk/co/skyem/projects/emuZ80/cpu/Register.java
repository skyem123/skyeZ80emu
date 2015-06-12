package uk.co.skyem.projects.emuZ80.cpu;

import uk.co.skyem.projects.emuZ80.util.InternalException;
import uk.co.skyem.projects.emuZ80.util.buffer.IByteBuffer;

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

	public abstract void setFlag(long flag, boolean value);

	public abstract void toggleFlag(long flag);

	public abstract T getData();

	public abstract void setData(T data);

	/**
	 * Set data with data from another register.
	 * WARNING: This will cast, not error.
	 */
	public abstract void setData(Register<?> register);

	public abstract void increment();

	public abstract void decrement();

	public abstract void increment(T value);

	public abstract void decrement(T value);

	public abstract void clear();

	public abstract void rotate(int amount);

	public void rotateLeft(int amount) {
		rotate(-amount);
	}

	public void rotateRight(int amount) {
		rotate(amount);
	}

	public abstract int getSize();

	public static class Register8 extends Register<Byte> {

		public byte data;
		public static final int SIZE = Byte.SIZE;

		@Override
		public boolean getFlag(int flag) {
			return (data & flag) != 0;
		}

		@Override
		public void setFlag(long flag, boolean value) {
			if (value) {
				data = (byte) (data | flag);
			} else {
				data = (byte) (data & ~flag);
			}
		}

		@Override
		public void toggleFlag(long flag) {
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

		@Override
		public void setData(Register<?> register) {
			setData((Byte)register.getData());
		}

		@Override
		public void increment() {
			++data;
		}

		@Override
		public void decrement() {
			--data;
		}

		@Override
		public void increment(Byte value) {
			setData((byte)(getData() + value));
		}

		@Override
		public void decrement(Byte value) {
			setData((byte)(getData() - value));
		}

		@Override
		public void clear() {
			setData((byte) 0);
		}

		@Override
		public void rotate(int amount) {
			setData(ALU.rotate(getData(), SIZE, amount));
		}

		@Override
		public int getSize() {
			return SIZE;
		}
	}

	public static class Register16 extends Register<Short> {

		public static final int SIZE = Short.SIZE;

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
			if (flag > 127) {
				return upper.getFlag(flag >>> 8);
			} else {
				return lower.getFlag(flag);
			}
		}

		@Override
		public void setFlag(long flag, boolean value) {
			if (flag > 127) {
				upper.setFlag(flag >>> 8, value);
			} else {
				lower.setFlag(flag, value);
			}
		}

		@Override
		public void toggleFlag(long flag) {
			if (flag > 127) {
				upper.toggleFlag(flag >>> 8);
			} else {
				lower.toggleFlag(flag);
			}
		}

		@Override
		public Short getData() {
			return (short) ((lower.getData() & 0xFF) | upper.getData() << 8);
		}

		@Override
		public void setData(Short data) {
			byte data1 = (byte) (data & 0xFF);
			byte data2 = (byte) (data >>> 8);
			lower.setData(data1);
			upper.setData(data2);
		}

		@Override
		public void setData(Register<?> register) {
			setData((Short)register.getData());
		}

		@Override
		public void increment() {
			setData((short) (getData() + 1));
		}

		@Override
		public void decrement() {
			setData((short) (getData() - 1));
		}

		@Override
		public void increment(Short value) {
			setData((short) (getData() + value));
		}

		@Override
		public void decrement(Short value) {
			setData((short) (getData() - value));
		}

		@Override
		public void clear() {
			setData((short) 0);
		}

		@Override
		public void rotate(int amount) {
			setData(ALU.rotate(getData(), SIZE, amount));
		}

		@Override
		public int getSize() {
			return SIZE;
		}
	}

	public static class MemoryRegister8 extends Register8 {
		private final IByteBuffer memory;
		private final int position;

		public MemoryRegister8(IByteBuffer memory, int position) {
			this.memory = memory;
			this.position = position;
		}

		private byte data() {
			return memory.getByte(position);
		}

		private void data(byte data) {
			memory.putByte(position, data);
		}

		@Override
		public boolean getFlag(int flag) {
			return (data() & flag) != 0;
		}

		@Override
		public void setFlag(long flag, boolean value) {
			if (value) {
				data((byte) (data() | flag));
			} else {
				data((byte) (data() & ~flag));
			}
		}

		@Override
		public void toggleFlag(long flag) {
			data((byte) (data() ^ flag));
		}

		@Override
		public Byte getData() {
			return data();
		}

		@Override
		public void setData(Byte data) {
			data(data);
		}

		@Override
		public void setData(Register<?> register) {
			data((Byte) register.getData());
		}

		@Override
		public void increment() {
			data((byte) (data() + 1));
		}


		@Override
		public void decrement() {
			data((byte) (data() - 1));
		}

		@Override
		public void increment(Byte value) {
			data((byte) (data() + value));
		}

		@Override
		public void decrement(Byte value) {
			data((byte) (data() - value));
		}

		@Override
		public void clear() {
			data((byte) 0);
		}
	}
}