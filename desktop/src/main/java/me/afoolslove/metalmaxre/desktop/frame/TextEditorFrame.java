package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.desktop.adapter.SearchTextDoubleClickedSelectedAdapter;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import me.afoolslove.metalmaxre.editors.text.TextBuilder;
import me.afoolslove.metalmaxre.editors.text.WordBank;
import me.afoolslove.metalmaxre.editors.text.action.SpriteAction;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXSearchField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class TextEditorFrame extends AbstractEditorFrame {
    private JPanel contentPane;
    private JXList textPages;
    private JButton save;
    private JXList textPage;
    private JTextArea textAreaAfter;
    private JXEditorPane textBytes;
    private JLabel pageCapacity;
    private JLabel pageCount;
    private JLabel currentCount;
    private JXSearchField searchText;
    private JXList textWord;
    private JXSearchField textWordFilter;
    private JTextArea textAreaBefore;
    private JButton spriteMoveUP;
    private JButton spriteMoveDown;
    private JButton spriteMoveLeft;
    private JButton spriteMoveRight;
    private JButton spriteLockUP;
    private JButton spriteLockDown;
    private JButton spriteLockLeft;
    private JButton spriteLockRight;
    private JButton referenceText;
    private JButton select;
    private JList<SpriteAction> spriteActions;
    private JButton saveName1;
    private JButton saveName2;
    private JButton currentName;
    private JButton playerName1;
    private JButton playerName2;
    private JButton playerName3;
    private JButton nameWrap;
    private JButton confirmNameWrap;
    private JButton wrap;
    private JComboBox<String> speakerValue;
    private JButton speaker;
    private JButton restoreTextSpeed;
    private JButton textSleep;
    private JButton addSpriteActions;
    private JButton clearSpriteActions;
    private JSlider textSpeedValue;
    private JButton textSpeed;
    private JSlider textSleepValue;
    private JButton removeSelectedSpriteActions;

    public TextEditorFrame(@NotNull Frame frame, @NotNull MetalMaxRe metalMaxRe) {
        super(frame, metalMaxRe);
        init("文本编辑器", contentPane);
    }

    @Override
    protected void createMenu(@NotNull JMenuBar menuBar) {
        JMenuItem search = new JMenuItem("搜索");
        search.addActionListener(e -> {
            SearchTextFrame searchTextFrame = new SearchTextFrame(this, getMetalMaxRe());
            searchTextFrame.pack();
            // 右键定位到选中的位置
            searchTextFrame.getSearchTexts().addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        int index = searchTextFrame.getSearchTexts().locationToIndex(e.getPoint());
//                    searchTexts.setSelectedIndex(index);
                        if (index != -1) {
                            String text = searchTextFrame.getSearchTexts().getElementAt(index).toString();
                            getTextPages().setSelectedIndex(Integer.parseInt(text.substring(0, 2), 16));
                            getTextPage().setSelectedIndex(Integer.parseInt(text.substring(2, 4), 16));

                            // 显示选中的位置
                            getTextPages().ensureIndexIsVisible(getTextPages().getSelectedIndex());
                            getTextPage().scrollRectToVisible(getTextPage().getCellBounds(
                                    getTextPage().getMinSelectionIndex(),
                                    getTextPage().getMaxSelectionIndex()
                            ));

                            if (!isActive()) {
                                setState(ICONIFIED);
                                setState(NORMAL);
                            }
                        }
                    }
                }
            });

            searchTextFrame.setVisible(true);

        });
        menuBar.add(search);
    }

    @Override
    protected void createLayout() {
        searchText.addActionListener(e -> {
            ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);

            List<Integer> containsTextPages = new ArrayList<>();

            for (Map.Entry<Integer, List<TextBuilder>> entry : textEditor.getPages().entrySet()) {
                for (int i = 0; i < entry.getValue().size(); i++) {
                    TextBuilder textBuilder = entry.getValue().get(i);
                    if (textBuilder.toText().contains(e.getActionCommand())) {
                        containsTextPages.add(entry.getKey());
                        break;
                    }
                }
            }
            if (searchText.getText().isEmpty()) {
                textPages.setRowFilter(null);
            } else {
                textPages.setRowFilter(new RowFilter<ListModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                        return containsTextPages.contains(entry.getIdentifier());
                    }
                });
            }
        });

        String[] pages = new String[0x16];
        for (int i = 0; i < pages.length; i++) {
            pages[i] = String.format("%02X", i);
        }
        textPages.setListData(pages);


        textPages.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() || textPages.getSelectedIndex() == -1) {
                return;
            }

            ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);
            List<TextBuilder> indexPage = textEditor.getPage(getPageIndex());

            List<String> textIndexes = new ArrayList<>();
            for (int i = 0; i < indexPage.size(); i++) {
//                if (!searchText.getText().isEmpty()) {
//                    TextBuilder textBuilder = indexPage.get(i);
//                    String text = textBuilder.toText();
//                    if (!text.contains(searchText.getText())) {
//                        continue;
//                    }
//                }
                textIndexes.add(String.format("%02X", i));
            }

            int currentLength = indexPage.parallelStream().mapToInt(TextBuilder::length).sum();
            pageCapacity.setText(String.format("%04d/%04d", currentLength, textEditor.getTextAddresses().get(textPages.getSelectedIndex()).length()));
            pageCount.setText(String.format("%02d/255", indexPage.size()));


            int selectedIndex = textPage.getSelectedIndex();
            if (selectedIndex != -1) {
                textPage.removeSelectionInterval(0, textPage.getModel().getSize());
            }

            DefaultListModel<String> model = (DefaultListModel<String>) textPage.getModel();
            model.clear();
            // 添加后自动过滤
            model.addAll(textIndexes);
        });
        textAreaAfter.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSpace(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSpace(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSpace(e);
            }

            // 红色
            private final DefaultHighlighter.DefaultHighlightPainter redHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);

            private void updateSpace(DocumentEvent e) {
                if (textPages.isSelectionEmpty() || textPage.isSelectionEmpty()) {
                    // 没选中不更新
                    return;
                }
                try {
                    Document document = e.getDocument();
                    if (e.getType() == DocumentEvent.EventType.INSERT || e.getType() == DocumentEvent.EventType.CHANGE) {
                        String insertText = document.getText(e.getOffset(), e.getLength());
                        if (insertText.length() == 1 &&
                            ("[]{}".contains(insertText))) {
                            return;
                        }
                    } else if (e.getType() == DocumentEvent.EventType.REMOVE) {
                        if (e.getLength() == 1) {
                            if (e.getOffset() > 0) {
                                // 如果被删除的字符上一个是\，一并删除
                                char preChar = document.getText(e.getOffset() - 1, 1).charAt(0);
                                if (preChar == '\\') {
                                    // 暂时不进行操作
                                    return;
                                }
                            }
                        }
                    }

                    Highlighter highlighter = textAreaAfter.getHighlighter();
                    highlighter.removeAllHighlights();

                    String text = textAreaAfter.getText();
                    text = text.replaceAll("\\\\.", "  ");
                    text = text.replaceAll("[\\[\\]{}]", " ");

                    int offset = 0;
                    char[] chars = text.toCharArray();
                    for (int i = 0; i < chars.length; i++, offset++) {
                        char ch = chars[i];
                        if (ch == ' ') {
                            continue;
                        }
                        Object temp = WordBank.getValue(ch);
                        if (temp == null) {
                            // 红色显示
                            try {
                                highlighter.addHighlight(offset, i + 1, redHighlightPainter);
                            } catch (BadLocationException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }

                    List<TextBuilder> selectedTexts = TextBuilder.fromTexts(textAreaAfter.getText());

                    // 转换为字节显示
                    StringBuilder byteText = new StringBuilder();
                    for (TextBuilder textBuilder : selectedTexts) {
                        byteText.append(NumberR.toPlainHexString(textBuilder.toByteArray()));
                    }
                    textBytes.setText(byteText.toString());

                    ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);
                    int maxSize = Integer.parseInt(currentCount.getToolTipText());

                    int pagesSelectedIndex = textPages.convertIndexToModel(textPages.getSelectedIndex());
                    // 该页的剩余空间
                    int pageCapacityRem = 0;
                    int[] selectedIndices = convertSelectedIndicesToModel();
                    List<TextBuilder> page = new ArrayList<>(textEditor.getPage(pagesSelectedIndex));
                    for (int i = 0, len = Math.min(selectedIndices.length, selectedTexts.size()); i < len; i++) {
                        page.set(selectedIndices[i], selectedTexts.get(i));
                    }

                    for (TextBuilder textBuilder : page) {
                        pageCapacityRem += textBuilder.length();
                    }

                    int pageCapacity = textEditor.getTextAddresses().get(pagesSelectedIndex).length();
                    currentCount.setText(String.format("当前/可用 字节：%7d/%-7d %7d/%-7d",
                            byteText.length() / 2, maxSize,
                            pageCapacity - pageCapacityRem, pageCapacity
                    ));

                    if (textAreaAfter.hasFocus()) {
                        if (!selectedTexts.isEmpty()) {
                            textAreaBefore.setText(null);
                            StringBuilder currentText = new StringBuilder();
                            boolean hasF9 = false;
                            for (TextBuilder textBuilder : selectedTexts) {
                                currentText.append(textBuilder.toText());
                                hasF9 = textBuilder.has9F();
                                if (hasF9) {
                                    currentText.append('\n');
                                } else {
                                    currentText.append("\r\n");
                                }
                            }

                            if (!hasF9) {
                                textAreaBefore.setText(currentText.substring(0, currentText.length() - 2));
                            } else {
                                textAreaBefore.setText(currentText.toString());
                            }
                        }
                    }

                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });

        textPage.setModel(new DefaultListModel<String>());
        textPage.setRowFilter(new RowFilter<ListModel, Integer>() {
            @Override
            public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                if (searchText.getText().isEmpty()) {
                    return true;
                }
                ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);
                List<TextBuilder> indexPage = textEditor.getPage(getPageIndex());
                return indexPage.get(entry.getIdentifier()).toText().contains(searchText.getText());
            }
        });
        textPage.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() || textPages.getSelectedIndex() == -1 || textPage.getSelectedIndex() == -1) {
                return;
            }
            // 已选中的文本项
            int[] selectedIndices = convertSelectedIndicesToModel();

            ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);
            List<TextBuilder> indexPage = textEditor.getPage(getPageIndex());

            // 将被选中的文本项放入集合中
            List<TextBuilder> selectedTexts = new ArrayList<>();
            for (int selectedIndex : selectedIndices) {
                selectedTexts.add(indexPage.get(selectedIndex));
            }

            // 转换为字节显示
            StringBuilder byteText = new StringBuilder();
            for (TextBuilder textBuilder : selectedTexts) {
                byteText.append(NumberR.toPlainHexString(textBuilder.toByteArray()));
            }
            textBytes.setText(byteText.toString());

            currentCount.setToolTipText(Integer.toString(byteText.length() / 2));
