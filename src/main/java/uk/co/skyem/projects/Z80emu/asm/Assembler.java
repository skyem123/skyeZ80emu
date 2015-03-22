package uk.co.skyem.projects.Z80emu.asm;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;
import uk.co.skyem.projects.Z80emu.asm.AssemblerException.InvalidASMDirectiveException;
import uk.co.skyem.projects.Z80emu.asm.AssemblerException.InvalidInstructionException;
import uk.co.skyem.projects.Z80emu.asm.Token.*;
import uk.co.skyem.projects.Z80emu.asm.Token.Instruction;
import uk.co.skyem.projects.Z80emu.util.InternalException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Assembler {

	private static final HashMap<String, Class<? extends Instruction>> instructions = new HashMap<>();
	private static final HashMap<String, Class<? extends ASMDirective>> asmDirectives = new HashMap<>();

	static {
		registerToken("LD", instructionLD.class);
	}
	// Our regex patterns
	// Gets rid of all unnecessary blanks
	private static final Pattern PATTERN_UNNECESSARY_BLANKS = Pattern.compile(
		    // First alternative, match quoted strings.
			"([\"'])"   // Group 1, matches start of a quote, can either be " or '
		+   "("         // Group 2, matches the rest of the quote
		+       "(\\\\{2})*"        // Group 3, matches escape characters, any \\, for 0 or more times
		+       "|"                 // Second alternative
		+       "(.*?[^\\\\]"       // Group 4, matches any character for 0 or more times, followed by any character that is not a \
		+   	    "(\\\\{2})*"    // Group 5, matches escape characters, any \\, for 0 or more times
		+       ")"
		+   ")\\1"      // Matches what group one matched
		    // Second alternative
		+   "|("        // Group 6, matches repeated whitespaces
		+   	"(?<![^\\s])"   // Negative look-behind, asserts that it is not possible to match any character that is not a blank
		+   	"\\s+"          // Match a blank, for 1 or more times
		+   	"|"             // Second alternative
		+       "\\s+$"         // Matches a blank, for 1 or more times, followed by the end of the line
		+   ")"
	);
	// Gets rid of comments (things after ';')
	private static final Pattern PATTERN_COMMENTS = Pattern.compile(";.*");
	// Matches a label
	private static final Pattern PATTERN_LABEL = Pattern.compile("^([a-zA-Z_]):\\s*");
	// Matches an instruction
	private static final Pattern PATTERN_INSTRUCTION = Pattern.compile("^((.)[A-Z.]|[a-z.])\\s*");

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

	private static String regexReplaceAll(Pattern pattern, String input, String replace) {
		return pattern.matcher(input).replaceAll(replace);
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

			line = regexReplaceAll(PATTERN_COMMENTS, line, "");

			// Matcher for repeated blanks
			Matcher matcher = PATTERN_UNNECESSARY_BLANKS.matcher(line);
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

			Matcher labelMatcher = PATTERN_LABEL.matcher(line);
			if (labelMatcher.matches()) {
				// The whole line matches, so we only have a label here
				tokens.add(new Token.Label(labelMatcher.group(1)));
				continue;
			} else if(labelMatcher.find()) {
				tokens.add(new Token.Label(labelMatcher.group(1)));
				line = line.substring(labelMatcher.end());
			}

			Matcher instructionMatcher = PATTERN_INSTRUCTION.matcher(line);
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
