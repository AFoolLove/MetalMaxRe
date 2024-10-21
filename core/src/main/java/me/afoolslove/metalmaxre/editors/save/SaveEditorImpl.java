package me.afoolslove.metalmaxre.editors.save;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.monster.MonsterType;
import me.afoolslove.metalmaxre.editors.player.Player;
import me.afoolslove.metalmaxre.editors.tank.EquipmentDamaged;
import me.afoolslove.metalmaxre.editors.tank.Tank;
import me.afoolslove.metalmaxre.editors.text.mapping.ICharMap;
import me.afoolslove.metalmaxre.utils.GameMode;
import me.afoolslove.metalmaxre.utils.ListSingleMap;
import me.afoolslove.metalmaxre.utils.NumberR;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

public class SaveEditorImpl implements ISaveEditor {
    private final MetalMaxRe metalMaxRe;
    private final SaveData saveData;

    public SaveEditorImpl(@NotNull MetalMaxRe metalMaxRe, @NotNull SaveData saveData) {
        this.metalMaxRe = metalMaxRe;
        this.saveData = saveData;
    }

    @Override
    public MetalMaxRe getMetalMaxRe() {
        return metalMaxRe;
    }

    @Override
    public SaveData getSaveData() {
        return saveData;
    }

    @Override
    public void setPlayerName(@NotNull Player player, String name) {
        byte[] bytes = ICharMap.toBytes(getMetalMaxRe().getCharMap(), name, null);
        // 玩家名称占 4+1字节
        setData(0x0000 + player.getId() * 5, bytes, 0x00, Math.min(bytes.length, 0x05));
    }

    @Override
    public void setTankName(@NotNull Tank tank, String name) {
        byte[] bytes = ICharMap.toBytes(getMetalMaxRe().getCharMap(), name, null);
        // 坦克名称占 6+1字节
        setData(0x000F + tank.getId() * 7, bytes, 0x00, Math.min(bytes.length, 0x07));
    }

    @Override
    public void setGameMode(@NotNull GameMode gameMode) {
        setData(0x005C, gameMode.getValue());
    }

    @Override
    public void setMoney(int money) {
        setData(0x005D, NumberR.toByteArray(money, 3, false));
    }

    @Override
    public void setPlayerAttribute(@NotNull Player player, @NotNull SavePlayerAttribute attribute) {
        // 最大血量
        setData(0x0060 + player.getId() * 2, attribute.getBytesMaxHealth());
        // 当前血量
        setData(0x0066 + player.getId() * 2, attribute.getBytesHealth());
        // 当前攻击力
        setData(0x006C + player.getId() * 2, attribute.getBytesAttack());
        // 当前防御力
        setData(0x0072 + player.getId() * 2, attribute.getBytesDefense());
        // 入队状态
        setData(0x0078 + player.getId(), attribute.getTeamStatus());
        // 异常状态
        setData(0x007B + player.getId(), attribute.getDeBuff());
        // 当前等级
        setData(0x007E + player.getId(), attribute.getLevel());
        // 当前力量
        setData(0x0081 + player.getId(), attribute.getStrength());
        // 当前智力
        setData(0x0084 + player.getId(), attribute.getWisdom());
        // 当前速度
        setData(0x0087 + player.getId(), attribute.getSpeed());
        // 当前体力
        setData(0x008A + player.getId(), attribute.getVitality());
        // 当前战斗等级
        setData(0x008D + player.getId(), attribute.getBattleSkill());
        // 当前修理等级
        setData(0x0090 + player.getId(), attribute.getRepairSkill());
        // 当前驾驶等级
        setData(0x0093 + player.getId(), attribute.getDrivingSkill());
        // 当前装备
        setData(0x0096 + player.getId() * 8, attribute.getEquipment());
        // 当前道具
        setData(0x00AE + player.getId() * 8, attribute.getInventory());
        // 当前装备穿戴状态
        setData(0x00C6 + player.getId(), attribute.getEquipmentState());
        // 当前经验值
        setData(0x00C9 + player.getId() * 3, attribute.getBytesExperience());
    }