//            currentCount.setText(String.format("%d/%d", byteText.length() / 2, byteText.length() / 2));

            textAreaAfter.setText(null);
            StringBuilder selectedText = new StringBuilder();

            boolean hasF9 = false;
            for (TextBuilder textBuilder : selectedTexts) {
                selectedText.append(textBuilder.toText());
                hasF9 = textBuilder.has9F();

                if (hasF9) {
                    selectedText.append('\n');
                } else {
                    selectedText.append("\r\n");
                }
            }

            if (!hasF9) {
                textAreaBefore.setText(selectedText.substring(0, selectedText.length() - 2));
            } else {
                textAreaBefore.setText(selectedText.toString());
            }
            textAreaAfter.setText(textAreaBefore.getText());
        });

        textAreaAfter.addKeyListener(new KeyAdapter() {
            final UndoManager undoManager = new UndoManager();

            {
                textAreaAfter.getDocument().addUndoableEditListener(undoManager);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
                    if (e.isShiftDown()) {
                        if (undoManager.canRedo()) {
                            undoManager.redo();
                        }
                    } else {
                        if (undoManager.canUndo()) {
                            undoManager.undo();
                        }
                    }
                }
            }
        });


        textWordFilter.addActionListener(e -> {
            if (e.getActionCommand().isEmpty()) {
                textWord.setRowFilter(null);
            } else {
                char[] chars = e.getActionCommand().toCharArray();
                final String[] charStrings = new String[chars.length];
                for (int i = 0; i < chars.length; i++) {
                    charStrings[i] = Character.toString(chars[i]);
                }
                textWord.setRowFilter(new RowFilter<ListModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                        for (String ch : charStrings) {
                            if (entry.getStringValue(0).contains(ch)) {
                                return true;
                            }
                        }
                        return false;
                    }
                });
            }


        });

        new Thread(() -> {
            // 设置字库和搜索字库
            List<String> words = new ArrayList<>();
            for (Map.Entry<Character, ?> word : WordBank.ALL_FONTS) {
                StringBuilder builder = new StringBuilder();
                Object value = word.getValue();
                if (value instanceof byte[] bytes) {
                    for (byte b : bytes) {
                        builder.append(String.format("%02X", b));
                    }
                } else {
                    builder.append(String.format("%02X", ((byte) value)));
                }
                builder.append(' ');
                builder.append(word.getKey());
                words.add(builder.toString());
            }
            textWord.setListData(words.stream().toList().toArray());
        }).start();

        save.addActionListener(e -> {
            if (textPages.getSelectedIndex() != -1) {
                int[] selectedIndices = convertSelectedIndicesToModel();

                ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);
                List<TextBuilder> indexPage = textEditor.getPage(getPageIndex());

                List<TextBuilder> textBuilders = TextBuilder.fromTexts(textAreaAfter.getText());

                if (selectedIndices.length != textBuilders.size()) {
                    // 将差数转换为正数
                    int count = Math.abs(selectedIndices.length - textBuilders.size());

                    if (selectedIndices.length > textBuilders.size()) {
                        // 文本项数量变小
                        int option = JOptionPane.showOptionDialog(this,
                                String.format("""
                                        生成了小于选中数量的文本段 %d/%d
                                        保存：后面的文本会向前移动，出现文本错位的情况。
                                        替换：使用[9F]替换空白文本，保持文本位置不变。\
                                        """, textBuilders.size(), selectedIndices.length),
                                "文本段数量变小",
                                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
                                new String[]{"保存", "[9F]替换", "取消"}, "取消");
                        if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                            // 取消或关闭窗口不执行保存
                            return;
                        }
                        if (option == JOptionPane.NO_OPTION) {
                            // 使用[9F]替换空白文本
                            for (int i = 0; i < count; i++) {
                                textBuilders.add(new TextBuilder(true));
                            }
                        } else {
                            // 删除多出来的文本段
                            int[] tmpSelectedIndices = textPage.getSelectedIndices();
                            DefaultListModel<String> model = (DefaultListModel<String>) textPage.getModel();
                            for (int i = 0; i < count; i++) {
                                int index = tmpSelectedIndices[selectedIndices.length - 1 - i];
                                indexPage.remove(index);
                                model.remove(index);
                            }
                        }
                    } else {
                        // 文本项数量变大
                        int option = JOptionPane.showOptionDialog(this,
                                String.format("""
                                        生成了大于选中数量的文本段 %d/%d
                                        保存：多余的文本会丢失。
                                        追加：多余的文本会保存到当前文本页的末尾。
                                        """, textBuilders.size(), selectedIndices.length),
                                "文本项数量变大",
                                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
                                new String[]{"保存", "追加", "取消"}, "取消");
                        if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
                            // 取消或关闭窗口不执行保存
                            return;
                        }

                        if (option == JOptionPane.NO_OPTION) {
                            // 添加多出来的文本段
                            indexPage.addAll(textBuilders.subList(selectedIndices.length, textBuilders.size()));
                            // 更新列表
                            DefaultListModel<String> model = (DefaultListModel<String>) textPage.getModel();
                            for (int i = 0; i < count; i++) {
                                model.addElement((String.format("%02X", model.getSize())));
                            }
                        }
                    }
                }

                for (int i = 0, len = Math.min(selectedIndices.length, textBuilders.size()); i < len; i++) {
                    indexPage.set(selectedIndices[i], textBuilders.get(i));
                }

                int currentLength = indexPage.parallelStream().mapToInt(TextBuilder::length).sum();
                pageCapacity.setText(String.format("%04d/%04d", currentLength, textEditor.getTextAddresses().get(getPageIndex()).length()));

                // 重新选中
                int maxSelectionIndex = textPage.getMaxSelectionIndex();
                textPage.removeSelectionInterval(textPage.getMinSelectionIndex(), maxSelectionIndex);
                convertSelectedIndicesToModel(selectedIndices);
                textPage.setSelectedIndices(selectedIndices);
                if (textPage.isSelectionEmpty()) {
                    textPage.setSelectedIndex(Math.min(maxSelectionIndex, textPage.getModel().getSize()));
                }
            }
        });


        // 基本变量

        // 名字
        // 存档一 名字
        saveName1.addActionListener(e -> insertText("[FC3F]", false));
        // 存档二 名字
        saveName2.addActionListener(e -> insertText("[FC40]", false));
        // 当前玩家名字
        currentName.addActionListener(e -> insertText("[E8]", false));
        // 3个玩家名字
        playerName1.addActionListener(e -> insertText("[FD00]", false));
        playerName2.addActionListener(e -> insertText("[FD01]", false));
        playerName3.addActionListener(e -> insertText("[FD02]", false));

        // 对话

        // 更换说话人的名称
        speaker.addActionListener(e -> {
            String value = Objects.toString(speakerValue.getSelectedItem()).substring(0, 2);
            insertText(String.format("[F0%s]", value), false);
        });
        // 换行
        wrap.addActionListener(e -> insertText("\t", false));
        // 带名字换行
        nameWrap.addActionListener(e -> insertText("[E4]", false));
        // 确认并带名字换行
        confirmNameWrap.addActionListener(e -> insertText("[FE]", false));
        // 文字显示速度
        textSpeed.addActionListener(e -> insertText(String.format("[EE%02X]", textSpeedValue.getValue()), false));
        // 恢复文字显示速度
        restoreTextSpeed.addActionListener(e -> insertText("[EE00]", false));
        // 等待一段时间后再继续显示文字
        textSleep.addActionListener(e -> insertText(String.format("[F1%02X]", textSleepValue.getValue()), false));

        // 其它

        // 引用其它文本
        referenceText.addActionListener(e -> {
            SearchTextFrame searchTextFrame = new SearchTextFrame("引用文本", this, getMetalMaxRe());
            searchTextFrame.pack();
            // 双击选择
            searchTextFrame.getSearchTexts().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        String selectedText = searchTextFrame.getSearchTexts().getSelectedValue().toString();

                        int confirmDialog = JOptionPane.showConfirmDialog(searchTextFrame,
                                selectedText, "引用该文本", JOptionPane.YES_NO_OPTION);

                        if (confirmDialog == JOptionPane.YES_OPTION) {
                            // 文本页
                            String page = selectedText.substring(0, 2);
                            // 下标
                            String index = selectedText.substring(2, 4);

                            selectedText = switch (page) {
                                case "13" -> String.format("[EA%s]", index);
                                case "14" -> String.format("[EC%s]", index);
                                case "0C" -> String.format("[F0%s]", index);
                                case "09" -> String.format("[F2%s]", index);
                                case "11" -> String.format("[F3%s]", index);
                                default -> String.format("[F7%s%s]{%s}", index, page,
                                        selectedText.substring(5)
                                );
                            };

                            insertText(selectedText, true);
                            // 关闭窗口
                            searchTextFrame.setVisible(false);
                        }
                    }
                }
            });
            searchTextFrame.setVisible(true);
        });
        // 玩家进行选择
        select.addActionListener(e -> {
            selectText("选择[是]后显示的文本", select, yes -> {
                selectText("选择[否]显示的文本", select, no -> {
                    textAreaAfter.replaceSelection(String.format("[EB%s%s]",
                            yes.substring(2, 4),
                            no.substring(2, 4)
                    ));
                });
            });
        });


        // 设置名字列表
        ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);
        List<TextBuilder> textBuilders = textEditor.getPage(0x0C);
        String[] speakers = new String[textBuilders.size()];
        for (int i = 0; i < speakers.length; i++) {
            speakers[i] = String.format("%02X %s", i, textBuilders.get(i).toText());
        }
        speakerValue.setModel(new DefaultComboBoxModel<>(speakers));


        // 操作精灵

        DefaultListModel<SpriteAction> spriteActionsModel = (DefaultListModel<SpriteAction>) spriteActions.getModel();
        // 精灵移动一格
        spriteMoveUP.addActionListener(e -> spriteActionsModel.addElement(SpriteAction.MOVE_UP));
        spriteMoveDown.addActionListener(e -> spriteActionsModel.addElement(SpriteAction.MOVE_DOWN));
        spriteMoveLeft.addActionListener(e -> spriteActionsModel.addElement(SpriteAction.MOVE_LEFT));
        spriteMoveRight.addActionListener(e -> spriteActionsModel.addElement(SpriteAction.MOVE_RIGHT));
        // 改变精灵朝向
        spriteLockUP.addActionListener(e -> spriteActionsModel.addElement(SpriteAction.LOCK_UP));
        spriteLockDown.addActionListener(e -> spriteActionsModel.addElement(SpriteAction.LOCK_DOWN));
        spriteLockLeft.addActionListener(e -> spriteActionsModel.addElement(SpriteAction.LOCK_LEFT));
        spriteLockRight.addActionListener(e -> spriteActionsModel.addElement(SpriteAction.LOCK_RIGHT));
        // 移除选中的精灵操作
        removeSelectedSpriteActions.addActionListener(e -> {
            // 当前选中的精灵操作
            int[] selectedIndices = spriteActions.getSelectedIndices();
            for (int selectedIndex : selectedIndices) {
                spriteActionsModel.remove(selectedIndex);
            }
        });
        // 清空精灵操作列表
        clearSpriteActions.addActionListener(e -> spriteActionsModel.clear());
        // 添加当前的精灵操作列表到文本中
        addSpriteActions.addActionListener(e -> {
            Object[] objects = spriteActionsModel.toArray();
            if (objects.length == 0) {
                return;
            }
            StringBuilder actions = new StringBuilder();
            actions.append('[');
            for (Object action : objects) {
                if (action instanceof SpriteAction spriteAction) {
                    actions.append(String.format("F5%02X", spriteAction.getAction()));
                }
            }
            actions.append(']');
            insertText(actions.toString(), true);
        });
    }

    private void insertText(String text, boolean selected) {
        textAreaAfter.replaceSelection(text);
        if (selected) {
            int caretPosition = textAreaAfter.getCaretPosition();
            textAreaAfter.select(caretPosition, caretPosition + text.length());
        }
    }

    /**
     * 将过滤后的索引转换为实际所在的索引
     */
    public void convertSelectedIndicesToModel(int[] selectedIndices) {
        if (textPage.getSearchable() != null) {
            for (int i = 0; i < selectedIndices.length; i++) {
                if (selectedIndices[i] < textPage.getModel().getSize()) {
                    selectedIndices[i] = textPage.convertIndexToModel(selectedIndices[i]);
                }
            }
        }
    }

    /**
     * 将实际的索引转换为过滤后的索引
     */
    public void convertModelToSelectedIndices(int[] modelIndices) {
        if (textPage.getSearchable() != null) {
            for (int i = 0; i < modelIndices.length; i++) {
                modelIndices[i] = textPage.convertIndexToView(modelIndices[i]);
            }
        }
    }

    public int[] convertSelectedIndicesToModel() {
        int[] selectedIndices = textPage.getSelectedIndices();
        convertSelectedIndicesToModel(selectedIndices);
        return selectedIndices;
    }

    public JXList getTextPages() {
        return textPages;
    }

    public JXList getTextPage() {
        return textPage;
    }

    public int getPageIndex(int selectedIndex) {
        if (selectedIndex == -1) {
            return -1;
        }
        String stringAt = textPages.getStringAt(selectedIndex);
        return Integer.parseInt(stringAt, 16);
    }

    public int getPageIndex() {
        return getPageIndex(textPages.getSelectedIndex());
    }

    /**
     * 打开搜索界面选择一个文本
     *
     * @param tile   标题
     * @param source 控件
     * @param result 选择的文本
     */
    public void selectText(String tile, Object source, Consumer<String> result) {
        SearchTextFrame searchTextFrame = new SearchTextFrame(tile, this, getMetalMaxRe());
        searchTextFrame.pack();

        // 将过滤器设置为只能搜索当前页的文本段
        JXSearchField search = searchTextFrame.getSearch();
        JXList searchTexts = searchTextFrame.getSearchTexts();
        // 清除初始过滤器
        for (ActionListener actionListener : search.getActionListeners()) {
            search.removeActionListener(actionListener);
        }
        // 设置新的过滤器
        ActionListener actionListener = e -> {
            // 当前文本页，十六进制
            String currentPage = String.format("%02X", textPages.convertIndexToModel(textPages.getSelectedIndex()));

            searchTexts.setRowFilter(new RowFilter<ListModel, Integer>() {
                @Override
                public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                    String s = entry.getValue(0).toString();
                    // 当前文本页
                    if (!Objects.equals(s.substring(0, 2), currentPage)) {
                        // 不是当前文本页
                        return false;
                    }

                    if (e.getActionCommand().isEmpty()) {
                        return true;
                    }
                    return s.contains(search.getText());
                }
            });
        };
        search.addActionListener(actionListener);
        actionListener.actionPerformed(new ActionEvent(source, 0, ""));

        // 双击选择
        searchTextFrame.getSearchTexts().addMouseListener(new SearchTextDoubleClickedSelectedAdapter(searchTextFrame, result));
        searchTextFrame.setVisible(true);
    }
}
