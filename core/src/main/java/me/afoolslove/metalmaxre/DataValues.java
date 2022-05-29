package me.afoolslove.metalmaxre;

import org.jetbrains.annotations.Range;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数据值
 * 用于：商品价格、攻击力、防御力等
 *
 * @author AFoolLove
 */
public class DataValues {
    public static final Map<Number, Integer> VALUES = new HashMap<>();

    static {
        // 1byte
        VALUES.put(0x00, 0x000005);
        VALUES.put(0x01, 0x000008);
        VALUES.put(0x02, 0x00000A);
        VALUES.put(0x03, 0x00000C);
        VALUES.put(0x04, 0x000000);
        VALUES.put(0x05, 0x00000F);
        VALUES.put(0x06, 0x000000);
        VALUES.put(0x07, 0x000014);
        VALUES.put(0x08, 0x000018);
        VALUES.put(0x09, 0x000019);
        VALUES.put(0x0A, 0x00001C);
        VALUES.put(0x0B, 0x00001E);
        VALUES.put(0x0C, 0x000023);
        VALUES.put(0x0D, 0x000000);
        VALUES.put(0x0E, 0x000028);
        VALUES.put(0x0F, 0x000030);
        VALUES.put(0x10, 0x000032);
        VALUES.put(0x11, 0x00003C);
        VALUES.put(0x12, 0x000000);
        VALUES.put(0x13, 0x000046);
        VALUES.put(0x14, 0x000050);
        VALUES.put(0x15, 0x000053);
        VALUES.put(0x16, 0x00005A);
        VALUES.put(0x17, 0x000064);
        VALUES.put(0x18, 0x000069);
        VALUES.put(0x19, 0x00006E);
        VALUES.put(0x1A, 0x000078);
        VALUES.put(0x1B, 0x000082);
        VALUES.put(0x1C, 0x00008C);
        VALUES.put(0x1D, 0x000091);
        VALUES.put(0x1E, 0x000096);
        VALUES.put(0x1F, 0x000000);
        VALUES.put(0x20, 0x000000);
        VALUES.put(0x21, 0x0000A0);
        VALUES.put(0x22, 0x0000AA);
        VALUES.put(0x23, 0x0000AF);
        VALUES.put(0x24, 0x0000B4);
        VALUES.put(0x25, 0x000000);
        VALUES.put(0x26, 0x0000BE);
        VALUES.put(0x27, 0x0000C8);
        VALUES.put(0x28, 0x0000D2);
        VALUES.put(0x29, 0x0000DC);
        VALUES.put(0x2A, 0x000000);
        VALUES.put(0x2B, 0x0000E6);
        VALUES.put(0x2C, 0x0000F0);
        VALUES.put(0x2D, 0x000001);
        VALUES.put(0x2E, 0x000016);
        VALUES.put(0x2F, 0x000041);
        VALUES.put(0x30, 0x0000FA);
        VALUES.put(0x31, 0x000000);
        VALUES.put(0x32, 0x000002);
        VALUES.put(0x33, 0x000003);
        VALUES.put(0x34, 0x000007);
        VALUES.put(0x35, 0x000024);
        VALUES.put(0x36, 0x00002D);
        VALUES.put(0x37, 0x000006);
        VALUES.put(0x38, 0x00004B);
        VALUES.put(0x39, 0x000063);
        VALUES.put(0x3A, 0x000010);
        VALUES.put(0x3B, 0x000020);
        VALUES.put(0x3C, 0x00000B);
        VALUES.put(0x3D, 0x00001A);
        VALUES.put(0x3E, 0x00003A);
        VALUES.put(0x3F, 0x000056);
        VALUES.put(0x40, 0x00004E);
        VALUES.put(0x41, 0x00005F);
        VALUES.put(0x42, 0x000073);
        VALUES.put(0x43, 0x000087);
        VALUES.put(0x44, 0x0000CD);
        VALUES.put(0x45, 0x0000F5);
        VALUES.put(0x46, 0x000004);
        // 2byte
        VALUES.put(0x47, 0x000104);
        VALUES.put(0x48, 0x000000);
        VALUES.put(0x49, 0x000000);
        VALUES.put(0x4A, 0x000118);
        VALUES.put(0x4B, 0x000000);
        VALUES.put(0x4C, 0x00012C);
        VALUES.put(0x4D, 0x000136);
        VALUES.put(0x4E, 0x000000);
        VALUES.put(0x4F, 0x000140);
        VALUES.put(0x50, 0x00014A);
        VALUES.put(0x51, 0x000154);
        VALUES.put(0x52, 0x00015E);
        VALUES.put(0x53, 0x000168);
        VALUES.put(0x54, 0x00017C);
        VALUES.put(0x55, 0x000190);
        VALUES.put(0x56, 0x00019A);
        VALUES.put(0x57, 0x0001A4);
        VALUES.put(0x58, 0x0001B8);
        VALUES.put(0x59, 0x0001D6);
        VALUES.put(0x5A, 0x0001E0);
        VALUES.put(0x5B, 0x0001EA);
        VALUES.put(0x5C, 0x0001F4);
        VALUES.put(0x5D, 0x000208);
        VALUES.put(0x5E, 0x00021C);
        VALUES.put(0x5F, 0x000226);
        VALUES.put(0x60, 0x000230);
        VALUES.put(0x61, 0x00023A);
        VALUES.put(0x62, 0x000000);
        VALUES.put(0x63, 0x000258);
        VALUES.put(0x64, 0x00026C);
        VALUES.put(0x65, 0x000000);
        VALUES.put(0x66, 0x00028A);
        VALUES.put(0x67, 0x000294);
        VALUES.put(0x68, 0x0002A8);
        VALUES.put(0x69, 0x0002BC);
        VALUES.put(0x6A, 0x0002C6);
        VALUES.put(0x6B, 0x0002D0);
        VALUES.put(0x6C, 0x0002E4);
        VALUES.put(0x6D, 0x0002F8);
        VALUES.put(0x6E, 0x00030C);
        VALUES.put(0x6F, 0x000320);
        VALUES.put(0x70, 0x00035C);
        VALUES.put(0x71, 0x000000);
        VALUES.put(0x72, 0x000384);
        VALUES.put(0x73, 0x000398);
        VALUES.put(0x74, 0x0003D4);
        VALUES.put(0x75, 0x000127);
        VALUES.put(0x76, 0x000163);
        VALUES.put(0x77, 0x0001C2);
        VALUES.put(0x78, 0x0003E8);
        VALUES.put(0x79, 0x0004B0);
        VALUES.put(0x7A, 0x0004BA);
        VALUES.put(0x7B, 0x000000);
        VALUES.put(0x7C, 0x000640);
        VALUES.put(0x7D, 0x0006F4);
        VALUES.put(0x7E, 0x000708);
        VALUES.put(0x7F, 0x0007BC);
        VALUES.put(0x80, 0x0007D0);
        VALUES.put(0x81, 0x000960);
        VALUES.put(0x82, 0x0009B0);
        VALUES.put(0x83, 0x000A28);
        VALUES.put(0x84, 0x000000);
        VALUES.put(0x85, 0x000AF0);
        VALUES.put(0x86, 0x000BB8);
        VALUES.put(0x87, 0x000C4E);
        VALUES.put(0x88, 0x000C80);
        VALUES.put(0x89, 0x000000);
        VALUES.put(0x8A, 0x000ED8);
        VALUES.put(0x8B, 0x000FA0);
        VALUES.put(0x8C, 0x0011F8);
        VALUES.put(0x8D, 0x00128E);
        VALUES.put(0x8E, 0x000000);
        VALUES.put(0x8F, 0x000000);
        VALUES.put(0x90, 0x001450);
        VALUES.put(0x91, 0x000000);
        VALUES.put(0x92, 0x001770);
        VALUES.put(0x93, 0x000000);
        VALUES.put(0x94, 0x00189C);
        VALUES.put(0x95, 0x001A5E);
        VALUES.put(0x96, 0x001C84);
        VALUES.put(0x97, 0x001F40);
        VALUES.put(0x98, 0x002012);
        VALUES.put(0x99, 0x000433);
        VALUES.put(0x9A, 0x002134);
        VALUES.put(0x9B, 0x00224C);
        VALUES.put(0x9C, 0x0006A4);
        VALUES.put(0x9D, 0x00047E);
        VALUES.put(0x9E, 0x000654);
        VALUES.put(0x9F, 0x000898);
        VALUES.put(0xA0, 0x000582);
        VALUES.put(0xA1, 0x00044C);
        VALUES.put(0xA2, 0x0005DC);
        VALUES.put(0xA3, 0x000BA4);
        VALUES.put(0xA4, 0x000BF4);
        VALUES.put(0xA5, 0x00157C);
        VALUES.put(0xA6, 0x0026AC);
        VALUES.put(0xA7, 0x0008FC);
        VALUES.put(0xA8, 0x000C1C);
        VALUES.put(0xA9, 0x001388);
        VALUES.put(0xAA, 0x001B58);
        VALUES.put(0xAB, 0x001EBE);
        // ？？？
        VALUES.put(0xAC, 0x002710);
        VALUES.put(0xAD, 0x002EE0);
        VALUES.put(0xAE, 0x0030D4);
        VALUES.put(0xAF, 0x003200);
        VALUES.put(0xB0, 0x00C288);
        VALUES.put(0xB1, 0x00413C);
        VALUES.put(0xB2, 0x0045EC);
        VALUES.put(0xB3, 0x004650);
        VALUES.put(0xB4, 0x0055F0);
        VALUES.put(0xB5, 0x000000);
        VALUES.put(0xB6, 0x006D60);
        VALUES.put(0xB7, 0x007D00);
        VALUES.put(0xB8, 0x007E90);
        VALUES.put(0xB9, 0x000000);
        VALUES.put(0xBA, 0x008E94);
        VALUES.put(0xBB, 0x009B78);
        VALUES.put(0xBC, 0x000000);
        VALUES.put(0xBD, 0x000000);
        VALUES.put(0xBE, 0x00BB80);
        VALUES.put(0xBF, 0x00DB88);
        VALUES.put(0xC0, 0x000000);
        VALUES.put(0xC1, 0x00F230);
        VALUES.put(0xC2, 0x00FA00);
        VALUES.put(0xC3, 0x0061A8);
        VALUES.put(0xC4, 0x002968);
        VALUES.put(0xC5, 0x0040D8);
        VALUES.put(0xC6, 0x005FDA);
        VALUES.put(0xC7, 0x0088B8);
        VALUES.put(0xC8, 0x00C350);
        VALUES.put(0xC9, 0x007468);
        VALUES.put(0xCA, 0x002AF8);
        VALUES.put(0xCB, 0x002FA8);
        VALUES.put(0xCC, 0x003C8C);
        VALUES.put(0xCD, 0x005078);
        VALUES.put(0xCE, 0x0059D8);
        VALUES.put(0xCF, 0x00A410);
        // 3byte
        VALUES.put(0xD0, 0x013E5C);
        VALUES.put(0xD1, 0x014C08);
        VALUES.put(0xD2, 0x0157C0);
        VALUES.put(0xD3, 0x0185D8);
        VALUES.put(0xD4, 0x0189C0);
        VALUES.put(0xD5, 0x027100);
        VALUES.put(0xD6, 0x0203A0);
        VALUES.put(0xD7, 0x0222E0);
        VALUES.put(0xD8, 0x009C40);
        VALUES.put(0xD9, 0x00A410);
        VALUES.put(0xDA, 0x00ABE0);
        VALUES.put(0xDB, 0x00C350);
        VALUES.put(0xDC, 0x00D6D8);
        VALUES.put(0xDD, 0x00EA60);
        VALUES.put(0xDE, 0x013880);
        VALUES.put(0xDF, 0x0186A0);
        VALUES.put(0xE0, 0x007918);
        VALUES.put(0xE1, 0x011940);
        VALUES.put(0xE2, 0x00DAC0);
        VALUES.put(0xE3, 0x013880);
        VALUES.put(0xE4, 0x016F30);
        VALUES.put(0xE5, 0x0186A0);
        VALUES.put(0xE6, 0x014884);
        VALUES.put(0xE7, 0x028FDC);
        VALUES.put(0xE8, 0x030D40);

    }

    /**
     * @return 该值的所有key
     */
    public static List<Number> getKeys(@Range(from = 0x00, to = 0xFFFFFF) int value) {
        return VALUES.entrySet().parallelStream()
                .filter(entry -> entry.getValue() == value)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * @return 1Byte的数据
     */
    public static Map<Number, Integer> get1ByteValue() {
        return getFormByteLength(1);
    }

    /**
     * @return 2Byte的数据
     */
    public static Map<Number, Integer> get2ByteValue() {
        return getFormByteLength(2);
    }

    /**
     * @return 3Byte的数据
     */
    public static Map<Number, Integer> get3ByteValue() {
        return getFormByteLength(3);
    }

    private static Map<Number, Integer> getFormByteLength(@Range(from = 0x01, to = 0x03) int length) {
        if (length != 0x01 && length != 0x02) {
            return new HashMap<>(VALUES);
        }
        return VALUES.entrySet().parallelStream()
                .filter(entry -> switch (length) {
                    case 1 -> entry.getValue() <= 0x0000FF;
                    case 2 -> entry.getValue() <= 0x00FFFF;
//                    case 3 -> entry.getValue() <= 0xFFFFFF;
                    default -> true;
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private DataValues() {
    }
}