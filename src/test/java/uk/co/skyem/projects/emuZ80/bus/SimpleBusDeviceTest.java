package uk.co.skyem.projects.emuZ80.bus;

import org.junit.Test;
import uk.co.skyem.projects.emuZ80.util.buffer.AbstractByteBuffer;

import static org.assertj.core.api.Assertions.*;

public class SimpleBusDeviceTest {
	@Test
	public void testGetBytes() throws Exception {
		SimpleBusDevice simpleBusDevice = new SimpleBusDevice(42) {
			private byte[] datas = new byte[8];
			@Override
			public void putByte(int position, byte data) {
				if (!(position < offset || position >= datas.length + offset)) {
					datas[position - offset] = data;
				}
			}

			@Override
			public byte getByte(int position) {
				if (!(position < offset || position >= datas.length + offset)) {
					return datas[position - offset];
				} else return (byte) 0;
			}
		};
		System.out.println("Testing SimpleBusDevice.getBytes()...");
		simpleBusDevice.putByte(42, (byte)0xFF);
		simpleBusDevice.putByte(43, (byte)0xAA);
		simpleBusDevice.putByte(44, (byte)0x01);
		simpleBusDevice.putByte(45, (byte)0x44);
		simpleBusDevice.putByte(46, (byte)0x54);
		simpleBusDevice.putByte(47, (byte)0xF5);
		simpleBusDevice.putByte(48, (byte)0x3D);

		assertThat(simpleBusDevice.getBytes(42, 8)).isEqualTo(
			new byte[] {(byte)0xFF, (byte)0xAA, (byte)0x01, (byte)0x44, (byte)0x54, (byte)0xF5, (byte)0x3D, (byte)0x00});

		System.out.println("Testing SimpleBusDevice.getBytes() with different offset...");
		simpleBusDevice.changeOffset(34523);
		assertThat(simpleBusDevice.getOffset()).isEqualTo(34523);

		assertThat(simpleBusDevice.getBytes(34523, 8)).isEqualTo(
			new byte[] {(byte)0xFF, (byte)0xAA, (byte)0x01, (byte)0x44, (byte)0x54, (byte)0xF5, (byte)0x3D, (byte)0x00});
	}
}
