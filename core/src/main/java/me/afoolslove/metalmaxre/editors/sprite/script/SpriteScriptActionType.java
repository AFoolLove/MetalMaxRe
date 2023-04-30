package me.afoolslove.metalmaxre.editors.sprite.script;

public enum SpriteScriptActionType {
    /**
     * 动态类型指令，只能在非玩家主动对话时使用
     */
    DYNAMIC_TYPE,
    /**
     * 静态类型指令，只能在玩家主动对话时使用
     */
    STATIC_TYPE,
    /**
     * 通用类型指令，可以在其它两种里任何时候使用
     */
    GENERAL_TYPE
}
