package me.afoolslove.metalmaxre.editors.text;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;

import java.util.List;
import java.util.Map;

public interface ITextEditor extends IRomEditor {
    @Override
    default String getId() {
        return "textEditor";
    }

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
     * 获取指定页，指定段的文本
     *
     * @param page 文本页
     * @param row  文本段
     * @return 获取失败返回 {@code null}
     */
    default TextBuilder getText(int page, int row) {
        List<TextBuilder> list = getPage(page);
        if (list != null && list.size() > row) {
            return list.get(row);
        }
        return null;
    }

    /**
     * 获取指定页，指定段的纯文本
     *
     * @param page 文本页
     * @param row  文本段
     * @return 获取失败返回 {@code null}
     */
    default String getPlainText(int page, int row) {
        TextBuilder text = getText(page, row);
        return text == null ? null : text.toText(getCharMap());
    }

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
     * 获取物品的名称
     *
     * @param itemId 物品的id
     * @return 物品的名称
     */
    default String getItemName(byte itemId) {
        return getItemName(itemId & 0xFF);
    }

    /**
     * 获取怪物的名称
     *
     * @param monsterId 怪物的id
     * @return 怪物的名称
     */
    String getMonsterName(int monsterId);

    /**
     * 彩蛋名称
     * <p>
     * 如果玩家的名称与彩蛋名称相同，就会触发彩蛋名称
     * <p>
     * K：触发彩蛋名称
     * V：彩蛋名称
     */
    Map<TextBuilder, List<TextBuilder>> getEasterEggNames();

    /**
     * 玩家二的名称池
     */
    List<TextBuilder> getPlayer1NamePool();

    /**
     * 玩家三的名称池
     */
    List<TextBuilder> getPlayer2NamePool();

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
     * 设置彩蛋名称
     */
    void setEasterEggName(TextBuilder name, List<TextBuilder> easterEggName);

    /**
     * 设置玩家二名称池
     */
    void setPlayer1NamePool(List<TextBuilder> namePool);

    /**
     * 设置玩家三名称池
     */
    void setPlayer2NamePool(List<TextBuilder> namePool);

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

    /**
     * 获取彩蛋名称的地址
     *
     * @return 彩蛋名称的地址
     */
    DataAddress getEasterEggNameAddress();

    /**
     * 获取玩家二名称池的地址
     *
     * @return 玩家二名称池的地址
     */
    DataAddress getPlayer1NamePoolAddress();

    /**
     * 获取玩家三名称池的地址
     *
     * @return 玩家三名称池的地址
     */
    DataAddress getPlayer2NamePoolAddress();
}
