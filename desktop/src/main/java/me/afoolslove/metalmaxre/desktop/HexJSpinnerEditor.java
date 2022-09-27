package me.afoolslove.metalmaxre.desktop;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;

public class HexJSpinnerEditor extends JSpinner.DefaultEditor {

    public HexJSpinnerEditor(JSpinner spinner) {
        this(spinner, false);
    }

    public HexJSpinnerEditor(JSpinner spinner, boolean twoHex) {
        super(spinner);
        if (!(spinner.getModel() instanceof SpinnerNumberModel)) {
            throw new IllegalArgumentException("model not a SpinnerNumberModel");
        }
        getTextField().setEditable(true);
        if (twoHex) {
            getTextField().setFormatterFactory(new DefaultFormatterFactory(TwoHexFormatter.getInstance()));
        } else {
            getTextField().setFormatterFactory(new DefaultFormatterFactory(HexFormatter.getInstance()));
        }
    }
}
