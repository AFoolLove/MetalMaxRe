package me.afoolslove.metalmaxre.editors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public @interface Editor {

    /**
     * 加载编辑器数据的方法，一个类中必须有1个该注解
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Load {
        Type value() default Type.LOAD;
    }

    /**
     * 应用编辑器数据的方法，一个类中必须有1个该注解
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Apply {
        Type value() default Type.APPLY;
    }

    enum Type {
        PRE,
        LOAD,
        APPLY,
        POST
    }
}
