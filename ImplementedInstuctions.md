Implemented Instructions
========================

##CPU##
The emulated Z80 CPU supports these instructions:
(List is not finished.)

|Opcode|Memonic   |What it does                                                          |Affect on Flags
|------|----------|----------------------------------------------------------------------|---------------
| 0x00 |nop       |Do Nothing.                                                           |               
| 0x01 |ld BC,nn  |Put fixed 16 bit value nn into register BC.                           |
| 0x02 |ld (BC),A |Put the data in register A into the memory address specified in BC.   |
| 0x03 |inc BC    |Increment (add one) to BC                                             |
| 0x08 |ex AF, AF'|Swaps AF out with its shadow, AF'.                                    |
| 0x10 |DJNZ d    |Decrement B by one, then relative jump with offset d if B is not zero.|
| 0x18 |jr d      |Relative jump with offset d.                                          |

##Assembler##
