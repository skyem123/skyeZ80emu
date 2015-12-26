package uk.co.skyem.projects.emuZ80.asm;

import uk.co.skyem.projects.emuZ80.cpu.Register;
import uk.co.skyem.projects.emuZ80.cpu.Registers;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;

public class Arguments {

	private ArrayList<Map.Entry<ArgumentType, Object>> arguments = new ArrayList<>();

	private Arguments() {

	}

	@SuppressWarnings({"unchecked"})
	public <T> T get(int index, ArgumentType<T> type) {
		return (T) arguments.get(index).getValue();
	}

	public int size() {
		return arguments.size();
	}

	public boolean isPresent(int index) {
		return index > 0 && index < arguments.size();
	}

	public ArgumentType<?> getType(int index) {
		return arguments.get(index).getKey();
	}

	public boolean isType(int index, ArgumentType<?> type) {
		return getType(index) == type;
	}

	private <T> void add(ArgumentType<T> type, T object) {
		arguments.add(new AbstractMap.SimpleImmutableEntry<>(type, object));
	}

	public static class ArgumentType<T> {

		private ArgumentType() {

		}

		// Argument types
		public static final ArgumentType<Reg> REGISTER = new ArgumentType<>();
		public static final ArgumentType<Token.Label> LABEL = new ArgumentType<>();
		public static final ArgumentType<Integer> NUMBER = new ArgumentType<>();
		public static final ArgumentType<Reg> POINTER_REGISTER = new ArgumentType<>();
		public static final ArgumentType<Token.Label> POINTER_LABEL = new ArgumentType<>();
		public static final ArgumentType<Integer> POINTER_NUMBER = new ArgumentType<>();
		public static final ArgumentType<String> STRING = new ArgumentType<>();
	}

	public static enum Reg {
		PC, I, R, IX, IY, SP, F, A, B, C, D, E, H, L, AF, BC, DE;

		public Register get(Registers registers) {
			return registers.getRegister(ordinal());
		}
	}

	public static Arguments parse(String args, int line) throws AssemblerException {
		Arguments arguments = new Arguments();
		if (args.length() == 0)
			return arguments;

		Matcher matcher = Patterns.ARGUMENTS.matcher(args);
		int start = 0;
		if (matcher.find()) {
			do {
				// If we could find a group 6 (which is the comma)
				if (matcher.group(6) != null) {
					int pos = matcher.start(6);
					// Get the argument (and trim excess spaces)
					parseArgument(arguments, Patterns.regexRemoveAll(Patterns.TRIM, args.substring(start, pos)), line);
					start = pos;
				}
			} while (matcher.find());
		} else {
			parseArgument(arguments, Patterns.regexRemoveAll(Patterns.TRIM, args), line);
		}
		return arguments;
	}

	private static void parseArgument(Arguments arguments, String arg, int line) throws AssemblerException {

		boolean brackets = false;
		String s = arg;
		if ((s = validateWrapperChars(arg, line, "\"\"", "''")) != null) {
			arguments.add(ArgumentType.STRING, s);
			return;
		} else if ((s = validateWrapperChars(arg, line, "()")) != null) {
			brackets = true;
		}

		try {
			arguments.add(brackets ? ArgumentType.POINTER_REGISTER : ArgumentType.REGISTER, Reg.valueOf(s));
		} catch (IllegalArgumentException e) {
			// 0x0000 hexadecimal
			// $0000 hexadecial
			// 0000h hexadecial
			// 0000 decimal
			Matcher matcher = Patterns.NUMBER.matcher(s);
			if (matcher.find()) {
				String match = matcher.group(2);
				int number = Integer.parseInt(match, matcher.groupCount() > 1 ? 16 : 10);
				arguments.add(brackets ? ArgumentType.POINTER_NUMBER : ArgumentType.NUMBER, number);
			} else {

			}
		}
	}

	private static String validateWrapperChars(String arg, int line, String... chars) throws AssemblerException {
		for (String wrapper : chars) {
			if (wrapper.length() == 2)
				throw new IllegalArgumentException("Need to specify a pair of wrapper characters!");
			if (arg.charAt(0) == wrapper.charAt(0)) {
				if (arg.charAt(wrapper.length()) == wrapper.charAt(1)) {
					return arg.substring(1, arg.length() - 1);
				}
			} else {
				throw new AssemblerException(line, arg, "Not pair \"" + wrapper + "\"");
			}
		}
		return null;
	}
}
