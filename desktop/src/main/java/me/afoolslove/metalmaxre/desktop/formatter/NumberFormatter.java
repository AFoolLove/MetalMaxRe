package me.afoolslove.metalmaxre.desktop.formatter;

import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.text.ParseException;
import java.util.regex.Pattern;

public class NumberFormatter extends JFormattedTextField.AbstractFormatter {
    private static final NumberFormatter INSTANCE = new NumberFormatter();

    public static NumberFormatter getInstance() {
        return INSTANCE;
    }

    private Pattern pattern;
    private int defValue;

    public NumberFormatter() {
        this(null, 0x00);
    }

    public NumberFormatter(@Nullable Pattern pattern, int defValue) {
        this.pattern = pattern;
        this.defValue = defValue;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public void setDefValue(int defValue) {
        this.defValue = defValue;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public int getDefValue() {
        return defValue;
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        if (text.isEmpty()) {
            return defValue;
        }
        if (pattern != null) {
            if (!pattern.matcher(text).find()) {
                return defValue;
            }
        }
        return patternToValue(text);
    }

    public Object patternToValue(String text) throws ParseException {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return defValue;
        }
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value instanceof Number number) {
            return Integer.toString(number.intValue());
        }
        return Integer.toString(defValue);
    }
}
