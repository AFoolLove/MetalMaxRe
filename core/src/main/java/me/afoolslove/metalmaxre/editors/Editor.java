package me.afoolslove.metalmaxre.editors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author AFoolLove
 */
public @interface Editor {

    /**
     * 加载编辑器数据的方法，一个类中必须有1个该注解
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Load {
        Type[] value() default Type.LOAD;
    }

    /**
     * 应用编辑器数据的方法，一个类中必须有1个该注解
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Apply {
        Type[] value() default Type.APPLY;
    }

    /**
     * 不会对编辑器进行任何操作，只是引用编辑器实例
     * <p>
     * *Apply注解中的参数才会生效
     */
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @interface QuoteOnly {
    }

    /**
     * 可以指定编辑器
     * <p>
     * *空数组表示所有编辑器
     * *Load或Apply注解中的参数才会生效
     */
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @interface TargetEditor {
        Class<? extends IRomEditor>[] value() default {};
    }

    enum Type {
        PRE,
        LOAD,
        APPLY,
        POST
    }
}
