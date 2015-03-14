package uk.co.skyem.projects.Z80emu.asm;

import java.util.HashMap;
import java.util.regex.Pattern;

import uk.co.skyem.projects.Z80emu.asm.Token.*;
import uk.co.skyem.projects.Z80emu.asm.AssemblerException.*;
import uk.co.skyem.projects.Z80emu.util.InternalException;

public class Assembler {

	private static final HashMap<String, Class<? extends Instruction>> instructions = new HashMap<>();
	private static final HashMap<String, Class<? extends ASMDirective>> asmDirectives = new HashMap<>();

	static {
		registerToken("TOKEN", CPUInstruction.class);
	}

	private static void registerToken(String name, Class<? extends Instruction> clazz) {
		instructions.put(name, clazz);
		if (clazz.isAssignableFrom(ASMDirective.class)) {
			asmDirectives.put(name, (Class<? extends ASMDirective>) clazz);
		}
	}

	private Instruction getInstruction(String name, String arguments) throws InvalidInstructionException {
		if (instructions.containsKey(name)) {
			try {
				return instructions.get(name).getConstructor(String.class).newInstance(arguments);
			} catch (Exception e) {
				throw new InternalException(e);
			}
		} else {
			throw new InvalidInstructionException(name, lineNumber, line);
		}
	}

	private ASMDirective getASMDirective(String name, String arguments) throws InvalidASMDirectiveException {
		try {
			Instruction instruction = getInstruction(name, arguments);
			if (instruction instanceof ASMDirective) {
				return (ASMDirective) instruction;
			}
		} finally {
			throw new InvalidASMDirectiveException(line, lineNumber, name);
		}
	}

	// Our regex patterns
	// Gets rid of all unnecessary blanks
	private static final Pattern PATTERN_UNNECESSARY_BLANKS = Pattern.compile("(\".*\")|(?<![^\\s])\\s+|\\s+$");
	// Gets rid of comments (things after ';')
	private static final Pattern PATTERN_COMMENTS = Pattern.compile(";.*");

	private String source;

	private short origin;
	private int lineNumber;
	private String line;

	public Assembler(String source) {
		this.source = source;
	}

	/**
	 * Assemble Z80 asm to Z80 machine code.
	 *
	 * @return a bytearray of assembled Z80 code.
	 */
	public void assemble() throws AssemblerException {
		String[] preparsed = preparse();
		Token[] tokens = tokenize(preparsed);
	}

	// Public for now...
	public String[] preparse() {
		String[] splitted = source.split("\n");
		for (int i = 0; i < splitted.length; i++) {
			String line = splitted[i];

			line = regexReplaceAll(PATTERN_COMMENTS, line, "");
			line = regexReplaceAll(PATTERN_UNNECESSARY_BLANKS, line, "$1");

			if(line.length() == 0) line = null;
			splitted[i] = line;
		}
		return splitted;
	}

	// Takes pre-parsed code and spits out tokens.
	private Token[] tokenize(String[] input) throws AssemblerException {
		// Turn ALL tokens to a token array
		// Then sort out labels

		for (lineNumber = 0; lineNumber < input.length; lineNumber++) {
			line = input[lineNumber];
			Token token = getInstruction("stuff", "args");
		}

		return new Token[0];
	}

	private static String regexReplaceAll(Pattern pattern, String input, String replace) {
		return pattern.matcher(input).replaceAll(replace);
	}
}
