package uk.co.skyem.projects.Z80emu.asm;

import java.util.regex.Pattern;

public class Patterns {

	private Patterns () {
	}

	// Our regex patterns
	// Gets rid of all unnecessary blanks
	public static final Pattern UNNECESSARY_BLANKS = Pattern.compile(
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
	public static final Pattern COMMENTS = Pattern.compile(";.*");
	// Matches a label
	public static final Pattern LABEL = Pattern.compile("^([a-zA-Z_]):\\s*");
	// Matches an instruction
	public static final Pattern INSTRUCTION = Pattern.compile("^((.)[A-Z.]|[a-z.])\\s*");

	public static String regexReplaceAll(Pattern pattern, String input, String replace) {
		return pattern.matcher(input).replaceAll(replace);
	}
}
