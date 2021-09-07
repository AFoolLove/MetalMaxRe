package me.afoolslove.metalmaxre.editor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 编辑器写入顺序
 * <p>
 * 写入当前编辑器数据之前先写入目标编辑器的数据
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface WriteBefore {
    Class<? extends AbstractEditor<?>>[] value();
}
