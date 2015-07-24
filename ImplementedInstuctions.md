Implemented Instructions
========================

##CPU##
The emulated Z80 CPU supports these instructions:
(List may be incorrect, but I really hope not.)
(List is not finished.)

|Opcode|Memonic   |What it does                                                          |Effect on Flags
|------|----------|----------------------------------------------------------------------|---------------------------------------------------------------------------------------
| 0x00 |nop       |Do Nothing.                                                           |No effect.
| 0x01 |ld BC,nn  |Put fixed 16 bit value nn into register BC.                           |No effect.
| 0x02 |ld (BC),A |Put the data in register A into the memory address specified in BC.   |No effect.
| 0x03 |inc BC    |Increment (add one) to BC                                             |No effect.
| 0x07 |rlca      |Rotate accumulator (register A) one bit left.                         |The MSB is copied into the the carry flag. Resets the subtraction and half-carry flags.
| 0x08 |ex AF, AF'|Swaps AF out with its shadow, AF'.                                    |The flags are swapped with the shadow flags.
| 0x0F |rrca      |Rotate accumulator (register A) one bit left.                         |The LSB is copied into the the carry flag. Resets the subtraction and half-carry flags.
| 0x10 |djnz d    |Decrement B by one, then relative jump with offset d if B is not zero.|No effect.
| 0x17 |rla       |Rotate accumulator (register A) one bit left trough carry.            |The carry flag is used as an "extra bit". Resets the subtraction and half-carry flags.
| 0x18 |jr d      |Relative jump with offset d.                                          |No effect.
| 0x1F |rra       |Rotate accumulator (register A) one bit right trough carry.           |The carry flag is used as an "extra bit". Resets the subtraction and half-carry flags.
| 0x27 |daa       |Changes accumulator after an operation with BCD input, to make it BCD.|The addition / subtraction flag is not affected, others are.
| 0x2F |cpl       |Inverts all the bits in the accumulator.                              |The addition / subtraction flag and the half carry flags are set.
| 0x37 |scf       |Sets the carry flag.                                                  |Carry flag is set. Addition / subtraction flag is reset. Half-carry flag is reset.
| 0x3F |ccf       |Inverts the carry flag.                                               |Carry is inverted. Addition / subtraction is reset. Half-carry is old carry.

##Assembler##
