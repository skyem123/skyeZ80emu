package uk.co.skyem.projects.emuZ80.util.buffer;

import org.junit.Test;
import uk.co.skyem.projects.emuZ80.util.buffer.AbstractByteBuffer.Endian;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ByteBufferTest {

	@Test
	public void testPutGetByte() throws Exception {
		System.out.println("Testing put BYTE");
		ByteBuffer bb = new ByteBuffer(42);
		bb.putByte(0, (byte) 0x43);
		assertThat(bb.getByte(0)).isEqualTo((byte) 0x43);
	}

	@Test
	public void testPutGetWord() throws Exception {

		System.out.println("Testing put WORD big endian");
		// BEST NAME 100/10 -IGN
		ByteBuffer bbbig = new ByteBuffer(Endian.BIG, 42);
		bbbig.putWord(0, (short) 0x4342);
		assertThat(bbbig.getWord(0)).isEqualTo((short) 0x4342);

		System.out.println("Testing put WORD little endian");
		ByteBuffer bblittle = new ByteBuffer(Endian.LITTLE, 42);
		bblittle.putWord(1, (short) 0x4342);
		assertThat(bblittle.getWord(1)).isEqualTo((short) 0x4342);
	}

	@Test
	public void testPutGetDWord() throws Exception {

		System.out.println("Testing put DWORD big endian");
		// BEST NAME 100/10 -IGN
		ByteBuffer bbbig = new ByteBuffer(Endian.BIG, 42);
		bbbig.putDWord(0, 0x43424140);
		assertThat(bbbig.getDWord(0)).isEqualTo(0x43424140);

		System.out.println("Testing put DWORD little endian");
		ByteBuffer bblittle = new ByteBuffer(Endian.LITTLE, 42);
		bblittle.putDWord(4, 0x43424140);
		assertThat(bblittle.getDWord(4)).isEqualTo(0x43424140);
	}

	@Test
	public void testPutGetQWord() throws Exception {
		System.out.println("Testing put QWORD big endian");
		// BEST NAME 100/10 -IGN
		ByteBuffer bbbig = new ByteBuffer(Endian.BIG, 42);
		bbbig.putQWord(0, 0x4342414039383736L);
		assertThat(bbbig.getQWord(0)).isEqualTo(0x4342414039383736L);

		System.out.println("Testing put QWORD little endian");
		ByteBuffer bblittle = new ByteBuffer(Endian.LITTLE, 42);
		bblittle.putQWord(8, 0x4342414039383736L);
		assertThat(bblittle.getQWord(8)).isEqualTo(0x4342414039383736L);
	}

	@Test
	public void testInsert() throws Exception {
		System.out.println("Testing insert");
		ByteBuffer bb1 = new ByteBuffer(4);
		bb1.putDWord(0, 0xAAAAAAAA);

		ByteBuffer bb2 = new ByteBuffer(4);
		bb2.putDWord(0, 0xBBBBBBBB);

		bb1.insert(2, bb2);
		assertThat(bb1.getQWord(0)).isEqualTo(0xAAAABBBBBBBBAAAAL);

		ByteBuffer bb3 = new ByteBuffer(2);
		bb3.putWord(0, (short) 0xCCCC);
		bb1.insert(12, bb3);

		assertThat(bb1.getSize()).isEqualTo(14);
		assertThat(bb1.getQWord(6)).isEqualTo(0xAAAA00000000CCCCL);
	}

	@Test
	public void testAppend() throws Exception {
		System.out.println("Testing append");
		ByteBuffer bb1 = new ByteBuffer(4);
		bb1.putDWord(0, 0xAAAAAAAA);

		ByteBuffer bb2 = new ByteBuffer(4);
		bb2.putDWord(0, 0xBBBBBBBB);

		bb1.append(bb2);
		assertThat(bb1.getQWord(0)).isEqualTo(0xAAAAAAAABBBBBBBBL);
	}

	@Test
	public void testAppendByte() throws Exception {
		System.out.println("Testing append BYTE");
		ByteBuffer bb = new ByteBuffer();
		bb.appendByte((byte) 0xAA);
		assertThat(bb.getByte(0)).isEqualTo((byte) 0xAA);
		bb.appendByte((byte) 0xBB);
		assertThat(bb.getByte(0)).isEqualTo((byte) 0xAA);
		assertThat(bb.getByte(1)).isEqualTo((byte) 0xBB);
	}

	@Test
	public void testAppendWord() throws Exception {
		System.out.println("Testing append WORD big endian");
		ByteBuffer bb1 = new ByteBuffer(Endian.BIG);
		bb1.appendWord((short) 0xAAAA);
		assertThat(bb1.getWord(0)).isEqualTo((short) 0xAAAA);
		bb1.appendWord((short) 0xBBBB);
		assertThat(bb1.getWord(0)).isEqualTo((short) 0xAAAA);
		assertThat(bb1.getWord(2)).isEqualTo((short) 0xBBBB);

		System.out.println("Testing append WORD little endian");
		ByteBuffer bb2 = new ByteBuffer(Endian.LITTLE);
		bb2.appendWord((short) 0xAAAA);
		assertThat(bb2.getWord(1)).isEqualTo((short) 0xAAAA);
		bb2.appendWord((short) 0xBBBB);
		assertThat(bb2.getWord(1)).isEqualTo((short) 0xAAAA);
		assertThat(bb2.getWord(3)).isEqualTo((short) 0xBBBB);
	}

	@Test
	public void testAppendDWord() throws Exception {
		System.out.println("Testing append DWORD big endian");
		ByteBuffer bb1 = new ByteBuffer(Endian.BIG);
		bb1.appendDWord(0xAAAAAAAA);
		assertThat(bb1.getDWord(0)).isEqualTo(0xAAAAAAAA);
		bb1.appendDWord(0xBBBBBBBB);
		assertThat(bb1.getDWord(0)).isEqualTo(0xAAAAAAAA);
		assertThat(bb1.getDWord(4)).isEqualTo(0xBBBBBBBB);

		System.out.println("Testing append DWORD little endian");
		ByteBuffer bb2 = new ByteBuffer(Endian.LITTLE);
		bb2.appendDWord(0xAAAAAAAA);
		assertThat(bb2.getDWord(3)).isEqualTo(0xAAAAAAAA);
		bb2.appendDWord(0xBBBBBBBB);
		assertThat(bb2.getDWord(3)).isEqualTo(0xAAAAAAAA);
		assertThat(bb2.getDWord(7)).isEqualTo(0xBBBBBBBB);
	}

	@Test
	public void testAppendQWord() throws Exception {
		System.out.println("Testing append DWORD big endian");
		ByteBuffer bb1 = new ByteBuffer(Endian.BIG);
		bb1.appendQWord(0xAAAAAAAAAAAAAAAAL);
		assertThat(bb1.getQWord(0)).isEqualTo(0xAAAAAAAAAAAAAAAAL);
		bb1.appendQWord(0xBBBBBBBBBBBBBBBBL);
		assertThat(bb1.getQWord(0)).isEqualTo(0xAAAAAAAAAAAAAAAAL);
		assertThat(bb1.getQWord(8)).isEqualTo(0xBBBBBBBBBBBBBBBBL);

		System.out.println("Testing append DWORD little endian");
		ByteBuffer bb2 = new ByteBuffer(Endian.LITTLE);
		bb2.appendQWord(0xAAAAAAAAAAAAAAAAL);
		assertThat(bb2.getQWord(7)).isEqualTo(0xAAAAAAAAAAAAAAAAL);
		bb2.appendQWord(0xBBBBBBBBBBBBBBBBL);
		assertThat(bb2.getQWord(7)).isEqualTo(0xAAAAAAAAAAAAAAAAL);
		assertThat(bb2.getQWord(15)).isEqualTo(0xBBBBBBBBBBBBBBBBL);
	}

	@Test
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
		while (threads.get() != 0) Thread.sleep(10);

		for (Map.Entry<Integer, Long> entry : expected.entrySet()) {
			assertThat(bb.getQWord(entry.getKey())).isEqualTo(entry.getValue());
		}
	}

	@Test
	public void testExceptions() throws Exception {
		ByteBuffer bb = new ByteBuffer(Endian.BIG);
		assertThatThrownBy(() -> bb.getByte(-100)).isInstanceOf(ArrayIndexOutOfBoundsException.class);
		assertThatThrownBy(() -> bb.getByte(+100)).isInstanceOf(ArrayIndexOutOfBoundsException.class);
		assertThatThrownBy(() -> bb.putByte(-100, (byte) 0xFF)).isInstanceOf(ArrayIndexOutOfBoundsException.class);
		assertThatThrownBy(() -> bb.putByte(+100, (byte) 0xFF)).isInstanceOf(ArrayIndexOutOfBoundsException.class);

	}
}
