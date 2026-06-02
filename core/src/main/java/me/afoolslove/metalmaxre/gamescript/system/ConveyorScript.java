package me.afoolslove.metalmaxre.gamescript.system;

import me.afoolslove.metalmaxre.gamescript.BaseSpriteScript;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.jetbrains.annotations.NotNull;

/**
 * 变更传送带的方向
 */
public class ConveyorScript extends BaseSpriteScript {
    /* 传送带停止 */
    public static final String STOP = "stop";
    /* 传送带正转 */
    public static final String FORWARD = "forward";
    /* 传送带反转 */
    public static final String BACKWARD = "backward";

    public String dir;

    public ConveyorScript(@NotNull TerminalNode dir) {
        super("6A");
        this.dir = switch (dir.getText()) {
            case FORWARD -> "01";
            case BACKWARD -> "02";
            default -> "00";
        };
    }

    public ConveyorScript(@NotNull String dir) {
        super("6A");
        this.dir = switch (dir) {
            case FORWARD -> "01";
            case BACKWARD -> "02";
            default -> "00";
        };
    }

    @Override
    public String toCode() {
        return op + dir;
    }

    @Override
    public String toScript() {
        String keyword = switch (dir) {
            case "01" -> "forward";
            case "02" -> "reverse";
            default -> "stop";
        };
        return "#conveyor " + keyword;
    }
}
