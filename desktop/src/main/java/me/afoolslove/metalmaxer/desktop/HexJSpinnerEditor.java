package me.afoolslove.metalmaxer.desktop;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;

public class HexJSpinnerEditor extends JSpinner.DefaultEditor {

    public HexJSpinnerEditor(JSpinner spinner) {
        super(spinner);
        if (!(spinner.getModel() instanceof SpinnerNumberModel)) {
            throw new IllegalArgumentException("model not a SpinnerNumberModel");
        }
        getTextField().setEditable(true);
        getTextField().setFormatterFactory(new DefaultFormatterFactory(HexFormatter.getInstance()));
    }
}
