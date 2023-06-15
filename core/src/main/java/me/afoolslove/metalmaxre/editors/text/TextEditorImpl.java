package me.afoolslove.metalmaxre.editors.text;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文本编辑器
 * <p>
 * *暂不支持读取日文原版文本
 *
 * @author AFoolLove
 */
@Editor.TargetVersions
public class TextEditorImpl extends RomBufferWrapperAbstractEditor implements ITextEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextEditorImpl.class);
    private final Map<Integer, DataAddress> textAddresses;

    private final Map<DataAddress, List<TextBuilder>> text = new HashMap<>();

    public Map<DataAddress, List<TextBuilder>> getText() {
        return text;
    }

    public TextEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe, Map.ofEntries(
                Map.entry(0x14, DataAddress.from(0x0BE90 - 0x10, 0x0C00F - 0x10)), //-
                Map.entry(0x09, DataAddress.from(0x10010 - 0x10, 0x10128 - 0x10)), //-
                Map.entry(0x02, DataAddress.from(0x10129 - 0x10, 0x10DB2 - 0x10)), //-
                Map.entry(0x03, DataAddress.from(0x10DB3 - 0x10, 0x112F1 - 0x10)), //-
                Map.entry(0x04, DataAddress.from(0x112F2 - 0x10, 0x1157B - 0x10)), //-
                Map.entry(0x12, DataAddress.from(0x1157C - 0x10, 0x11932 - 0x10)), //-
                Map.entry(0x13, DataAddress.from(0x11933 - 0x10, 0x11A1F - 0x10)), //--
                Map.entry(0x00, DataAddress.from(0x11A20 - 0x10, 0x11F74 - 0x10)), //-
//                Map.entry(-1, DataAddress.from(0x11F75 - 0x10, 0x1200F - 0x10)), //-*
                Map.entry(0x0C, DataAddress.from(0x12010 - 0x10, 0x120DF - 0x10)), //-
                Map.entry(0x0D, DataAddress.from(0x120E0 - 0x10, 0x124ED - 0x10)), //-
                Map.entry(0x06, DataAddress.from(0x124EE - 0x10, 0x1331F - 0x10)), //-
                Map.entry(0x10, DataAddress.from(0x13320 - 0x10, 0x1400F - 0x10)), //-
                Map.entry(0x05, DataAddress.from(0x14010 - 0x10, 0x1600F - 0x10)), //-
                Map.entry(0x07, DataAddress.from(0x16010 - 0x10, 0x16BDC - 0x10)), //-
                Map.entry(0x0A, DataAddress.from(0x16BDD - 0x10, 0x1767F - 0x10)), //-
                Map.entry(0x0F, DataAddress.from(0x17680 - 0x10, 0x1800F - 0x10)), //-
                Map.entry(0x0E, DataAddress.from(0x18010 - 0x10, 0x1A00F - 0x10)), //-
                Map.entry(0x11, DataAddress.from(0x1F99A - 0x10, 0x2000F - 0x10)), //-
                Map.entry(0x01, DataAddress.from(0x21AF6 - 0x10, 0x21E80 - 0x10)), //-
                Map.entry(0x08, DataAddress.from(0x21E81 - 0x10, 0x2200F - 0x10)), //-
                Map.entry(0x0B, DataAddress.from(0x36010 - 0x10, 0x3800F - 0x10)), //-
                Map.entry(0x15, DataAddress.from(0x384B5 - 0x10, 0x3886D - 0x10))
        ));
    }

    public TextEditorImpl(@NotNull MetalMaxRe metalMaxRe, @NotNull Map<Integer, DataAddress> textAddresses) {
        super(metalMaxRe);
        this.textAddresses = textAddresses;
    }

    @Editor.Load
    public void onLoad() {
        textAddresses.values().parallelStream().forEach(textAddress -> {
            // 得到这段文本的数据长度
            final byte[] bytes = new byte[textAddress.length()];
            // 定位，读取
            getBuffer().get(textAddress, bytes);

            this.text.put(textAddress, TextBuilder.fromBytes(bytes, getCharMap()));
        });
    }

    @Editor.Apply
    public void onApply() {
        text.entrySet().parallelStream().forEach(entry -> {
            final int length = entry.getKey().length();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(length);
            // 将该段地址的文本转换为游戏使用的字节
            entry.getValue().stream().map(v -> v.toByteArray(getCharMap())).forEach(outputStream::writeBytes);
            byte[] bytes = outputStream.toByteArray();
            // 覆盖写入该段地址的文本
            getBuffer().put(entry.getKey(), bytes, 0, Math.min(bytes.length, length));

            if (bytes.length != length) {
                if (bytes.length < length) {
                    LOGGER.info("文本编辑器：{}-{} 剩余{}个字节",
                            NumberR.toHex(5, entry.getKey().getStartAddress()), NumberR.toHex(5, entry.getKey().getEndAddress()),
                            length - bytes.length);
                } else {
                    LOGGER.error("文本编辑器：{}-{} 溢出{}个字节未写入",
                            NumberR.toHex(5, entry.getKey().getStartAddress()), NumberR.toHex(5, entry.getKey().getEndAddress()),
                            bytes.length - length);
                }
            }
        });
    }

    @Override
    public Map<Integer, List<TextBuilder>> getPages() {
        Map<Integer, List<TextBuilder>> map = new HashMap<>();
        for (Map.Entry<Integer, DataAddress> entry : getTextAddresses().entrySet()) {
            map.put(entry.getKey(), text.get(entry.getValue()));
        }
        return map;
    }

    @Override
    public List<TextBuilder> getPage(int page) {
        return text.get(getTextAddresses().get(page));
    }

    @Override
    public String getTownName(int townId) {
        return text.get(getTownNameAddress()).get(0x30 + townId).toText(getCharMap());
    }

    @Override
    public String getItemName(int itemId) {
        List<TextBuilder> textBuilders = text.get(getItemNameAddress());
        if (itemId >= textBuilders.size()) {
            return "null";
        }
        return textBuilders.get(itemId).toText(getCharMap());
    }

    @Override
    public String getMonsterName(int monsterId) {
        List<TextBuilder> textBuilders = text.get(getMonsterNameAddress());
        if (monsterId >= textBuilders.size()) {
            return "null";
        }
        return textBuilders.get(monsterId).toText(getCharMap());
    }

    @Override
    public void setTownName(int townId, String newName) {
        List<TextBuilder> textBuilders = text.get(getTownNameAddress());
        if ((0x30 + townId) >= textBuilders.size()) {
            return;
        }
        textBuilders.set(0x30 + townId, new TextBuilder(new Text(newName)));
    }

    @Override
    public void setItemName(int itemId, String newName) {
        List<TextBuilder> textBuilders = text.get(getItemNameAddress());
        if (itemId >= textBuilders.size()) {
            return;
        }
        textBuilders.set(itemId, new TextBuilder(new Text(newName)));
    }

    @Override
    public void setMonsterName(int monsterId, String newName) {
        List<TextBuilder> textBuilders = text.get(getItemNameAddress());
        if (monsterId >= textBuilders.size()) {
            return;
        }
        textBuilders.set(monsterId, new TextBuilder(new Text(newName)));
    }

    @Override
    public Map<Integer, DataAddress> getTextAddresses() {
        return textAddresses;
    }

    @Override
    public DataAddress getTownNameAddress() {
        return getTextAddresses().get(0x0D);
    }

    @Override
    public DataAddress getItemNameAddress() {
        return getTextAddresses().get(0x00);
    }

    @Override
    public DataAddress getMonsterNameAddress() {
        return getTextAddresses().get(0x01);
    }

    @Editor.TargetVersion("japanese")
    public static class JPTextEditorImpl extends TextEditorImpl {

        public JPTextEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe, Map.ofEntries(
                    Map.entry(0x14, DataAddress.from(0x0BE90 - 0x10, 0x0C00F - 0x10)), //-
                    Map.entry(0x09, DataAddress.from(0x10010 - 0x10, 0x10128 - 0x10)), //-
                    Map.entry(0x02, DataAddress.from(0x10129 - 0x10, 0x10DB2 - 0x10)), //-
                    Map.entry(0x03, DataAddress.from(0x10DB3 - 0x10, 0x112F1 - 0x10)), //-
                    Map.entry(0x04, DataAddress.from(0x112F2 - 0x10, 0x1157B - 0x10)), //-
                    Map.entry(0x12, DataAddress.from(0x1157C - 0x10, 0x11932 - 0x10)), //-
                    Map.entry(0x13, DataAddress.from(0x11933 - 0x10, 0x11A1F - 0x10)), //--
                    Map.entry(0x00, DataAddress.from(0x11A26 - 0x10, 0x11F74 - 0x10)), //-
//                Map.entry(-1, DataAddress.from(0x11F75 - 0x10, 0x1200F - 0x10)), //-*
                    Map.entry(0x0C, DataAddress.from(0x12010 - 0x10, 0x120DF - 0x10)), //-
                    Map.entry(0x0D, DataAddress.from(0x120E0 - 0x10, 0x124ED - 0x10)), //-
                    Map.entry(0x06, DataAddress.from(0x124EE - 0x10, 0x1331F - 0x10)), //-
                    Map.entry(0x10, DataAddress.from(0x13320 - 0x10, 0x1400F - 0x10)), //-
                    Map.entry(0x05, DataAddress.from(0x14010 - 0x10, 0x1600F - 0x10)), //-
                    Map.entry(0x07, DataAddress.from(0x16010 - 0x10, 0x16BDC - 0x10)), //-
                    Map.entry(0x0A, DataAddress.from(0x16BDD - 0x10, 0x1767F - 0x10)), //-
                    Map.entry(0x0F, DataAddress.from(0x17680 - 0x10, 0x1800F - 0x10)), //-
                    Map.entry(0x0E, DataAddress.from(0x18010 - 0x10, 0x1A00F - 0x10)), //-
                    Map.entry(0x11, DataAddress.from(0x1F99A - 0x10, 0x2000F - 0x10)), //-
                    Map.entry(0x01, DataAddress.from(0x21AF6 - 0x10, 0x21E80 - 0x10)), //-
                    Map.entry(0x08, DataAddress.from(0x21E81 - 0x10, 0x2200F - 0x10)), //-
                    Map.entry(0x0B, DataAddress.from(0x36010 - 0x10, 0x3800F - 0x10)), //-
                    Map.entry(0x15, DataAddress.from(0x384B5 - 0x10, 0x3886D - 0x10))
            ));
        }

        @Override
        @Editor.Load
        public void onLoad() {
            super.onLoad();
        }

        @Override
        @Editor.Apply
        public void onApply() {
            super.onApply();
        }
    }

    @Editor.TargetVersion({"super_hack", "super_hack_general"})
    public static class SHGTextEditorImpl extends TextEditorImpl {

        public SHGTextEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe, Map.ofEntries(
                    Map.entry(0x14, DataAddress.from(0x0BE90 - 0x10, 0x0C00F - 0x10)), //-
                    Map.entry(0x09, DataAddress.from(0x10010 - 0x10, 0x10128 - 0x10)), //-
                    Map.entry(0x02, DataAddress.from(0x10129 - 0x10, 0x10DB2 - 0x10)), //-
                    Map.entry(0x03, DataAddress.from(0x10DB3 - 0x10, 0x112F1 - 0x10)), //-
                    Map.entry(0x04, DataAddress.from(0x112F2 - 0x10, 0x1157B - 0x10)), //-
                    Map.entry(0x12, DataAddress.from(0x1157C - 0x10, 0x11932 - 0x10)), //-
                    Map.entry(0x13, DataAddress.from(0x11933 - 0x10, 0x11A1F - 0x10)), //--
//                    Map.entry(0x00, DataAddress.from(0x11A20 - 0x10, 0x11F74 - 0x10)), //-
                    Map.entry(0x00, DataAddress.from(0x52010 - 0x10, 0x527AF - 0x10)), //-
                    Map.entry(0x0C, DataAddress.from(0x12010 - 0x10, 0x120DF - 0x10)), //-
                    Map.entry(0x0D, DataAddress.from(0x120E0 - 0x10, 0x124ED - 0x10)), //-
                    Map.entry(0x06, DataAddress.from(0x124EE - 0x10, 0x1331F - 0x10)), //-
                    Map.entry(0x10, DataAddress.from(0x13320 - 0x10, 0x1400F - 0x10)), //-
                    Map.entry(0x05, DataAddress.from(0x14010 - 0x10, 0x1600F - 0x10)), //-
                    Map.entry(0x07, DataAddress.from(0x16010 - 0x10, 0x16BDC - 0x10)), //-
                    Map.entry(0x0A, DataAddress.from(0x16BDD - 0x10, 0x1767F - 0x10)), //-
                    Map.entry(0x0F, DataAddress.from(0x17680 - 0x10, 0x1800F - 0x10)), //-
                    Map.entry(0x0E, DataAddress.from(0x18010 - 0x10, 0x1A00F - 0x10)), //-
                    Map.entry(0x11, DataAddress.from(0x1F99A - 0x10, 0x2000F - 0x10)), //-
                    Map.entry(0x01, DataAddress.from(0x21AF6 - 0x10, 0x21E80 - 0x10)), //-
                    Map.entry(0x08, DataAddress.from(0x21E81 - 0x10, 0x2200F - 0x10)), //-
                    Map.entry(0x0B, DataAddress.from(0x36010 - 0x10, 0x3800F - 0x10)), //-
                    Map.entry(0x15, DataAddress.from(0x384B5 - 0x10, 0x3886D - 0x10))
            ));
        }

        @Override
        @Editor.Load
        public void onLoad() {
            super.onLoad();
        }

        @Override
        @Editor.Apply
        public void onApply() {
            super.onApply();
        }
    }
}
