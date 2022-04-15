package me.afoolslove.metalmaxre.editors.treasure;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.AbstractEditor;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 宝藏编辑器
 *
 * @author AFoolLove
 */
public class TreasureEditorImpl extends AbstractEditor implements ITreasureEditor {

    public TreasureEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        super(metalMaxRe);
    }


    @Override
    public int getTreasureMaxCount() {
        return 0;
    }

    @Override
    public int getRandomTreasureMaxCount() {
        return 0;
    }

    @Override
    public int getCheckPointMaxCount() {
        return 0;
    }

    @Override
    public List<Treasure> getTreasures() {
        return null;
    }
}
