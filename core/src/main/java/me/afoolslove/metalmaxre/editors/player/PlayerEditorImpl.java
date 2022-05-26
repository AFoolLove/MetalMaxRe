package me.afoolslove.metalmaxre.editors.player;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

/**
 * 玩家编辑器
 * 包括不限于玩家初始属性
 * 目前只能修改初始数据
 *
 * @author AFoolLove
 */
public class PlayerEditorImpl extends RomBufferWrapperAbstractEditor implements IPlayerEditor {
    private final DataAddress playerAddress;
    /**
     * 初始属性
     */
    private final EnumMap<Player, PlayerInitialAttributes> initialAttributes = new EnumMap<>(Player.class);

    /**
     * 玩家的初始金钱（3byte）
     */
    public int money;

    public PlayerEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x280CD - 0x10, 0x02813F - 0x10));
    }

    public PlayerEditorImpl(@NotNull MetalMaxRe metalMaxRe, @NotNull DataAddress playerAddress) {
        super(metalMaxRe);
        this.playerAddress = playerAddress;
    }

    @Editor.Load
    public void onLoad(@NotNull ByteBuffer buffer) {
        // 读取前清空数据
        initialAttributes.clear();

        // 读取初始属性
        PlayerInitialAttributes[] playerInitialAttributes = new PlayerInitialAttributes[Player.values().length];
        for (Player player : Player.values()) {
            playerInitialAttributes[player.getId()] = new PlayerInitialAttributes();
            initialAttributes.put(player, playerInitialAttributes[player.getId()]);
        }

        // 从初始金钱开始读取
        position(getPlayerAddress());
        money = NumberR.toInt(getBuffer().get(), getBuffer().get(), getBuffer().get());

        // 读取初始最大生命值
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setMaxHealth(NumberR.toInt(getBuffer().get(), getBuffer().get()));
        }
        // 读取初始当前生命值
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setHealth(NumberR.toInt(getBuffer().get(), getBuffer().get()));
        }
        // 读取初始攻击力（含已装备的武器
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setAttack(NumberR.toInt(getBuffer().get(), getBuffer().get()));
        }
        // 读取初始防御力（含已装备的防具
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setDefense(NumberR.toInt(getBuffer().get(), getBuffer().get()));
        }
        // 读取初始队伍状态
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setTeamStatus(getBuffer().get());
        }
        // 读取初始异常状态
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setDeBuff(getBuffer().get());
        }
        // 读取初始等级
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setLevel(getBuffer().get());
        }
        // 读取初始力量
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setStrength(getBuffer().get());
        }
        // 读取初始智力
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setWisdom(getBuffer().get());
        }
        // 读取初始速度
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setSpeed(getBuffer().get());
        }
        // 读取初始体力
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setVitality(getBuffer().get());
        }
        // 读取初始战斗等级
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setBattleSkill(getBuffer().get());
        }
        // 读取初始修理等级
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setRepairSkill(getBuffer().get());
        }
        // 读取初始驾驶等级
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setDrivingSkill(getBuffer().get());
        }

        byte[] temp = new byte[0x08];
        // 读取初始装备
        for (int i = 0; i < 0x03; i++) {
            getBuffer().get(temp);
            playerInitialAttributes[i].setEquipment(Arrays.copyOf(temp, temp.length));
        }
        // 读取初始道具
        for (int i = 0; i < 0x03; i++) {
            getBuffer().get(temp);
            playerInitialAttributes[i].setInventory(Arrays.copyOf(temp, temp.length));
        }
        // 读取初始装备的装备/卸下状态
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setEquipmentState(getBuffer().get());
        }
        // 读取初始经验值
        for (int i = 0; i < 0x03; i++) {
            playerInitialAttributes[i].setExperience(NumberR.toInt(getBuffer().get(), getBuffer().get(), getBuffer().get()));
        }
    }

    @Editor.Apply
    public void onApply() {
        // 写入初始属性

        // 转换为数组
        PlayerInitialAttributes[] playerInitialAttributes = new PlayerInitialAttributes[Player.values().length];
        for (Player player : Player.values()) {
            playerInitialAttributes[player.getId()] = getInitialAttributes(player);
        }

        // 从初始金钱开始写入
        position(getPlayerAddress());
        getBuffer().put(getMoneyByteArray());
        // 写入初始最大生命值
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getBytesMaxHealth());
        }
        // 写入初始当前生命值
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getBytesHealth());
        }
        // 写入初始攻击力（含已装备的武器
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getBytesAttack());
        }
        // 写入初始防御力（含已装备的防具
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getBytesDefense());
        }
        // 写入初始队伍状态
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getTeamStatus());
        }
        // 写入初始异常状态
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getDeBuff());
        }
        //  写入初始等级
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getLevel());
        }
        // 写入初始力量
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getStrength());
        }
        // 写入初始智力
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getWisdom());
        }
        // 写入初始速度
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getSpeed());
        }
        // 写入初始体力
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getVitality());
        }
        // 写入初始战斗等级
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getBattleSkill());
        }
        // 写入初始修理等级
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getRepairSkill());
        }
        // 写入初始驾驶等级
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getDrivingSkill());
        }
        // 写入初始装备
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getEquipment());
        }
        // 写入初始道具
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getInventory());
        }
        // 写入初始装备的装备状态
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getEquipmentState());
        }
        // 写入初始经验值
        for (int i = 0; i < 0x03; i++) {
            getBuffer().put(playerInitialAttributes[i].getBytesExperience());
        }
    }

    @Override
    public DataAddress getPlayerAddress() {
        return playerAddress;
    }

    @Override
    public Map<Player, PlayerInitialAttributes> getInitialAttributes() {
        return initialAttributes;
    }

    @Override
    public PlayerInitialAttributes getInitialAttributes(Player player) {
        return initialAttributes.get(player);
    }

    @Override
    public void setMoney(@Range(from = 0x00, to = 0xFFFFFF) int money) {
        this.money = money;
    }

    @Range(from = 0x00, to = 0xFFFFFF)
    @Override
    public int getMoney() {
        return money;
    }

    /**
     * @return 数组形式的金钱
     */
    public byte[] getMoneyByteArray() {
        return NumberR.toByteArray(money, 3, false);
    }
}
