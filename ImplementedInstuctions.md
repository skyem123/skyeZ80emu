Implemented Instructions
========================

##CPU##
The emulated Z80 CPU supports these instructions:
(List may be incorrect.)
(List is not finished.)

|Opcode|Memonic   |What it does                                                          |Effect on Flags
|------|----------|----------------------------------------------------------------------|------------------------------------------
| 0x00 |nop       |Do Nothing.                                                           |No effect.
| 0x01 |ld BC,nn  |Put fixed 16 bit value nn into register BC.                           |No effect.
| 0x02 |ld (BC),A |Put the data in register A into the memory address specified in BC.   |No effect.
| 0x03 |inc BC    |Increment (add one) to BC                                             |No effect.
| ~~0x07~~ |~~rlca~~      |~~Rotate accumulator (register A) one bit left.~~                         |~~The MSB is copied into the the carry flag.~~
| 0x08 |ex AF, AF'|Swaps AF out with its shadow, AF'.                                    |No effect.
| ~~0x0~~F |~~rrca~~      |~~Rotate accumulator (register A) one bit left.~~                         |~~The LSB is copied into the the carry flag.~~
| 0x10 |DJNZ d    |Decrement B by one, then relative jump with offset d if B is not zero.|No effect.
| 0x18 |jr d      |Relative jump with offset d.                                          |No effect.

##Assembler##