    @Override
    public void setTankAttribute(@NotNull Tank tank, @NotNull SaveTankAttribute attribute) {
        // 当前底盘重量
        setData(0x00D2 + tank.getId() * 2, attribute.getWeightByteArray());
        // 当前SP
        setData(0x00E8 + tank.getId() * 2, attribute.getSpByteArray());
        // 当前底盘防御力
        setData(0x00FE + tank.getId() * 2, attribute.getDefenseByteArray());
        // 当前异常状态
        setData(0x0114 + tank.getId(), attribute.getDeBuff());
        // 当前弹仓容量
        setData(0x011F + tank.getId(), attribute.getShells());
        // 当前装备装备状态
        setData(0x012A + tank.getId(), attribute.getEquipmentState());
        // 当前装备破损状态和武器残弹数量
        for (int i = 0; i < 0x08; i++) {
            byte equipmentData = attribute.getEquipmentsDamaged()[i].getValue();
            equipmentData |= attribute.getResidualShells()[i]; // & 0x3F
            setData(0x0135 + ((tank.getId() * 8) + i), equipmentData);
        }
        // 当前特殊炮弹和数量
        for (int i = 0; i < 0x06; i++) {
            SingleMapEntry<Byte, Byte> entry = attribute.getSpecialShells().get(i);
            // 特殊炮弹数量
            setData(0x018D + ((tank.getId() * 6) + i), entry.getValue());
            // 特殊炮弹
            setData(0x0322 + ((tank.getId() * 6) + i), entry.getKey());
        }
        // 当前工具
        setData(0x01CF + tank.getId() * 8, attribute.getInventory());
        // 当前装备
        setData(0x0227 + tank.getId() * 8, attribute.getEquipment());
        // 开洞状态
        setData(0x0317 + tank.getId(), attribute.getSlot());
    }

    @Override
    public void setWantedBreakLevel(int wantedMonsterId, int breakLevel) {
        setData(0x027F + wantedMonsterId, (byte) (breakLevel & 0xFF));
    }

    @Override
    public void setBreakNumber(@NotNull MonsterType type, int number) {
        int value = (type.getValue() & 0xFF) >>> 6;
        setData(0x028A + value * 2, NumberR.toByteArray(number, 2, false));
    }

    @Override
    public void setGoldChime(int gold) {
        setData(0x0290, NumberR.toByteArray(gold, 3, false));
    }

    @Override
    public void setCheckCode(int checkCode) {
        setData(0x0293, (byte) (checkCode & 0xFF));
    }

    @Override
    public void setTrunkItems(byte[] items) {
        setData(0x0294, items);
    }

    @Override
    public void setTowObject(byte object) {
        setData(0x0364, object);
    }

    @Override
    public void setTaxState(@NotNull Tank taxTank, int tankId) {
        // TAX1-TAX3
        if (!taxTank.isTax() || taxTank.getId() >= Tank.TAX_4.getId()) {
            return;
        }
        setData(0x0368 + taxTank.getId(), (byte) (tankId & 0xFF));
    }


    @Override
    public byte[] getPlayerName(@NotNull Player player) {
        return getData(0x0000 + player.getId() * 0x05, 0x05);
    }

    @Override
    public byte[] getTankName(@NotNull Tank tank) {
        return getData(0x000F + tank.getId() * 0x07, 0x07);
    }

    @Override
    public byte getGameMode() {
        return getData(0x005C);
    }

    @Override
    public byte[] getMoney() {
        return getData(0x005D, 0x03);
    }

    @Override
    public SavePlayerAttribute getPlayerAttribute(@NotNull Player player) {
        SavePlayerAttribute attribute = new SavePlayerAttribute();
        // 最大血量
        attribute.setMaxHealth(NumberR.toInt(getData(0x0060 + player.getId() * 2, 2)));
        // 当前血量
        attribute.setHealth(NumberR.toInt(getData(0x0066 + player.getId() * 2, 2)));
        // 当前攻击力
        attribute.setAttack(NumberR.toInt(getData(0x006C + player.getId() * 2, 2)));
        // 当前防御力
        attribute.setDefense(NumberR.toInt(getData(0x0072 + player.getId() * 2, 2)));
        // 入队状态
        attribute.setTeamStatus(getData(0x0078 + player.getId()));
        // 异常状态
        attribute.setDeBuff(getData(0x007B + player.getId()));
        // 当前等级
        attribute.setLevel(getData(0x007E + player.getId()));
        // 当前力量
        attribute.setStrength(getData(0x0081 + player.getId()));
        // 当前智力
        attribute.setWisdom(getData(0x0084 + player.getId()));
        // 当前速度
        attribute.setSpeed(getData(0x0087 + player.getId()));
        // 当前体力
        attribute.setVitality(getData(0x008A + player.getId()));
        // 当前战斗等级
        attribute.setBattleSkill(getData(0x008D + player.getId()));
        // 当前修理等级
        attribute.setRepairSkill(getData(0x0090 + player.getId()));
        // 当前驾驶等级
        attribute.setDrivingSkill(getData(0x0093 + player.getId()));
        // 当前装备
        attribute.setEquipment(getData(0x0096 + player.getId() * 8, 8));
        // 当前道具
        attribute.setInventory(getData(0x00AE + player.getId() * 8, 8));
        // 当前装备穿戴状态
        attribute.setEquipmentState(getData(0x00C6 + player.getId()));
        // 当前经验值
        attribute.setExperience(NumberR.toInt(getData(0x00C9 + player.getId() * 3, 3)));
        return attribute;
    }

