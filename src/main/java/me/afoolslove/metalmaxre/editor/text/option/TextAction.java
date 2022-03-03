package me.afoolslove.metalmaxre.editor.text.option;

import me.afoolslove.metalmaxre.editor.player.Player;

public enum TextAction {
    CURRENT_PLAYER_NAME(bytes((byte) 0xE8)),
    PLAYER_NAME_HANTA(bytes((byte) 0xFD, Player.HANTA.getId())),
    PLAYER_NAME_TAMPER(bytes((byte) 0xFD, Player.TAMPER.getId())),
    PLAYER_NAME_ANNE(bytes((byte) 0xFD, Player.ANNE.getId())),

    NEW_LINE(bytes((byte) 0xE5)),

    WAIT_FOR_KEY(bytes((byte) 0xE4)),
    WAIT_FOR_KEY_AND_NEW_LINE_NAME(bytes((byte) 0xFE));

    private final byte[] value;

    TextAction(byte[] value) {
        this.value = value;
    }

    public byte[] toByteArray() {
        return value;
    }


    private static byte[] bytes(byte... bytes) {
        return bytes;
    }
}
