package uk.co.skyem.projects.Z80emu.instructionGroups;

import uk.co.skyem.projects.Z80emu.InstructionDecoder;
import uk.co.skyem.projects.Z80emu.instructionGroups.unprefixed.MiscInstruction;

import java.util.ArrayList;
import java.util.List;

public class InstructionGroups extends Instruction{
	//private static List<Class<? extends Instruction>> unprefixedClass = new ArrayList<>();
	// private static List<Instruction> unprefixed = new ArrayList<>();

	static {
		//unprefixedClass.add(MiscInstruction.class);
	}

	MiscInstruction miscInstruction = new MiscInstruction(instructionDecoder);

	public InstructionGroups(InstructionDecoder instructionDecoder) {
		super(instructionDecoder);
		//for (Class<? extends Instruction> instructionClass : unprefixedClass) {
		//	try {unprefixed.add(instructionClass.getConstructor(InstructionDecoder.class).newInstance(instructionDecoder));}
		//	catch (ReflectiveOperationException e) {throw new RuntimeException(e);}
		//}
	}

	@Override
	public void runOpcode(InstructionDecoder.SplitInstruction splitInstruction) {
		switch (splitInstruction.prefix) {
			case 0:
				miscInstruction.runOpcode(splitInstruction);
		}
	}
}
