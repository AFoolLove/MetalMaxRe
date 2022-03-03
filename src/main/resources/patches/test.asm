        BASE $CF89
        LDA #$FF
        JMP test
test:
        RTS
        BASE $8000
        LDX #$EF
        RTS
