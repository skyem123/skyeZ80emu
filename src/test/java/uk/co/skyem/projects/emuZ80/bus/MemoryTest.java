package uk.co.skyem.projects.emuZ80.bus;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.*;

public class MemoryTest {
	int memory1size = 10;
	Memory memory1;
	final byte[] testBytes1 = {(byte) 0x90, (byte) 0x29, (byte) 0x42, (byte) 0x12, (byte) 0x34};
	final byte[] testBytes2 = {(byte) 0x6, (byte) 0x23, (byte) 0x86, (byte) 0x10, (byte) 0xFF, (byte) 0x01, (byte) 0xAA, (byte) 0xBB, (byte) 0x00, (byte) 0xDD};
	final byte[] testBytes3 = {(byte) 0xF0, (byte) 0xB0, (byte) 0x0B, (byte) 0x51};

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
		IntStream.range(0, testBytes1.length)
				.parallel()
				.forEach(i -> memory1.putByte(i, testBytes1[i]));
		IntStream.range(0, testBytes1.length)
				.parallel()
				.forEach(i -> assertThat(memory1.getByte(i)).isEqualTo(testBytes1[i]));
	}

	@Test
	public void testPutBytes() throws Exception {
		System.out.println("Making sure that memory1 can set byte[] and retrieve it as array and one by one");
		int offset = 2;
		byte[] result1;

		memory1.putBytes(offset, testBytes1);
		result1 = memory1.getBytes(offset, testBytes1.length + offset);
		assertThat(Arrays.equals(result1, testBytes1));
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
		int random = new Random().nextInt( testBytes2.length);

		memory1.putBytes(0, testBytes2);
		result = memory1.getBytes(0, testBytes2.length);
		assertThat(Arrays.equals(result, testBytes2));

		result1 = memory1.getBytes(0, random);
		assertThat(Arrays.equals(result1, Arrays.copyOfRange(testBytes2, 0, random)));

		result2 = memory1.getBytes(random, testBytes2.length - random);
		assertThat(Arrays.equals(result2, Arrays.copyOfRange(testBytes2, random, testBytes2.length - random)));
	}

	@Test
	public void testGetBytes1() throws Exception {

	}

	@Test
	public void testGetOffset() throws Exception {

	}

	@Test
	public void testChangeOffset() throws Exception {

	}

	@Test
	public void testGetEndian() throws Exception {

	}

	@Test
	public void testSetEndian() throws Exception {

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
