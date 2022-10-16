package me.afoolslove.metalmaxre.desktop;

import javax.swing.*;

public class HexJSpinnerEditor extends FormatterJSpinnerEditor {

    public HexJSpinnerEditor(JSpinner spinner) {
        super(spinner, HexFormatter.getInstance());
    }

    public HexJSpinnerEditor(JSpinner spinner, JFormattedTextField.AbstractFormatter formatter) {
        super(spinner, formatter);
    }
}
