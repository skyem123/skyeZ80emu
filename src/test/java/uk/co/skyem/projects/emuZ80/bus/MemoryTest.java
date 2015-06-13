package uk.co.skyem.projects.emuZ80.bus;

import org.junit.Before;
import org.junit.Test;
import uk.co.skyem.projects.emuZ80.util.buffer.AbstractByteBuffer;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;
import static uk.co.skyem.projects.emuZ80.util.buffer.AbstractByteBuffer.Endian;

public class MemoryTest {
	int memory1size = 10;
	Memory memory1;
	final byte[] testBytes1 = {(byte) 0x90, (byte) 0x29, (byte) 0x42, (byte) 0x12, (byte) 0x34};
	final byte[] testBytes2 = {(byte) 0x6, (byte) 0x23, (byte) 0x86, (byte) 0x10, (byte) 0xFF, (byte) 0x01, (byte) 0xAA, (byte) 0xBB, (byte) 0x00, (byte) 0xDD}; //max sizes array
	final byte[] testBytes3 = {(byte) 0xF0, (byte) 0xB0, (byte) 0x0B}; //smaller array

	@Before
	public void setUp() throws Exception {
		System.out.println("Making new Memory of size " + memory1size);
		memory1 = new Memory(memory1size);
	}

	/*
	@After
	public void tearDown() throws Exception {

	}
	*/

	@Test
	public void testGetSize() throws Exception {
		System.out.println("Making sure that memory1 has a size of " + memory1size);
		assertThat(memory1.getSize()).isEqualTo(memory1size);
	}

	@Test
	public void testPutByte() throws Exception {
		System.out.println("Making sure that memory1 can set an retrieve a byte.");
		byte testByte = (byte) 0x42;
		memory1.putByte(0, testByte);
		assertThat(memory1.getByte(0)).isEqualTo(testByte);
	}

	@Test
	public void testGetByte() throws Exception {
		System.out.println("Making sure that memory1 can set and retrieve a bytes one by one.");
		byte[] result;
		IntStream.range(0, testBytes1.length)
				.parallel()
				.forEach(i -> memory1.putByte(i, testBytes1[i]));

		IntStream.range(0, testBytes1.length)
				.parallel()
				.forEach(i -> assertThat(memory1.getByte(i)).isEqualTo(testBytes1[i]));
		result = memory1.getBytes(0, testBytes1.length);

		assertThat(Arrays.equals(result, testBytes1));
	}

	@Test
	public void testPutBytes() throws Exception {
		System.out.println("Making sure that memory1 can set byte[] and retrieve it as array and one by one");
		int offset = 2;
		byte[] result;

		memory1.putBytes(offset, testBytes1);

		result = memory1.getBytes(offset, testBytes1.length + offset);

		assertThat(Arrays.equals(result, testBytes1));
		IntStream.range(0, testBytes1.length)
				.parallel()
				.forEach(i -> assertThat(memory1.getByte(i + offset)).isEqualTo(testBytes1[i]));
	}

	@Test
	public void testGetBytes() throws Exception {
		System.out.println("Making sure that memory1 can set byte[] and retrieve them as arrays");
		byte[] result;
		byte[] result1;
		byte[] result2;
		int split = new Random().nextInt( testBytes2.length);

		memory1.putBytes(0, testBytes2);

		result = memory1.getBytes(0, testBytes2.length);
		result1 = memory1.getBytes(0, split);
		result2 = memory1.getBytes(split, testBytes2.length - split);

		assertThat(Arrays.equals(result, testBytes2));
		assertThat(Arrays.equals(result1, Arrays.copyOfRange(testBytes2, 0, split)));
		assertThat(Arrays.equals(result2, Arrays.copyOfRange(testBytes2, split, testBytes2.length)));
	}

	@Test
	public void testGetBytes1() throws Exception {
		System.out.println("Making sure that memory1 can set bytes and retrieve them as arrays");
		byte[] result;

		int offset = memory1size - testBytes3.length;

		IntStream.range(0, testBytes1.length)
				.parallel()
				.forEach(i -> memory1.putByte(i + offset, testBytes1[i]));

		result = memory1.getBytes(offset, testBytes3.length);

		assertThat(Arrays.equals(result, testBytes3));
	}

	@Test
	public void testGetOffset() throws Exception {
		System.out.println("Making sure that memory1 can get the correct offset | doing nothing");
		//TODO ask skye about offset
	}

	@Test
	public void testChangeOffset() throws Exception {
		System.out.println("Making sure that memory1 can set the offset and set and retrieve data | doing nothing");
		//TODO ask skye about offset
	}

	@Test
	public void testGetEndian() throws Exception {
		System.out.println("Making sure that memory1 can set endian and retrieve the correct value");

		assertThat(memory1.getEndian().equals(Endian.BIG));
		memory1.setEndian(Endian.LITTLE_ALT);
		assertThat(memory1.getEndian().equals(Endian.LITTLE_ALT));
		memory1.setEndian(Endian.LITTLE);
		assertThat(memory1.getEndian().equals(Endian.LITTLE));
	}

	@Test
	public void testSetEndian() throws Exception {
		System.out.println("Making sure that memory1 can set endian and set and retrieve bytes");

		memory1.setEndian(Endian.LITTLE);
		//TODO add tests

		memory1.setEndian(Endian.LITTLE_ALT);
		//TODO add tests
	}

	@Test
	public void testGetBytes2() throws Exception {

	}

	@Test
	public void testGetWord() throws Exception {

	}

	@Test
	public void testGetDWord() throws Exception {

	}

	@Test
	public void testGetQWord() throws Exception {

	}

	@Test
	public void testPutWord() throws Exception {

	}

	@Test
	public void testPutDWord() throws Exception {

	}

	@Test
	public void testPutQWord() throws Exception {

	}

	@Test
	public void testPutBytes1() throws Exception {

	}
}
