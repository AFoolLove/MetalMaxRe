package me.afoolslove.metalmaxre;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 40   战车装备   44  战车工具    48  人类武器     4C  人类工具
 * 50   战车出租   54  特种炮弹    58  旅馆        5C  酒吧
 * 60   酒吧      64  装饰店      68  点唱机      6C  道具售货机
 * 70   炮弹售货机 74  装甲片售货机 78  人类装备/道具 7C  电梯
 */
public enum InteractType {
    // -------- 商店类型
    /* 战车装备 */
    TANK_EQUIPMENT("list", 0x40),
    /* 战车根据 */
    TANK_ITEM("list", 0x44),
    /* 人类装备 */
    PLAYER_EQUIPMENT("list", 0x48),
    /* 人类工具 */
    PLAYER_ITEM("list", 0x4C),
    /* 战车出租 */
    TANK_TAX("list", 0x50),
    /* 特殊炮弹 */
    TANK_SPECIAL_SHELL("list", 0x54),
    /* 旅馆 */
    IHH("list", 0x58),
    /* 酒吧A */
    BAR_A("list", 0x5C),
    /* 酒吧B */
    BAR_B("list", 0x60),
    /* 装饰店 */
    DECORATION("list", 0x64),
    /* 点唱机 */
    JUKEBOX("list", 0x68),
    /* 道具售货机 */
    VENDING_MACHINE_ITEM("list", 0x6C),
    /* 炮弹售货机 */
    VENDING_MACHINE_SHELL("list", 0x70),
    /* 装甲片售货机 */
    VENDING_MACHINE_SP("list", 0x74),
    /* 药店 */
    PHARMACY("list", 0x78),
    /* 电梯 */
    ELEVATOR("list", 0x7C),

    // -------- 交互类型
    /* 战车补给全满 */
    TANK_SUPPLY_FULL("interact", 0x80),
    /* 老爹 */
    DADDY("interact", 0x84),
    /* 明奇博士 */
    MINCER("interact", 0x88),
    /* 战车底盘改装 */
    TANK_CHASSIS_CUSTOMIZED("interact", 0x8C),
    /* 奸商 */
    MERCHANT("interact", 0x90),
    /* 猎人事务所 */
    HUNTER_OFFICE("interact", 0x94),
    /* 猎人保管出，暂定 */
    HUNTER_WAREHOUSE("interact", 0x98),
    /* 战车底盘改装说明 */
    TANK_CHASSIS_CUSTOMIZED_DESC("interact", 0x9C),
    /* 战车引擎改装 */
    TANK_ENGINE_CUSTOMIZED("interact", 0xA0),
    /* 战车修理 */
    TANK_REPAIR("interact", 0xA4),
    /* 募捐 */
    DONATE("interact", 0xA8),
    /* 暗云博士（激光炮） */
    YAMIS("interact", 0xAC),
    /* 战车贩卖 */
    TANK_SELLER("interact", 0xB0),
    /* 传送装置 */
    TELEPORT_DEVICE("interact", 0xB4),
    /* 战车清洗 */
    TANK_CLEAN("interact", 0xB8),
    /* 按摩治疗师 */
    MASSAGE_THERAPIST("interact", 0xBC),
    /* 出租战车回收 */
    TANK_TAX_RECOVERY("interact", 0xC0),
    /* 读档 */
    READ_SAVE("interact", 0xC4),
    /* 青蛙游戏机 */
    FLOG_RACE("interact", 0xC8),
    /* 免费青蛙游戏机 */
    FLOG_RACE_FREE("interact", 0xCC),
    /* 存档小姐 */
    SAVE_MINION("interact", 0xD0),
    /* 剩余升级所需经验 */
    REMAIN_EXPERIENCE("interact", 0xD4),
    /* 控制室A */
    CONTROL_ROOM_A("interact", 0xD8),
    /* 控制室B */
    CONTROL_ROOM_B("interact", 0xDC),
    /* Noa大门密码 */
    NOA_GATE_PASSWORD("interact", 0xE0),

    ;
    public static final Map<String, EnumSet<InteractType>> TYPES = new HashMap<>();

    private final String type;

    private final int value;

    static {
        TYPES.put("list", EnumSet.noneOf(InteractType.class));
        TYPES.put("interact", EnumSet.noneOf(InteractType.class));

        for (InteractType interactType : values()) {
            TYPES.get(interactType.type).add(interactType);
        }
    }

    InteractType(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EnumSet<InteractType> getListTypes() {
        return TYPES.get("list");
    }

    public static EnumSet<InteractType> getInteractTypes() {
        return TYPES.get("interact");
    }
}
