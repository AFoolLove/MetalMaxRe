;       �ó���Ϊ����ROM����������
;       ��֮��Ӧ�Ľ���������Ҫ�����ڶ�Ӧ��Bank�У�������������
;
;       ���ܣ�����ͨ�� (0x0C)12�ֽ� ���ܽ���������״̬�������ת�����ݵ�ROM�м���ִ��

;       ע������ͨ��ջʵ�֣���ִ�е����ݵ�ROM��ʱ��ջΪ


;       JSR $D3C3   �л� $0000-$07FF PPU��#$80 �� �Ĵ��� X
;       JSR $D3CC   �л� $0800-$0FFF PPU��#$81 �� �Ĵ��� X
;       JSR $D3D5   �л� $1000-$13FF PPU��#$82 �� �Ĵ��� X
;       JSR $D3DE   �л� $1400-$17FF PPU��#$83 �� �Ĵ��� X
;       JSR $D3E7   �л� $1800-$1BFF PPU��#$84 �� �Ĵ��� X
;       JSR $D3F0   �л� $1C00-$1FFF PPU��#$85 �� �Ĵ��� X
;       #$86��#$87 �Ƚ�����
;       JSR $D3F9   �л� $8000-$9FFF ����#$86 �� �Ĵ��� X
;       JSR $D414   �л� $A000-$BFFF ����#$87 �� �Ĵ��� X

MainPageNumber= $30
        ; CF7A = 0FCF8A
        ; CF89 = 0FCF99
        .ORG $CF7A      ;����ROM���ÿռ� 70BYTE
        ;ֻ�� JMP ��ת������ڣ������Ƿ� JMP ��������
        ;��תǰ"PHP"������ѹջ "JSR"��ѹջ2��PROM��ַ

ResetBank:  ; �ָ�PROM��ҳ 8000-9FFF
        JSR $D3F9
        PLA
        TAX
        PLA
        PLP
        RTS

SwitchBank: ; ��ҳ����ռ�ã�(0x0C)12�ֽ�
        ; ʹ�� JSR SwitchBank �������
        ; �Ĵ��� A��B ��Ϊ��ִ�еĳ�����ڵ�ַ���ֱ�Ϊ ��λ����λ
        ; �Ĵ��� Y ��ΪҪ�л��ĵ�ַ

        ; e.g:
        ; JSR SwitchBank
        ; LDA #$12
        ; LDY #$23
        ; LDX #$41
        ;|JSR SwitchBank2   ; �˷������ת�Ƶ� $1223
        ;-RTS			    ; �˷������ת�Ƶ� $1223
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
        ADC #$FF    ; ���� FF ��������һ
SwitchBank2_0:
        DEY
        PHA
        TYA
        PHA
        JMP $D3F9
