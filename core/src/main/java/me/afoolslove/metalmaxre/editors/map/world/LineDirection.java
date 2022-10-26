package me.afoolslove.metalmaxre.editors.map.world;

/**
 * 航线方向
 */
public enum LineDirection {
    UP((byte) 0x50),
    DOWN((byte) 0x51),
    LEFT((byte) 0x52),
    RIGHT((byte) 0x53),
    END((byte) 0x5E);

    private final byte action;

    LineDirection(byte action) {
        this.action = action;
    }

    /**
     * @return 航线对应的代码
     */
    public byte getAction() {
        return action;
    }

    /**
     * @return 通过代码得到实例
     */
    public static LineDirection fromAction(byte action) {
        for (LineDirection value : values()) {
            if (value.action == action) {
                return value;
            }
        }
        return null;
    }
}
