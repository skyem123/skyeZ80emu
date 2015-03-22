package uk.co.skyem.projects.Z80emu.asm;

import uk.co.skyem.projects.Z80emu.asm.AssemblerException.InvalidASMDirectiveException;
import uk.co.skyem.projects.Z80emu.asm.AssemblerException.InvalidInstructionException;
import uk.co.skyem.projects.Z80emu.asm.Token.*;
import uk.co.skyem.projects.Z80emu.asm.Token.Instruction;
import uk.co.skyem.projects.Z80emu.util.InternalException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

public class Assembler {

	private static final HashMap<String, Class<? extends Instruction>> instructions = new HashMap<>();
	private static final HashMap<String, Class<? extends ASMDirective>> asmDirectives = new HashMap<>();

	static {
		registerToken("LD", instructionLD.class);
	}

	private String source;
	private short origin;
	private int lineNumber;
	private String line;

	public Assembler(String source) {
		this.source = source;
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

	/**
	 * Assemble Z80 asm to Z80 machine code.
	 */
	public void assemble() throws AssemblerException {
		String[] preparsed = preparse();
		List<Token> tokens = tokenize(preparsed);
	}

	// Public for now...
	public String[] preparse() {
		String[] splitted = source.split("\n");
		for (int i = 0; i < splitted.length; i++) {
			String line = splitted[i];

			line = Patterns.regexReplaceAll(Patterns.COMMENTS, line, "");

			// Matcher for repeated blanks
			Matcher matcher = Patterns.UNNECESSARY_BLANKS.matcher(line);
			while (matcher.find()) {
				// If we could find a group 6 (which is the repeated blank)
				if (matcher.group(6) != null) {
					int start = matcher.start(6);
					int end = matcher.end(6);
					// Take the blanks out
					line = line.substring(0, start) + line.substring(end);
					// Feed the matcher with the new, replaced version
					matcher.reset(line);
				}
			}

			if (line.length() == 0) line = null;
			splitted[i] = line;
		}
		return splitted;
	}

	// Takes pre-parsed code and spits out tokens.
	private List<Token> tokenize(String[] input) throws AssemblerException {

		List<Token> tokens = new ArrayList<>();
		// Turn ALL tokens to a token array
		// Then sort out labels

		for (lineNumber = 0; lineNumber < input.length; lineNumber++) {
			line = input[lineNumber];

			Matcher labelMatcher = Patterns.LABEL.matcher(line);
			if (labelMatcher.matches()) {
				// The whole line matches, so we only have a label here
				tokens.add(new Token.Label(labelMatcher.group(1)));
				continue;
			} else if(labelMatcher.find()) {
				tokens.add(new Token.Label(labelMatcher.group(1)));
				line = line.substring(labelMatcher.end());
			}

			Matcher instructionMatcher = Patterns.INSTRUCTION.matcher(line);
			if (instructionMatcher.find()) {
				if (instructionMatcher.group(2) != null) {
					// We have an asm directive because it matches the starting dot
					tokens.add(getASMDirective(instructionMatcher.group(1), line.substring(instructionMatcher.end())));
				} else {
					// We have an unspecified instruction
					tokens.add(getInstruction(instructionMatcher.group(1), line.substring(instructionMatcher.end())));
				}
			} else {
				throw new AssemblerException.SyntaxException(lineNumber, line);
			}
		}

		return tokens;
	}
}
