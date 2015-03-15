package uk.co.skyem.projects.Z80emu.util.buffer;

import org.junit.Test;
import uk.co.skyem.projects.Z80emu.util.buffer.AbstractByteBuffer.Endian;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertTrue;

public class ByteBufferTest {

	@Test
	public void testPutGetByte() throws Exception {
		ByteBuffer bb = new ByteBuffer(42);
		bb.putByte(0, (byte) 0x43);
		assertTrue(bb.getByte(0) == 0x43);
	}

	@Test
	public void testPutGetWord() throws Exception {

		System.out.println("Testing WORD big endian");
		// BEST NAME 100/10 -IGN
		ByteBuffer bbbig = new ByteBuffer(Endian.BIG, 42);
		bbbig.putWord(0, (short) 0x4342);
		assertTrue(bbbig.getWord(0) == 0x4342);

		System.out.println("Testing WORD little endian");
		ByteBuffer bblittle = new ByteBuffer(Endian.LITTLE, 42);
		bblittle.putWord(1, (short) 0x4342);
		assertTrue(bblittle.getWord(1) == 0x4342);
	}

	@Test
	public void testPutGetDWord() throws Exception {

		System.out.println("Testing DWORD big endian");
		// BEST NAME 100/10 -IGN
		ByteBuffer bbbig = new ByteBuffer(Endian.BIG, 42);
		bbbig.putDWord(0, 0x43424140);
		assertTrue(bbbig.getDWord(0) == 0x43424140);

		System.out.println("Testing DWORD little endian");
		ByteBuffer bblittle = new ByteBuffer(Endian.LITTLE, 42);
		bblittle.putDWord(4, 0x43424140);
		assertTrue(bblittle.getDWord(4) == 0x43424140);
	}

	@Test
	public void testPutGetQWord() throws Exception {
		System.out.println("Testing QWORD big endian");
		// BEST NAME 100/10 -IGN
		ByteBuffer bbbig = new ByteBuffer(Endian.BIG, 42);
		bbbig.putQWord(0, 0x4342414039383736L);
		assertTrue(bbbig.getQWord(0) == 0x4342414039383736L);

		System.out.println("Testing QWORD little endian");
		ByteBuffer bblittle = new ByteBuffer(Endian.LITTLE, 42);
		bblittle.putQWord(8, 0x4342414039383736L);
		assertTrue(bblittle.getQWord(8) == 0x4342414039383736L);
	}

	@Test
	public void testInsert() throws Exception {

	}

	@Test
	public void testInsert1() throws Exception {

	}

	@Test
	public void testAppend() throws Exception {

	}

	@Test
	public void testAppend1() throws Exception {

	}

	@Test
	public void testAppendByte() throws Exception {

	}

	@Test
	public void testAppendWord() throws Exception {

	}

	@Test
	public void testAppendDWord() throws Exception {

	}

	@Test
	public void testAppendQWord() throws Exception {

	}



	/*@Test
	public void testThreadSafe() throws Exception {
		ByteBuffer bb = new ByteBuffer();
		Random random = new Random();
		Map<Integer, Long> expected = new ConcurrentHashMap<>();

		AtomicInteger threads = new AtomicInteger(10);
		for (int i = 0; i < 10; i++) {
			new Thread(() -> {
				for (int j = 0; j < 50; j++) {
					long value = random.nextLong();
					int pos = bb.appendQWord(value);
					expected.put(pos, value);
				}
				threads.decrementAndGet();
			}).start();
		}
		while(threads.get() != 0) Thread.sleep(10);

		for (Map.Entry<Integer, Long> entry : expected.entrySet()) {
			assertTrue(bb.getByte(entry.getKey()) == entry.getValue());
		}
	}*/
}
