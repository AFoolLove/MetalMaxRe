package me.afoolslove.metalmaxre.editors.sprite.script;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.editors.sprite.Sprite;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface ISpriteScriptEditor extends IRomEditor {

    /**
     * 获取精灵脚本的最大数量
     *
     * @return 精灵脚本的最大数量
     */
    default int getSpriteScriptMaxCount() {
        return 0xA9;
    }

    /**
     * 获取所有精灵脚本
     *
     * @return 所有精灵脚本
     */
    Map<Integer, SpriteScript> getSpriteScripts();

    /**
     * 通过精灵的action获取脚本
     */
    default SpriteScript getSpriteScript(int action) {
        return getSpriteScripts().get(action);
    }

    /**
     * 通过精灵的action获取脚本
     */
    default SpriteScript getSpriteScript(@NotNull Sprite sprite) {
        return getSpriteScript(sprite.intAction());
    }

    /**
     * 获取精灵脚本地址
     *
     * @return 精灵脚本地址
     */
    DataAddress getSpriteScriptIndexAddress();

    /**
     * 获取精灵脚本地址
     *
     * @return 精灵脚本地址
     */
    DataAddress getSpriteScriptAddress();
}
