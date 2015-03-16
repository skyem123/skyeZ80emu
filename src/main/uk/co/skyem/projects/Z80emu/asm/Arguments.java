package uk.co.skyem.projects.Z80emu.asm;

import sun.dc.pr.PRError;
import uk.co.skyem.projects.Z80emu.Register;
import uk.co.skyem.projects.Z80emu.Registers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Arguments {

	private Arguments() {

	}

	public static enum Reg {
		PC, I, R, IX, IY, SP, F, A, B, C, D, E, H, L, AF, BC, DE;

		public Register get(Registers registers) {
			return registers.getRegister(ordinal());
		}
	}

	public static class RegEntry {
		public RegEntry(Reg register, boolean pointer) {

		}
	}

	public static Object[] parse(String arguments) {
		List<Object> arglist = new ArrayList<>();
		for (String argument : arguments.split("\\s+")) {
			try {
				arglist.add(Reg.valueOf(argument));
			} catch (IllegalArgumentException e) {
				// Parse everything else...
			}
		}
		return arglist.toArray();
	}

	public static boolean matches(Class... clazz) {
		return false;
	}
}
