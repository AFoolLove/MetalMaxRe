package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.editors.IRomEditor;

public interface IMonsterModelEditor extends IRomEditor {
    @Override
    default String getId() {
        return "monsterModelEditor";
    }

}
