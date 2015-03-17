Implemented Instructions
========================

##CPU##
The emulated Z80 CPU supports these instructions:

|Opcode|Memonic  |What it does                                                       |
|------|---------|-------------------------------------------------------------------|
| 0x00 |   NOP   |Nothing.                                                           |
| 0x01 |ld BC,nn |Put fixed 16 bit value nn into register BC.                        |
| 0x02 |ld (BC),A|Put the data in register A into the memory address specified in BC.|

##Assembler##
