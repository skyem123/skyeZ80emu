package uk.co.skyem.projects.emuZ80.cpu;

/*         _/Calls to execute Z80 instructions1<-+     +---3>this way: data comes from here>+
 * +------/-----+    calls for services          |     |    +-----------------+             |
 * |Instruction*|2-> and gets mem/io   ----------+-----+-2> |ALU :mem,flags,io| <---------+ |
 * |:splitInstr,|                      \  +------^----------+-----------------+           | |
 * |InstrDecoder|                       -->InstructionDecode>-+3> gets memoryrouters from + |
 * +------------+                         |:registers,alu,in| |                             |
 *                           />runs step.0/-+---------+-----+ |                    +--------V---+
 *                    +-----^---------------+Registers|<-r/w<3+4> memory access -> |MemoryRouter|
 *                    |Core :mem,io,regs,dec+---------+                            |:bus        |
 *                    +---------------------+                  Your IBusDevice     +------------V
 * Note: To prevent duplicate references, both the decoder and Instruction    \<getByte/putByte / 4,5
 *       both get to MemoryRouter via the ALU. So by that point, you're either at step 3 or 4.
 *       By the time you actually get to your IBusDevice you're on step 4 or 5.
 *       Also, this picture omits the fact that registers are *objects*, so they have to be read.
 *       That would be step 4.
 *       Finally, this picture is quite representative of the system in that:
 *       InstructionDecoder does everything
 *       Core is useless
 *       And your IBusDevice only sees int addresses because we *want* it to see int addresses.
 *
 * Though in actual fact the whole picture is more like:
 * +-Instruction-Safety-Tape-Space----+                +-Easy-To-Mess-Up-Code-That-Handles-IX-And-IY-+
 * |Instruction, ALU. Isn't given any |                |InstructionDecoder, Registers, Core, and also|
 * |access to Registers, since access |-> yandere for->|MemoryRouter. This code handles stuff that is|
 * |must be gatewayed due to IX/IY... |                |gatewayed for the Instructions. Apart from   |
 * |Since Memory doesn't have this    |                |InstructionDecoder, it's mostly storage space|
 * |problem, it has access to that.   |<- senpai of  <-|and gateways, which are used for IX/IY and IO|
 * |(...via MemoryRouter, which is    | (and knows it) +---------------------------------------------+
 * | yet another safety tape gateway.)|                  |                  +---------------+
 * +----------------------------------+                  +-Accomplice with->|Your IBusDevice|
 *                                                                          +---------------+
 * Hope that explained it somewhat for you.
 */

import uk.co.skyem.projects.emuZ80.util.buffer.IByteHandler;

public class Core {

	// These were protected anyway, and they're a sign-extension accident waiting to happen. --gamemanj
	// Don't touch them, btw
	private IByteHandler memoryBus;
	private IByteHandler IOBus;
	// --
	// INSTRUCTIONS ARE NOT SUPPOSED TO ACCESS THIS!!!
	// Let InstructionDecoder do it - it handles IX/IY.
	public Registers registers;
	InstructionDecoder instructionDecoder;
	ALU alu;
	// Safe to touch.
	MemoryRouter memRouter, ioRouter;

	public Core(IByteHandler memory, IByteHandler io) {
		memoryBus = memory;
		IOBus = io;

		memRouter = new MemoryRouter(memoryBus);
		ioRouter = new MemoryRouter(IOBus);

		// Yay, no more hidden nefarious dependencies or (this) references!
		registers = new Registers();
		alu = new ALU(memRouter, ioRouter, registers.flags);
		instructionDecoder = new InstructionDecoder(registers, alu);
		reset();
	}

	// Instructions should never call this!!! Use the return value instead!!!
	// I've been making it impossible for Instructions to access anything they shouldn't.
	public void jump(short address) {
		registers.programCounter.setData(address);
	}

	public void reset() {
		registers.clear();
	}

	public void step() {
		instructionDecoder.step();
	}

	public void halt() {
		halt(true);
	}

	public void halt(boolean state) {
		registers.halted = state;
	}

	public void unhalt() {
		halt(false);
	}

	public boolean halted() {
		return registers.halted;
	}

}
