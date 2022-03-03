;       该程序为扩容ROM的引导程序
;       与之对应的解析程序需要放置在对应的Bank中，才能正常运行
;
;       功能：可以通过 (0x0C)12字节 就能将程序数据状态无损的跳转到扩容的ROM中继续执行

;       注：功能通过栈实现，但执行到扩容的ROM中时，栈为


;       JSR $D3C3   切换 $0000-$07FF PPU，#$80 和 寄存器 X
;       JSR $D3CC   切换 $0800-$0FFF PPU，#$81 和 寄存器 X
;       JSR $D3D5   切换 $1000-$13FF PPU，#$82 和 寄存器 X
;       JSR $D3DE   切换 $1400-$17FF PPU，#$83 和 寄存器 X
;       JSR $D3E7   切换 $1800-$1BFF PPU，#$84 和 寄存器 X
;       JSR $D3F0   切换 $1C00-$1FFF PPU，#$85 和 寄存器 X
;       #$86和#$87 比较特殊
;       JSR $D3F9   切换 $8000-$9FFF 程序，#$86 和 寄存器 X
;       JSR $D414   切换 $A000-$BFFF 程序，#$87 和 寄存器 X

MainPageNumber= $30
        ; CF7A = 0FCF8A
        ; CF89 = 0FCF99
        .ORG $CF7A      ;中文ROM可用空间 70BYTE
        ;只能 JMP 跳转到此入口，不考虑非 JMP 进入此入口
        ;跳转前"PHP"，主动压栈 "JSR"，压栈2个PROM地址

ResetBank:  ; 恢复PROM切页 8000-9FFF
        JSR $D3F9
        PLA
        TAX
        PLA
        PLP
        RTS

SwitchBank: ; 切页程序，占用：(0x0C)12字节
        ; 使用 JSR SwitchBank 命令进入
        ; 寄存器 A、B 作为被执行的程序入口地址，分别为 高位、低位
        ; 寄存器 Y 作为要切换的地址

        ; e.g:
        ; JSR SwitchBank
        ; LDA #$12
        ; LDY #$23
        ; LDX #$41
        ;|JSR SwitchBank2   ; 此返回命令将转移到 $1223
        ;-RTS			    ; 此返回命令将转移到 $1223
        ; S: $20 Y X A P (XX XX)-

        ; S:    (| |) (XX XX)-
        PHP
        PHP

        PHP
        PHA
        TXA
        PHA
        TYA
        PHA
        LDA $0020
        PHA
        ; S: $20 Y X A P (| |) (XX XX)-
        PHA
        PHA
        ; S: (- -) $20 Y X A P (| |) (XX XX)-
        LDX #MainPageNumber
        JSR $D3F9
        JSR $8000
        ; S: (XX XX) $20 Y X A P (| |) (XX XX)-
        RTS
SwitchBank2_x41:
        LDX #MainPageNumber
SwitchBank2:
        CPY #$00
        BNE SwitchBank2_0
        CLC
        ADC #$FF    ; 增加 FF 来做到减一
SwitchBank2_0:
        DEY
        PHA
        TYA
        PHA
        JMP $D3F9
