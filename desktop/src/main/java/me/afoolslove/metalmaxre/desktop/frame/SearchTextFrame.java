package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.desktop.adapter.UpdateDocumentListener;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import me.afoolslove.metalmaxre.editors.text.TextBuilder;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXSearchField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class SearchTextFrame extends AbstractEditorFrame {
    private JPanel contentPane;
    private JXList searchTexts;
    private JXSearchField search;
    private JCheckBox patternSearch;

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
        search.getDocument().addDocumentListener(new UpdateDocumentListener() {
            /**
             * 红色
             */
            private final DefaultHighlighter.DefaultHighlightPainter redHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

            @Override
            public void update(Type type, DocumentEvent e) {
                if (!patternSearch.isSelected()) {
                    return;
                }
                Highlighter highlighter = search.getHighlighter();
                highlighter.removeAllHighlights();
                try {
                    Pattern.compile(search.getText());
                } catch (PatternSyntaxException ex) {
                    // 错误的正则表达式
                    try {
                        highlighter.addHighlight(0, search.getText().length(), redHighlightPainter);
                    } catch (BadLocationException ignored) {
                    }
                }
            }
        });
        ActionListener filter = e -> {
            if (!e.getActionCommand().isEmpty()) {
                if (patternSearch.isSelected()) {
                    if (search.getHighlighter().getHighlights().length != 0x00) {
                        // 正则表达式错误，不进行过滤
                        return;
                    }
                    searchTexts.setRowFilter(new RowFilter<ListModel, Integer>() {
                        final Pattern pattern = Pattern.compile(search.getText());

                        @Override
                        public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                            return pattern.matcher(entry.getValue(0).toString()).find();
                        }
                    });
                } else {
                    searchTexts.setRowFilter(new RowFilter<ListModel, Integer>() {
                        @Override
                        public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                            return entry.getValue(0).toString().contains(search.getText());
                        }
                    });
                }
            } else {
                searchTexts.setRowFilter(null);
            }
        };
        search.addActionListener(filter);
        patternSearch.addChangeListener(e -> filter.actionPerformed(new ActionEvent(search, 0, search.getText())));

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
