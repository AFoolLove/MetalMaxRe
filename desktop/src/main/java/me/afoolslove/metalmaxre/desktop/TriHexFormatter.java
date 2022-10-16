package me.afoolslove.metalmaxre.desktop;

import javax.swing.*;
import java.util.regex.Pattern;

public class TriHexFormatter extends JFormattedTextField.AbstractFormatter {
    private final static Pattern HEX_PATTERN = Pattern.compile("^([0-9a-zA-Z]{1,6})$"); // 00-FF
    private static final TriHexFormatter INSTANCE = new TriHexFormatter();

    public static TriHexFormatter getInstance() {
        return INSTANCE;
    }

    @Override
    public Object stringToValue(String text) {
        if (text.isEmpty() || !HEX_PATTERN.matcher(text).find()) {
            return 0x00;
        }
        return Integer.parseInt(text, 16);
    }

    @Override
    public String valueToString(Object value) {
        if (!(value instanceof Number number)) {
            return "000000";
        }
        return String.format("%06X", number.intValue() & 0xFFFFFF);
    }
}
