package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import me.afoolslove.metalmaxre.editors.text.TextBuilder;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXSearchField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchTextFrame extends AbstractEditorFrame {
    private JPanel contentPane;
    private JXList searchTexts;
    private JXSearchField search;

    public SearchTextFrame(@NotNull Frame frame, @NotNull MetalMaxRe metalMaxRe) {
        super(frame, metalMaxRe);
        init("文本搜索", contentPane);
    }

    public SearchTextFrame(@NotNull String tile, @NotNull Frame frame, @NotNull MetalMaxRe metalMaxRe) {
        super(frame, metalMaxRe);
        init(tile, contentPane);
    }

    @Override
    protected void createLayout() {
        search.addActionListener(e -> {
            if (!e.getActionCommand().isEmpty()) {
                searchTexts.setRowFilter(new RowFilter<ListModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                        return entry.getValue(0).toString().contains(search.getText());
                    }
                });
            } else {
                searchTexts.setRowFilter(null);
            }
        });

        List<String> texts = new ArrayList<>();
        ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);
        for (Map.Entry<Integer, List<TextBuilder>> entry : textEditor.getPages().entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                texts.add(String.format("%02X%02X %s", entry.getKey(), i, entry.getValue().get(i).toText()));
            }
        }
        searchTexts.setListData(texts.toArray(new String[0x00]));
    }

    public JXSearchField getSearch() {
        return search;
    }

    public JXList getSearchTexts() {
        return searchTexts;
    }
}