    @Override
    public SaveTankAttribute getTankAttribute(@NotNull Tank tank) {
        SaveTankAttribute attribute = new SaveTankAttribute();
        // 当前底盘重量
        attribute.setWeight(NumberR.toInt(getData(0x00D2 + tank.getId() * 2, 2)));
        // 当前SP
        attribute.setSp(NumberR.toInt(getData(0x00E8 + tank.getId() * 2, 2)));
        // 当前底盘防御力
        attribute.setDefense(NumberR.toInt(getData(0x00FE + tank.getId() * 2, 2)));
        // 当前异常状态
        attribute.setDeBuff(getData(0x0114 + tank.getId()));
        // 当前弹仓容量
        attribute.setShells(getData(0x011F + tank.getId()));
        // 当前装备装备状态
        attribute.setEquipmentState(getData(0x012A + tank.getId()));
        // 当前装备破损状态和武器残弹数量
        // 破损状态
        EquipmentDamaged[] equipmentDamaged = new EquipmentDamaged[0x08];
        // 残弹数量
        byte[] residualShells = new byte[0x08];
        for (int i = 0; i < 0x08; i++) {
            byte equipmentData = getData(0x0135 + ((tank.getId() * 8) + i));
            equipmentDamaged[i] = EquipmentDamaged.fromId(equipmentData & 0B1100_0000);
            residualShells[i] = (byte) (equipmentData & 0B0011_1111);
        }
        attribute.setEquipmentsDamaged(equipmentDamaged);
        attribute.setResidualShells(residualShells);
        // 当前特殊炮弹和数量
        ListSingleMap<Byte, Byte> specialShells = new ListSingleMap<>();
        for (int i = 0; i < 0x06; i++) {
            byte value = getData(0x018D + ((tank.getId() * 6) + i));
            byte key = getData(0x0322 + ((tank.getId() * 6) + i));
            specialShells.add(key, value);
        }
        attribute.setSpecialShells(specialShells);
        // 当前工具
        attribute.setInventory(getData(0x01CF + (tank.getId() * 8), 8));
        // 当前装备
        attribute.setEquipment(getData(0x0227 + (tank.getId() * 8), 8));
        // 当前开洞状态
        attribute.setSlot(getData(0x0317 + tank.getId()));
        return null;
    }

    @Override
    public byte getWantedBreakLevel(int wantedMonsterId) {
        return getData(0x027F + wantedMonsterId);
    }

    @Override
    public byte[] getBreakNumber(@NotNull MonsterType type) {
        int value = (type.getValue() & 0xFF) >>> 6;
        return getData(0x028A + (value * 2), 2);
    }

    @Override
    public byte[] getGoldChime() {
        return getData(0x0290, 0x03);
    }

    @Override
    public byte getCheckCode() {
        return getData(0x0293);
    }

    @Override
    public byte[] getTrunkItems() {
        return getData(0x0294, 0x41);
    }

    @Override
    public byte getTowObject() {
        return getData(0x0364);
    }

    @Override
    public byte getTaxState(@NotNull Tank taxTank) {
        // TAX1-TAX3
        if (!taxTank.isTax() || taxTank.getId() >= Tank.TAX_4.getId()) {
            return 0x00;
        }
        return getData(0x0368 + taxTank.getId());
    }
}
