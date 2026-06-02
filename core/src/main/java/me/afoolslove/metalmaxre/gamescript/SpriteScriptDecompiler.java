package me.afoolslove.metalmaxre.gamescript;

import me.afoolslove.metalmaxre.gamescript.i.IBlock;
import me.afoolslove.metalmaxre.gamescript.system.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

/**
 * 脚本字节码反解析器
 * 将字节数组解析为符合 SpriteScript.g4 规则的文本格式
 */
public class SpriteScriptDecompiler {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpriteScriptDecompiler.class);
    /**
     * 调试日志开关，设为 true 可输出详细的反解析过程日志
     */
    public static boolean DEBUG = false;

    // ========== Opcode 扩展注册机制 ==========

    /**
     * 自定义 opcode 处理器函数式接口
     * <p>
     * 实现此接口以支持非预设的 opcode 解析。
     * 处理器接收 opcode 值（0x00-0xFF），返回解析后的 {@link BaseSpriteScript}。
     * 如果无法处理，应返回 null 以回退到默认逻辑。
     *
     * <h3>使用示例：</h3>
     * <pre>{@code
     * SpriteScriptDecompiler.registerOpcode(0xFD, (opcode, reader) -> {
     *     byte param = reader.readByte();
     *     return new MyCustomScript(opcode, param);
     * });
     * }</pre>
     */
    @FunctionalInterface
    public interface OpcodeHandler {
        /**
         * 处理自定义 opcode
         *
         * @param opcode 操作码值 (0x00-0xFF)
         * @param reader 字节读取器，用于读取后续参数字节
         * @return 解析后的脚本对象；如果无法处理则返回 null 回退到默认逻辑
         * @throws IOException 读取参数时发生 I/O 错误
         */
        BaseSpriteScript handle(int opcode, ByteReader reader) throws IOException;
    }

    /**
     * 字节读取器，提供给 {@link OpcodeHandler} 读取后续参数字节
     */
    public interface ByteReader {
        /**
         * 读取下一个字节，同时推进 position 指针
         */
        byte readByte() throws IOException;
    }

    /**
     * 自定义 opcode 注册表（静态共享，所有实例共用）
     * key: opcode 值 (0x00-0xFF)
     * value: 对应的处理器
     */
    private static final Map<Integer, OpcodeHandler> OPCODE_HANDLERS = new HashMap<>();

    /**
     * 注册自定义 opcode 处理器
     * <p>
     * 已注册的 opcode 会优先于内置 switch 分支被匹配。
     * 如果同一 opcode 被多次注册，后注册的处理器会覆盖先前的。
     *
     * @param opcode  操作码值 (0x00-0xFF)
     * @param handler 处理器实现，不能为 null
     * @throws IllegalArgumentException 如果 opcode 超出范围或 handler 为 null
     */
    public static void registerOpcode(int opcode, OpcodeHandler handler) {
        if (opcode < 0 || opcode > 0xFF) {
            throw new IllegalArgumentException("opcode 必须在 0x00-0xFF 范围内: 0x" + Integer.toHexString(opcode));
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler 不能为 null");
        }
        OPCODE_HANDLERS.put(opcode, handler);
        LOGGER.info("已注册自定义 opcode: 0x{}", String.format("%02X", opcode));
    }

    /**
     * 注销自定义 opcode 处理器
     *
     * @param opcode 要注销的操作码值
     * @return 被移除的处理器，如果该 opcode 未注册则返回 null
     */
    public static OpcodeHandler unregisterOpcode(int opcode) {
        OpcodeHandler removed = OPCODE_HANDLERS.remove(opcode);
        if (removed != null) {
            LOGGER.info("已注销自定义 opcode: 0x{}", String.format("%02X", opcode));
        }
        return removed;
    }

    /**
     * 检查指定 opcode 是否已注册自定义处理器
     *
     * @param opcode 操作码值
     * @return 如果已注册返回 true
     */
    public static boolean hasCustomOpcode(int opcode) {
        return OPCODE_HANDLERS.containsKey(opcode);
    }

    /**
     * 清除所有已注册的自定义 opcode 处理器
     */
    public static void clearCustomOpcodes() {
        int size = OPCODE_HANDLERS.size();
        OPCODE_HANDLERS.clear();
        if (size > 0) {
            LOGGER.info("已清除全部 {} 个自定义 opcode 注册", size);
        }
    }

    private final ByteArrayInputStream inputStream;
    private final List<BaseSpriteScript> scripts;
    private int position;
    /**
     * 被引用的标签集合（只有这些标签会在 toText 中输出）
     */
    private final Set<String> referencedLabels = new HashSet<>();
    /**
     * 缓存：全部脚本列表（避免反复遍历脚本树）
     */
    private List<BaseSpriteScript> cachedAllScripts;

    public SpriteScriptDecompiler(byte[] data) {
        this.inputStream = new ByteArrayInputStream(data);
        this.scripts = new ArrayList<>();
        this.position = 0;
    }

    /**
     * 反解析字节数组为脚本列表
     *
     * @return 解析后的脚本列表
     */
    public List<BaseSpriteScript> decompile() {
        try {
            while (inputStream.available() > 0) {
                BaseSpriteScript script = parseNextScript();
                if (script != null) {
                    scripts.add(script);
                } else {
                    break;
                }
            }
            // 反解析完成后，处理 JMP 标签
            processJmpLabels();
            // 处理 Do-While 循环
            processDoLoops();
            // 处理向上的 if 代码块（负 blockLength）
            processNegativeBlocks();
            // 转换冲突的 if 为标签跳转模式
            convertConflictingIfsToJumps();
            // 收集所有被实际引用的标签，toText 中只输出这些标签
            collectReferencedLabels();
        } catch (IOException e) {
            LOGGER.error("反解析脚本时发生错误", e);
        }
        return scripts;
    }

    /**
     * 解析下一条指令
     */
    private BaseSpriteScript parseNextScript() throws IOException {
        // 先记录当前位置（这是opcode的位置）
        int scriptPosition = position;

        int opcode = inputStream.read();
        if (opcode == -1) {
            return null;
        }
        position++;

        if (DEBUG) LOGGER.debug("parseNextScript: scriptPosition={}, opcode=0x{}, afterRead_position={}",
                scriptPosition, String.format("%02X", opcode), position);

        // 优先检查自定义 opcode 注册表
        OpcodeHandler customHandler = OPCODE_HANDLERS.get(opcode);
        BaseSpriteScript script;
        if (customHandler != null) {
            script = customHandler.handle(opcode, () -> (byte) readByte());
            if (script != null) {
                if (DEBUG) LOGGER.debug("自定义 opcode 0x{} 匹配成功: {}",
                        String.format("%02X", opcode), script.getClass().getSimpleName());
            } else {
                if (DEBUG) LOGGER.debug("自定义 opcode 0x{} 返回 null，回退到内置解析",
                        String.format("%02X", opcode));
                // 自定义处理器返回 null，回退到内置逻辑
                script = parseBuiltinOpcode(opcode);
            }
        } else {
            script = parseBuiltinOpcode(opcode);
        }
        // 设置脚本的字节位置
        script.bytePosition = scriptPosition;
        if (DEBUG) LOGGER.debug("设置脚本 {} 的bytePosition={}", script.getClass().getSimpleName(), scriptPosition);

        return script;
    }

    /**
     * 内置 opcode 解析（switch 分发）
     * <p>
     * 处理所有预设的 opcode 映射，未匹配的 opcode 走 {@link #parseUnknownScript(int)}。
     */
    private BaseSpriteScript parseBuiltinOpcode(int opcode) throws IOException {
        return switch (opcode) {
            case 0x00 -> parseEndScript();
            case 0x01, 0x02, 0x03, 0x48 -> parseTextQuoteScript(opcode);
            case 0x68 -> parseTextQuoteScript(opcode);
            case 0x04 -> parseEventOpenScript();
            case 0x05 -> parseEventCloseScript();
            case 0x06 -> parseIfEventScript();
            case 0x07 -> parseDoLoopScript();
            case 0x08 -> parseIfXyScript();
            case 0x09 -> parseIfOptionYesScript();
            case 0x0A, 0x0B -> parseNpcMoveWanderScript(opcode);
            case 0x0C -> new NpcScript.FaceScript.Up();
            case 0x0D -> new NpcScript.FaceScript.Down();
            case 0x0E, 0x0F -> parseFaceScript(opcode);
            case 0x10 -> new NpcScript.FaceScript.Back();
            case 0x11 -> new NpcScript.FaceScript.Player();
            case 0x12 -> new NpcScript.MoveScript.Up();
            case 0x13 -> new NpcScript.MoveScript.Down();
            case 0x14 -> new NpcScript.MoveScript.Left();
            case 0x15 -> new NpcScript.MoveScript.Right();
            case 0x16 -> parseNpcMoveToScript();
            case 0x17 -> new NpcScript.MoveScript.Chase(1);
            case 0x18 -> parseJmpScript();
            case 0x19 -> parseWaitTimeScript();
            case 0x1A -> new NpcScript.HideScript();
            case 0x1B -> parseNpcAnimFrameScript();
            case 0x1C -> parseNpcModelTileTypeScript();
            case 0x1D -> parseMusicScript();
            case 0x1E -> parseSleepScript();
            case 0x1F -> new NpcScript.Act();
            case 0x20 -> parseNpcMoveTpScript();
            case 0x21 -> new NextDayScript();
            case 0x22 -> parseIfRideTankScript();
            case 0x23 -> parseTeamJoinNowScript();
            case 0x24 -> parseIfTeamScript();
            case 0x25 -> parseSpeakerScript();
            case 0x26 -> parseMenuScript();
            case 0x27 -> parseEventWaitScript();
            case 0x28 -> parseSceneScript();
            case 0x29 -> parseNpcAttrsScript();
            case 0x2A -> parseIfTeammateDeadScript();
            case 0x2B -> parseRecoverScript();
            case 0x2C -> parseIfFaceScript();
            case 0x2D -> parseIfHasOkTankScript();
            case 0x2E -> new NpcScript.MoveScript.Tank();
            case 0x2F -> parseNpcPatrolScript();
            case 0x30 -> new NpcScript.EnterTank();
            case 0x31 -> parseNpcTankExitScript();
            case 0x32 -> new NpcScript.Anim.Resume();
            case 0x33 -> parseNpcAnimThrowScript();
            case 0x34 -> new NpcScript.MoveScript.Chase();
            case 0x35 -> parseIfAreaScript();
            case 0x36 -> parseIfLevelScript();
            case 0x37 -> parseBattleScript();
            case 0x38 -> new SceneScript.End();
            case 0x39 -> parseIfHasItemScript();
            case 0x3A -> parseSwapPlayerItemScript();
            case 0x3B -> parseNpcModelSetScript();
            case 0x3C -> new NpcScript.Model.Next();
            case 0x3D -> new NpcScript.Model.Previous();
            case 0x3E -> parseTilesSpriteScript();
            case 0x3F -> parseNpcMoveTpOffsetScript();
            case 0x40 -> parseNpcBecomeScript();
            case 0x41 -> parsePlayerShowHideScript();
            case 0x42 -> new NpcScript.Explode();
            case 0x43 -> new ControlScript.Change();
            case 0x44 -> parseQuakeScript();
            case 0x45 -> parseIfMoneyScript();
            case 0x46 -> parseTeamJoinScript();
            case 0x47 -> parseTeamLeaveScript();
            case 0x49 -> parseGivePlayerItemScript();
            case 0x4A -> parseNpcAnimPlayScript();
            case 0x4B -> parseMoneySpendScript();
            case 0x4D -> parseIfDrankScript();
            case 0x4E -> parsePlayerBecomeScript();
            case 0x4F -> new NpcScript.OpenDoor();
            case 0x50, 0x51, 0x52, 0x53 -> parseScrollScript(opcode);
            case 0x54 -> parseDetectorScript();
            case 0x55 -> new NpcScript.RemoveScript();
            case 0x56 -> new NpcScript.Hurt();
            case 0x59 -> parseNpcDrawTileScript();
            case 0x5A -> parseIfTankRidingScript();
            case 0x5B -> parseIfTankHereScript();
            case 0x5C -> parseIfTreasureScript();
            case 0x5D -> parseIfHpScript();
            case 0x5F -> new TeamScript.Hide();
            case 0x4C, 0x5E, 0x73 -> parsePlayerTpScript(opcode);
            case 0x60 -> parsePlayerPullScript();
            case 0x61 -> new PlayerScript.Unpull();
            case 0x62 -> parseWaitEventScript();
            case 0x63, 0x64, 0x65 -> parseTextEventScript(opcode);
            case 0x66 -> parseTreasureCloseScript();
            case 0x67 -> new HenshinScript();
            case 0x69 -> parseFlashScreenScript();
            case 0x6A -> parseConveyorScript();
            case 0x6B -> parseSystemScript();
            case 0x6C -> new PlayerScript.Hide.All();
            case 0x6D -> parsePlayerFaceScript();
            case 0x6E -> parseChangeTankMapScript();
            case 0x6F -> new MedalScript();
            case 0x70, 0x71, 0x72 -> parseWaitTimeByteScript(opcode);
            default -> parseUnknownScript(opcode);
        };
    }

    // ========== 通用辅助方法 ==========

    /**
     * 获取脚本的指令参数长度（通过 IBlock 接口）
     */
    private int getArgsLength(BaseSpriteScript script) {
        if (script instanceof IBlock block) {
            return block.argsLength();
        }
        LOGGER.warn("{} 未实现 IBlock 接口，使用默认 argsLength=2",
                script.getClass().getSimpleName());
        return 2;
    }

    /**
     * 从 args 中获取 BLOCK 值（16进制字符串）
     */
    private String getBlockValue(ListScript listScript) {
        return listScript.args != null ? listScript.args.get(IBlock.BLOCK) : null;
    }

    /**
     * 查找目标脚本所属的父级列表（泛型版本，同时替代 findParentList 和 findParentListForScript）
     *
     * @return 直接包含 target 的脚本列表；找不到返回 null
     */
    private List<BaseSpriteScript> findParentListGeneric(List<BaseSpriteScript> scriptList, BaseSpriteScript target) {
        if (scriptList.contains(target)) {
            return scriptList;
        }
        for (BaseSpriteScript script : scriptList) {
            if (script instanceof ListScript listScript) {
                List<BaseSpriteScript> result = findParentListGeneric(listScript.scripts, target);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * 通用逻辑：按字节位置范围从父列表中提取子脚本、找到插入位置、将子脚本移入目标容器
     * 同时被 moveScriptsToIfBlock 和 moveScriptsToLoop 使用
     *
     * @param parentScripts 父级脚本列表
     * @param bodyStart     代码块起始字节位置（含）
     * @param bodyEnd       代码块结束字节位置（不含）
     * @param container     要移入的目标容器（IfScript 或 LoopScript）
     */
    private void moveScriptsByByteRange(List<BaseSpriteScript> parentScripts,
                                        int bodyStart, int bodyEnd, ListScript container) {
        List<BaseSpriteScript> bodyScripts = new ArrayList<>();
        List<BaseSpriteScript> toRemove = new ArrayList<>();
        for (BaseSpriteScript script : parentScripts) {
            if (script.bytePosition >= bodyStart && script.bytePosition < bodyEnd) {
                bodyScripts.add(script);
                toRemove.add(script);
            }
        }

        parentScripts.removeAll(toRemove);
        parentScripts.remove(container);

        // 查找原来第一个脚本的位置作为插入点
        int insertIndex = -1;
        for (int i = 0; i < parentScripts.size(); i++) {
            if (parentScripts.get(i).bytePosition >= bodyStart) {
                insertIndex = i;
                break;
            }
        }

        if (insertIndex >= 0) {
            parentScripts.add(insertIndex, container);
        } else {
            parentScripts.add(container);
        }

        container.scripts.addAll(bodyScripts);

        if (DEBUG) LOGGER.debug("移动了 {} 个脚本到 {}，插入位置={}",
                bodyScripts.size(), container.getClass().getSimpleName(), insertIndex);

        // 树结构已变更，使缓存失效
        invalidateAllScriptsCache();
    }

    /**
     * 获取全部脚本缓存（懒初始化）
     */
    private List<BaseSpriteScript> getAllScripts() {
        if (cachedAllScripts == null) {
            cachedAllScripts = new ArrayList<>();
            collectAllScripts(scripts, cachedAllScripts);
        }
        return cachedAllScripts;
    }

    /**
     * 当脚本树结构变更后调用，使缓存失效
     */
    private void invalidateAllScriptsCache() {
        cachedAllScripts = null;
    }

    // ========== 解析辅助方法 ==========

    private int readByte() throws IOException {
        int b = inputStream.read();
        if (b == -1) {
            throw new IOException("Unexpected end of stream");
        }
        position++;
        return b;
    }

    private byte[] readBytes(int count) throws IOException {
        byte[] bytes = new byte[count];
        for (int i = 0; i < count; i++) {
            bytes[i] = (byte) readByte();
        }
        return bytes;
    }

    // ========== 具体指令解析 ==========

    private BaseSpriteScript parseEndScript() {
        return new EndScript();
    }

    private BaseSpriteScript parseTextQuoteScript(int opcode) throws IOException {
        byte page, line;
        if (opcode == 0x76) {
            page = (byte) readByte();
            line = (byte) readByte();
        } else {
            page = switch (opcode) {
                case 0x01, 0x64 -> 0x05;
                case 0x02, 0x68 -> 0x0E;
                case 0x03 -> 0x0F;
                case 0x48 -> 0x0B;
                default -> 0x00;
            };
            line = (byte) readByte();
        }
        boolean isDynamic = (opcode == 0x68 || opcode == 0x03 || opcode == 0x76);
        return new TextScript.Quote(isDynamic, page, line);
    }

    private BaseSpriteScript parseEventOpenScript() throws IOException {
        byte hex = (byte) readByte();
        return new EventScript.Open(hex);
    }

    private BaseSpriteScript parseEventCloseScript() throws IOException {
        byte hex = (byte) readByte();
        return new EventScript.Close(hex);
    }

    private BaseSpriteScript parseIfEventScript() throws IOException {
        byte eventCode = (byte) readByte();
        byte blockLength = (byte) readByte();
        if (DEBUG) LOGGER.debug("解析 #if event: eventCode=0x{}, blockLength={}, 当前position={}",
                String.format("%02X", eventCode), blockLength & 0xFF, position);
        IfScript.Event ifScript = new IfScript.Event(eventCode);
        parseBlock(ifScript, blockLength);
        if (DEBUG) LOGGER.debug("#if event 解析完成, scripts.size={}", ifScript.scripts.size());
        return ifScript;
    }

    private BaseSpriteScript parseDoLoopScript() throws IOException {
        byte offset = (byte) readByte();  // 有符号偏移量
        int count = readByte();           // 循环次数

        LoopScript loopScript = new LoopScript(count);

        // bytePosition 会在 parseNextScript 中设置，这里先存储offset
        // 稍后在 processDoLoops 中使用 loopScript.bytePosition + offset 计算 bodyStartPosition
        loopScript.args = new java.util.HashMap<>();
        loopScript.args.put("offset", String.valueOf(offset));

        return loopScript;
    }

    private BaseSpriteScript parseIfXyScript() throws IOException {
        byte x = (byte) readByte();
        byte y = (byte) readByte();
        byte blockLength = (byte) readByte();
        IfScript.Xy ifScript = new IfScript.Xy(x, y);
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parseIfOptionYesScript() throws IOException {
        byte blockLength = (byte) readByte();
        IfScript.Option.Yes ifScript = new IfScript.Option.Yes();
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parseNpcMoveWanderScript(int opcode) throws IOException {
        if (opcode == 0x0A) {
            return new NpcScript.MoveScript.Wander();
        } else {
            byte x1 = (byte) readByte();
            byte y1 = (byte) readByte();
            byte x2 = (byte) readByte();
            byte y2 = (byte) readByte();
            return new NpcScript.MoveScript.Wander(x1, y1, x2, y2);
        }
    }

    private BaseSpriteScript parseFaceScript(int opcode) throws IOException {
        String direction = switch (opcode) {
            case 0x0E -> "left";
            case 0x0F -> "right";
            default -> throw new IllegalArgumentException("Invalid face opcode: " + opcode);
        };
        return switch (direction) {
            case "left" -> new NpcScript.FaceScript.Left();
            case "right" -> new NpcScript.FaceScript.Right();
            default -> throw new IllegalStateException();
        };
    }

    private BaseSpriteScript parseNpcMoveToScript() throws IOException {
        byte x = (byte) readByte();
        byte y = (byte) readByte();
        return new NpcScript.MoveScript.To(x, y);
    }

    private BaseSpriteScript parseJmpScript() throws IOException {
        byte offset = (byte) readByte();
        // 暂时使用偏移量作为临时标记，后续会替换为真正的标签名称（L0000格式）
        return new JmpScript(String.format("jmp_offset_%02X", offset & 0xFF));
    }

    private BaseSpriteScript parseWaitTimeScript() throws IOException {
        byte time = (byte) readByte();
        return new WaitScript.Time(time);
    }

    private BaseSpriteScript parseSleepScript() throws IOException {
        byte room = (byte) readByte();
        return new SleepScript(room);
    }

    private BaseSpriteScript parseNpcModelTileTypeScript() throws IOException {
        byte model = (byte) readByte();
        return new NpcScript.Model.TileType(model);
    }

    private BaseSpriteScript parseNpcAnimFrameScript() throws IOException {
        byte frame = (byte) readByte();
        return new NpcScript.Anim.Frame(frame);
    }

    private BaseSpriteScript parseMusicScript() throws IOException {
        byte music = (byte) readByte();
        return new MusicScript(music);
    }

    private BaseSpriteScript parseNpcMoveTpScript() throws IOException {
        byte x = (byte) readByte();
        byte y = (byte) readByte();
        return new NpcScript.MoveScript.Tp(x, y);
    }

    private BaseSpriteScript parseTeamJoinNowScript() throws IOException {
        byte hex = (byte) readByte();
        return new TeamScript.Join.Now(hex);
    }

    private BaseSpriteScript parseIfTeamScript() throws IOException {
        byte player = (byte) readByte();
        byte blockLength = (byte) readByte();
        IfScript.Team ifScript = new IfScript.Team(player);
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parseSpeakerScript() throws IOException {
        byte speaker = (byte) readByte();
        return new SpeakerScript(speaker);
    }

    private BaseSpriteScript parseMenuScript() throws IOException {
        byte b1 = (byte) readByte();
        byte b2 = (byte) readByte();
        return new MenuScript(b1, b2);
    }

    private BaseSpriteScript parseSceneScript() throws IOException {
        byte scene = (byte) readByte();
        if (scene == 0x00) {
            return new SceneScript.End();
        }
        return new SceneScript(scene);
    }

    private BaseSpriteScript parseNpcAttrsScript() throws IOException {
        byte attrs = (byte) readByte();
        return new NpcScript.Attrs(attrs);
    }

    private BaseSpriteScript parseIfHasOkTankScript() throws IOException {
        byte blockLength = (byte) readByte();
        IfScript.HasOkTank ifScript = new IfScript.HasOkTank();
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parseIfRideTankScript() throws IOException {
        byte blockLength = (byte) readByte();
        IfScript.RideTank ifScript = new IfScript.RideTank();
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parseIfTeammateDeadScript() throws IOException {
        byte teammate = (byte) readByte();
        byte blockLength = (byte) readByte();
        IfScript.TeammateDead ifScript = new IfScript.TeammateDead(teammate);
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parseRecoverScript() throws IOException {
        byte hex = (byte) readByte();
        return new RecoverScript(hex);
    }

    private BaseSpriteScript parseIfFaceScript() throws IOException {
        byte face = (byte) readByte();
        byte blockLength = (byte) readByte();
        String faceStr = switch (face) {
            case 0x00 -> "up";
            case 0x01 -> "down";
            case 0x02 -> "left";
            case 0x03 -> "right";
            default -> String.format("%02X", face);
        };
        IfScript.Face ifScript = new IfScript.Face(faceStr);
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parseNpcPatrolScript() throws IOException {
        byte type = (byte) readByte();
        return new NpcScript.Patrol(type);
    }

    private BaseSpriteScript parseNpcTankExitScript() throws IOException {
        byte model = (byte) readByte();
        return new NpcScript.ExitTank(model);
    }

    private BaseSpriteScript parseNpcAnimThrowScript() throws IOException {
        byte b1 = (byte) readByte();
        byte npc = (byte) readByte();
        return new NpcScript.Anim.Throw(b1, npc);
    }

    private BaseSpriteScript parseIfAreaScript() throws IOException {
        byte xx1 = (byte) readByte();
        byte xx2 = (byte) readByte();
        byte yy1 = (byte) readByte();
        byte yy2 = (byte) readByte();
        byte blockLength = (byte) readByte();
        IfScript.Area ifScript = new IfScript.Area(xx1, xx2, yy1, yy2);
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parseIfLevelScript() throws IOException {
        byte level = (byte) readByte();
        byte blockLength = (byte) readByte();
        IfScript.Level ifScript = new IfScript.Level(level);
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parseIfHasItemScript() throws IOException {
        byte item = (byte) readByte();
        byte blockLength = (byte) readByte();
        IfScript.HasItem ifScript = new IfScript.HasItem(item);
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parseNpcModelSetScript() throws IOException {
        byte model = (byte) readByte();
        return new NpcScript.Model.Set(model);
    }

    private BaseSpriteScript parseNpcMoveTpOffsetScript() throws IOException {
        byte xy = (byte) readByte();
        return new NpcScript.MoveScript.TpOffset(xy);
    }

    private BaseSpriteScript parseNpcBecomeScript() throws IOException {
        byte player = (byte) readByte();
        return new NpcScript.Become(player);
    }

    private BaseSpriteScript parsePlayerShowHideScript() throws IOException {
        byte type = (byte) readByte();
        if (type == 0x00) {
            return new PlayerScript.Hide();
        } else if (type == 0x01) {
            return new PlayerScript.Show();
        } else {
            return new PlayerScript.Show(type);
        }
    }

    private BaseSpriteScript parseNpcAnimPlayScript() throws IOException {
        byte anim = (byte) readByte();
        return new NpcScript.Anim.Play(anim);
    }

    private BaseSpriteScript parsePlayerBecomeScript() throws IOException {
        byte player = (byte) readByte();
        return new PlayerScript.Become(player);
    }

    private BaseSpriteScript parseScrollScript(int opcode) throws IOException {
        byte count = (byte) readByte();
        return switch (opcode) {
            case 0x50 -> new ScrollScript.Up(count);
            case 0x51 -> new ScrollScript.Down(count);
            case 0x52 -> new ScrollScript.Left(count);
            case 0x53 -> new ScrollScript.Right(count);
            default -> throw new IllegalArgumentException("Invalid scroll opcode: " + opcode);
        };
    }

    private BaseSpriteScript parseDetectorScript() throws IOException {
        byte range = (byte) readByte();
        byte blockLength = (byte) readByte();
        DetectorScript detectorScript = new DetectorScript(range);
        parseBlock(detectorScript, blockLength);
        return detectorScript;
    }

    private BaseSpriteScript parseNpcDrawTileScript() throws IOException {
        byte tile = (byte) readByte();
        return new NpcScript.DrawTile(tile);
    }

    private BaseSpriteScript parseIfTankRidingScript() throws IOException {
        byte tank = (byte) readByte();
        byte blockLength = (byte) readByte();
        IfScript.Tank.Riding ifScript = new IfScript.Tank.Riding(tank);
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parseIfTankHereScript() throws IOException {
        byte tank = (byte) readByte();
        byte blockLength = (byte) readByte();
        IfScript.Tank.Here ifScript = new IfScript.Tank.Here(tank);
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parseIfTreasureScript() throws IOException {
        byte treasure = (byte) readByte();
        byte blockLength = (byte) readByte();
        IfScript.Treasure ifScript = new IfScript.Treasure(treasure);
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parseIfHpScript() throws IOException {
        byte hp = (byte) readByte();
        byte blockLength = (byte) readByte();
        IfScript.Hp ifScript = new IfScript.Hp(hp);
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parsePlayerTpScript(int opcode) throws IOException {
        if (opcode == 0x4C) {
            return new PlayerScript.Tp();
        }
        byte map = (byte) readByte();
        byte x = (byte) readByte();
        byte y = (byte) readByte();
        boolean quiet = (opcode == 0x73);
        return new PlayerScript.Tp(map, x, y, quiet);
    }

    private BaseSpriteScript parsePlayerPullScript() throws IOException {
        byte pull = (byte) readByte();
        return new PlayerScript.Pull(pull);
    }

    private BaseSpriteScript parseEventWaitScript() throws IOException {
        byte hex = (byte) readByte();
        return new EventScript.Wait(hex);
    }

    private BaseSpriteScript parseBattleScript() throws IOException {
        byte monster = (byte) readByte();
        byte eventCode = (byte) readByte();
        byte stage = (byte) readByte();
        return new BattleScript(monster, eventCode, stage);
    }

    private BaseSpriteScript parseWaitEventScript() throws IOException {
        byte hex = (byte) readByte();
        return new WaitScript.Event(hex);
    }

    private BaseSpriteScript parseTextEventScript(int opcode) throws IOException {
        byte eventCode = (byte) readByte();
        byte trueLine = (byte) readByte();
        byte falseLine = (byte) readByte();
        byte page = switch (opcode) {
            case 0x63 -> 0x05;
            case 0x64 -> 0x0E;
            case 0x65 -> 0x0B;
            default -> throw new IllegalArgumentException("Invalid text event opcode: " + opcode);
        };
        return new TextScript.Event(page, eventCode, trueLine, falseLine);
    }

    private BaseSpriteScript parseTreasureCloseScript() throws IOException {
        byte treasureId = (byte) readByte();
        return new TreasureScript.Close(treasureId);
    }

    private BaseSpriteScript parsePlayerFaceScript() throws IOException {
        byte direction = (byte) readByte();
        return switch (direction) {
            case 0x40 -> new PlayerScript.Face.Up();
            case 0x41 -> new PlayerScript.Face.Down();
            case 0x42 -> new PlayerScript.Face.Left();
            case 0x43 -> new PlayerScript.Face.Right();
            default -> new PlayerScript.Face(direction);
        };
    }

    private BaseSpriteScript parseWaitTimeByteScript(int opcode) throws IOException {
        // 70 71 72
        return new WaitScript.Time(switch (opcode) {
            case 0x71 -> WaitScript.Time.NORMAL;
            case 0x72 -> WaitScript.Time.LONG;
            default -> WaitScript.Time.SHORT;
        });
    }

    private BaseSpriteScript parseIfMoneyScript() throws IOException {
        byte money = (byte) readByte();
        byte blockLength = (byte) readByte();
        IfScript.Money ifScript = new IfScript.Money(money);
        parseBlock(ifScript, blockLength);
        return ifScript;
    }

    private BaseSpriteScript parseTeamJoinScript() throws IOException {
        byte hex = (byte) readByte();
        return new TeamScript.Join(hex);
    }

    private BaseSpriteScript parseTeamLeaveScript() throws IOException {
        byte hex = (byte) readByte();
        return new TeamScript.Leave(hex);
    }

    private BaseSpriteScript parseQuakeScript() throws IOException {
        byte time = (byte) readByte();
        return new QuakeScript(time);
    }

    private BaseSpriteScript parseMoneySpendScript() throws IOException {
        byte money = (byte) readByte();
        return new MoneyScript.Spend(money);
    }

    private BaseSpriteScript parseIfDrankScript() throws IOException {
        byte blockLength = (byte) readByte();
        IfScript.Drank ifDrank = new IfScript.Drank();
        parseBlock(ifDrank, blockLength);
        return ifDrank;
    }

    private BaseSpriteScript parseFlashScreenScript() throws IOException {
        byte time = (byte) readByte();
        return new FlashScreenScript(time);
    }

    private BaseSpriteScript parseConveyorScript() throws IOException {
        byte dir = (byte) readByte();
        String dirStr = switch (dir) {
            case 0x01 -> "forward";
            case 0x02 -> "reverse";
            default -> "stop";
        };
        return new ConveyorScript(dirStr);
    }

    private BaseSpriteScript parseSystemScript() throws IOException {
        byte commandCode = (byte) readByte();
        return new SystemScript(commandCode);
    }

    private BaseSpriteScript parseTilesSpriteScript() throws IOException {
        byte bank = (byte) readByte();
        return new TilesScript.Sprite(bank);
    }

    private BaseSpriteScript parseGivePlayerItemScript() throws IOException {
        byte item = (byte) readByte();
        byte text = (byte) readByte();
        return new GiveScript.Player.Item(item, text);
    }

    private BaseSpriteScript parseSwapPlayerItemScript() throws IOException {
        byte b1 = (byte) readByte();
        byte b2 = (byte) readByte();
        return new SwapScript.Player.Item(b1, b2);
    }

    private BaseSpriteScript parseChangeTankMapScript() throws IOException {
        byte fromMap = (byte) readByte();
        byte toMap = (byte) readByte();
        return new ChangeTankMapScript(fromMap, toMap);
    }

    /**
     * 解析代码块（用于条件判断、循环等包含子脚本的结构）
     */
    private void parseBlock(ListScript listScript, byte blockLength) throws IOException {
        if (blockLength == 0) {
            // 自身循环代码块
            listScript.isLoopList = true;
            if (DEBUG) LOGGER.debug("parseBlock: blockLength为0，直接返回");
            return;
        }

        // 检查 blockLength 是否为负数（有符号字节）
        int signedBlockLength = blockLength & 0xFF;
        if (signedBlockLength > 127) {
            signedBlockLength = signedBlockLength - 256; // 转换为负数
        }

        if (signedBlockLength < 0) {
            // 负数表示向上回溯，这种情况下不立即解析，而是稍后处理
            // 将 blockLength 存储到 args 中供后续处理
            if (listScript.args == null) {
                listScript.args = new java.util.HashMap<>();
            }
            listScript.args.put("blockLength", String.valueOf(signedBlockLength));
            if (DEBUG) LOGGER.debug("parseBlock: 发现向上代码块，blockLength={}, signedBlockLength={}",
                    blockLength & 0xFF, signedBlockLength);
            return;
        }

        // blockLength 是从指令起始位置（opcode）到块结束的总长度
        // 通过 IBlock 接口的 argsLength() 方法获取指令的总长度
        // actualBlockLength = blockLength - argsLength()
        int argsLength = getArgsLength(listScript);

        int actualBlockLength = signedBlockLength - argsLength;

        if (DEBUG)
            LOGGER.debug("parseBlock: blockLength={}, signedBlockLength={}, argsLength={}, actualBlockLength={}, 当前position={}",
                    blockLength & 0xFF, signedBlockLength, argsLength, actualBlockLength, position);

        if (actualBlockLength <= 0) {
            if (DEBUG) LOGGER.debug("parseBlock: actualBlockLength <= 0，直接返回");
            // 即使没有实际内容，也要保存 blockLength
            if (listScript.args == null) {
                listScript.args = new java.util.HashMap<>();
            }
            listScript.args.put(IBlock.BLOCK, String.format("%02X", blockLength & 0xFF));
            return;
        }

        int startPos = position;
        int endPos = startPos + actualBlockLength;
        if (DEBUG) LOGGER.debug("parseBlock: startPos={}, endPos={}", startPos, endPos);

        int scriptCount = 0;
        while (position < endPos && inputStream.available() > 0) {
            int beforeRead = position;
            if (DEBUG) LOGGER.debug("parseBlock: 准备读取脚本, beforeRead={}, endPos={}", beforeRead, endPos);
            BaseSpriteScript script = parseNextScript();
            if (script != null) {
                if (DEBUG) LOGGER.debug("parseBlock: 读取到脚本 {}, 类型={}, position={}",
                        scriptCount++, script.getClass().getSimpleName(), position);
                // 只要脚本开始位置在块内，就添加它
                if (beforeRead < endPos) {
                    listScript.scripts.add(script);
                    if (DEBUG) LOGGER.debug("parseBlock: 脚本已添加到列表, 当前列表大小={}", listScript.scripts.size());
                    // 如果读取后超出了边界，退出循环
                    if (position > endPos) {
                        if (DEBUG) LOGGER.debug("块解析结束: 位置 {} 超出边界 {}", position, endPos);
                        break;
                    }
                } else {
                    // 脚本开始位置已经超出边界，退出
                    if (DEBUG) LOGGER.debug("块解析结束: 脚本开始位置 {} 超出边界 {}", beforeRead, endPos);
                    break;
                }
            } else {
                if (DEBUG) LOGGER.debug("块解析结束: parseNextScript返回null");
                break;
            }
        }
        if (DEBUG) LOGGER.debug("parseBlock: 总共解析了 {} 个脚本", scriptCount);

        // 保存 blockLength 到 args[BLOCK] 中，供后续冲突检测使用
        if (DEBUG) LOGGER.debug("parseBlock: 准备保存 blockLength, listScript={}, blockLength=0x{}",
                listScript.getClass().getSimpleName(), String.format("%02X", blockLength & 0xFF));
        if (listScript.args == null) {
            listScript.args = new java.util.HashMap<>();
            if (DEBUG) LOGGER.debug("parseBlock: 创建了新的 args HashMap");
        }
        listScript.args.put(IBlock.BLOCK, String.format("%02X", blockLength & 0xFF));
        if (DEBUG) LOGGER.debug("parseBlock: 成功保存 blockLength 到 args[BLOCK] = {}, 当前 args 内容: {}",
                String.format("%02X", blockLength & 0xFF), listScript.args);
    }

    /**
     * 处理 JMP 指令的标签
     * 1. 为所有脚本生成唯一的标签名称
     * 2. 将 JMP 指令的偏移量转换为对应的标签名称
     */
    private void processJmpLabels() {
        // 第一步：收集所有脚本并按字节位置排序
        List<BaseSpriteScript> allScripts = getAllScripts();

        // 按字节位置排序
        allScripts.sort((a, b) -> Integer.compare(a.bytePosition, b.bytePosition));

        // 第二步：为每个脚本生成唯一的标签名称（使用序号）
        int labelCounter = 0;
        for (BaseSpriteScript script : allScripts) {
            script.label = String.format("L%04d", labelCounter++);
            if (DEBUG) LOGGER.debug("为脚本 {} 设置标签: {} at byte position {}",
                    script.getClass().getSimpleName(), script.label, script.bytePosition);
        }

        // 第三步：更新 JMP 指令的目标标签
        for (BaseSpriteScript script : scripts) {
            updateJmpLabelsInScript(script, allScripts);
        }
    }

    /**
     * 递归收集所有脚本
     */
    private void collectAllScripts(List<BaseSpriteScript> scriptList, List<BaseSpriteScript> allScripts) {
        for (BaseSpriteScript script : scriptList) {
            allScripts.add(script);

            // 如果是包含子脚本的块，递归收集
            if (script instanceof ListScript listScript) {
                collectAllScripts(listScript.scripts, allScripts);
            }
        }
    }

    /**
     * 递归更新 JMP 指令的标签（处理嵌套的脚本）
     */
    private void updateJmpLabelsInScript(BaseSpriteScript script, List<BaseSpriteScript> allScripts) {
        if (script instanceof JmpScript jmpScript) {
            // 从标签名称中提取偏移量
            String oldLabel = jmpScript.targetLabel;
            if (oldLabel.startsWith("jmp_offset_")) {
                try {
                    int offset = Integer.parseInt(oldLabel.substring(11), 16);
                    // 将有符号字节转换：如果 > 127，则为负数
                    if (offset > 127) {
                        offset = offset - 256;
                    }

                    // 关键修复：JMP 偏移量是从 JMP 指令自身开始计算的
                    // 18(offset=0) 05(offset=1) ...
                    // 目标位置 = JMP起始位置 + 偏移量
                    // offset可以是负数（向上跳转）或正数（向下跳转）
                    int targetPos = jmpScript.bytePosition + offset;

                    if (DEBUG)
                        LOGGER.debug("JMP 计算: JMP位置={}, 偏移量={}, 目标位置={}", jmpScript.bytePosition, offset, targetPos);

                    // 查找目标位置的标签
                    String targetLabel = findLabelAtBytePosition(targetPos, allScripts);
                    if (targetLabel != null) {
                        jmpScript.targetLabel = targetLabel;
                        if (DEBUG) LOGGER.debug("JMP 标签转换: {} -> {}", oldLabel, targetLabel);
                    } else {
                        LOGGER.warn("未找到目标位置 {} 的标签，可用位置: {}", targetPos,
                                allScripts.stream().map(s -> String.valueOf(s.bytePosition)).reduce((a, b) -> a + ", " + b).orElse(""));
                        // 如果找不到目标，保持原始偏移量格式以便调试
                        jmpScript.targetLabel = String.format("offset_%d_at_pos_%d", offset, jmpScript.bytePosition);
                    }
                } catch (NumberFormatException e) {
                    LOGGER.warn("无法解析 JMP 标签: {}", oldLabel);
                }
            }
        } else if (script instanceof ListScript listScript) {
            // 递归处理嵌套的脚本
            for (BaseSpriteScript nestedScript : listScript.scripts) {
                updateJmpLabelsInScript(nestedScript, allScripts);
            }
        }
    }

    /**
     * 在脚本列表中查找指定字节位置的标签名称
     */
    private String findLabelAtBytePosition(int targetPos, List<BaseSpriteScript> allScripts) {
        for (BaseSpriteScript script : allScripts) {
            if (script.bytePosition == targetPos && script.label != null) {
                return script.label;
            }
        }
        return null;
    }

    /**
     * 处理 Do-While 循环
     * 1. 收集所有 LoopScript
     * 2. 根据 offset 找到循环体起始位置
     * 3. 将循环体内的脚本移动到 LoopScript.scripts 中
     */
    private void processDoLoops() {
        // 收集所有 LoopScript
        List<LoopScript> loopScripts = new ArrayList<>();
        collectLoopScripts(scripts, loopScripts);

        // 为每个 LoopScript 处理循环体
        for (LoopScript loopScript : loopScripts) {
            if (loopScript.args != null && loopScript.args.containsKey("offset")) {
                int offset = Integer.parseInt(loopScript.args.get("offset"));
                int loopPosition = loopScript.bytePosition;
                int bodyStartPosition = loopPosition + offset;  // offset是负数，向上回溯

                if (DEBUG) LOGGER.debug("处理 DoLoop: position={}, offset={}, bodyStart={}",
                        loopPosition, offset, bodyStartPosition);

                // 找到 LoopScript 真正所属的父级脚本列表（可能在嵌套的 if 块内部）
                List<BaseSpriteScript> realParentList = findParentListForScript(scripts, loopScript);

                // 从父级脚本列表中找到并移动循环体内的脚本
                moveScriptsToLoop(realParentList, bodyStartPosition, loopPosition, loopScript);

                // 清除临时存储的参数
                loopScript.args.remove("offset");
            }
        }
    }

    /**
     * 处理向上的 if 代码块（blockLength 为负数）
     * 1. 收集所有带有负 blockLength 的 if 指令
     * 2. 根据 blockLength 找到代码块起始位置
     * 3. 将目标位置的标签存储到 if 指令中
     */
    private void processNegativeBlocks() {
        // 收集所有带有负 blockLength 的 ListScript
        List<ListScript> negativeBlockScripts = new ArrayList<>();
        collectNegativeBlockScripts(scripts, negativeBlockScripts);

        // 为每个负 blockLength 的脚本处理代码块
        for (ListScript listScript : negativeBlockScripts) {
            if (listScript.args != null && listScript.args.containsKey("blockLength")) {
                int blockLength = Integer.parseInt(listScript.args.get("blockLength"));
                int ifPosition = listScript.bytePosition;

                int argsLength = getArgsLength(listScript);

                // blockLength 是从 opcode 到块结束的总长度（负数）
                // bodyStart = ifPosition + blockLength
                int bodyStartPosition = ifPosition + blockLength;

                if (DEBUG) LOGGER.debug("处理向上代码块: ifPosition={}, blockLength={}, argsLength={}, bodyStart={}",
                        ifPosition, blockLength, argsLength, bodyStartPosition);

                // 查找目标位置的标签
                String targetLabel = findLabelAtBytePosition(bodyStartPosition, getAllScripts());

                if (targetLabel != null) {
                    // 将目标标签存储到 targetLabel 成员变量中
                    if (listScript instanceof IfScript ifScript) {
                        ifScript.setTargetLabel(targetLabel);
                    }
                    if (DEBUG) LOGGER.debug("向上 if 目标标签: bodyStart={} -> {}", bodyStartPosition, targetLabel);
                } else {
                    LOGGER.warn("未找到向上 if 的目标位置 {} 的标签", bodyStartPosition);
                }

                // 将负的 blockLength 转换为有符号字节格式并保存到 args[BLOCK] 中
                // 例如：-1 -> 0xFF, -2 -> 0xFE
                int unsignedBlockLength = blockLength & 0xFF;
                if (listScript.args == null) {
                    listScript.args = new java.util.HashMap<>();
                }
                listScript.args.put(IBlock.BLOCK, String.format("%02X", unsignedBlockLength));

                if (DEBUG)
                    LOGGER.debug("向上 if BLOCK值设置: ifPosition={}, blockLength={}, unsignedBlockLength=0x{}, args[BLOCK]={}",
                            ifPosition, blockLength, String.format("%02X", unsignedBlockLength), listScript.args.get(IBlock.BLOCK));

                // 清除临时存储的参数
                listScript.args.remove("blockLength");
            }
        }
    }

    /**
     * 转换冲突的 if 为标签跳转模式
     * 如果 #if 命令的结束点在其它结构中，将其转换为标签跳转模式
     */
    private void convertConflictingIfsToJumps() {
        // 前面的 processDoLoops 可能已改变树结构，使缓存失效
        invalidateAllScriptsCache();
        if (DEBUG) LOGGER.debug("开始检查冲突的 if 命令...");

        // 收集所有 IfScript
        List<IfScript> allIfScripts = new ArrayList<>();
        collectIfScripts(scripts, allIfScripts);

        if (DEBUG) LOGGER.debug("共找到 {} 个 IfScript", allIfScripts.size());

        // 按嵌套深度降序排列（最内层的 if 最先处理）
        // 这样内层 if 先被提升到父级，外层 if 后处理时能正确工作
        allIfScripts.sort((a, b) -> {
            int depthA = getNestingDepth(scripts, a);
            int depthB = getNestingDepth(scripts, b);
            return Integer.compare(depthB, depthA);  // 降序：深的先处理
        });

        if (DEBUG) LOGGER.debug("IfScript 处理顺序（按嵌套深度降序）:");
        for (IfScript ifs : allIfScripts) {
            if (DEBUG) LOGGER.debug("  depth={} {} @ {}", getNestingDepth(scripts, ifs),
                    ifs.getClass().getSimpleName(), ifs.bytePosition);
        }

        // 检查每个 IfScript 的结束点是否在其他结构中
        for (IfScript ifScript : allIfScripts) {
            if (DEBUG) LOGGER.debug("检查 IfScript: {}, bytePosition={}, hasTargetLabel={}",
                    ifScript.getClass().getSimpleName(), ifScript.bytePosition, ifScript.hasTargetLabel());

            // 只处理已经有 targetLabel 的（已经是跳转模式）或者需要转换的
            if (!ifScript.hasTargetLabel()) {
                // 检查是否需要转换为跳转模式
                if (shouldConvertToJump(ifScript)) {
                    if (DEBUG) LOGGER.debug("需要将 if 转换为跳转模式");
                    convertIfToJump(ifScript);
                } else {
                    if (DEBUG) LOGGER.debug("不需要转换，保持代码块模式");
                }
            } else {
                if (DEBUG) LOGGER.debug("已经是跳转模式，跳过");
            }
        }

        if (DEBUG) LOGGER.debug("冲突检查完成");
    }

    /**
     * 计算 IfScript 在脚本树中的嵌套深度
     *
     * @param scriptList 当前层级的脚本列表（从顶层 scripts 开始）
     * @param target     要查找的 IfScript
     * @return 嵌套深度（顶层=0，每深入一层+1），如果找不到返回 -1
     */
    private int getNestingDepth(List<BaseSpriteScript> scriptList, IfScript target) {
        for (BaseSpriteScript script : scriptList) {
            if (script == target) {
                return 0;
            }
            if (script instanceof ListScript listScript) {
                int depth = getNestingDepth(listScript.scripts, target);
                if (depth >= 0) {
                    return depth + 1;
                }
            }
        }
        return -1;
    }

    /**
     * 递归收集所有 IfScript
     */
    private void collectIfScripts(List<BaseSpriteScript> scriptList, List<IfScript> ifScripts) {
        for (BaseSpriteScript script : scriptList) {
            if (script instanceof IfScript ifScript) {
                ifScripts.add(ifScript);
            }
            // 递归收集嵌套的脚本
            if (script instanceof ListScript listScript) {
                collectIfScripts(listScript.scripts, ifScripts);
            }
        }
    }

    /**
     * 判断是否应该将 if 转换为跳转模式
     * 检查 if 的结束点是否在其他结构中
     */
    private boolean shouldConvertToJump(IfScript ifScript) {
        if (DEBUG) LOGGER.debug("shouldConvertToJump 被调用: {}", ifScript.getClass().getSimpleName());

        // 从 args 中获取 blockLength
        if (ifScript.args == null) {
            if (DEBUG) LOGGER.debug("args 为 null，无法判断");
            return false; // 没有 blockLength 信息，无法判断
        }

        String blockValue = ifScript.args.get(IBlock.BLOCK);
        if (DEBUG) LOGGER.debug("args[BLOCK] = {}", blockValue);

        if (blockValue == null) {
            if (DEBUG) LOGGER.debug("args[BLOCK] 为 null，无法判断");
            return false;
        }

        int blockLength = Integer.parseInt(blockValue, 16);
        int ifEndPosition = ifScript.bytePosition + blockLength;

        if (DEBUG) LOGGER.debug("检查 if 结束点: ifPosition={}, blockLength=0x{}, ifEndPosition={}",
                ifScript.bytePosition, String.format("%02X", blockLength), ifEndPosition);

        // 检查结束点是否在其他结构中
        boolean result = isPositionInsideOtherStructure(ifScript, ifEndPosition);
        if (DEBUG) LOGGER.debug("isPositionInsideOtherStructure 返回: {}", result);

        return result;
    }

    /**
     * 检查指定位置是否在其他结构（LoopScript、其他 IfScript）内部
     * 注意：
     * - 跳过当前 if 本身
     * - 跳过当前 if 的所有祖先结构（包含它的外层结构），因为结束点落在祖先范围内是正常的
     * - 不跳过后代结构，因为结束点落在后代/兄弟子结构范围内才是真正的冲突
     */
    private boolean isPositionInsideOtherStructure(IfScript currentIf, int position) {
        // 收集所有脚本（包括嵌套的）进行全局检查
        List<BaseSpriteScript> allScripts = getAllScripts();

        for (BaseSpriteScript script : allScripts) {
            // 1. 跳过当前 if 本身
            if (script == currentIf) {
                continue;
            }

            // 2. 跳过当前 if 的祖先结构（包含 currentIf 的外层结构）
            // 结束点落在祖先范围内是完全正常的，不算冲突
            if (isAncestorOf(script, currentIf)) {
                continue;
            }

            // 检查是否是 ListScript（包含子脚本的结构）
            if (script instanceof ListScript listScript) {
                int structureStart = script.bytePosition;
                int structureEnd = structureStart;

                // 通过 IBlock 接口获取长度
                if (script instanceof IBlock block) {
                    String blockValue = getBlockValue(listScript);
                    if (blockValue != null) {
                        int length = Integer.parseInt(blockValue, 16);
                        structureEnd = structureStart + length;
                    }
                }

                // 检查 position 是否在这个结构内部（不包括边界）
                if (position > structureStart && position < structureEnd) {
                    if (DEBUG) LOGGER.debug("位置 {} 在结构 {} 内部 ({} - {}) [冲突]", position,
                            script.getClass().getSimpleName(), structureStart, structureEnd);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查 potentialAncestor 是否是 target 的祖先结构
     * （即 potentialAncestor 直接或间接包含 target）
     *
     * @param potentialAncestor 可能的祖先脚本
     * @param target            目标脚本
     * @return 如果 potentialAncestor 是 target 的祖先则返回 true
     */
    private boolean isAncestorOf(BaseSpriteScript potentialAncestor, BaseSpriteScript target) {
        if (!(potentialAncestor instanceof ListScript listAncestor)) {
            return false;
        }
        // 检查 target 是否在潜在祖先的 scripts 列表中（直接或间接）
        return containsScript(listAncestor.scripts, target);
    }

    /**
     * 递归检查目标脚本是否在脚本列表中（直接或在嵌套的子列表中）
     */
    private boolean containsScript(List<BaseSpriteScript> scriptList, BaseSpriteScript target) {
        for (BaseSpriteScript script : scriptList) {
            if (script == target) {
                return true;
            }
            if (script instanceof ListScript listScript) {
                if (containsScript(listScript.scripts, target)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 将 if 转换为跳转模式
     */
    private void convertIfToJump(IfScript ifScript) {
        // 1. 计算结束点位置
        int argsLength = getArgsLength(ifScript);

        if (ifScript.args == null || !ifScript.args.containsKey(IBlock.BLOCK)) {
            LOGGER.warn("无法转换 if 为跳转模式：缺少 blockLength 信息");
            return;
        }

        int blockLength = Integer.parseInt(ifScript.args.get(IBlock.BLOCK), 16);
        int ifEndPosition = ifScript.bytePosition + blockLength;

        if (DEBUG)
            LOGGER.debug("转换 if 为跳转模式: ifPosition={}, ifEndPosition={}", ifScript.bytePosition, ifEndPosition);

        // 2. 找到结束点的标签
        String targetLabel = findLabelAtBytePosition(ifEndPosition, getAllScripts());

        if (targetLabel == null) {
            LOGGER.warn("未找到结束点 {} 的标签，无法转换为跳转模式", ifEndPosition);
            return;
        }

        if (DEBUG) LOGGER.debug("找到目标标签: {} at position {}", targetLabel, ifEndPosition);

        // 3. 设置 targetLabel（使用跳转模式而非向上if模式）
        ifScript.setTargetLabel(targetLabel);

        // 4. 将子脚本从 ifScript 中提取出来，提升到父级同级
        // （因为 if 现在是跳转模式，不再包裹子脚本）
        // 需要找到 ifScript 真正所属的父级列表，而不是顶层列表
        List<BaseSpriteScript> realParentList = findParentList(scripts, ifScript);
        promoteChildScriptsToParent(realParentList, ifScript);

        if (DEBUG) LOGGER.debug("已将 if 转换为跳转模式: #if -> #if {} {}",
                ifScript.op, targetLabel);
    }

    /**
     * 将 ifScript 的子脚本提升到父级列表中的同一级别
     * 在父级列表中找到 ifScript 的位置，在其后插入所有子脚本，然后清空 ifScript.scripts
     */
    private void promoteChildScriptsToParent(List<BaseSpriteScript> parentScripts, IfScript ifScript) {
        if (ifScript.scripts.isEmpty()) {
            return;
        }

        int index = parentScripts.indexOf(ifScript);
        if (index < 0) {
            LOGGER.warn("未在父级列表中找到 ifScript，无法提升子脚本");
            return;
        }

        // 在 ifScript 后面位置插入所有子脚本
        for (int i = 0; i < ifScript.scripts.size(); i++) {
            parentScripts.add(index + 1 + i, ifScript.scripts.get(i));
        }

        if (DEBUG) LOGGER.debug("将 {} 个子脚本从 {} 提升到父级同级", ifScript.scripts.size(),
                ifScript.getClass().getSimpleName());

        // 清空 ifScript 的 scripts 列表
        ifScript.scripts.clear();

        // 树结构已变更，使缓存失效
        invalidateAllScriptsCache();
    }

    /**
     * 查找 ifScript 真正所属的父级脚本列表
     */
    private List<BaseSpriteScript> findParentList(List<BaseSpriteScript> scriptList, IfScript target) {
        return findParentListGeneric(scriptList, target);
    }

    /**
     * 递归收集所有带有负 blockLength 的 ListScript
     */
    private void collectNegativeBlockScripts(List<BaseSpriteScript> scriptList, List<ListScript> negativeBlockScripts) {
        for (BaseSpriteScript script : scriptList) {
            if (script instanceof ListScript listScript && listScript.args != null && listScript.args.containsKey("blockLength")) {
                int blockLength = Integer.parseInt(listScript.args.get("blockLength"));
                if (blockLength < 0) {
                    negativeBlockScripts.add(listScript);
                }
            } else if (script instanceof ListScript listScript) {
                collectNegativeBlockScripts(listScript.scripts, negativeBlockScripts);
            }
        }
    }

    /**
     * 将指定范围内的脚本移动到 IfScript 的代码块中
     */
    private void moveScriptsToIfBlock(List<BaseSpriteScript> parentScripts, int bodyStart, int ifPosition, ListScript ifScript, int argsLength) {
        moveScriptsByByteRange(parentScripts, bodyStart, ifPosition, ifScript);
    }

    /**
     * 递归收集所有 LoopScript
     */
    private void collectLoopScripts(List<BaseSpriteScript> scriptList, List<LoopScript> loopScripts) {
        for (BaseSpriteScript script : scriptList) {
            if (script instanceof LoopScript loopScript) {
                loopScripts.add(loopScript);
            } else if (script instanceof ListScript listScript) {
                collectLoopScripts(listScript.scripts, loopScripts);
            }
        }
    }

    /**
     * 查找任意脚本真正所属的父级脚本列表
     */
    private List<BaseSpriteScript> findParentListForScript(List<BaseSpriteScript> scriptList, BaseSpriteScript target) {
        return findParentListGeneric(scriptList, target);
    }

    /**
     * 将指定范围内的脚本移动到 LoopScript 中
     */
    private void moveScriptsToLoop(List<BaseSpriteScript> parentScripts, int bodyStart, int loopPosition, LoopScript loopScript) {
        moveScriptsByByteRange(parentScripts, bodyStart, loopPosition, loopScript);
    }

    private BaseSpriteScript parseUnknownScript(int opcode) {
        LOGGER.warn("遇到未知的操作码: 0x{} at position {}", "%02X".formatted(opcode), position - 1);
        return new CodeScript(new byte[]{(byte) opcode});
    }

    /**
     * 收集所有被实际引用的标签，然后清除未被引用的标签
     * 遍历脚本树，从 JmpScript.targetLabel、IfScript.targetLabel 中收集
     */
    private void collectReferencedLabels() {
        collectReferencedLabels(scripts);
        if (DEBUG) LOGGER.debug("收集到 {} 个被引用的标签: {}", referencedLabels.size(), referencedLabels);
        // 清除所有未被引用的脚本的 label 字段，这样 toScript() 递归输出时也不会显示它们
        clearUnreferencedLabels(scripts);
    }

    private void collectReferencedLabels(List<BaseSpriteScript> scriptList) {
        for (BaseSpriteScript script : scriptList) {
            // 收集 JMP 指令的目标标签
            if (script instanceof JmpScript jmp && jmp.targetLabel != null) {
                referencedLabels.add(jmp.targetLabel);
            }
            // 收集 if 的目标标签
            if (script instanceof IfScript ifScript && ifScript.getTargetLabel() != null) {
                referencedLabels.add(ifScript.getTargetLabel());
            }
            // 递归处理子脚本
            if (script instanceof ListScript listScript) {
                collectReferencedLabels(listScript.scripts);
            }
        }
    }

    /**
     * 递归清除所有未被引用的脚本的 label 字段
     */
    private void clearUnreferencedLabels(List<BaseSpriteScript> scriptList) {
        for (BaseSpriteScript script : scriptList) {
            if (script.label != null && !referencedLabels.contains(script.label)) {
                script.label = null;
            }
            if (script instanceof ListScript listScript) {
                clearUnreferencedLabels(listScript.scripts);
            }
        }
    }

    /**
     * 将解析结果转换为文本格式
     *
     * @return 脚本的文本表示
     */
    public String toText() {
        StringBuilder sb = new StringBuilder();
        for (BaseSpriteScript script : scripts) {
            // 只输出被实际引用的标签
            if (script.label != null && !script.label.isEmpty()
                    && referencedLabels.contains(script.label)) {
                sb.append(script.label).append(":\n");
            }
            String scriptText = script.toScript();
            if (scriptText != null && !scriptText.isEmpty()) {
                sb.append(scriptText).append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 静态便捷方法：直接反解析字节数组为文本
     *
     * @param data 字节数组
     * @return 脚本的文本表示
     */
    public static String decompileToText(byte[] data) {
        SpriteScriptDecompiler decompiler = new SpriteScriptDecompiler(data);
        decompiler.decompile();
        return decompiler.toText();
    }
}
