package uk.co.skyem.projects.Z80emu.asm;

import java.util.regex.Pattern;

public class Assembler {
	// Our regex patterns
	// Gets rid of repeated whitespace and whitespace at the start of a line
	private static final Pattern PATTERN_REPEATED_BLANKS = Pattern.compile("(?<![^\\s])\\s+");
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

	private static String preparse(String input) {
		// Order is probably important
		input = regexReplaceAll(PATTERN_COMMENTS, input, "");
		input = regexReplaceAll(PATTERN_REPEATED_BLANKS, input, "");
		input = regexReplaceAll(PATTERN_WHITESPACE_EOL, input, "");
		return input;
	}

	private static String regexReplaceAll(Pattern pattern, String input, String replace) {
		return pattern.matcher(input).replaceAll(replace);
	}
}
