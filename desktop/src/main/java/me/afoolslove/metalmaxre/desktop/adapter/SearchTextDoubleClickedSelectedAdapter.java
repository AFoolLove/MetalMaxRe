package me.afoolslove.metalmaxre.desktop.adapter;

import me.afoolslove.metalmaxre.desktop.frame.SearchTextFrame;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * 文本搜索使用
 * <p>
 * 双击获取文本，并关闭搜索界面
 */
public class SearchTextDoubleClickedSelectedAdapter extends MouseAdapter {

    private final SearchTextFrame searchTextFrame;
    private final Consumer<String> result;

    public SearchTextDoubleClickedSelectedAdapter(SearchTextFrame searchTextFrame, Consumer<String> result) {
        this.searchTextFrame = searchTextFrame;
        this.result = result;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            String selectedText = searchTextFrame.getSearchTexts().getSelectedValue().toString();

            int confirmDialog = JOptionPane.showConfirmDialog(searchTextFrame,
                    selectedText, "选择文本", JOptionPane.YES_NO_OPTION);

            if (confirmDialog == JOptionPane.YES_OPTION) {
                // 关闭窗口
                searchTextFrame.setVisible(false);

                result.accept(selectedText);
            }
        }
    }
}
