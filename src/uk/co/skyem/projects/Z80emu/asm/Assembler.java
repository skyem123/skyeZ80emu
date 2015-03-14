package uk.co.skyem.projects.Z80emu.asm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import uk.co.skyem.projects.Z80emu.asm.Token.*;
import uk.co.skyem.projects.Z80emu.asm.AssemblerException.*;
import uk.co.skyem.projects.Z80emu.util.InternalExcpetion;

public class Assembler {

	private static final HashMap<String, Class<? extends ASMDirective>> asmDirectives = new HashMap<>();
	private static final HashMap<String, Class<? extends CPUInstruction>> instructions = new HashMap<>();

	static {
		registerToken("TOKEN", CPUInstruction.class);
	}

	private static void registerToken(String name, Class<? extends Token> tokenClass) {
		if (tokenClass.isAssignableFrom(ASMDirective.class)) {
			asmDirectives.put(name, (Class<? extends ASMDirective>) tokenClass);
		} else {
			instructions.put(name, (Class<? extends CPUInstruction>) tokenClass);
		}
	}

	private static ASMDirective getASMDirective(String name, int lineNumber, String line, String arguments) throws InvalidTokenException {
		if(asmDirectives.containsKey(name)) {
			try {
				return asmDirectives.get(name).getConstructor(String.class).newInstance(arguments);
			} catch (Exception e) {
				throw new InternalExcpetion();
			}
		} else {
			throw new InvalidTokenException(name, true, lineNumber, line);
		}
	}

	private static CPUInstruction getCPUInstruction(String name, int lineNumber, String line, String arguments) throws InvalidTokenException {
		if(instructions.containsKey(name)) {
			try {
				return instructions.get(name).getConstructor(String.class).newInstance(arguments);
			} catch (Exception e) {
				throw new InternalExcpetion();
			}
		} else {
			throw new InvalidTokenException(name, false, lineNumber, line);
		}
	}

	// Our regex patterns
	// Gets rid of repeated whitespace and whitespace at the start of a line
	private static final Pattern PATTERN_REPEATED_BLANKS = Pattern.compile("(\".*\")|(?<![^\\s])\\s+");
	// Gets rid of comments (things after ';')
	private static final Pattern PATTERN_COMMENTS = Pattern.compile(";.*");
	// Gets rid of whitespace at the end of a line
	private static final Pattern PATTERN_WHITESPACE_EOL = Pattern.compile("(\\s+(?=$|\\n))");

	private Assembler() {
	}

	/**
	 * Assembled Z80 asm to Z80 machine code.
	 *
	 * @param input The source code
	 * @return a bytearray of assembled Z80 code.
	 */
	public static byte[] assemble(String input) {
		System.out.println(preparse(input));
		return new byte[0];
	}

	// Public just for now...
	public static String[] preparse(String input) {
		String[] splitted = input.split("\n");
		for (int i = 0; i < splitted.length; i++) {
			String line = splitted[i];
			// Order is probably important
			line = regexReplaceAll(PATTERN_COMMENTS, line, "");
			line = regexReplaceAll(PATTERN_REPEATED_BLANKS, line, "$1");
			line = regexReplaceAll(PATTERN_WHITESPACE_EOL, line, "");

			if(line.length() == 0) line = null;
			splitted[i] = line;
		}
		return splitted;
	}

	// Takes preparsed code and spits out tokens.
	private static Token[] tokenize(String[] input) {
		return new Token[0];
	}

	private static String regexReplaceAll(Pattern pattern, String input, String replace) {
		return pattern.matcher(input).replaceAll(replace);
	}
}
