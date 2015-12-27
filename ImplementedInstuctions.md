Implemented Instructions
========================

##CPU##
The emulated Z80 CPU supports these instructions:
(List may be incorrect, but I really hope not.)
(List is not finished.)
(S-A-D: Set as defined)
(Test: OK = "this is tested", OKIX = "this is tested with the IX prefix too",
       MOST = "mostly tested - enough that the instruction logic's fine,
               but certain cases that would require their own tests are not checked",
       MSIX = "MOST, where tested, done with IX"
|Test|Opcode|Mnemonic  |What it does                                                          |Effect on Flags
|----|------|----------|----------------------------------------------------------------------|---------------------------------------------------------------------------------------
|OKIX|      |INC rp    |Increment (add one) to register pair                                  |No effect.
|OKIX|      |DEC rp    |Decrement (subtract one) from register pair                           |No effect.
| OK | 0x00 |NOP       |Do Nothing.                                                           |No effect.
|OKIX|      |INC r     |Increment register/(HL)                                               |Parity/Overflow is set for the "overflow" condition. Sub/HalfC/Zero/Sign are S-A-D
|OKIX|      |DEC r     |Decrement register/(HL)                                               |See INC r
|OKIX|      |LD r, n   |Put fixed 8 bit value n into register/(HL).                           |No effect.
| OK | 0x76 |HALT      |Halts the CPU until an interrupt or reset                             |No effect.
| OK |      |JP c, nn  |Conditional jump, setting PC to nn if the condition is true.          |No effect.
| OK | 0xC3 |JP nn     |Unconditional jump, setting PC to nn.                                 |No effect.
|MSIX|      |LD r, r   |Load LHS register/(HL) with RHS register/(HL):LD (HL),(HL) is HALT.   |No effect.
| OK | 0x18 |JR d      |Relative unconditional jump with signed offset byte d.                |No effect.
| OK |      |JR c, d   |Relative conditional jump with signed offset byte offset d.           |No effect.
|    | 0x01 |LD BC,nn  |Put fixed 16 bit value nn into register BC.                           |No effect.
|    | 0x02 |LD (BC), A|Put the data in register A into the memory address specified in BC.   |No effect.
|    | 0x07 |RLCA      |Rotate accumulator (register A) one bit left.                         |The MSB is copied into the the carry flag. Resets the subtraction and half-carry flags.
|    | 0x08 |EX AF, AF'|Swaps AF out with its shadow, AF'.                                    |The flags are swapped with the shadow flags.
|    | 0x0F |RRCA      |Rotate accumulator (register A) one bit left.                         |The LSB is copied into the the carry flag. Resets the subtraction and half-carry flags.
|    | 0x10 |DJNZ d    |Decrement B by one, then relative jump with offset d if B is not zero.|No effect.
|    | 0x17 |RLA       |Rotate accumulator (register A) one bit left trough carry.            |The carry flag is used as an "extra bit". Resets the subtraction and half-carry flags.
|    | 0x1F |RRA       |Rotate accumulator (register A) one bit right trough carry.           |The carry flag is used as an "extra bit". Resets the subtraction and half-carry flags.
|    | 0x27 |DAA       |Changes accumulator after an operation with BCD input, to make it BCD.|The addition / subtraction flag is not affected, others are.
|    | 0x2F |CPL       |Inverts all the bits in the accumulator.                              |The addition / subtraction flag and the half carry flags are set.
|    | 0x37 |SCF       |Sets the carry flag.                                                  |Carry flag is set. Addition / subtraction flag is reset. Half-carry flag is reset.
|    | 0x3F |CCF       |Inverts the carry flag.                                               |Carry is inverted. Addition / subtraction is reset. Half-carry is old carry.

adc *might* work, it's basically add but with the carry flag, and that should be working.
Not completely tested though - only tested: adc A, (HL)

##Assembler##
