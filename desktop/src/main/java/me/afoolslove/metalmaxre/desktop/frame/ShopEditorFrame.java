package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.computer.shop.IShopEditor;
import me.afoolslove.metalmaxre.editors.computer.shop.VendorItemList;
import me.afoolslove.metalmaxre.editors.items.IItemEditor;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ShopEditorFrame extends AbstractEditorFrame {
    private JPanel contentPane;
    private org.jdesktop.swingx.JXList shops;
    private org.jdesktop.swingx.JXList vendors;
    private org.jdesktop.swingx.JXList shopList;
    private JButton shopItemRemove;
    private JButton shopItemAdd;
    private JComboBox<String> shopItem;
    private JComboBox<String> vendor00;
    private JComboBox<String> vendor02;
    private JComboBox<String> vendor04;
    private JComboBox<String> vendor01;
    private JComboBox<String> vendor03;
    private JComboBox<String> vendor05;
    private JComboBox<String> vendorAward;
    private JButton 保存修改Button;

    public ShopEditorFrame(@NotNull Frame frame, @NotNull MetalMaxRe metalMaxRe) {
        super(frame, metalMaxRe);
        init("商店编辑器", contentPane);
    }


    @Override
    protected void createLayout() {
        IShopEditor shopEditor = getMetalMaxRe().getEditorManager().getEditor(IShopEditor.class);

        String[] hexShops = new String[shopEditor.getShopLists().size()];
        for (int i = 0; i < hexShops.length; i++) {
            hexShops[i] = String.format("%02X", i);
        }
        shops.setListData(hexShops);
        shops.addListSelectionListener(e -> {
            ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);
            int index = Integer.parseInt(shops.getSelectedValue().toString(), 16);

            shopList.setListData(shopEditor.getShopList(index).stream()
                    .map(m -> String.format("%02X %s", m, textEditor.getItemName(m & 0xFF)))
                    .toList().toArray(new String[0x00]));
        });


        String[] hexVendors = new String[shopEditor.getVendorMaxCount()];
        for (int i = 0; i < hexVendors.length; i++) {
            hexVendors[i] = String.format("%02X", i);
        }
        vendors.setListData(hexVendors);
        vendors.addListSelectionListener(e -> {
            int index = Integer.parseInt(vendors.getSelectedValue().toString(), 16);
            VendorItemList vendorItemList = shopEditor.getVendorItemList(index);
            vendor00.setSelectedIndex(vendorItemList.get(0x00).getItem() & 0xFF);
            vendor01.setSelectedIndex(vendorItemList.get(0x01).getItem() & 0xFF);
            vendor02.setSelectedIndex(vendorItemList.get(0x02).getItem() & 0xFF);
            vendor03.setSelectedIndex(vendorItemList.get(0x03).getItem() & 0xFF);
            vendor04.setSelectedIndex(vendorItemList.get(0x04).getItem() & 0xFF);
            vendor05.setSelectedIndex(vendorItemList.get(0x05).getItem() & 0xFF);
            vendorAward.setSelectedIndex(vendorItemList.getAward() & 0xFF);
        });

        ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);
        IItemEditor iItemEditor = getMetalMaxRe().getEditorManager().getEditor(IItemEditor.class);

        List<String> items = new ArrayList<>();
        for (int i = 0; i < iItemEditor.getItems().size(); i++) {
            items.add(String.format("%02X %s", i, textEditor.getItemName(i)));
        }
        String[] itemsObj = items.toArray(new String[0]);
        // 得到焦点后全选文本
        FocusAdapter focusSelectAll = new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (e.getComponent() instanceof JTextField textField) {
                    textField.selectAll();
                }
            }
        };
        // 为装备和道具可以输入id后直接设置
        for (JComboBox<String> comboBox : List.of(
                vendor00, vendor01,
                vendor02, vendor03,
                vendor04, vendor05,
                vendorAward
        )) {
            comboBox.setModel(new DefaultComboBoxModel<>(itemsObj));
            comboBox.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        // 按下回车截取前两个字符作为为十六进制转换为数字并索引到该位置
                        String hexIndex = comboBox.getEditor().getItem().toString();
                        hexIndex = hexIndex.trim();
                        int index = comboBox.getSelectedIndex();
                        try {
                            index = Integer.parseInt(hexIndex, 16);
                        } catch (NumberFormatException ignored) {
                        }
                        // 不能太小，不能太大
                        index = Math.max(0x00, index);
                        index = Math.min(index, comboBox.getModel().getSize() - 1);
                        comboBox.setSelectedIndex(index);
                    }
                }
            });
            comboBox.getEditor().getEditorComponent().addFocusListener(focusSelectAll);
        }
    }
}
