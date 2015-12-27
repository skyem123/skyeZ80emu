package uk.co.skyem.projects.emuZ80.asm;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import uk.co.skyem.projects.emuZ80.asm.AssemblerException.InvalidASMDirectiveException;
import uk.co.skyem.projects.emuZ80.asm.AssemblerException.InvalidInstructionException;
import uk.co.skyem.projects.emuZ80.asm.Token.*;
import uk.co.skyem.projects.emuZ80.asm.Token.Instruction;
import uk.co.skyem.projects.emuZ80.util.InternalException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;

import org.apache.commons.collections4.map.MultiKeyMap;

public class Assembler {

	private static final HashMap<String, Supplier<Instruction>> instructions = new HashMap<>();
	private static final HashMap<String, Supplier<ASMDirective>> asmDirectives = new HashMap<>();

	static {
		registerInstruction("LD", LD::new);
		registerInstruction("NOP", NOP::new);
	}

	private String source;
	private short origin;
	private int lineNumber;
	private int instructionPosition;
	private String line;
	private Program program;

	private List<Instruction> instructionList = new ArrayList<>();
	private MultiKeyMap<Object, Label> labelMap = new MultiKeyMap<>();
	private List<Pair<CPUInstruction, Label>> labelsToInsert = new ArrayList<>();

	public Assembler(String source) {
		this.source = source;
	}

	private static void registerASMDirective(String name, Supplier<ASMDirective> supplier) {
		asmDirectives.put(name, supplier);
		registerInstruction(name, supplier::get);
	}

	private static void registerInstruction(String name, Supplier<Instruction> supplier) {
		instructions.put(name, supplier);
	}

	private Instruction getInstruction(String name, String arguments) throws InvalidInstructionException {
		name = name.toUpperCase();
		if (instructions.containsKey(name)) {
			try {
				Instruction instruction = instructions.get(name).get();
				instruction.create(this, Arguments.parse(arguments, lineNumber));
				return instruction;
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

	private void addLabel(Label label, int reference) {
		labelMap.put(label.name, reference, label);
	}

	public void insertLabel(Label label, int position) {
		//Instruction in
		//labelsToInsert.add(new ImmutablePair<>(, label));
	}

	public Optional<Label> getLabel(String name) {
		return Optional.ofNullable(labelMap.get(name));
	}

	protected int getLineNumber() {
		return lineNumber;
	}

	protected Program getProgram() {
		return program;
	}

	/**
	 * Assemble Z80 asm to Z80 machine code.
	 *
	 * @throws AssemblerException
	 */
	public void assemble() throws AssemblerException {
		lineNumber = origin = 0;
		labelMap.clear();
		labelsToInsert.clear();

		String[] preparsed = preparse();
		tokenize(preparsed);
		write();
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

			if (line.length() == 0)
				line = null;
			splitted[i] = line;
		}
		return splitted;
	}

	// Takes pre-parsed code and spits out tokens.
	public void tokenize(String[] input) throws AssemblerException {
		instructionList.clear();
		// Turn ALL tokens to a token array

		int instructionNumber = 0;
		for (lineNumber = 0; lineNumber < input.length; lineNumber++) {
			line = input[lineNumber];
			if (line == null)
				continue;

			Matcher labelMatcher = Patterns.LABEL.matcher(line);
			if (labelMatcher.matches()) {
				// The whole line matches, so we only have a label here
				addLabel(new Label(labelMatcher.group(1)), instructionNumber);
				continue;
			} else {
				labelMatcher.reset();
				if (labelMatcher.find()) {
					addLabel(new Label(labelMatcher.group(1)), instructionNumber);
					line = line.substring(labelMatcher.end());
				}
			}

			Matcher instructionMatcher = Patterns.INSTRUCTION.matcher(line);
			if (instructionMatcher.find()) {
				if (instructionMatcher.group(1) != null) {
					// We have an asm directive because it matches the starting dot
					instructionList.add(getASMDirective(instructionMatcher.group(2), line.substring(instructionMatcher.end())));
				} else {
					// We have an unspecified instruction
					instructionList.add(getInstruction(instructionMatcher.group(2), line.substring(instructionMatcher.end())));
				}
			} else {
				throw new AssemblerException.SyntaxException(lineNumber, line);
			}
			++instructionNumber;
		}
	}

	public void write() {
		program = new Program(origin);
		for (instructionPosition = 0; instructionPosition < instructionList.size(); instructionPosition++) {
			Label label = labelMap.get(instructionPosition);

			if (label != null) {
				label.position = program.getSize();
			}

			Instruction instruction = instructionList.get(instructionPosition);
			instruction.insert(program, program.getSize());
		}
	}
}
