package me.afoolslove.metalmaxre.event;

import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.event.editors.EditorEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 事件处理器
 *
 * @author AFoolLove
 */
public class EventHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventHandler.class);
    private final Map<Class<Event>, Set<EventListener>> eventListeners = new HashMap<>();

    /**
     * 注册这个监听器里的所有有效的事件监听器
     *
     * @param listener 被注册的监听器
     */
    public synchronized void register(@NotNull EventListener listener) {
        for (Method declaredMethod : listener.getClass().getDeclaredMethods()) {
            if (declaredMethod.getParameterCount() == 1) {
                Parameter parameter = declaredMethod.getParameters()[0];
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
    public synchronized <E extends Event> void register(@NotNull Class<E> eventType, @NotNull EventListener listener) {
        @SuppressWarnings("unchecked")
        Set<EventListener> listeners = eventListeners.computeIfAbsent((Class<Event>) eventType, k -> new HashSet<>());
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
                Parameter parameter = declaredMethod.getParameters()[0];
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
        Set<EventListener> listeners = eventListeners.get(eventType);
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
                            Parameter parameter = declaredMethod.getParameters()[0];
                            if (!parameter.getType().isInstance(event)) {
                                // 参数事件类与事件类不同或不是父子关系
                                continue;
                            }

                            Editor.TargetEditor targetEditor = parameter.getAnnotation(Editor.TargetEditor.class);
                            if (targetEditor != null && event instanceof EditorEvent editorEvent) {
                                // 如果没有指定编辑器，就是包括所有编辑器
                                if (targetEditor.value().length != 0) {
                                    // 指定了编辑器
                                    Optional<Class<? extends IRomEditor>> any = Arrays.stream(targetEditor.value())
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
                            } catch (Exception ex) {
                                if (ex.getCause() != null) {
                                    LOGGER.error("事件执行时异常", ex.getCause());
                                } else {
                                    LOGGER.error("事件执行时异常", ex);
                                }
                            }
                        }
                    }
                });

    }

    /**
     * 通知一个编辑器事件的发生
     * <p>
     * 注：该方法与#callEvent(EditorEvent)方法唯一不同的是，它需要一个编辑器的 加载耗时 参数
     * 但不是所有编辑器事件都需要 加载耗时 ，所以编辑器事件使用#callEvent(EditorEvent)方法也不会有什么问题
     *
     * @param event 编辑器发生的事件
     */
    public void callEvent(@NotNull EditorEvent event, long completionTime) {
        event.setCompletionTime(completionTime);
        callEvent(event);
    }
}
