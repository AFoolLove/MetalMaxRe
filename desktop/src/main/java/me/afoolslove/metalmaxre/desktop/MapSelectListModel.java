package me.afoolslove.metalmaxre.desktop;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.map.IMapEditor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class MapSelectListModel extends DefaultListModel<String> implements DocumentListener {
    private final LinkedList<MetalMaxRe> metalMaxRes;
    private final JList<String> mapList;
    private final JTextField mapFilter;
    private final List<Integer> mapIds = new ArrayList<>();

    public MapSelectListModel(JList<String> mapList, LinkedList<MetalMaxRe> metalMaxRes, JTextField mapFilter) {
        this.mapList = mapList;
        this.metalMaxRes = metalMaxRes;
        this.mapFilter = mapFilter;

        mapFilter.getDocument().addDocumentListener(this);
    }

    @Override
    public int getSize() {
        if (metalMaxRes.isEmpty()) {
            return 0x01;
        }
        mapIds.clear();

        MetalMaxRe metalMaxRe = metalMaxRes.getFirst();
        IMapEditor mapEditor = metalMaxRe.getEditorManager().getEditor(IMapEditor.class);

        if (mapFilter == null || mapFilter.getText().isEmpty()) {
            // 不过滤
            IntStream.range(0, mapEditor.getMapMaxCount()).forEach(mapIds::add);
            return mapIds.size();
        }

        String filterText = mapFilter.getText().toUpperCase();
        for (int mapId = 0; mapId < mapEditor.getMapMaxCount(); mapId++) {
            String format = String.format("%02X", mapId);
            if (mapId == 0x00 || format.contains(filterText)) {
                mapIds.add(mapId);
            }
        }
        return mapIds.size();
    }

    @Override
    public String getElementAt(int index) {
        if (index == 0x00) {
            return "00";
        }
        return String.format("%02X", mapIds.get(index));
    }

    @Override
    public String get(int index) {
        return getElementAt(index);
    }


    public int getSelectMap() {
        if (mapList.getSelectedIndex() == -1) {
            return -1;
        }
        return mapIds.get(mapList.getSelectedIndex());
    }


    @Override
    public void insertUpdate(DocumentEvent e) {
        mapList.updateUI();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        mapList.updateUI();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        mapList.updateUI();
    }
}
