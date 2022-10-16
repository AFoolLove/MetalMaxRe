package me.afoolslove.metalmaxre.editors.text;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;

import java.util.List;
import java.util.Map;

public interface ITextEditor extends IRomEditor {
    /**
     * 获取所有索引文本集
     *
     * @return 所有索引文本集
     */
    Map<Integer, List<TextBuilder>> getPages();

    /**
     * 获取指定页的文本集
     *
     * @param page 页
     * @return 指定页的文本集
     */
    List<TextBuilder> getPage(int page);

    /**
     * 获取城镇的名称
     *
     * @param townId 城镇id
     * @return 城镇的名称
     */
    String getTownName(int townId);

    /**
     * 获取物品的名称
     *
     * @param itemId 物品的id
     * @return 物品的名称
     */
    String getItemName(int itemId);

    /**
     * 获取怪物的名称
     *
     * @param monsterId 怪物的id
     * @return 怪物的名称
     */
    String getMonsterName(int monsterId);

    /**
     * 设置城镇的名称
     *
     * @param townId  城镇的id
     * @param newName 新的城镇名称
     */
    void setTownName(int townId, String newName);

    /**
     * 设置物品的名称
     *
     * @param itemId  物品的id
     * @param newName 新的物品名称
     */
    void setItemName(int itemId, String newName);

    /**
     * 设置怪物的名称
     *
     * @param monsterId 怪物的id
     * @param newName   新的怪物名称
     */
    void setMonsterName(int monsterId, String newName);

    /**
     * 获取已知的文本段的地址
     *
     * @return 已知的文本段的地址
     */
    Map<Integer, DataAddress> getTextAddresses();

    /**
     * 获取城镇名称的地址
     *
     * @return 城镇名称的地址
     */
    DataAddress getTownNameAddress();

    /**
     * 获取物品名称的地址
     *
     * @return 物品名称的地址
     */
    DataAddress getItemNameAddress();

    /**
     * 获取怪物名称的地址
     *
     * @return 怪物名称的地址
     */
    DataAddress getMonsterNameAddress();
}
