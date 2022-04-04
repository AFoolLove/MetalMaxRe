package me.afoolslove.metalmaxre.editors.impl;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.IEditorListener;
import me.afoolslove.metalmaxre.editors.IEditorManager;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.editors.computer.IComputerEditor;
import me.afoolslove.metalmaxre.editors.computer.impl.ComputerEditorImpl;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

import javax.crypto.CipherSpi;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;

public class EditorManagerImpl implements IEditorManager {
    private final MetalMaxRe metalMaxRe;

    private final Map<Class<? extends IRomEditor>, IRomEditor> editors = new LinkedHashMap<>();

    private final Map<Class<? extends IRomEditor>, Function<MetalMaxRe, ? extends IRomEditor>> editorBuilders = new HashMap<>();

    private final Map<Class<? extends IRomEditor>, SingleMapEntry<String, Class<? extends IRomEditor>[]>> loadMethods = new HashMap<>();
    private final Map<Class<? extends IRomEditor>, SingleMapEntry<String, Class<? extends IRomEditor>[]>> applyMethods = new HashMap<>();

    public EditorManagerImpl(@NotNull MetalMaxRe metalMaxRe) {
        this.metalMaxRe = metalMaxRe;

        editorBuilders.put(IComputerEditor.class, ComputerEditorImpl.JapaneseComputerEditor::new);
    }

    @Override
    public MetalMaxRe getMetalMaxRe() {
        return metalMaxRe;
    }

    @Override
    public synchronized <E extends IRomEditor> void register(@NotNull Class<E> editorType, @NotNull Function<MetalMaxRe, E> builder) {
        if (editorBuilders.get(editorType) != null) {
            return;
        }
        editorBuilders.put(editorType, builder);

        // 寻找加载和应用方法
        Method loadMethod = null, applyMethod = null;
        for (Method method : editorType.getDeclaredMethods()) {
            if (loadMethod == null) {
                // 加载方法
                Annotation annotation = method.getAnnotation(Editor.Load.class);
                if (annotation != null) {
                    loadMethod = method;
                }
            } else if (applyMethod == null) {
                // 应用方法
                Annotation annotation = method.getAnnotation(Editor.Apply.class);
                if (annotation != null) {
                    applyMethod = method;
                }
            } else {
                // 加载和应用方法都已经找到
                break;
            }
        }
        if (loadMethod == null || applyMethod == null) {
            //TODO 没有找到加载或应用数据的方法
            return;
        }

        @SuppressWarnings("unchecked")
        final Class<E>[] loadPars = (Class<E>[]) Array.newInstance(editorType, loadMethod.getParameterCount());
        if (loadPars.length > 0) {
            var parameters = loadMethod.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                if (IRomEditor.class.isAssignableFrom(parameters[i].getType())) {
                    loadPars[i] = (Class<E>) parameters[i].getType();
                }
            }
        }
        loadMethods.put(editorType, SingleMapEntry.create(loadMethod.getName(), loadPars));

        @SuppressWarnings("unchecked")
        final Class<E>[] applyPars = (Class<E>[]) Array.newInstance(editorType, applyMethod.getParameterCount());
        if (applyPars.length > 0) {
            var parameters = applyMethod.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                if (IRomEditor.class.isAssignableFrom(parameters[i].getType())) {
                    applyPars[i] = (Class<E>) parameters[i].getType();
                }
            }
        }

        applyMethods.put(editorType, SingleMapEntry.create(applyMethod.getName(), applyPars));
    }

    @Override
    public <R> R loadEditors() {
        for (var builder : editorBuilders.entrySet()) {
            if (!editors.containsKey(builder.getKey())) {
                loadEditor(builder.getKey());
            }
        }
        return null;
    }

    @Override
    public <R> R reloadEditors() {
        return null;
    }

    @Override
    public <R> R loadEditor(Class<? extends IRomEditor> type) {
        var e = getEditor(type);
        if (e != null) {
            // 已经加载
            return null;
        }

        var builder = editorBuilders.get(type);
        if (builder != null) {
            IRomEditor editor = builder.apply(getMetalMaxRe());
            var loadMethod = loadMethods.get(type);

            Object[] pars = new Object[loadMethod.getValue().length];

            if (pars.length != 0) {
                for (int i = 0, len = loadMethod.getValue().length; i < len; i++) {
                    pars[i] = getEditor(loadMethod.getValue()[i]);
                }
            }

            try {
                // 准备加载
                for (IEditorListener listener : editor.getListeners()) {
                    listener.onPreLoad(editor);
                }

                final long start = System.currentTimeMillis();
                type.getMethod(loadMethod.getKey(), loadMethod.getValue()).invoke(editor, pars); // 开始加载
                final long end = System.currentTimeMillis() - start;

                // 加载完毕
                for (IEditorListener listener : editor.getListeners()) {
                    listener.onPostLoad(editor, end);
                }
            } catch (Exception ex) {
                ex.printStackTrace();

                // 加载失败
                for (IEditorListener listener : editor.getListeners()) {
                    listener.onPostLoad(editor, -1);
                }
            }
            editors.put(type, editor);
        }
        return null;
    }

    @Override
    public <R> R applyEditor(Class<? extends IRomEditor> type) {
        var editor = getEditor(type);
        if (editor != null) {
            var applyMethod = applyMethods.get(type);

            Object[] pars = new Object[applyMethod.getValue().length];

            if (pars.length != 0) {
                for (int i = 0, len = applyMethod.getValue().length; i < len; i++) {
                    pars[i] = getEditor(applyMethod.getValue()[i]);
                }
            }

            try {
                // 准备应用数据
                for (IEditorListener listener : editor.getListeners()) {
                    listener.onPreApply(editor);
                }

                final long start = System.currentTimeMillis();
                type.getMethod(applyMethod.getKey(), applyMethod.getValue()).invoke(editor, pars); // 开始应用数据
                final long end = System.currentTimeMillis() - start;

                // 应用数据完成
                for (IEditorListener listener : editor.getListeners()) {
                    listener.onPostApply(editor, end);
                }
            } catch (Exception e) {
                e.printStackTrace();

                // 应用数据失败
                for (IEditorListener listener : editor.getListeners()) {
                    listener.onPostLoad(editor, -1);
                }
            }
        }
        return null;
    }

    @Override
    public <R> R reloadEditor(Class<? extends IRomEditor> type) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends IRomEditor> E getEditor(Class<? extends IRomEditor> editor) {
        return (E) editors.get(editor);
    }

}
