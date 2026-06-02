package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.editors.IRomEditor;

import java.util.List;

/**
 * 怪物模型编辑器
 * <p>
 * 未完成
 *
 * @author AFoolLove
 */
public interface IMonsterModelEditor extends IRomEditor {
    @Override
    default String getId() {
        return "monsterModelEditor";
    }


    List<MonsterModel> getMonsterModels();

    MonsterModel getMonsterModel(int monsterId);
}
