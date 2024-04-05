package me.afoolslove.metalmaxre.editors.text;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import me.afoolslove.metalmaxre.utils.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
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
    private final Map<TextBuilder, List<TextBuilder>> easterEggNames = new LinkedHashMap<>();
    private final List<TextBuilder> player1NamePool = new ArrayList<>();
    private final List<TextBuilder> player2NamePool = new ArrayList<>();

    public TextEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                ResourceManager.getAsString("/text_addresses/chinese.json")
        );
    }

    public TextEditorImpl(@NotNull MetalMaxRe metalMaxRe, String jsonTextAddresses) {
        this(metalMaxRe,
                jsonTextAddresses,
                DataAddress.from(0x29C17 - 0x10, 0x29C66 - 0x10),
                DataAddress.from(0x29C67 - 0x10, 0x29C92 - 0x10),
                DataAddress.from(0x29C93 - 0x10, 0x29CBE - 0x10)
        );
    }

    public TextEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                          @Nullable String jsonTextAddresses,
                          @NotNull DataAddress easterEggNameAddress,
                          @NotNull DataAddress player1NamePoolAddress,
                          @NotNull DataAddress player2NamePoolAddress) {
        this(metalMaxRe, new HashMap<>(), easterEggNameAddress, player1NamePoolAddress, player2NamePoolAddress);
        if (jsonTextAddresses == null) {
            return;
        }
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonTextAddresses, JsonObject.class);

        for (String s : jsonObject.keySet()) {
            int page = Integer.parseInt(s, 16) & 0xFF;
            String[] split = jsonObject.get(s).getAsString().split("-", 2);
            int startAddr = Integer.parseInt(split[0], 16) & 0xFFFFF;
            int endAddr = Integer.parseInt(split[1], 16) & 0xFFFFF;
            textAddresses.put(page, DataAddress.from(startAddr - 0x10, endAddr - 0x10));
        }
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
            tmpBytes = entry.getKey().toByteArray(getCharMap());
            copyArrayTo(tmpBytes, bytes, 0x04, (byte) 0x9F);
            getBuffer().putAABytes(0, 0x04, bytes);

            for (TextBuilder textBuilder : entry.getValue()) {
                tmpBytes = textBuilder.toByteArray(getCharMap());
                copyArrayTo(tmpBytes, bytes, 0x04, (byte) 0x9F);
                getBuffer().putAABytes(0, 0x04, bytes);
            }
        }

        position(getPlayer1NamePoolAddress());
        for (int i = 0; i < 0x0B; i++) {
            TextBuilder textBuilder = player1NamePool.get(i);
            tmpBytes = textBuilder.toByteArray(getCharMap());
            copyArrayTo(tmpBytes, bytes, 0x04, (byte) 0x9F);
            getBuffer().put(bytes);
        }
        position(getPlayer2NamePoolAddress());
        for (int i = 0; i < 0x0B; i++) {
            TextBuilder textBuilder = player2NamePool.get(i);
            tmpBytes = textBuilder.toByteArray(getCharMap());
            copyArrayTo(tmpBytes, bytes, 0x04, (byte) 0x9F);
            getBuffer().put(bytes);
        }
    }

    private void copyArrayTo(byte[] src, byte[] dst, int length, byte fillValue) {
        Arrays.fill(dst, fillValue);
        System.arraycopy(src, 0, dst, 0, Math.min(src.length, length));
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
    public void setEasterEggNames(Map<TextBuilder, List<TextBuilder>> easterEggNames) {
        this.easterEggNames.clear();
        this.easterEggNames.putAll(easterEggNames);
    }

    @Override
    public void setPlayer1NamePool(List<TextBuilder> namePool) {
        this.player1NamePool.clear();
        this.player1NamePool.addAll(namePool);
    }

    @Override
    public void setPlayer2NamePool(List<TextBuilder> namePool) {
        this.player2NamePool.clear();
        this.player2NamePool.addAll(namePool);
    }

    @Override
    public Map<Integer, DataAddress> getTextAddresses() {
        return textAddresses;
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
            this(metalMaxRe,
                    ResourceManager.getAsString("/text_addresses/japanese.json")
            );
        }

        public JPTextEditorImpl(@NotNull MetalMaxRe metalMaxRe, String jsonTextAddresses) {
            super(metalMaxRe, jsonTextAddresses);
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
            this(metalMaxRe,
                    ResourceManager.getAsString("/text_addresses/super_hack.json")
            );
        }

        public SHTextEditorImpl(@NotNull MetalMaxRe metalMaxRe, String jsonTextAddresses) {
            super(metalMaxRe, jsonTextAddresses);
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
            this(metalMaxRe,
                    ResourceManager.getAsString("/text_addresses/super_hack_general.json")
            );
        }

        public SHGTextEditorImpl(@NotNull MetalMaxRe metalMaxRe, String jsonTextAddresses) {
            super(metalMaxRe, jsonTextAddresses);
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
