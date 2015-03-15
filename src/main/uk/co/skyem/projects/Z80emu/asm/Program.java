package uk.co.skyem.projects.Z80emu.asm;

import uk.co.skyem.projects.Z80emu.util.buffer.ByteBuffer;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

public class Program extends ByteBuffer {

	private final int origin;

	public Program(int origin) {
		super(Endian.LITTLE);
		this.origin = origin;
	}

	public static Program combine(Program... programs) {
		if(programs.length < 1) throw new IllegalArgumentException("Can't combine an empty array of programs!");
		Program firstProgram;
		Stream<Program> sortedPrograms = Arrays.stream(programs).sorted((program1, program2) -> Integer.compare(program1.origin, program2.origin));
		firstProgram = sortedPrograms.findFirst().get();
		sortedPrograms.forEach(program -> firstProgram.insert(program.origin, program));
		return firstProgram;
	}
}
