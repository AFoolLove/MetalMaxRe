package me.afoolslove.metalmaxre.editors.text;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.text.action.*;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 文本编辑器
 * <p>
 * *暂不支持读取日文原版文本
 *
 * @author AFoolLove
 */
@Editor.TargetVersions
public class TextEditorImpl extends RomBufferWrapperAbstractEditor implements ITextEditor {
    private final Map<Integer, DataAddress> textAddresses;

    private final Map<DataAddress, List<TextBuilder>> text = new HashMap<>();

    private final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);


    public TextEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe, Map.ofEntries(
                Map.entry(0x0BE90 - 0x10, DataAddress.from(0x0BE90 - 0x10, 0x0C00F - 0x10)),
                Map.entry(0x10010 - 0x10, DataAddress.from(0x10010 - 0x10, 0x10DB2 - 0x10)),
                Map.entry(0x10DB3 - 0x10, DataAddress.from(0x10DB3 - 0x10, 0x1157B - 0x10)),
                Map.entry(0x1157C - 0x10, DataAddress.from(0x1157C - 0x10, 0x11932 - 0x10)),
                Map.entry(0x11933 - 0x10, DataAddress.from(0x11933 - 0x10, 0x11A1F - 0x10)),
                Map.entry(0x11A20 - 0x10, DataAddress.from(0x11A20 - 0x10, 0x11F74 - 0x10)),
                Map.entry(0x11F75 - 0x10, DataAddress.from(0x11F75 - 0x10, 0x1200F - 0x10)),
                Map.entry(0x12010 - 0x10, DataAddress.from(0x12010 - 0x10, 0x120DF - 0x10)),
                Map.entry(0x120E0 - 0x10, DataAddress.from(0x120E0 - 0x10, 0x132FE - 0x10)),
                Map.entry(0x14010 - 0x10, DataAddress.from(0x14010 - 0x10, 0x15A9F - 0x10)),
                Map.entry(0x17680 - 0x10, DataAddress.from(0x17680 - 0x10, 0x1800F - 0x10)),
                Map.entry(0x1F99A - 0x10, DataAddress.from(0x1F99A - 0x10, 0x20F83 - 0x10)),
                Map.entry(0x21AF6 - 0x10, DataAddress.from(0x21AF6 - 0x10, 0x21E80 - 0x10)),
                Map.entry(0x36010 - 0x10, DataAddress.from(0x36010 - 0x10, 0x37CA5 - 0x10))
        ));
    }

    public TextEditorImpl(@NotNull MetalMaxRe metalMaxRe, @NotNull Map<Integer, DataAddress> textAddresses) {
        super(metalMaxRe);
        this.textAddresses = textAddresses;
    }

    @Editor.Load
    public void onLoad() {
        textAddresses.values().parallelStream().forEach(textAddress -> {
            try {
                EXECUTOR.submit(() -> {
                    List<TextBuilder> textBuilders = new ArrayList<>();

                    // 得到这段文本的数据长度
                    final byte[] bytes = new byte[textAddress.length()];
                    // 定位，读取
                    getBuffer().get(textAddress, bytes);

                    final ByteBuffer buffer = ByteBuffer.wrap(bytes);

                    // 一段文本，包括各种action
                    TextBuilder textBuilder = new TextBuilder();
                    // 缓存一段纯文本
                    StringBuilder builder = new StringBuilder();

                    // 最大缓存4字节
                    byte[] temp = new byte[4];
                    // 文本起始
                    data:
                    for (int position = 0, end = textAddress.length(); position <= end; ) {
                        final int tempLength = Math.min(temp.length, end - position);
                        buffer.get(position, temp, 0, tempLength);

                        // 在字库中查找对应的字符文本
                        allFonts:
                        for (Map.Entry<Character, ?> entry : WordBank.ALL_FONTS) {
                            if (entry.getValue() instanceof byte[] bs) {
                                for (int i = 0; i < bs.length; i++) {
                                    if (temp[i] != bs[i]) {
                                        continue allFonts;
                                    }
                                }
                                builder.append(entry.getKey());
                                position += bs.length;
                                continue data;
                            } else {
                                if (Objects.equals(entry.getValue(), temp[0x00])) {
                                    if (temp[0x00] == (byte) 0x9F) {
                                        // 断句
                                        // 保存当前文本，并清空缓存文本
                                        if (!builder.isEmpty()) {
                                            textBuilder.add(new Text(builder.toString()));
                                            builder.setLength(0);
                                        }

                                        textBuilders.add(textBuilder);

                                        textBuilder = new TextBuilder();
                                    } else {
                                        // 单byte对应的字符
                                        builder.append(entry.getKey());
                                    }
                                    position++;
                                    continue data;
                                }
                            }
                        }


                        // 单字节的操作码
                        var opcode = WordBank.OPCODES.get(temp[0x00]);
                        if (opcode == null) {
                            builder.append(String.format("[%02X]", temp[0x00]));
                            position++;
                            continue data;
                        }

                        switch (temp[0x00] & 0xFF) {
                            case 0xEB: // 进行选择
                                // 保存当前文本，并清空缓存文本
                                if (!builder.isEmpty()) {
                                    textBuilder.add(new Text(builder.toString()));
                                    builder.setLength(0);
                                }
                                // 添加选择action
                                textBuilder.add(new SelectAction(temp[0x01], temp[0x02]));
                                position += 3;

                                // 因为选择只能放在最后，所以结束当前文本
                                textBuilders.add(textBuilder);

                                textBuilder = new TextBuilder();
                                break;
                            case 0xEE: // 全局文本速度
                                // 保存当前文本，并清空缓存文本
                                if (!builder.isEmpty()) {
                                    textBuilder.add(new Text(builder.toString()));
                                    builder.setLength(0);
                                }
                                // 添加设置文本速度
                                textBuilder.add(new TextSpeedAction(temp[0x01]));
                                position += 2;
                                break;
                            case 0xED: // 空白占位
                                // 保存当前文本，并清空缓存文本
                                if (!builder.isEmpty()) {
                                    textBuilder.add(new Text(builder.toString()));
                                    builder.setLength(0);
                                }
                                // 添加占位
                                textBuilder.add(new SpaceAction(temp[0x01]));
                                position += 2;
                                break;
                            case 0xF1: // 文本等待
                                // 保存当前文本，并清空缓存文本
                                if (!builder.isEmpty()) {
                                    textBuilder.add(new Text(builder.toString()));
                                    builder.setLength(0);
                                }
                                // 添加文本等待
                                textBuilder.add(new WaitTimeAction(temp[0x01]));
                                position += 2;
                                break;
                            case 0xF5: // 精灵动作
                                // 保存当前文本，并清空缓存文本
                                if (!builder.isEmpty()) {
                                    textBuilder.add(new Text(builder.toString()));
                                    builder.setLength(0);
                                }
                                // 添加精灵动作
                                textBuilder.add(new SpriteAction(temp[0x01]));
                                position += 2;
                                break;
                            case 0xF6:  // 读取到 0x9F 或 0x63 后结束
                                builder.append(String.format("[%02X", temp[0x00]));
                                // 0x9E + 1byte     (填充数量)空格占位，第二字节为占多少
                                // 0x63             (结束)
                                // 0x8C + 2byte     (填充数量，填充字符)
                                // 0x43 ???
                                whileF6:
                                while (true) {
                                    if (++position >= bytes.length) {
                                        // 读取数据完毕
                                        builder.append(']');
                                        break data;
                                    }

                                    switch (buffer.get(position) & 0xFF) {
                                        case 0x9E: // 0x9E + 1byte     (填充数量)空格占位，第二字节为占多少
                                            // 写入 9E
                                            builder.append(" 9E");
                                            // 空格填充
                                            if (++position >= bytes.length) {
                                                // 读取完毕
                                                // 没有数量，直接结束
                                                builder.append(']');
                                                break data;
                                            }
                                            // 填充数量
                                            builder.append(String.format("%02X ", buffer.get(position)));
                                            break;
                                        case 0x63: // 0x63             (结束)
                                            // 0xF6 的结束符
                                            builder.append(" 63]");
                                            position++;
                                            continue data;
                                        case 0x8C: // 0x8C + 2byte     (填充数量，填充字符)
                                            builder.append(" 8C");
                                            // 使用指定字符填充指定数量
                                            if (++position >= bytes.length) {
                                                // 读取完毕
                                                // 没有字符，也没有数量，直接结束
                                                builder.append(']');
                                                break data;
                                            }
                                            // 填充数量
                                            builder.append(String.format("%02X", buffer.get(position)));
                                            if (++position >= bytes.length) {
                                                // 读取完毕
                                                // 没有字符，直接结束
                                                builder.append(']');
                                                break data;
                                            }
                                            // 填充字符
                                            builder.append(String.format("%02X ", buffer.get(position)));
                                            break;
                                        case 0x9F:
                                            // 文本段结束，另一个的开始
                                            builder.append("]");

                                            // 断句
                                            // 保存当前文本，并清空缓存文本
                                            textBuilder.add(new Text(builder.toString()));
                                            builder.setLength(0);

                                            textBuilders.add(textBuilder);

                                            textBuilder = new TextBuilder();

                                            position++;
                                            // emm？要不要结束？
                                            break whileF6;
                                        default:
                                            // 写入不认识的字节
                                            builder.append(String.format("%02X", buffer.get(position)));
                                            break;
                                    }
                                }
                                break;
                            default:
                                // 其余的直接读取相应的字节数量
                                builder.append('[');
                                int len = Math.min(tempLength, opcode + 1);
                                for (int j = 0; j < len; j++) {
                                    builder.append(String.format("%02X", temp[j]));
                                }
                                // 至少 +1 opcode
                                position += len;
                                // 读取结束
                                builder.append(']');
                                break;
                        }
                    }
                    text.put(textAddress, textBuilders);
                    return null;
                }).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    @Editor.Apply
    public void onApply() {
        text.entrySet().parallelStream().forEach(entry -> {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(entry.getKey().length());
            // 将该段地址的文本转换为游戏使用的字节
            entry.getValue().stream().map(TextBuilder::toByteArray).forEach(outputStream::writeBytes);
            byte[] bytes = outputStream.toByteArray();
            // 覆盖写入该段地址的文本
            getBuffer().put(entry.getKey(), bytes, 0, Math.min(bytes.length, entry.getKey().length() - 1));

            if (bytes.length < entry.getKey().length()) {
                System.out.println(String.format("文本编辑器：%05X-%05X 剩余%d个字节", entry.getKey().getStartAddress(), entry.getKey().getEndAddress(), entry.getKey().length() - bytes.length));
            } else if (bytes.length > entry.getKey().length()) {
                System.out.println(String.format("文本编辑器：%05X-%05X 溢出%d个字节", entry.getKey().getStartAddress(), entry.getKey().getEndAddress(), bytes.length - entry.getKey().length()));
            }
        });
    }


    @Override
    public String getTownName(int townId) {
        return text.get(getTownNameAddress()).get(0x30 + townId).toText();
    }

    @Override
    public String getItemName(int itemId) {
        return text.get(getItemNameAddress()).get(itemId).toText();
    }

    @Override
    public String getMonsterName(int monsterId) {
        return text.get(getMonsterNameAddress()).get(monsterId).toText();
    }

    @Override
    public void setTownName(int townId, String newName) {
        text.get(getTownNameAddress()).set(0x30 + townId, new TextBuilder(new Text(newName)));
    }

    @Override
    public void setItemName(int itemId, String newName) {
        text.get(getItemNameAddress()).set(itemId, new TextBuilder(new Text(newName)));
    }

    @Override
    public void setMonsterName(int monsterId, String newName) {
        text.get(getItemNameAddress()).set(monsterId, new TextBuilder(new Text(newName)));
    }

    @Override
    public Map<Integer, DataAddress> getTextAddresses() {
        return textAddresses;
    }

    @Override
    public DataAddress getTownNameAddress() {
        return getTextAddresses().get(0x120E0 - 0x10);
    }

    @Override
    public DataAddress getItemNameAddress() {
        return getTextAddresses().get(0x11A20 - 0x10);
    }

    @Override
    public DataAddress getMonsterNameAddress() {
        return getTextAddresses().get(0x21AF6 - 0x10);
    }

    @Editor.TargetVersion({"super_hack", "super_hack_general"})
    public static class SHGTextEditorImpl extends TextEditorImpl {

        public SHGTextEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
            super(metalMaxRe, Map.ofEntries(
                    Map.entry(0x0BE90 - 0x10, DataAddress.from(0x0BE90 - 0x10, 0x0C00F - 0x10)),
                    Map.entry(0x10010 - 0x10, DataAddress.from(0x10010 - 0x10, 0x10DB2 - 0x10)),
                    Map.entry(0x10DB3 - 0x10, DataAddress.from(0x10DB3 - 0x10, 0x1157B - 0x10)),
                    Map.entry(0x1157C - 0x10, DataAddress.from(0x1157C - 0x10, 0x11932 - 0x10)),
                    Map.entry(0x11933 - 0x10, DataAddress.from(0x11933 - 0x10, 0x11A1F - 0x10)),
                    Map.entry(0x11F75 - 0x10, DataAddress.from(0x11F75 - 0x10, 0x1200F - 0x10)),
                    Map.entry(0x12010 - 0x10, DataAddress.from(0x12010 - 0x10, 0x120DF - 0x10)),
                    Map.entry(0x120E0 - 0x10, DataAddress.from(0x120E0 - 0x10, 0x132FE - 0x10)),
                    Map.entry(0x14010 - 0x10, DataAddress.from(0x14010 - 0x10, 0x15A9F - 0x10)),
                    Map.entry(0x17680 - 0x10, DataAddress.from(0x17680 - 0x10, 0x1800F - 0x10)),
                    Map.entry(0x1F99A - 0x10, DataAddress.from(0x1F99A - 0x10, 0x20F83 - 0x10)),
                    Map.entry(0x21AF6 - 0x10, DataAddress.from(0x21AF6 - 0x10, 0x21E80 - 0x10)),
                    Map.entry(0x36010 - 0x10, DataAddress.from(0x36010 - 0x10, 0x37CA5 - 0x10)),
//                    Map.entry(0x11A20 - 0x10, DataAddress.from(0x11A20 - 0x10, 0x11F74 - 0x10)),
                    Map.entry(0x52010 - 0x10, DataAddress.from(0x52010 - 0x10, 0x527AF - 0x10))
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

        @Override
        public DataAddress getItemNameAddress() {
            return getTextAddresses().get(0x52010 - 0x10);
        }
    }
}
