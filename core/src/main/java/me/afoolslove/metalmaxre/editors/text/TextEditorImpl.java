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
import java.util.*;

/**
 * 文本编辑器
 *
 * @author AFoolLove
 */
@Editor.TargetVersions
public class TextEditorImpl extends RomBufferWrapperAbstractEditor implements ITextEditor {
    private static final Logger LOGGER = LoggerFactory.getLogger(TextEditorImpl.class);
    private final Map<Integer, DataAddress> textAddresses;

    private final DataAddress easterEggNameAddress;
    private final DataAddress player1NamePoolAddress;
    private final DataAddress player2NamePoolAddress;

    private final Map<DataAddress, List<TextBuilder>> text = new HashMap<>();

    public Map<DataAddress, List<TextBuilder>> getText() {
        return text;
    }

    private final Map<TextBuilder, List<TextBuilder>> easterEggNames = new HashMap<>();
    private final List<TextBuilder> player1NamePool = new ArrayList<>();
    private final List<TextBuilder> player2NamePool = new ArrayList<>();

    public TextEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe, Map.ofEntries(
                        Map.entry(0x14, DataAddress.from(0x0BE90 - 0x10, 0x0C00F - 0x10)), //-
                        Map.entry(0x09, DataAddress.from(0x10010 - 0x10, 0x10128 - 0x10)), //-
                        Map.entry(0x02, DataAddress.from(0x10129 - 0x10, 0x10DB2 - 0x10)), //-
                        Map.entry(0x03, DataAddress.from(0x10DB3 - 0x10, 0x112F1 - 0x10)), //-
                        Map.entry(0x04, DataAddress.from(0x112F2 - 0x10, 0x1157B - 0x10)), //-
                        Map.entry(0x12, DataAddress.from(0x1157C - 0x10, 0x11932 - 0x10)), //-
                        Map.entry(0x13, DataAddress.from(0x11933 - 0x10, 0x11A1F - 0x10)), //-
                        Map.entry(0x00, DataAddress.from(0x11A20 - 0x10, 0x1200F - 0x10)), //-
                        Map.entry(0x0C, DataAddress.from(0x12010 - 0x10, 0x120DF - 0x10)), //-
                        Map.entry(0x0D, DataAddress.from(0x120E0 - 0x10, 0x124ED - 0x10)), //-
                        Map.entry(0x06, DataAddress.from(0x124EE - 0x10, 0x1331F - 0x10)), //-
                        Map.entry(0x10, DataAddress.from(0x13320 - 0x10, 0x1400F - 0x10)), //-
                        Map.entry(0x05, DataAddress.from(0x14010 - 0x10, 0x15A9F - 0x10)), //-
                        Map.entry(0x07, DataAddress.from(0x16010 - 0x10, 0x16BDC - 0x10)), //-
                        Map.entry(0x0A, DataAddress.from(0x16BDD - 0x10, 0x1767F - 0x10)), //-
                        Map.entry(0x0F, DataAddress.from(0x17680 - 0x10, 0x1800F - 0x10)), //-
                        Map.entry(0x0E, DataAddress.from(0x18010 - 0x10, 0x1A00F - 0x10)), //-
                        Map.entry(0x11, DataAddress.from(0x1F99A - 0x10, 0x2000F - 0x10)), //-
                        Map.entry(0x01, DataAddress.from(0x21AF6 - 0x10, 0x21E80 - 0x10)), //-
                        Map.entry(0x08, DataAddress.from(0x21E81 - 0x10, 0x2200F - 0x10)), //-
                        Map.entry(0x0B, DataAddress.from(0x36010 - 0x10, 0x3800F - 0x10)), //-
                        Map.entry(0x15, DataAddress.from(0x384B5 - 0x10, 0x3886D - 0x10))
                ),
                DataAddress.from(0x29C17 - 0x10, 0x29C66 - 0x10),
                DataAddress.from(0x29C67 - 0x10, 0x29C92 - 0x10),
                DataAddress.from(0x29C93 - 0x10, 0x29CBE - 0x10)
        );
    }

    public TextEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                          @NotNull Map<Integer, DataAddress> textAddresses,
                          @NotNull DataAddress easterEggNameAddress,
                          @NotNull DataAddress player1NamePoolAddress,
                          @NotNull DataAddress player2NamePoolAddress) {
        super(metalMaxRe);
        this.textAddresses = textAddresses;
        this.easterEggNameAddress = easterEggNameAddress;
        this.player1NamePoolAddress = player1NamePoolAddress;
        this.player2NamePoolAddress = player2NamePoolAddress;
    }

    @Editor.Load
    public void onLoad() {
        this.text.clear();
        easterEggNames.clear();
        player1NamePool.clear();
        player2NamePool.clear();

        textAddresses.values().parallelStream().forEach(textAddress -> {
            // 得到这段文本的数据长度
            final byte[] bytes = new byte[textAddress.length()];
            // 定位，读取
            getBuffer().get(textAddress, bytes);

            this.text.put(textAddress, TextBuilder.fromBytes(bytes, getCharMap()));
        });


        position(getEasterEggNameAddress());
        byte[][] easterEggNameBytes = new byte[0x04][0x04];
        for (int easterEggName = 0; easterEggName < 0x05; easterEggName++) {
            getBuffer().getAABytes(0, 0x04, easterEggNameBytes);

            TextBuilder key = TextBuilder.fromBytes(easterEggNameBytes[0], getCharMap()).get(0);
            List<TextBuilder> value = new ArrayList<>();
            for (int i = 1; i < easterEggNameBytes.length; i++) {
                value.add(TextBuilder.fromBytes(easterEggNameBytes[i], getCharMap()).get(0));
            }
            easterEggNames.put(key, value);
        }

        position(getPlayer1NamePoolAddress());
        byte[] bytes = new byte[0x04];
        for (int i = 0; i < 0x0B; i++) {
            getBuffer().get(bytes);
            player1NamePool.add(TextBuilder.fromBytes(bytes, getCharMap()).get(0));
        }
        position(getPlayer2NamePoolAddress());
        for (int i = 0; i < 0x0B; i++) {
            getBuffer().get(bytes);
            player2NamePool.add(TextBuilder.fromBytes(bytes, getCharMap()).get(0));
        }
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

        position(getEasterEggNameAddress());
        byte[] bytes = new byte[0x04];
        byte[] tmpBytes;
        for (Map.Entry<TextBuilder, List<TextBuilder>> entry : easterEggNames.entrySet()) {
            Arrays.fill(bytes, (byte) 0x9F);
            tmpBytes = entry.getKey().toByteArray(getCharMap());
            System.arraycopy(bytes, 0, tmpBytes, 0, Math.min(tmpBytes.length, 0x04));
            getBuffer().putAABytes(0, 0x04, bytes);

            for (TextBuilder textBuilder : entry.getValue()) {
                Arrays.fill(bytes, (byte) 0x9F);
                tmpBytes = textBuilder.toByteArray(getCharMap());
                System.arraycopy(bytes, 0, tmpBytes, 0, Math.min(tmpBytes.length, 0x04));
                getBuffer().putAABytes(0, 0x04, bytes);
            }
        }

        position(getPlayer1NamePoolAddress());
        for (int i = 0; i < 0x0B; i++) {
            TextBuilder textBuilder = player1NamePool.get(i);
            Arrays.fill(bytes, (byte) 0x9F);
            tmpBytes = textBuilder.toByteArray(getCharMap());
            System.arraycopy(bytes, 0, tmpBytes, 0, Math.min(tmpBytes.length, 0x04));
            getBuffer().put(bytes);
        }
        position(getPlayer2NamePoolAddress());
        for (int i = 0; i < 0x0B; i++) {
            TextBuilder textBuilder = player2NamePool.get(i);
            Arrays.fill(bytes, (byte) 0x9F);
            tmpBytes = textBuilder.toByteArray(getCharMap());
            System.arraycopy(bytes, 0, tmpBytes, 0, Math.min(tmpBytes.length, 0x04));
            getBuffer().put(bytes);
        }
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
        List<TextBuilder> textBuilders = text.get(getTownNameAddress());
        if (textBuilders == null || (0x30 + townId) >= textBuilders.size()) {
            return "null";
        }
        return textBuilders.get(0x30 + townId).toText(getCharMap());
    }

    @Override
    public String getItemName(int itemId) {
        List<TextBuilder> textBuilders = text.get(getItemNameAddress());
        if (textBuilders == null || itemId >= textBuilders.size()) {
            return "null";
        }
        return textBuilders.get(itemId).toText(getCharMap());
    }

    @Override
    public String getMonsterName(int monsterId) {
        List<TextBuilder> textBuilders = text.get(getMonsterNameAddress());
        if (textBuilders == null || monsterId >= textBuilders.size()) {
            return "null";
        }
        return textBuilders.get(monsterId).toText(getCharMap());
    }

    @Override
    public Map<TextBuilder, List<TextBuilder>> getEasterEggNames() {
        return easterEggNames;
    }

    @Override
    public List<TextBuilder> getPlayer1NamePool() {
        return player1NamePool;
    }

    @Override
    public List<TextBuilder> getPlayer2NamePool() {
        return player2NamePool;
    }

    @Override
    public void setTownName(int townId, String newName) {
        List<TextBuilder> textBuilders = text.get(getTownNameAddress());
        if (textBuilders == null || (0x30 + townId) >= textBuilders.size()) {
            return;
        }
        textBuilders.set(0x30 + townId, new TextBuilder(new Text(newName)));
    }

    @Override
    public void setItemName(int itemId, String newName) {
        List<TextBuilder> textBuilders = text.get(getItemNameAddress());
        if (textBuilders == null || itemId >= textBuilders.size()) {
            return;
        }
        textBuilders.set(itemId, new TextBuilder(new Text(newName)));
    }

    @Override
    public void setMonsterName(int monsterId, String newName) {
        List<TextBuilder> textBuilders = text.get(getItemNameAddress());
        if (textBuilders == null || monsterId >= textBuilders.size()) {
            return;
        }
        textBuilders.set(monsterId, new TextBuilder(new Text(newName)));
    }

    @Override
    public void setEasterEggName(TextBuilder name, List<TextBuilder> easterEggName) {

    }

    @Override
    public void setPlayer1NamePool(List<TextBuilder> namePool) {

    }

    @Override
    public void setPlayer2NamePool(List<TextBuilder> namePool) {

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

    @Override
    public DataAddress getEasterEggNameAddress() {
        return easterEggNameAddress;
    }

    @Override
    public DataAddress getPlayer1NamePoolAddress() {
        return player1NamePoolAddress;
    }

    @Override
    public DataAddress getPlayer2NamePoolAddress() {
        return player2NamePoolAddress;
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
                            Map.entry(0x13, DataAddress.from(0x11933 - 0x10, 0x11A25 - 0x10)), //-
                            Map.entry(0x00, DataAddress.from(0x11A26 - 0x10, 0x1200F - 0x10)), //-
                            Map.entry(0x0C, DataAddress.from(0x12010 - 0x10, 0x120B5 - 0x10)), //-
                            Map.entry(0x0D, DataAddress.from(0x120B6 - 0x10, 0x124ED - 0x10)), //-
                            Map.entry(0x06, DataAddress.from(0x124EE - 0x10, 0x13396 - 0x10)), //-
                            Map.entry(0x10, DataAddress.from(0x13397 - 0x10, 0x1400F - 0x10)), //-
                            Map.entry(0x05, DataAddress.from(0x14010 - 0x10, 0x1600F - 0x10)), //-
                            Map.entry(0x07, DataAddress.from(0x16010 - 0x10, 0x16BDC - 0x10)), //-
                            Map.entry(0x0A, DataAddress.from(0x16BDD - 0x10, 0x17502 - 0x10)), //-
                            Map.entry(0x0F, DataAddress.from(0x17503 - 0x10, 0x1800F - 0x10)), //-
                            Map.entry(0x0E, DataAddress.from(0x18010 - 0x10, 0x1A00F - 0x10)), //-
                            Map.entry(0x11, DataAddress.from(0x1F99A - 0x10, 0x2000F - 0x10)), //-
                            Map.entry(0x01, DataAddress.from(0x21AF6 - 0x10, 0x21ECC - 0x10)), //-
                            Map.entry(0x08, DataAddress.from(0x21ECD - 0x10, 0x2200F - 0x10)), //-
                            Map.entry(0x0B, DataAddress.from(0x36010 - 0x10, 0x3800F - 0x10)), //-
                            Map.entry(0x15, DataAddress.from(0x384B5 - 0x10, 0x3886D - 0x10))
                    ),
                    DataAddress.from(0x29C17 - 0x10, 0x29C66 - 0x10),
                    DataAddress.from(0x29C67 - 0x10, 0x29C92 - 0x10),
                    DataAddress.from(0x29C93 - 0x10, 0x29CBE - 0x10));
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

    @Editor.TargetVersion("super_hack")
    public static class SHTextEditorImpl extends TextEditorImpl {

        public SHTextEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe, Map.ofEntries(
                            Map.entry(0x14, DataAddress.from(0x0BE90 - 0x10, 0x0C00F - 0x10)), //-
                            Map.entry(0x09, DataAddress.from(0x10010 - 0x10, 0x10128 - 0x10)), //-
                            Map.entry(0x02, DataAddress.from(0x10129 - 0x10, 0x10DB2 - 0x10)), //-
                            Map.entry(0x03, DataAddress.from(0x10DB3 - 0x10, 0x112F1 - 0x10)), //-
                            Map.entry(0x04, DataAddress.from(0x112F2 - 0x10, 0x1157B - 0x10)), //-
                            Map.entry(0x12, DataAddress.from(0x1157C - 0x10, 0x11932 - 0x10)), //-
                            Map.entry(0x13, DataAddress.from(0x11933 - 0x10, 0x11A1F - 0x10)), //-
                            Map.entry(0x00, DataAddress.from(0x7E010 - 0x10, 0x7E7AF - 0x10)), //-
                            Map.entry(0x0C, DataAddress.from(0x12010 - 0x10, 0x120DF - 0x10)), //-
                            Map.entry(0x0D, DataAddress.from(0x120E0 - 0x10, 0x124ED - 0x10)), //-
                            Map.entry(0x06, DataAddress.from(0x124EE - 0x10, 0x1331F - 0x10)), //-
                            Map.entry(0x10, DataAddress.from(0x13320 - 0x10, 0x1400F - 0x10)), //-
                            Map.entry(0x05, DataAddress.from(0x14010 - 0x10, 0x15A9F - 0x10)), //-
                            Map.entry(0x07, DataAddress.from(0x16010 - 0x10, 0x16BDC - 0x10)), //-
                            Map.entry(0x0A, DataAddress.from(0x16BDD - 0x10, 0x1767F - 0x10)), //-
                            Map.entry(0x0F, DataAddress.from(0x17680 - 0x10, 0x1800F - 0x10)), //-
                            Map.entry(0x0E, DataAddress.from(0x18010 - 0x10, 0x1A00F - 0x10)), //-
                            Map.entry(0x11, DataAddress.from(0x1F99A - 0x10, 0x2000F - 0x10)), //-
                            Map.entry(0x01, DataAddress.from(0x21AF6 - 0x10, 0x21E80 - 0x10)), //-
                            Map.entry(0x08, DataAddress.from(0x21E81 - 0x10, 0x2200F - 0x10)), //-
                            Map.entry(0x0B, DataAddress.from(0x36010 - 0x10, 0x3800F - 0x10)), //-
                            Map.entry(0x15, DataAddress.from(0x384B5 - 0x10, 0x3886D - 0x10))
                    ),
                    DataAddress.from(0x29C17 - 0x10, 0x29C66 - 0x10),
                    DataAddress.from(0x29C67 - 0x10, 0x29C92 - 0x10),
                    DataAddress.from(0x29C93 - 0x10, 0x29CBE - 0x10));
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

    @Editor.TargetVersion("super_hack_general")
    public static class SHGTextEditorImpl extends TextEditorImpl {

        public SHGTextEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe, Map.ofEntries(
                            Map.entry(0x14, DataAddress.from(0x0BE90 - 0x10, 0x0C00F - 0x10)), //-
                            Map.entry(0x09, DataAddress.from(0x10010 - 0x10, 0x10128 - 0x10)), //-
                            Map.entry(0x02, DataAddress.from(0x10129 - 0x10, 0x10DB2 - 0x10)), //-
                            Map.entry(0x03, DataAddress.from(0x10DB3 - 0x10, 0x112F1 - 0x10)), //-
                            Map.entry(0x04, DataAddress.from(0x112F2 - 0x10, 0x1157B - 0x10)), //-
                            Map.entry(0x12, DataAddress.from(0x1157C - 0x10, 0x11932 - 0x10)), //-
                            Map.entry(0x13, DataAddress.from(0x11933 - 0x10, 0x11A1F - 0x10)), //-
                            Map.entry(0x00, DataAddress.from(0x52010 - 0x10, 0x527AF - 0x10)), //-
                            Map.entry(0x0C, DataAddress.from(0x12010 - 0x10, 0x120DF - 0x10)), //-
                            Map.entry(0x0D, DataAddress.from(0x120E0 - 0x10, 0x124ED - 0x10)), //-
                            Map.entry(0x06, DataAddress.from(0x124EE - 0x10, 0x1331F - 0x10)), //-
                            Map.entry(0x10, DataAddress.from(0x13320 - 0x10, 0x1400F - 0x10)), //-
                            Map.entry(0x05, DataAddress.from(0x14010 - 0x10, 0x15A9F - 0x10)), //-
                            Map.entry(0x07, DataAddress.from(0x16010 - 0x10, 0x16BDC - 0x10)), //-
                            Map.entry(0x0A, DataAddress.from(0x16BDD - 0x10, 0x1767F - 0x10)), //-
                            Map.entry(0x0F, DataAddress.from(0x17680 - 0x10, 0x1800F - 0x10)), //-
                            Map.entry(0x0E, DataAddress.from(0x18010 - 0x10, 0x1A00F - 0x10)), //-
                            Map.entry(0x11, DataAddress.from(0x1F99A - 0x10, 0x2000F - 0x10)), //-
                            Map.entry(0x01, DataAddress.from(0x21AF6 - 0x10, 0x21E80 - 0x10)), //-
                            Map.entry(0x08, DataAddress.from(0x21E81 - 0x10, 0x2200F - 0x10)), //-
                            Map.entry(0x0B, DataAddress.from(0x36010 - 0x10, 0x3800F - 0x10)), //-
                            Map.entry(0x15, DataAddress.from(0x384B5 - 0x10, 0x3886D - 0x10))
                    ),
                    DataAddress.from(0x29C17 - 0x10, 0x29C66 - 0x10),
                    DataAddress.from(0x29C67 - 0x10, 0x29C92 - 0x10),
                    DataAddress.from(0x29C93 - 0x10, 0x29CBE - 0x10));
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
