package me.afoolslove.metalmaxre.editors.sprite.script;

import me.afoolslove.metalmaxre.editors.IRomEditor;

import java.util.Map;

public interface ISpriteScriptEditor extends IRomEditor {
    @Override
    default String getId() {
        return "spriteScriptEditor";
    }

    /**
     *
     * @return 动态精灵脚本的最大数量
     */
    default int getDynamicScriptMaxCount() {
        return 0xA9;
    }

    /**
     *
     * @return 静态精灵脚本的最大数量
     */
    default int getStaticScriptMaxCount() {
        return 0x6C;
    }

    /**
     *
     * @return 获取所有动态脚本
     */
    Map<Integer, byte[]> getDynamicScripts();

    /**
     *
     * @return 获取所有静态脚本
     */
    Map<Integer, byte[]> getStaticScripts();


    Map<Integer, SpriteScriptAction> getSpriteScriptActions();

}
