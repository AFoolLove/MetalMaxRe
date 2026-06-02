package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;

public class SystemScript extends BaseSpriteScript {
    public byte commandCode;

    public SystemScript(TerminalNode commandCode) {
        super("6B");
        this.commandCode = byteVal(commandCode);
    }

    public SystemScript(int commandCode) {
        super("6B");
        this.commandCode = (byte) commandCode;
    }

    public SystemScript(String commandCode) {
        super("6B");
        this.commandCode = switch (commandCode) {
            case "restart" -> 0x00;
            case "battle" -> 0x02;
            case "reset map" -> 0x03;
            case "reset map fast" -> 0x04;
            case "sleep" -> 0x05;
            case "tank rename" -> 0x06;
            default -> 0x01;
        };
    }

    @Override
    public String toCode() {
        return op + String.format("%02X", commandCode);
    }

    @Override
    public String toScript() {
        return String.format("#system 0x%02X", commandCode);
    }
}
