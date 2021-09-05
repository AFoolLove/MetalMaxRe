package me.afoolslove.metalmaxre.editor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 编辑器顺序
 * <p>
 * 加载当前编辑器之前先加载目标编辑器
 *
 * @author AFoolLove
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.TYPE)
public @interface ReadBefore {
    Class<? extends AbstractEditor<?>>[] value();
}
