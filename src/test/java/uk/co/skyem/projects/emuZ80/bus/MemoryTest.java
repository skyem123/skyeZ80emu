package uk.co.skyem.projects.emuZ80.bus;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

public class MemoryTest {
	int memory1size = 10;
	Memory memory1;

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

	}

	@Test
	public void testPutBytes() throws Exception {

	}

	@Test
	public void testGetBytes() throws Exception {

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
