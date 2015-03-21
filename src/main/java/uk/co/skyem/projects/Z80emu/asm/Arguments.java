package uk.co.skyem.projects.Z80emu.asm;

import uk.co.skyem.projects.Z80emu.Register;
import uk.co.skyem.projects.Z80emu.Registers;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		public static final ArgumentType<Reg>           REGISTER            = new ArgumentType<>();
		public static final ArgumentType<Token.Label>   LABEL               = new ArgumentType<>();
		public static final ArgumentType<Integer>       NUMBER              = new ArgumentType<>();
		public static final ArgumentType<Reg>           POINTER_REGISTER    = new ArgumentType<>();
		public static final ArgumentType<Token.Label>   POINTER_LABEL       = new ArgumentType<>();
		public static final ArgumentType<Integer>       POINTER_NUMBER      = new ArgumentType<>();
		public static final ArgumentType<String>        STRING              = new ArgumentType<>();
	}

	public static enum Reg {
		PC, I, R, IX, IY, SP, F, A, B, C, D, E, H, L, AF, BC, DE;

		public Register get(Registers registers) {
			return registers.getRegister(ordinal());
		}
	}

	public static Arguments parse(String args) {
		Arguments arguments = new Arguments();
		for (String argument : args.split("\\s+")) {
			try {
				arguments.add(ArgumentType.REGISTER, Reg.valueOf(argument));
			} catch (IllegalArgumentException e) {
				// Parse everything else...
			}
		}
		return arguments;
	}
}
