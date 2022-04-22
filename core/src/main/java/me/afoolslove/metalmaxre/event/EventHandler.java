package me.afoolslove.metalmaxre.event;

import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.event.editors.EditorEvent;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 事件处理器
 *
 * @author AFoolLove
 */
public class EventHandler {
    private final Map<Class<Event>, Set<EventListener>> eventListeners = new HashMap<>();

    /**
     * 注册这个监听器里的所有有效的事件监听器
     *
     * @param listener 被注册的监听器
     */
    public synchronized void register(@NotNull EventListener listener) {
        for (Method declaredMethod : listener.getClass().getDeclaredMethods()) {
            if (declaredMethod.getParameterCount() == 1) {
                var parameter = declaredMethod.getParameters()[0];
                if (Event.class.isAssignableFrom(parameter.getType())) {
                    register((Class<Event>) parameter.getType(), listener);
                }
            }
        }
    }

    /**
     * 注册一个事件监听器
     *
     * @param eventType 事件类型
     * @param listener  被注册的监听器
     */
    public synchronized void register(@NotNull Class<Event> eventType, @NotNull EventListener listener) {
        var listeners = eventListeners.computeIfAbsent(eventType, k -> new HashSet<>());
        listeners.add(listener);
    }

    /**
     * 注销事件监听器里所有有效的事件监听器
     *
     * @param listener 被注销所有事件监听的监听器
     */
    public synchronized void unregister(@NotNull EventListener listener) {
        for (Method declaredMethod : listener.getClass().getDeclaredMethods()) {
            if (declaredMethod.getParameterCount() == 1) {
                var parameter = declaredMethod.getParameters()[0];
                if (Event.class.isAssignableFrom(parameter.getType())) {
                    unregister((Class<Event>) parameter.getType(), listener);
                }
            }
        }
    }

    /**
     * 注销一个事件监听器
     *
     * @param eventType 事件类型
     * @param listener  被注销的监听器
     */
    public synchronized void unregister(@NotNull Class<Event> eventType, @NotNull EventListener listener) {
        var listeners = eventListeners.get(eventType);
        if (listeners == null || listeners.isEmpty()) {
            return;
        }
        listeners.remove(listener);
    }

    /**
     * 通知一个事件的发生
     *
     * @param event 发生的事件
     */
    public void callEvent(@NotNull Event event) {
        eventListeners.entrySet().stream()
                // 过滤出相关事件
                // 目标事件类和它父类才能接受事件
                .filter(entry -> entry.getKey().isInstance(event))
                .map(Map.Entry::getValue)
                .flatMap(Collection::stream) // 将所有事件监听器合并
                .distinct() // 去重
                .forEach(eventListener -> {
                    for (Method declaredMethod : eventListener.getClass().getDeclaredMethods()) {
                        if (declaredMethod.getParameterCount() == 1) {
                            var parameter = declaredMethod.getParameters()[0];
                            if (!parameter.getType().isInstance(event)) {
                                // 参数事件类与事件类不同或不是父子关系
                                continue;
                            }

                            var targetEditor = parameter.getAnnotation(Editor.TargetEditor.class);
                            if (targetEditor != null && event instanceof EditorEvent editorEvent) {
                                // 如果没有指定编辑器，就是包括所有编辑器
                                if (targetEditor.value().length != 0) {
                                    // 指定了编辑器
                                    var any = Arrays.stream(targetEditor.value())
                                            .filter(clazz -> clazz.isInstance(editorEvent.getEditor()))
                                            .findAny();
                                    if (any.isEmpty()) {
                                        // 不是指定的编辑器，不执行这个监听器
                                        continue;
                                    }
                                }
                            }

                            try {
                                declaredMethod.setAccessible(true);
                                declaredMethod.invoke(eventListener, parameter.getType().cast(event));
                            } catch (IllegalAccessException | InvocationTargetException ex) {
                                // throw new RuntimeException(ex);
                                ex.printStackTrace();
                            }
                        }
                    }
                });

    }
}
