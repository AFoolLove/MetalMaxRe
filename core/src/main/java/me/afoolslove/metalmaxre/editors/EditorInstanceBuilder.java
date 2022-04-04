package me.afoolslove.metalmaxre.editors;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class EditorInstanceBuilder {
    private final Map<Class<IRomEditor>, Supplier<IRomEditor>> builders = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <E extends IRomEditor> EditorInstanceBuilder add(Class<E> editor, Supplier<IRomEditor> supplier) {
        builders.put((Class<IRomEditor>) editor, supplier);
        return this;
    }

    public <E extends IRomEditor> E get(Class<E> editor) {
        var supplier = builders.get(editor);
        if (supplier != null) {
            return (E) supplier.get();
        } else if (editor.isInterface()) {
            return null;
        } else {
            try {
                return editor.getConstructor().newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
}
