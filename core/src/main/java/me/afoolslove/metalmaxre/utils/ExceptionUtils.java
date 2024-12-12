package me.afoolslove.metalmaxre.utils;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

public final class ExceptionUtils {

    /**
     * 将异常日志转换为字符串
     *
     * @param e 异常
     * @return 异常日志
     */
    public static String toString(@NotNull Exception e) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter printWriter = new PrintWriter(out);
        e.printStackTrace(printWriter);
        printWriter.flush();
        return out.toString();
    }
}
