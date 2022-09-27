package me.afoolslove.metalmaxre.desktop;

import javax.swing.*;
import java.util.regex.Pattern;

public class TwoHexFormatter extends JFormattedTextField.AbstractFormatter {
    private final static Pattern HEX_PATTERN = Pattern.compile("^([0-9a-zA-Z]{1,4})$"); // 00-FF
    private static final TwoHexFormatter INSTANCE = new TwoHexFormatter();

    public static TwoHexFormatter getInstance() {
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
            return "0000";
        }
        return String.format("%04X", number.intValue() & 0xFFFF);
    }
}
