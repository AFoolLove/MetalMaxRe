package me.afoolslove.metalmaxre.editors.list;

import me.afoolslove.metalmaxre.InteractType;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.ItemList;

import java.util.List;
import java.util.Map;

public interface IListEditor extends IRomEditor {
    @Override
    default String getId() {
        return "listEditor";
    }

    /**
     * @return 获取所有类型的清单
     */
    Map<InteractType, List<ItemList<ItemValue>>> getTypeLists();

    /**
     * 通过清单类型获取清单
     *
     * @param type 清单类型
     * @return 清单
     */
    default List<ItemList<ItemValue>> getTypeList(InteractType type) {
        return getTypeLists().get(type);
    }
}
