package me.afoolslove.metalmaxre.patch;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * 补丁管理器
 */
public interface IPatchManager {

    /**
     * 获取全部补丁
     */
    @NotNull
    Map<String, IRomPatch> getPatches();

    /**
     * 获取补丁
     *
     * @param key 补丁key
     */
    @Nullable
    IRomPatch getPatch(String key);

    /**
     * 添加补丁
     *
     * @param patch 补丁
     */
    void putPatch(@NotNull IRomPatch patch);

    /**
     * 移除补丁
     *
     * @param key 补丁key
     * @return 返回被移除的补丁
     */
    @Nullable
    IRomPatch removePatch(String key);

}
