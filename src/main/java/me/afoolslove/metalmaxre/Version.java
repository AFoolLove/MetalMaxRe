package me.afoolslove.metalmaxre;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * 游戏ROM的版本
 */
public enum Version {
    /**
     * 原版
     * <p>
     * *未进行兼容
     */
    ORIGINAL,
    /**
     * 中文版
     * <p>
     * *主要修改目标
     */
    CHINESE,
    /**
     * SuperHack版
     * <p>
     * *未进行兼容
     */
    SUPER_HACK,
    /**
     * SuperHack通用版
     * <p>
     * *未进行兼容
     */
    SUPER_HACK_GENERAL;


    public static void getVersion(@NotNull Consumer<Version> action) {
        var instance = MetalMaxRe.getInstance();
    }


}
