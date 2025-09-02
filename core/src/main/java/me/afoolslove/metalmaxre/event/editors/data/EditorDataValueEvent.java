package me.afoolslove.metalmaxre.event.editors.data;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.event.editors.EditorEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 数据值事件
 *
 * @author AFoolLove
 */
public class EditorDataValueEvent extends EditorEvent {
    private final int index;
    private final Number dataValue;

    private EditorDataValueEvent(@NotNull MetalMaxRe metalMaxRe, @NotNull IRomEditor editor, int index, Number dataValue) {
        super(metalMaxRe, editor);
        this.index = index;
        this.dataValue = dataValue;
    }

    public int getIndex() {
        return index;
    }

    public Number getDataValue() {
        return dataValue;
    }

    /**
     * 数据值变更
     */
    public static class ValueChanged extends EditorDataValueEvent {
        private final Number newValue;

        public ValueChanged(@NotNull MetalMaxRe metalMaxRe, @NotNull IRomEditor editor, int index, Number oldValue, Number newValue) {
            super(metalMaxRe, editor, index, oldValue);
            this.newValue = newValue;
        }

        /**
         * @return 旧的数据值
         */
        public Number getOldDataValue() {
            return getDataValue();
        }

        /**
         * @return 新的数据值
         */
        public Number getNewDataValue() {
            return newValue;
        }
    }
}
