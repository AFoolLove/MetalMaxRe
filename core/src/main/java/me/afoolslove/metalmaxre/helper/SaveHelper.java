package me.afoolslove.metalmaxre.helper;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.MultipleMetalMaxRe;
import me.afoolslove.metalmaxre.RomBuffer;
import me.afoolslove.metalmaxre.RomVersion;
import me.afoolslove.metalmaxre.editors.player.Player;
import me.afoolslove.metalmaxre.editors.save.ISaveEditor;
import me.afoolslove.metalmaxre.editors.save.SaveData;
import me.afoolslove.metalmaxre.editors.save.SaveEditorImpl;
import me.afoolslove.metalmaxre.editors.save.SavePlayerAttribute;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

public class SaveHelper {
    /**
     * 加载sav文件存档
     *
     * @return 存档数据
     */
    public static ISaveEditor loadSav(@NotNull MetalMaxRe metalMaxRe, byte[] data) {
        ByteBuffer buffer = ByteBuffer.allocate(0x036E);
        buffer.put(data, 0x400, 0x036E);
        SaveData saveData = new SaveData(buffer);
        return new SaveEditorImpl(metalMaxRe, saveData);
    }


    public static void main(String[] args) throws Exception {
        MultipleMetalMaxRe multipleMetalMaxRe = new MultipleMetalMaxRe();
        MetalMaxRe metalMaxRe = multipleMetalMaxRe.create(new RomBuffer(RomVersion.getChinese(), (Path) null), true);
        metalMaxRe.getEditorManager().loadEditors().get();

        //byte[] bytes = {}; // Files.readAllBytes(Path.of("*.sav"));
        byte[] bytes = Files.readAllBytes(Path.of("E:\\emulator\\fceux\\sav\\debug.sav"));
        ISaveEditor saveEditor = loadSav(multipleMetalMaxRe.current(), bytes);
        SavePlayerAttribute playerAttribute = saveEditor.getPlayerAttribute(Player.PLAYER_0);
        int level = playerAttribute.getLevel();
        byte[] playerName = saveEditor.getPlayerName(Player.PLAYER_0);
        System.out.println(multipleMetalMaxRe.current().getCharMap().toString(playerName));
        System.out.println("Level: " + level);
        System.out.println("装备: ");
        ITextEditor textEditor = metalMaxRe.getEditorManager().getEditor(ITextEditor.class);
        byte[] equipment = playerAttribute.getEquipment();
        for (int i = 0; i < equipment.length; i++) {
            byte item = equipment[i];
            if (item == 0x00) {
                continue;
            }
            byte equipmentState = playerAttribute.getEquipmentState();
            System.out.println(((equipmentState & (1 << (7 - i))) == 0 ? "未装备 " : "已装备 ") + textEditor.getItemName(item));
        }
        System.out.println("道具: ");
        for (byte item : playerAttribute.getInventory()) {
            if (item == 0x00) {
                continue;
            }
            System.out.println(textEditor.getItemName(item));
        }
    }
}
