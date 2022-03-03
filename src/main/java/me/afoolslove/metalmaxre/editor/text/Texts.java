package me.afoolslove.metalmaxre.editor.text;

public class Texts {
    public static IBaseText yesOrNo(byte yes) {
        return new BaseTextImpl("yes_or_no", 2, (byte) 0xE3, yes)
                .priority(Byte.MAX_VALUE);
    }

    public static IBaseText yesAndNo(byte yes, byte no) {
        return new BaseTextImpl("yes_or_no", 3, (byte) 0xE8, yes, no)
                .priority(Byte.MAX_VALUE);
    }

    public static IBaseText textOffset(byte offset) {
        return new BaseTextImpl("text_offset", 2, (byte) 0xED, offset);
    }

    public static IBaseText[] textOffsets(int offset) {
        var size = offset / 0x100; // 0xEDFF 的数量
        var rem = offset % 0x100; // 余数
        IBaseText[] texts = new IBaseText[Math.max(1, size + (rem == 0 ? 0 : 1))];


    }
}
