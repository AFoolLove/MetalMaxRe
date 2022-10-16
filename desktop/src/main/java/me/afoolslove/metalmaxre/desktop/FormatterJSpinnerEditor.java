package me.afoolslove.metalmaxre.desktop;

import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;

public class FormatterJSpinnerEditor extends JSpinner.DefaultEditor {

    public FormatterJSpinnerEditor(JSpinner spinner) {
        this(spinner, null);
    }

    public FormatterJSpinnerEditor(JSpinner spinner, JFormattedTextField.AbstractFormatter formatter) {
        super(spinner);
        if (!(spinner.getModel() instanceof SpinnerNumberModel spinnerNumberModel)) {
            throw new IllegalArgumentException("model not a SpinnerNumberModel");
        }
        getTextField().setEditable(true);
        if (formatter == null) {
            formatter = new DefaultFormatter();
        }
        getTextField().setFormatterFactory(new DefaultFormatterFactory(formatter));
    }


}
